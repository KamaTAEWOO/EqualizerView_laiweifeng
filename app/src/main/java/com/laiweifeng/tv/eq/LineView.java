package com.laiweifeng.tv.eq;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineView extends View {

    private Paint paint;
    private int size=0;
    private Path path;
    private List<Float> lineList;

    private Map<Integer, Float> lineMap=new HashMap<>();

    public LineView(Context context) {
        this(context,null);
    }

    public LineView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        path = new Path();
    }

    // Progress Bar Max를 100 -> 4096으로 바꾸었을 때 다시 드려줘야함.
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        path.reset();
        path.moveTo(72 , getHeight()/2); //시작 라인
        int size=lineMap.size();
        for (int i = 1; i <= lineMap.size(); i++) {
            //float positionX=(getWidth()/size*i)-(getWidth()/size/2); //x축으로 움직임.
            float positionX=(getWidth()/size*i)-(getWidth()/size/2); //x축으로 움직임.
            float value = lineMap.get(i); //선과 원의 거리 +5면 위로 5 올라감.
            float positionY= getHeight() - (float)(((getHeight() - 70)/100.0)*value); // 고정값 70 Max or Min 라인 맞추기. -> UI 변경시 변경해주어야함.
            Log.d("LineView::", "positionX: " + positionX + " value: " + value + " positionY: " + positionY);
            path.lineTo(positionX,positionY);
        }
        path.lineTo(getWidth() - 74 , getHeight()/2); //끝라인
        canvas.drawPath(path,paint);
    }

    public void setData(Map<Integer, Float> lineMap) {
        this.lineMap=lineMap;
        invalidate();
    }
}
