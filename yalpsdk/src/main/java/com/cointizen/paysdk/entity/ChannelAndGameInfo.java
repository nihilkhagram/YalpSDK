package com.cointizen.paysdk.entity;

public class ChannelAndGameInfo {

	/**
	 * 账户名称
	 */
	private String account;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 用户昵称
	 */
	private String nikeName;
	
	/**
	 * 平台币余额
	 */
	private float platformMoney;
	
	/**
	 * 平台币余额
	 */
	private float bindPtbMoney;
	
	/**
	 * 游戏名称
	 */
	private String gameName;
	
	/**
	 * 手机号
	 */
	private String phoneNumber;

	/**
	 * 邮箱
	 */
	private String eMail;
	
	private String userId;

	private String token;
	
	private String id;

	private String is_uc;
	private String age_status;

	private String real_name;

	private String idcard;

	private String head_img;
	private String point;

	private int vip_level;
	private String next_vip;

	private String gold_coin;
	private String third_authentication;

	private String small_account_user_id;  //小号userid
	private String small_account_token;    //小号token

	private int sex; //性别 0男 1女

	/**
	 * 0游客 1帐号 2手机 3微信 4QQ 5百度 6微博 7邮箱
	 */
	private int userRegisteType;

	private int sign_status; //签到入口后台开关  0关 1开
	private int today_signed; //当前用户今天是否已签 0否 1是


	public ChannelAndGameInfo(){
		account = "";
		nikeName = "";
		platformMoney = 0;
		gameName = "";
		phoneNumber="";
		userId = "";
		token = "";
		head_img = "";
		vip_level = 0;
		point = "";
		gold_coin = "";
		third_authentication = "";
		userRegisteType = -1;
		small_account_user_id = "";
		small_account_token = "";
		next_vip = "";
		sex = 0;
		sign_status = 0;
		today_signed = 0;
	}

	public int getSign_status() {
		return sign_status;
	}

	public void setSign_status(int sign_status) {
		this.sign_status = sign_status;
	}

	public int getToday_signed() {
		return today_signed;
	}

	public void setToday_signed(int today_signed) {
		this.today_signed = today_signed;
	}

	public String getNext_vip() {
		return next_vip;
	}

	public void setNext_vip(String next_vip) {
		this.next_vip = next_vip;
	}

	public int getVip_level() {
		return vip_level;
	}

	public void setVip_level(int vip_level) {
		this.vip_level = vip_level;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getGold_coin() {
		return gold_coin;
	}

	public void setGold_coin(String gold_coin) {
		this.gold_coin = gold_coin;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getThird_authentication() {
		return third_authentication;
	}

	public void setThird_authentication(String third_authentication) {
		this.third_authentication = third_authentication;
	}

	public String getHead_img() {
		return head_img;
	}

	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}
	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getAge_status() {
		return age_status;
	}

	public void setAge_status(String age_status) {
		this.age_status = age_status;
	}

	public String getIs_uc() {
		return is_uc;
	}

	public void setIs_uc(String is_uc) {
		this.is_uc = is_uc;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getNikeName() {
		return nikeName;
	}

	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}

	public float getPlatformMoney() {
		return platformMoney;
	}

	public void setPlatformMoney(float platformMoney) {
		this.platformMoney = platformMoney;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public float getBindPtbMoney() {
		return bindPtbMoney;
	}

	public void setBindPtbMoney(float bindPtbMoney) {
		this.bindPtbMoney = bindPtbMoney;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getUserRegisteType() {
		return userRegisteType;
	}

	public void setUserRegisteType(int userRegisteType) {
		this.userRegisteType = userRegisteType;
	}
	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}


	public void setSmallAccountUserId(String smallUserId){
		this.small_account_user_id = smallUserId;
	}

	public String getSmallAccountUserId(){
		return small_account_user_id;
	}

	public void setSmallAccountToken(String smallUserToken){
		this.small_account_token= smallUserToken;
	}

	public String getSmallAccountToken(){
		return small_account_token;
	}


	public void setClearAll(){
		account = "";
		nikeName = "";
		platformMoney = 0;
		gameName = "";
		phoneNumber="";
		userId = "";
		token = "";
		userRegisteType = -1;
		small_account_user_id = "";
		small_account_token = "";
	}

	@Override
	public String toString() {
		return "ChannelAndGameInfo{" +
				"account='" + account + '\'' +
				", password='" + password + '\'' +
				", nikeName='" + nikeName + '\'' +
				", platformMoney=" + platformMoney +
				", bindPtbMoney=" + bindPtbMoney +
				", gameName='" + gameName + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", userId='" + userId + '\'' +
				", token='" + token + '\'' +
				", id='" + id + '\'' +
				", userRegisteType=" + userRegisteType +
				", eMail=" + eMail +
				'}';
	}
}
