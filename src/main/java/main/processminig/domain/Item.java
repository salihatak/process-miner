package main.processminig.domain;

import java.util.Date;

public class Item {
    private String caseId;
    private String activity;
    private Date date;

    public Item(String caseId, String activity, Date date) {
        this.caseId = caseId;
        this.activity = activity;
        this.date = date;
    }

    public String getCaseId() {
        return caseId;
    }

    public String getActivity() {
        return activity;
    }

    public Date getDate() {
        return date;
    }

    // Optionally, override toString(), equals(), and hashCode() as needed.
} 