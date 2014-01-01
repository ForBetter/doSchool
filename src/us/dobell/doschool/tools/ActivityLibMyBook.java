package us.dobell.doschool.tools;

import java.util.List;

import us.dobell.doschool.R;
import us.dobell.doschool.base.Values;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityLibMyBook extends Activity{

	private ImageButton left;
	private TextView title;
	private ListView listView;
	private Handler handler;
	private List<List<String>> data,data2;
	private LayoutInflater inflater;
	TextView text;
	
	public void back(View view){
		finish();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_activity_lib_mybook);
		left = (ImageButton)findViewById(R.id.tools_top_leftbutton);
		title = (TextView)findViewById(R.id.tools_top_text);
		title.setText("我的书籍");
		listView = (ListView)findViewById(R.id.tools_activity_lib_mybook_list);
		handler = new Handler();
		inflater = LayoutInflater.from(this);
		//listView.setAdapter(new MyListAdapter());
		init();
	}
	
	private void init(){
		new Thread(){
			public void run(){
				data = MRemoteServer.getMyBook(1,
						Values.User.getCurrentUserFunId(ActivityLibMyBook.this),
						ActivityLibMyBook.this.getIntent().getStringExtra("password"),
						getApplicationContext(),
						handler
						);
				
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						if(data==null){
							finish();
							return;
						}
						listView.setAdapter(new MyListAdapter());
						listView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								if(arg2==data.size()){
									text.setText("正在加载");
									new Thread(){
										public void run(){
											data2 = MRemoteServer.getMyBook(2,
													Values.User.getCurrentUserFunId(ActivityLibMyBook.this),
													ActivityLibMyBook.this.getIntent().getStringExtra("password"),
													getApplicationContext(),
													handler
													);
											
											handler.post(new Runnable() {
												@Override
												public void run() {
													listView.setAdapter(new MyListAdapter());
													text.setText("更多记录");
												}
											});
											
										}
									}.start();
									
									
								}
							}
						});
					}
				});
				
			}
		}.start();
		
		
		
	}
	
	class MyListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return data.size()+1+(data2==null?0:data2.size());
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
		public View getView(int position, View convertView, ViewGroup parent) {
			if(position < data.size()){
				View v = inflater.inflate(R.layout.tools_lib_mybook_item, null);
				TextView t1 =(TextView) v.findViewById(R.id.tools_lib_mybook_item_text1),
						 t2 =(TextView) v.findViewById(R.id.tools_lib_mybook_item_text2);
				t1.setText(data.get(position).get(0));
				t1.setTextColor(Color.BLUE);
				t2.setText("归还日期："+data.get(position).get(4));
				return v;
				
			}
			if(position == data.size()){
				//ImageView v = new ImageView(ActivityLibMyBook.this);
				View v = inflater.inflate(R.layout.tools_activity_lib_mybook_more, null);
				text = (TextView)v.findViewById(R.id.tools_lib_mybook_more_text);
				text.setText("更多记录");
				return v;
			}
			if(position > data.size()){
				View v = inflater.inflate(R.layout.tools_lib_mybook_item, null);
				TextView t1 =(TextView) v.findViewById(R.id.tools_lib_mybook_item_text1),
						 t2 =(TextView) v.findViewById(R.id.tools_lib_mybook_item_text2);
				t1.setText(data2.get(position-data.size()-1).get(0));
				t1.setTextColor(Color.BLUE);
				t2.setText("归还日期："+data2.get(position-data.size()-1).get(4));
				return v;
				
			}
			return null;
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			if(position == data.size())
				return true;
			return false;
		}

		
		
	}
	
}
