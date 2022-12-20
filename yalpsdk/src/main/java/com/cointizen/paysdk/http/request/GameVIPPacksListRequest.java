package com.cointizen.paysdk.http.request;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.GamePackInfo;
import com.cointizen.paysdk.entity.PacksInfo;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.TimeConvertUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import com.cointizen.paysdk.http.HttpConstants;

public class GameVIPPacksListRequest {
    private static final String TAG = "GameTeQuanPacksListRequest";

    HttpUtils http;
    Handler mHandler;

    public GameVIPPacksListRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");

            noticeResult(Constant.GET_TEQUAN_LIST_FAIL, HttpConstants.S_VKHKJPHQds);
            return;
        }

        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.GET_TEQUAN_LIST_FAIL, HttpConstants.S_uVIIKmGFwV);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                PacksInfo packsInfo = new PacksInfo();
                try {
                    JSONObject json = new JSONObject(response);
                    int status = json.optInt("code");

                    if (status == 200) {
                        packsInfo.setStatus("1");
                        List<GamePackInfo> packInfoList = getGamePackList(json);
                        packsInfo.setPackInfoList(packInfoList);
                        noticeResult(Constant.GET_TEQUAN_LIST_SUCCESS, packsInfo);
                    } else {
                        String msg = json.optString("msg");
                        if (TextUtils.isEmpty(msg)) {
                            msg = HttpConstants.S_rLrNAHMNhs;
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.GET_TEQUAN_LIST_FAIL, msg);
                    }
                } catch (Exception e) {
                    MCLog.e(TAG, HttpConstants.Log_eccGLkHjiX + e);
                    noticeResult(Constant.GET_TEQUAN_LIST_FAIL, HttpConstants.S_bcwiMnfyeK);
                }
            }
        });
    }


    protected List<GamePackInfo> getGamePackList(JSONObject json) {
        if (null == json) {
            return null;
        }
        List<GamePackInfo> packInfoList = new ArrayList<GamePackInfo>();
        try {
            JSONArray jsonArray = json.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {// 遍历JSONArray
                GamePackInfo packInfo = new GamePackInfo();

                JSONObject jsonPack = jsonArray.getJSONObject(i);
                packInfo.setGiftId(jsonPack.optString("gift_id"));
                packInfo.setPackName(jsonPack.optString("giftbag_name"));

//				packInfo.setGameId(jsonPack.optString("game_id"));
//				packInfo.setGameName(jsonPack.optString("game_name"));
				packInfo.setPackCode(jsonPack.optString("novice"));
                packInfo.setSurplus(jsonPack.optInt("surplus",0));
                packInfo.setPackDesc(jsonPack.optString("digest"));
                packInfo.setPackImageUrl(jsonPack.optString("icon"));
                packInfo.setVipLimit(jsonPack.optInt("vip"));
//				packInfo.setPackStatus(jsonPack.optString("status"));

                packInfo.setPackStatus(jsonPack.optString("received"));
                String mEndTime = jsonPack.optString("end_time");
                if (!mEndTime.equals("0")) {
                    packInfo.setStartTime(TimeConvertUtils.timedate3(jsonPack.optString("start_time")));
                    packInfo.setEndTime(TimeConvertUtils.timedate3(jsonPack.optString("end_time")));
                } else {
                    packInfo.setEndTime("0");
                }
//                long startTime = stringToLong(jsonPack.optString("start_time"));
//                long endTime = stringToLong(jsonPack.optString("end_time"));
//                long nowTime = stringToLong(jsonPack.optString("now_time"));
//                long day = endTime - nowTime;
//                if (day < 0 || (endTime - startTime) < 0) {
//                    packInfo.setEffectiveDates("0");
//                } else {
//                    int dayInt = Integer.parseInt(String.valueOf(day / (3600 * 24)));
//                    packInfo.setEffectiveDates(String.valueOf(dayInt));
//                }

                packInfoList.add(packInfo);
            }
        } catch (JSONException e) {
            MCLog.e(TAG, HttpConstants.Log_FOTjUXBypl + e);
        }

        return packInfoList;
    }

    private long stringToLong(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        long time;
        try {
            time = Long.parseLong(str);
        } catch (NumberFormatException e) {
            time = 0;
        }
        return time;
    }

    private void noticeResult(int type, Object obj) {
        Message msg = new Message();
        msg.what = type;
        msg.obj = obj;

        if (null != mHandler) {
            mHandler.sendMessage(msg);
        }
    }

}
