package com.cointizen.paysdk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.lidroid.xutils.BitmapUtils;

import java.io.File;

/**
 * Author: Administrator
 * Date: 13-11-12
 * : 上午10:24
 */
public class BitmapHelp {
    private BitmapHelp() {
    }

    private static BitmapUtils bitmapUtils;

    /**
     * BitmapUtils不是单例的 根据需要重载多个获取实例的方法
     *
     * @param appContext application context
     * @return
     */
    public static BitmapUtils getBitmapUtils(Context appContext) {
        if (bitmapUtils == null) {
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                String diskCachePath = appContext.getExternalFilesDir(null).getPath() + File.separator + "XiGuSDK";
//                String diskCachePath=Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"XiGuSDK";
//            内存缓存大小
                int memoryCacheSize= (int) (Runtime.getRuntime().totalMemory()/8);
//            磁盘缓存大小 100M
                int diskCacheSize=1024*1024*100;
                bitmapUtils=new BitmapUtils(appContext,diskCachePath,memoryCacheSize,diskCacheSize);
            }else{
                bitmapUtils = new BitmapUtils(appContext);
            }

//        设置
//        设置图片的质量
            bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.ARGB_8888);
//        设置自动旋转 eg:手机拍照时,有横竖之分
//            bitmapUtils.configDefaultAutoRotation(true);
//        设置宽高
//            bitmapUtils.configDefaultBitmapMaxSize(new BitmapSize(200,300));
//        设置联网超时时间
            bitmapUtils.configDefaultConnectTimeout(5000);
//        设置联网请求加载失败时 显示的图片
//            bitmapUtils.configDefaultLoadFailedImage(R.mipmap.ic_launcher);
//        设置正在加载时显示的图片
//            bitmapUtils.configDefaultLoadingImage(R.mipmap.ic_launcher);
//        设置缓存失效时间 1天
            bitmapUtils.configDefaultCacheExpiry(1000*60*60*24*7);
//        设置SD卡是否缓存
            bitmapUtils.configDiskCacheEnabled(true);
//        设置是否进行内存缓存
            bitmapUtils.configMemoryCacheEnabled(true);
        }

        return bitmapUtils;
    }
}
