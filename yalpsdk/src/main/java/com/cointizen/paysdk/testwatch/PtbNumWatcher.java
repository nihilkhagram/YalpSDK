package com.cointizen.paysdk.testwatch;

import java.util.regex.Pattern;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.cointizen.open.FlagControl;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.MCMoneyUtils;

public class PtbNumWatcher implements TextWatcher {

	private static final String TAG = "PtbNumWatcher";
	private final Activity activity;

	private TextView txtPayRmb;//支付人民币
	private TextView btnAddPtb;
	private EditText edt;//平台币输入框

	public PtbNumWatcher(Activity mcPayPTBActivity, TextView txtPayRmb, TextView btnAddPtb, EditText edt){
		activity = mcPayPTBActivity;
		this.txtPayRmb = txtPayRmb;
		this.btnAddPtb = btnAddPtb;
		this.edt = edt;
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {}

	@Override
	public void afterTextChanged(Editable s) {
		if(null != s){
			String txt = s.toString().trim();
//			if(!TextUtils.isEmpty(txt) && isNumeric(txt)){
				if(!TextUtils.isEmpty(txt)){
				try {
//					String ptbStr = getPayRmb(txt);
					showPayResult(txt);
				} catch (NumberFormatException e) {
					showPayResult("0");
					MCLog.e(TAG, "#afterTextChanged e="+e);
				}
			}else{
//				MCLog.e(TAG, "fun#afterTextChanged -----");
				showPayResult("0");
//				if(!isNumeric(txt)){
//				}
			}
		}
		if(!FlagControl.BUTTON_CLICKABLE) {
			FlagControl.BUTTON_CLICKABLE = true;
		}
	}

	private String getPayRmb(String ptbStr) {
		int point = ptbStr.indexOf(".");
//		MCLog.e(TAG, "fun#getPayRmb point = " + point);
		if((point > 0 && point < 8) || 
				(point< 0 && ptbStr.length() <= 8)){
			float ptb = Float.parseFloat(ptbStr);
			return String.format("%.2f", ptb);
		}
//		if(point > 0){
//			if(point < 8){
//				float ptb = Float.parseFloat(ptbStr);
//				return String.format("%.2f", ptb);
//			}
//		}else{
//			if(ptbStr.length() <= 8){
//				float ptb = Float.parseFloat(ptbStr);
//				return String.format("%.2f", ptb);
//			}
//		}
		return "0";//			System.out.println(pattern.matcher(str).matches());

    }

	public boolean isNumeric(String str){ 
		if(str.length() >= 5){
			str = str.substring(0, str.length() - 1);
			edt.setText(str);
			return false;
		}else {
//			  Pattern pattern = Pattern.compile("[0-9.]*");
			Pattern pattern = Pattern.compile("[0-9]*");
			return pattern.matcher(str).matches();
		}
	}
	
	private void showPayResult(String res) {
		if(null != txtPayRmb){
			txtPayRmb.setText(res);
		}
		float v = MCMoneyUtils.priceToFloat(res);
		if(null != btnAddPtb && v==0){
			btnAddPtb.setEnabled(false);
			btnAddPtb.setBackgroundResource(MCHInflaterUtils.getDrawable(activity,"mch_btn_huise_bg"));
			txtPayRmb.setText("0");
		}else{
			btnAddPtb.setEnabled(true);
			FlagControl.BUTTON_CLICKABLE = true;
			btnAddPtb.setBackgroundResource(MCHInflaterUtils.getDrawable(activity,"mch_btn_lan_bg"));
		}
	}

}
