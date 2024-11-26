package com.sunny.microservices.course.service;

import com.sunny.microservices.course.dto.DTO.TopicPreview;
import com.sunny.microservices.course.dto.response.TopicResponse;
import com.sunny.microservices.course.entity.MainTopic;
import com.sunny.microservices.course.entity.SubTopic;
import com.sunny.microservices.course.exception.AppException;
import com.sunny.microservices.course.exception.ErrorCode;
import com.sunny.microservices.course.repository.MainTopicRepository;
import com.sunny.microservices.course.repository.SubTopicRepository;
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
    MainTopicRepository mainTopicRepository;
    SubTopicRepository subTopicRepository;

    public List<TopicResponse> getTopics() {
        List<MainTopic> mainTopics = mainTopicRepository.findAll();

        return mainTopics.stream()
                .map(topic -> {
                    List<TopicPreview> subTopics = findTopicsByIds(topic.getSubTopic());
                    return TopicResponse.builder()
                            .id(topic.getId())
                            .name(topic.getName())
                            .subtopics(subTopics).build();
                })
                .collect(Collectors.toList());
    }

    private List<TopicPreview> findTopicsByIds(List<String> topicIds) {
        List<SubTopic> topics =  subTopicRepository.findAllById(topicIds);

        return topics.stream()
                .map(topic -> TopicPreview.builder()
                        .id(topic.getId())
                        .name(topic.getName()).build())
                .collect(Collectors.toList());
    }

    public String getMainTopicById(String mainTopicId) {
        MainTopic mainTopic = mainTopicRepository.findById(mainTopicId)
                .orElseThrow(()-> new AppException(ErrorCode.TOPIC_NOT_FOUND));

        return mainTopic.getName();
    }

    public String getSubTopicById(String subId) {
        SubTopic subTopic = subTopicRepository.findById(subId)
                .orElseThrow(()-> new AppException(ErrorCode.TOPIC_NOT_FOUND));

        return subTopic.getName();
    }
}

