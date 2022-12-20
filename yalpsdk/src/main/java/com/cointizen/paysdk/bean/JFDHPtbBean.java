package com.cointizen.paysdk.bean;

import java.util.List;

/**
 * 描述：平台币兑换记录
 * 时间: 2018-10-27 16:11
 */

public class JFDHPtbBean {

    /**
     * code : 200
     * msg : 成功
     * data : [{"id":"3","good_name":"平台币","good_type":"3","number":"1","pay_amount":"100","create_time":"1540627196","count":"300"},{"id":"2","good_name":"平台币","good_type":"3","number":"1","pay_amount":"100","create_time":"1540627120","count":"300"},{"id":"1","good_name":"平台币","good_type":"3","number":"1","pay_amount":"100","create_time":"1540627037","count":"300"}]
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
         * id : 3
         * good_name : 平台币
         * good_type : 3
         * number : 1
         * pay_amount : 100
         * create_time : 1540627196
         * count : 300
         */

        private String id;
        private String good_name;
        private String good_type;
        private String number;
        private String pay_amount;
        private String create_time;
        private String count;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGood_name() {
            return good_name;
        }

        public void setGood_name(String good_name) {
            this.good_name = good_name;
        }

        public String getGood_type() {
            return good_type;
        }

        public void setGood_type(String good_type) {
            this.good_type = good_type;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getPay_amount() {
            return pay_amount;
        }

        public void setPay_amount(String pay_amount) {
            this.pay_amount = pay_amount;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}
