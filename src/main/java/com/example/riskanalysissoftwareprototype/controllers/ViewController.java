package com.example.riskanalysissoftwareprototype.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

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
}
