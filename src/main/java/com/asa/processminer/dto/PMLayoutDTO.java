package com.asa.processminer.dto;

import com.asa.processminer.model.PMEdge;
import com.asa.processminer.model.PMVariant;
import lombok.Data;

import java.util.List;

@Data
public class PMLayoutDTO {

    private List<PMActivityDTO> activities;
    private List<PMEdge> edges;
    private List<PMVariant> variants;
    private List<PMVariant> selectedVariants;
}
