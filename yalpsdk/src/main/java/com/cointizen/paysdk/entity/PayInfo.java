package com.cointizen.paysdk.entity;

public class PayInfo {

	private String goodsName;
	private String goodsPrice;
	private String payMd5Key;
	
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public String getPayMd5Key() {
		return payMd5Key;
	}
	public void setPayMd5Key(String payMd5Key) {
		this.payMd5Key = payMd5Key;
	}
	@Override
	public String toString() {
		return "PayInfo [goodsName=" + goodsName + ", goodsPrice=" + goodsPrice + ", payMd5Key=" + payMd5Key + "]";
	}
	
	
	
}
