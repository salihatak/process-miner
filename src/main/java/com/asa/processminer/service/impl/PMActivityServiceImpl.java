package com.asa.processminer.service.impl;

import com.asa.processminer.dto.PMActivityDTO;
import com.asa.processminer.service.PMActivityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PMActivityServiceImpl implements PMActivityService {

    public List<PMActivityDTO> findAll() {
        return null;
    }
}
