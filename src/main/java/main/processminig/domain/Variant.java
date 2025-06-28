package main.processminig.domain;

import java.util.List;
import java.io.Serializable;

public class Variant implements Serializable {
	private static final long serialVersionUID = 1L;
	private String variantId;
	private String variantNo;
	private Integer count = 0; 
	private List<String> activities;
	
	public Variant() {
		// TODO Auto-generated constructor stub
	}
	
	public String getVariantId() {
		return variantId;
	}
	public void setVariantId(String variantId) {
		this.variantId = variantId;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public List<String> getActivities() {
		return activities;
	}
	public void setActivities(List<String> activities) {
		this.activities = activities;
	}



	public String getVariantNo() {
		return variantNo;
	}



	public void setVariantNo(String variantNo) {
		this.variantNo = variantNo;
	}
}
