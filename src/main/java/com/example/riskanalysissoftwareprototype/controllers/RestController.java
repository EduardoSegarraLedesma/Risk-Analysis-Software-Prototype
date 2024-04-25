package com.example.riskanalysissoftwareprototype.controllers;

import com.example.riskanalysissoftwareprototype.MonteCarloItems.WorkSchedule;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    MonteCarloController monteCarloController = MonteCarloController.getInstance();
    PersistenceController persistenceController = PersistenceController.getInstance();

    @PostMapping("/saveSchedule")
    public ResponseEntity<?> saveSchedule(@RequestBody String data) {
        String result = monteCarloController.saveData(data);
        return ResponseEntity.ok(new HashMap<String, Object>() {{
            put("message", result);
        }});
    }

    @GetMapping("/getProjects")
    public ResponseEntity<?> getProjects() {
        try {
            return ResponseEntity.ok(persistenceController.retrieveAllProjectNames());
        } catch (SQLException e) {
            return ResponseEntity.ok(new HashMap<String, Object>() {{
                put("message", "couldn't load projects");
            }});
        }
    }

    @GetMapping("/retrieveSchedule")
    public ResponseEntity<?> retrieveSchedule(@RequestParam String projectName) {
        try {
            WorkSchedule schedule = persistenceController.retrieveProjectSchedule(projectName);
            monteCarloController.setData(schedule);
            return ResponseEntity.ok(schedule.getJSONschedule());
        } catch (SQLException e) {
            return ResponseEntity.ok(new HashMap<String, Object>() {{
                put("message", "couldn't retrieving schedule");
            }});
        }
    }

    @PostMapping("/deleteProject")
    public ResponseEntity<?> deleteProject(@RequestBody String projectName) {
        try {
            persistenceController.deleteProject(projectName);
            return ResponseEntity.ok(new HashMap<String, Object>() {{
                put("message", "success");
            }});
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.ok(new HashMap<String, Object>() {{
                put("message", "Couldn't delete project");
            }});
        }
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
