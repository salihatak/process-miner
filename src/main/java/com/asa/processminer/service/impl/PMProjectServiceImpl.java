package com.asa.processminer.service.impl;

import com.asa.processminer.model.PMProject;
import com.asa.processminer.repository.PMProjectRepository;
import com.asa.processminer.service.PMProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PMProjectServiceImpl implements PMProjectService {

    final PMProjectRepository pmProjectRepository;

    @Transactional(readOnly = true)
    public List<PMProject> findAll() {
        return pmProjectRepository.findAll();
    }

}
