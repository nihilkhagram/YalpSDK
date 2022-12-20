package com.cointizen.paysdk.utils;

import java.math.BigDecimal;


public class MCMoneyUtils {

	
	private static final String TAG = "MCMoneyUtils";
	

	public static float priceToFloat(String price){
		float p = 0;
		try {
			p = Float.parseFloat(price);
		} catch (NumberFormatException e) {
			MCLog.e(TAG, "fun#priceToFloat NumberFormatException:" + e);
		}
		return p;
	}

	/**
	 * double ����ȡ��
	 * 
	 * @param d
	 *            ԭ����
	 * @param n
	 *            ��ȷ��С�����nλ
	 * @return
	 */
	public static double getCeil(double d, int n) {
		BigDecimal b = new BigDecimal(String.valueOf(d));
		b = b.divide(BigDecimal.ONE, n, BigDecimal.ROUND_CEILING);
		return b.doubleValue();
	}
	/**
	 * �����ۿ�֮�����Ǯ
	 * 
	 * @param old
	 *            ԭ�۸�Ԫ��
	 * @return (Ԫ)
	 * @discount �ۿۣ���5.2�ۣ�
	 */
	public static String real(String old, String discount) {
		try {
			Double d1 = Double.parseDouble(old);
			Double d2 = Double.parseDouble(discount);
			Double d3 = d2 * d1;
			String real = (d3 / 10) + "";
			// System.out.println("d3/10 = "+d3/10);
			// System.out.println("d3*0.1 = "+d3*0.1);
			// d3 = 6.0
			// d3/10 = 0.6
			// d3*0.1 = 0.6000000000000001
			return real;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * �ۿ�֮��۸�
	 * 
	 * @param old
	 * @param discount
	 * @return
	 */
	public static String re(String old, String discount) {
		try {
			String real = real(old, discount);
			Double dreal = Double.parseDouble(real);
			dreal = getCeil(dreal, 2);
			return dreal.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
