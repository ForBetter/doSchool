package us.dobell.doschool.tools;

import java.util.List;

import us.dobell.doschool.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityGongGao extends Activity {
	ListView list;
	List<List<String>> data;
	Handler handler;
	LayoutInflater inflater;
	TextView head;

	public void back(View view) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_activity_inform);
		handler = new Handler();
		head = (TextView) findViewById(R.id.tools_top_text);
		head.setText("通知公告");
		list = (ListView) findViewById(R.id.tools_activity_inform_list);
		inflater = LayoutInflater.from(this);
		new Thread() {
			public void run() {
				data = MRemoteServer.getGongao(ActivityGongGao.this, handler);
				if (data == null) {
					finish();
					return;
				}
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						list.setAdapter(new MyListAdapter());
						list.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								Intent it = new Intent(ActivityGongGao.this,
										ActivityGongGaoDetail.class);
								it.putExtra("text", data.get(arg2).get(1));
								it.putExtra("name", data.get(arg2).get(0));
								it.putExtra("time", data.get(arg2).get(2));
								startActivity(it);
							}

						});
					}
				});

			}
		}.start();

	}

	class MyListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = inflater.inflate(R.layout.tools_inform_item, null);
			TextView t1, t2;
			t1 = (TextView) v.findViewById(R.id.tools_inform_text1);
			t2 = (TextView) v.findViewById(R.id.tools_inform_text2);
			t1.setText(data.get(position).get(0));
			t2.setText(data.get(position).get(2));
			return v;
		}

	}

}
