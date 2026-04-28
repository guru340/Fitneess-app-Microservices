package com.fitness.AiServices.Service;

import com.fitness.AiServices.Repo.AIRepository;
import com.fitness.AiServices.model.Activity;
import com.fitness.AiServices.model.Recommdentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class ActvityaiServices {

    private final GroqServices groqServices;
    private final AIRepository aiRepository;

    public Recommdentation generateRecommendation(Activity activity){
        String prompt=createPromptForActivity(activity);
        String AIresponse=groqServices.getRecommendations(prompt);
        log.info("RESPONSE FROM AI {}",AIresponse);
        Recommdentation recommdentation = processAIResponse(activity,AIresponse);
        return aiRepository.save(recommdentation);
    }

    private Recommdentation processAIResponse(Activity activity, String aIresponse) {
        try {
            // Reading For Json File
            ObjectMapper mapper = new ObjectMapper();

            JsonNode rootNode = mapper.readTree(aIresponse);

            String content = rootNode
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText()
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

//            log.info("AI RESPONSE CLEANED",aIresponse);
            JsonNode analysisJson=mapper.readTree(content);
            JsonNode analysisNode=analysisJson.path("analysis");
            StringBuilder fullanalysis=new StringBuilder();
            addAnalysis(fullanalysis,analysisNode,"overall","Overall:");
            addAnalysis(fullanalysis,analysisNode,"pace","Pace:");
            addAnalysis(fullanalysis,analysisNode,"heartRate","Heart Rate:");
            addAnalysis(fullanalysis,analysisNode,"caloriesBurned","Calories:");

            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
            List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));
            List<String> safety = extractSafetyGuidelines(analysisJson.path("safety"));

            return Recommdentation.builder()
                    .acitivityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .recommendation(fullanalysis.toString().trim())
                    .improvements(improvements)
                    .suggestion(suggestions)
                    .safety(safety)
                    .createdAt(LocalDateTime.now())
                    .build();

        }catch (Exception e){
            e.printStackTrace();
            return createDefaultRecommendation(activity);
            }

    }

    private Recommdentation createDefaultRecommendation(Activity activity) {
        return Recommdentation.builder()
                .acitivityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(activity.getType())
                .recommendation("Unable to generate detailed analysis")
                .improvements(Collections.singletonList("Continue with your current routine"))
                .suggestion(Collections.singletonList("Consider consulting a fitness professional"))
                .safety(Arrays.asList(
                        "Always warm up before exercise",
                        "Stay hydrated",
                        "Listen to your body"
                ))
                .createdAt(LocalDateTime.now())
                .build();
    }



    private List<String> extractSafetyGuidelines(JsonNode safetyNode) {
        List<String> safety = new ArrayList<>();
        if (safetyNode.isArray()) {
            safetyNode.forEach(item -> safety.add(item.asText()));
        }
        return safety.isEmpty() ?
                Collections.singletonList("Follow general safety guidelines") :
                safety;
    }


    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> suggestions = new ArrayList<>();
        if (suggestionsNode.isArray()) {
            suggestionsNode.forEach(suggestion -> {
                String workout = suggestion.path("workout").asText();
                String description = suggestion.path("description").asText();
                suggestions.add(String.format("%s: %s", workout, description));
            });
        }
        return suggestions.isEmpty() ?
                Collections.singletonList("No specific suggestions provided") :
                suggestions;
    }


    private List<String> extractImprovements(JsonNode improvementsNode) {
        List<String> improvements = new ArrayList<>();
        if (improvementsNode.isArray()) {
            improvementsNode.forEach(improvement -> {
                String area = improvement.path("area").asText();
                String detail = improvement.path("recommendation").asText();
                improvements.add(String.format("%s: %s", area, detail));
            });
        }
        return improvements.isEmpty() ?
                Collections.singletonList("No specific improvements provided") :
                improvements;
    }

    private void addAnalysis(StringBuilder fullanalysis, JsonNode analysisNode, String key
            , String prefix) {
        if(!analysisNode.path(key).isMissingNode()){
            fullanalysis.append(prefix)
                    .append(prefix)
                    .append(analysisNode.path(key).asText())
                    .append("\n\n");
        }

    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
        Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
        {
          "analysis": {
            "overall": "Overall analysis here",
            "pace": "Pace analysis here",
            "heartRate": "Heart rate analysis here",
            "caloriesBurned": "Calories analysis here"
          },
          "improvements": [
            {
              "area": "Area name",
              "recommendation": "Detailed recommendation"
            }
          ],
          "suggestions": [
            {
              "workout": "Workout name",
              "description": "Detailed workout description"
            }
          ],
          "safety": [
            "Safety point 1",
            "Safety point 2"
          ]
        }

        Analyze this activity:
        Activity Type: %s
        Duration: %d minutes
        Calories Burned: %d
        Additional Metrics: %s
        
        Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
        Ensure the response follows the EXACT JSON format shown above.
        """,
                activity.getType(),
                activity.getDuration(),
                activity.getCalories(),
                activity.getAdditionalMetrics()
        );
    }
}

