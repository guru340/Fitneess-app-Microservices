package com.fitness.AiServices.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Document(collation = "recommendations")
@Data
@Builder
public class Recommdentation {

    private String id;
    private  String acitivityId;
    private String userId;
    private List<String> improvements;
    private List<String> suggestion;
    private List<String> safety;

    @CreatedDate
    private LocalDateTime createdAt;
}
