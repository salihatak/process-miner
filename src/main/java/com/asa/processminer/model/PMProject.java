package com.asa.processminer.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@RequiredArgsConstructor
@Getter @Setter
public class PMProject extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long projectId;

    public String projectName;

    public String description;

}
