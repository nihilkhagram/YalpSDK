package com.cointizen.smartrefresh.layout.listener;


import androidx.annotation.NonNull;

import com.cointizen.smartrefresh.layout.api.RefreshLayout;
import com.cointizen.smartrefresh.layout.constant.RefreshState;

//import android.support.annotation.RestrictTo;

//import static android.support.annotation.RestrictTo.Scope.LIBRARY;
//import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;
//import static android.support.annotation.RestrictTo.Scope.SUBCLASSES;

/**
 * 刷新状态改变监听器
 * Created by SCWANG on 2017/5/26.
 */

public interface OnStateChangedListener {
    /**
     * 状态改变事件 {@link RefreshState}
     * @param refreshLayout RefreshLayout
     * @param oldState 改变之前的状态
     * @param newState 改变之后的状态
     */
//    @RestrictTo({LIBRARY,LIBRARY_GROUP,SUBCLASSES})
    void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState);
}
