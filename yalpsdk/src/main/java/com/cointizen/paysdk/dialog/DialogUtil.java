package com.cointizen.paysdk.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.cointizen.open.ApiCallback;
import com.cointizen.open.GPExitResult;
import com.cointizen.paysdk.bean.InitModel;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.ScreenshotUtils;

public final class DialogUtil {
    public static Dialog mDialog;

    private DialogUtil() {
    }

    private static DialogUtil single = null;

    public static DialogUtil getInstance() {
        if (single == null) {
            single = new DialogUtil();
        }
        return single;
    }

    /**
     * This is the system exit pop tips
     *
     * @param context
     * @param pTitle
     * @param pMsg
     */
    public static void showCustomMessage(final Activity context, String pTitle, final String pMsg, String ok, String cancel) {
        final GPExitResult exitResult = new GPExitResult();
        final Dialog lDialog = new Dialog(context,
//				android.R.style.Theme_Translucent_NoTitleBar);
                MCHInflaterUtils.getIdByName(context, "style", "mch_MCSelectPTBTypeDialog"));
        lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        lDialog.setContentView(getIdByName(context, "layout",
//				"mch_dialog_alert_exit_main"));
                "mch_dialog_alert_exit_main_light"));
        ((TextView) lDialog.findViewById(getIdByName(context,
                "id", "dialog_title"))).setText(pTitle);
        ((TextView) lDialog.findViewById(getIdByName(context,
                "id", "dialog_message"))).setText(pMsg);
        ((Button) lDialog.findViewById(getIdByName(context, "id",
                "ok"))).setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {  //点击了确认退出按钮
                lDialog.dismiss();
                exitResult.mResultCode = GPExitResult.GPSDKExitResultCodeExitGame;
                if (null != ApiCallback.getExitObsv()) {
                    InitModel.init().offLine(context, true);

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ScreenshotUtils.getInstance().onDestroy(); //解除屏幕截图广播注册
                    ApiCallback.getExitObsv().onExitFinish(exitResult);
                }
                return;
            }
        });
        ((Button) lDialog.findViewById(getIdByName(context, "id",
                "ok"))).setText(ok);
        lDialog.show();
        ((Button) lDialog.findViewById(getIdByName(context, "id",
                "cancel"))).setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                lDialog.dismiss();
                exitResult.mResultCode = GPExitResult.GPSDKExitResultCodeCloseWindow;
                if (null != ApiCallback.getExitObsv()) {
                    ApiCallback.getExitObsv().onExitFinish(exitResult);
                }
                return;
            }
        });

        ((Button) lDialog.findViewById(getIdByName(context, "id",
                "cancel"))).setText(cancel);
    }


    public static Dialog lDialog;

    /**
     * @param mypay
     * @param pTitle
     * @param pMsg
     * @param getApplicationContext
     */
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    public static void showCustomMsg(final Context mypay, String pTitle, final String pMsg, final Context getApplicationContext, String ok, String cancel,
//                                     final boolean isCertificate) {
//        final GPExitResult exitResult = new GPExitResult();
//        if (lDialog != null) {
//            lDialog.dismiss();
//        }
//        if(mypay == null ||  ((Activity)mypay).isDestroyed() ||  ((Activity)mypay).isFinishing()){
//            return;
//        }
//        lDialog = new Dialog(mypay, MCHInflaterUtils.getIdByName(mypay, "style", "mch_MCSelectPTBTypeDialog"));
//        lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        lDialog.setContentView(getIdByName(getApplicationContext, "layout", "mch_dialog_alert_exit_main_light"));
//        ((TextView) lDialog.findViewById(getIdByName(getApplicationContext, "id", "dialog_title"))).setText(pTitle);
//        ((TextView) lDialog.findViewById(getIdByName(getApplicationContext, "id", "dialog_message"))).setText(pMsg);
//        ((TextView) lDialog.findViewById(getIdByName(getApplicationContext, "id", "dialog_message"))).setMovementMethod(ScrollingMovementMethod.getInstance());
//        ((Button) lDialog.findViewById(getIdByName(getApplicationContext, "id",
//                "ok"))).setOnClickListener(new OnMultiClickListener() {
//            @Override
//            public void onMultiClick(View v) {
//                lDialog.dismiss();
//                if (isCertificate) {
//                    Intent intent = new Intent(mypay, MCHToCertificateActivity.class);
//                    intent.putExtra("type", "0");
//                    intent.putExtra("dialog", "1");
//                    mypay.startActivity(intent);
//                }
//            }
//        });
//        ((Button) lDialog.findViewById(getIdByName(getApplicationContext, "id", "ok"))).setText(ok);
//        ((Button) lDialog.findViewById(getIdByName(getApplicationContext, "id", "cancel"))).setOnClickListener(new OnMultiClickListener() {
//            @Override
//            public void onMultiClick(View v) {
//                lDialog.dismiss();
//                return;
//            }
//        });
//
//        Button viewById = (Button) lDialog.findViewById(getIdByName(getApplicationContext, "id", "cancel"));
//        viewById.setText(cancel);
//        if(cancel.equals("")){
//            viewById.setVisibility(View.GONE);
//        }
//        lDialog.show();
//    }

    /**
     * 删除好友
     */
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    public static void showCustomMsg2(final String id, final Context mypay, String pTitle,
//                                      final String pMsg, final Context getApplicationContext, String ok,
//                                      String cancel) {
//        final GPExitResult exitResult = new GPExitResult();
//        if (lDialog != null) {
//            lDialog.dismiss();
//        }
//        if(mypay == null ||  ((Activity)mypay).isDestroyed() ||  ((Activity)mypay).isFinishing()){
//            return;
//        }
//        lDialog = new Dialog(mypay,
////				android.R.style.Theme_Translucent_NoTitleBar);
//                MCHInflaterUtils.getIdByName(mypay, "style", "mch_MCSelectPTBTypeDialog"));
//        lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        lDialog.setContentView(getIdByName(getApplicationContext, "layout",
////				"mch_dialog_alert_exit_main"));
//                "mch_dialog_alert_exit_main_light"));
//        ((TextView) lDialog.findViewById(getIdByName(getApplicationContext,
//                "id", "dialog_title"))).setText(pTitle);
//        ((TextView) lDialog.findViewById(getIdByName(getApplicationContext,
//                "id", "dialog_message"))).setText(pMsg);
//        ((Button) lDialog.findViewById(getIdByName(getApplicationContext, "id",
//                "ok"))).setOnClickListener(new OnMultiClickListener() {
//            @Override
//            public void onMultiClick(View v) {
//
//                lDialog.dismiss();
//            }
//        });
//        ((Button) lDialog.findViewById(getIdByName(getApplicationContext, "id",
//                "ok"))).setText(ok);
//        ((Button) lDialog.findViewById(getIdByName(getApplicationContext, "id",
//                "cancel"))).setOnClickListener(new OnMultiClickListener() {
//            @Override
//            public void onMultiClick(View v) {
//                lDialog.dismiss();
//                return;
//            }
//        });
//
//        Button viewById = (Button) lDialog.findViewById(getIdByName(getApplicationContext, "id",
//                "cancel"));
//        viewById.setText(cancel);
//        if(cancel.equals("")){
//            viewById.setVisibility(View.GONE);
//        }
//        lDialog.show();
//    }



    public static void mch_alert_exit(Activity context) {
        DialogUtil.showCustomMessage(context, DialogConstants.S_rvlSenplVy, DialogConstants.S_FOAaPEJRqI, DialogConstants.S_eWBfpNUUQu, DialogConstants.S_LsXupyLcLb);
    }


    public static Dialog mch_alert_msg(final Context mypay, String pTitle,
                                       final String pMsg, final Context getApplicationContext, String substr,
                                       String clestr, final OnClickListener sublis) {
        final Dialog lDialog = new Dialog(mypay,
                MCHInflaterUtils.getIdByName(mypay, "style", "mch_MCSelectPTBTypeDialog"));
        lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        lDialog.setContentView(getIdByName(getApplicationContext, "layout",
//				"mch_dialog_alert_exit_main"));
                "mch_dialog_alert_exit_main_light"));
        ((TextView) lDialog.findViewById(getIdByName(getApplicationContext,
                "id", "dialog_title"))).setText(pTitle);
        ((TextView) lDialog.findViewById(getIdByName(getApplicationContext,
                "id", "dialog_message"))).setText(pMsg);
        ((Button) lDialog.findViewById(getIdByName(getApplicationContext, "id",
                "ok"))).setOnClickListener(new OnMultiClickListener() {

            @Override
            public void onMultiClick(View v) {
                sublis.onClick(v);
                lDialog.dismiss();
            }
        });
        ((Button) lDialog.findViewById(getIdByName(getApplicationContext, "id",
                "ok"))).setText(substr);
        ((Button) lDialog.findViewById(getIdByName(getApplicationContext, "id",
                "cancel"))).setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                lDialog.dismiss();
            }
        });

        ((Button) lDialog.findViewById(getIdByName(getApplicationContext, "id",
                "cancel"))).setText(clestr);
        return lDialog;
    }



    public static Dialog mch_down_time_alert(final Context context, final String pMsg, String substr, final OnClickListener sublis) {
        final Dialog lDialog = new Dialog(context, MCHInflaterUtils.getIdByName(context, "style", "mch_MCSelectPTBTypeDialog"));
        lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        lDialog.setContentView(getIdByName(context, "layout", "mch_dialog_alert_exit_main_light2"));
        ((TextView) lDialog.findViewById(getIdByName(context, "id", "dialog_message"))).setText(pMsg);
        ((TextView) lDialog.findViewById(getIdByName(context, "id", "dialog_message"))).setTextSize(14);
        ((Button) lDialog.findViewById(getIdByName(context, "id", "ok")))
                .setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        sublis.onClick(v);
                        lDialog.dismiss();
                    }
        });
        ((Button) lDialog.findViewById(getIdByName(context, "id", "ok"))).setText(substr);
        return lDialog;
    }

    /**
     * 白色提示弹窗
     */
    public static void showAlert(final Context mypay, String pTitle,
                                 final String pMsg, final Context getApplicationContext, String ok) {
        final Dialog lDialog = new Dialog(mypay, MCHInflaterUtils.getIdByName(mypay, "style", "mch_MCSelectPTBTypeDialog"));
        lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        lDialog.setContentView(getIdByName(getApplicationContext, "layout",
                "mch_dialog_alert_main"));
        ((TextView) lDialog.findViewById(getIdByName(getApplicationContext,
                "id", "dialog_title"))).setText(pTitle);
        ((TextView) lDialog.findViewById(getIdByName(getApplicationContext,
                "id", "dialog_message"))).setText(pMsg);
        ((Button) lDialog.findViewById(getIdByName(getApplicationContext, "id",
                "ok"))).setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                lDialog.dismiss();
                return;
            }
        });
        ((Button) lDialog.findViewById(getIdByName(getApplicationContext, "id",
                "ok"))).setText(ok);
        lDialog.show();
    }

    /**
     * 跳转弹窗样式
     */
    public static void showRoundProcessDialog(Context mContext, int layout) {
        OnKeyListener keyListener = new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_HOME
                        || keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                }
                return false;
            }
        };
        mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.setOnKeyListener(keyListener);
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);
    }

    /**
     * 跳转弹窗样式
     */
    public static Dialog RoundProcessDialog(Context mContext, int layout) {
        OnKeyListener keyListener = new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_HOME
                        || keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                }
                return false;
            }
        };
        mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.setOnKeyListener(keyListener);
        return mDialog;
        // 注意此处要放在show之后 否则会报异常
        //mDialog.setContentView(layout);
    }

    /**
     * 设置dialog 位置和大小
     */

    public static Dialog setDialog(Dialog dialog) {
         /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置,
         * 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);

        /*
         * lp.x与lp.y表示相对于原始位置的偏移.
         * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
         * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
         * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
         * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
         * 当参数值包含Gravity.CENTER_HORIZONTAL时
         * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
         * 当参数值包含Gravity.CENTER_VERTICAL时
         * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
         * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
         * Gravity.CENTER_VERTICAL.
         * 
         * 本来setGravity的参数值为Gravity.LEFT | Gravity.TOP时对话框应出现在程序的左上角,但在
         * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了,
         * Gravity.LEFT, Gravity.TOP, Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
         */
//        lp.x = 100; // 新位置X坐标
//        lp.y = 100; // 新位置Y坐标
//        lp.width = 300; // 宽度
//        lp.height = 300; // 高度
//        lp.alpha = 0.7f; // 透明度

        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        // dialog.onWindowAttributesChanged(lp);
//        dialogWindow.setAttributes(lp);

        /*
         * 将对话框的大小按屏幕大小的百分比设置
         */
//        WindowManager m = dialogWindow.getWindowManager();
//        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.x = 100; // 新位置X坐标
//        p.y = 100; // 新位置Y坐标
//        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
//        p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
//        dialogWindow.setAttributes(p);

        Window window = dialog.getWindow();
        WindowManager wm = window.getWindowManager();
        Point windowSize = new Point();
        wm.getDefaultDisplay().getSize(windowSize);
        float size;
        int width = windowSize.x;
        int height = windowSize.y;
        //横屏
        if (width >= height) {
            size = 0.5f;
        } else {
            size = 0.8f;
        }
        window.getAttributes().width = (int) (windowSize.x * size);
//		window.getAttributes().width = (int) 400;
        window.getAttributes().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // window.getAttributes().width = windowSize.x;
//		window.getAttributes().height = 500;
        window.setGravity(Gravity.CENTER);
        return dialog;
    }

    /**
     * Refer to external project resources
     *
     * @param context
     * @param className
     * @param name
     * @return
     */
    private static int getIdByName(Context context, String className, String name) {
        if (null == context) {
            return -1;
        }
        int id = getIdByContext(context, className, name);
        if (id > 0) {
            return id;
        }
        String packageName = context.getPackageName();
        Class r = null;
        id = 0;
        try {
            r = Class.forName(packageName + ".R");
            Class[] classes = r.getClasses();
            Class desireClass = null;
            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }
            if (desireClass != null)
                id = desireClass.getField(name).getInt(desireClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return id;
    }

    private static int getIdByContext(Context context, String className,
                                      String name) {
        Resources res = null;
        int id = 0;
        try {
            res = context.getResources();
            id = res.getIdentifier(name, className, context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }


}
