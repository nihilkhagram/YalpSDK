package com.cointizen.paysdk.config;

import android.util.Log;

public class MCHConstant {

    private static final String TAG = "MCHConstant";

    /**
     * 1登陆Url
     */
    private String platformUserLoginUrl = "";

    /**
     * 2普通注册Url
     */
    private String platformUserRegisterUrl = "";

    /**
     * 3获取订单信息
     */
    private String goodsOrderInfoUrl = "";

    /**
     * 4支付宝回调
     */
    // private String zfbCallbackUrl = "";

    /**
     * 5 支付宝订单验证
     */
    private String zfbVerificationOrderUrl = "";

    /**
     * Stripe init order
     */
    private String stripeInitOrderUrl = "";

    /**
     * 6 微信支付请求
     */
    // private String wxVerificationOrderUrl = "";

    /**
     * 7平台币支付
     */
    private String ptbPayOrderUrl = "";

    /**
     * 8平台币支付
     */
    // private String bindPtbPayOrderUrl = "";

    /**
     * 9支付之后验证
     */
    private String payResultVerificationUrl = "";

    /**
     * 10通知游戏后台Payment done successfully
     */
    private String noticeGamePaySuccess = "";

    /**
     * 11修改密码
     */
    // private String userModifyPassword = "";
    /**
     * 12获得手机验证码
     */
    private String verifyPhoneCodeUrl;
    /**
     * 获得邮箱验证码
     */
    private String verifyMailCodeUrl;
    /**
     * 13手机号注册
     */
    // private String platformPhoneRegisterUrl = "";
    /**
     * 14用户在该游戏中的礼包列表
     */
    private String gamePacksList;

    /**
     * 礼包详情
     */
    private String gamePacksDet;

    /**
     * 退出广告
     */
    private String LogoutADV = "";

    /**
     * 猜你喜欢
     */
    private String guessYouLike = "";

    /**
     * 游戏公告
     */
    private String gameNotice;

    /**
     * 机型统计
     */
    private String ModelStatistics;

    /**
     * 游戏礼包码
     */
    private String packsCodeUrl;

    /**
     * 15用户信息
     */
    private String userInfoUrl;

    /**
     * 16 绑定手机
     */
    private String updateUserInfoUrl;

    /**
     * 解绑手机
     */
    private String userUnBindPhoneUrl;

    /**
     * 验证手机验证码
     */
    private String checkPhoneCode;

    /**
     * 验证邮箱验证码
     */
    private String checkMailCode;

    /**
     * 17忘记密码
     */
    private String forgmentPasswordUrl;
    /**
     * 19平台币充值结果通知
     */
    private String noticeAddPTBUrl = "";

    /**
     * 20平台币充值记录
     */
    private String addPTBRecordURL = "";
    /**
     * 常见问题和解决办法
     */
    private String commonProblem = "";
    /**
     * 问答列表
     */
    private String problemFeedback = "";
    /**
     * 获取用户平台币
     */
    private String queryUserPTBUrl = "";

    /**
     * 威富通请求订单
     */
    private String wftOrderInfoUrl = "";
    /**
     * 微信支付接口
     */
    private String wxPayUrl = "";
    /**
     * 游戏充值记录
     */

    private String gameRecodeUrl = "";
    /**
     * 绑定游戏充值记录
     */
    private String gameRecodeUrl_b = "";
    /**
     * 获得开通哪些支付方式
     */
    private String showPayTypeUrl = "";
    /**
     * 微信登錄之前獲得wxappid
     */
    private String put_appidUrl = "";
    /**
     * 威信第三方授權之後返送code
     */
    private String get_code = "";
    /**
     * 微信地方放授权登录
     */
    private String userWxLoginUrl;

    /**
     * 获取折扣 首冲 续冲
     */
    private String userDiscountUrl;

    /**
     * 获取折扣 首冲 续冲
     */
    private String thirdLoginTypeUrl;

    /**
     * 修改玩家等级和用户名
     */
    private String changeLevelNameUrl;


    /**
     * 找回密码验证账号
     */
    private String forgetAccountUrl;

    /**
     * logo
     */
    private String downloadLogoUrl;
    /**
     * 微信商户id
     */
    public String wxShopId;

    /**
     * 验签字符串
     */
    private String MCHKEY;

    private String platformDomain;
    private String sdkBaseUrl;
    private String certificate;
    private String ZFBwap;


    private String jftOrderInfoUrl;

    private String hfbOrderInfoUrl;
    //获得第三方登录需要的参数，qqappid等
    private String thirdloginUrl;
    //第三方登录请求
    private String thirdloginrequest;

    //用户注册协议链接
    private String UserAgreementUrl;

    //根据用户uid获取该用户是否实名
    private String antiAddictionUrl;
    //下线通知
    private String offlineAnnouceUrl;

    //威富通wap支付
    private String wftWapPayUrl;

    //获取微信官方支付结果
    private String wxPayResultUrl;

    //获取客服QQ
    private String getServiceUrl;
    /**
     * 上传角色
     */
    private String uploadRoleUrl = "";

    private String initFloatingUrl = "";
    private static MCHConstant instance;

    //添加小号
    private String addSmallAccount = "";

    //小号登录
    private String smallAccountLogin = "";

    //上传头像
    private String upHread = "";

    //消息通知
    private String MsgTZ = "";

    //通知详情
    private String MsgTZDet = "";

    //投放平台
    private String Invitation = "";

    //绑定邮箱
    private String BindEmail = "";

    //帮助中心
    private String Helper = "";

    //帮助中心
    private String HelperList = "";

    //帮助中心
    private String HelperDet = "";

    //分享
    private String Share = "";

    //提现
    private String TiXian = "";

    //绑币集合
    private String BangBiList = "";

    //积分兑换平台币
    private String JFPTB = "";

    //积分兑换记录
    private String JFDHJL = "";

    private String UnBindEmail = "";


    private String checkCodeUrl = "";


    private String ShareNum = "";


    //设备上线
    private String DeviceOnlineUrl = "";
    //设备下线
    private String DeviceDownUrl = "";

    //修改小号昵称
    private String editSmallNickname = "";

    //游戏消费记录
    private String PayRecordUrl = "";

    //获取小号列表
    private String smallAccountList = "";

    //折扣返利html信息
    private String discountHTML = "";

    //领取代金券
    private String receiveCoupon = "";

    //所有满减券
    private String allCoupon = "";

    //满足支付条件的满减券
    private String canUseCoupon = "";

    //查询后台版本号
    private String backgroundVersion = "";

    //用户签到
    private String userSign = "";

    //签到信息详情
    private String signDet = "";

    //获取校验码
    private String getAuthCode = "";

    //验证授权
    private String checkAuth = "";

    //获取功能开关
    private String switchStatus = "";

    //极验初始化
    private String jiyanInit = "";

    //获取微信扫码二维码
    private String getWxRqCode = "";

    //获取支付宝扫码二维码
    private String getZfbRqCode = "";

    //查询扫码付款结果
    private String getScanResult = "";

    //上报talking_code
    private String uploadTalkingCode = "";

    private String getSwitchInfo = "";

    private String privacyUrl = "";

    private String delAccountUrl = "";

    private String logoutVerifyUrl = "";

    private String logoutSendSMSUrl = "";

    private String messageListUrl = "";

    private String awardListUrl = "";

    public static MCHConstant getInstance() {
        if (null == instance) {
            instance = new MCHConstant();
        }
        return instance;
    }

    private MCHConstant() {
    }

    public void initUrlInfo() {
        initSDKChannelUrl();
    }

    public void initSDKChannelUrl() {
        messageListUrl = sdkBaseUrl + "center/sdk_notice_list";//
        Log.e("MyLogData" , "" + sdkBaseUrl );
        Log.e("MyLogData" , "" + messageListUrl );
        logoutSendSMSUrl = sdkBaseUrl + "user/verifyCode";//
        logoutVerifyUrl = sdkBaseUrl + "user/verifyUnsub";//
        delAccountUrl = sdkBaseUrl + "user/unsubscribe";//
        privacyUrl = sdkBaseUrl + "server/get_protocol";//
        platformUserLoginUrl = sdkBaseUrl + "user/user_login";//
        checkCodeUrl = sdkBaseUrl + "User/checkCode";//
        platformUserRegisterUrl = sdkBaseUrl + "User/register";//
        zfbVerificationOrderUrl = sdkBaseUrl + "pay/and_alipay_pay";//
        stripeInitOrderUrl = sdkBaseUrl + "pay/stripe_pay";//

        ptbPayOrderUrl = sdkBaseUrl + "Pay/platform_coin_pay";//
        queryUserPTBUrl = sdkBaseUrl + "pay/user_platform_coin";//获取用户平台币
        gamePacksList = sdkBaseUrl + "GameGift/gift_list";// 获得礼包列表
        gamePacksDet = sdkBaseUrl + "GameGift/gift_detail";// 礼包详情
        packsCodeUrl = sdkBaseUrl + "GameGift/receive_gift";// 领取礼包
        userInfoUrl = sdkBaseUrl + "center/getUserInfo";//
        verifyPhoneCodeUrl = sdkBaseUrl + "user/send_sms";//
        verifyMailCodeUrl = sdkBaseUrl + "user/send_email";
        forgetAccountUrl = sdkBaseUrl + "user/forget_account";
        updateUserInfoUrl = sdkBaseUrl + "User/user_update_data";//
        checkPhoneCode = sdkBaseUrl + "User/verify_sms";
        checkMailCode = sdkBaseUrl + "User/verify_email";
        forgmentPasswordUrl = sdkBaseUrl + "User/forget_password";//
        hfbOrderInfoUrl = sdkBaseUrl + "pay/heepay_pay";//汇付宝订单很支付请求
        jftOrderInfoUrl = sdkBaseUrl + "pay/jft_pay";//
        ZFBwap = sdkBaseUrl + "WapPay/alipay_pay";
        addPTBRecordURL = sdkBaseUrl + "center/balance_record";
        userUnBindPhoneUrl = sdkBaseUrl + "User/user_phone_unbind";
        wftOrderInfoUrl = sdkBaseUrl + "Pay/outher_pay";// 微付通订单信息
        wxPayUrl = sdkBaseUrl + "pay/wx_pay";
        payResultVerificationUrl = sdkBaseUrl + "Pay/pay_validation";// 支付验证
        noticeGamePaySuccess = sdkBaseUrl + "GameNotify/game_pay_notify";// Payment done successfully通知后台
        /** 非绑定消费 */
        gameRecodeUrl = sdkBaseUrl + "Spend/spend_recond_list";
        /** 绑定消费 */
        gameRecodeUrl_b = sdkBaseUrl + "Spend/bind_spend_recond_list";
        commonProblem = sdkBaseUrl + "user/get_problem";
        problemFeedback = sdkBaseUrl + "user/get_question";
        showPayTypeUrl = sdkBaseUrl + "pay/get_pay_server";
        get_code = sdkBaseUrl + "user/get_code";
        userWxLoginUrl = sdkBaseUrl + "user/wx_login";
        put_appidUrl = sdkBaseUrl + "user/put_appid";
        //安卓SDK客户端向SDK服务器请求获得客户端第三方登录需要的参数
        thirdloginUrl = sdkBaseUrl + "user/oauth_param";
        thirdloginrequest = sdkBaseUrl + "user/oauth_login";
        userDiscountUrl = sdkBaseUrl + "user/get_user_discount";
        thirdLoginTypeUrl = sdkBaseUrl + "User/thirdparty";

        UserAgreementUrl = platformDomain + "mobile/user/protocol/issdk/1.html";
        changeLevelNameUrl = sdkBaseUrl + "user/update_user_play";
        downloadLogoUrl = platformDomain + "Public/Sdk/logo.png";
        antiAddictionUrl = sdkBaseUrl + "user/return_age";
        offlineAnnouceUrl = sdkBaseUrl + "user/get_down_time";
        wftWapPayUrl = sdkBaseUrl + "pay/and_weixin_pay";
        uploadRoleUrl = sdkBaseUrl + "User/save_user_play_info";//上传角色
        wxPayResultUrl = sdkBaseUrl + "pay/get_orderno_restart";
        getServiceUrl = sdkBaseUrl + "Game/get_game_ccustom_service_qq";
        initFloatingUrl = sdkBaseUrl + "center/get_suspend";
        addSmallAccount = sdkBaseUrl +"user/add_small";
        smallAccountLogin = sdkBaseUrl + "user/get_enter_game_info";
        upHread = sdkBaseUrl + "center/set_head_portrait";
        gameNotice = sdkBaseUrl + "center/today_notice";             //游戏公告
        ModelStatistics = sdkBaseUrl + "User/device_record";         //机型统计
        MsgTZ = sdkBaseUrl + "center/today_notice";                   //通知列表
        MsgTZDet = sdkBaseUrl + "center/notice_detail";                 //通知详情
        Invitation = sdkBaseUrl + "User/launch_record";                 //投放平台
        LogoutADV = sdkBaseUrl + "center/get_adv";                 //退出广告
        guessYouLike = sdkBaseUrl + "center/guess_like";                 //猜你喜欢
        BindEmail = sdkBaseUrl + "User/bind_email";                   //绑定邮箱
        UnBindEmail = sdkBaseUrl + "User/unbind_email";                   //解绑邮箱
        Helper = sdkBaseUrl + "center/customer_contact";                   //帮助中心
        HelperList = sdkBaseUrl + "center/customer_question_list";                   //帮助中心list
        HelperDet = sdkBaseUrl + "center/customer_question_detail";                   //帮助详情
        Share = sdkBaseUrl + "center/get_sdk_share_url";                   //获取分享内容
        ShareNum = sdkBaseUrl + "center/share_game";                   //上传分享次数
        TiXian = sdkBaseUrl + "User/withdraw_gold_url";                   //提现
        BangBiList = sdkBaseUrl + "user/get_user_bind_coin";                   //绑币记录
        JFPTB = sdkBaseUrl + "PointShop/point_convert_coin";                   //积分兑换平台币
        JFDHJL = sdkBaseUrl + "PointShop/get_user_buy_record";                   //积分兑换记录
        PayRecordUrl = sdkBaseUrl + "center/spend_record";                   //充值记录
        DeviceOnlineUrl = sdkBaseUrl + "user/equipment_login";                   //设备上线统计
        DeviceDownUrl = sdkBaseUrl + "user/equipment_down";                   //设备下线统计
        editSmallNickname = sdkBaseUrl + "user/change_small_nickname";
        smallAccountList = sdkBaseUrl + "user/get_small_lists";
        discountHTML = sdkBaseUrl + "center/get_game_welfare";
        receiveCoupon = sdkBaseUrl + "center/reciver_coupon";
        allCoupon = sdkBaseUrl + "center/get_coupon";
        canUseCoupon = sdkBaseUrl + "center/get_valid_coupon";
        backgroundVersion = sdkBaseUrl + "server/get_server_version";
        userSign = sdkBaseUrl + "center/sign_in";
        signDet = sdkBaseUrl + "center/sign_detail";
        getAuthCode = sdkBaseUrl + "user/get_auth_code";
        checkAuth = platformDomain + "mobile/user/check_auth_code";
        switchStatus = sdkBaseUrl + "user/get_switch_status";
        jiyanInit = sdkBaseUrl + "Gt/init";
        getWxRqCode = sdkBaseUrl + "Scanpay/adrWechat";
        getZfbRqCode = sdkBaseUrl + "Scanpay/adrAlipay";
        getScanResult = sdkBaseUrl + "Scanpay/check_status";
        uploadTalkingCode = sdkBaseUrl + "User/collect_login";
        getSwitchInfo = sdkBaseUrl + "server/get_switch";
    }

    public String getAwardListUrl() {
        return awardListUrl;
    }

    public String getMessageListUrl() {
        return messageListUrl;
    }

    public String getLogoutVerifyUrl() {
        return logoutVerifyUrl;
    }

    public String getLogoutSendSMSUrl() {
        return logoutSendSMSUrl;
    }

    public String getPrivacyUrl() {
        return privacyUrl;
    }

    public String getDelAccountUrl() {
        return delAccountUrl;
    }

    public String getUploadTalkingCode() {
        return uploadTalkingCode;
    }

    public String getGetScanResult() {
        return getScanResult;
    }

    public String getGetWxRqCode() {
        return getWxRqCode;
    }

    public String getGetZfbRqCode() {
        return getZfbRqCode;
    }

    public String getJiyanInit() {
        return jiyanInit;
    }

    public String getSwitchStatus() {
        return switchStatus;
    }

    public String getGetAuthCode() {
        return getAuthCode;
    }

    public String getCheckAuth() {
        return checkAuth;
    }

    public String getUserSign() {
        return userSign;
    }

    public String getSignDet() {
        return signDet;
    }

    public String getBackgroundVersion() {
        return backgroundVersion;
    }

    public String getCanUseCoupon() {
        return canUseCoupon;
    }

    public String getAllCoupon() {
        return allCoupon;
    }

    public String getReceiveCoupon() {
        return receiveCoupon;
    }

    public String getDiscountHTML() {
        return discountHTML;
    }

    public String getSmallAccountList() {
        return smallAccountList;
    }

    public String getEditSmallNickname() {
        return editSmallNickname;
    }

    public String getDeviceOnlineUrl() {
        return DeviceOnlineUrl;
    }

    public String getDeviceDownUrl() {
        return DeviceDownUrl;
    }

    public String getPayRecordUrl() {
        return PayRecordUrl;
    }

    public String getShareNum() {
        return ShareNum;
    }

    public String getCheckCodeUrl() {
        return checkCodeUrl;
    }

    public String getForgetAccountUrl() {
        return forgetAccountUrl;
    }

    public String getJFDHJL() {
        return JFDHJL;
    }

    public String getJFPTB() {
        return JFPTB;
    }

    public String getBangBiList() {
        return BangBiList;
    }
    public String getTiXian() {
        return TiXian;
    }

    public String getShare() {
        return Share;
    }

    public String getHelperDet() {
        return HelperDet;
    }
    public String getHelperList() {
        return HelperList;
    }

    public String getHelper() {
        return Helper;
    }
    public String getUnBindEmail() {
        return UnBindEmail;
    }

    public String getBindEmail() {
        return BindEmail;
    }

    public String getGuessYouLike() {
        return guessYouLike;
    }
    public String getLogoutADV() {
        return LogoutADV;
    }


    public String getInvitation() {
        return Invitation;
    }

    public String getMsgTZDet() {
        return MsgTZDet;
    }

    public String getMsgTZ() {
        return MsgTZ;
    }
    public String getModelStatistics() {
        return ModelStatistics;
    }

    public String getGameNotice() {
        return gameNotice;
    }

    public String getGamePacksDet() {
        return gamePacksDet;
    }

    public String getUpHread() {
        return upHread;
    }

    public String getCertificate() {
        return certificate;
    }

    public String getOfflineAnnouceUrl() {
        return offlineAnnouceUrl;
    }

    public String getUserWxLoginUrl() {
        return userWxLoginUrl;
    }

    public String getPut_appidUrl() {
        return put_appidUrl;
    }

    public String getGet_code() {
        return get_code;
    }

    public String getPlatformUserLoginUrl() {
        return platformUserLoginUrl;
    }

    public String getChangeLevelNameUrl() {
        return changeLevelNameUrl;
    }

    public String getPlatformUserRegisterUrl() {
        return platformUserRegisterUrl;
    }

    public String getGoodsOrderInfoUrl() {
        return goodsOrderInfoUrl;
    }


    public String getZfbVerificationOrderUrl() {
        return zfbVerificationOrderUrl;
    }

    public String getPtbPayOrderUrl() {
        return ptbPayOrderUrl;
    }

    public String getPayResultVerificationUrl() {
        return payResultVerificationUrl;
    }

    public String getNoticeGamePaySuccess() {
        return noticeGamePaySuccess;
    }

    public String getWxShopId() {
        return wxShopId;
    }

    public String getGamePacksList() {
        return gamePacksList;
    }

    public String getUserInfoUrl() {
        return userInfoUrl;
    }

    public String getAntiAddictionUrl() {
        return antiAddictionUrl;
    }

    public String getVerifyPhoneCodeUrl() {
        return verifyPhoneCodeUrl;
    }

    public String getVerifyMailCodeUrl() {
        return verifyMailCodeUrl;
    }

    public String getUpdateUserInfoUrl() {
        return updateUserInfoUrl;
    }

    public String getForgmentPasswordUrl() {
        return forgmentPasswordUrl;
    }

    public String getNoticeAddPTBUrl() {
        return noticeAddPTBUrl;
    }

    public String getAddPTBRecordURL() {
        return addPTBRecordURL;
    }

    public String getMCHKEY() {
        return MCHKEY;
    }

    public String getQueryUserPTBUrl() {
        return queryUserPTBUrl;
    }

    public String getWftOrderInfoUrl() {
        return wftOrderInfoUrl;
    }

    public String getWxPayUrl() {
        return wxPayUrl;
    }

    public String getPacksCodeUrl() {
        return packsCodeUrl;
    }

    public String getSdkBaseUrl() {
        return sdkBaseUrl;
    }

    public String getPlatformDomain() {
        return platformDomain;
    }

    public void setSdkBaseUrl(String sdkBaseUrl) {
        this.platformDomain = sdkBaseUrl;
        this.sdkBaseUrl = sdkBaseUrl + "/sdk/";
    }

    public void setMCHKEY(String mCHKEY) {
        MCHKEY = mCHKEY;
    }

    public String getUserUnBindPhoneUrl() {
        return this.userUnBindPhoneUrl;
    }

    public String getGameRecodeUrl() {
        return gameRecodeUrl;
    }

    public String getUserDiscountUrl() {
        return userDiscountUrl;
    }

    public String getJftOrderInfoUrl() {
        return jftOrderInfoUrl;
    }

    public String getProblemFeedback() {
        return this.problemFeedback;
    }

    public String getCommonProblem() {
        return this.commonProblem;
    }

    public String getShowPayTypeUrl() {
        return showPayTypeUrl;
    }

    public String getHfbOrderInfoUrl() {
        return hfbOrderInfoUrl;
    }

    public String getThirdloginUrl() {
        return thirdloginUrl;
    }

    public String getThirdloginrequest() {
        return thirdloginrequest;
    }

    public String getThirdLoginTypeUrl() {
        return thirdLoginTypeUrl;
    }

    public String getUserAgreementUrl() {
        return UserAgreementUrl;
    }

    public String getDownloadLogoUrl() {
        return downloadLogoUrl;
    }

    public String getWftWapPayUrl() {
        return wftWapPayUrl;
    }

    public String getCheckPhoneCode()

    {
        return checkPhoneCode;
    }

    public String getCheckMailCode()

    {
        return checkMailCode;
    }

    public String getUploadRoleUrl() {
        return uploadRoleUrl;
    }

    public String getWxPayResultUrl() {
        return wxPayResultUrl;
    }

    public String getGetServiceUrl() {
        return getServiceUrl;
    }

    public String getInitFloatingUrl() {
        return initFloatingUrl;
    }

    public String getAddSmallAccount(){
        return addSmallAccount;
    }

    public String getSmallAccountLoginURL(){
        return smallAccountLogin;
    }

    public String getGetSwitchInfo() {
        return getSwitchInfo;
    }

}
