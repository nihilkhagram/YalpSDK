package com.cointizen.paysdk.entity;

public class ReturnResult<T> {
	private String return_code;
	private String return_status;
	private String return_msg;
	public String getReturn_status() {
		return return_status;
	}
	public void setReturn_status(String return_status) {
		this.return_status = return_status;
	}
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	public String getReturn_msg() {
		return return_msg;
	}
	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}
	public ReturnResult(String return_code, String return_status,
			String return_msg) {
		super();
		this.return_code = return_code;
		this.return_status = return_status;
		this.return_msg = return_msg;
	}

	public ReturnResult() {
		super();
	}
	@Override
	public String toString() {
		return "ReturnResult [return_code=" + return_code + ", return_status=" + return_status + ", return_msg="
				+ return_msg + "]";
	}
	
	
}
