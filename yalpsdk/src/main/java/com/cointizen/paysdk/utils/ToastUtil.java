package com.cointizen.paysdk.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.cointizen.open.YalpGamesSdk;
import com.cointizen.paysdk.view.util.MCHToast;

public class ToastUtil {
	   private static String oldMsg;  
	    protected static MCHToast toast = null;
	    private static long oldTime = 0;
	    private static long currentTime = 0;
	      
	    private static void showToast(Context context, String s){
			if (android.os.Build.VERSION.SDK_INT >= 28){
				MCHToast.makeText(context, s, Toast.LENGTH_SHORT).show();
			}else {
				if(toast == null){
					toast = MCHToast.makeText(context, s, Toast.LENGTH_SHORT);
					toast.show();
					oldTime = System.currentTimeMillis();
				}else{
					currentTime = System.currentTimeMillis();
					if(s.equals(oldMsg)){
						if((currentTime - oldTime) > Toast.LENGTH_SHORT){
							toast.show();
						}
					}else{
						oldMsg = s;
						toast.setText(s);
						toast.show();
					}
				}
				oldTime = currentTime;
			}
	    }

	public static void show(String msg){
		showToast(YalpGamesSdk.getMainActivity(), msg);
	}

	public static void show(Context context,String msg){
	    	showToast(context, msg);
	    }

	    public static void showToast(Context context, int resId){     
	        showToast(context, context.getString(resId));  
	    }  
	    
	    public static void showToastInThread(final Activity context,final String msg){
			context.runOnUiThread(() -> {
				Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
				toast.show();
			});
		}
}
