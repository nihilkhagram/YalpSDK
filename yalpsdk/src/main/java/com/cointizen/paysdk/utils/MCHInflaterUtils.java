package com.cointizen.paysdk.utils;


import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class MCHInflaterUtils {

	private static final String TAG = "MCHInflaterUtils";

	/**
	 * 获取布局文件
	 *
	 * @param con
	 *            上下文
	 * @param layoutName
	 *            布局文件名称
	 * @return
	 */
	public static int getLayout(Context con, String layoutName) {
		return getIdByName(con, "layout", layoutName);
	}

	/**
	 * 获取控件
	 *
	 * @param con
	 *            上下文
	 * @param controlName
	 *            控件名称
	 * @return
	 */
	public static int getControl(Context con, String controlName) {
		return getIdByName(con, "id", controlName);
	}

	/**
	 * 获取资源文件
	 *
	 * @param con
	 *            上下文
	 * @param drawableName
	 *            资源名称
	 * @return
	 */
	public static int getDrawable(Context con, String drawableName) {
		return getIdByName(con, "drawable", drawableName);
	}

	/**
	 * 获取控件
	 *
	 * @param con
	 *            上下文
	 * @param controlName
	 *            控件名称
	 * @return
	 */
	public static int getColor(Context con, String controlName) {
		return getIdByName(con, "color", controlName);
	}

	/**
	 * Refer to external project resources
	 *
	 * @param context
	 * @param className
	 * @param name
	 * @return
	 */
	private static int getIdByName1(Context context, String className,
			String name) {
		String packageName = null;
		Class<?> r = null;
		int id = 0;
		try {
			if(context==null){
				MCLog.e(UtilsConstants.Log_GLOJNICgBk,UtilsConstants.Log_GLOJNICgBk);
			}
			packageName = context.getPackageName();
			//MCLog.w(TAG, "packageName:"+packageName);
			r = Class.forName(packageName + ".R");
			Class<?>[] classes = r.getClasses();
			Class<?> desireClass = null;
			for (int i = 0; i < classes.length; ++i) {
				if (classes[i].getName().split("\\$")[1].equals(className)) {
					desireClass = classes[i];
					break;
				}
			}
			if (desireClass != null) {
				id = desireClass.getField(name).getInt(desireClass);
			}
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return id;
	}

	private static int getIdByName2(Context context, String className,
			String name) {
		Resources res = null;
		int id = 0;
		try {
			res = context.getResources();
			id = res.getIdentifier(name, className, context.getPackageName());
		} catch (Exception e) {
			MCLog.v(TAG, "getIdByName2 ClassNotFoundException-" + className);
			e.printStackTrace();
		}
		return id;
	}

	/**
	 *
	 * @param context
	 *            上下文
	 * @param className
	 *            类名
	 * @param name
	 *            属性名
	 * @return
	 */
	public static int getIdByName(Context context, String className, String name) {
		int id = -1;
		int id1 = getIdByName1(context, className, name);
		int id2 = getIdByName2(context, className, name);
		if (id2 != 1 && id2 != 0) {
			id = id2;
		}else if (id1 != 0) {
			id = id1;
		}else {
			MCLog.e(TAG, UtilsConstants.Log_byrYWBHnAf+"className:"+className+";   name:"+name);
		}
		return id;
	}

	public static int[] getStyleableIntArray(Context context, String name) {
		try {
			Field[] fields = Class.forName(context.getPackageName() + ".R$styleable").getFields();//.与$ difference,$表示R的子类
			for (Field field : fields) {
				if (field.getName().equals(name)) {
					int ret[] = (int[]) field.get(null);
					return ret;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 遍历R类得到styleable数组资源下的子资源，1.先找到R类下的styleable子类，2.遍历styleable类获得字段值
	 *
	 * @param context
	 * @param styleableName
	 * @param styleableFieldName
	 * @return
	 */
	public static int getStyleableFieldId(Context context, String styleableName, String styleableFieldName) {
		String className = context.getPackageName() + ".R";
		String type = "styleable";
		String name = styleableName + "_" + styleableFieldName;
		try {
			Class<?> cla = Class.forName(className);
			for (Class<?> childClass : cla.getClasses()) {
				String simpleName = childClass.getSimpleName();
				if (simpleName.equals(type)) {
					for (Field field : childClass.getFields()) {
						String fieldName = field.getName();
						if (fieldName.equals(name)) {
							return (int) field.get(null);
						}
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return 0;
	}




	/**
	 * @param context
	 * @param type
	 * @param name
	 * @return
	 */
	public static Object getResourceData(Context context, String type, String name) {
		try {
			Class<?> arrayClass = getResourceClass(context, type).getClass();
			Field intField = arrayClass.getField(name);
			return intField.get(arrayClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static HashMap<String, Object> ResourceClass = new HashMap<String, Object>();

	private static Object getResourceClass(Context context, String type) {
		if (ResourceClass.containsKey(type)) {
			return ResourceClass.get(type);
		} else {
			try {
				Class<?> resource = Class.forName(context.getPackageName() + ".R");
				Class<?>[] classes = resource.getClasses();
				for (Class<?> c : classes) {
					int i = c.getModifiers();
					String className = c.getName();
					String s = Modifier.toString(i);
					if (s.contains("static") && className.contains(type)) {
						ResourceClass.put(type, c.getConstructor().newInstance());
						return ResourceClass.get(type);
					} else {
						continue;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
