package com.cointizen.paysdk.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.cointizen.paysdk.activity.MCHTransparencyActivity;
import com.cointizen.paysdk.callback.OnPermission;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.dialog.YouKeLoginDialog;
import com.cointizen.paysdk.entity.UserLogin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 描述：截屏工具类
 * 时间: 2018-08-25 11:42
 */

public class ScreenshotUtils {

    private static ScreenshotUtils instance;
    private Activity activity;
    private MediaProjectionManager mMediaProjectionManager;      //媒体投影管理器 实例
    private String TAG = "ScreenshotUtils";
    private Handler handler1 = new Handler();
    private String pathImage = Environment.getExternalStorageDirectory().getPath() + "/ScreenShot/";
    private File imageFile;
    private WindowManager mWindowManager1;
    private int windowWidth;
    private int windowHeight;
    private DisplayMetrics metrics;         //当前手机屏幕的显示指标
    private int mScreenDensity;             //屏幕密度
    private ImageReader mImageReader;       //图像阅读器
    private MediaProjection mMediaProjection;
    private int result;
    private Intent intent;
    private VirtualDisplay mVirtualDisplay;         //虚拟显示
    private ImageView imageView;                    //用于展示截图后的图片容器
    private int mRequestCode;

    /**
     * 首次调用悬浮窗
     */
    private boolean isFirst = true;
    private ScreenBroadCastReceiver screenBroadCastReceiver;
    private YouKeLoginDialog ykLoginDialog;
    private static boolean execute = false;
    private String account;
    private String password;
    private UserLogin smallAccount;

    public static ScreenshotUtils getInstance() {
        if (null == instance) {
            instance = new ScreenshotUtils();
        }
        return instance;
    }

    /**
     *
     * @param img           如果截屏后需要展示则传img    否则传null
     * @param requestCode   requestCode由用户自己定义防止冲突
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void Screenshot(ImageView img,int requestCode){
        imageView = img;
        mRequestCode = requestCode;
        if (null == mMediaProjectionManager) {
            //获取媒体投影管理器 实例
            mMediaProjectionManager = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        }
        activity.startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), mRequestCode);
        MCLog.w(TAG, UtilsConstants.Log_kNBLjnmMEn);
    }

    public void init(Activity activity){
        this.activity = activity;
        if(screenBroadCastReceiver == null && activity != null && !SwitchUtils.getInstance().getIsRegister()) {
            screenBroadCastReceiver = new ScreenBroadCastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("isYKLogin");                    //拦截游客登录广播
            intentFilter.addAction("NoticeClose");                    //拦截公告弹窗消失
            activity.registerReceiver(screenBroadCastReceiver,intentFilter);
            SwitchUtils.getInstance().setIsRegister(true);
        }
    }

    public void onDestroy(){
        if(activity != null && screenBroadCastReceiver != null && SwitchUtils.getInstance().getIsRegister()){
            activity.unregisterReceiver(screenBroadCastReceiver);
            screenBroadCastReceiver = null;
            SwitchUtils.getInstance().setIsRegister(false);
        }
    }

    public void ActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == mRequestCode&&resultCode == Activity.RESULT_OK) {
            MCLog.w(TAG, "fun#ActivityResult requestCode=" + requestCode + ",mRequestCode="+
                    mRequestCode + "," + (resultCode == Activity.RESULT_OK));
            if(activity == null) {
                MCLog.w(TAG, UtilsConstants.Log_IMqAlVgpVa);
            }else {
                if (data != null && resultCode != 0) {
                    result = resultCode;
                    intent = data;
                    //请求写SD卡权限
                    requestPermission();
                }else {
                    MCLog.e(TAG, UtilsConstants.Log_xfhHdjUUmJ);
                }
            }
        }else {
            if(ykLoginDialog != null){
                ykLoginDialog.dismiss();
            }
            MCLog.w(TAG, "fun#ActivityResult requestCode="+
                    requestCode+",mRequestCode="+
                    mRequestCode+","+(resultCode == Activity.RESULT_OK));
        }
    }


    /**
     * 请求写SD卡权限
     */
    private void requestPermission() {
        PermissionsUtils.with(activity)
                .permission(PermissionsUtils.STORAGE)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        Toast t = Toast.makeText(activity,UtilsConstants.S_qIBOXAweTc,Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                        t.show();
                        //进行截图及保存
                        createVirtualEnvironment();
                        SwitchUtils.getInstance().setIsShowShare(false);
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        ToastUtil.show(activity,UtilsConstants.S_jWpVtuBqqA);
                        if(ykLoginDialog != null){
                            ykLoginDialog.dismiss();
                        }
                    }

                });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createVirtualEnvironment() {
        imageFile = new File(pathImage);
        if (!imageFile.exists()) {
            imageFile.mkdirs();
        }
        mWindowManager1 = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        windowWidth = mWindowManager1.getDefaultDisplay().getWidth();
        windowHeight = mWindowManager1.getDefaultDisplay().getHeight();
        metrics = new DisplayMetrics();
        mWindowManager1.getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        //参数1 2  是生成图像的宽高  参数3  是图像的格式，这个格式必须是 ImageFormat 或 PixelFormat 中的一个
        //参数4  获取图片张数  字面意思不是很懂
        mImageReader = ImageReader.newInstance(windowWidth, windowHeight, PixelFormat.RGBA_8888, 2); //ImageFormat.RGB_565

        handler1.postDelayed(new Runnable() {
            public void run() {
                //start virtual
                startVirtual();
                handler1.postDelayed(new Runnable() {
                    public void run() {
                        //capture the screen
                        startCapture();
                    }
                }, 500);
            }
        }, 500);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startVirtual() {
        if (mMediaProjection != null) {
            virtualDisplay();
        } else {
            setUpMediaProjection();
            virtualDisplay();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpMediaProjection() {
        mMediaProjection = mMediaProjectionManager.getMediaProjection(result, intent);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void virtualDisplay() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                windowWidth, windowHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
    }

    /**
     * 将Image转成Bitmap 存到相册
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startCapture() {
        File file = new File(pathImage);
        if(!file.exists()){
            file.mkdirs();
        }

        String nameImage = "";
        if(account!=null){
             nameImage = pathImage + account + ".png";
        }else{
             nameImage = pathImage + System.currentTimeMillis() + ".png";
        }
        Image image = mImageReader.acquireLatestImage();
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        image.close();
        if (bitmap != null) {
//            ivImage.setImageBitmap(bitmap);
            try {
                File imagePath = new File(nameImage);
                if (!imagePath.exists()) {
                    imagePath.createNewFile();
                }
                FileOutputStream out = new FileOutputStream(imagePath);
                if (out != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(imagePath);
                    media.setData(contentUri);
                    activity.sendBroadcast(media);
//                    startImgFloating(activity,nameImage);
                    ToastUtil.show(activity,UtilsConstants.S_paBzdWPeSM);
                    MCLog.w(TAG, UtilsConstants.Log_GTEczyBWbo+nameImage+UtilsConstants.Log_ZtrXoEFVLZ);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if(ykLoginDialog != null){
                                ykLoginDialog.dismiss();
                            }
                        }
                    },1500);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                MCLog.e(TAG, "fun#startCapture FileNotFoundException:"+e);
            } catch (IOException e) {
                e.printStackTrace();
                MCLog.e(TAG, "fun#startCapture IOException:"+e);
            }
        }else{
            MCLog.e(TAG, UtilsConstants.Log_AAQvTXYpGC);
        }
    }



    class ScreenBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("isYKLogin")) {
                account = intent.getStringExtra("account");
                password = intent.getStringExtra("password");
                smallAccount = (UserLogin)intent.getSerializableExtra("SmallAccount");
                if(MCHTransparencyActivity.instance!=null){
                    MCHTransparencyActivity.instance.closeActivity();
                }
//                if(!TextUtils.isEmpty(password)){
//                    handler.sendEmptyMessageDelayed(1,500);     //延迟的目的是为了  让登录成功的弹窗在  认证的弹窗上面  截图时可以截到登录成功的弹窗
//                }else{
//                    LoginModel.instance().smallAccountLoginSuccess(smallAccount);
//                }
                handler.sendEmptyMessageDelayed(1,500);     //延迟的目的是为了  让登录成功的弹窗在  认证的弹窗上面  截图时可以截到登录成功的弹窗
            }else if(action.equals("NoticeClose")){
                Constant.showedNoteDialog = true;
            }
        }
    }


    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            String ykPassword = SharedPreferencesUtils.getInstance().getYKPassword(activity);
            ykLoginDialog = new YouKeLoginDialog.Builder()
                    .setAccount(account)
                    .setPassword(ykPassword)
                    .setLoginInfo(smallAccount)
                    .show(activity, activity.getFragmentManager());

            File file = new File(pathImage + account + ".png");
            if(!file.exists()){
                if(Build.VERSION.SDK_INT > 21){
                    Screenshot(null,333);
                }else{
                    MCLog.e(TAG,UtilsConstants.Log_oSFktnoSPB);
                }
                if(Build.VERSION.SDK_INT < 23) {
                    mmhandler.sendEmptyMessageDelayed(1,3000);
                }
            }else{
                MCLog.e(TAG,UtilsConstants.Log_KmnCZEyixn);
                mmhandler.sendEmptyMessageDelayed(1,1000);     //弹窗显示1秒
            }
        }
    };

    /**
     * 延迟关闭登录成功弹窗
     */
    Handler mmhandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(ykLoginDialog != null){
                        ykLoginDialog.dismiss();
                    }
                    break;
//                case Constant.NOTICE_SUCCESS:
//                    NoticeModel obj = (NoticeModel) msg.obj;
//                    if(obj.getList().size()>0){
//                        Intent intent = new Intent(activity, NoticeDialog.class);
//                        intent.putExtra("NoticeModel",obj);
//                        activity.startActivity(intent);
//                    }
//                    break;
//                case Constant.NOTICE_FAIL:
//                    MCLog.e(TAG, UtilsConstants.Log_paBCRWvovz + msg.obj);
//                    break;
            }
        }
    };
}
