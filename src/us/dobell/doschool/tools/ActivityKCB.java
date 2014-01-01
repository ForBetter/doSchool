package us.dobell.doschool.tools;

import java.util.ArrayList;
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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Space;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityKCB extends Activity {
	
	int inW,inH;
	
	int tongzhilanHeight;
	boolean hasMeasured;
	TextView tv;
	ImageButton lBt,rBt;
	GridLayout gl;
	LinearLayout ll;
	GridLayout.LayoutParams glp;
	List<Project> pjlist = new ArrayList<Project>();
	Handler handler;
	float heightForInit,weightForInit;
	RelativeLayout v;
	String pw="";
	Context context;
	AlertDialog alertDialog,alertChecking;
	EditText edit;
	public void back(View view){
		finish();
	}
	
	private void init() {
		Log.i("tttt","2"+context);
		edit = new EditText(context);
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
											
										
											handler.post(new Runnable() {
												public void run(){
													init(inH,inW);
												}
											});
										
										}

									}
								}.start();

							}
						}).create();

	
	}

	public void ready() {
		init();
		
		final String passw = 
				Values.User.getFunPasswd(context,Values.User.getCurrentUserFunId(context));
		
		
		Log.i("code","rrr");
		
		if (passw == null || passw.equals("")) {
			alertDialog.show();
			Log.i("code","pw "+passw);
		}
		else{
			handler.post(new Runnable() {
			public void run(){
				Log.i("code","rrr"+inH+" "+inW);
				init(inH,inW);
			}
		});
		}
		/*
		else
		{
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
							passw);
					
					int ss = -1;
					try {
						ss = o.getInt("code");
						Log.i("code","aaa "+ss);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					alertChecking.dismiss();
					if (ss!=0x00000012) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated
								Dialog alertDialog = new AlertDialog.Builder(
										context)
										.setMessage(
												"错误!请检查密码是否正确")
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
								Values.User.saveFunPasswd(context,Values.User.getCurrentUserFunId(context) ,"");
								alertDialog.show();
								
							}
						});
					}
					else{
						Values.User.saveFunPasswd(context,Values.User.getCurrentUserFunId(context) ,passw);
						pw = passw;
						handler.post(new Runnable() {
							public void run(){
								init(inH,inW);
							}
						});
					}

				}
			}.start();
		}*/
		/*
		handler.post(new Runnable() {
			public void run(){
				init(inH,inW);
			}
		});*/
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_activity_kcb);
		context = this;
		/**
		 * 测试数据
		 */
		/*
		File f = new File(Environment.getExternalStorageDirectory()+"/aa.txt");
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		handler = new Handler();
		v  = (RelativeLayout)findViewById(R.id.tools_activity_kcb_top);
		lBt = (ImageButton) findViewById(R.id.tools_top_leftbutton);
		rBt= (ImageButton) findViewById(R.id.tools_top_rightbutton);
		tv = (TextView) findViewById(R.id.tools_top_text);
		ll = (LinearLayout) findViewById(R.id.tool_kcb_layout);
		gl = (GridLayout) findViewById(R.id.tool_kcb_gridlayout);
		// gl.setUseDefaultMargins(true);
		// gl.setAlignmentMode(GridLayout.ALIGN_MARGINS);
		// gl.setRowOrderPreserved(false);
		// gl.setColumnOrderPreserved(false);
		gl.setRowCount(12);
		gl.setColumnCount(9);
		tv.setText("课程表");
		rBt.setImageResource(R.drawable.tools_kcb_refresh);
		rBt.setBackgroundResource(R.drawable.tools_kcb_refreshbt);
		// llp.bottomMargin=1;

		rBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								MRemoteServer.deleteKCBListFromDB(Values.User
										.getCurrentUserFunId(getApplicationContext()));
								Log.i("buyaoa","aa");
								//init();
								
								for (int i = 1; i < 12; i++) {
									glp = new android.support.v7.widget.GridLayout.LayoutParams();
									glp.rowSpec = GridLayout.spec(i + 1, 1);
									glp.columnSpec = GridLayout.spec(1, 9, GridLayout.FILL);
									glp.height = (int) heightForInit;
									Space s = new Space(context);
									gl.addView(s, glp);
								}
								
								alertDialog.show();
								/*
								Log.i("buyaoa","aa2");
								String pw = 
										Values.User.getFunPasswd(context,Values.User.getCurrentUserFunId(context));
								Log.i("buyaoa","aa3");
								if ((pjlist = MRemoteServer.getKCBListFromDB(Values.User
										.getCurrentUserFunId(getApplicationContext()))) == null) {
									Log.i("buyaoa","aa4");
									pjlist = MRemoteServer.getKCBList(
											Values.User
											.getCurrentUserFunId(getApplicationContext()),
											pw,
											getApplicationContext(),
											handler);
									if (pjlist == null) {
										finish();
										return;
									}
								}

								for (int i = 0; i < pjlist.size(); i++) {
									pjlist.get(i).setContext(context);
								}
								
								handler.post(new Runnable() {
									
									@Override
									public void run() {
										
										for (int i = 1; i < 12; i++) {
											glp = new android.support.v7.widget.GridLayout.LayoutParams();
											glp.rowSpec = GridLayout.spec(i + 1, 1);
											glp.columnSpec = GridLayout.spec(1, 9, GridLayout.FILL);
											glp.height = (int) heightForInit;
											Space s = new Space(context);
											gl.addView(s, glp);
										}
										
										//添加课程
										for (int i = 0; i < pjlist.size(); i++) {
											Project v = pjlist.get(i);
											glp = new android.support.v7.widget.GridLayout.LayoutParams();
											// glp.columnSpec = GridLayout.spec(i,1);
											glp.rowSpec = GridLayout.spec(v.getStartTime()+1, v.getTimes(),
													GridLayout.FILL);
											glp.columnSpec = GridLayout.spec(v.getStartWeek() + 1, 1);
											glp.width = (int) weightForInit;
											glp.height = (int) heightForInit;
											// v.setLayoutParams(glp);
											View vv = new View(context);
											vv.setBackgroundColor(Color.rgb(v.getColor()/0x10000, v.getColor()/0x100-v.getColor()/0x10000*0x100, v.getColor()%0x100));
											gl.addView(vv, glp);
											gl.addView(v.getView(null), glp);
										}
										
									}
								});*/
							}
						});
						

					}
				}).start();
			}
		});
		
		ViewTreeObserver vto = ll.getViewTreeObserver();

		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				if (hasMeasured == false) {
					int notificationHeight = 0;
					
					int resourceId = getResources().getIdentifier(
							"status_bar_height", "dimen", "android");
					if (resourceId > 0) {
						notificationHeight = getResources()
								.getDimensionPixelSize(resourceId);
					}

					int height;
					int width;
					DisplayMetrics dm = new DisplayMetrics();

					getWindowManager().getDefaultDisplay().getMetrics(dm);
					height = dm.heightPixels;
					width = dm.widthPixels;

					int btHeight = v.getHeight();
					
					inH = height - btHeight - notificationHeight;
					inW = width;
					

					hasMeasured = true;
					ready();
				}
				return true;
			}
		});
		
		// ll.addView(gl, new
		// LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT));

	}

	private void init(int height,int width) {
		/**
		 * 周x 一行的高度
		 */
		int weekHeight=25;
		
		/**
		 * 序号栏的宽度
		 */
		int numWidth = 25;
		
		/**
		 * 内部View 的宽度和高度
		 */
		final float h = (height -weekHeight)  / 11.0f;
		final float w = (width-numWidth) / 7;
		heightForInit = h;
		weightForInit = w;
		// 添加虚线的背景view
		for (int i = 1; i < 12; i++) {
			glp = new android.support.v7.widget.GridLayout.LayoutParams();
			glp.rowSpec = GridLayout.spec(i + 1, 1);
			glp.columnSpec = GridLayout.spec(1, 9, GridLayout.FILL);
			glp.height = (int) h;
			View s = new View(this);
			ImageView im = new ImageView(this);
			im.setImageResource(R.drawable.ic_category_bar);
			s.setBackgroundDrawable(im.getDrawable());
			gl.addView(s, glp);
		}
		//添加序号
		for (int i = 1; i < 12; i++) {
			glp = new android.support.v7.widget.GridLayout.LayoutParams();
			TextView t = new TextView(this);
			t.setBackgroundColor(Color.TRANSPARENT);
			t.setText("" + (i));
			t.setGravity(Gravity.CENTER);
			t.setTextColor(Color.BLACK);
			//t.setWidth((int) w);
			glp.rowSpec = GridLayout.spec(i + 1, 1);
			glp.height = (int) h;
			glp.width = (int) numWidth;
			glp.columnSpec = GridLayout.spec(1, 1);
			gl.addView(t, glp);
		}
		//添加日期
		for (int i = 1; i < 8; i++) {
			glp = new android.support.v7.widget.GridLayout.LayoutParams();
			TextView t = new TextView(this);
			t.setBackgroundColor(Color.TRANSPARENT);
			t.setText("周" + (i));
			t.setTextColor(Color.MAGENTA);
			t.setGravity(Gravity.CENTER);
			//t.setHeight(weekHeight);
			glp.rowSpec = GridLayout.spec(1, 1);
			glp.height = (int) weekHeight;
			glp.width = (int) w;
			glp.columnSpec = GridLayout.spec(i+1, 1);
			gl.addView(t, glp);
		}
		
		// 异步加载课程。。
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if ((pjlist = MRemoteServer.getKCBListFromDB(Values.User
						.getCurrentUserFunId(getApplicationContext()))) == null) {
					Log.i("hhhh", "pjlist == null");
					String pw = Values.User.getFunPasswd(context,Values.User.getCurrentUserFunId(context));
					pjlist = MRemoteServer.getKCBList(
							Values.User
							.getCurrentUserFunId(getApplicationContext()),
							pw,
							getApplicationContext(),
							handler);
					if (pjlist == null) {
						Log.i("hhhh", "kcb is finish");
						finish();
						return;
					}
				}

				for (int i = 0; i < pjlist.size(); i++) {
					pjlist.get(i).setContext(context);
				}
				
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						//添加课程
						for (int i = 0; i < pjlist.size(); i++) {
							Project v = pjlist.get(i);
							glp = new android.support.v7.widget.GridLayout.LayoutParams();
							// glp.columnSpec = GridLayout.spec(i,1);
							glp.rowSpec = GridLayout.spec(v.getStartTime()+1, v.getTimes(),
									GridLayout.FILL);
							glp.columnSpec = GridLayout.spec(v.getStartWeek() + 1, 1);
							glp.width = (int) w;
							glp.height = (int) h;
							// v.setLayoutParams(glp);
							View vv = new View(context);
							Log.i("ss",""+v.getColor());
							vv.setBackgroundColor(Color.rgb(v.getColor()/0x10000, v.getColor()/0x100-v.getColor()/0x10000*0x100, v.getColor()%0x100));
							gl.addView(vv, glp);
							gl.addView(v.getView(null), glp);
						}
					}
				});

			}
		}).start();
		


	}

}
