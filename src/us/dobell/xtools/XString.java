package us.dobell.xtools;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.SpannableString;
import android.text.Spanned;

public class XString {
	public String string;
	public ArrayList<XSpan> spans;

	public XString(String s) {
		this.string = s;
		this.spans = new ArrayList<XSpan>();
	}

	public XString(JSONObject jObj) throws JSONException {
		this.string = jObj.getString("string");
		this.spans = new ArrayList<XSpan>();
		JSONArray jArr = jObj.getJSONArray("spans");
		for (int i = 0; i < jArr.length(); i++) {
			spans.add(new XSpan(jArr.getJSONObject(i)));
		}
	}

	public XString cutOff(int start, int count) {
		if (count <= 0) {
			return this;
		} else {
			XString xStr = new XString(string.substring(0, start)
					+ string.substring(start + count));
			for (XSpan xSpan : spans) {
				if (xSpan.end <= start) {
					xStr.spans.add(xSpan.copy());
				} else if (xSpan.start >= start + count) {
					XSpan x = xSpan.copy();
					x.moveLeft(count);
					xStr.spans.add(x);
				}
			}
			return xStr;
		}
	}

	public XString addOn(int start, XString xString) {
		if (xString == null || xString.string.length() == 0) {
			return this;
		} else {
			XString xStr = new XString(string.substring(0, start)
					+ xString.string + string.substring(start));
			for (XSpan xSpan : spans) {
				if (xSpan.end <= start) {
					xStr.spans.add(xSpan.copy());
				} else if (xSpan.start >= start) {
					XSpan x = xSpan.copy();
					x.moveLeft(-xString.string.length());
					xStr.spans.add(x);
				}
			}
			for (XSpan xSpan : xString.spans) {
				XSpan x = xSpan.copy();
				x.moveLeft(-start);
				xStr.spans.add(x);
			}
			return xStr;
		}
	}

	public XString addXSpan(XSpan xSpan, String content) {
		if (content == null || content.length() == 0) {
			return this;
		} else {
			XString xStr = new XString(string + content);
			for (XSpan x : spans) {
				xStr.spans.add(x.copy());
			}
			XSpan newXSpan = xSpan.copy();
			newXSpan.start = string.length();
			newXSpan.end = newXSpan.start + content.length();
			xStr.spans.add(newXSpan);
			return xStr;
		}
	}

	public XString copy() {
		XString xstring = new XString(string);
		for (XSpan xSpan : spans) {
			xstring.spans.add(xSpan.copy());
		}
		return xstring;
	}

	public SpannableString toSpannableString() {
		SpannableString s = new SpannableString(string);
		for (XSpan xSpan : spans) {
			s.setSpan(xSpan.copy(), xSpan.start, xSpan.end,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return s;
	}

	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObj = new JSONObject();
		JSONArray jArr = new JSONArray();
		for (XSpan xSpan : spans) {
			jArr.put(xSpan.toJSONObject());
		}
		jObj.put("string", string);
		jObj.put("spans", jArr);
		return jObj;
	}

	@Override
	public String toString() {
		return string + spans.toString();
	}
}
