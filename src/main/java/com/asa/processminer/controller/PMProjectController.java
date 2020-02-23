package com.asa.processminer.controller;

import com.asa.processminer.model.PMProject;
import com.asa.processminer.dto.PMProjectDTO;
import com.asa.processminer.service.PMProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class PMProjectController {

    final PMProjectService pmProjectService;

    @GetMapping("/project")
    public List<PMProject> index(){
        return pmProjectService.findAll();
    }

    @PostMapping("/project")
    public ResponseEntity<PMProject> create(@RequestBody PMProjectDTO pmProject){
        PMProject project = pmProjectService.createProject(pmProject);
        if(project != null)
            return new ResponseEntity<>(project, HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
    }

    @PutMapping("/project/{projectId}")
    public ResponseEntity<PMProject> update(@PathVariable("projectId") long projectId, @RequestBody PMProjectDTO pmProject){
        PMProject project = pmProjectService.updateProject(projectId, pmProject);
        if(project != null)
            return new ResponseEntity<>(project, HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
    }

}
