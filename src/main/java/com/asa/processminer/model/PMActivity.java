package com.asa.processminer.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name="pm_activity")
public class PMActivity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long activityId;

    private String caseKey;
    private String activityName;
    private Date eventTime;
    private String userName;
    private String userType;
    private int sortKey;
}
