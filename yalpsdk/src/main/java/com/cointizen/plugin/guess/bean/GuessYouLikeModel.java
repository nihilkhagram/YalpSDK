package com.cointizen.plugin.guess.bean;

import java.util.List;

/**
 * 描述：猜你喜欢
 * 时间: 2018-10-24 11:34
 */

public class GuessYouLikeModel {

    /**
     * status : 200
     * list : [{"title":"百度","url":"http://baidu.com/","icon":"http://sysdk20.ceshi.vlcms.com/Uploads/Picture/2018-10-23/5bcedbc45ba76.png"},{"title":"头条","url":"http://baidu.com/","icon":"http://sysdk20.ceshi.vlcms.com/Uploads/Picture/2018-10-24/5bcfdd0926ecf.png"}]
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
         * title : 百度
         * url : http://baidu.com/
         * icon : http://sysdk20.ceshi.vlcms.com/Uploads/Picture/2018-10-23/5bcedbc45ba76.png
         */

        private String title;
        private String url;
        private String icon;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
