package us.dobell.xtools;

import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.doschool.ActivityMain;
import us.dobell.doschool.microblog.PagePerson;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class XSpan extends ClickableSpan {
	public int start;
	public int end;
	public int type;
	public int id;

	public XSpan(int start, int end, int type, int id) {
		this.start = start;
		this.end = end;
		this.type = type;
		this.id = id;
	}

	public XSpan(JSONObject jObj) throws JSONException {
		this.start = jObj.getInt("start");
		this.end = jObj.getInt("end");
		this.type = jObj.getInt("type");
		this.id = jObj.getInt("id");
	}

	public void moveLeft(int distance) {
		this.start -= distance;
		this.end -= distance;
	}

	@Override
	public void onClick(View widget) {
		JSONObject jObj = new JSONObject();
		switch (type) {
		case 0x00001:
			try {
				PagePerson pagePerson = new PagePerson(widget.getContext());
				jObj.put("id", id);
				pagePerson.setInfo(jObj);
				ActivityMain.addXPage(pagePerson);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		ds.setUnderlineText(false);
		switch (type) {
		case 0x00001:
			ds.setColor(Color.BLUE);
			break;
		default:
			break;
		}
	}

	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObj = new JSONObject();
		jObj.put("start", start);
		jObj.put("end", end);
		jObj.put("type", type);
		jObj.put("id", id);
		return jObj;
	}

	public XSpan copy() {
		return new XSpan(start, end, type, id);
	}

	@Override
	public String toString() {
		return "(" + start + "," + end + "," + type + "," + id + ")";
	}

}
