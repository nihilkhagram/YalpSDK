package com.cointizen.paysdk.bean;

/**
 * 描述：提现
 * 时间: 2018-10-27 14:14
 */

public class TiXianBean {

    /**
     * url : http://sysdk20.ceshi.vlcms.com/mobile.php?s=/Trade/withdraw_gold.html
     * IS_OPEN_SMALL_ACCOUNT : 0
     */

    private String url;
    private int IS_OPEN_SMALL_ACCOUNT;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIS_OPEN_SMALL_ACCOUNT() {
        return IS_OPEN_SMALL_ACCOUNT;
    }

    public void setIS_OPEN_SMALL_ACCOUNT(int IS_OPEN_SMALL_ACCOUNT) {
        this.IS_OPEN_SMALL_ACCOUNT = IS_OPEN_SMALL_ACCOUNT;
    }
}
