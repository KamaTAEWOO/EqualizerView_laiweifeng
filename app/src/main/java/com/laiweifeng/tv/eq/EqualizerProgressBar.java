package com.laiweifeng.tv.eq;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;


public class EqualizerProgressBar extends View {

    private final static int ANIM_DUR = 500;

    private Paint paint = new Paint();
    private float processValue = 50 ; // 시작 값
    private float maxProgress = 112; // 최대 값 -> UI가 바뀌면 다시 조정해야함.

    //UI
    private int barWidth = 15 ; // 갈색 막대 width
    private int barbgWidth = 8 ; // 흰색 막대 width
    private int innerCircleRedius = 14; // 내부 원 크기
    private int outerCircleRedius = 30; // 외부 원 크기
    private int innerCircleWidth = 20; // 내부 원 너비
    private int outerCirclewidth = 8; // 외부 원 너비

    private boolean gainFocus=false;


    private OnEqualizerProgressBarListener equalizerProgressBarListener;


    public EqualizerProgressBar(Context context) {
        this(context,null);
    }

    public EqualizerProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EqualizerProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(true);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        this.gainFocus=gainFocus;
        invalidate();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAntiAlias(true);
        // 초점 이동 지원
        if(gainFocus){
            //
            float position  = getHeight() -  getHeight() * processValue /maxProgress;
            paint.setColor(getResources().getColor(R.color.equalizer_progressbar_selected_color));
            paint.setStrokeWidth(barWidth);
            canvas.drawLine((getRight() - getLeft()) / 2 ,getHeight() , (getRight() - getLeft()) / 2 , position , paint);

            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(barbgWidth);
            canvas.drawLine((getRight() - getLeft()) / 2 ,position , (getRight() - getLeft()) / 2 , 0 , paint);
            // 외부 원 그리기
            paint.setColor(getResources().getColor(R.color.equalizer_progressbar_selected_color));
            paint.setStrokeWidth(innerCircleWidth);
            canvas.drawCircle((getRight() - getLeft()) / 2, position, outerCircleRedius, paint);

            // 내부 원 그리기
            paint.setColor(Color.GRAY);
            paint.setStrokeWidth(outerCirclewidth);
            canvas.drawCircle((getRight() - getLeft()) / 2, position, innerCircleRedius, paint);
        }else{

            float position  = getHeight() -  getHeight() * processValue /maxProgress ;
            paint.setColor(getResources().getColor(R.color.equalizer_progressbar_color));
            paint.setStrokeWidth(barWidth);
            canvas.drawLine((getRight() - getLeft()) / 2 ,getHeight() , (getRight() - getLeft()) / 2 , position , paint);

            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(barbgWidth);
            canvas.drawLine((getRight() - getLeft()) / 2 ,position  , (getRight() - getLeft()) / 2, 0 , paint);
            //绘制外圆
            paint.setColor(getResources().getColor(R.color.equalizer_progressbar_color));
            paint.setStrokeWidth(innerCircleWidth);
            canvas.drawCircle((getRight() - getLeft()) / 2, position, outerCircleRedius, paint);

            //绘制内圆
            paint.setColor(Color.GRAY);
            paint.setStrokeWidth(outerCirclewidth);
            canvas.drawCircle((getRight() - getLeft()) / 2, position, innerCircleRedius, paint);
        }
    }


    /**
     *  set progress
     * @param process
     */
    public void setProcessValue(float process) {
        float startProgress = processValue;
        ValueAnimator animator = ValueAnimator.ofFloat(startProgress , process);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                processValue = (float)animation.getAnimatedValue();
                invalidate();
                if(equalizerProgressBarListener != null) {
                    equalizerProgressBarListener.onVlaueUpdating(EqualizerProgressBar.this,processValue);
                }
            }
            
        });

        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(ANIM_DUR);
        animator.start();

    }


    /**
     * get process value
     * @return
     */
    public float getProcessValue(){
        return processValue;
    }


    /**
     * set equalizerProgressBarListener
     * @param listener
     */
    public void setOnEqualizerProgressBarListener(OnEqualizerProgressBarListener listener){
        equalizerProgressBarListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                float positionY = (getHeight() - event.getY() - getTop())* maxProgress/getHeight();
                processValue = positionY;
                if (processValue < 5 || processValue > 106) {
                    return true;
                }
                invalidate();
                if(equalizerProgressBarListener != null){
                    equalizerProgressBarListener.onVlaueChanged(this,processValue);
                }
                break;
        }

        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_UP:
                processValue = getProcessValue()+1;
                break;

            case KeyEvent.KEYCODE_DPAD_DOWN:
                processValue = getProcessValue()-1;
                break;
        }
        if (processValue < 5 || processValue > 106) {
            return true;
        }
        invalidate();
        if(equalizerProgressBarListener != null){
            equalizerProgressBarListener.onVlaueChanged(this,processValue);
        }
        return super.onKeyDown(keyCode, event);
    }

        public interface OnEqualizerProgressBarListener {
            void onVlaueChanged(View view, float value);
            void onVlaueUpdating(View view, float value);

        }


}
