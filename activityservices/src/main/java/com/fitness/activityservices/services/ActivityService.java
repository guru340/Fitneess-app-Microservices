package com.fitness.activityservices.services;

import com.fitness.activityservices.Repo.ServiceRepo;
import com.fitness.activityservices.dto.ActivityRequest;
import com.fitness.activityservices.model.Activity;
import com.fitness.activityservices.response.ActivityResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {


    private final ServiceRepo serviceRepo;
    private final UserValidationService userValidationService;
    private final KafkaTemplate<String,Activity> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    public  ActivityResponse trackActivity(ActivityRequest request) {

        boolean isValiduser= userValidationService.valiateUser(request.getUserId());
        if(!isValiduser){
            throw new RuntimeException("Invalid User"+ request.getUserId());
        }


        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .starttime(request.getStarttime())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .duration(request.getDuration())
                .calories(request.getCalories())
                .type(request.getType())
                .additionalMetrics(request.getAdditionalMetrics())

                .build();

        Activity savedActivity=serviceRepo.save(activity);
        try{
            kafkaTemplate.send(topicName,savedActivity.getUserId(),savedActivity);
        }catch (Exception e){
            e.printStackTrace();
        }

        return mapTOresponse(savedActivity);
    }

    private ActivityResponse mapTOresponse(Activity activity) {
        ActivityResponse activityResponse=new ActivityResponse();
        activityResponse.setId(activity.getId());
        activityResponse.setUserId(activity.getUserId());
        activityResponse.setType(activity.getType());
        activityResponse.setCalories(activity.getCalories());
        activityResponse.setDuration(activity.getDuration());
        activityResponse.setStarttime(activity.getStarttime());
        activityResponse.setCreatedAt(activity.getCreatedAt());
        activityResponse.setUpdatedAt(activity.getUpdatedAt());

        return activityResponse;

    }

}
