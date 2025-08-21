package com.sunny.microservices.course.service.lesson;

import com.sunny.microservices.basedomain.event.LessonCreatedEvent;
import com.sunny.microservices.course.client.AzureFileStorageClient;
import com.sunny.microservices.course.client.UserClient;
import com.sunny.microservices.course.dto.response.ProfileResponse;
import com.sunny.microservices.course.entity.Doc;
import com.sunny.microservices.course.entity.Lesson;
import com.sunny.microservices.course.entity.Section;
import com.sunny.microservices.course.entity.Video;
import com.sunny.microservices.course.exception.AppException;
import com.sunny.microservices.course.exception.ErrorCode;
import com.sunny.microservices.course.kafka.LessonProducer;
import com.sunny.microservices.course.repository.DocRepository;
import com.sunny.microservices.course.repository.LessonRepository;
import com.sunny.microservices.course.repository.SectionRepository;
import com.sunny.microservices.course.repository.VideoRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonProcessingService {
    VideoRepository videoRepository;
    LessonRepository lessonRepository;
    SectionRepository sectionRepository;
    AzureFileStorageClient azureFileStorageClient;
    UserClient userClient;
    LessonProducer lessonProducer;
    DocRepository docRepository;
    @Value("${azure.blob.video-container-name}")
    @NonFinal
    String videoContainer;

    @Value("${azure.blob.doc-container-name}")
    @NonFinal
    String docContainer;

    @Value("${azure.blob.thumbnail-container-name}")
    @NonFinal
    String thumbnailContainer;

    final String MASTER_PLAYLIST = "master.m3u8";
    final String OUTPUT_PATTERN = "variant_%v.m3u8";
    @Async("threadPoolTaskExecutor")
    public void processCreateVideoLesson(String userId, Section section, Lesson lesson,
                                         Double duration,
                                         String videoName, byte[] videoFile,
                                         String thumbnailName, byte[] thumbnailFile) {

        ProfileResponse user = userClient.getProfile(userId);

        String pathThumbnail = azureFileStorageClient.uploadFile(thumbnailContainer,
                thumbnailName,
                new ByteArrayInputStream(thumbnailFile),
                thumbnailFile.length);

        try {
            String masterUrl = uploadVideo(lesson, videoName, videoFile);

            Video video = Video.builder()
                    .videoUrl(masterUrl)
                    .duration(duration)
                    .thumbnailUrl(pathThumbnail)
                    .build();

            videoRepository.save(video);
            lesson.setType_id(video.getId());
            lessonRepository.save(lesson);

            Double newDuraton = section.getDuration() + duration;

            section.getLessons().add(lesson.getId());
            section.setTotalLesson(section.getTotalLesson() + 1);
            section.setDuration(newDuraton);

            sectionRepository.save(section);

            log.info("success create video lesson");
            LessonCreatedEvent event = new LessonCreatedEvent(lesson.getName(), user.getUsername(), user.getEmail());

            lessonProducer.sendMessage(event);

        }catch (IOException | InterruptedException e) {
            throw new AppException(ErrorCode.FILE_CANNOT_UPLOAD);
        }
    }

    @Async("threadPoolTaskExecutor")
    public void processUpdateVideo(Section section,Lesson lesson, Video video, String videoName, byte[] videoFile, Double duration) {
        String blobFolder = "lesson_" + lesson.getId();
        azureFileStorageClient.deleteDirectory(videoContainer, blobFolder);

        try {
            String masterUrl = uploadVideo(lesson, videoName, videoFile);
            Double oldTotalDuration = section.getDuration();
            Double currTotalDuration = oldTotalDuration - video.getDuration();
            Double newTotalDuration = currTotalDuration + duration;

            section.setDuration(newTotalDuration);
            sectionRepository.save(section);

            video.setVideoUrl(masterUrl);
            video.setDuration(duration);
            videoRepository.save(video);

        } catch (IOException | InterruptedException e) {
            throw new AppException(ErrorCode.FILE_CANNOT_UPLOAD);
        }
    }

    @Async("threadPoolTaskExecutor")
    public void processUpdateThumbnail(Video video, String thumbnailName, byte[] thumbnailFile) {
        String thumbnailUrl = extractFileName(video.getThumbnailUrl());
        azureFileStorageClient.deleteFile(thumbnailContainer, thumbnailUrl);

        String pathThumbnail = azureFileStorageClient.uploadFile(thumbnailContainer,
                thumbnailName,
                new ByteArrayInputStream(thumbnailFile),
                thumbnailFile.length);

        video.setThumbnailUrl(pathThumbnail);
        videoRepository.save(video);
    }

    @Async("threadPoolTaskExecutor")
    public void processCreateDocLesson(String userId, String sectionId,
                                       String lessonName, Integer partNumber,
                                       String docName, byte[] docFile) {

        ProfileResponse user = userClient.getProfile(userId);
        Section section = sectionRepository.findById(sectionId).orElseThrow(
                () -> new AppException(ErrorCode.SECTION_NOT_FOUND)
        );

        Lesson lesson = Lesson.builder()
                .name(lessonName)
                .type("DOC")
                .partNumber(partNumber).build();

        String pathFile = azureFileStorageClient.uploadFile(docContainer, docName,
                new ByteArrayInputStream(docFile), docFile.length);

        Doc doc = Doc.builder()
                .fileUrl(pathFile).build();
        docRepository.save(doc);
        lesson.setType_id(doc.getId());
        lessonRepository.save(lesson);
        section.getLessons().add(lesson.getId());
        section.setTotalLesson(section.getTotalLesson() + 1);
        sectionRepository.save(section);

        log.info("success create doc lesson");
        LessonCreatedEvent event = new LessonCreatedEvent(lesson.getName(), user.getUsername(), user.getEmail());

        lessonProducer.sendMessage(event);
    }

    private String extractFileName(String blobUrl) {
        return blobUrl.substring(blobUrl.lastIndexOf("/") + 1);
    }

    private void runFfmpegHls(Path inputVideo, Path outputDir) throws IOException, InterruptedException {
        List<String> cmd = new ArrayList<>();
        cmd.add("ffmpeg");
        cmd.add("-i");
        cmd.add(inputVideo.toAbsolutePath().toString());

        // Map 3 video variant
        cmd.add("-map"); cmd.add("0:v"); cmd.add("-map"); cmd.add("0:a");
        cmd.add("-map"); cmd.add("0:v"); cmd.add("-map"); cmd.add("0:a");
        cmd.add("-map"); cmd.add("0:v"); cmd.add("-map"); cmd.add("0:a");

        // Scale & bitrate
        cmd.add("-filter:v:0"); cmd.add("scale=w=640:h=360");
        cmd.add("-c:v:0"); cmd.add("libx264"); cmd.add("-profile:v:0"); cmd.add("baseline"); cmd.add("-level:v:0"); cmd.add("3.0"); cmd.add("-b:v:0"); cmd.add("800k");

        cmd.add("-filter:v:1"); cmd.add("scale=w=842:h=480");
        cmd.add("-c:v:1"); cmd.add("libx264"); cmd.add("-profile:v:1"); cmd.add("main"); cmd.add("-level:v:1"); cmd.add("3.1"); cmd.add("-b:v:1"); cmd.add("1400k");

        cmd.add("-filter:v:2"); cmd.add("scale=w=1280:h=720");
        cmd.add("-c:v:2"); cmd.add("libx264"); cmd.add("-profile:v:2"); cmd.add("high"); cmd.add("-level:v:2"); cmd.add("4.0"); cmd.add("-b:v:2"); cmd.add("2800k");

        // Audio
        cmd.add("-c:a"); cmd.add("aac"); cmd.add("-ar"); cmd.add("48000"); cmd.add("-b:a"); cmd.add("128k");

        cmd.add("-f"); cmd.add("hls");
        cmd.add("-hls_time"); cmd.add("10");
        cmd.add("-hls_list_size"); cmd.add("0");
        cmd.add("-master_pl_name"); cmd.add(MASTER_PLAYLIST);
        cmd.add("-var_stream_map"); cmd.add("v:0,a:0 v:1,a:1 v:2,a:2");

        cmd.add(outputDir.resolve(OUTPUT_PATTERN).toAbsolutePath().toString());

        ProcessBuilder pb = new ProcessBuilder(cmd);
        Process process = pb.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("FFmpeg exited with code " + exitCode);
        }
    }

    private String uploadVideo(Lesson lesson, String videoName, byte[] videoFile) throws IOException, InterruptedException {
        Path tempDir;
        tempDir = Files.createTempDirectory("hls_" + UUID.randomUUID());
        Path videoPath = tempDir.resolve(videoName);
        Files.write(videoPath, videoFile);

        Path outputDir = tempDir.resolve("output");
        Files.createDirectories(outputDir);

        runFfmpegHls(videoPath, outputDir);

        String blobFolder = "lesson_" + lesson.getId();

        String url = azureFileStorageClient.uploadDirectory(videoContainer,
                    outputDir.toAbsolutePath().toString(),
                    blobFolder);

        FileUtils.deleteDirectory(tempDir.toFile());

        return url;
    }
}
