package com.fitness.AiServices.Service;

import com.fitness.AiServices.Repo.AIRepository;
import com.fitness.AiServices.model.Recommdentation;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class RecommandationService {

    private final AIRepository aiRepository;
    public  List<Recommdentation> getuserrecommdenation(String userId) {
        return aiRepository.findByUserId(userId);

    }

    public  Recommdentation getactivityrecommendation(String activityId) {
        return aiRepository.findByAcitivityId(activityId)
                .orElseThrow(()->new RuntimeException("No Recommendation find for this Id"));

    }
}
