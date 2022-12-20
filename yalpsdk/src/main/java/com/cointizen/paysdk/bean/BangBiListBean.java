package com.cointizen.paysdk.bean;

import java.util.List;

/**
 * 描述：绑币列表
 * 时间: 2018-10-27 14:58
 */

public class BangBiListBean {

    /**
     * code : 200
     * msg : 成功
     * data : [{"game_name":"测试游戏(安卓版)","game_id":"1","bind_balance":"0.00","icon":""}]
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
         * game_name : 测试游戏(安卓版)
         * game_id : 1
         * bind_balance : 0.00
         * icon :
         */

        private String game_name;
        private String game_id;
        private String bind_balance;
        private String icon;

        public String getGame_name() {
            return game_name;
        }

        public void setGame_name(String game_name) {
            this.game_name = game_name;
        }

        public String getGame_id() {
            return game_id;
        }

        public void setGame_id(String game_id) {
            this.game_id = game_id;
        }

        public String getBind_balance() {
            return bind_balance;
        }

        public void setBind_balance(String bind_balance) {
            this.bind_balance = bind_balance;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
