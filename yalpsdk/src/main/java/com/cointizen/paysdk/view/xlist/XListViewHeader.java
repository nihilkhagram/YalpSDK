package com.cointizen.paysdk.view.xlist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.view.ViewConstants;

public class XListViewHeader extends LinearLayout{
//	private final static String TAG = "XListViewHeader";

	Context context;
	
	private LinearLayout mContainer;
	private ImageView mArrowImageView;
	//	private TextView mHeaderTimeView;
	private ProgressBar mProgressBar;
	private TextView mHintTextView;

	//	private TextView mHeaderTimeLabel;
	private int mState = STATE_NORMAL;

//	private Animation mRotateUpAnim;
//	private Animation mRotateDownAnim;

//	private final int ROTATE_ANIM_DURATION = 180;

	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;


	static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
	static final int ROTATION_ANIMATION_DURATION = 1000 * 2;//1200
//	protected ImageView mHeaderImage;
//	private Matrix mHeaderImageMatrix;
//	private Animation mRotateAnimation;
//	private float mRotationPivotX, mRotationPivotY;
//	private boolean mRotateDrawableWhilePulling;
//	private boolean mUseIntrinsicAnimation;

	public XListViewHeader(Context context) {
		super(context);
		initView(context, null);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public XListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs);
	}

	private void initView(Context context, AttributeSet attrs) {
		this.context = context;
		// 初始情况，设置下拉刷新view高度为0
		LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
				MCHInflaterUtils.getLayout(context, "mch_xlistview_header"), null);
		addView(mContainer, lp);
		setGravity(Gravity.BOTTOM);

		mArrowImageView = (ImageView)findViewById(
				MCHInflaterUtils.getControl(context, "xlistview_header_arrow"));
//		mHintTextView = (TextView)findViewById(
//				MCHInflaterUtils.getControl(context, "xlistview_header_tip_textview"));
		mHintTextView = (TextView)findViewById(
				MCHInflaterUtils.getControl(context, "xlistview_header_hint_textview"));
		mProgressBar = (ProgressBar)findViewById(
				MCHInflaterUtils.getControl(context, "xlistview_header_progressbar"));
//		mHeaderTimeView = (TextView)findViewById(R.id.xlistview_header_time);
//		mHeaderTimeLabel = (TextView)findViewById(R.id.xlistview_header_time_label);



//		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
//				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//				0.5f);
//		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
//		mRotateUpAnim.setFillAfter(true);
//		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
//				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//				0.5f);
//		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
//		mRotateDownAnim.setFillAfter(true);

//		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullToRefresh);
//		initHeaderAnimation(a);
	}

//	private void initHeaderAnimation(TypedArray attrs) {
//		mRotateDrawableWhilePulling = attrs.getBoolean(R.styleable.PullToRefresh_ptrRotateDrawableWhilePulling, true);
//
//		mHeaderImage = (ImageView)findViewById(R.id.pull_to_refresh_image);
//		mHeaderImage.setScaleType(ScaleType.MATRIX);
//		mHeaderImageMatrix = new Matrix();
//		mHeaderImage.setImageMatrix(mHeaderImageMatrix);
//
//		mRotateAnimation = new RotateAnimation(0, -720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//				0.5f);
//		mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
//		mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
//		mRotateAnimation.setRepeatCount(Animation.INFINITE);
//		mRotateAnimation.setRepeatMode(Animation.RESTART);
//	}

//	public void onLoadingDrawableSet(Drawable imageDrawable) {
//		mHeaderImage.setImageDrawable(imageDrawable);
//		mUseIntrinsicAnimation = (imageDrawable instanceof AnimationDrawable);
//		if (null != imageDrawable) {
//			mRotationPivotX = imageDrawable.getIntrinsicWidth() / 2f;
//			mRotationPivotY = imageDrawable.getIntrinsicHeight() / 2f;
//		}
//	}

//	protected void onPullImpl(float scaleOfLayout) {
//		float angle;
//		if (mRotateDrawableWhilePulling) {
//			angle = scaleOfLayout * 90f;
//		} else {
//			angle = Math.max(0f, Math.min(180f, scaleOfLayout * 360f - 180f));
//		}
////        SuningLog.e(TAG, "scaleOfLayout = " + scaleOfLayout + " angle = " + angle);
//		mHeaderImageMatrix.setRotate(angle, mRotationPivotX, mRotationPivotY);
//		mHeaderImage.setImageMatrix(mHeaderImageMatrix);
//	}

//	private void resetImageRotation() {
//		if (null != mHeaderImageMatrix) {
//			mHeaderImageMatrix.reset();
//			mHeaderImage.setImageMatrix(mHeaderImageMatrix);
//		}
//	}

	public void setState(int state) {
		if (state == mState) return ;

//		if (state == STATE_REFRESHING) {	// 显示进度
//		    mArrowImageView.setImageResource(R.drawable.lion_open);
//			mArrowImageView.clearAnimation();
//			mArrowImageView.setVisibility(View.INVISIBLE);
//			mProgressBar.setVisibility(View.VISIBLE);
//		} else {	// 显示箭头图片
//		    mArrowImageView.setImageResource(R.drawable.lion_close);
//			mArrowImageView.setVisibility(View.VISIBLE);
//			mProgressBar.setVisibility(View.INVISIBLE);
//		}

		switch(state){
			case STATE_NORMAL:
				mProgressBar.setVisibility(View.GONE);
				mArrowImageView.setVisibility(View.VISIBLE);
				mArrowImageView.setImageResource(
						MCHInflaterUtils.getDrawable(context, "mch_xlistview_down_arrow"));
//			if (mState == STATE_READY) {
//			    mArrowImageView.setImageResource(R.drawable.lion_open);
////				mArrowImageView.startAnimation(mRotateDownAnim);
//			}
//			if (mState == STATE_REFRESHING) {
////				mArrowImageView.clearAnimation();
//				mArrowImageView.setImageResource(R.drawable.lion_open);
//			}
				mHintTextView.setText(ViewConstants.S_ZBYTpEJpIs);
				break;
			case STATE_READY:
				mProgressBar.setVisibility(View.GONE);
				mArrowImageView.setVisibility(View.VISIBLE);
				mArrowImageView.setImageResource(MCHInflaterUtils.getDrawable(context, "mch_xlistview_up_arrow"));
				mHintTextView.setText(ViewConstants.S_FuyujJcnaF);
//			if (mState != STATE_READY) {
//			    mArrowImageView.setImageResource(R.drawable.lion_close);
////				mArrowImageView.clearAnimation();
////				mArrowImageView.startAnimation(mRotateUpAnim);
//				mHintTextView.setText(R.string.xlistview_header_hint_ready);
//			}
				break;
			case STATE_REFRESHING:
				mArrowImageView.setVisibility(View.GONE);
//				mArrowImageView.setImageResource(R.drawable.lion_open);
				mProgressBar.setVisibility(View.VISIBLE);
//				if (mUseIntrinsicAnimation) {
//					((AnimationDrawable) mHeaderImage.getDrawable()).start();
//				} else {
//					mHeaderImage.startAnimation(mRotateAnimation);
//				}
				mHintTextView.setText(ViewConstants.S_STXCgSxJOG);
				break;
			default:
		}

		mState = state;
	}

	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LayoutParams lp = (LayoutParams) mContainer
				.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getVisiableHeight() {
		return mContainer.getHeight();
	}

	public void setRefreshTime(String time) {
//		mHeaderImage.clearAnimation();
//		resetImageRotation();
//		mHeaderTimeLabel.setVisibility(View.VISIBLE);
//		mHeaderTimeView.setText(time);
	}
}
