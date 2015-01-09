package com.example.emotion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;

public class EmotionsUtil {


	/**
	 * 文字转图片
	 * 
	 * @param (String) content传入文字
	 * @param (Context) c
	 * @return
	 */
	public static SpannableString strToImage(String src, Context c) {
		SpannableString ss = new SpannableString(src);
		int start = 0;
		int end = 0;
		Pattern pattern = Pattern.compile("\\[[^\\]]+\\]");
		String str = "";
		Matcher m = pattern.matcher(src);
		while (m.find()) {
			start = start + str.length() + src.indexOf("[");
			end = start + m.group().length() - 1;
			str = m.group();
			replaceImage(c, m.group(), ss, start, end);
			src = src.substring(src.indexOf(m.group()) + m.group().length());
			m = pattern.matcher(src);
		}
		return ss;
	}

	private static void replaceImage(Context c, String src, SpannableString ss,
			int starts, int end) {
		SharedPreferences shared = c.getSharedPreferences("emotion", 0); // 通过xml文件获取对应图片ID
		int resource = shared.getInt(src, 0);
		try {
			Drawable drawable = c.getResources().getDrawable(resource);
			if (drawable != null) {
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight());
				ImageSpan span = new ImageSpan(drawable,
						ImageSpan.ALIGN_BASELINE);
				ss.setSpan(span, starts, end + 1,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		} catch (Exception ex) {

		}
	}
	
	/**
	 * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
	 * 
	 * @param context
	 * @param str
	 * @return
	 */
	public static SpannableString getExpressionString(Context context, String str) {
		SpannableString spannableString = new SpannableString(str);
		// 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
		String zhengze = "\\[[^\\]]+\\]";
		// 通过传入的正则表达式来生成一个pattern
		Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
		try {
			dealExpression(context, spannableString, sinaPatten, 0);
		} catch (Exception e) {
			Log.e("dealExpression", e.getMessage());
		}
		return spannableString;
	}

	/**
	 * 添加表情
	 * 
	 * @param context
	 * @param imgId
	 * @param spannableString
	 * @return
	 */
	public SpannableString addFace(Context context, int imgId, String spannableString) {
		if (TextUtils.isEmpty(spannableString)) {
			return null;
		}
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				imgId);
		bitmap = Bitmap.createScaledBitmap(bitmap, 35, 35, true);
		ImageSpan imageSpan = new ImageSpan(context, bitmap);
		SpannableString spannable = new SpannableString(spannableString);
		spannable.setSpan(imageSpan, 0, spannableString.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}
	
	
	/**
	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
	 * 
	 * @param context
	 * @param spannableString
	 * @param patten
	 * @param start
	 * @throws Exception
	 */
	private static void dealExpression(Context context,
			SpannableString spannableString, Pattern patten, int start)
			throws Exception {
		Matcher matcher = patten.matcher(spannableString);
		while (matcher.find()) {
			String key = matcher.group();
			// 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
			if (matcher.start() < start) {
				continue;
			}
			
			String value = PinyinUtils.getPingYin(key);
			System.out.println("key:"+key+",value:"+value);
			if (TextUtils.isEmpty(value)) {
				continue;
			}
			
			int resId = 0;
			
			resId = context.getResources().getIdentifier("d_"+value.replace("[", "").replace("]", ""), "drawable",context.getPackageName());
			
			
			if(resId==0)
				context.getResources().getIdentifier("h_"+value.replace("[", "").replace("]", ""), "drawable",context.getPackageName());
			
			System.out.println("resid:"+resId);
			
			// 通过上面匹配得到的字符串来生成图片资源id，下边的方法可用，但是你工程混淆的时候就有事了，你懂的。不是我介绍的重点
			// Field field=R.drawable.class.getDeclaredField(value);
			// int resId=Integer.parseInt(field.get(null).toString());
			if (resId != 0) {
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
				bitmap = Bitmap.createScaledBitmap(bitmap,200,200, true);
				// 通过图片资源id来得到bitmap，用一个ImageSpan来包装
				ImageSpan imageSpan = new ImageSpan(bitmap);
				// 计算该图片名字的长度，也就是要替换的字符串的长度
				int end = matcher.start() + key.length();
				// 将该图片替换字符串中规定的位置中
				spannableString.setSpan(imageSpan, matcher.start(), end,
						Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				if (end < spannableString.length()) {
					// 如果整个字符串还未验证完，则继续。。
					dealExpression(context, spannableString, patten, end);
				}
				break;
			}
		}
	}
}
