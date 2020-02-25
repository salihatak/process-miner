package com.asa.processminer.model;

import lombok.*;

import javax.persistence.*;

@Builder
@Data
@Entity
@Table(name="pm_project")
@AllArgsConstructor @NoArgsConstructor
public class PMProject extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long projectId;
    public String projectName;
    public String description;
}
