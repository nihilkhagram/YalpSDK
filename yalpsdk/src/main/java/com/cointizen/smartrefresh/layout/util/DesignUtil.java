package com.cointizen.smartrefresh.layout.util;
import android.view.View;

import com.cointizen.smartrefresh.layout.api.RefreshKernel;
import com.cointizen.smartrefresh.layout.listener.CoordinatorLayoutListener;

/**
 * Design 兼容包缺省尝试
 * Created by SCWANG on 2018/1/29.
 */

public class DesignUtil {

    public static void checkCoordinatorLayout(View content, RefreshKernel kernel, CoordinatorLayoutListener listener) {
        try {//try 不能删除，不然会出现兼容性问题
        } catch (Throwable ignored) {
        }
    }


}
