package us.dobell.xtools;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

public abstract class XListViewPlus<X extends XObject> extends XListView {
	public static final int LOAD_UP = 0;
	public static final int LOAD_DOWN = 1;

	private ArrayList<X> list;
	private XAdapter adapter;
	private RefreshTask refreshTask;
	private LoadTask loadTask;

	public XListViewPlus(Context context) {
		super(context);
		init();
	}

	public XListViewPlus(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public XListViewPlus(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		list = new ArrayList<X>();
		adapter = new XAdapter();
		setXLoadUpListener(new XLoadUpListener() {

			@Override
			public void onLoadUp() {
				if (refreshTask == null
						|| refreshTask.getStatus() == Status.FINISHED) {
					refreshTask = new RefreshTask(LOAD_UP);
					refreshTask.execute();
				}
			}
		});
		setXLoadDownListener(new XLoadDownListener() {

			@Override
			public void onLoadDown() {
				if (list.size() == 0) {
					if (refreshTask == null
							|| refreshTask.getStatus() == Status.FINISHED) {
						refreshTask = new RefreshTask(LOAD_DOWN);
						refreshTask.execute();
					}
				} else {
					if (loadTask == null
							|| loadTask.getStatus() == Status.FINISHED) {
						loadTask = new LoadTask();
						loadTask.execute();
					}
				}
			}
		});
		getXList().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				listenerItemClick(arg0, arg1, arg2, arg3);
			}
		});
		getXList().setAdapter(adapter);
	}

	class RefreshTask extends AsyncTask<Void, Void, Boolean> {
		private int type;

		public RefreshTask(int type) {
			this.type = type;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			ArrayList<X> temp = null;
			temp = taskDatabaseRefresh();
			if (temp != null && temp.size() > 0) {
				list = temp;
				publishProgress();
			}
			temp = taskServerRefresh();
			if (temp != null) {
				if (temp.size() > 0) {
					list = temp;
					for (X x : list) {
						taskDatabaseSave(x);
					}
					publishProgress();
					return false;
				} else {
					return true;
				}
			} else {
				return null;
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			adapter.notifyDataSetChanged();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (type == LOAD_UP) {
				if (result == null) {
					loadUpFailed();
				} else {
					loadUpSuccessful();
				}
			} else {
				if (result == null) {
					loadDownFailed();
				} else if (result) {
					loadDownOver();
				} else {
					loadSuccessful();
				}
			}
		}
	}

	class LoadTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			ArrayList<X> temp = null;
			int lastId = list.get(list.size() - 1).id;
			temp = taskDatabaseLoad(lastId);
			if (temp != null && temp.size() > 0) {
				list.addAll(temp);
				publishProgress();
			}
			temp = taskServerLoad(lastId);
			if (temp != null) {
				if (temp.size() > 0) {
					mergeList(lastId, temp);
					publishProgress();
					return false;
				} else {
					return true;
				}
			} else {
				return null;
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			adapter.notifyDataSetChanged();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result == null) {
				loadDownFailed();
			} else if (result) {
				loadDownOver();
			} else {
				loadSuccessful();
			}
		}

		private void mergeList(int lastId, ArrayList<X> temp) {
			int i = 0, j = 0;
			// 略过原来已经存在在列表中的
			for (; i < list.size(); i++) {
				if (list.get(i).id <= lastId) {
					break;
				}
			}
			// 从这里开始将从数据库中得到的数据和网络得到的数据比较，更新本地数据库数据，删除服务器端删除的数据
			try {
				while (true) {
					if (list.get(i).id == temp.get(j).id) {
						// 这个数据在本地数据库中存在，网络数据库中也存在，更新本地数据库
						list.set(i, temp.get(j));
						taskDatabaseSave(temp.get(j));
						i++;
						j++;
					} else if (list.get(i).id < temp.get(j).id) {
						// 这个数据在本地数据库中不存在，网络数据库存在，添加到本地数据库
						list.add(i, temp.get(j));
						taskDatabaseSave(temp.get(j));
						i++;
						j++;
					} else {
						// 这个数据在本地数据库中存在，网路数据库中不存在，删除它
						taskDatabaseDelete(list.remove(i));
					}
				}
			} catch (Exception e) {
			}
		}
	}

	class XAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return adapterGetCount();
		}

		@Override
		public Object getItem(int position) {
			return adapterGetItem(position);
		}

		@Override
		public long getItemId(int position) {
			return adapterGetItemId(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return adapterGetView(position, convertView, parent);
		}

	}

	public abstract void listenerItemClick(AdapterView<?> arg0, View arg1,
			int arg2, long arg3);

	public abstract int adapterGetCount();

	public abstract Object adapterGetItem(int position);

	public abstract long adapterGetItemId(int position);

	public abstract View adapterGetView(int position, View convertView,
			ViewGroup parent);

	public abstract ArrayList<X> taskDatabaseRefresh();

	public abstract ArrayList<X> taskServerRefresh();

	public abstract ArrayList<X> taskDatabaseLoad(int lastId);

	public abstract ArrayList<X> taskServerLoad(int lastId);

	public abstract void taskDatabaseSave(X x);

	public abstract void taskDatabaseDelete(X x);

}
