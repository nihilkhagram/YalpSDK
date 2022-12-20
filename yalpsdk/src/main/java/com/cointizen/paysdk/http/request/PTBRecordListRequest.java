package com.cointizen.paysdk.http.request;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.AddPtbEntity;
import com.cointizen.paysdk.entity.AddPtbRecordEntity;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.cointizen.paysdk.http.HttpConstants;

public class PTBRecordListRequest {

    private static final String TAG = "PTBRecordListRequest";

    HttpUtils http;
    Handler mHandler;

    public PTBRecordListRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null");
            noticeResult(Constant.RECORD_LIST_FAIL, null, HttpConstants.S_ElwWiRoGqB);
            return;
        }
        MCLog.e(TAG, "fun#post url = " + url);
        //设置当前请求的缓存时间
        http.configCurrentHttpCacheExpiry(0);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                AddPtbRecordEntity addPtbRecordEntity = new AddPtbRecordEntity();
                int status;

                try {
                    JSONObject json = new JSONObject(response);
                    status = json.optInt("code");

//					addPtbRecordEntity.setStatus(status);
                    if (status == 200) {//  支付宝充平台币成功
                        addPtbRecordEntity.setTotal(json.optInt("total", 0));
                        addPtbRecordEntity.setAddPtbList(getGamePackList(json));
                        noticeResult(Constant.RECORD_LIST_SUCCESS, addPtbRecordEntity, "");
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("msg"))) {
                            msg = json.optString("msg");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(json.optInt("code"));
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.RECORD_LIST_FAIL, null, msg);
                    }
                } catch (JSONException e) {
                    noticeResult(Constant.RECORD_LIST_FAIL, null, HttpConstants.S_bcwiMnfyeK);
                    MCLog.e(TAG, "fun#post  JSONException:" + e);
                } catch (Exception e) {
                    noticeResult(Constant.LOGIN_FAIL, null, HttpConstants.S_bcwiMnfyeK);
                    MCLog.e(TAG, HttpConstants.Log_ChrQvUhomg + e);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.RECORD_LIST_FAIL, null, HttpConstants.S_uVIIKmGFwV);
            }
        });
    }

    /**
     * 0:平台币,1:支付宝,2:微信(扫码)3微信app 4 威富通 5聚宝云
     */
    private List<AddPtbEntity> getGamePackList(JSONObject json) {
        if (null == json) {
            return null;
        }
        List<AddPtbEntity> addPtbList = new ArrayList<AddPtbEntity>();
        try {
            JSONObject jsonArray = json.getJSONObject("data");
            JSONArray lists = jsonArray.getJSONArray("lists");
            AddPtbEntity addPtb;
            JSONObject jsonPack;
            for (int i = 0; i < lists.length(); i++) {// 遍历JSONArray
                addPtb = new AddPtbEntity();
                jsonPack = lists.getJSONObject(i);
                addPtb.setBlannce(jsonPack.optString("pay_amount", ""));
                addPtb.setAddPtbTime(jsonPack.optString("pay_time", ""));
                String payType = jsonPack.optString("pay_way", "");
//                String payStr = HttpConstants.S_QztrTFNKQG;
//                if ("0".equals(payType)) {
//                    payStr = HttpConstants.S_DnZaxzgjuD;
//                } else if ("1".equals(payType)) {
//                    payStr = HttpConstants.S_kDJixLzwlk;
//                } else if ("2".equals(payType)) {
//                    payStr = HttpConstants.S_GDsjCBdEXi;
//                } else if ("3".equals(payType)) {
//                    payStr = HttpConstants.S_lZwMGKWJKB;
//                } else if ("4".equals(payType)) {
//                    payStr = HttpConstants.S_rhHxEKZKqJ;
//                } else if ("5".equals(payType)) {
//                    payStr = HttpConstants.S_GhfjeYJxkf;
//                }
                addPtb.setBuyPTBType(payType);
                addPtbList.add(addPtb);
//                if("1".equals(jsonPack.optString("pay_status", ""))){//如过Payment done successfully，显示在充值记录界面上
//					addPtbList.add(addPtb);
//                }
            }
        } catch (JSONException e) {
            addPtbList = null;
            MCLog.e(TAG, "fun#getGamePackList  JSONException:" + e);
        }

        return addPtbList;
    }

    private void noticeResult(int type, AddPtbRecordEntity addPtbRecordEntity, String str) {
        Message msg = new Message();
        msg.what = type;
        if (Constant.RECORD_LIST_SUCCESS == type) {
            msg.obj = addPtbRecordEntity;
        } else {
            msg.obj = str;
        }
        if (null != mHandler) {
            mHandler.sendMessage(msg);
        }
    }
}
