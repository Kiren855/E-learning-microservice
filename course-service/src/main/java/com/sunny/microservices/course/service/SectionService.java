package com.sunny.microservices.course.service;

import com.sunny.microservices.basedomain.course.dto.DTO.LessonLearning;
import com.sunny.microservices.basedomain.course.dto.DTO.SectionLearning;
import com.sunny.microservices.course.dto.DTO.LessonDetail;
import com.sunny.microservices.course.dto.DTO.LessonPreview;
import com.sunny.microservices.course.dto.DTO.SectionDetail;
import com.sunny.microservices.course.dto.DTO.SectionPreview;
import com.sunny.microservices.course.dto.request.SectionRequest;
import com.sunny.microservices.course.dto.response.IdResponse;
import com.sunny.microservices.course.entity.Course;
import com.sunny.microservices.course.entity.Lesson;
import com.sunny.microservices.course.entity.Section;
import com.sunny.microservices.course.exception.AppException;
import com.sunny.microservices.course.exception.ErrorCode;
import com.sunny.microservices.course.repository.CourseRepository;
import com.sunny.microservices.course.repository.SectionRepository;
import com.sunny.microservices.course.service.lesson.LessonService;
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
import java.util.ArrayList;
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
        CourseRepository courseRepository;
        public IdResponse createSection(String courseId, SectionRequest request) {
            try{
                var section = Section.builder()
                        .name(request.getName())
                        .partNumber(request.getPartNumber())
                        .duration(Duration.ofSeconds(0).getSeconds() * 1.0)
                        .totalLesson(0).build();
                section.setLessons(new ArrayList<>());
                sectionRepository.save(section);

                mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(courseId)),
                        new Update().addToSet("sections", section.getId()),
                        Course.class);

                return IdResponse.builder().Id(section.getId()).build();
            }catch (Exception e) {
                throw new AppException(ErrorCode.CREATE_FAILED);
            }
        }

        public String updateSection(String sectionId, SectionRequest request) {
            var section = sectionRepository.findById(sectionId)
                    .orElseThrow(() -> new AppException(ErrorCode.SECTION_NOT_FOUND));

            if(request.getName() != null)
                section.setName(request.getName());

            if(request.getPartNumber() != null)
                section.setPartNumber(request.getPartNumber());

            sectionRepository.save(section);

            return "cập nhật thông tin phần học thành công";
        }

        public String deleteSection(String courseId, String sectionId) {
            try {
                var course = courseRepository.findById(courseId)
                        .orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));

                var section = sectionRepository.findById(sectionId)
                        .orElseThrow(() -> new AppException(ErrorCode.SECTION_NOT_FOUND));

                if (section.getLessons() == null || section.getLessons().isEmpty()) {
                    course.getSections().removeIf(s -> s.equals(sectionId));

                    courseRepository.save(course);
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
                    SectionPreview sectionPreview = SectionPreview.builder()
                            .name(section.getName())
                            .duration(section.getDuration()).build();

                    if(section.getLessons() == null || section.getLessons().isEmpty())
                        sectionPreview.setLessons(new ArrayList<>());
                    else {
                        List<Lesson> lessons = lessonService.findLessonsByIds(section.getLessons());
                        List<LessonPreview> lessonLearnings = lessonService.mapToLessonPreviews(lessons);
                        sectionPreview.setLessons(lessonLearnings);
                    }
                    return sectionPreview;
                }).collect(Collectors.toList());
    }

    public List<SectionLearning> findSectionLearningByIds(List<String> ids) {
        List<Section> sections = sectionRepository.findAllById(ids);

        return sections.stream()
                .sorted(Comparator.comparingInt(Section::getPartNumber))
                .map(section -> {
            SectionLearning sectionLearning = SectionLearning.builder()
                    .name(section.getName())
                    .duration(section.getDuration()).build();

            if(section.getLessons() == null || section.getLessons().isEmpty())
                sectionLearning.setLessons(new ArrayList<>());
            else {
                List<Lesson> lessons = lessonService.findLessonsByIds(section.getLessons());
                List<LessonLearning> lessonLearnings = lessonService.mapToLessonLearnings(lessons);
                sectionLearning.setLessons(lessonLearnings);
            }
            return sectionLearning;
        }).collect(Collectors.toList());
    }

    public List<SectionDetail> findSectionDetailByIds(List<String> ids) {
        List<Section> sections = sectionRepository.findAllById(ids);

        return sections.stream()
                .sorted(Comparator.comparingInt(Section::getPartNumber))
                .map(section -> {

                    SectionDetail sectionDetail = SectionDetail.builder()
                            .id(section.getId())
                            .name(section.getName())
                            .partNumber(section.getPartNumber())
                            .duration(section.getDuration()).build();

                    if(section.getLessons() == null || section.getLessons().isEmpty())
                        sectionDetail.setLessons(new ArrayList<>());
                    else {
                        List<Lesson> lessons = lessonService.findLessonsByIds(section.getLessons());
                        List<LessonDetail> lessonDetails = lessonService.mapToLessonDetails(lessons);
                        sectionDetail.setLessons(lessonDetails);
                    }
                    return sectionDetail;
                }).collect(Collectors.toList());
    }
}
