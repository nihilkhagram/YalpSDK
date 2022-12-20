package com.cointizen.plugin.qg.utils;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：接口返回结果
 * 时间: 2018-09-05 9:26
 */

public class UpdateBean implements Serializable {


    /**
     * and_version_code : 6
     * and_file_url : ./Uploads/App/and_app_package_for_version.apk
     * and_file_size : 99.67MB
     * and_remark : []
     */

    private String version_name;
    private int and_version_code;
    private String and_file_url;
    private String and_file_size;

    public int getIs_force_update() {
        return is_force_update;
    }

    public void setIs_force_update(int is_force_update) {
        this.is_force_update = is_force_update;
    }

    private int is_force_update;
    private List<String> and_remark;


    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public int getAnd_version_code() {
        return and_version_code;
    }

    public void setAnd_version_code(int and_version_code) {
        this.and_version_code = and_version_code;
    }

    public String getAnd_file_url() {
        return and_file_url;
    }

    public void setAnd_file_url(String and_file_url) {
        this.and_file_url = and_file_url;
    }

    public String getAnd_file_size() {
        return and_file_size;
    }

    public void setAnd_file_size(String and_file_size) {
        this.and_file_size = and_file_size;
    }

    public List<String> getAnd_remark() {
        return and_remark;
    }

    public void setAnd_remark(List<String> and_remark) {
        this.and_remark = and_remark;
    }
}
