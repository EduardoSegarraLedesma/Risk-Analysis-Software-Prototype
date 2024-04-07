package com.example.riskanalysissoftwareprototype.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ServerController {

    @GetMapping("/")
    public String showIndexPage() {
        return "index.html";
    }

    @GetMapping("/sizeEstimation")
    public String showSizeEstimationPage() {
        return "SizeEstimationPage.html";
    }

    @GetMapping("/monteCarlo")
    public String showMonteCarloAnalysisPage() {
        return "MonteCarloAnalysisPage.html";
    }
}
