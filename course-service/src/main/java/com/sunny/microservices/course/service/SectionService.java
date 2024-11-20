package com.sunny.microservices.course.service;

import com.sunny.microservices.basedomain.course.dto.DTO.LessonDetail;
import com.sunny.microservices.basedomain.course.dto.DTO.SectionDetail;
import com.sunny.microservices.course.dto.DTO.LessonPreview;
import com.sunny.microservices.course.dto.DTO.SectionPreview;
import com.sunny.microservices.course.dto.request.SectionRequest;
import com.sunny.microservices.course.entity.Course;
import com.sunny.microservices.course.entity.Lesson;
import com.sunny.microservices.course.entity.Section;
import com.sunny.microservices.course.exception.AppException;
import com.sunny.microservices.course.exception.ErrorCode;
import com.sunny.microservices.course.repository.SectionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SectionService {

        SectionRepository sectionRepository;
        MongoTemplate mongoTemplate;
        LessonService lessonService;
        public  String createSection(String courseId, SectionRequest request) {
            try{
                var section = Section.builder()
                        .name(request.getName())
                        .partNumber(request.getPartNumber())
                        .duration(Duration.ofSeconds(0).getSeconds() * 1.0)
                        .totalLesson(0).build();
                section.setLessons(List.of());
                sectionRepository.save(section);

                mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(courseId)),
                        new Update().addToSet("sections", section.getId()),
                        Course.class);

                return "tạo phần học thành công";
            }catch (Exception e) {
                throw new AppException(ErrorCode.CREATE_FAILED);
            }
        }

        public String updateSection(String sectionId, SectionRequest request) {
            var section = sectionRepository.findById(sectionId)
                    .orElseThrow(() -> new AppException(ErrorCode.SECTION_NOT_FOUND));

            section.setName(request.getName());
            section.setPartNumber(request.getPartNumber());

            sectionRepository.save(section);

            return "cập nhật thông tin phần học thành công";
        }

        public String deleteSection(String courseId, String sectionId) {
            try {
                var section = sectionRepository.findById(sectionId)
                        .orElseThrow(() -> new AppException(ErrorCode.SECTION_NOT_FOUND));

                if (section.getLessons().isEmpty()) {
                    mongoTemplate.updateFirst(
                            new Query(Criteria.where("_id").is(courseId)),
                            new Update().pull("sections", new ObjectId(sectionId)),
                            Course.class
                    );
                    sectionRepository.delete(section);
                    return "xoá phần học thành công";
                } else {
                    throw new AppException(ErrorCode.LESSON_NOT_EMPTY);
                }

            }catch (Exception e){
                throw new AppException(ErrorCode.DELETE_FAILED);
            }
        }

    public List<SectionPreview> findSectionsByIds(List<String> sectionIds) {
        List<Section> sections = sectionRepository.findAllById(sectionIds);

        return sections.stream()
                .sorted(Comparator.comparingInt(Section::getPartNumber))
                .map(section -> {
            List<Lesson> lessons = lessonService.findLessonsByIds(section.getLessons());

            List<LessonPreview> lessonPreviews = lessonService.mapToLessonPreviews(lessons);

            return SectionPreview.builder()
                    .name(section.getName())
                    .lessons(lessonPreviews)
                    .duration(section.getDuration())
                    .build();
        }).collect(Collectors.toList());
    }

    public List<SectionDetail> findSectionsDetailByIds(List<String> ids) {
        List<Section> sections = sectionRepository.findAllById(ids);

        return sections.stream()
                .sorted(Comparator.comparingInt(Section::getPartNumber))
                .map(section -> {
            List<Lesson> lessons = lessonService.findLessonsByIds(section.getLessons());
            List<LessonDetail> lessonDetails = lessonService.mapToLessonDetails(lessons);

            return SectionDetail.builder()
                    .name(section.getName())
                    .lessons(lessonDetails)
                    .duration(section.getDuration())
                    .build();
        }).collect(Collectors.toList());
    }
}
