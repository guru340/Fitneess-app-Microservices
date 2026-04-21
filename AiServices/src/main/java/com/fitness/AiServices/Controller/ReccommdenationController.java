package com.fitness.AiServices.Controller;

import com.fitness.AiServices.Service.RecommandationService;
import com.fitness.AiServices.model.Recommdentation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommdenations")
public class ReccommdenationController {

    private final RecommandationService recommandationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommdentation>>getUserRecommdation(@PathVariable String userId){
        return ResponseEntity.ok(recommandationService.getuserrecommdenation(userId));
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Recommdentation>getActivityRecommdation(@PathVariable String activityId){
        return ResponseEntity.ok(recommandationService.getactivityrecommendation(activityId));
    }
}
