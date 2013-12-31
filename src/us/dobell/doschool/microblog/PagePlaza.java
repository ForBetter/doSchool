package us.dobell.doschool.microblog;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.doschool.ActivityMain;
import us.dobell.doschool.R;
import us.dobell.doschool.base.Values;
import us.dobell.doschool.base.ViewsFactory;
import us.dobell.xtools.XDatabase;
import us.dobell.xtools.XListView;
import us.dobell.xtools.XListView.XLoadDownListener;
import us.dobell.xtools.XListView.XLoadUpListener;
import us.dobell.xtools.XPage;
import us.dobell.xtools.XServer;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class PagePlaza extends XPage {
	public static final String TAG = "PagePlaza";
	public static final int TYPE_PLAZA = 1;
	public static final int TYPE_FRIEND = 2;
	public static final int TYPE_UP = 0;
	public static final int TYPE_DOWN = 1;

	private RadioGroup titleTab;
	private RadioButton plazaBtn;
	private RadioButton friendBtn;

	private XListView plazaMblogList;
	private XAdapter plazaAdapter;

	private XListView friendMblogList;
	private XAdapter friendAdapter;

	XAdapter currentAdapter;

	private boolean isFirst;

	private FrameLayout layout;

	private LoadTask loadUpTask;

	public PagePlaza(Context context) {
		super(context);
	}

	@Override
	public void onCreate() {
		titleRightBtn.setOnClickListener(new TitleRightBtnListener());
		titleRightBtn.setBackgroundColor(Color.TRANSPARENT);
		titleRightBtn.setImageResource(R.drawable.ic_btn_write);
		titleMidLayout.addView(LayoutInflater.from(getContext()).inflate(
				R.layout.plaza_mid_layout, null));
		titleTab = (RadioGroup) titleMidLayout.findViewById(R.id.titleTab);
		plazaBtn = (RadioButton) titleTab.findViewById(R.id.plazaBtn);
		friendBtn = (RadioButton) titleTab.findViewById(R.id.friendBtn);
		// titleTab = new RadioGroup(getContext());
		// titleTab.setOrientation(RadioGroup.HORIZONTAL);
		// plazaBtn = new RadioButton(getContext());
		// plazaBtn.setBackgroundResource(R.drawable.ic_tab);
		// plazaBtn.setButtonDrawable(Color.TRANSPARENT);
		// plazaBtn.setGravity(Gravity.CENTER);
		// plazaBtn.setText("广场");
		// titleTab.addView(plazaBtn, LinearLayout.LayoutParams.WRAP_CONTENT,
		// LinearLayout.LayoutParams.MATCH_PARENT);
		// friendBtn = new RadioButton(getContext());
		// friendBtn.setBackgroundResource(R.drawable.ic_tab);
		// friendBtn.setButtonDrawable(Color.TRANSPARENT);
		// friendBtn.setText("好友们");
		// titleTab.addView(friendBtn, LinearLayout.LayoutParams.WRAP_CONTENT,
		// LinearLayout.LayoutParams.MATCH_PARENT);
		// titleTab.setGravity(Gravity.CENTER);
		// titleMidLayout.addView(titleTab,
		// LinearLayout.LayoutParams.MATCH_PARENT,
		// LinearLayout.LayoutParams.MATCH_PARENT);
		titleMidLayout.setGravity(Gravity.CENTER);
		layout = new FrameLayout(getContext());
		LoadDownListener loadListener = new LoadDownListener();
		LoadUpListener refreshListener = new LoadUpListener();
		XItemListener xItemClickListener = new XItemListener();
		plazaAdapter = new XAdapter(new ArrayList<Microblog>(), TYPE_PLAZA);
		plazaMblogList = new XListView(getContext());
		plazaMblogList.getXList().setAdapter(plazaAdapter);
		plazaMblogList.setXLoadDownListener(loadListener);
		plazaMblogList.setXLoadUpListener(refreshListener);
		plazaMblogList.setBackgroundColor(Color.WHITE);
		plazaMblogList.getXList().setOnItemClickListener(xItemClickListener);
		friendAdapter = new XAdapter(new ArrayList<Microblog>(), TYPE_FRIEND);
		friendMblogList = new XListView(getContext());
		friendMblogList.getXList().setAdapter(friendAdapter);
		friendMblogList.setXLoadDownListener(loadListener);
		friendMblogList.setXLoadUpListener(refreshListener);
		friendMblogList.setBackgroundColor(Color.WHITE);
		friendMblogList.getXList().setOnItemClickListener(xItemClickListener);
		layout.addView(friendMblogList, FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		layout.addView(plazaMblogList, FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		contentLayout.addView(layout, LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		titleTab.setOnCheckedChangeListener(new TitleTabBtnListener());
		titleTab.check(plazaBtn.getId());
		currentAdapter = plazaAdapter;
		new LoadTask(friendAdapter).execute(TYPE_DOWN, XDatabase.LOAD_START);
		isFirst = true;
	}

	@Override
	public void onResume() {
		// if (!isFirst) {
		// new RefreshTask().execute();
		// } else {
		// isFirst = false;
		// }
	}

	class TitleTabBtnListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (checkedId == plazaBtn.getId()) {
				plazaMblogList.bringToFront();
				currentAdapter = plazaAdapter;
				plazaMblogList.setVisibility(View.VISIBLE);
				friendMblogList.setVisibility(View.INVISIBLE);
			} else {
				friendMblogList.bringToFront();
				currentAdapter = friendAdapter;
				friendMblogList.setVisibility(View.VISIBLE);
				plazaMblogList.setVisibility(View.INVISIBLE);
			}
		}
	}

	class TitleRightBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			ActivityMain.addXPage(new PageWrite(getContext()));
		}
	}

	class LoadDownListener implements XLoadDownListener {

		@Override
		public void onLoadDown() {
			if (titleTab.getCheckedRadioButtonId() == plazaBtn.getId()) {
				new LoadTask(plazaAdapter).execute(TYPE_DOWN,
						XDatabase.LOAD_OLD);
			} else {
				new LoadTask(friendAdapter).execute(TYPE_DOWN,
						XDatabase.LOAD_OLD);
			}
		}
	}

	class LoadUpListener implements XLoadUpListener {

		@Override
		public void onLoadUp() {
			if (titleTab.getCheckedRadioButtonId() == plazaBtn.getId()) {
				if (loadUpTask == null
						|| loadUpTask.getStatus() == Status.FINISHED) {
					loadUpTask = new LoadTask(plazaAdapter);
					loadUpTask.execute(TYPE_UP, XDatabase.LOAD_START);
				}
			} else {
				if (loadUpTask == null
						|| loadUpTask.getStatus() == Status.FINISHED) {
					loadUpTask = new LoadTask(friendAdapter);
					loadUpTask.execute(TYPE_UP, XDatabase.LOAD_START);
				}
			}
		}
	}

	class XAdapter extends BaseAdapter {
		public int type;
		public ArrayList<Microblog> list;

		public XAdapter(ArrayList<Microblog> list, int type) {
			this.list = list;
			this.type = type;
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
			return ViewsFactory.MicroblogFactory.getMicroblogView(getContext(),
					list.get(arg0));
		}
	}

	class RefreshTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				Log.d(TAG, "正在用数据库中的数据刷新列表");
				friendAdapter.list = MicroblogDatabase.microblogList(0,
						Microblog.TYPE_FRIEND,
						friendAdapter.list.get(0).id + 1, XServer.LOAD_OLD,
						friendAdapter.list.size());
				plazaAdapter.list = MicroblogDatabase.microblogList(0,
						Microblog.TYPE_FRIEND, plazaAdapter.list.get(0).id + 1,
						XServer.LOAD_OLD, plazaAdapter.list.size());
				return true;
			} catch (Exception e) {
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				friendAdapter.notifyDataSetChanged();
				plazaAdapter.notifyDataSetChanged();
			}
		}
	}

	class LoadTask extends AsyncTask<Integer, Boolean, Integer> {
		XAdapter xAdapter;
		int callType;
		int rollType;

		public LoadTask(XAdapter xAdapter) {
			this.xAdapter = xAdapter;
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			callType = params[0];
			rollType = params[1];
			int lastId = 0;
			if (xAdapter.list.size() == 0) {
				rollType = params[1] = XDatabase.LOAD_START;
			}
			if (rollType == XDatabase.LOAD_START) {
				Log.d(TAG, "初始加载");
				lastId = 0;
				rollType = XDatabase.LOAD_START;
			} else if (rollType == XDatabase.LOAD_NEW) {
				Log.d(TAG, "加载新的");
				lastId = xAdapter.list.get(0).id;
			} else {
				Log.d(TAG, "加载旧的");
				lastId = xAdapter.list.get(xAdapter.list.size() - 1).id;
			}
			// XListView的上面的加载条只能做向最新的刷新LOAD_NEW;
			// XListView的下面的加载条能做初始加载和向更旧的加载LOAD_START & LOAD_OLD;
			if (callType == TYPE_UP && rollType == XDatabase.LOAD_OLD) {
				return XDatabase.LOAD_SUCCESSFUL;
			}
			if (callType == TYPE_DOWN && rollType == XDatabase.LOAD_NEW) {
				return XDatabase.LOAD_SUCCESSFUL;
			}
			Log.d(TAG, "从数据库中加载缓存数据 lastId=" + lastId + " rollType=" + rollType);
			ArrayList<Microblog> result = MicroblogDatabase.microblogList(0,
					callType, lastId, rollType, XDatabase.LOAD_COUNT);
			Log.d(TAG, "数据为" + result.toString());
			if (rollType == XDatabase.LOAD_NEW) {
				xAdapter.list.addAll(0, result);
			} else {
				xAdapter.list.addAll(result);
			}
			Log.d(TAG, "刷新界面");
			publishProgress(result != null && result.size() != 0);
			Log.d(TAG, "从服务器上加载最新的数据");
			JSONObject jObj = MicroblogServer.microblogList(Values.User.me.id,
					0, xAdapter.type, lastId, rollType, XDatabase.LOAD_COUNT);
			result = new ArrayList<Microblog>();
			try {
				JSONArray jArr = jObj.getJSONArray("data");
				for (int i = 0; i < jArr.length(); i++) {
					try {
						result.add(new Microblog(jArr.getJSONObject(i)));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} catch (JSONException e) {
				return XDatabase.LOAD_FAILED;
			}
			if (result.size() == 0) {
				return XDatabase.LOAD_OVER;
			}
			Log.d(TAG, "数据为" + result.toString());
			if (rollType == XDatabase.LOAD_START) {
				xAdapter.list.clear();
			}
			xAdapter.list.addAll(0, result);
			sortXList(xAdapter.list);
			onlyXList(xAdapter.list);
			// if (rollType == XDatabase.LOAD_NEW) {
			// // 加载更新的几条
			// int i = 0;
			// // 先将获得的所有的项更新(update)到列表和数据库
			// Log.d(TAG, "更新数据中的缓存");
			// for (i = result.size() - 1; i >= 0; i--) {
			// Microblog microblog = result.get(i);
			// MicroblogDatabase.microblogSet(microblog);
			// int index = index(xAdapter.list, microblog);
			// if (index == -1) {
			// Log.d(TAG, "向列表中添加" + microblog.id);
			// xAdapter.list.add(0, microblog);
			// } else {
			// Log.d(TAG, "根新列表中的" + microblog.id);
			// xAdapter.list.set(index, microblog);
			// }
			// }
			// // 移除服务器上被删除的,删除其在本地的缓存
			// for (i = 0; i < xAdapter.list.size(); i++) {
			// Microblog microblog = xAdapter.list.get(i);
			// if (microblog.id >= result.get(result.size() - 1).id) {
			// int index = index(result, microblog);
			// if (index == -1) {
			// Log.d(TAG, "删除了一个本地错误缓存" + microblog.id);
			// MicroblogDatabase.microblogDelete(microblog.id,
			// XDatabase.DELETE_ONE);
			// xAdapter.list.remove(i--);
			// }
			// }
			// }
			// Log.d(TAG, "组织后的列表为" + xAdapter.list.toString());
			// } else {
			// // 加载更旧的几条
			// int i = 0;
			// // 先将获得的所有的项更新(update)到列表和数据库
			// Log.d(TAG, "更新数据中的缓存");
			// for (i = 0; i < result.size(); i++) {
			// Microblog microblog = result.get(i);
			// MicroblogDatabase.microblogSet(microblog);
			// int index = index(xAdapter.list, microblog);
			// if (index == -1) {
			// Log.d(TAG, "向列表中添加" + microblog.id);
			// xAdapter.list.add(microblog);
			// } else {
			// Log.d(TAG, "更新列表中的" + microblog.id);
			// xAdapter.list.set(index, microblog);
			// }
			// }
			// // 找出服务器上被删除的,删除其在本地的缓存
			// for (i = 0; i < xAdapter.list.size(); i++) {
			// Microblog microblog = xAdapter.list.get(i);
			// if (microblog.id <= result.get(0).id) {
			// int index = index(result, microblog);
			// if (index == -1) {
			// Log.d(TAG, "删除了一个本地错误缓存" + microblog.id);
			// MicroblogDatabase.microblogDelete(microblog.id,
			// XDatabase.DELETE_ONE);
			// xAdapter.list.remove(i--);
			// }
			// }
			// }
			// Log.d(TAG, "组织后的列表为" + xAdapter.list.toString());
			// }
			return XDatabase.LOAD_SUCCESSFUL;
		}

		@Override
		protected void onProgressUpdate(Boolean... values) {
			if (values[0]) {
				xAdapter.notifyDataSetChanged();
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			Log.d(TAG, "测试BUG");
			switch (result) {
			case XDatabase.LOAD_FAILED:
				if (callType == TYPE_UP) {
					if (xAdapter.type == TYPE_PLAZA) {
						Log.d(TAG, "广场刷新失败");
						plazaMblogList.loadUpFailed();
					} else {
						Log.d(TAG, "好友刷新失败");
						friendMblogList.loadUpFailed();
					}
				} else {
					if (xAdapter.type == TYPE_PLAZA) {
						Log.d(TAG, "广场加载失败");
						plazaMblogList.loadDownFailed();
					} else {
						Log.d(TAG, "好友加载失败");
						friendMblogList.loadDownFailed();
					}
				}
				break;
			case XDatabase.LOAD_OVER:
				if (callType == TYPE_UP) {
					if (xAdapter.type == TYPE_PLAZA) {
						Log.d(TAG, "广场刷新完了");
						plazaMblogList.loadUpSuccessful();
					} else {
						Log.d(TAG, "好友刷新完了");
						friendMblogList.loadUpSuccessful();
					}
				} else {
					if (xAdapter.type == TYPE_PLAZA) {
						Log.d(TAG, "广场加载完了");
						plazaMblogList.loadDownOver();
					} else {
						Log.d(TAG, "好友加载完了");
						friendMblogList.loadDownOver();
					}
				}
				break;
			case XDatabase.LOAD_SUCCESSFUL:
				if (callType == TYPE_UP) {
					if (xAdapter.type == TYPE_PLAZA) {
						Log.d(TAG, "广场刷新成功");
						plazaMblogList.loadUpSuccessful();
					} else {
						Log.d(TAG, "好友刷新成功");
						friendMblogList.loadUpSuccessful();
					}
				} else {
					if (xAdapter.type == TYPE_PLAZA) {
						Log.d(TAG, "广场加载成功");
						plazaMblogList.loadSuccessful();
					} else {
						Log.d(TAG, "好友加载成功");
						friendMblogList.loadSuccessful();
					}
				}
				break;
			default:
				break;
			}
			// sortXList(xAdapter.list);
			// onlyXList(xAdapter.list);
			xAdapter.notifyDataSetChanged();
		}
		//
		// private int index(ArrayList<Microblog> microblogs, Microblog
		// microblog) {
		// for (int i = 0; i < microblogs.size(); i++) {
		// if (microblogs.get(i).id == microblog.id) {
		// return i;
		// }
		// }
		// return -1;
		// }
	}

	class XItemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			try {
				JSONObject jObj = new JSONObject();
				Log.d(TAG, "进入微博" + currentAdapter.list.get(arg2).id);
				jObj.put("id", currentAdapter.list.get(arg2).id);
				XPage xPage = new PageMicroblog(getContext());
				xPage.setInfo(jObj);
				ActivityMain.addXPage(xPage);
			} catch (JSONException e) {
			} catch (IndexOutOfBoundsException e) {
			}
		}

	}

	private void sortXList(ArrayList<Microblog> list) {
		for (int i = 0; i < list.size(); i++) {
			int maxId = 0, maxIndex = 0;
			for (int j = i; j < list.size(); j++) {
				if (list.get(j).id > maxId) {
					maxId = list.get(j).id;
					maxIndex = j;
				}
			}
			Microblog x = list.get(maxIndex);
			list.set(maxIndex, list.get(i));
			list.set(i, x);
		}
	}

	private void onlyXList(ArrayList<Microblog> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			if (list.get(i).id == list.get(i + 1).id) {
				list.remove(i + 1);
			}
		}
	}
}
