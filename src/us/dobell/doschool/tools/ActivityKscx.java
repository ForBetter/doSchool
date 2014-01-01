package us.dobell.doschool.tools;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.doschool.R;
import us.dobell.doschool.base.Values;
import us.dobell.doschool.user.UserServer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityKscx extends Activity{
	ListView list;
	List<List<String>> data;
	Handler handler;
	LayoutInflater inflater;
	TextView head;
	int count;
	int offList=0;
	AlertDialog alertDialog,alertChecking;
	Context context;
	EditText edit;
	public void back(View view){
		finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_activity_kscx);
		context = this;
		edit = new EditText(context);
		handler = new Handler();
		list = (ListView)findViewById(R.id.tools_activity_kscx_list);
		head = (TextView)findViewById(R.id.tools_top_text);
		head.setText("考试查询");
		inflater = LayoutInflater.from(this);
		
		init();
		alertDialog.show();
	
	
	
	
	
	
	}
	
	class MyListAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size()-count;
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
			View v = inflater.inflate(R.layout.tools_kscx_item, null);
			TextView t;
			t= (TextView)v.findViewById(R.id.tools_kscx_item_text1);
			String s[] = data.get(position+offList).get(1).split("\\(");
			t.setText(s[0]);
			LinearLayout ll = (LinearLayout)v.findViewById(R.id.tools_kscx_item_layout);
			Log.i("www","************");
			while(true){
				
				View tt = inflater.inflate(R.layout.tools_kscx_item_ks, null);
				TextView t1,t2,t3,t4;
				t1= (TextView)tt.findViewById(R.id.tools_kscx_ks_text1);
				t2= (TextView)tt.findViewById(R.id.tools_kscx_ks_text2);
				t3= (TextView)tt.findViewById(R.id.tools_kscx_ks_text3);
				t4= (TextView)tt.findViewById(R.id.tools_kscx_ks_text4);
				t1.setText(data.get(position+offList).get(0));
				
				t2.setText(data.get(position+offList).get(1).split("\\(")[1].replace(")", ""));
				t3.setText(data.get(position+offList).get(2));
				t4.setText("座位号："+data.get(position+offList).get(3));
				ll.addView(tt);
				Log.i("www position:"+position+" offList:"+offList+"ssize"+data.get(position+offList).size(),"a2");
				if(data.size() > position+offList+1 &&  data.get(position+offList+1).size()>5){//日期相同则，，，
					offList++;
					Log.i("www"+position+t1.getText()+offList,"dataSize"+data.get(position+offList).size());
				}
				else{
					Log.i("www"+position+t1.getText()+offList,"eee"+data.get(position+offList).size());
					break;
				}
				
			}
			
			
			return v;
		}
		
	}
	
	
	private void readyData(List<List<String>> data){
		int off=0;
		int c=0;
		for(int i=0;i<data.size()-1;i++){
			String t = data.get(i-off).get(1);//获取日期
			if(t.split("\\(")[0].equals(
					data.get(i+1).get(1).split("\\(")[0]))
			{
				off++;
				data.get(i+1).add("la");//标记 使数组数量达到6
			}
			else{
				c+=off;
				off=0;
			}
			
		}
		Log.i("count","A"+c);
		
		count = c;
	}
	
	private void init(){
		alertDialog = new AlertDialog.Builder(
				context)
				.setTitle("请输入你的教务处密码")
				.setView(edit)
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(
									DialogInterface dialog,
									int which) {
								if(edit.getText().toString().trim().equals("")){
									return;
								}
								
								alertDialog.dismiss();
								handler.post(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated
										// method stub
										alertChecking = new AlertDialog.Builder(
												context)
												.setMessage("正在检测")
												.create();
										alertChecking.show();
									}
								});
								new Thread() {
									public void run() {
										JSONObject o = UserServer.userCheck(
												Values.User.getCurrentUserFunId(context),
												edit.getText().toString().trim());
										int ss = -1;
										try {
											ss = o.getInt("code");
											Log.i("code",""+ss);
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										alertChecking.dismiss();
										if (ss!=0x12) {
											handler.post(new Runnable() {

												@Override
												public void run() {
													// TODO Auto-generated
													Dialog alertDialog = new AlertDialog.Builder(
															context)
															.setMessage(
																	"错误!")
															.setPositiveButton(
																	"确定",
																	new DialogInterface.OnClickListener() {
																		public void onClick(
																				DialogInterface dialog,
																				int which) {
																			finish();
																		} 
																	})
															.create();
													alertDialog.show();
													Values.User.saveFunPasswd(context, 
															Values.User.getCurrentUserFunId(context), "");
												}
											});
										}
										else{
											String pw  = 
													edit.getText().toString()
													.trim();
											Values.User.saveFunPasswd(context, 
													Values.User.getCurrentUserFunId(context), pw);
											

											new Thread(){
												public void run(){
													data = MRemoteServer.getKscx(ActivityKscx.this,Values.User.getCurrentUserFunId(context),
															Values.User.getFunPasswd(context, Values.User.getCurrentUserFunId(context)), handler);
													
													
													if(data == null){
														finish();
														return;
													}
											 		
													readyData(data);
													Log.i("www","a");
													
													handler.post(new Runnable() {
														
														@Override
														public void run() {
															// TODO Auto-generated method stub
															list.setAdapter(new MyListAdapter());
															
														}
													});
													
												}
											}.start();
											
										
										}

									}
								}.start();

							}
						}).create();
	}

}
