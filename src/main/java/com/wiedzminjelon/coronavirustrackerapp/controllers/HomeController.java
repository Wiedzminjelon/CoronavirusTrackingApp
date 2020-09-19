package com.wiedzminjelon.coronavirustrackerapp.controllers;

import com.wiedzminjelon.coronavirustrackerapp.models.LocationStats;
import com.wiedzminjelon.coronavirustrackerapp.services.CoronaVirusDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    CoronaVirusDataService coronaVirusDataService;

    public HomeController(CoronaVirusDataService coronaVirusDataService) {
        this.coronaVirusDataService = coronaVirusDataService;
    }

    @GetMapping("/")
    public String home(Model model){
        Integer totalReportedCases = coronaVirusDataService.getAllStats().stream()
                .mapToInt(LocationStats::getLatestTotalCases)
                .sum();
        model.addAttribute("location", coronaVirusDataService.getAllStats());
        model.addAttribute("totalReportedCases",totalReportedCases);

        return "home";
    }


//    public int calculateTotalReportedCases(){
//        List<LocationStats> stats = new ArrayList<>();
//        stats.addAll(coronaVirusDataService.getAllStats());
//        int result = 0;
//        for (LocationStats locationStat:stats){
//            result += locationStat.getLatestTotalCases();
//        }
//        return result;
//    }
}
