package com.cointizen.paysdk.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.dialog.MCLoadDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 描述：工具类
 * 时间: 2018-09-21 9:32
 */

public class AppUtils {

    /**
     * Regex of email.
     */
    public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    private static HttpURLConnection conn;

    public static void returnBitMap(final String url1){
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (TextUtils.isEmpty(url1)) {
                    MCLog.e(UtilsConstants.Log_HbwklXQNYb, "imageURL is null");
                    return;
                }
                URL url = null;
                InputStream is = null;
                try {
                    url = new URL(url1);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    if (conn.getResponseCode() == 200) {
                        is = conn.getInputStream();
                        Constant.ShareBitmap = BitmapFactory.decodeStream(is);

                        if (Constant.ShareBitmap == null){
                            MCLog.e(UtilsConstants.Log_HbwklXQNYb, "BitmapFactory.decodeStream is null");
                            return;
                        }

                        Bitmap thumbBmp = Bitmap.createScaledBitmap(Constant.ShareBitmap, 150, 150, true);
                        Constant.ShareBitmap = thumbBmp;
                        for (int imageSize = 0, quality = 100; imageSize > 31; quality--) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            thumbBmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                            byte[] bytes = baos.toByteArray();
                            Constant.ShareBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            imageSize = bytes.length / 1024;
                            MCLog.e("wechat", "压缩后图片的大小" + (Constant.ShareBitmap.getByteCount() / 1024 / 1024)
                                    + UtilsConstants.S_jUKihFQAlF + Constant.ShareBitmap.getWidth() + UtilsConstants.S_TNKeVOVSJB + Constant.ShareBitmap.getHeight()
                                    + "bytes.length=  " + (bytes.length / 1024) + "KB"
                                    + "quality=" + quality);

                        }
                    }
                } catch (MalformedURLException e) {
                    Constant.ShareBitmap = null;
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    public static int getTotalHeightofListView(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            //mView.measure(0, 0);
            totalHeight += mView.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
        return totalHeight;
    }


    /**
     * 调用第三方浏览器打开
     * @param context
     * @param url 要浏览的资源地址
     */
    public static void openBrowser(Context context, String url){
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            context.startActivity(Intent.createChooser(intent, UtilsConstants.S_WWXCdHOFAu));
        } else {
            ToastUtil.show(context,UtilsConstants.S_zBpifixEda);
        }
    }

    public static String MonthDay(String time,String geshi) {
        SimpleDateFormat sdr = new SimpleDateFormat(geshi);
        long i = Long.parseLong(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //输入身份证号 获得生日信息
    public static String getBirthday(String id){
        try {
            if (id.length() == 18){
                String year = id.substring(6, 10);// 截取年
                String month = id.substring(10, 12);// 截取月份
                String  day = id.substring(12, 14);// 截取天
                return year + month + day;
            }else if (id.length() == 15){
                String year = "19" + id.substring(6, 8);// 截取年
                String month = id.substring(8, 10);// 截取月份
                String  day = id.substring(10, 12);// 截取天
                return year + month + day;
            }
            else return "";
        }catch (Exception e){
            Log.e(UtilsConstants.S_hTcbSzuMnq,e.toString());
            return "";
        }
    }


    /**
     * 计算百分比
     */
    public static String getPercentage(int All,int surplus){
        NumberFormat numberFormat = NumberFormat.getInstance();
        DecimalFormat df = new DecimalFormat("0.00"); //百分比格式，后面不足2位的用0补齐
        String result = df.format(((float) surplus / (float) All) * 100);
        return result;
    }


    private static MCLoadDialog loadDialog;
    //显示加载弹窗
    public static void ShowLoadDialog(Context context){
        loadDialog = new MCLoadDialog(context,MCHInflaterUtils.getIdByName(context, "style", "mch_MyDialogStyle"));
        loadDialog.show();
    }

    //关闭加载弹窗
    public static void DissLoadDialog(){
        if (loadDialog!=null && loadDialog.isShowing()){
            loadDialog.dismiss();
        }
    }


    /**
     * 验证校验码，加载页面链接
     * @param authCode
     * @param pageUrl
     */
    public static void loadUrl(Activity activity,String authCode,String pageUrl){
        String url = MCHConstant.getInstance().getPlatformDomain() + pageUrl;
        String base64AuthCode = android.util.Base64.encodeToString(authCode.getBytes(), android.util.Base64.DEFAULT);
        String base64URL = android.util.Base64.encodeToString(url.getBytes(), Base64.DEFAULT);
        String location = MCHConstant.getInstance().getCheckAuth() + "/auth_code/" + base64AuthCode  + "/redirect_url/" + base64URL;

        WebViewUtil.webView(activity, location);
    }

    /**
     * 得到图片字节流 数组大小
     * */
    public static byte[] readStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = inStream.read(buffer)) != -1){
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    //小时转换成秒
    public static double getDouble(String str) {
        if (!TextUtils.isEmpty(str)) {
            return Double.parseDouble(str) * 60 * 60;
        } else {
            return 0.00;
        }
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground(Activity activity) {
        ActivityManager activityManager = (ActivityManager) activity.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = activity.getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 倒计时
     */
    public static void getTiming(final TextView textView){
        textView.setEnabled(false);
        CountDownTimer timer = new CountDownTimer(60*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                String str = millisUntilFinished/1000+"s";
                SpannableStringBuilder style=new SpannableStringBuilder(str);
                if (String.valueOf(millisUntilFinished/1000).length()>1){
                    style.setSpan(new ForegroundColorSpan(Color.parseColor("#2089FE")),0,3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else {
                    style.setSpan(new ForegroundColorSpan(Color.parseColor("#2089FE")),0,2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                textView.setText(style);
            }

            @Override
            public void onFinish() {
                textView.setText(UtilsConstants.S_xExbltTqqS);
                textView.setEnabled(true);
            }
        }.start();
    }

}
