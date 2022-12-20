package com.cointizen.paysdk.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cointizen.paysdk.adapter.MCHGameRecordAdapter;
import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.bean.UserReLogin;
import com.cointizen.paysdk.bean.UserReLogin.ReLoginCallback;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.GameRecordEntity;
import com.cointizen.paysdk.http.process.GameRecordListProcess;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.view.xlist.XListView;
import com.cointizen.paysdk.view.xlist.XListView.IXListViewListener;

/**
 * 游戏充值记录
 * 
 * @author Administrator
 *
 */
public class MCHGameRecordActivity extends MCHBaseActivity implements
		IXListViewListener {

	private final static String TAG = "MCGameRecordActivity";

	/**
	 * 无充值记录提示
	 */
	TextView txtRecordTip;

	/**
	 * 标题
	 */
	LinearLayout llRedordTitle;

	TextView txtAccount;
	
	TextView txtGameName;

	/**
	 * 充值记录列表
	 */
	private List<GameRecordEntity> gameRecordList = new ArrayList<GameRecordEntity>();

	private XListView xListView;// 显示数据
	private MCHGameRecordAdapter recordAdapter;

	private Handler mHandler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.GAME_RECODE_SUCCESS:
				GameRecordEntity gameRecordEntity = (GameRecordEntity) msg.obj;
				handlerRecordList(gameRecordEntity);
				break;
			case Constant.GAME_RECODE_FAIL:
				String tipStr = (String) msg.obj;
				if (TextUtils.isEmpty(tipStr)) {
					tipStr = ActivityConstants.S_WBHCQICYeM;
				}
				Toast.makeText(MCHGameRecordActivity.this, tipStr,
						Toast.LENGTH_SHORT).show();
				txtRecordTip.setVisibility(View.VISIBLE);
				xListView.setVisibility(View.GONE);
				llRedordTitle.setVisibility(View.GONE);
				break;
			default:
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayout("mch_activity_game_record"));

		initView();
		initData();
	}

	/**
	 * 刷新游戏充值记录列表
	 * 
	 */
	protected void handlerRecordList(GameRecordEntity gameRecordEntity) {
		if (gameRecordEntity.getGameRecordes().size()!=0) {
			MCLog.e(TAG, "fun#handlerRecordList  size = "
					+ gameRecordEntity.getGameRecordes().size());
			gameRecordList.addAll(gameRecordEntity.getGameRecordes());
			recordAdapter.notifyDataSetChanged();
		} else {
			txtRecordTip.setVisibility(View.VISIBLE);
			xListView.setVisibility(View.GONE);
			llRedordTitle.setVisibility(View.GONE);
		}
	}

	/**
	 * 获取充值记录
	 */
	private void initData() {
		if (TextUtils.isEmpty(UserLoginSession.getInstance().getUserId())) {
			txtRecordTip.setVisibility(View.VISIBLE);
			xListView.setVisibility(View.GONE);
			llRedordTitle.setVisibility(View.GONE);

			UserReLogin reLogin = new UserReLogin(MCHGameRecordActivity.this);
			reLogin.userToLogin(new ReLoginCallback() {

				@Override
				public void reLoginResult(boolean res) {
					if (res) {
						showRecodeList();
					} else {
						Toast.makeText(MCHGameRecordActivity.this,
								ActivityConstants.S_DefHZgLfwx, Toast.LENGTH_SHORT).show();
						finish();
					}
				}
			});
			return;
		}
		showRecodeList();
	}

	private void showRecodeList() {
		StringBuilder accountTxt = new StringBuilder();
		accountTxt.append(ActivityConstants.S_nwviDDDbZf).append(
				UserLoginSession.getInstance().getAccount());
		txtAccount.setText(accountTxt.toString());
		StringBuilder gameNameTxt = new StringBuilder();
		gameNameTxt.append(ActivityConstants.S_PyIETEnFxT).append(
				UserLoginSession.getInstance().getGameName());
		MCLog.e(TAG, "accountTxt:" + accountTxt.toString() + " gameNameTxt:"
				+ gameNameTxt.toString());
		txtGameName.setText(gameNameTxt.toString());

		new GameRecordListProcess().post(mHandler);
	}

	/**
	 * 初始化方法
	 */
	private void initView() {
		TextView txtTitle = (TextView) findViewById(getId("tv_mch_header_title"));
		txtTitle.setText(ActivityConstants.S_GEuzSeLpSY);
		ImageView ivBack = (ImageView) findViewById(getId("iv_mch_header_back"));
		ivBack.setVisibility(View.VISIBLE);
		ivBack.setOnClickListener(backClick);
		ImageView ivClose = (ImageView) findViewById(getId("iv_mch_header_close"));
		ivClose.setVisibility(View.GONE);

		llRedordTitle = (LinearLayout) findViewById(getId("ll_mch_redord_title"));
		llRedordTitle.setVisibility(View.VISIBLE);
		txtAccount = (TextView) findViewById(getId("txt_mch_redord_account"));
		txtGameName = (TextView) findViewById(getId("txt_mch_redord_gamename"));

		txtRecordTip = (TextView) findViewById(getId("txt_mch_redord_tip"));
		txtRecordTip.setVisibility(View.GONE);

		xListView = (XListView) findViewById(getId("xlistview_mch_record"));
		xListView.setVisibility(View.VISIBLE);
		recordAdapter = new MCHGameRecordAdapter(getApplicationContext(),
				gameRecordList);
		xListView.setAdapter(recordAdapter);
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(false);
		xListView.setXListViewListener(this);
		xListView.pullRefreshing();
		xListView.setDividerHeight(1);
	}

	/**
	 * 退出监听
	 */
	OnClickListener backClick = new OnMultiClickListener() {

		@Override
		public void onMultiClick(View v) {
			finish();
		}
	};

	/**
	 * 停止刷新,重置标题视图
	 */
	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				xListView.stopRefresh();
			}
		}, 1000);
	}

	@Override
	public void onLoadMore() {

	}
}
