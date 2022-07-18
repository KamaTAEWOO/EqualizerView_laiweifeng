package com.laiweifeng.tv.eq;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EqSettingsActivity extends BaseActivity implements EqualizerProgressBar.OnEqualizerProgressBarListener {


    private List<EqualizerProgressBar> mEqualizerProgressBars = new ArrayList<>();

    private List<TextView> mTextView = new ArrayList<>();

    private LineView lineView;

    int[] eqValues;


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
            mTextView.get(i).setTag(position + 1);
        }
    }

    @Override
    public void initData() {
        eqValues = new int[]{
                50, 30, 60, 12, 18, 30, 50, 60, 70
        };
        for (int i = 0; i < eqValues.length; i++) {
            mEqualizerProgressBars.get(i).setProcessValue(eqValues[i]);
            mTextView.get(i).setText(String.valueOf(eqValues[i]));
            lineMap.put(i + 1, (float) eqValues[i]);
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


    @Override
    public void onVlaueChanged(View view, float value) {
        int index = (int) view.getTag();
        //Log.d("0nChanged()", "index: " + index + "value: " + value);
        for (int i = 1; i <= mTextView.size(); i++) {
            // Tag Number == i가 같다면
            if (index == i) {
                // 해당 Text에 setText
                mTextView.get(i-1).setText(String.valueOf((int) value));
            }
            lineMap.put(index, value);
            lineView.setData(lineMap);
            eqValues[index - 1] = (int) value;
        }
    }

    @Override
    public void onVlaueUpdating(View view, float value) {
        int index = (int) view.getTag();
        lineMap.put(index, value);
        lineView.setData(lineMap);
    }

}
