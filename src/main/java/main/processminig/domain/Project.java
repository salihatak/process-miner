package main.processminig.domain;

import java.util.Date;

public class Project {
    private String id;
    private String name;
    private String process;
    private Date creationDate;
    private String createdBy;
    private String updatedBy;
    private String pdata;

    public Project() { }

    public Project(String id, String name, String process, Date creationDate, String createdBy, String updatedBy) {
        this.id = id;
        this.name = name;
        this.process = process;
        this.creationDate = creationDate;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    public String getPdata() {
		return pdata;
	}
	public void setPdata(String pdata) {
		this.pdata = pdata;
	}
} 