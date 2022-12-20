package com.cointizen.paysdk.utils;

import com.cointizen.paysdk.bean.UserInfoBean;
import com.cointizen.paysdk.common.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import java.util.LinkedList;

public class PreSharedManager {

    public static final String SHARED_PREFS_FILE = "INTERACTIVE";
    public static final String USERNAME = "MENGCHUANG_USERNAME";

    /**
     * 设置string值
     *
     * @param key
     * @param value
     * @param context
     */
    public static void setString(String key, String value, Context context) {
        Editor editor = context.getSharedPreferences(Constant.PRE_NAME,
                Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 得到string值
     *
     * @param key
     * @param context
     * @return
     */
    public static String getString(String key, Context context) {
        return context.getSharedPreferences(Constant.PRE_NAME,
                Context.MODE_PRIVATE).getString(key, "");
    }
//
//	/**
//	 * 得到long值
//	 *
//	 * @param key
//	 * @param context
//	 * @return
//	 */
//	public static long getLong(String key, Context context) {
//		return context.getSharedPreferences(Constant.PRE_NAME,
//				Context.MODE_PRIVATE).getLong(key, 0);
//	}
//
//
//	/**
//	 * 得到int值
//	 *
//	 * @param key
//	 * @param context
//	 * @param defValue
//	 * @return
//	 */
//	public static int getInt(String key, Context context, int defValue) {
//		return context.getSharedPreferences(Constant.PRE_NAME,
//				Context.MODE_PRIVATE).getInt(key, defValue);
//	}
//
//	/**
//	 * 设置long值
//	 *
//	 * @param key
//	 * @param value
//	 * @param context
//	 */
//	public static void setLong(String key, Long value, Context context) {
//		Editor editor = context.getSharedPreferences(Constant.PRE_NAME,
//				Context.MODE_PRIVATE).edit();
//		editor.putLong(key,value);
////		editor.putString(key, value);
//		editor.commit();
//	}
//
//	/**
//	 * 设置int值
//	 *
//	 * @param key
//	 * @param value
//	 * @param context
//	 */
//	public static void setInt(String key, int value, Context context) {
//		Editor editor = context.getSharedPreferences(Constant.PRE_NAME,
//				Context.MODE_PRIVATE).edit();
//		editor.putInt(key, value);
//		editor.commit();
//	}

    /**
     * 存储list<bean>
     */
    private static void setUserInfoList(LinkedList<UserInfoBean> list, Context context) {
        Editor editor = context.getSharedPreferences(SHARED_PREFS_FILE,
                Context.MODE_PRIVATE).edit();
        if (list == null || list.size() == 0) {
            editor.clear();
            editor.commit();
            return;
        }
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(list);
//        editor.clear();
        editor.putString(USERNAME, strJson);
        editor.commit();
    }

    /**
     * 获取用户信息List<UserInfoBean>
     *
     * @param context
     * @return
     */
    public static LinkedList<UserInfoBean> getUserInfoList(Context context) {
        LinkedList<UserInfoBean> list = new LinkedList<>();
        String strJson = context.getSharedPreferences(SHARED_PREFS_FILE,
                Context.MODE_PRIVATE).getString(USERNAME, "");
        if (strJson == null) {
            return list;
        }
        Gson gson = new Gson();
        list = gson.fromJson(strJson, new TypeToken<LinkedList<UserInfoBean>>() {
        }.getType());
        return list;
    }


    /**
     * 操作用户信息List<UserInfoBean>，最多保存10个
     *
     * @param context
     * @return
     */
    public static void saveUserInfoList(Context context, UserInfoBean userInfoBean) {
        LinkedList<UserInfoBean> oldList = getUserInfoList(context);
        if (null != oldList && oldList.size() != 0) {
            for (int i = 0; i < oldList.size(); i++) {
                //只保留最新登录的账号密码
                oldList.get(i).setPwd("");
                if (userInfoBean.getAccount().equals(oldList.get(i).getAccount())) {
                    oldList.remove(i);
                }
            }
            if (oldList.size() == 5) {
                //最多保存5个用户名密码
                oldList.remove(4);
            }
            oldList.addFirst(userInfoBean);
        } else {
            oldList = new LinkedList<>();
            oldList.add(userInfoBean);
        }
        setUserInfoList(oldList, context);
    }

    /**
     * 删除list某个项并保存
     *
     * @param context
     * @param
     */
    public static void removeAndSaveUserInfoList(Context context, int position) {
        LinkedList<UserInfoBean> oldList = getUserInfoList(context);
        oldList.remove(position);
        setUserInfoList(oldList, context);
    }


}
