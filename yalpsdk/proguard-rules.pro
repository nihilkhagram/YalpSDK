# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\myProgram\myPlugin\android-sdk-windows/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontskipnonpubliclibraryclassmembers  # 不忽略非公共的库类
-dontoptimize                          # 优化不优化输入的类文件
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*     # 混淆时所采用的算法
-optimizationpasses 5                  # 指定代码的压缩级别
-dontusemixedcaseclassnames            # 是否使用大小写混合
-keepattributes *Annotation*           # 保持注解
-dontpreverify                         # 混淆时是否做预校验
-verbose                               # 混淆时是否记录日志
-ignorewarnings                        # 忽略警告

#保持哪些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.app.DialogFragment
-keep public class * extends android.app.Application
-keep class com.cointizen.paysdk.XiGuService { *; }

#生成日志数据，gradle build时在本项目根目录输出
#-dump class_files.txt            #apk包内所有class的内部结构
#-printseeds seeds.txt            #未混淆的类和成员
#-printusage unused.txt           #打印未被使用的代码
#-printmapping mapping.txt        #混淆前后的映射
#-keepnames class * implements java.io.Serializable #不混淆Serializable
-keep public class * extends android.support.**   #如果有引用v4或者v7包，需添加
#-dontwarn com.xxx**              #忽略某个包的警告
#-keepattributes Signature        #不混淆泛型

#-keep public class com.cointizen.paysdk.utils.MCLog {
#    <fields>;
#    <methods>;
#}

-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

#-obfuscationdictionary dic.txt
#-classobfuscationdictionary dic.txt
#-packageobfuscationdictionary dic.txt

-keep public class com.cointizen.plugin.**

-keep public class android.app.** {*; }
-keep public class com.cointizen.open.** {*;}

# 极验 SDK 已做混淆处理，集成时请带上混淆规则，勿再次混淆 SDK
-dontwarn com.geetest.sdk.**
-keep class com.geetest.sdk.**{*;}

#xutil 框架
-keepattributes Signature
-keep class com.lidroid.xutils.** {*; }
-keep public enum com.lidroid.xutils.http.client.HttpRequest.HttpMethod{*; }
-keep class * extends java.lang.annotation.Annotation { *; }


-keep class com.cointizen.paysdk.utils.ScreenshotUtils{*;}

#smartrefresh 刷新控件
-keep class com.mch.smartrefresh.** {*; }

# Share IM 即时通讯
-keep class com.mob.**{*;}


#聚宝云
-keep class com.fanwei.bluearty.** {*; }
-keep class com.fanwei.jubaosdk.** {*; }

#支付宝
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class org.json.alipay.JSONException{ public *;}

## WeChat
-keep class com.tencent.mm.** { *; }

## sdk
-keep class com.cointizen.paysdk.common.Constant{*;}
-keep class com.cointizen.plugin.yc.utils.**{ *; }
-keep class com.cointizen.plugin.guess.utils.**{ *; }
-keep class com.cointizen.plugin.moni.utils.**{ *; }
-keep class com.cointizen.plugin.qg.utils.UpdateUtils{ *; }

#unity3d
-keep class com.unity3d.player.** {*; }
-keep class org.fmod.** {*; }

#百度
-keep class com.baidu.** {*; }

#微信和QQ
-keep class com.tencent.** {*; }

#微博
-keep class com.sina.** {*; }

#模拟器检测
-keep class com.snail.antifake.** {*; }

#jni
-keep class com.cointizen.paysdk.jni.MCHKeyTools{*; }

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# RxJava RxAndroid
-dontwarn sun.misc.**

# Gson混淆脚本
-keep class com.google.gson.stream.** {*;}
-keep class com.youyou.uuelectric.renter.Network.user.** {*;}

#混淆JFTpay
-dontwarn sdk.pay.**
-keep class sdk.pay.**{*;}

-keep class com.bytedance.** { *; }

# OAID sdk start
-keep class com.bun.miitmdid.** { *; }
# asus
-keep class com.asus.msa.SupplementaryDID.** { *; }
-keep class com.asus.msa.sdid.** { *; }
# freeme
-keep class com.android.creator.** { *; }
-keep class com.android.msasdk.** { *; }
# huawei
-keep class com.huawei.hms.ads.identifier.** { *; }
#-keep class com.uodis.opendevice.aidl.** { *; }
# lenovo
-keep class com.zui.deviceidservice.** { *; }
-keep class com.zui.opendeviceidlibrary.** { *; }
# meizu
-keep class com.meizu.flyme.openidsdk.** { *; }
# nubia
-keep class com.bun.miitmdid.provider.nubia.NubiaIdentityImpl
# oppo
-keep class com.heytap.openid.** { *; }
# samsung
-keep class com.samsung.android.deviceidservice.** { *; }
# vivo
-keep class com.vivo.identifier.** { *; }
# xiaomi
-keep class com.bun.miitmdid.provider.xiaomi.IdentifierManager
# zte
-keep class com.bun.lib.** { *; }
# coolpad
-keep class com.coolpad.deviceidsupport.** { *; }
# OAID sdk end

-keep class org.apache.** {
    <fields>;
    <methods>;
}

-keep class com.goole.** {
    <fields>;
    <methods>;
}

-keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
}

-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

-keep class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {   #不混淆资源类
    public static <fields>;
}

-keepclasseswithmembers class * {  # 保持自定义控件类不被混淆
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers class * {  # 保持自定义控件类不被混淆
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepclassmembers enum  * {        # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclasseswithmembers,allowshrinking class * {  # 保持 native 方法不被混淆
    native <methods>;
}

