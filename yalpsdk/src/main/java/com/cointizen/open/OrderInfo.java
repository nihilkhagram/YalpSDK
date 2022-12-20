package com.cointizen.open;

public class OrderInfo {

	/**
	 * 商品名称
	 */
	private String productName = "";

	/**
	 * 商品价格,以分为单位
	 */
	private int productPrice = 0;

	/**
	 * 商品描述
	 */
	private String productDesc = "";

	/**
	 * 游戏中的交易订单号，回传给游戏，用于确定充值玩家
	 */
	private String extendInfo = "";

	/**
	 * 游戏服务器编号
	 */
	private String gameServerId = "";

	/**
	 * 区服名称
	 */
	private String serverName = "";

	/**
	 * 角色名称
	 */
	private String roleName = "";

	/**
	 * 角色ID
	 */
	private String roleId = "";

	/**
	 *角色等级
	 */
	private String roleLevel = "";

	/**
	 * 预留参数
	 */
	private String extra_param = "";

	/**
	 * 预留参数
	 */
	private String goodsReserve = "";

	public String getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(String roleLevel) {
		this.roleLevel = roleLevel;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getExtra_param() {
		return extra_param;
	}

	public void setExtra_param(String extra_param) {
		this.extra_param = extra_param;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getGameServerId() {
		return gameServerId;
	}

	public void setGameServerId(String gameServerId) {
		this.gameServerId = gameServerId;
	}

	private int getAmount() {
		return productPrice;
	}

	public void setAmount(int productPrice) {
		this.productPrice = productPrice;
	}
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String name) {
		productName = name;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String desc) {
		productDesc = desc;
	}

	public String getExtendInfo() {
		return extendInfo;
	}

	public void setExtendInfo(String extend) {
		extendInfo = extend;
	}

	public String getGoodsReserve() {
		return goodsReserve;
	}

	public void setGoodsReserve(String goodsReserve) {
		this.goodsReserve = goodsReserve;
	}

	/**
	 * 商品价格Float格式
	 */
	public String getGoodsPriceYuan() {
		return String.format("%.2f", getFloatGoodsPriceYuan());
	}

	public String getGoodsPriceYuanByZK(int zknum) {
		return String.format("%.2f", getFloatGoodsPriceYuan() * zknum * 0.1f);
	}

	/**
	 * 商品价格int格式
	 */
	public float getFloatGoodsPriceYuan() {
		return (float)getAmount() / 100;
	}

	public String getGoodsPriceFen() {
		return getAmount() + "";
	}

	@Override
	public String toString() {
		return "OrderInfo{" +
				"productName='" + productName + '\'' +
				", productPrice=" + productPrice +
				", productDesc='" + productDesc + '\'' +
				", extendInfo='" + extendInfo + '\'' +
				", gameServerId='" + gameServerId + '\'' +
				", serverName='" + serverName + '\'' +
				", roleName='" + roleName + '\'' +
				", roleId='" + roleId + '\'' +
				", roleLevel='" + roleLevel + '\'' +
				", extra_param='" + extra_param + '\'' +
				'}';
	}

	public String getPayerWalletAddress() {
		return null;
	}

	// todo
	public String getProductId() {
		return null;
	}

	public String getGameId() {
		return null;
	}
}
