package com.cointizen.paysdk.entity;

import java.util.List;

public class PacksInfo {

	private String status;
	
	private List<GamePackInfo> packInfoList;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<GamePackInfo> getPackInfoList() {
		return packInfoList;
	}

	public void setPackInfoList(List<GamePackInfo> packInfoList) {
		this.packInfoList = packInfoList;
	}
	
	
}
