package com.cointizen.paysdk.bean;

/**
 * 描述：帮助中心
 * 时间: 2018-10-26 16:36
 */

public class HelperBean {

    /**
     * APP_QQ : 1064910627
     * APP_TEL :
     * QQ_GROUP_KEY : 123456
     * APP_EMAIL :
     */

    private String gameCommunity;
    private String APP_TEL;
    private String QQ_GROUP_KEY;
    private String APP_EMAIL;
    private String ServiceH5Switch;//1开
    private String ServiceH5URL;
    public String serviceSwitch;

    public String getAPP_QQ_GROUP() {
        return APP_QQ_GROUP;
    }

    public void setAPP_QQ_GROUP(String APP_QQ_GROUP) {
        this.APP_QQ_GROUP = APP_QQ_GROUP;
    }

    private String APP_QQ_GROUP;

    public String getGameCommunity() {
        return gameCommunity;
    }

    public void setGameCommunity(String APP_QQ) {
        this.gameCommunity = APP_QQ;
    }

    public String getAPP_TEL() {
        return APP_TEL;
    }

    public void setAPP_TEL(String APP_TEL) {
        this.APP_TEL = APP_TEL;
    }

    public String getQQ_GROUP_KEY() {
        return QQ_GROUP_KEY;
    }

    public void setQQ_GROUP_KEY(String QQ_GROUP_KEY) {
        this.QQ_GROUP_KEY = QQ_GROUP_KEY;
    }

    public String getAPP_EMAIL() {
        return APP_EMAIL;
    }

    public void setAPP_EMAIL(String APP_EMAIL) {
        this.APP_EMAIL = APP_EMAIL;
    }

    public boolean getServiceH5Switch() {
        return "1".equals(ServiceH5Switch);
    }

    public void setServiceH5Switch(String serviceH5Switch) {
        ServiceH5Switch = serviceH5Switch;
    }

    public String getServiceH5URL() {
        return ServiceH5URL;
    }

    public void setServiceH5URL(String serviceH5URL) {
        ServiceH5URL = serviceH5URL;
    }
}
