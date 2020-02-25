package com.asa.processminer.service;

import com.asa.processminer.model.PMProject;
import com.asa.processminer.dto.PMProjectDTO;

import java.util.List;
import java.util.Optional;

public interface  PMProjectService {

    public List<PMProject> findAll();

    public PMProject createProject(PMProjectDTO pmProject);

    public PMProject updateProject(long projectId, PMProjectDTO pmProject);
}
