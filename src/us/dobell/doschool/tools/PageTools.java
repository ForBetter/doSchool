package us.dobell.doschool.tools;

import us.dobell.doschool.R;
import us.dobell.xtools.XMenu;
import us.dobell.xtools.XPage;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PageTools extends XPage {
	ImageView bt1, bt2, bt3, bt4, bt5, bt6;
	TextView t1, t2, t3;
	Context context;
	View pageTools;
	XMenu xMenu;
	Handler handler;

	public PageTools(Context context) {
		// TODO Auto-generated constructor stub
		super(context);

		this.context = getContext();
	}

	public void onCreate() {
		context = getContext();
		TextView titleNameTxt = new TextView(getContext());
		titleNameTxt.setGravity(Gravity.CENTER);
		titleNameTxt.setTextSize(24);
		titleNameTxt.setText("小工具");
		titleMidLayout.addView(titleNameTxt,
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		this.pageTools = LayoutInflater.from(context).inflate(
				R.layout.page_tools, null);

		Log.i("aaa", "aaaaa1" + pageTools);
		contentLayout.addView(pageTools,
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		bt1 = (ImageView) (View) pageTools.findViewById(R.id.page_tools_bt1)
				.findViewById(R.id.tools_ic_image);
		bt1.setBackgroundResource(R.drawable.tools_pageic_kcb);

		bt2 = (ImageView) (View) pageTools.findViewById(R.id.page_tools_bt2)
				.findViewById(R.id.tools_ic_image);
		bt2.setBackgroundResource(R.drawable.tools_pageic_tsg);

		bt3 = (ImageView) (View) pageTools.findViewById(R.id.page_tools_bt3)
				.findViewById(R.id.tools_ic_image);
		bt3.setBackgroundResource(R.drawable.tools_pageic_jz);

		bt4 = (ImageView) (View) pageTools.findViewById(R.id.page_tools_bt4)
				.findViewById(R.id.tools_ic_image);
		bt4.setBackgroundResource(R.drawable.tools_pageic_wm);

		bt5 = (ImageView) (View) pageTools.findViewById(R.id.page_tools_bt5)
				.findViewById(R.id.tools_ic_image);
		bt5.setBackgroundResource(R.drawable.tools_ic_tongzhi);
		bt5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(context, ActivityGongGao.class);
				context.startActivity(it);
			}
		});

		bt4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(context, ActivityWaiMai.class);
				context.startActivity(it);
			}
		});

		t1 = (TextView) (View) pageTools.findViewById(R.id.page_tools_bt1)
				.findViewById(R.id.tools_ic_text);
		t2 = (TextView) (View) pageTools.findViewById(R.id.page_tools_bt2)
				.findViewById(R.id.tools_ic_text);
		t3 = (TextView) (View) pageTools.findViewById(R.id.page_tools_bt3)
				.findViewById(R.id.tools_ic_text);
		handler = new Handler();
		// User.setMyFuncPasswd(context, "624955391");

		bt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(context, ActivityKCB.class);
				context.startActivity(it);
			}
		}

		);

		bt2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(context, ActivityLib.class);
				context.startActivity(it);
			}
		});

		bt3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(context, ActivityInform.class);
				context.startActivity(it);
			}
		});

	}

}
