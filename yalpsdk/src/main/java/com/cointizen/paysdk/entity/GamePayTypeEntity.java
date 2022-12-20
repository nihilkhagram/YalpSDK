package com.cointizen.paysdk.entity;

/**
 * Created by zhujinzhujin
 * on 2017/1/11.
 */

public class GamePayTypeEntity {

    public boolean isHavePTB;
    public boolean isHaveZFB;
    public boolean isHaveWX;
    public boolean ZFBisWapPay;
    public boolean isHaveBindPtb;
    public boolean isHaceScan;
    public boolean wxRecommand;
    public boolean zfbRecommand;
    public boolean ptbRecommand;
    public boolean bindRecommand;

    public String wxRemark;
    public String zfbRemark;
    public String ptbRemark;
    public String bindRemark;
    public boolean supportYlp;
    public boolean supportFiat;
}
