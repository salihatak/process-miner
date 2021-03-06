package com.asa.processminer.service.impl;

import com.asa.processminer.exception.ResourceNotFoundException;
import com.asa.processminer.model.PMProject;
import com.asa.processminer.dto.PMProjectDTO;
import com.asa.processminer.repository.PMProjectRepository;
import com.asa.processminer.service.PMProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PMProjectServiceImpl implements PMProjectService {

    final PMProjectRepository pmProjectRepository;

    @Transactional(readOnly = true)
    public List<PMProject> findAll() {
        return pmProjectRepository.findAll();
    }

    public PMProject createProject(PMProjectDTO pmProject) {
        PMProject project = PMProject.builder()
                    .projectName(pmProject.getProjectName())
                    .description(pmProject.getDescription())
                    .build();
        return pmProjectRepository.save(project);
    }

    public PMProject updateProject(long projectId, PMProjectDTO pmProject) {
        return pmProjectRepository.findById(projectId).map(p -> {
            p.setProjectName(pmProject.getProjectName());
            p.setDescription(pmProject.getDescription());
            return p;
        }).orElseThrow(()-> new ResourceNotFoundException("Project [projectId=" + projectId + "] can't be found"));
    }
}
