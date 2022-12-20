package com.cointizen.paysdk.bean;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.cointizen.open.FlagControl;
import com.cointizen.open.IGPSDKInitObsv;
import com.cointizen.paysdk.bean.privacy.PrivacyManager;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.process.DeviceDownProcess;
import com.cointizen.paysdk.http.process.GetBackgroundVersionProcess;
import com.cointizen.paysdk.http.process.SwitchStatusProcess;
import com.cointizen.paysdk.http.switchinfo.SwitchInfoProcess;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.emulator.EmulatorCheckUtil;

public class InitModel {
	private static final String TAG = "InitModel";

	private static InitModel ins;

	private boolean isSimulator = false;
	private IGPSDKInitObsv mInitCallback;

	public synchronized static InitModel init(){
		if(null == ins){
			ins = new InitModel();
		}
		return ins;
	}
	private InitModel() {
	}

	public void doInit(Context context, final IGPSDKInitObsv initObsv) {
		MCLog.i(TAG, "init model do init start");
//		FWPay.initialize((Activity)context, MCLog.isDebug);
		this.mInitCallback = initObsv;
		SdkDomain.getInstance().init(context);

		new GetBackgroundVersionProcess(context).post(handler);

		MCLog.w(TAG, "init model do init end." + SdkDomain.getInstance().toString());

		isSimulator = EmulatorCheckUtil.getSingleInstance().readSysProperty(context, null);

		new SwitchInfoProcess().post(null);
		PrivacyManager.getInstance().queryAgreeInfo(false);
	}

	public void initCallback(int result) {
		if (mInitCallback != null) {
			mInitCallback.onInitFinish(result);
		}
	}

	/**
	 * 用户下线
	 */
	public void offLine(Activity activity, boolean isLogout) {
		if (FlagControl.isLogin && Constant.userIsOnLine) {

			OffLineAnnounceModel offLineAnnounceModel = new OffLineAnnounceModel(activity);
			offLineAnnounceModel.offLineAnnouce();
		}

		DeviceDownProcess deviceDownProcess = new DeviceDownProcess(activity);
		deviceDownProcess.post();
	}

	Handler handler = new Handler(Looper.getMainLooper()){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case Constant.GET_BACKGROUND_VERSION_SUCCESS:
					Constant.MCH_BACKGROUND_VERSION = (int) msg.obj;
					if (Constant.MCH_BACKGROUND_VERSION >= Constant.VERSION_920){ //9.2.0版本获取功能开关
						new SwitchStatusProcess().post(handler);
					}
					break;
				case Constant.GET_BACKGROUND_VERSION_FAIL:
					Constant.MCH_BACKGROUND_VERSION = 0;
					break;

				case Constant.SWITCH_STATUS_SUCCESS:  //获取功能开关成功
					break;
				case Constant.SWITCH_STATUS_FAIL:
					break;
			}
			Log.i("sdk:" + TAG,"网络请求到的服务端版本号：" + Constant.MCH_BACKGROUND_VERSION);
		}
	};

	public boolean isSimulator() {
		return isSimulator;
	}
}
