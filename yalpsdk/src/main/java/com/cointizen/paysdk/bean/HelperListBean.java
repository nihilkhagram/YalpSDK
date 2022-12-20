package com.cointizen.paysdk.bean;

import java.util.List;

/**
 * 描述：帮助中心集合
 * 时间: 2018-10-26 17:50
 */

public class HelperListBean {

    /**
     * code : 200
     * msg : 成功
     * data : [{"id":"1","first_title":"密码问题","mark":"mima","second_title":["我修改密码的时候，为什么接收不到短信验证码？该怎么办呢？","我换了手机，但他那里提示要输入密码，我注册的时候是自动登陆进去的？该怎么找回密码？","密码问题1"]},{"id":"6","first_title":"账户问题","mark":"account","second_title":[]},{"id":"7","first_title":"充值问题","mark":"pay","second_title":["充值后没有给奖励","充值问题1","充值问题2"]},{"id":"8","first_title":"礼包问题","mark":"gift","second_title":["礼包问题1"]},{"id":"9","first_title":"常见问题","mark":"often","second_title":["为什什么我登录游戏的时什么我登录游戏的时什么我登录游戏的时什么我登录游戏的时什么我登录游戏的时么我登录游戏的时为什么我登录游戏的时为什么我登录游戏的时为什么我登录游戏的时候，登不上去，没有任何提示？","闪退\\掉线\\卡机如何处理?"]},{"id":"25","first_title":"商城问题","mark":"shop","second_title":[]}]
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
         * id : 1
         * first_title : 密码问题
         * mark : mima
         * second_title : [BeanConstants.S_pDZUwEyuAj,BeanConstants.S_EHxhGOZXeu,BeanConstants.S_JkzYqWHFaU]
         */

        private String id;
        private String first_title;
        private String mark;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        private String icon;
        private List<String> second_title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirst_title() {
            return first_title;
        }

        public void setFirst_title(String first_title) {
            this.first_title = first_title;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public List<String> getSecond_title() {
            return second_title;
        }

        public void setSecond_title(List<String> second_title) {
            this.second_title = second_title;
        }
    }
}
