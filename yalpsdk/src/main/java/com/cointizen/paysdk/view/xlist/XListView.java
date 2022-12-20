package com.cointizen.paysdk.view.xlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;

/**
 * 单个的下拉刷新控件
 *
 * @ClassName: XListView
 * @Description: TODO
 * @author Administrator
 * @date 2014-4-24 下午5:09:02
 */
@SuppressLint("ClickableViewAccessibility")
public class XListView extends ListView implements OnScrollListener {
    private final static String TAG = "XListView";
    private float mLastY = -1; // save event y
//    private float mLastAniY = -1; // save event y
    private Scroller mScroller; // used for scroll back
    private OnScrollListener mScrollListener; // user's scroll listener

    // the interface to trigger refresh and load more.
    private IXListViewListener mListViewListener;

    // -- header view
    private XListViewHeader mHeaderView;
    private boolean isStopRefash = true;
    // header view content, use it to calculate the Header's height. And hide it
    // when disable pull refresh.
    private RelativeLayout mHeaderViewContent;
    private int mHeaderViewHeight; // header view's height
    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false; // is refreashing.

    // -- footer view
    private XListViewFooter mFooterView = null;
    private boolean mEnablePullLoad = false;
    private boolean mPullLoading = false;

    // total list items, used to detect is at the bottom of listview.
    private int mTotalItemCount;

    // for mScroller, scroll back from header or footer.
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 400; // scroll back duration
    private final static int PULL_LOAD_MORE_DELTA = 30; // when pull up >= 30px
    // at bottom, trigger
    // load more.
    private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
    // feature.
    static final float FRICTION = 2.0f;

    private Context m_context;

    private boolean isScrollStop;

    /**
     * @param context
     */
    public XListView(Context context) {
        super(context);
        initWithContext(context, null);
    }

    public XListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context, attrs);
    }

    public XListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context, attrs);
    }

    private void initWithContext(Context context, AttributeSet attrs) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XListView need the scroll event, and it will dispatch the event to
        // user's listener (as a proxy).
        super.setOnScrollListener(this);

        m_context = context;
        isStopRefash = true;
        isScrollStop = true;
        // init header view
        mHeaderView = new XListViewHeader(context, attrs);
        mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(
        		MCHInflaterUtils.getControl(m_context, "xlistview_header_content"));
        addHeaderView(mHeaderView);

        // init header height
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                mHeaderViewHeight = mHeaderViewContent.getHeight();
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
//        Drawable imageDrawable = context.getResources().getDrawable(R.drawable.default_ptr_rotate);
//        mHeaderView.onLoadingDrawableSet(imageDrawable);
    }

    public boolean getPullLoading() {
        return this.mPullLoading;
    }

    public boolean getPullRefreshing() {
        return this.mPullRefreshing;
    }

    public boolean isScrollStop() {
        MCLog.e(TAG, "isScrollStop:" + isScrollStop);
        return isScrollStop;
    }

    public void pullRefreshing() {
        if (!mEnablePullRefresh) {
            return;
        }
        mHeaderView.setVisiableHeight(mHeaderViewHeight);
        mPullRefreshing = false;
        mHeaderView.setState(XListViewHeader.STATE_NORMAL);
    }

    /**
     * enable or disable pull down refresh feature.
     *
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) { // disable, hide the content
            mHeaderViewContent.setVisibility(View.INVISIBLE);
        } else {
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * enable or disable pull up load more feature.
     *
     * @param enable
     */
    public void setPullLoadEnable(boolean enable) {
        if (mEnablePullLoad == enable)
            return;

        mEnablePullLoad = enable;
        if (!mEnablePullLoad) {
            if (mFooterView != null) {
                this.removeFooterView(mFooterView);
            }
        } else {
            // mPullLoading = false;
            if (mFooterView == null) {
                mFooterView = new XListViewFooter(m_context);
                mFooterView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startLoadMore();
                    }
                });
            }
            this.addFooterView(mFooterView);
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
            // both "pull up" and "click" will invoke load more.
        }
    }

    /**
     * stop refresh, reset header view.
     */
    public void stopRefresh() {
        Time time = new Time();
        time.setToNow();
        mHeaderView.setRefreshTime(time.format("%Y-%m-%d %T"));
        if (mPullRefreshing == true) {
            mPullRefreshing = false;
            resetHeaderHeight(true);
        }
    }

    /**
     * stop load more, reset footer view.
     */
    public void stopLoadMore() {
        if (mPullLoading == true) {
            mPullLoading = false;
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
        }
    }

    public void hideFooterView() {
        if (null != mFooterView) {
            mFooterView.hide();
        }
    }

    public void showFooterView() {
        if (null != mFooterView) {
            mFooterView.show();
        }
    }

    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisiableHeight((int) delta + mHeaderView.getVisiableHeight());
        if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
            // SuningLog.e(TAG, mHeaderView.getVisiableHeight() + ">" + mHeaderViewHeight +
            // " is " + (mHeaderView.getVisiableHeight() > mHeaderViewHeight));
            if (mHeaderView.getVisiableHeight() > (mHeaderViewHeight - 20)) {
                mHeaderView.setState(XListViewHeader.STATE_READY);
            } else {
                mHeaderView.setState(XListViewHeader.STATE_NORMAL);
            }
        }
        // setSelection(0); // scroll to top each time
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight(boolean isCan) {
        int height = mHeaderView.getVisiableHeight();
        if (height == 0) // not visible.
            return;
        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderViewHeight) {
            return;
        }
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > mHeaderViewHeight) {
            finalHeight = mHeaderViewHeight;
        }

        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        if (isCan) {
            isStopRefash = true;
        }
        // trigger computeScroll
        invalidate();
    }

    private void updateFooterHeight(float delta) {
        int height = mFooterView.getBottomMargin() + (int) delta;
//        Log.e(TAG, "height = " + height + " --" + PULL_LOAD_MORE_DELTA);
        if (mEnablePullLoad && !mPullLoading) {
            if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load // more.
                mFooterView.setState(XListViewFooter.STATE_READY);
            } else {
                mFooterView.setState(XListViewFooter.STATE_NORMAL);
            }
        }
        mFooterView.setBottomMargin(height);
        // setSelection(mTotalItemCount - 1); // scroll to bottom
    }

    private void resetFooterHeight() {
        int bottomMargin = mFooterView.getBottomMargin();
        if (bottomMargin > 0) {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
            invalidate();
        }
    }

    private void startLoadMore() {
        mPullLoading = true;
        mFooterView.setState(XListViewFooter.STATE_LOADING);
        if (mListViewListener != null) {
            mListViewListener.onLoadMore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
//        int newScrollValue = 0;
        // float deltaY = 0;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
//            	mLastAniY = ev.getRawY();
                mLastY  = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                // float aniDeltaY = mLastY - ev.getRawY();
                mLastY = ev.getRawY();
//                newScrollValue = Math.round(Math.max(mLastY - mLastAniY, 0) / FRICTION);
                // SuningLog.e("XListViewHeader", "mLastY = " + mLastY + " mLastAniY = " + mLastAniY);
                // SuningLog.e("XListViewHeader", "newScrollValue = " + newScrollValue);
                if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    invokeOnScrolling();
//                    float scale = Math.abs(newScrollValue) / (float) mHeaderViewHeight;
//                    mHeaderView.onPullImpl(scale);
                } else if (mEnablePullLoad && (getLastVisiblePosition() == mTotalItemCount - 1)
                        && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
                    // last item, already pulled up or want to pull up.
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
                }
                break;
            default:
//                Log.e(TAG, "LastVisiblePosition = " + getLastVisiblePosition() + " mTotalItemCount = "
//                        + mTotalItemCount);
                mLastY = -1; // reset
                if (getFirstVisiblePosition() == 0) {
                    // invoke refresh
                    if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight && isStopRefash) {
                        isStopRefash = false;
                        mPullRefreshing = true;
                        mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
                        if (mListViewListener != null) {
                            mListViewListener.onRefresh();
                        }
                    }
                    resetHeaderHeight(false);

                    if (mEnablePullLoad) {
//                        Log.e(TAG, "BottomMargin = " + mFooterView.getBottomMargin());
                        if (mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
                            startLoadMore();
                        }
                        resetFooterHeight();
                    }
                } else if (getLastVisiblePosition() == mTotalItemCount - 1) {
                    // invoke load more.
//                    Log.e(TAG, "mEnablePullLoad = " + mEnablePullLoad + " footScroll = " + footScroll);
                    if (mEnablePullLoad) {
                        if (mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
                            startLoadMore();
                        }
                        resetFooterHeight();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            } else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
            // setSelection(0); // scroll to top each time
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
        if(SCROLL_STATE_IDLE == scrollState){
            isScrollStop  = true;
        }else{
            isScrollStop  = false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // send to user's listener

        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public void setXListViewListener(IXListViewListener l) {
        mListViewListener = l;
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        public void onXScrolling(View view);
    }

    /**
     * implements this interface to get refresh/load more event.
     */
    public interface IXListViewListener {
        public void onRefresh();

        public void onLoadMore();
    }
}
