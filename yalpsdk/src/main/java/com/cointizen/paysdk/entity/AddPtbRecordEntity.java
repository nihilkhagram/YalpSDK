package com.cointizen.paysdk.entity;

import java.util.List;

public class AddPtbRecordEntity {

	private String account;
	
	private String status;
	
	private String errorMsg;
	
	private List<AddPtbEntity> addPtbList;

	private int total;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<AddPtbEntity> getAddPtbList() {
		return addPtbList;
	}

	public void setAddPtbList(List<AddPtbEntity> addPtbList) {
		this.addPtbList = addPtbList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}
