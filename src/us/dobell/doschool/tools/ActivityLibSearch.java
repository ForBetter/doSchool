package us.dobell.doschool.tools;

import java.util.List;

import us.dobell.doschool.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityLibSearch extends Activity {

	private ImageButton left, login, search;
	private ListView listView;
	private LayoutInflater inflater;
	private List<List<String>> listData, more;
	private Handler handler;
	private View footer;
	private View img;
	private TextView head;
	private RotateAnimation animation;
	// 从第一页开始
	private int page = 1;
	private boolean loadFinish = false;
	MOnItemClickListener listener;
	int lastItem;
	MListViewAdapter adapter;

	public void back(View view) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tools_activity_lib_search);
		left = (ImageButton) findViewById(R.id.tools_top_leftbutton);
		handler = new Handler();
		listView = (ListView) findViewById(R.id.tools_activity_lib_search_listview);
		head = (TextView) findViewById(R.id.tools_top_text);
		head.setText("搜索结果");
		inflater = LayoutInflater.from(this);
		footer = inflater.inflate(R.layout.tools_lib_search_footer, null);
		// img = (View)footer.findViewById(R.id.tools_lib_search_footer_img);
		animation = new RotateAnimation(0.0f, +360.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation.setRepeatMode(RotateAnimation.INFINITE);

		listener = new MOnItemClickListener();
		listView.setOnItemClickListener(listener);
		listView.setOnScrollListener(new ScrollListener());
		adapter = new MListViewAdapter();
		init();
	}

	private void init() {
		new Thread() {
			public void run() {
				handler.post(new Runnable() {

					@Override
					public void run() {
						listView.addFooterView(footer);
						listView.setAdapter(adapter);

					}
				});

				// img.startAnimation(animation);
				listData = MRemoteServer.getLibSearchList(getIntent()
						.getStringExtra("word"),
						getIntent().getStringExtra("type"), page,
						getApplicationContext(), handler);
				if (listData == null) {
					finish();
					return;
				}
				handler.post(new Runnable() {

					@Override
					public void run() {
						adapter.notifyDataSetChanged();
						if (MRemoteServer.NEXT_PAGE == 0) {
							if (listView.getFooterViewsCount() > 0)
								listView.removeFooterView(footer);
						}
						loadFinish = true;
					}
				});
			}
		}.start();

	}

	class ScrollListener implements OnScrollListener {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (MRemoteServer.NEXT_PAGE == 0) {
				if (listView.getFooterViewsCount() > 0)
					listView.removeFooterView(footer);
				return;
			} else if (loadFinish && lastItem == 0
					&& scrollState == SCROLL_STATE_IDLE) {
				new Thread() {
					public void run() {
						loadFinish = false;
						Log.i("tttt", "a new thread is running!");
						// img.startAnimation(animation);
						page++;
						more = MRemoteServer.getLibSearchList(getIntent()
								.getStringExtra("word"), getIntent()
								.getStringExtra("type"), page,
								getApplicationContext(), handler);
						if (more == null)
							return;

						handler.post(new Runnable() {
							@Override
							public void run() {
								listData.addAll(more);
								adapter.notifyDataSetChanged();
								loadFinish = true;
							}
						});
					}
				}.start();

			}

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			lastItem = firstVisibleItem + visibleItemCount - totalItemCount;

		}
	}

	class MListViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return listData != null ? listData.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = inflater.inflate(R.layout.tools_lib_search_item, null);
			TextView name, author, pub;
			name = (TextView) v.findViewById(R.id.tools_lib_search_item_name);
			author = (TextView) v
					.findViewById(R.id.tools_lib_search_item_author);
			pub = (TextView) v.findViewById(R.id.tools_lib_search_item_pubname);
			name.setText(listData.get(position).get(0));
			author.setText(listData.get(position).get(1));
			pub.setText(listData.get(position).get(3));
			return v;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			if (position < listData.size())
				return true;
			return false;
		}

	}

	class MOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int p, long aa) {
			Intent it = new Intent(ActivityLibSearch.this,
					ActivityLibDetail.class);
			it.putExtra("name", listData.get(p).get(0));
			it.putExtra("author", listData.get(p).get(1));
			it.putExtra("req_number", listData.get(p).get(2));
			it.putExtra("publisher", listData.get(p).get(3));
			it.putExtra("pub_date", listData.get(p).get(4));
			it.putExtra("link", listData.get(p).get(5));
			ActivityLibSearch.this.startActivity(it);
		}

	}

}
