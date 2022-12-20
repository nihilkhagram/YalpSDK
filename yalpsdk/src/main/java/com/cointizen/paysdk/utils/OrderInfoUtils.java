package com.cointizen.paysdk.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.cointizen.paysdk.config.MCHConstant;

public class OrderInfoUtils {

	private static final String TAG = "OrderInfoUtils";
	//静态常量换成静态方法，修正设置测试参数中key不起作用
    //public static String MCH_MD5KEY = MCHConstant.getInstance().getMCHKEY();// 和服务器验证用的md5key
	public static String MCH_MD5KEY(){
		return MCHConstant.getInstance().getMCHKEY();
	}
	/**
	 * 创建支付宝订单
	 * @param zfbshopid 签约合作者身份ID
	 * @param zfbshopname  签约卖家支付宝账号
	 * @param goodsName  商品名称
	 * @param body 商品详情
	 * @param price 商品金额
	 * @param zfbansynoticeurl 服务器异步通知页面路径
	 * @return
	 */
	public static String createZFBOrderInfo(String zfbshopid, String zfbshopname, String tradeNo,
			String goodsName, String body, String price, String zfbansynoticeurl) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + zfbshopid + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + zfbshopname + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + tradeNo + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + goodsName + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\""
		// + "http://tui.vlcms.com/server.php/CallBack/alipaycallback" + "\"";
				+ zfbansynoticeurl + "\"";
		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";
		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";
		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";
		
		MCLog.w(TAG, "fun#createZFBOrderInfo orderInfo = " + orderInfo);
		return orderInfo;
	}
	
	/**
	 * -随机订单号
	 * @return 订单号
	 */
	public static String getRandomNumber() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date(System.currentTimeMillis());
		String key = format.format(date);
		MCLog.w(TAG, "fun#getRandomNumber key = " + key);

//		Random r = new Random();
//		key = key + r.nextInt();
		key = key + (int)((Math.random()*9+1)*100000);
		MCLog.w(TAG, "fun#getRandomNumber key = " + key);
		key = key.substring(0, 15);
		MCLog.w(TAG, "fun#getRandomNumber key = " + key);
		return key;
	}
	
}
