package us.dobell.doschool.user;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.doschool.ActivityMain;
import us.dobell.doschool.R;
import us.dobell.doschool.base.Values;
import us.dobell.doschool.microblog.PagePerson;
import us.dobell.xtools.XImageView;
import us.dobell.xtools.XPage;
import us.dobell.xtools.XServer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PageFriends extends XPage {
	private ArrayList<User> friends;
	private ListView friendList;
	private XAdpater friendAdapter;
	private XItemListener friendListener;

	private SearchClickListener searchClickListener;
	private SearchAdapter searchAdapter;

	private boolean isInSearch;

	private EditText edtxt;

	public PageFriends(Context context) {
		super(context);
	}

	@Override
	public void onCreate() {
		TextView titleNameTxt = new TextView(getContext());
		titleNameTxt.setGravity(Gravity.CENTER);
		titleNameTxt.setTextSize(19);
		titleNameTxt.setText("好友们");
		titleMidLayout.addView(titleNameTxt,
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		titleRightBtn.setOnClickListener(new RightBtnListener());
		titleRightBtn.setImageResource(R.drawable.search);
		friendList = new ListView(getContext());
		contentLayout.addView(friendList,
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		friends = new ArrayList<User>();
		friendList.setAdapter(friendAdapter = new XAdpater());
		friendList.setOnItemClickListener(friendListener = new XItemListener());
	}

	@Override
	public void onResume() {
		new LoadTask().execute();
	}

	@Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
		if (isInSearch) {
			friendList.setAdapter(friendAdapter);
			friendList.setOnItemClickListener(friendListener);
			friendAdapter.notifyDataSetChanged();
			isInSearch = false;
			return true;
		} else {
			return false;
		}

	};

	class RightBtnListener implements View.OnClickListener {
		EditText editTxtNick;

		@Override
		public void onClick(View v) {
			if (!isInSearch) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getContext());
				builder.setTitle("搜索昵称");
				editTxtNick = new EditText(getContext());
				builder.setView(editTxtNick);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								isInSearch = true;
								new SearchTask().execute(editTxtNick.getText()
										.toString());
							}
						});
				builder.setNegativeButton("取消", null);
				builder.create().show();
			} else {
				friendList.setAdapter(friendAdapter);
				friendList.setOnItemClickListener(friendListener);
				friendAdapter.notifyDataSetChanged();
				isInSearch = false;
			}
		}
	}

	class XItemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			try {
				PagePerson page = new PagePerson(getContext());
				JSONObject jObj = new JSONObject();
				jObj.put("id", friends.get(arg2).id);
				page.setInfo(jObj);
				ActivityMain.addXPage(page);
			} catch (JSONException e) {
			}
		}
	}

	class XAdpater extends BaseAdapter {

		@Override
		public int getCount() {
			return friends.size();
		}

		@Override
		public Object getItem(int position) {
			return friends.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.item_user, null);
			}
			((XImageView) convertView.findViewById(R.id.itemUserHeadXimg))
					.setImageURL(friends.get(position).head);
			((TextView) convertView.findViewById(R.id.itemUserNickTxt))
					.setText(friends.get(position).nick);
			return convertView;
		}
	}

	class SearchClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (searchAdapter.list.get(arg2).id == SearchAdapter.LABEL_LOCAL
					|| searchAdapter.list.get(arg2).id == SearchAdapter.LABEL_REMOTE
					|| searchAdapter.list.get(arg2).id == SearchAdapter.LABEL_NONE) {
				return;
			}
			try {
				PagePerson page = new PagePerson(getContext());
				JSONObject jObj = new JSONObject();
				jObj.put("id", searchAdapter.list.get(arg2).id);
				page.setInfo(jObj);
				ActivityMain.addXPage(page);
			} catch (JSONException e) {
			}
		}
	}

	class SearchAdapter extends BaseAdapter {
		public static final int LABEL_LOCAL = -2;
		public static final int LABEL_REMOTE = -1;
		public static final int LABEL_NONE = 0;

		public ArrayList<User> list = new ArrayList<User>();
		int index = Integer.MAX_VALUE;

		public void addLocalUsers(ArrayList<User> localUsers) {
			if (localUsers != null && localUsers.size() != 0) {
				list.addAll(0, localUsers);
				list.add(0, new User(LABEL_LOCAL, "我的好友", "", 0, 0));
			}
		}

		public void addRemoteUsers(ArrayList<User> remoteUsers) {
			if (remoteUsers != null && remoteUsers.size() != 0) {
				index = list.size();
				list.add(new User(LABEL_REMOTE, "陌生人", "", 0, 0));
				list.addAll(remoteUsers);
			}
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg0 <= index) {
				arg1 = LayoutInflater.from(getContext()).inflate(
						R.layout.item_user, null);
				((XImageView) arg1.findViewById(R.id.itemUserHeadXimg))
						.setImageURL(list.get(arg0).head);
				((TextView) arg1.findViewById(R.id.itemUserNickTxt))
						.setText(list.get(arg0).nick);
				return arg1;
			} else {
				arg1 = LayoutInflater.from(getContext()).inflate(
						R.layout.item_user_search, null);
				((XImageView) arg1.findViewById(R.id.itemSearchUserHeadXimg))
						.setImageURL(list.get(arg0).head);
				((TextView) arg1.findViewById(R.id.itemSearchUserNickTxt))
						.setText(list.get(arg0).nick);
				Button cardBtn = (Button) arg1
						.findViewById(R.id.itemSearchUserCardBtn);
				OperateListener l = new OperateListener(list.get(arg0).id);
				if ((list.get(arg0).card & 1) == 0) {
					cardBtn.setText("发送名片");
					cardBtn.setOnClickListener(l);
				} else {
					cardBtn.setText("名片已发");
				}
				Button friendBtn = (Button) arg1
						.findViewById(R.id.itemSearchUserFriendBtn);
				if ((list.get(arg0).friend & 1) == 0) {
					friendBtn.setText("发送申请");
					friendBtn.setOnClickListener(l);
				} else {
					friendBtn.setText("申请已发");
				}
				return arg1;
			}
		}
	}

	class OperateListener implements OnClickListener {
		int usrId;

		public OperateListener(int usrId) {
			this.usrId = usrId;
		}

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.itemSearchUserCardBtn) {
				new RelationHandleTask(RelationHandleTask.CARD, usrId, "")
						.execute();
			} else {
				AlertDialog.Builder builer = new AlertDialog.Builder(
						getContext());
				builer.setTitle("填写申请理由");
				edtxt = new EditText(getContext());
				builer.setView(edtxt);
				builer.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								new RelationHandleTask(
										RelationHandleTask.FRIEND, usrId, edtxt
												.getText().toString())
										.execute();
							}
						});
				builer.setNegativeButton("取消", null);
				builer.create().show();
			}
		}
	}

	class RelationHandleTask extends AsyncTask<Void, Void, Integer> {
		public static final int CARD = 1;
		public static final int FRIEND = 2;
		int type;
		int usrId;
		String reason;

		public RelationHandleTask(int type, int usrId, String reason) {
			this.type = type;
			this.usrId = usrId;
			this.reason = reason;
		}

		@Override
		protected void onPreExecute() {
			Toast.makeText(getContext(),
					"正在发送" + (type == CARD ? "名片" : "好友申请"), Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			JSONObject jObj = null;
			try {
				if (type == CARD) {
					jObj = UserServer.userCardSend(Values.User.me.id, usrId);
					return jObj.getInt("data");
				} else {
					jObj = UserServer.userApplySend(Values.User.me.id, usrId,
							reason);
					return jObj.getInt("data");
				}
			} catch (Exception e) {
				return -1;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result != -1) {
				Toast.makeText(getContext(), "操作成功", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getContext(), "操作失败", Toast.LENGTH_SHORT).show();
			}
		}
	}

	class LoadTask extends AsyncTask<Void, Boolean, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			friends.clear();
			ArrayList<User> result = UserDatabase.userFriendList();
			if (result != null && result.size() != 0) {
				friends = result;
				publishProgress(true);
			}
			JSONObject jObj = UserServer.userFriendList(Values.User.me.id);
			ArrayList<User> friendList = new ArrayList<User>();
			try {
				JSONArray jArr = jObj.getJSONArray("data");
				for (int i = 0; i < jArr.length(); i++) {
					try {
						User user = new User(jArr.optJSONObject(i));
						friendList.add(user);
						UserDatabase.userSet(user);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				friends = friendList;
				return jObj.getInt("code");
			} catch (JSONException e1) {
				return XServer.NETWORK_ERROR;
			}
		}

		@Override
		protected void onProgressUpdate(Boolean... values) {
			if (values[0]) {
				friendAdapter.notifyDataSetChanged();
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
			case 0x00003040:
				friendAdapter.notifyDataSetChanged();
				break;
			case 0x00003041:
				Toast.makeText(getContext(), "用户不存在", Toast.LENGTH_SHORT)
						.show();
				break;
			case 0x0000304f:
				Toast.makeText(getContext(), "服务器异常", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				Toast.makeText(getContext(), "网络连接出错", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	}

	class SearchTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			searchAdapter = new SearchAdapter();
			searchClickListener = new SearchClickListener();
			ArrayList<User> result = UserDatabase.userSearch(params[0]);
			searchAdapter.addLocalUsers(result);
			JSONObject jObj = UserServer.userSearch(Values.User.me.id,
					params[0]);
			try {
				JSONArray jArr = jObj.getJSONArray("data");
				result = new ArrayList<User>();
				for (int i = 0; i < jArr.length(); i++) {
					try {
						result.add(new User(jArr.getJSONObject(i)));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				searchAdapter.addRemoteUsers(result);
				if (searchAdapter.list.size() == 0) {
					searchAdapter.list.add(new User(SearchAdapter.LABEL_NONE,
							"未找到结果", "", 0, 0));
				}
				return jObj.getInt("code");
			} catch (JSONException e1) {
				return XServer.NETWORK_ERROR;
			}

		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result) {
			case 0x00001030:
				friendList.setAdapter(searchAdapter);
				friendList.setOnItemClickListener(searchClickListener);
				searchAdapter.notifyDataSetChanged();
				isInSearch = true;
				break;
			case 0x0000103f:
				Toast.makeText(getContext(), "服务器异常", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				Toast.makeText(getContext(), "网络连接出错", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	}
}
