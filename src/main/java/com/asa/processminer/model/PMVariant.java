package com.asa.processminer.model;

import com.asa.processminer.dto.PMActivityDTO;
import lombok.Data;

import java.util.List;

@Data
public class PMVariant {

    private String variantId;
    private int count;
    private List<PMActivityDTO> activities;
}
