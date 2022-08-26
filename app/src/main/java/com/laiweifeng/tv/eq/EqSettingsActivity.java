package com.laiweifeng.tv.eq;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EqSettingsActivity extends BaseActivity implements EqualizerProgressBar.OnEqualizerProgressBarListener {
    
    private List<EqualizerProgressBar> mEqualizerProgressBars = new ArrayList<>();

    private List<TextView> mTextView = new ArrayList<>();

    private LineView lineView;

    //int[] eqValues;
    List<Integer> eqValues = new ArrayList<>();

    private Map<Integer, Float> lineMap = new HashMap<>();


    // layout에 그리기.
    @Override
    protected int getLayouId() {
        return R.layout.activity_eq_settings;
    }

    @Override
    public void init() {

    }

    // findViewById
    @Override
    public void findViews() {
        lineView = (LineView) findViewById(R.id.lineview);

        mEqualizerProgressBars.add((EqualizerProgressBar) findViewById(R.id.eqProgressbar1));
        mEqualizerProgressBars.add((EqualizerProgressBar) findViewById(R.id.eqProgressbar2));
        mEqualizerProgressBars.add((EqualizerProgressBar) findViewById(R.id.eqProgressbar3));
        mEqualizerProgressBars.add((EqualizerProgressBar) findViewById(R.id.eqProgressbar4));
        mEqualizerProgressBars.add((EqualizerProgressBar) findViewById(R.id.eqProgressbar5));
        mEqualizerProgressBars.add((EqualizerProgressBar) findViewById(R.id.eqProgressbar6));
        mEqualizerProgressBars.add((EqualizerProgressBar) findViewById(R.id.eqProgressbar7));
        mEqualizerProgressBars.add((EqualizerProgressBar) findViewById(R.id.eqProgressbar8));
        mEqualizerProgressBars.add((EqualizerProgressBar) findViewById(R.id.eqProgressbar9));
        for (int i = 0; i < mEqualizerProgressBars.size(); i++) {
            int position = i;
            mEqualizerProgressBars.get(i).setTag(position + 1);
        }

        mTextView.add((TextView) findViewById(R.id.txt_processValue1));
        mTextView.add((TextView) findViewById(R.id.txt_processValue2));
        mTextView.add((TextView) findViewById(R.id.txt_processValue3));
        mTextView.add((TextView) findViewById(R.id.txt_processValue4));
        mTextView.add((TextView) findViewById(R.id.txt_processValue5));
        mTextView.add((TextView) findViewById(R.id.txt_processValue6));
        mTextView.add((TextView) findViewById(R.id.txt_processValue7));
        mTextView.add((TextView) findViewById(R.id.txt_processValue8));
        mTextView.add((TextView) findViewById(R.id.txt_processValue9));
        for (int i = 0; i < mTextView.size(); i++) {
            int position = i;
            mTextView.get(i).setTag(position + 1); //처음에 값을 넣어주는 부분
        }
    }

    // 처음 앱을 실행 했을 때만 함수를 탐.
    @Override
    public void initData() {
//        eqValues = new int[]{
//                100, 30, 60, 12, 18, 30, 50, 60,70
//        };

        getEqValueList(); //List 받는 곳.
        for (int i = 0; i < eqValues.size(); i++) {
            mEqualizerProgressBars.get(i).setProcessValue((float)(eqValues.get(i) / 40.96));
            Log.d("initData()", "Data: " + i + "번째 " + (float)(eqValues.get(i) / 40.96));
            mTextView.get(i).setText(String.valueOf(eqValues.get(i) - 204)); //앱 실행시 데이터를 넣어주는 곳
            lineMap.put(i + 1, (float) eqValues.get(i));
        }
        lineView.setData(lineMap);
    }

    @Override
    public void setListener() {
        for (int i = 0; i < mEqualizerProgressBars.size(); i++) {
            mEqualizerProgressBars.get(i).setOnEqualizerProgressBarListener(this);
        }
    }

    @Override
    public void setting() {

    }

    // ProgressBar을 움직일 때 적용됨.
    @Override
    public void onVlaueChanged(View view, float value) {
        int index = (int) view.getTag();
        int parseValue = (int) value - 5;
        Log.d("0nChanged()", "index: " + index + "parseValue: " + parseValue);
        for (int i = 1; i <= mTextView.size(); i++) {
            // Tag Number == i가 같다면
            if (index == i) {
                // 해당 Text에 setText
                mTextView.get(i-1).setText(String.valueOf((int)(parseValue * 40.96))); // text에 보이는 숫자 : 5씩 더하고 빼야함.(min, max)
            }
            lineMap.put(index, value);
            lineView.setData(lineMap);
            //eqValues[index - 1] = (int) value;
            eqValues.set(index - 1, (int) value);
        }
    }

    // ProgressBar을 움직일 때 적용됨.
    @Override
    public void onVlaueUpdating(View view, float value) {
        int index = (int) view.getTag();
        lineMap.put(index, value);
        lineView.setData(lineMap);
    }

    public void getEqValueList() {
        // 5 ~ 105까지 범위를 잡음.
        // 204 ~ 4300까지 범위를 잡아야지 UI가 안 깨짐.
        // 즉 204가 0이라는 뜻 -> 값이 들어오면 204를 더해줘야함. 그래야 UI가 깨지지 않음.
        eqValues.add(getDensityValueChange(4300)); //40
        eqValues.add(getDensityValueChange(3022)); //50
        eqValues.add(getDensityValueChange(4000)); //60
        eqValues.add(getDensityValueChange(1006)); //70
        eqValues.add(getDensityValueChange(1304)); //80
        eqValues.add(getDensityValueChange(1600)); //90
        eqValues.add(getDensityValueChange(1000)); //100
        eqValues.add(getDensityValueChange(1800)); //110
        eqValues.add(getDensityValueChange(1200)); //125
    }


    // density를 올리고 그래프 화면으로 들어오기 때문에 intent할 때 density를 전달해주면 됨.
    public int getDensityValueChange (int value) {
        int density = getDensity();
        // 범위: -8 ~ 0 or 0 ~ 8
        if(density < 0) {
            // 범위: -8 ~ -1 -> -9%
            density = Math.abs(density);
            for(int i = 1; i <= density; i++){
                value *= 0.92;
            }
        } else if(density > 0) {
            // 범위: 1 ~ 8 -> +9%
            for(int i = 1; i <= density; i++){
                value *= 1.09;
            }
        } else {
            // 다른 숫자나 0이 들어올 경우
        }

        // 범위: 0 ~ 8 -> 9%
        return (int)value;
    }

    // density 값 들어오는 부분
    public int getDensity () {
        return -3;
    }

}
