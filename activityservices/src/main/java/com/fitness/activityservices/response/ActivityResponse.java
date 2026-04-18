package com.fitness.activityservices.response;

import com.fitness.activityservices.model.ActivityTYPE;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityResponse {
    private String Id;
    private String userId;
    private ActivityTYPE type;
    private Integer duration ;
    private Integer calories;
    private LocalDateTime starttime;
    private Map<String, Object> additionalMetrics;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
