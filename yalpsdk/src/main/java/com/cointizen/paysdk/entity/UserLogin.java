package com.cointizen.paysdk.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserLogin implements Serializable {

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 登陆成功返回字符串
	 */
	private String loginStatus;

	/**
	 * 登陆结果提示信息
	 */
	private String loginMsg;

	/**
	 * 登陆返回用户ID
	 */
	private String accountUserId;
	/**
	 * 登录token
	 */
	private String token;

	/**
	 * 登录时间戳
	 */
	private String timeStamp;

	/**
	 * 官网网址
	 */
	private String webURL;


	/**
	 * 后台是否开启了小号开关
	 */
	private int isOpenSmallAccount;  //0否 1是

	/**
	 * 小号页面官网按钮开关
	 */
	private int site_status;  //1开启   0关闭

	private LoginNotification loginNotification;

	/**
	 * 验证
	 */
	private String extra_param;

	/**
	 * 小号列表
	 */
	private List<SmallAccountEntity> listAccount = new ArrayList<>();
	private String hread_icon;

	public String getNickName() {
		return nickName;
	}

	private String nickName;

	public int getSite_status() {
		return site_status;
	}

	public void setSite_status(int site_status) {
		this.site_status = site_status;
	}

	private boolean isYKLogin;

	public int getIsOpenSmallAccount() {
		return isOpenSmallAccount;
	}

	public void setIsOpenSmallAccount(int isOpenSmallAccount) {
		this.isOpenSmallAccount = isOpenSmallAccount;
	}


	public String getExtra_param() {
		return extra_param;
	}

	public void setExtra_param(String extra_param) {
		this.extra_param = extra_param;
	}

	public boolean isYKLogin() {
		return isYKLogin;
	}

	public void setYKLogin(boolean YKLogin) {
		isYKLogin = YKLogin;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	public String getLoginMsg() {
		return loginMsg;
	}

	public void setLoginMsg(String loginMsg) {
		this.loginMsg = loginMsg;
	}

	public String getAccountUserId() {
		return accountUserId;
	}

	public void setAccountUserId(String accountUserId) {
		this.accountUserId = accountUserId;
	}


	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getWebUrl() {
		return webURL;
	}

	public void setWebURL(String webURL) {
		this.webURL = webURL;
	}

	public List<SmallAccountEntity> getSmallAccountList() {
		return listAccount;
	}

	public void setSmallAccountList(List<SmallAccountEntity> listAccount) {
		this.listAccount = listAccount;
	}

	public LoginNotification getLoginNotification() {
		return this.loginNotification;
	}

	public void setLoginNotification(LoginNotification notification) {
		this.loginNotification = notification;
	}
	public void setUserIcon(String hread) {
		this.hread_icon = hread;
	}

	public String getUserIcon() {
		return hread_icon;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}


    public static class SmallAccountEntity implements Serializable{
		private String small_user_id;
		private String small_nickname;
		private String small_token;
		private String small_account;

		public String getSmallAccount() {
			return small_account;
		}

		public void setSmallAccount(String small_account) {
			this.small_account = small_account;
		}

		public String getSmallUserId() {
			return small_user_id;
		}

		public void setSmallUserId(String user_id) {
			this.small_user_id = user_id;
		}


		public String getSmallNickname() {
			return small_nickname;
		}

		public void setSmallNickname(String user_account) {
			this.small_nickname = user_account;
		}

		public String getSmallToken() {
			return small_token;
		}

		public void setSmallToken(String small_token) {
			this.small_token = small_token;
		}
	}

	public static class LoginNotification {
		String id;
		String subject;
		String content;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}

}
