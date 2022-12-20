package com.cointizen.smartrefresh.layout.header;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cointizen.smartrefresh.layout.api.RefreshHeader;
import com.cointizen.smartrefresh.layout.api.RefreshLayout;
import com.cointizen.smartrefresh.layout.constant.RefreshState;
import com.cointizen.smartrefresh.layout.internal.ArrowDrawable;
import com.cointizen.smartrefresh.layout.internal.InternalClassics;
import com.cointizen.smartrefresh.layout.internal.ProgressDrawable;
import com.cointizen.smartrefresh.layout.util.DensityUtil;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import com.cointizen.smartrefresh.AppConstants;

/**
 * 经典下拉头部
 * Created by SCWANG on 2017/5/28.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ClassicsHeader extends InternalClassics<ClassicsHeader> implements RefreshHeader {

    public static final byte ID_TEXT_UPDATE = 4;

    public static String REFRESH_HEADER_PULLING = null;//AppConstants.STR_76985db7270fb8bc2add09291b637964;
    public static String REFRESH_HEADER_REFRESHING = null;//AppConstants.STR_a58e0b582cc0bf54b5def01e9db75fee;
    public static String REFRESH_HEADER_LOADING = null;//AppConstants.STR_bd0271eda19a0ddfff4a605d63c82e0f;
    public static String REFRESH_HEADER_RELEASE = null;//AppConstants.STR_bb045b7b0ce191f0568fb4d0a9858b8d;
    public static String REFRESH_HEADER_FINISH = null;//AppConstants.STR_c7b477fe1e0f79c3f0859c6a467f9d0b;
    public static String REFRESH_HEADER_FAILED = null;//AppConstants.STR_245c7a0541c033776b61a33bda10bd99;
    public static String REFRESH_HEADER_UPDATE = null;//AppConstants.STR_9b401510e0236451de9a33af7d2385ba;
    public static String REFRESH_HEADER_SECONDARY = null;//AppConstants.STR_df326830aa3f477fd0a4a06c738a519a;
//    public static String REFRESH_HEADER_UPDATE = "'Last update' M-d HH:mm";

    protected String KEY_LAST_UPDATE_TIME = "LAST_UPDATE_TIME";

    protected Date mLastTime;
    protected TextView mLastUpdateText;
    protected SharedPreferences mShared;
    protected DateFormat mLastUpdateFormat;
    protected boolean mEnableLastTime = true;

    //<editor-fold desc="RelativeLayout">
    public ClassicsHeader(Context context) {
        this(context, null);
    }

    public ClassicsHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicsHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (REFRESH_HEADER_PULLING == null) {
            REFRESH_HEADER_PULLING = context.getString(MCHInflaterUtils.getIdByName(context,"string","mch_srl_header_pulling"));
        }
        if (REFRESH_HEADER_REFRESHING == null) {
            REFRESH_HEADER_REFRESHING = context.getString(MCHInflaterUtils.getIdByName(context,"string","mch_srl_header_refreshing"));
        }
        if (REFRESH_HEADER_LOADING == null) {
            REFRESH_HEADER_LOADING = context.getString(MCHInflaterUtils.getIdByName(context,"string","mch_srl_header_loading"));
        }
        if (REFRESH_HEADER_RELEASE == null) {
            REFRESH_HEADER_RELEASE = context.getString(MCHInflaterUtils.getIdByName(context,"string","mch_srl_header_release"));
        }
        if (REFRESH_HEADER_FINISH == null) {
            REFRESH_HEADER_FINISH = context.getString(MCHInflaterUtils.getIdByName(context,"string","mch_srl_header_finish"));
        }
        if (REFRESH_HEADER_FAILED == null) {
            REFRESH_HEADER_FAILED = context.getString(MCHInflaterUtils.getIdByName(context,"string","mch_srl_header_failed"));
        }
        if (REFRESH_HEADER_UPDATE == null) {
            REFRESH_HEADER_UPDATE = context.getString(MCHInflaterUtils.getIdByName(context,"string","mch_srl_header_update"));
        }
        if (REFRESH_HEADER_SECONDARY == null) {
            REFRESH_HEADER_SECONDARY = context.getString(MCHInflaterUtils.getIdByName(context,"string","mch_srl_header_secondary"));
        }

        mLastUpdateText = new TextView(context);
        mLastUpdateText.setTextColor(0xff7c7c7c);
        mLastUpdateFormat = new SimpleDateFormat(REFRESH_HEADER_UPDATE, Locale.getDefault());

        final View thisView = this;
        final View arrowView = mArrowView;
        final View updateView = mLastUpdateText;
        final View progressView = mProgressView;
        final ViewGroup centerLayout = mCenterLayout;
        final DensityUtil density = new DensityUtil();

//        TypedArray ta = context.obtainStyledAttributes(attrs, MCHInflaterUtils.getStyleableIntArray(context,"ClassicsHeader"));
//
//        LayoutParams lpArrow = (LayoutParams) arrowView.getLayoutParams();
//        LayoutParams lpProgress = (LayoutParams) progressView.getLayoutParams();
        LinearLayout.LayoutParams lpUpdateText = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
//        lpUpdateText.topMargin = ta.getDimensionPixelSize(MCHInflaterUtils.getStyleableFieldId(context,
//                "styleable","ClassicsHeader_srlTextTimeMarginTop"), density.dip2px(0));
//        lpProgress.rightMargin = ta.getDimensionPixelSize(MCHInflaterUtils.getStyleableFieldId(context,
//                "styleable","ClassicsFooter_srlDrawableMarginRight"), density.dip2px(20));
//        lpArrow.rightMargin = lpProgress.rightMargin;
//
//        lpArrow.width = ta.getLayoutDimension(MCHInflaterUtils.getStyleableFieldId(context,
//                "styleable","ClassicsHeader_srlDrawableArrowSize"), lpArrow.width);
//        lpArrow.height = ta.getLayoutDimension(MCHInflaterUtils.getStyleableFieldId(context,
//                "styleable","ClassicsHeader_srlDrawableArrowSize"), lpArrow.height);
//        lpProgress.width = ta.getLayoutDimension(MCHInflaterUtils.getStyleableFieldId(context,
//                "styleable","ClassicsHeader_srlDrawableProgressSize"), lpProgress.width);
//        lpProgress.height = ta.getLayoutDimension(MCHInflaterUtils.getStyleableFieldId(context,
//                "styleable","ClassicsHeader_srlDrawableProgressSize"), lpProgress.height);
//
//        lpArrow.width = ta.getLayoutDimension(MCHInflaterUtils.getStyleableFieldId(context,
//                "styleable","ClassicsHeader_srlDrawableSize"), lpArrow.width);
//        lpArrow.height = ta.getLayoutDimension(MCHInflaterUtils.getStyleableFieldId(context,
//                "styleable","ClassicsHeader_srlDrawableSize"), lpArrow.height);
//        lpProgress.width = ta.getLayoutDimension(MCHInflaterUtils.getStyleableFieldId(context,
//                "styleable","ClassicsHeader_srlDrawableSize"), lpProgress.width);
//        lpProgress.height = ta.getLayoutDimension(MCHInflaterUtils.getStyleableFieldId(context,
//                "styleable","ClassicsHeader_srlDrawableSize"), lpProgress.height);
//
//        mFinishDuration = ta.getInt(MCHInflaterUtils.getStyleableFieldId(context,
//                "styleable","ClassicsHeader_srlFinishDuration"), mFinishDuration);
//        mEnableLastTime = ta.getBoolean(MCHInflaterUtils.getStyleableFieldId(context,
//                "styleable","ClassicsHeader_srlEnableLastTime"), mEnableLastTime);
//        mSpinnerStyle = SpinnerStyle.values()[ta.getInt(MCHInflaterUtils.getStyleableFieldId(context,
//                "styleable","ClassicsHeader_srlClassicsSpinnerStyle"),mSpinnerStyle.ordinal())];
//
//        if (ta.hasValue(MCHInflaterUtils.getStyleableFieldId(context,
//                "styleable","ClassicsHeader_srlDrawableArrow"))) {
//            mArrowView.setImageDrawable(ta.getDrawable(MCHInflaterUtils.getStyleableFieldId(context,
//                    "styleable","ClassicsHeader_srlDrawableArrow")));
//        } else {
            mArrowDrawable = new ArrowDrawable();
            mArrowDrawable.setColor(0xff666666);
            mArrowView.setImageDrawable(mArrowDrawable);
//        }
//
//        if (ta.hasValue(MCHInflaterUtils.getStyleableFieldId(context,
//                "styleable","ClassicsHeader_srlDrawableProgress"))) {
//            mProgressView.setImageDrawable(ta.getDrawable(MCHInflaterUtils.getStyleableFieldId(context,
//                    "styleable","ClassicsHeader_srlDrawableProgress")));
//        } else {
            mProgressDrawable = new ProgressDrawable();
            mProgressDrawable.setColor(0xff666666);
            mProgressView.setImageDrawable(mProgressDrawable);
//        }
//
//        if (ta.hasValue(MCHInflaterUtils.getStyleableFieldId(context,
//                "styleable","ClassicsHeader_srlTextSizeTitle"))) {
//            mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ta.getDimensionPixelSize(MCHInflaterUtils.getStyleableFieldId(context,
//                    "styleable","ClassicsHeader_srlTextSizeTitle"), DensityUtil.dp2px(16)));
//        } else {
            mTitleText.setTextSize(16);
//        }
//
//        if (ta.hasValue(MCHInflaterUtils.getStyleableFieldId(context,
//                "styleable","ClassicsHeader_srlTextSizeTime"))) {
//            mLastUpdateText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ta.getDimensionPixelSize(MCHInflaterUtils.getStyleableFieldId(context,
//                    "styleable","ClassicsHeader_srlTextSizeTime"), DensityUtil.dp2px(12)));
//        } else {
            mLastUpdateText.setTextSize(12);
//        }
//
//        if (ta.hasValue(MCHInflaterUtils.getStyleableFieldId(context,
//                "styleable","ClassicsHeader_srlPrimaryColor"))) {
//            setPrimaryColor(ta.getColor(MCHInflaterUtils.getStyleableFieldId(context,
//                    "styleable","ClassicsHeader_srlPrimaryColor"), 0));
//        }
//        if (ta.hasValue(MCHInflaterUtils.getStyleableFieldId(context,
//                "styleable","ClassicsHeader_srlAccentColor"))) {
//            setAccentColor(ta.getColor(MCHInflaterUtils.getStyleableFieldId(context,
//                    "styleable","ClassicsHeader_srlAccentColor"), 0));
//        }
//
//        ta.recycle();

        updateView.setId(ID_TEXT_UPDATE);
        updateView.setVisibility(mEnableLastTime ? VISIBLE : GONE);
        centerLayout.addView(updateView, lpUpdateText);
        mTitleText.setText(thisView.isInEditMode() ? REFRESH_HEADER_REFRESHING : REFRESH_HEADER_PULLING);

        try {//try 不能删除-否则会出现兼容性问题
            if (context instanceof FragmentActivity) {
                FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                if (manager != null) {
                    @SuppressLint("RestrictedApi")
                    List<Fragment> fragments = manager.getFragments();
                    if (fragments != null && fragments.size() > 0) {
                        setLastUpdateTime(new Date());
                        return;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        KEY_LAST_UPDATE_TIME += context.getClass().getName();
        mShared = context.getSharedPreferences("mch_ClassicsHeader", Context.MODE_PRIVATE);
        setLastUpdateTime(new Date(mShared.getLong(KEY_LAST_UPDATE_TIME, System.currentTimeMillis())));

    }

//    @Override
//    protected ClassicsHeader self() {
//        return this;
//    }

    //</editor-fold>

    //<editor-fold desc="RefreshHeader">

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        if (success) {
            mTitleText.setText(REFRESH_HEADER_FINISH);
            if (mLastTime != null) {
                setLastUpdateTime(new Date());
            }
        } else {
            mTitleText.setText(REFRESH_HEADER_FAILED);
        }
        return super.onFinish(layout, success);//延迟500毫秒之后再弹回
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        final View arrowView = mArrowView;
        final View updateView = mLastUpdateText;
        switch (newState) {
            case None:
                updateView.setVisibility(mEnableLastTime ? VISIBLE : GONE);
            case PullDownToRefresh:
                mTitleText.setText(REFRESH_HEADER_PULLING);
                arrowView.setVisibility(VISIBLE);
                arrowView.animate().rotation(0);
                break;
            case Refreshing:
            case RefreshReleased:
                mTitleText.setText(REFRESH_HEADER_REFRESHING);
                arrowView.setVisibility(GONE);
                break;
            case ReleaseToRefresh:
                mTitleText.setText(REFRESH_HEADER_RELEASE);
                arrowView.animate().rotation(180);
                break;
            case ReleaseToTwoLevel:
                mTitleText.setText(REFRESH_HEADER_SECONDARY);
                arrowView.animate().rotation(0);
                break;
            case Loading:
                arrowView.setVisibility(GONE);
                updateView.setVisibility(mEnableLastTime ? INVISIBLE : GONE);
                mTitleText.setText(REFRESH_HEADER_LOADING);
                break;
        }
    }
    //</editor-fold>

    //<editor-fold desc="API">

    public ClassicsHeader setLastUpdateTime(Date time) {
        final View thisView = this;
        mLastTime = time;
        mLastUpdateText.setText("Last update: " + mLastUpdateFormat.format(time));
        if (mShared != null && !thisView.isInEditMode()) {
            mShared.edit().putLong(KEY_LAST_UPDATE_TIME, time.getTime()).apply();
        }
        return this;
    }

    public ClassicsHeader setTimeFormat(DateFormat format) {
        mLastUpdateFormat = format;
        if (mLastTime != null) {
            mLastUpdateText.setText("Last update: " + mLastUpdateFormat.format(mLastTime));
        }
        return this;
    }

    public ClassicsHeader setLastUpdateText(CharSequence text) {
        mLastTime = null;
        mLastUpdateText.setText(text);
        return this;
    }

    public ClassicsHeader setAccentColor(@ColorInt int accentColor) {
        mLastUpdateText.setTextColor(accentColor&0x00ffffff|0xcc000000);
        return super.setAccentColor(accentColor);
    }

    public ClassicsHeader setEnableLastTime(boolean enable) {
        final View updateView = mLastUpdateText;
        mEnableLastTime = enable;
        updateView.setVisibility(enable ? VISIBLE : GONE);
        if (mRefreshKernel != null) {
            mRefreshKernel.requestRemeasureHeightFor(this);
//            mRefreshKernel.requestRemeasureHeightForHeader();
        }
        return this;
    }

    public ClassicsHeader setTextSizeTime(float size) {
        mLastUpdateText.setTextSize(size);
        if (mRefreshKernel != null) {
            mRefreshKernel.requestRemeasureHeightFor(this);
//            mRefreshKernel.requestRemeasureHeightForHeader();
        }
        return this;
    }

//    public ClassicsHeader setTextSizeTime(int unit, float size) {
//        mLastUpdateText.setTextSize(unit, size);
//        if (mRefreshKernel != null) {
//            mRefreshKernel.requestRemeasureHeightForHeader();
//        }
//        return this;
//    }

    public ClassicsHeader setTextTimeMarginTop(float dp) {
        final View updateView = mLastUpdateText;
        MarginLayoutParams lp = (MarginLayoutParams)updateView.getLayoutParams();
        lp.topMargin = DensityUtil.dp2px(dp);
        updateView.setLayoutParams(lp);
        return this;
    }

//    public ClassicsHeader setTextTimeMarginTopPx(int px) {
//        MarginLayoutParams lp = (MarginLayoutParams)mLastUpdateText.getLayoutParams();
//        lp.topMargin = px;
//        mLastUpdateText.setLayoutParams(lp);
//        return this;
//    }

//    /**
//     * @deprecated 使用 findViewById(ID_TEXT_UPDATE) 代替
//     */
//    @Deprecated
//    public TextView getLastUpdateText() {
//        return mLastUpdateText;
//    }

    //</editor-fold>

}
