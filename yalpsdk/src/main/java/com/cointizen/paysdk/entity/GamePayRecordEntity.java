package com.cointizen.paysdk.entity;

import java.util.List;

/**
 * 描述：游戏消费记录
 * 时间: 2019/7/16 3:47 PM
 */
public class GamePayRecordEntity {

    /**
     * lists : [{"id":5584,"pay_order_number":"SP_201908281110146f2b","user_id":1136,"promote_id":0,"pay_amount":"0.01","pay_way":"支付宝","pay_time":1566961814,"game_name":"测试游戏(安卓版)"}]
     * count : 1
     */

    private double count;
    private List<ListsBean> lists;

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public List<ListsBean> getLists() {
        return lists;
    }

    public void setLists(List<ListsBean> lists) {
        this.lists = lists;
    }

    public static class ListsBean {
        /**
         * id : 5584
         * pay_order_number : SP_201908281110146f2b
         * user_id : 1136
         * promote_id : 0
         * pay_amount : 0.01
         * pay_way : 支付宝
         * pay_time : 1566961814
         * game_name : 测试游戏(安卓版)
         */

        private int id;
        private String pay_order_number;
        private int user_id;
        private int promote_id;
        private String pay_amount;
        private String pay_way;
        private long pay_time;
        private String game_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPay_order_number() {
            return pay_order_number;
        }

        public void setPay_order_number(String pay_order_number) {
            this.pay_order_number = pay_order_number;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getPromote_id() {
            return promote_id;
        }

        public void setPromote_id(int promote_id) {
            this.promote_id = promote_id;
        }

        public String getPay_amount() {
            return pay_amount;
        }

        public void setPay_amount(String pay_amount) {
            this.pay_amount = pay_amount;
        }

        public String getPay_way() {
            return pay_way;
        }

        public void setPay_way(String pay_way) {
            this.pay_way = pay_way;
        }

        public long getPay_time() {
            return pay_time;
        }

        public void setPay_time(long pay_time) {
            this.pay_time = pay_time;
        }

        public String getGame_name() {
            return game_name;
        }

        public void setGame_name(String game_name) {
            this.game_name = game_name;
        }
    }
}
