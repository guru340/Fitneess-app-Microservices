package com.fitness.AiServices.Service;

import com.fitness.AiServices.model.Activity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ActivityMessageListener {

    @KafkaListener(topics = "${kafka.topic.name}",groupId = "activity-processor-group")
    public void processActivity(Activity activity){
        log.info("Received Activity for Processing :{}",activity.getUserId());
    }
}
