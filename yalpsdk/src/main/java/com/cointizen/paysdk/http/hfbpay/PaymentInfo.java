package com.cointizen.paysdk.http.hfbpay;
import com.cointizen.paysdk.http.HttpConstants;

/**
 * 交易实体
 * 
 * @author Administrator
 * 
 */
public class PaymentInfo {
	// 支付初始化后返回的一个支付码 初始化才返回
	private String tokenID;
	// 商家生成的订单号 初始化才回返回
	private String billNo;
	private String agentId;
	// 返回是否有误
	private boolean hasError;

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	// 返回给前端的错误信息
	private String message;
	// 订单信息，查询接口才回返回
	private String billInfo;

	public String getBillInfo() {
		return billInfo;
	}

	public void setBillInfo(String billInfo) {
		this.billInfo = billInfo;
		this.billNo = this.getBillInfoItem("agent_bill_id");
	}

	public boolean isHasError() {
		return hasError;
	}

	public boolean hasError() {
		return hasError;
	}

	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getTokenID() {
		return tokenID;
	}

	public void setTokenID(String tokenID) {
		this.tokenID = tokenID;
	}

	private String getBillInfoItem(String name) {
		if (billInfo == null)
			return null;

		final String pattern = name + "=";
		int pos = billInfo.indexOf(pattern);
		if (pos < 0) {
			return null;
		}

		int pos2 = billInfo.indexOf('|', pos);
		String val = "";
		if (pos2 >= 0) {
			val = billInfo.substring(pos + pattern.length(), pos2);
		} else {
			val = billInfo.substring(pos + pattern.length());
		}

		return val;
	}

	public int getPayType() {
		String val = getBillInfoItem("pay_type");
		try {
			return Integer.parseInt(val);
		} catch (Exception e) {
			return -1;
		}
	}

	public int getPayResult() {
		String val = getBillInfoItem("result");
		try {
			return Integer.parseInt(val);
		} catch (Exception e) {
			return -1;
		}
	}

	public String getPayAmount() {
		String val = getBillInfoItem("pay_amt");
		return val;
	}

	public String getJunnetBillNo() {
		String val = getBillInfoItem("jnet_bill_no");
		return val;
	}

	public String getPayTypeName() {
		int payType = getPayType();
		switch (payType) {
		case 2:
			return HttpConstants.S_SsfpRsWhkT;
		case 10:
			return HttpConstants.S_VmIxNdprXI;
		case 13:
			return HttpConstants.S_PHnkZaztah;
		case 14:
			return HttpConstants.S_esgGuSTzIN;
		case 15:
			return HttpConstants.S_qaXYRIXWJe;
		case 17:
			return HttpConstants.S_bMkbXZeJqO;
		case 18:
			return HttpConstants.S_fdWmEiwEmB;
		case 19:
			return HttpConstants.S_pQqmpAqNWf;
		case 29:
			return HttpConstants.S_kWRJrOjZhH;
		case 30:
			return HttpConstants.S_EOfecBIssS;
		case 35:
			return HttpConstants.S_WKpmLpymDP;
		case 40:
			return HttpConstants.S_GYvTMqddXl;
		case 41:
			return HttpConstants.S_qKgdxdqFPF;
		case 42:
			return HttpConstants.S_OhtBPMepCX;
		case 43:
			return HttpConstants.S_iJoNNgnvSW;
		case 44:
			return HttpConstants.S_WJhpvlbfbD;
		case 45:
			return HttpConstants.S_LclwIHTEke;
		case 46:
			return HttpConstants.S_AlmFHOnkVl;
		case 47:
			return HttpConstants.S_yHOzFLAaTB;
		case 48:
			return HttpConstants.S_BdELROCzGG;
		case 49:
			return HttpConstants.S_OMQRoBTeUx;
		case 50:
			return HttpConstants.S_WQsYajWXgD;
		case 51:
			return HttpConstants.S_piGUhLayDJ;
		case 52:
			return HttpConstants.S_eysTcQsxXX;
		case 53:
			return HttpConstants.S_yKnsfhIyCU;
		case 54:
			return HttpConstants.S_MQPygvvuhk;
		case 55:
			return HttpConstants.S_FjXZHByNov;
		case 56:
			return HttpConstants.S_BxeXKsboEb;
		case 57:
			return HttpConstants.S_RJaqLQcpdd;
		default:
			return HttpConstants.S_MxgfJhqCOZ;
		}
	}

	public String getPayResultName() {
		int status = getPayResult();
		return getPayResultName(status);
	}

	public static String getPayResultName(int status) {
		String resultText = "";
		switch (status) {
		case 0:
			resultText = HttpConstants.S_cAdLMDJCSD;
			break;
		case -1:
			resultText = HttpConstants.S_svXbkGXNmg;
			break;
		case 1:
			resultText = HttpConstants.S_YZnrqrqZVi;
			break;
		case -2:
			resultText = HttpConstants.S_mKaWeFBksZ;
			break;
		}
		return resultText;
	}
}
