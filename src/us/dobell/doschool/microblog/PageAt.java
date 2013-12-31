package us.dobell.doschool.microblog;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.doschool.ActivityMain;
import us.dobell.doschool.base.Values;
import us.dobell.doschool.base.ViewsFactory;
import us.dobell.xtools.XDatabase;
import us.dobell.xtools.XPage;
import us.dobell.xtools.XPagePlus;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

public class PageAt extends XPagePlus<At> {

	public PageAt(Context context) {
		super(context, false);
	}

	@Override
	public View getSpecialView() {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		((TextView) titleMidLayout.getChildAt(0)).setText("@我的");
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		return ViewsFactory.MicroblogFactory.getAtView(getContext(),
				getXObject(position));
	}

	@Override
	public ArrayList<At> loadDatabase(int lastId, int rollType, int objCount) {
		return MicroblogDatabase.microblogAtList(lastId, rollType, objCount);
	}

	@Override
	public ArrayList<At> loadServer(int lastId, int rollType, int objCount) {
		JSONObject jObj = MicroblogServer.microblogAtList(Values.User.me.id,
				lastId, rollType, objCount);
		ArrayList<At> atList = new ArrayList<At>();
		JSONArray jArr;
		try {
			jArr = jObj.getJSONArray("data");
			for (int i = 0; i < jArr.length(); i++) {
				try {
					atList.add(new At(jArr.getJSONObject(i)));
					Log.d(TAG, "成功添加@");
				} catch (Exception e) {
				}
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return atList;
	}

	@Override
	public void saveToDatabase(At x) {
		MicroblogDatabase.microblogAtSet(x);
	}

	@Override
	public void deleteFromDatabase(At x) {
		MicroblogDatabase.microblogAtDelete(x.id, XDatabase.DELETE_ONE);
	}

	@Override
	public void itemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("id", getXObject(arg2).mblogId);
			XPage xPage = new PageMicroblog(getContext());
			xPage.setInfo(jObj);
			ActivityMain.addXPage(xPage);
		} catch (Exception e) {
		}
	}

}
