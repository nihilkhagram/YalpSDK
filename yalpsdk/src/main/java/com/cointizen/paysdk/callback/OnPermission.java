package com.cointizen.paysdk.callback;

import java.util.List;

/**
 * 描述：权限请求回调
 * 时间: 2018-08-29 13:32
 */

public interface OnPermission {

    /**
     * 有权限被授予时回调
     *
     * @param granted 请求成功的权限组
     * @param isAll   是否全部授予了
     */
    void hasPermission(List<String> granted, boolean isAll);

    /**
     * 有权限被拒绝授予时回调
     *
     * @param denied 请求失败的权限组
     * @param quick  是否被系统自动拒绝了
     */
    void noPermission(List<String> denied, boolean quick);
}
