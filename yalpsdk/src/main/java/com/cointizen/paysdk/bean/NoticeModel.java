package com.cointizen.paysdk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：公告
 * 时间: 2018-10-18 20:32
 */

public class NoticeModel implements Serializable{


    /**
     * status : 200
     * list : [{"title":"杀反对第三方手房东是个地方合同于一体","content":"的方式发顺丰 如果突然 大股东股份的"},{"title":"阿萨斯","content":"阿萨啊飒飒撒飒飒"}]
     */

    private int status;
    private List<ListBean> list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        /**
         * title : 杀反对第三方手房东是个地方合同于一体
         * content : 的方式发顺丰 如果突然 大股东股份的
         */

        private String title;
        private String content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
