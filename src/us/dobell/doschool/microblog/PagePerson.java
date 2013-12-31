package us.dobell.doschool.microblog;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.doschool.ActivityMain;
import us.dobell.doschool.R;
import us.dobell.doschool.base.Values;
import us.dobell.doschool.base.ViewsFactory;
import us.dobell.doschool.base.ViewsFactoryLazy;
import us.dobell.xtools.XDatabase;
import us.dobell.xtools.XPage;
import us.dobell.xtools.XPagePlus;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

public class PagePerson extends XPagePlus<Microblog> {
	private View personView;
	private int userId;

	public PagePerson(Context context) {
		super(context, true);
	}

	@Override
	public void onCreate() {
		try {
			userId = getInfo().getInt("id");
		} catch (Exception e) {
			Toast.makeText(getContext(), "用户不存在", Toast.LENGTH_SHORT).show();
		}
		super.onCreate();
	}

	@Override
	public View getSpecialView() {
		if (personView == null) {
			personView = LayoutInflater.from(getContext()).inflate(
					R.layout.view_person, null);
			ViewsFactoryLazy.MicroblogFactory.getPersonView(personView, userId);
		}
		return personView;
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		return ViewsFactory.MicroblogFactory.getMicroblogView(getContext(),
				getXObject(position));
	}

	@Override
	public ArrayList<Microblog> loadDatabase(int lastId, int rollType,
			int objCount) {
		return MicroblogDatabase.microblogList(userId, Microblog.TYPE_PERSON,
				lastId, rollType, objCount);
	}

	@Override
	public ArrayList<Microblog> loadServer(int lastId, int rollType,
			int objCount) {
		JSONObject jObj = getInfo();
		try {
			ArrayList<Microblog> microblogList = new ArrayList<Microblog>();
			int userId = jObj.getInt("id");
			jObj = MicroblogServer.microblogList(Values.User.me.id, userId,
					Microblog.TYPE_PERSON, lastId, rollType, objCount);
			JSONArray jArr = jObj.getJSONArray("data");
			for (int i = 0; i < jArr.length(); i++) {
				microblogList.add(new Microblog(jArr.getJSONObject(i)));
			}
			return microblogList;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void saveToDatabase(Microblog x) {
		MicroblogDatabase.microblogSet(x);
	}

	@Override
	public void deleteFromDatabase(Microblog x) {
		MicroblogDatabase.microblogDelete(x.id, XDatabase.DELETE_ONE);
	}

	@Override
	public void itemClick(AdapterView<?> xListView, View view, int index,
			long id) {
		try {
			Log.d(TAG, "点击" + index);
			JSONObject jObj = new JSONObject();
			jObj.put("id", getXObject(index).id);
			XPage xPage = new PageMicroblog(getContext());
			xPage.setInfo(jObj);
			ActivityMain.addXPage(xPage);
		} catch (JSONException e) {
		}
	}
}
