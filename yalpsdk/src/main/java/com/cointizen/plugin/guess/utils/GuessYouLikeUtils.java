package com.cointizen.plugin.guess.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cointizen.plugin.guess.http.process.GuessYouLikeProcess;
import com.lidroid.xutils.BitmapUtils;
import com.cointizen.paysdk.activity.MCHUserCenterActivity;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.AppUtils;
import com.cointizen.paysdk.utils.BitmapHelp;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.TextUtils;
import com.cointizen.paysdk.view.gifimageview.GifImageView;
import com.cointizen.plugin.guess.bean.GuessYouLikeModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import com.cointizen.plugin.AppConstants;

/**
 * 描述：猜你喜欢工具类
 * 时间: 2018-10-24 9:40
 */

public class GuessYouLikeUtils {

    private static GuessYouLikeUtils instance;
    private View layoutGuess;
    private HorizontalScrollView recyclerviewHorizontal;
    private LinearLayout gameInfoImagesLayout;
    private MCHUserCenterActivity mcUser;

    public boolean isShowGuessYouLike() {
        return showGuessYouLike;
    }

    private boolean showGuessYouLike = false;

    public static GuessYouLikeUtils getInstance() {
        if (null == instance) {
            instance = new GuessYouLikeUtils();
        }
        return instance;
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.GUESS_SUCCESS:
                    GuessYouLikeModel obj = (GuessYouLikeModel) msg.obj;
                    List<GuessYouLikeModel.ListBean> list = obj.getList();
                    if(list != null && list.size() > 0 ){
                        showGuessYouLike = true;
                        layoutGuess.setVisibility(View.VISIBLE);
                        BitmapUtils bitmapUtils = BitmapHelp.getBitmapUtils(mcUser);
                        initHorizontalSorollView(list,bitmapUtils);
                    }else {
                        showGuessYouLike = true;
                        layoutGuess.setVisibility(View.GONE);
                    }
                    break;
                case Constant.GUESS_FAIL:
                    break;
            }
        }
    };

    public void setCallback(View layoutGuess, HorizontalScrollView recyclerviewHorizontal,
                            LinearLayout gameInfoImagesLayout, MCHUserCenterActivity mcUserCenterActivity) {
        this.layoutGuess = layoutGuess;
        this.recyclerviewHorizontal = recyclerviewHorizontal;
        this.gameInfoImagesLayout = gameInfoImagesLayout;
        this.mcUser = mcUserCenterActivity;
        GuessYouLikeProcess guessYouLikeProcess = new GuessYouLikeProcess();
        guessYouLikeProcess.post(handler, mcUserCenterActivity);
    }


    /**
     * 横向滑动的游戏截图
     * @param list
     * @param bitmapUtils
     */
    private void initHorizontalSorollView(final List<GuessYouLikeModel.ListBean> list, BitmapUtils bitmapUtils) {
        try {
            if (list.size()>0){
                LayoutInflater inflater = (LayoutInflater)mcUser.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                int layout = MCHInflaterUtils.getLayout(mcUser, "mch_item_guess");
                for (int i = 0; i < list.size(); i++) {
                    View inflate = inflater.inflate(layout, null);
                    final GifImageView icon = inflate.findViewById(MCHInflaterUtils.getIdByName(mcUser, "id", "img_mch_icon"));
                    TextView tvName = inflate.findViewById(MCHInflaterUtils.getIdByName(mcUser, "id", "tv_mch_name"));
                    View layoutItem = inflate.findViewById(MCHInflaterUtils.getIdByName(mcUser, "id", "layout_item"));
                    ViewGroup.LayoutParams layoutParams = icon.getLayoutParams();
//                    layoutParams.height = AppUtils.dip2px(mcUser,50f);
//                    layoutParams.width = AppUtils.dip2px(mcUser,50f);
//                    MCLog.e(AppConstants.LOG_5d2eae897b9da0f167e90784a955d7a5,AppUtils.dip2px(mcUser,50f)+"");
                    icon.setLayoutParams(layoutParams);
//                    icon.setCornerRadius(10);
                    final GuessYouLikeModel.ListBean listBean = list.get(i);

                    if (!TextUtils.isEmpty(listBean.getIcon())){
                        String s = listBean.getIcon().substring(listBean.getIcon().length() - 4, listBean.getIcon().length());
                        if (s.equals(".gif") || s.equals(".GIF")){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    setFloatingPic(icon,listBean.getIcon());
                                }
                            }).start();
                        }else {
                            bitmapUtils.display(icon,listBean.getIcon());
                        }
                    }

                    tvName.setText(listBean.getTitle());
                    layoutItem.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            AppUtils.openBrowser(mcUser,listBean.getUrl());
                        }
                    });
                    gameInfoImagesLayout.addView(inflate);
                }
            }
        } catch (Exception e) {
            MCLog.e(AppConstants.LOG_cd42848ef53718aeb173bb2b27fa5bc7, e.toString());
        }
    }


    /**
     * 加载网络图片
     */
    private void setFloatingPic(GifImageView imgIcon,String logo) {
        InputStream is = null;
        try {
            URL url = new URL(logo);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                imgIcon.setBytes(AppUtils.readStream(is));
                imgIcon.startAnimation();
            }
        } catch (Exception e) {
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

}
