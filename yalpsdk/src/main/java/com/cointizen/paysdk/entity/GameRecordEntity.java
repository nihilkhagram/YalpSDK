package com.cointizen.paysdk.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 游戏充值记录
 * 
 * @author Administrator
 *
 */
public class GameRecordEntity {
	private static GameRecordEntity gre = null;

	public static GameRecordEntity getInstance() {
		if (null == gre) {
			gre = new GameRecordEntity();
		}
		return gre;
	}

	private static final String TAG = "GameRecordEntity";
	private List<GameRecordEntity> gameRecordes = new ArrayList<GameRecordEntity>();
	//充值时间
	private String payTime;
	//充值多少
	private String payMoney;
	//充值物品
	private String payName;
	private String payStatus;
	//充值类型
	private String payType;
	private String payTradNo;

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayTradNo() {
		return payTradNo;
	}

	public void setPayTradNo(String payTradNo) {
		this.payTradNo = payTradNo;
	}

	public List<GameRecordEntity> getGameRecordes() {
		return gameRecordes;
	}

	public void setGameRecordes(List<GameRecordEntity> gameRecordes) {
		this.gameRecordes = gameRecordes;
	}

	@Override
	public String toString() {
		return "GameRecordEntity [gameRecordes=" + gameRecordes + ", payTime="
				+ payTime + ", payMoney=" + payMoney + ", payName=" + payName
				+ ", payStatus=" + payStatus + ", payType=" + payType
				+ ", payTradNo=" + payTradNo + "]";
	}
}
