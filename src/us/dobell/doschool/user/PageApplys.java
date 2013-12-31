package us.dobell.doschool.user;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.doschool.R;
import us.dobell.doschool.base.Values;
import us.dobell.xtools.XDatabase;
import us.dobell.xtools.XImageView;
import us.dobell.xtools.XPagePlus;
import us.dobell.xtools.XUtils;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

public class PageApplys extends XPagePlus<Apply> {

	public PageApplys(Context context) {
		super(context, false);
	}

	@Override
	public View getSpecialView() {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		((TextView) titleMidLayout.getChildAt(0)).setText("好友申请");
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_apply, null);
		}
		((XImageView) convertView.findViewById(R.id.itemApplyUsrHeadXimg))
				.setImageURL(getXObject(position).usrHead);
		((TextView) convertView.findViewById(R.id.itemApplyUsrNickTxt))
				.setText(getXObject(position).usrNick);
		((TextView) convertView.findViewById(R.id.itemApplyTimeTxt))
				.setText(XUtils.getTime(getXObject(position).time));
		((TextView) convertView.findViewById(R.id.itemApplyReasonTxt))
				.setText(getXObject(position).reason);
		HandleListener handleListener = new HandleListener(
				getXObject(position).id);
		Button btnPositive = (Button) convertView
				.findViewById(R.id.itemApplyOperatePositiveBtn);
		Button btnNegative = (Button) convertView
				.findViewById(R.id.itemApplyOperateNegativeBtn);
		if (getXObject(position).state == Apply.HANDLE_POSITIVE) {
			btnNegative.setVisibility(View.GONE);
			btnPositive.setText("已接受");
		} else if (getXObject(position).state == Apply.HANDLE_NEGATIVE) {
			btnNegative.setVisibility(View.GONE);
			btnPositive.setText("拒绝");
		} else {
			btnNegative.setOnClickListener(handleListener);
			btnPositive.setOnClickListener(handleListener);
		}
		return convertView;
	}

	@Override
	public ArrayList<Apply> loadDatabase(int lastId, int rollType, int objCount) {
		return UserDatabase.userApplyList(lastId, rollType, objCount);
	}

	@Override
	public ArrayList<Apply> loadServer(int lastId, int rollType, int objCount) {
		JSONObject jObj = UserServer.userApplyList(Values.User.me.id, lastId,
				rollType, objCount);
		ArrayList<Apply> result = null;
		try {
			JSONArray jArr = jObj.getJSONArray("data");
			result = new ArrayList<Apply>();
			for (int i = 0; i < jArr.length(); i++) {
				try {
					result.add(new Apply(jArr.getJSONObject(i)));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return result;
	}

	@Override
	public void saveToDatabase(Apply x) {
		UserDatabase.userApplySet(x);
	}

	@Override
	public void deleteFromDatabase(Apply x) {
		UserDatabase.userApplyDelete(x.id, XDatabase.DELETE_ONE);
	}

	@Override
	public void itemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}

	class HandleListener implements OnClickListener {
		int applyId;

		public HandleListener(int applyId) {
			this.applyId = applyId;
		}

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.itemApplyOperatePositiveBtn) {
				new HandleTask(applyId, Apply.HANDLE_POSITIVE).execute();
			} else {
				new HandleTask(applyId, Apply.HANDLE_NEGATIVE).execute();
			}
		}
	}

	class HandleTask extends AsyncTask<Void, Void, Integer> {
		public int applyId;
		public int handleCode;

		public HandleTask(int applyId, int handleCode) {
			this.applyId = applyId;
			this.handleCode = handleCode;
		}

		@Override
		protected Integer doInBackground(Void... params) {
			try {
				JSONObject jObj = UserServer.userApplyHandle(applyId,
						handleCode);
				User user = new User(jObj.getJSONObject("data"));
				try {
					if (handleCode == Apply.HANDLE_POSITIVE) {
						UserDatabase.userSet(user);
					} else {
						UserDatabase.userDelete(user.id);
					}
				} catch (Exception e) {
				}
				return 1;
			} catch (Exception e) {
				return 0;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == 1) {
				getXObjectById(applyId).state = handleCode;
				xAdapter.notifyDataSetChanged();
			}
		}
	}
}
