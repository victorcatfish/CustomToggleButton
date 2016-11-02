package com.victor.togglebuttonlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义切换开关
 * created by victor 2016/11/1
 */
public class CustomToggleButton extends View {

    /**开关背景*/
    private Bitmap mSwitchBackGroundBitMap;

    /**开关滑块*/
    private Bitmap mSlideButtonBitMap;

    /**画笔*/
    private Paint mPaint;

    /**开关状态 默认false(关闭)*/
    private boolean mSwitchState = false;

    /**标示 当前是否为用户触摸状态 默认false*/
    private boolean mIsTouchMode = false;

    private float mStartX;
    private OnStateChangedListener mOnStateChangedListener;

    public CustomToggleButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CustomToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CustomToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    /**
     * 初始化数据
     * @param context  上下文环境
     * @param attrs  属性集合
     * @param defStyle 样式
     */
    private void init(Context context, AttributeSet attrs, int defStyle) {
        mPaint = new Paint(); // 初始化画笔

        //String namespace = "http://schemas.android.com/apk/res-auto";
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomToggleButton);
        int switchBgID = a.getResourceId(R.styleable.CustomToggleButton_switch_background, -1);
        System.out.println("switchBgID ====================== " + switchBgID);
        if (switchBgID != -1) {
            setSwitchBackgroundResource(switchBgID);
        }
        int slideBtnID = a.getResourceId(R.styleable.CustomToggleButton_slide_button, -1);
        if (slideBtnID != -1) {
            setSlideButtonResource(slideBtnID);
        }

        mSwitchState = a.getBoolean(R.styleable.CustomToggleButton_switch_state, false);

    }

    /**
     * 设置开关背景图片
     * @param ResourceId 图片资源ID
     */
    public void setSwitchBackgroundResource(int ResourceId) {
        mSwitchBackGroundBitMap = BitmapFactory.decodeResource(getResources(), ResourceId);
    }

    /**
     * 设置开关滑块图片
     * @param ResourceId 图片资源ID
     */
    public void setSlideButtonResource(int ResourceId) {
        mSlideButtonBitMap = BitmapFactory.decodeResource(getResources(), ResourceId);
    }

    /**
     * 设置开关状态
     * @param isOpen true 开; false 关
     */
    public void setSwitchState(boolean isOpen) {
        mSwitchState = isOpen;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mSwitchBackGroundBitMap != null) {
            setMeasuredDimension(mSwitchBackGroundBitMap.getWidth(), mSwitchBackGroundBitMap.getHeight());
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 绘制背景
        if (mSwitchBackGroundBitMap == null) {
            return;
        }
        canvas.drawBitmap(mSwitchBackGroundBitMap, 0, 0, mPaint);

        // 绘制滑块
        if (mSlideButtonBitMap == null) {
            return;
        }

        // 判断当前用户是否有触摸
        if (mIsTouchMode) {
            float left = mStartX - mSlideButtonBitMap.getWidth() / 2.0f;

            // 限定滑块的滑动范围
            if (left < 0) {
                left = 0;
            }
            int maxLeft = mSwitchBackGroundBitMap.getWidth() - mSlideButtonBitMap.getWidth();
            if (left > maxLeft) {
                left = maxLeft;
            }
            canvas.drawBitmap(mSlideButtonBitMap, left, 0, mPaint);
        } else {
            if (mSwitchState) { // 判断开关的状态
                // 开的情况
                int left = mSwitchBackGroundBitMap.getWidth() - mSlideButtonBitMap.getWidth();
                canvas.drawBitmap(mSlideButtonBitMap, left, 0, mPaint);
            } else {
                // 关的情况
                canvas.drawBitmap(mSlideButtonBitMap, 0, 0, mPaint);
            }
        }
    }

    // 重写触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mIsTouchMode = true;
                break;
            case MotionEvent.ACTION_MOVE:
                mStartX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsTouchMode = false;
                // 判断手指离开时的位置 超过一半 为开 否则为关
                float midX = mSwitchBackGroundBitMap.getWidth() / 2.0f;

                boolean state = mStartX > midX;

                // 状态发生变化 调用回调函数
                if (state != mSwitchState && mOnStateChangedListener != null) {
                    mOnStateChangedListener.onStateChanged(state);
                }
                // 更新状态
                mSwitchState = state;
                break;
            default:
                break;

        }

        // 重绘
        invalidate();
        return true;
    }

    /**
     * 设置状态改变监听器
     * @param onStateChangedListener 状态改变监听器
     */
    public void setOnStateChangeListener(OnStateChangedListener onStateChangedListener) {
        mOnStateChangedListener = onStateChangedListener;
    }

    /**
     * 开关状态监听器
     */
    public interface OnStateChangedListener {
        void onStateChanged(boolean state);
    }
}
