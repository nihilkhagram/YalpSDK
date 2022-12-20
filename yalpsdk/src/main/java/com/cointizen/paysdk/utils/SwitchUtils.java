package com.cointizen.paysdk.utils;

/**
 * 描述：控制开关 工具类
 * 时间: 2018-08-30 16:55
 */

public class SwitchUtils {


    private static SwitchUtils instance;
    private boolean show = false;
    private boolean ShareShow = true;       //默认截图后展示分享弹窗
    private boolean IsRegister = false;
    private long showTime;

    public static SwitchUtils getInstance() {
        if (null == instance) {
            instance = new SwitchUtils();
        }
        return instance;
    }

    /**
     * 设置是否已经展示过了FloatingHeadView
     * @param show
     */
    public void setIsShowFloatingHeadView(boolean show){
        this.show = show;
    }

    public boolean getIsShowFloatingHeadView(){
        return show;
    }

    /**
     * 设置FloatingHeadView剩余展示时间
     * @param show
     */
    public void setIsShowFloatingHeadViewTime(long show){
        showTime = show;
    }

    public long getIsShowFloatingHeadViewTime(){
        return showTime;
    }


    /**
     * 设置是否已经展示过了FloatingHeadView
     * @param show
     */
    public void setIsShowShare(boolean show){
        this.ShareShow = show;
    }

    public boolean getIsShowShare(){
        return ShareShow;
    }

    /**
     * 设置是否注册过广播了
     * @param IsRegister
     */
    public void setIsRegister(boolean IsRegister){
        this.IsRegister = IsRegister;
    }

    public boolean getIsRegister(){
        return IsRegister;
    }


}
