//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.mc.hubert.guide.core;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.mc.hubert.guide.NewbieGuide;
import com.mc.hubert.guide.listener.AnimationListenerAdapter;
import com.mc.hubert.guide.listener.OnLayoutInflatedListener;
import com.mc.hubert.guide.model.GuidePage;
import com.mc.hubert.guide.model.HighLight;
import com.mc.hubert.guide.model.HighlightOptions;
import com.mc.hubert.guide.model.RelativeGuide;

import java.util.Iterator;
import java.util.List;

public class GuideLayout extends FrameLayout {
    public static final int DEFAULT_BACKGROUND_COLOR = -1308622848;
    private Controller controller;
    private Paint mPaint;
    public GuidePage guidePage;
    private OnGuideLayoutDismissListener listener;
    private float downX;
    private float downY;
    private int touchSlop;

    public GuideLayout(Context context, GuidePage page, Controller controller) {
        super(context);
        this.init();
        this.setGuidePage(page);
        this.controller = controller;
    }

    private GuideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private GuideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        PorterDuffXfermode xfermode = new PorterDuffXfermode(Mode.CLEAR);
        this.mPaint.setXfermode(xfermode);
        this.mPaint.setMaskFilter(new BlurMaskFilter(1.0F, Blur.INNER));
        this.setLayerType(1, (Paint)null);
        this.setWillNotDraw(false);
        this.touchSlop = ViewConfiguration.get(this.getContext()).getScaledTouchSlop();
    }

    private void setGuidePage(GuidePage page) {
        this.guidePage = page;
        this.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (GuideLayout.this.guidePage.isEverywhereCancelable()) {
                    GuideLayout.this.remove();
                }

            }
        });
    }

    public boolean performClick() {
        return super.performClick();
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch(action) {
            case 0:
                this.downX = event.getX();
                this.downY = event.getY();
                break;
            case 1:
            case 3:
                float upX = event.getX();
                float upY = event.getY();
                if (Math.abs(upX - this.downX) < (float)this.touchSlop && Math.abs(upY - this.downY) < (float)this.touchSlop) {
                    List<HighLight> highLights = this.guidePage.getHighLights();
                    Iterator var6 = highLights.iterator();

                    while(var6.hasNext()) {
                        HighLight highLight = (HighLight)var6.next();
                        RectF rectF = highLight.getRectF((ViewGroup)this.getParent());
                        if (rectF.contains(upX, upY)) {
                            this.notifyClickListener(highLight);
                            return true;
                        }
                    }

                    this.performClick();
                }
            case 2:
        }

        return super.onTouchEvent(event);
    }

    private void notifyClickListener(HighLight highLight) {
        HighlightOptions options = highLight.getOptions();
        if (options != null && options.onClickListener != null) {
            options.onClickListener.onClick(this);
        }

    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int backgroundColor = this.guidePage.getBackgroundColor();
        canvas.drawColor(backgroundColor == 0 ? -1308622848 : backgroundColor);
        this.drawHighlights(canvas);
    }

    private void drawHighlights(Canvas canvas) {
        List<HighLight> highLights = this.guidePage.getHighLights();
        HighLight highLight;
        RectF rectF;
        if (highLights != null) {
            for(Iterator var3 = highLights.iterator(); var3.hasNext(); this.notifyDrewListener(canvas, highLight, rectF)) {
                highLight = (HighLight)var3.next();
                rectF = highLight.getRectF((ViewGroup)this.getParent());
                switch(highLight.getShape()) {
                    case CIRCLE:
                        canvas.drawCircle(rectF.centerX(), rectF.centerY(), highLight.getRadius(), this.mPaint);
                        break;
                    case OVAL:
                        canvas.drawOval(rectF, this.mPaint);
                        break;
                    case ROUND_RECTANGLE:
                        canvas.drawRoundRect(rectF, (float)highLight.getRound(), (float)highLight.getRound(), this.mPaint);
                        break;
                    case RECTANGLE:
                    default:
                        canvas.drawRect(rectF, this.mPaint);
                }
            }
        }

    }

    private void notifyDrewListener(Canvas canvas, HighLight highLight, RectF rectF) {
        HighlightOptions options = highLight.getOptions();
        if (options != null && options.onHighlightDrewListener != null) {
            options.onHighlightDrewListener.onHighlightDrew(canvas, rectF);
        }

    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.addCustomToLayout(this.guidePage);
        Animation enterAnimation = this.guidePage.getEnterAnimation();
        if (enterAnimation != null) {
            this.startAnimation(enterAnimation);
        }

    }

    private void addCustomToLayout(GuidePage guidePage) {
        removeAllViews();
        int layoutResId = guidePage.getLayoutResId();
        View layoutRes = guidePage.getLayoutRes();
        if (layoutResId != 0) {
            View view = LayoutInflater.from(getContext()).inflate(layoutResId, this, false);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            int[] viewIds = guidePage.getClickToDismissIds();
            if (viewIds != null && viewIds.length > 0) {
                for (int viewId : viewIds) {
                    View click = view.findViewById(viewId);
                    if (click != null) {
                        click.setOnClickListener(v -> remove());
                    } else {
                        Log.w(NewbieGuide.TAG, "can't find the view by id : " + viewId + " which used to remove guide page");
                    }
                }
            }
            OnLayoutInflatedListener inflatedListener = guidePage.getOnLayoutInflatedListener();
            if (inflatedListener != null) {
                inflatedListener.onLayoutInflated(view, controller);
            }
            addView(view, params);
        }
        else if (layoutRes != null) {
            LayoutParams params = new LayoutParams(-1, -1);
            int[] viewIds = guidePage.getClickToDismissIds();
            if (viewIds != null && viewIds.length > 0) {
                int length = viewIds.length;
                for(int i = 0; i < length; ++i) {
                    int viewId = viewIds[i];
                    View click = layoutRes.findViewById(viewId);
                    if (click != null) {
                        click.setOnClickListener(v -> GuideLayout.this.remove());
                    } else {
                        Log.w("NewbieGuide", "can't find the view by id : " + viewId + " which used to remove guide page");
                    }
                }
            }

            OnLayoutInflatedListener inflatedListener = guidePage.getOnLayoutInflatedListener();
            if (inflatedListener != null) {
                inflatedListener.onLayoutInflated(layoutRes, this.controller);
            }

            this.addView(layoutRes, params);
        }
        List<RelativeGuide> relativeGuides = guidePage.getRelativeGuides();
        if (relativeGuides.size() > 0) {
            for (RelativeGuide relativeGuide : relativeGuides) {
                addView(relativeGuide.getGuideLayout((ViewGroup) getParent(), controller));
            }
        }

    }

    public void setOnGuideLayoutDismissListener(OnGuideLayoutDismissListener listener) {
        this.listener = listener;
    }

    public void remove() {
        Animation exitAnimation = this.guidePage.getExitAnimation();
        if (exitAnimation != null) {
            exitAnimation.setAnimationListener(new AnimationListenerAdapter() {
                public void onAnimationEnd(Animation animation) {
                    GuideLayout.this.dismiss();
                }
            });
            this.startAnimation(exitAnimation);
        } else {
            this.dismiss();
        }

    }

    private void dismiss() {
        if (this.getParent() != null) {
            ((ViewGroup)this.getParent()).removeView(this);
            if (this.listener != null) {
                this.listener.onGuideLayoutDismiss(this);
            }
        }

    }

    public interface OnGuideLayoutDismissListener {
        void onGuideLayoutDismiss(GuideLayout var1);
    }
}
