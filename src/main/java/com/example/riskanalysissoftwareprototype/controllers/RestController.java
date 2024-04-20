package com.example.riskanalysissoftwareprototype.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    MonteCarloController monteCarloController = MonteCarloController.getInstance();

    @PostMapping("/saveSchedule")
    public ResponseEntity<?> saveSchedule(@RequestBody String data) {
        String result = monteCarloController.ParseData(data);
        return ResponseEntity.ok(new HashMap<String, Object>() {{
            put("message", result);
        }});
    }

    @GetMapping("/simulateMonteCarlo")
    public ResponseEntity<?> simulateMonteCarlo() {
        Map<String, Object> response = new HashMap<>();
        response.put("data", monteCarloController.doSimulation());
        response.put("minDuration", monteCarloController.getMinDuration());
        response.put("maxDuration", monteCarloController.getMaxDuration());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/calculateProbabilities")
    public float[][] calculateProbabilities() {
        return monteCarloController.obtainResultsPercentages();
    }

    @GetMapping("/getDeadlineData")
    public ResponseEntity<?> getDeadlineData() {
        Map<String, Object> response = new HashMap<>();
        response.put("risk30", monteCarloController.getDaysAccordingToRisk(30));
        response.put("risk15", monteCarloController.getDaysAccordingToRisk(15));
        return ResponseEntity.ok(response);
    }
}
