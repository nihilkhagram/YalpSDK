package com.cointizen.paysdk.bean;

/**
 * 描述：退出弹窗广告
 * 时间: 2018-10-23 17:27
 */

public class LogoutADVModel {

    /**
     * title : 测试
     * data : http://sysdk20.ceshi.vlcms.com/Uploads/Picture/2018-10-23/5bcedbc45ba76.png
     * url : http://www.google.com
     */

    private String title;
    private String data;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
