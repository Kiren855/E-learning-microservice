package com.sunny.microservices.course.service;

import com.sunny.microservices.course.dto.DTO.SectionPreview;
import com.sunny.microservices.course.dto.DTO.TopicPreview;
import com.sunny.microservices.course.dto.request.TopicRequest;
import com.sunny.microservices.course.dto.response.TopicResponse;
import com.sunny.microservices.course.entity.Topic;
import com.sunny.microservices.course.repository.TopicRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TopicService {
    TopicRepository topicRepository;
    public String createTopic(TopicRequest request) {
        Topic topic = Topic.builder()
                .name(request.getName())
                .description(request.getDescription()).build();

        topicRepository.save(topic);

        return "Tạo chủ đề thành công";
    }

    public List<TopicResponse> getTopics() {
        List<Topic> topics = topicRepository.findAll();

        return topics.stream()
                .map(topic -> TopicResponse.builder()
                        .name(topic.getName())
                        .id(topic.getId())
                        .description(topic.getDescription())
                        .createdAt(topic.getCreatedAt())
                        .updatedAt(topic.getUpdatedAt()).build())
                .collect(Collectors.toList());

    }

    public List<TopicPreview> findTopicsByIds(List<String> topicIds) {
        List<Topic> topics =  topicRepository.findAllById(topicIds);

        return topics.stream()
                .map(topic -> new TopicPreview( topic.getName()))
                .collect(Collectors.toList());
    }
}

