package com.example.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

public class TextHandler {
	
	public static boolean isEmpty(CharSequence s){
		if(s==null||s.toString().equals("")||s.toString().equals("null")){
			return true;
		}
		return false;
	}

	public static SpannableString createSpannable(String text, String keywords) {
		int index0 = text.indexOf(keywords);
		int index1 = index0 + keywords.length();
		SpannableString ss = new SpannableString(text);
		ss.setSpan(new ForegroundColorSpan(0xff3d94de), index0, index1,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		return ss;
	}

}
