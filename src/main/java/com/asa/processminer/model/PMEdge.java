package com.asa.processminer.model;

import lombok.Data;

@Data
public class PMEdge {

    private String id;
    private int weight;
    private String source;
    private String target;
}
