package com.cointizen.paysdk.bean;

import java.util.List;

/**
 * 描述：帮助详情
 * 时间: 2018-10-27 10:56
 */

public class HelperDetBean {

    /**
     * code : 200
     * msg : 成功
     * data : [{"title":"充值后没有给奖励","contend":"亲爱的玩家：您好!请您确认您充值的时间是否在活动时间内，充值的金额是否符合活动要求。若还有疑问，请拨打我们的客服电话：进行咨询，或者到我们梦创游戏平台官网联系在线QQ客服反馈。"},{"title":"充值问题1","contend":"充值问题1"},{"title":"充值问题2","contend":"充值问题2"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * title : 充值后没有给奖励
         * contend : 亲爱的玩家：您好!请您确认您充值的时间是否在活动时间内，充值的金额是否符合活动要求。若还有疑问，请拨打我们的客服电话：进行咨询，或者到我们梦创游戏平台官网联系在线QQ客服反馈。
         */

        private String title;
        private String contend;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContend() {
            return contend;
        }

        public void setContend(String contend) {
            this.contend = contend;
        }
    }
}
