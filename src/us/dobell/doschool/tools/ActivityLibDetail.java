package us.dobell.doschool.tools;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import us.dobell.doschool.R;


public class ActivityLibDetail extends Activity {

	private TextView nameText, authorText, reqText, pubText;
	private ListView listView;
	List<String> adds;
	LayoutInflater inflater;
	Handler handler;

	public void back(View view) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_activity_lib_searchdetail);
		inflater = LayoutInflater.from(this);
		handler = new Handler();
		nameText = (TextView) findViewById(R.id.tools_top_text);
		authorText = (TextView) findViewById(R.id.tools_activity_lib_searchdetail_author);
		reqText = (TextView) findViewById(R.id.tools_activity_lib_searchdetail_req);
		pubText = (TextView) findViewById(R.id.tools_activity_lib_searchdetail_pubname);
		listView = (ListView) findViewById(R.id.tools_activity_lib_searchdetail_listview);

		Intent it = getIntent();
		String name = it.getStringExtra("name");
		String author = it.getStringExtra("author");
		String req_number = it.getStringExtra("req_number");
		String publisher = it.getStringExtra("publisher");
		final String link = it.getStringExtra("link");
		// String pub_date = it.getStringExtra("pub_date");

		new Thread() {
			public void run() {
				adds = MRemoteServer.getAddressAndStatus(link,
						getApplicationContext(), handler);
				if (adds == null) {
					finish();
					return;
				}
				handler.post(new Runnable() {

					@Override
					public void run() {
						listView.setAdapter(new MListViewAdapter());
					}
				});

			}
		}.start();

		nameText.setText(name);
		authorText.setText(author);
		reqText.setText(req_number);
		pubText.setText(publisher);

	}

	class MListViewAdapter implements ListAdapter {

		@Override
		public int getCount() {
			return adds.size() / 2;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public int getItemViewType(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = inflater.inflate(R.layout.tools_lib_searchdetail_additem,
					null);
			ImageView im = (ImageView) v
					.findViewById(R.id.tools_lib_searchdetail_additem_img);
			TextView text = (TextView) v
					.findViewById(R.id.tools_lib_searchdetail_additem_add);
			if (adds.get(position * 2 + 1).equals("\u5728\u9986"))
				im.setBackgroundResource(R.drawable.tools_lib_in);
			else
				im.setBackgroundResource(R.drawable.tools_lib_out);
			text.setText(adds.get(position * 2));
			return v;
		}

		@Override
		public int getViewTypeCount() {
			return 1;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public boolean isEnabled(int arg0) {
			return false;
		}

	}
}
