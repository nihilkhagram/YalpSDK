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
            public void onMultiClick(View v) {  //???????????????????????????
                lDialog.dismiss();
                exitResult.mResultCode = GPExitResult.GPSDKExitResultCodeExitGame;
                if (null != ApiCallback.getExitObsv()) {
                    InitModel.init().offLine(context, true);

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ScreenshotUtils.getInstance().onDestroy(); //??????????????????????????????
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
     * ????????????
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
     * ??????????????????
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
     * ??????????????????
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
        // ?????????????????????show?????? ??????????????????
        mDialog.setContentView(layout);
    }

    /**
     * ??????????????????
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
        // ?????????????????????show?????? ??????????????????
        //mDialog.setContentView(layout);
    }

    /**
     * ??????dialog ???????????????
     */

    public static Dialog setDialog(Dialog dialog) {
         /*
         * ??????????????????????????????????????????????????????????????????????????????,
         * ??????????????????getWindow(),??????????????????Activity???Window
         * ??????,?????????????????????????????????????????????Activity?????????.
         */
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);

        /*
         * lp.x???lp.y????????????????????????????????????.
         * ??????????????????Gravity.LEFT???,????????????????????????,??????lp.x??????????????????????????????,????????????.
         * ??????????????????Gravity.RIGHT???,????????????????????????,??????lp.x??????????????????????????????,????????????.
         * ??????????????????Gravity.TOP???,????????????????????????,??????lp.y??????????????????????????????,????????????.
         * ??????????????????Gravity.BOTTOM???,????????????????????????,??????lp.y??????????????????????????????,????????????.
         * ??????????????????Gravity.CENTER_HORIZONTAL???
         * ,?????????????????????,??????lp.x???????????????????????????????????????lp.x??????,??????????????????,??????????????????.
         * ??????????????????Gravity.CENTER_VERTICAL???
         * ,?????????????????????,??????lp.y???????????????????????????????????????lp.y??????,??????????????????,??????????????????.
         * gravity???????????????Gravity.CENTER,???Gravity.CENTER_HORIZONTAL |
         * Gravity.CENTER_VERTICAL.
         * 
         * ??????setGravity???????????????Gravity.LEFT | Gravity.TOP??????????????????????????????????????????,??????
         * ??????????????????????????????????????????????????????????????????,??????????????????????????????????????????????????????,
         * Gravity.LEFT, Gravity.TOP, Gravity.BOTTOM???Gravity.RIGHT????????????,???????????????????????????
         */
//        lp.x = 100; // ?????????X??????
//        lp.y = 100; // ?????????Y??????
//        lp.width = 300; // ??????
//        lp.height = 300; // ??????
//        lp.alpha = 0.7f; // ?????????

        // ???Window???Attributes?????????????????????????????????,?????????????????????????????????????????????????????????,????????????setAttributes
        // dialog.onWindowAttributesChanged(lp);
//        dialogWindow.setAttributes(lp);

        /*
         * ??????????????????????????????????????????????????????
         */
//        WindowManager m = dialogWindow.getWindowManager();
//        Display d = m.getDefaultDisplay(); // ????????????????????????
//        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // ?????????????????????????????????
//        p.x = 100; // ?????????X??????
//        p.y = 100; // ?????????Y??????
//        p.height = (int) (d.getHeight() * 0.6); // ????????????????????????0.6
//        p.width = (int) (d.getWidth() * 0.65); // ????????????????????????0.65
//        dialogWindow.setAttributes(p);

        Window window = dialog.getWindow();
        WindowManager wm = window.getWindowManager();
        Point windowSize = new Point();
        wm.getDefaultDisplay().getSize(windowSize);
        float size;
        int width = windowSize.x;
        int height = windowSize.y;
        //??????
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
