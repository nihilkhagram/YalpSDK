package com.cointizen.paysdk.entity;

public class GamePackInfo {

	/**
	 * 索引
	 */
	private String id;

	/**
	 * 游戏索引
	 */
	private String gameId;

	/**
	 * 游戏名称
	 */
	private String gameName;

	/**
	 * 用户ID
	 */
	private String useId;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 礼包ID
	 */
	private String giftId;

	/**
	 * 礼包图片地址
	 */
	private String packImageUrl;

	/**
	 * 礼包名称
	 */
	private String packName;

	/**
	 * 礼包有效期剩余天数
	 */
	private String effectiveDates;

	/**
	 * 礼包介绍
	 */
	private String packDesc;

	/**
	 * 礼包码
	 */
	private String packCode;

	public int getSurplus() {
		return surplus;
	}

	public void setSurplus(int surplus) {
		this.surplus = surplus;
	}

	/**
	 * 礼包数量
	 */
	private int surplus;

	/**
	 * 礼包状态 0 未领取 1已领取
	 */
	private String packStatus;

	/**
	 * VIP礼包领取等级条件
	 */
	private int vipLimit;

	private String startTime;
	private String endTime;

	public int getVipLimit() {
		return vipLimit;
	}

	public void setVipLimit(int vipLimit) {
		this.vipLimit = vipLimit;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public GamePackInfo() {
		packImageUrl = "";
		packName = "";
		effectiveDates = "";
	}

	public String getPackImageUrl() {
		return packImageUrl;
	}

	public void setPackImageUrl(String packImageUrl) {
		this.packImageUrl = packImageUrl;
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public String getEffectiveDates() {
		return effectiveDates;
	}

	public void setEffectiveDates(String effectiveDates) {
		this.effectiveDates = effectiveDates;
	}

	public String getPackDesc() {
		return packDesc;
	}

	public void setPackDesc(String packDesc) {
		this.packDesc = packDesc;
	}

	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getUseId() {
		return useId;
	}

	public void setUseId(String useId) {
		this.useId = useId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

	public String getPackStatus() {
		return packStatus;
	}

	public void setPackStatus(String packStatus) {
		this.packStatus = packStatus;
	}

	@Override
	public String toString() {
		return "GamePackInfo [id=" + id + ", gameId=" + gameId + ", gameName=" + gameName + ", useId=" + useId
				+ ", createTime=" + createTime + ", giftId=" + giftId + ", packImageUrl=" + packImageUrl + ", packName="
				+ packName + ", effectiveDates=" + effectiveDates + ", packDesc=" + packDesc + ", packCode=" + packCode
				+ ", packStatus=" + packStatus + "]";
	}

}
