package com.asa.processminer.controller;

import com.asa.processminer.dto.PMActivityDTO;
import com.asa.processminer.service.PMActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/activity")
@RequiredArgsConstructor
public class PMActivityController {

    final PMActivityService activityService;

    @GetMapping("/findAll")
    public List<PMActivityDTO> findAllActivities(){
        return activityService.findAll();
    }

}
