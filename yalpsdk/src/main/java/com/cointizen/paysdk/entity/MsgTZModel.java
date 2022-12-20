package com.cointizen.paysdk.entity;

import java.util.List;

/**
 * 描述：消息通知
 * 时间: 2018-10-19 10:06
 */

public class MsgTZModel {

    /**
     * status : 200
     * list : [{"notice_id":"1","title":"阿萨斯","content":"阿萨啊飒飒撒飒飒","create_time":"1537372800","type":"1"},{"notice_id":"3","title":"杀反对第三方手房东是个地方合同于一体","content":"的方式发顺丰 如果突然 大股东股份的","create_time":"1537498800","type":"1"}]
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

    public static class ListBean {
        /**
         * notice_id : 1
         * title : 阿萨斯
         * content : 阿萨啊飒飒撒飒飒
         * create_time : 1537372800
         * type : 1
         */

        private String notice_id;
        private String title;
        private String content;
        private Long create_time;
        private String url;
        private String msgType;//2=资讯 3=活动 4=公告 5=攻略

        public String getRead() {
            return read;
        }

        public void setRead(String read) {
            this.read = read;
        }

        private String read;

        public String getNotice_id() {
            return notice_id;
        }

        public void setNotice_id(String notice_id) {
            this.notice_id = notice_id;
        }

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

        public Long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(Long create_time) {
            this.create_time = create_time;
        }

        public String getMsgType() {
            return msgType;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String typeName() {
            if ("2".equals(msgType)) {
                return EntityConstants.S_ALQVExFJMQ;
            }else if ("3".equals(msgType)) {
                return EntityConstants.S_IShSfYPsdg;
            }else if ("4".equals(msgType)) {
                return EntityConstants.S_KnGKlHPerS;
            }else if ("5".equals(msgType)) {
                return EntityConstants.S_RqkZSHAzfX;
            }
            return EntityConstants.S_WfKmnwsefk;
        }
    }
}
