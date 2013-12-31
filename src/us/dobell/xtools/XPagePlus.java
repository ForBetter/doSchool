package us.dobell.xtools;

import java.util.ArrayList;

import us.dobell.xtools.XListView.XLoadDownListener;
import us.dobell.xtools.XListView.XLoadUpListener;
import android.content.Context;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class XPagePlus<X extends XObject> extends XPage {
	public static final String TAG = "XPagePlus";
	public static final int TYPE_UP = 0;
	public static final int TYPE_DOWN = 1;

	private boolean hasSpecial;
	private ArrayList<X> xs;
	private XListView xList;
	public XAdapter xAdapter;
	public XListener xListener;
	public LoadTask loadUpTask;
	public LoadTask loadDownTask;

	public XPagePlus(Context context, boolean hasSpcial) {
		super(context);
		this.hasSpecial = hasSpcial;
	}

	@Override
	public void onCreate() {
		xs = new ArrayList<X>();
		TextView titleTxt = new TextView(getContext());
		titleTxt.setTextSize(19);
		titleTxt.setGravity(Gravity.CENTER);
		titleMidLayout.addView(titleTxt,
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		xList = new XListView(getContext());
		xAdapter = new XAdapter();
		xListener = new XListener();
		contentLayout.addView(xList, LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		xList.getXList().setAdapter(xAdapter);
		xList.getXList().setOnItemClickListener(xListener);
		xList.setXLoadDownListener(new LoadDownListener());
		xList.setXLoadUpListener(new LoadUpListener());
	}

	public X getXObject(int index) {
		return xs.get(index);
	}

	public X getXObjectById(int id) {
		for (X x : xs) {
			if (x.id == id) {
				return x;
			}
		}
		return null;
	}

	class XListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			try {
				Log.d(TAG, "点击了Item" + arg2);
				itemClick(arg0, arg1, arg2 - (hasSpecial ? 1 : 0), arg3);
			} catch (IndexOutOfBoundsException e) {
			}
		}
	}

	class LoadDownListener implements XLoadDownListener {

		@Override
		public void onLoadDown() {
			if (loadUpTask == null || loadUpTask.getStatus() == Status.FINISHED) {
				loadUpTask = new LoadTask();
				loadUpTask.execute(TYPE_DOWN, XDatabase.LOAD_OLD);
			}
		}
	}

	class LoadUpListener implements XLoadUpListener {

		@Override
		public void onLoadUp() {
			Log.d(TAG, "load Up");
			if (loadDownTask == null
					|| loadDownTask.getStatus() == Status.FINISHED) {
				xs.clear();
				loadDownTask = new LoadTask();
				loadDownTask.execute(TYPE_UP, XDatabase.LOAD_NEW);
			}
		}
	}

	public class XAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return xs.size() + (hasSpecial ? 1 : 0);
		}

		@Override
		public Object getItem(int position) {
			if (hasSpecial && position == 0) {
				return null;
			} else {
				return xs.get(position - (hasSpecial ? 1 : 0));
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (hasSpecial) {
				if (position == 0) {
					return getSpecialView();
				} else {
					return getItemView(position - 1, null, parent);
				}
			} else {
				return getItemView(position, null, parent);
			}
		}
	}

	// class RefreshTask extends AsyncTask<Void, Void, Boolean> {
	//
	// @Override
	// protected Boolean doInBackground(Void... params) {
	// try {
	// Log.d(TAG, "正在用数据库中的数据刷新列表+lastId=" + xs.get(0).id + "size="
	// + xs.size());
	// xs = loadDatabase(xs.get(0).id + 1, XServer.LOAD_OLD, xs.size());
	// return true;
	// } catch (Exception e) {
	// return false;
	// }
	// }
	//
	// @Override
	// protected void onPostExecute(Boolean result) {
	// if (result) {
	// xAdapter.notifyDataSetChanged();
	// }
	// }
	// }

	class LoadTask extends AsyncTask<Integer, Boolean, Integer> {
		int callType;
		int rollType;

		@Override
		protected Integer doInBackground(Integer... params) {
			callType = params[0];
			rollType = params[1];
			int lastId = 0;
			if (xs.size() == 0) {
				Log.d(TAG, "初始加载");
				lastId = 0;
				rollType = XDatabase.LOAD_START;
			} else {
				if (rollType == XDatabase.LOAD_NEW) {
					Log.d(TAG, "加载新的");
					lastId = xs.get(0).id;
				} else {
					Log.d(TAG, "加载旧的");
					rollType = XDatabase.LOAD_OLD;
					lastId = xs.get(xs.size() - 1).id;
				}
			}
			// XListView的上面的加载条只能做向最新的刷新LOAD_NEW;
			// XListView的下面的加载条能做初始加载和向更旧的加载LOAD_START & LOAD_OLD;
			if (callType == TYPE_UP && rollType == XDatabase.LOAD_OLD) {
				return XDatabase.LOAD_SUCCESSFUL;
			}
			if (callType == TYPE_DOWN && rollType == XDatabase.LOAD_NEW) {
				return XDatabase.LOAD_SUCCESSFUL;
			}
			Log.d(TAG, "从数据库中加载缓存数据");
			ArrayList<X> result = loadDatabase(lastId, rollType,
					XDatabase.LOAD_COUNT);
			Log.d(TAG, "数据为" + result.toString());
			if (rollType == XDatabase.LOAD_NEW) {
				xs.addAll(0, result);
			} else {
				xs.addAll(result);
			}
			Log.d(TAG, "刷新界面");
			publishProgress(result != null && result.size() != 0);
			Log.d(TAG, "从服务器上加载最新的数据");
			result = loadServer(lastId, rollType, XDatabase.LOAD_COUNT);
			if (result == null) {
				Log.d(TAG, "服务器加载失败");
				return XDatabase.LOAD_FAILED;
			}
			if (result.size() == 0) {
				Log.d(TAG, "服务器没有更多数据了");
				return XDatabase.LOAD_OVER;
			}
			Log.d(TAG, "数据为" + result.toString());
			if (rollType == XDatabase.LOAD_NEW) {
				// 加载更新的几条
				int i = 0;
				// 先将获得的所有的项更新(update)到列表和数据库
				Log.d(TAG, "更新数据中的缓存");
				for (i = result.size() - 1; i >= 0; i--) {
					X x = result.get(i);
					saveToDatabase(x);
					int index = index(xs, x);
					if (index == -1) {
						xs.add(0, x);
					} else {
						xs.set(index, x);
					}
				}
				// // 移除服务器上被删除的,删除其在本地的缓存
				// for (i = 0; i < xs.size(); i++) {
				// X x = xs.get(i);
				// if (x.id >= result.get(result.size() - 1).id) {
				// int index = index(result, x);
				// if (index == -1) {
				// Log.d(TAG, "删除了一个本地错误缓存");
				// deleteFromDatabase(x);
				// xs.remove(i--);
				// }
				// }
				// }
			} else {
				// 加载更旧的几条
				int i = 0;
				// 先将获得的所有的项更新(update)到列表和数据库
				Log.d(TAG, "更新数据中的缓存");
				for (i = 0; i < result.size(); i++) {
					X x = result.get(i);
					saveToDatabase(x);
					int index = index(xs, x);
					if (index == -1) {
						xs.add(x);
					} else {
						xs.set(index, x);
					}
				}
				// // 找出服务器上被删除的,删除其在本地的缓存
				// for (i = 0; i < xs.size(); i++) {
				// X x = xs.get(i);
				// if (x.id <= result.get(0).id) {
				// int index = index(result, x);
				// if (index == -1) {
				// Log.d(TAG, "删除了一个本地错误缓存");
				// deleteFromDatabase(x);
				// xs.remove(i--);
				// }
				// }
				// }
			}
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
			switch (result) {
			case XDatabase.LOAD_FAILED:
				if (callType == TYPE_UP) {
					xList.loadUpFailed();
				} else {
					xList.loadDownFailed();
				}
				break;
			case XDatabase.LOAD_OVER:
				if (callType == TYPE_UP) {
					xList.loadUpSuccessful();
				} else {
					xList.loadDownOver();
				}
				break;
			case XDatabase.LOAD_SUCCESSFUL:
				if (callType == TYPE_UP) {
					xList.loadUpSuccessful();
				} else {
					xList.loadSuccessful();
				}
				Log.d(TAG, "正在重新组织列表");
				break;
			default:
				break;
			}
			sortXList();
			onlyXList();
			xAdapter.notifyDataSetChanged();
		}

		private int index(ArrayList<X> xs, X x) {
			for (int i = 0; i < xs.size(); i++) {
				if (xs.get(i).id == x.id) {
					return i;
				}
			}
			return -1;
		}
	}

	private void sortXList() {
		for (int i = 0; i < xs.size(); i++) {
			int maxId = 0, maxIndex = 0;
			for (int j = i; j < xs.size(); j++) {
				if (xs.get(j).id > maxId) {
					maxId = xs.get(j).id;
					maxIndex = j;
				}
			}
			X x = xs.get(maxIndex);
			xs.set(maxIndex, xs.get(i));
			xs.set(i, x);
		}
	}

	private void onlyXList() {
		for (int i = 0; i < xs.size() - 1; i++) {
			if (xs.get(i).id == xs.get(i + 1).id) {
				xs.remove(i + 1);
			}
		}
	}

	public abstract void itemClick(AdapterView<?> xListView, View view,
			int index, long id);

	public abstract View getSpecialView();

	public abstract View getItemView(int index, View convertView,
			ViewGroup parent);

	public abstract ArrayList<X> loadDatabase(int lastId, int rollType,
			int objCount);

	public abstract ArrayList<X> loadServer(int lastId, int rollType,
			int objCount);

	public abstract void saveToDatabase(X x);

	public abstract void deleteFromDatabase(X x);
}
