package com.fitness.activityservices.controller;

import com.fitness.activityservices.dto.ActivityRequest;
import com.fitness.activityservices.model.ActivityTYPE;
import com.fitness.activityservices.response.ActivityResponse;
import com.fitness.activityservices.services.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getUserActivity(@RequestHeader(value = "X-USER-ID") String userId){

        return ResponseEntity.ok(activityService.getUserActivites(userId));
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> getActivity(@PathVariable String activityId){
        return ResponseEntity.ok(activityService.getActivityById(activityId));
    }
}
