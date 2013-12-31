package us.dobell.doschool.microblog;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.doschool.ActivityMain;
import us.dobell.doschool.R;
import us.dobell.doschool.base.ViewsFactory;
import us.dobell.doschool.base.ViewsFactoryLazy;
import us.dobell.xtools.XDatabase;
import us.dobell.xtools.XPagePlus;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

public class PageMicroblog extends XPagePlus<Comment> {
	private View mblogView;
	private int mblogId;

	public PageMicroblog(Context context) {
		super(context, true);
	}

	@Override
	public void onCreate() {
		try {
			mblogId = getInfo().getInt("id");
		} catch (Exception e) {
			Toast.makeText(getContext(), "该微博不存在", Toast.LENGTH_SHORT).show();
			ActivityMain.delXPage();
		}
		super.onCreate();
	}

	@Override
	public View getSpecialView() {
		if (mblogView == null) {
			mblogView = LayoutInflater.from(getContext()).inflate(
					R.layout.view_microblog_normal, null);
			ViewsFactoryLazy.MicroblogFactory.getMicroblogView(mblogView,
					mblogId);
		}
		return mblogView;
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "获取评论的View");
		return ViewsFactory.MicroblogFactory.getMicroblogCommentView(
				getContext(), getXObject(position));
	}

	@Override
	public ArrayList<Comment> loadDatabase(int lastId, int rollType,
			int objCount) {
		return MicroblogDatabase.microblogCommentList(mblogId, lastId,
				rollType, objCount);
	}

	@Override
	public ArrayList<Comment> loadServer(int lastId, int rollType, int objCount) {
		try {
			ArrayList<Comment> commentList = new ArrayList<Comment>();
			JSONObject jObj = MicroblogServer.microblogCommentList(mblogId,
					lastId, rollType, objCount);
			JSONArray jArr = jObj.getJSONArray("data");
			for (int i = 0; i < jArr.length(); i++) {
				try {
					commentList.add(new Comment(jArr.getJSONObject(i)));
				} catch (JSONException e) {
				}
			}
			return commentList;
		} catch (JSONException e) {
			return null;
		}
	}

	@Override
	public void saveToDatabase(Comment x) {
		MicroblogDatabase.microblogCommentSet(x);
	}

	@Override
	public void deleteFromDatabase(Comment x) {
		MicroblogDatabase.microblogCommentDelete(x.id, XDatabase.DELETE_ONE);
	}

	@Override
	public void itemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}
}
