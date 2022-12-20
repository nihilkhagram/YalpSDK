package com.cointizen.paysdk.bean;

public class SwitchManager {

    private static SwitchManager switchManager;
    public static SwitchManager getInstance() {
        if (switchManager == null) {
            switchManager = new SwitchManager();
        }
        return switchManager;
    }

    private String accountRegister = "1"; //0 关闭,1 开启
    private String phoneRegister = "1"; //0 关闭,1 开启
    private String emailRegister = "1"; //0 关闭,1 开启
    private String vipLevel = "0"; //0 关闭,1 开启
    private String floatingNetwork = "0"; //0 关闭,1 开启
    private String share = "0"; //0 关闭,1 开启
    private String floating = "1"; //0 关闭,1 开启

    public String getFloatingIconUrl() {
        return floatingIconUrl;
    }

    private String floatingIconUrl = "";
    private String logoutAdvert = "1"; //0 关闭,1 开启
    private String ptbAdd = "1"; //0 关闭,1 开启
    private String changeAccount = "1"; //0 关闭,1 开启

    //至少一个关闭
    public boolean unopenAllRegister() {
        return "0".equals(accountRegister) ||
                "0".equals(phoneRegister) ||
                "0".equals(emailRegister);
    }

    public boolean isCloseRegister() {
        return !isOpenAccountRegister() && !isOpenPhoneRegister() ;
    }

    public boolean isOpenAccountRegister() {
        return "1".equals(accountRegister);
    }

    public boolean isOpenPhoneRegister() {
        return "1".equals(phoneRegister);
    }

    public boolean isOpenEmailRegister() {
        return "1".equals(emailRegister);
    }

    public boolean vipLevel() {
        return "1".equals(vipLevel);
    }

    public boolean floatingNetwork() {
        return "1".equals(floatingNetwork);
    }

    public boolean share() {
        return "1".equals(share);
    }

    public boolean floating() {
        return "1".equals(floating);
    }

    public boolean logoutAdvert() {
        return "1".equals(logoutAdvert);
    }

    public boolean ptb() {
        return "1".equals(ptbAdd);
    }

    public boolean changeAccount() {
        return "1".equals(changeAccount);
    }

    public static void setSwitchManager(SwitchManager switchManager) {
        SwitchManager.switchManager = switchManager;
    }

    public void setAccountRegister(String accountRegister) {
        this.accountRegister = accountRegister;
    }

    public void setPhoneRegister(String phoneRegister) {
        this.phoneRegister = phoneRegister;
    }

    public void setEmailRegister(String emailRegister) {
        this.emailRegister = emailRegister;
    }

    public void setVipLevel(String vipLevel) {
        this.vipLevel = vipLevel;
    }

    public void setFloatingNetwork(String floatingNetwork) {
        this.floatingNetwork = floatingNetwork;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public void setFloating(String floating) {
        this.floating = floating;
    }

    public void setFloatingIconUrl(String floatingIconUrl) {
        this.floatingIconUrl = floatingIconUrl;
    }

    public void setLogoutAdvert(String logoutAdvert) {
        this.logoutAdvert = logoutAdvert;
    }

    public void setPtbAdd(String ptbAdd) {
        this.ptbAdd = ptbAdd;
    }

    public void setChangeAccount(String changeAccount) {
        this.changeAccount = changeAccount;
    }
}
