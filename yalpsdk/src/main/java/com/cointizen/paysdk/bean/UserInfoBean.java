package com.cointizen.paysdk.bean;

/**
 * 描述：用户信息
 * 作者：Administrator
 * 时间: 2018-03-10 9:29
 */

public class UserInfoBean {
    private boolean isSavePwd;
    private String account;
    private String pwd;

    public boolean isSavePwd() {
        return isSavePwd;
    }

    public void setSavePwd(boolean savePwd) {
        isSavePwd = savePwd;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public UserInfoBean() {
    }
}
