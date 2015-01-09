package com.example.emotion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.example.myfucntest.R;

public class EmotionFragment extends Fragment {
	
	String str = "庆祝#小米手机#3周年，送出100枚新品F码！转发即有机会赢取，谢谢米粉们一直以来的支持！[爱你]";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SpannableString spannable = EmotionsUtil.getExpressionString(getActivity(),str);
		View view = inflater.inflate(R.layout.demo_emotions, container ,false);
		AQuery aq = new AQuery(view);
		aq.id(R.id.tv_emotions).text(spannable);
		return view;
	}

}
