package com.cointizen.paysdk.entity;

/**
 * Created by Administrator
 * on 2016/12/27.
 */
public class ThirdLoginParamsBean {
    /**
     * 返回状态码 1 为成功
     */
    public String status;
    /**
     * 返回信息
     */
    public String message;
    /**
     * 其他信息
     */
//    public Object param;
    /**
     * 支付类型
     */
    public String login_type;

    //微信
    public String weixinappid;
    //qq
    public String qqappid;
    //weibo
    public String weiboappkey;
    public String redirecturl;
    public String wbscope;
    //baidu
    public String bdclientid;
}
