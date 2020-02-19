package com.asa.processminer.controller;

import com.asa.processminer.model.PMProject;
import com.asa.processminer.service.PMProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/project")
@RequiredArgsConstructor
public class PMProjectController {

    final PMProjectService pmProjectService;

    @GetMapping("/findAll")
    public List<PMProject> findAllProjects(){
        return pmProjectService.findAll();
    }

}
