package com.cointizen.paysdk.common;

import android.graphics.Bitmap;

import java.util.regex.Pattern;

public class Constant {

    public static final String FRAGMENT_TAG = "yalpsdk_fragment";
    public static final String PRE_NAME = "userInfo";
    public static final String CUSTOMER = "account";
    public static final String PASSWORD = "password";
    public static final String CUSTOMER_YK = "ykaccount";
    public static final String CUSTOMER_YK_PASSWORD = "ykpassword";
    public static String AGREEMENT_NAME = ""; //注册协议名字
    public static String QQ_appid = "";
    public static boolean IsOpenSmallAccount = false;

    public static String ShareUrl = "";
    public static String ShareTitle;
    public static String ShareLogo;
    public static Bitmap ShareBitmap;
    public static String ShareZY;
    public static String TiXian;
    public static int LoginType;  //   1 账号登录     2第三方登录    3 游客登录

    public static int count = 1;    // 1 已读
    public static int request_count;    //请求未读消息数
    public static int notice_count;    //通知未读消息数
    public static Bitmap XFBitmap;    //悬浮球图片

    public static boolean deviceIsOnLine = false; //设备是否已经在线

//    public static boolean canUserOnLine = true;    //是否可以请求用户上线
//    public static boolean canUserOffLine = false;    //是否可以请求用户下线
    public static boolean userIsOnLine = false; //用户是否已经在线
    public static boolean showedNoteDialog = false; //是否显示过了公告弹窗

    public static int CHOOSE_COUPON_CODE = 0x01;
    public static int COUPON_OK_CODE = 0x02;

    //版本控制，用户兼容旧版本客户
    public static int MCH_BACKGROUND_VERSION; //当前域名服务端的版本号
    public static int VERSION_840 = 840;  //8.4.0版本新增：签到、余额积分、特权礼包
    public static int VERSION_920 = 920;  //9.2.0版本新增：拆红包入口

    public static int RED_BAG_STATUS; //领红包开关
    public static String SDK_LOGO_URL = "";

    /**
     * 微信appid
     */
    public static String WXAPPID = "";

    /**
     * 6-15位数字或英文字母
     */
    public static final String REGULAR_ACCOUNT = "^[a-zA-Z0-9_]{6,15}$";
    /**
     * 手机号码
     */
    public static final String REGULAR_PHONENUMBER = "^1[0-9]{10}$";
    public static String WX_appid = "";


    /**
     * 校验邮箱
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean REGULAR_MAIL(String email) {
        return Pattern.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", email);
    }
    /**
     * 校验邮箱
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean REGULAR_MAIL2(String email) {
        return Pattern.matches(CommonConstants.S_cziPdcumXS +
                "(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$", email);
    }

    /**
     * 正则：身份证号码15位
     */
    public static final String REGEX_ID_CARD15 = CommonConstants.S_wTsfHFVJWG;
    /**
     * 正则：身份证号码18位
     */
    public static final String REGEX_ID_CARD18 = CommonConstants.S_piYOaVziiI;

    /**
     * 正则: 判断是否是中文人名，支持少数名族
     */
    public static final String REGULAR_NAME = "[\\u4E00-\\u9FA5]{2,25}(?:·[\\u4E00-\\u9FA5]{2,5})*";

    /**
     * 登陆成功
     */
    public static final int HTTP_REQUEST_FAIL = 0x00;

    /**
     * 登陆成功
     */
    public static final int LOGIN_SUCCESS = 0x01;

    /**
     * 登陆失败
     */
    public static final int LOGIN_FAIL = 0x02;


    /**
     * 注册成功
     */
    public static final int REGISTER_SUCCESS = 0x03;

    /**
     * 注册失败
     */
    public static final int REGISTER_FAIL = 0x04;

    public static final int PRIVACY_SUCCESS = 0x05;

    public static final int DELETE_ACCOUNT_SUCCESS = 0x06;

    /**
     * 支付宝请求成功
     */
    public static final int ZFB_PAY_VALIDATE_SUCCESS = 0x07;

    /**
     * 支付宝请求失败
     */
    public static final int ZFB_PAY_VALIDATE_FAIL = 0x08;

    /**
     * 支付宝App返回
     */
    public static final int SDK_PAY_FLAG = 0x09;

    /**
     * 支付验证成功
     */
    public static final int VERIFICATION_SUCCESS = 0x10;

    /**
     * 支付验证失败
     */
    public static final int VERIFICATION_FAIL = 0x11;
    public static final int PRIVACY_STATUS_CLOSE = 0x12;
    public static final int LOGOUT_VERIFY = 0x13;
    public static final int LOGOUT_SEND_SMS = 0x14;

    /**
     * 平台币Payment done successfully
     */
    public static final int YLPD_PAY_SUCCESS = 0x16;

    /**
     * 平台币Payment failed
     */
    public static final int YLPD_PAY_FAIL = 0x17;

    /**
     * 平台币余额
     */
    public static final int YLPD_BALANCE_QUERY_SUCCESS = 0x18;
    public static final int YLPD_BALANCE_QUERY_FAIL = 0x19;

    /**
     * 修改密码
     */
    public static final int MODIFY_PASSWORD_SUCCESS = 0x20;
    public static final int MODIFY_PASSWORD_FAIL = 0x21;

    /**
     * 微富通订单获取
     */
    public static final int WFT_ORDERINFO_SUCCESS = 0x22;
    public static final int WFT_ORDERINFO_FAIL = 0x23;
    /**
     * 获取礼包列表
     */
    public static final int GET_PACKS_LIST_SUCCESS = 0x24;
    public static final int GET_PACKS_LIST_FAIL = 0x25;

    /**
     * 获取用户信息
     */
    public static final int GET_USER_INFO_SUCCESS = 0x43;
    public static final int GET_USER_INFO_FAIL = 0x26;

    /**
     * 修改昵称
     */
    public static final int UPDATE_NIKE_SUCCESS = 0x27;
    public static final int UPDATE_NIKE_FAIL = 0x28;

    public static final int AWARD_SUCCESS = 0x29;

    /**
     * 获取验证码
     */
    public static final int VERIFYCODE_REQUEST_SUCCESS = 0x31;
    public static final int VERIFYCODE_REQUEST_FAIL = 0x32;

    /**
     * 绑定手机
     */
    public static final int USER_BIND_PHONE_SUCCESS = 0x33;
    public static final int USER_BIND_PHONE_FAIL = 0x34;


    /**
     * 获取充值记录
     */
    public static final int RECORD_LIST_SUCCESS = 0x37;
    public static final int RECORD_LIST_FAIL = 0x38;

    /**
     * 获取礼包码
     */
    public static final int PACKS_CODE_SUCCESS = 0x39;
    public static final int PACKS_CODE_FAIL = 0x40;

    /**
     * 获取游戏充值记录
     */
    public static final int GAME_RECODE_SUCCESS = 0X41;
    public static final int GAME_RECODE_FAIL = 0X42;

    /**
     * 获得第三方登录需要的参数
     */
    public static final int USER_GET_PARAMS_SUCCESS = 0x44;
    public static final int USER_GET_PARAMS_FAIL = 0x45;

    /**
     * 第三方登录成功
     */
    public static final int USER_THIRD_PARAMS_SUCCESS = 0x102;
    public static final int USER_THIRD_PARAMS_FAIL = 0x103;

    /**
     * 汇付宝订单请求成功
     */
    public static final int HFB_PAY_REQUEST_SUCCESS = 0x48;
    public static final int HFB_PAY_REQUEST_FAIL = 0x49;

    /**
     * 游戏支付方式
     */
    public static final int GAME_PAY_TYPE_SUCCESS = 0x50;
    public static final int GAME_PAY_TYPE_FAIL = 0x51;

    /**
     * 获取礼包列表
     */
    public static final int GET_USER_DISCOUNT_SUCCESS = 0x52;
    public static final int GET_USER_DISCOUNT_FAIL = 0x53;

    /**
     * 更新游客信息（账户、密码）
     */
    public static final int UPDATE_VISITOR_INFO_SUCCESS = 0x54;
    public static final int UPDATE_VISITOR_INFO_FAIL = 0x55;

    /**
     * 获取第三方登录的方式
     */
    public static final int THIRD_LOGIN_TYPE_SUCCESS = 0x56;
    public static final int THIRD_LOGIN_TYPE_FAIL = 0x57;

    /**
     * 实名认证
     */
    public static final int To_Certificate_SUCCESS = 0x58;
    public static final int To_Certificate_WAITING = 0x581;
    public static final int To_Certificate_FAIL = 0x59;

    /**
     * 请求防沉迷信息成功
     */
    public static final int REQUEST_ANTI_ADDICTION_SUCCESS = 0x60;


    /**
     * 请求防沉迷信息失败
     */
    public static final int REQUEST_ANTI_ADDICTION_FAIL = 0x61;

    /**
     * 下线通知成功
     */
    public static final int OFFLINE_SUCCESS = 0x62;


    /**
     * 下线通知失败
     */
    public static final int OFFLINE_FAIL = 0x63;

    /**
     * 检查验证码
     */
    public static final int IS_CODE_SUCCESS = 0x64;
    public static final int IS_CODE_FAIL = 0x65;


    /**
     * 上传角色成功
     */
    public static final int UPLOAD_ROLE_SUCCESS = 0x66;


    /**
     * 上传角色失败
     */
    public static final int UPLOAD_ROLE_FAIL = 0x67;

    /**
     * 获取微信官方支付结果
     */
    public static final int GET_WX_PAY_RESULT_SUCCESS = 0x68;
    public static final int GET_WX_PAY_RESULT_FAIL = 0x69;

    /**
     * 添加小号成功
     */
    public static final int ADD_SMALL_ACCOUNT_SUCCESS = 0x72;
    public static final int ADD_SMALL_ACCOUNT_FAIL = 0x73;

    /**
     * 小号登录成功
     */
    public static final int SMALL_ACCOUNT_LOGIN_SUCCESS = 0x74;
    public static final int SMALL_ACCOUNT_LOGIN_FAIL = 0x75;

    /**
     * 头像上传成功
     */
    public static final int AVATAR_UPLOAD_SUCCESS = 0x76;
    public static final int AVATAR_UPLOAD__FAIL = 0x77;

    /**
     * 是否强更成功
     */
    public static final int FORCED_UPDATE_SUCCESS = 0x78;
    public static final int FORCED_UPDATE_FAIL = 0x79;

    /**
     * 获取礼包详情成功
     */
    public static final int GIFTDET_SUCCESS = 0x80;
    public static final int GIFTDET_FAIL = 0x81;

    /**
     * 获取公告成功
     */
    public static final int NOTICE_SUCCESS = 0x82;
    public static final int NOTICE_FAIL = 0x83;

    /**
     * 设备信息
     */
    public static final int MODEL_SUCCESS = 0x84;
    public static final int MODEL_FAIL = 0x85;

    /**
     * 消息通知
     */
    public static final int MSGTZ_SUCCESS = 0x86;
    public static final int MSGTZ_FAIL = 0x87;

    /**
     * 通知详情
     */
    public static final int MSGTZDET_SUCCESS = 0x88;
    public static final int MSGTZDET_FAIL = 0x89;

    /**
     * 邀请奖励
     */
    public static final int LAUNCH_SUCCESS = 0x90;
    public static final int LAUNCH_FAIL = 0x91;

    /**
     * 退出弹窗广告
     */
    public static final int LOGOUTADV_SUCCESS = 0x96;
    public static final int LOGOUTADV_FAIL = 0x97;

    /**
     * 猜你喜欢
     */
    public static final int GUESS_SUCCESS = 0x98;
    public static final int GUESS_FAIL = 0x99;

    /**
     * 获取域名
     */
    public static final int HOST_SUCCESS = 0x104;
    public static final int HOST_FAIL = 0x105;

    /**
     * 绑定邮箱
     */
    public static final int USER_BIND_EMAIL_SUCCESS = 0x106;
    public static final int USER_BIND_EMAIL_FAIL = 0x107;

    /**
     * 解绑邮箱
     */
    public static final int USER_UNBIND_EMAIL_SUCCESS = 0x108;
    public static final int USER_UNBIND_EMAIL_FAIL = 0x109;

    /**
     * 帮助中心
     */
    public static final int HELPER_SUCCESS = 0x110;
    public static final int HELPER_FAIL = 0x111;

    /**
     * 帮助中心list
     */
    public static final int HELPERLIST_SUCCESS = 0x112;
    public static final int HELPERLIST_FAIL = 0x113;

    /**
     * 帮助详情
     */
    public static final int HELPERDET_SUCCESS = 0x114;
    public static final int HELPERDET_FAIL = 0x115;

    /**
     * 提现
     */
    public static final int TIXIAN_SUCCESS = 0x116;
    public static final int TIXIAN_FAIL = 0x117;

    /**
     * 绑币列表
     */
    public static final int BANGBI_LIST_SUCCESS = 0x118;
    public static final int BANGBI_LIST_FAIL = 0x119;

    /**
     * 积分兑换平台币
     */
    public static final int JFPTB_SUCCESS = 0x120;
    public static final int JFPTB_FAIL = 0x121;

    /**
     * 积分兑换记录
     */
    public static final int JFDHJL_SUCCESS = 0x122;
    public static final int JFDHJL_FAIL = 0x123;

    /**
     * 签到
     */
    public static final int SIGNIN_SUCCESS = 0x128;
    public static final int SIGNIN_FAIL = 0x129;

    /**
     * VIP
     */
    public static final int VIP_SUCCESS = 0x130;
    public static final int VIP_FAIL = 0x131;


    public static final int ZFB_WAPPAY_ORDERINFO_SUCCESS = 0x132;
    public static final int ZFB_WAPPAY_ORDERINFO_FAIL = 0x133;


    /**
     * 绑定平台币余额
     */
    public static final int BIND_PTB_MONEY_SUCCESS = 0x148;
    public static final int BIND_PTB_MONEY_FAIL = 0x149;


    /**
     * 验证 验证码
     */
    public static final int CHECK_CODE_SUCCESS = 0x150;
    public static final int CHECK_CODE_FAIL = 0x151;



    /**
     * 充值记录
     */
    public static final int PAY_RECORD_SUCCESS = 0x152;
    public static final int PAY_RECORD_FAIL = 0x153;

    /**
     * 修改小号昵称
     */
    public static final int SMALNICKNAME_SUCCESS = 0x156;
    public static final int SMALNICKNAME_FAIL = 0x157;

    /**
     * 获取小号列表
     */
    public static final int SMALLACCOUNT_LIST_SUCCESS = 0x158;
    public static final int SMALLACCOUNT_LIST_FAIL = 0x159;


    /**
     * 获取折扣
     */
    public static final int DISCOUNT_SUCCESS = 0x160;
    public static final int DISCOUNT_FAIL = 0x161;

    /**
     * 查询平台币、绑币余额
     */
    public static final int PTB_BALANCE_FAIL = 0x162;
    public static final int PTB_BALANCE_SUCCESS = 0x163;

    /**领取优惠券
     */
    public static final int RECEIVE_COUPON_FAIL = 0x164;
    public static final int RECEIVE_COUPON_SUCCESS = 0x165;

    /**
     * 获取全部代金券列表
     */
    public static final int ALL_COUPON_SUCCESS = 0x166;
    public static final int ALL_COUPON_FAIL = 0x167;

    /**
     * 获取已领取的可用代金券列表
     */
    public static final int USABLE_COUPON_SUCCESS = 0x168;
    public static final int USABLE_COUPON_FAIL = 0x169;

    /**
     * 获取特权礼包列表
     */
    public static final int GET_TEQUAN_LIST_SUCCESS = 0x170;
    public static final int GET_TEQUAN_LIST_FAIL = 0x171;

    /**
     * 查询后台版本号信息
     */
    public static final int GET_BACKGROUND_VERSION_SUCCESS = 0x172;
    public static final int GET_BACKGROUND_VERSION_FAIL = 0x173;

    /**
     * 签到信息详情
     */
    public static final int SIGN_DET_SUCCESS = 0x174;
    public static final int SIGN_DET_FAIL = 0x175;

    /**
     * 获取校验码
     */
    public static final int GET_AUTH_CODE_SUCCESS = 0x176;
    public static final int GET_AUTH_CODE_FAIL = 0x177;

    /**
     * 验证校验码
     */
    public static final int CHECK_AUTH_SUCCESS = 0x178;
    public static final int CHECK_AUTH_FAIL = 0x179;

    /**
     * 获取功能开关
     */
    public static final int SWITCH_STATUS_SUCCESS = 0x180;
    public static final int SWITCH_STATUS_FAIL = 0x181;

    /**
     * 获取微信扫码二维码
     */
    public static final int SCAN_WX_SUCCESS = 0x182;
    public static final int SCAN_WX_FAIL = 0x183;

    /**
     * 获取支付宝扫码二维码
     */
    public static final int SCAN_ZFB_SUCCESS = 0x184;
    public static final int SCAN_ZFB_FAIL = 0x185;

    /**
     * 查询扫码支付结果
     */
    public static final int SCAN_PAY_RESULT_SUCCESS = 0x186;
    public static final int SCAN_PAY_RESULT_FAIL = 0x187;

    /**
     * 百度云系统登录
     */
    public static final int YUN_LOGIN_SUCCESS = 0x188;

    /**
     * 分享
     */
    public static final int GET_SHARE_SUCCESS = 0x190;
    public static final int GET_SHARE_FAIL = 0x191;

    /**
     * get product price
     */
    public static final int GET_PRODUCT_PRICE_SUCCESS = 0x194;
    public static final int GET_PRODUCT_PRICE_FAIL = 0x195;


    /**
     * MGATE payment processing result
     */
    public static final int MGATE_ORDER_INIT_SUCCESS = 0x194;
    public static final int MGATE_ORDER_INIT_FAIL = 0x195;


}
