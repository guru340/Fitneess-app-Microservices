package com.fitness.activityservices.controller;

import com.fitness.activityservices.dto.ActivityRequest;
import com.fitness.activityservices.model.ActivityTYPE;
import com.fitness.activityservices.response.ActivityResponse;
import com.fitness.activityservices.services.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
@AllArgsConstructor
public class ActivityController {

    private ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest request,
                                                          @RequestHeader(value = "X-USER-ID", required = false) String userId){
        if (userId != null) {
            request.setUserId(userId);
        }
        return ResponseEntity.ok(activityService.trackActivity(request));
    }
}
