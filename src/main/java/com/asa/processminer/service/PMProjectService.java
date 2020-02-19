package com.asa.processminer.service;

import com.asa.processminer.model.PMProject;
import com.asa.processminer.repository.PMProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface  PMProjectService {

    public List<PMProject> findAll();

}
