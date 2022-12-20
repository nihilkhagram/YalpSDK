package com.cointizen.paysdk.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.cointizen.paysdk.BuildConfig;
import com.cointizen.paysdk.bean.SdkDomain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 描述：验证APP  确保一个aar对应一个游戏
 * 时间: 2018-09-19 16:38
 */

public class ConfigureApp {

    private static ConfigureApp instance;
    private static final String TAG = "ConfigureApp";
    private char HEX_DIGITS[] =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private String encode;
    private String ooo = BuildConfig.OOO;
    private Context context;

    public static ConfigureApp getInstance() {
        if (null == instance) {
            instance = new ConfigureApp();
        }
        return instance;
    }


    public boolean Configure(Context context){
        this.context = context;
        if(BuildConfig.oooo){
            boolean encode1 = Encode();
            return Configure1(encode1);
        }
        return true;
    }

    private boolean Configure1(boolean encode1) {
        if(encode1){
            String aPackage = getPackage(encode);
            return Equals(aPackage);
        }
        ToastUtil.showToast(context,MCHInflaterUtils.getIdByName(context,"string","mch_ooooo"));
        return encode1;
    }

    private boolean Equals(String aPackage) {
        MCLog.e(TAG,md5(aPackage)+UtilsConstants.Log_KXtGPFhCNZ+ooo);
        return md5(aPackage).equals(ooo);
    }

    private boolean
    Encode() {
        try {
            encode = URLEncoder.encode(SdkDomain.getInstance().getGameName(), "UTF-8");
            MCLog.e(TAG,UtilsConstants.Log_gjIAmlbhQz+encode);
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            MCLog.e(TAG,e.toString());
        }
        return false;
    }

    private String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getPackage(String encode){
        String packageName = context.getApplicationContext().getPackageName();
        String appSignatureHash = getAppSignatureHash(packageName);
        MCLog.e(TAG,UtilsConstants.Log_gjIAmlbhQz+encode+"  packageName:"+packageName+"  签名："+appSignatureHash.replace(":","").toLowerCase());
        return md5(encode+"&"+packageName+"&"+appSignatureHash.replace(":","").toLowerCase()+"&yalpsdk");
//        return md5(encode+"&"+14+"&asdfasdfasdf&yalpsdk");
    }

    private String getAppSignatureHash(final String packageName) {
        Signature[] signature = getAppSignature(packageName);
        if (signature == null || signature.length <= 0) return "";
        return bytes2HexString(hashTemplate(signature[0].toByteArray(), "MD5"))
                .replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
    }

    private Signature[] getAppSignature(final String packageName) {
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return pi == null ? null : pi.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String bytes2HexString(final byte[] bytes) {
        if (bytes == null) return "";
        int len = bytes.length;
        if (len <= 0) return "";
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = HEX_DIGITS[bytes[i] >> 4 & 0x0f];
            ret[j++] = HEX_DIGITS[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    private byte[] hashTemplate(final byte[] data, final String algorithm) {
        if (data == null || data.length <= 0) return null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
