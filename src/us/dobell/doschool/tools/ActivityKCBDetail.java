package us.dobell.doschool.tools;

import us.dobell.doschool.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ActivityKCBDetail extends Activity {
	private ImageButton leftBt, rightBt;
	private TextView head, name, address, time, teacher;

	/**
	 * 下面的便签,需要么= =
	 */
	public void back(View view) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_activity_kcb_detail);

		leftBt = (ImageButton) findViewById(R.id.tools_top_leftbutton);
		rightBt = (ImageButton) findViewById(R.id.tools_top_rightbutton);
		head = (TextView) findViewById(R.id.tools_top_text);
		address = (TextView) findViewById(R.id.tools_kcb_detail_address);
		time = (TextView) findViewById(R.id.tools_kcb_detail_time);
		teacher = (TextView) findViewById(R.id.tools_kcb_detail_teachername);
		Intent it = this.getIntent();

		head.setText(it.getStringExtra("name"));
		address.setText("地点：" + it.getStringExtra("address"));
		teacher.setText("教师：" + it.getStringExtra("teacherName"));
		time.setText("第"
				+ it.getIntExtra("fromWeek", -1)
				+ "~"
				+ it.getIntExtra("toWeek", -1)
				+ "周，第"
				+ it.getIntExtra("startTime", -1)
				+ "~"
				+ (it.getIntExtra("startTime", -1) + it
						.getIntExtra("times", -1)) + "节");
	}

}
