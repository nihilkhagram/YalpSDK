package com.cointizen.paysdk.view.util;

import java.util.Timer;
import java.util.TimerTask;

import com.cointizen.paysdk.observer.ConcreteTimeChange;
import com.cointizen.paysdk.AppConstants;

/**
 * 倒计时生成
 */
public class TimeUtil {

	protected static final String TAG = "TimeUtil";

	private ConcreteTimeChange timeChange;

	private static TimeUtil timeUtil;
	private int current = 60;
	private Timer mTimer;// 定时器

	public int getType() {
		return -1;
	}

	public static TimeUtil getTimeUtil() {
		if (null == timeUtil) {
			timeUtil = new TimeUtil();
		}
		return timeUtil;
	}

	public TimeUtil() {
		current = 60;
		mTimer = new Timer();
		timeChange = new ConcreteTimeChange();
	}

	public void Start() {
		try {
			if (mTimer != null) {
				mTimer.cancel();// 退出之前的mTimer
				mTimer = null;
			}
			current = 60;
			mTimer = new Timer();// new一个Timer,否则会报错
			timerTask();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	public void calcel() {
		if (mTimer != null) {
			current = 0;
			timeChange.noticeCurrentSeconds(current + "");
			
			mTimer.cancel();// 退出之前的mTimer
			mTimer = null;
		}
	}

	private void timerTask() {
		// 创建定时线程执行更新任务
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				timeChange.noticeCurrentSeconds(current + AppConstants.STR_2135fc14ff8eec3f1c8a50c6fc9ad7f4);
				if (current <= 0) {
					calcel();
				}
				current--;
//				MCLog.e(TAG, "current= " + current);
			}
		}, 1000, 1000);// 定时任务
	}

	public ConcreteTimeChange getTimeChange() {
		return timeChange;
	}

}
