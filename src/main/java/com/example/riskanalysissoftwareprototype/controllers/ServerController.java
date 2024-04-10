package com.example.riskanalysissoftwareprototype.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ServerController {

    MonteCarloController monteCarloControler = MonteCarloController.getInstance();

    @GetMapping("/")
    public String showIndexPage() {
        return "index.html";
    }

    // SOFTWARE SIZE ESTIMATION TOOL
    @GetMapping("/sizeEstimation")
    public String showSizeEstimationPage() {
        return "SizeEstimationPage.html";
    }

    // MONTE CARLO ANALYSIS TOOL

    @GetMapping("/monteCarlo")
    public String showMonteCarloAnalysisPage() {
        return "MonteCarloAnalysisPage.html";
    }

    @PostMapping("/saveSchedule")
    public String saveSchedule(@RequestBody String data) {
        return monteCarloControler.ParseData(data);
    }
}
