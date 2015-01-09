package com.example.mpchart;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfucntest.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendPosition;

public class MPChartFragment extends Fragment implements
		OnChartValueSelectedListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.demo_mpchart, container, false);
		init(view);
		return view;
	}

	private PieChart mChart;

	private void init(View view) {
		mChart = (PieChart) view.findViewById(R.id.chart1);

		// change the color of the center-hole
		mChart.setHoleColor(Color.rgb(235, 235, 235));

		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
				"OpenSans-Regular.ttf");

		mChart.setValueTypeface(tf);
		mChart.setCenterTextTypeface(Typeface.createFromAsset(getActivity()
				.getAssets(), "OpenSans-Light.ttf"));

		mChart.setHoleRadius(60f);

		mChart.setDescription("");

		mChart.setDrawYValues(true);
		mChart.setDrawCenterText(true);

		mChart.setDrawHoleEnabled(true);

		mChart.setRotationAngle(0);

		// draws the corresponding description value into the slice
		mChart.setDrawXValues(true);

		// enable rotation of the chart by touch
		mChart.setRotationEnabled(true);

		// display percentage values
		mChart.setUsePercentValues(true);
		// mChart.setUnit(" €");
		// mChart.setDrawUnitsInChart(true);

		// add a selection listener
		mChart.setOnChartValueSelectedListener(this);
		// mChart.setTouchEnabled(false);

		mChart.setCenterText("资金流向");

		setData(3, 100);

		mChart.animateXY(1500, 1500);
		// mChart.spin(2000, 0, 360);

		Legend l = mChart.getLegend();
		l.setPosition(LegendPosition.RIGHT_OF_CHART);
		l.setXEntrySpace(7f);
		l.setYEntrySpace(5f);

	}
	
	
    protected String[] mParties = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };
	
	private void setData(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count + 1; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet set1 = new PieDataSet(yVals1, "Election Results");
        set1.setSliceSpace(3f);
        
        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        
        colors.add(ColorTemplate.getHoloBlue());

        set1.setColors(colors);

        PieData data = new PieData(xVals, set1);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

	@Override
	public void onValueSelected(Entry e, int dataSetIndex) {

		if (e == null)
			return;
		Log.i("VAL SELECTED",
				"Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
						+ ", DataSet index: " + dataSetIndex);
	}

	@Override
	public void onNothingSelected() {
		Log.i("PieChart", "nothing selected");
	}
}
