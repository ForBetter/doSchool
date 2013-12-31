package us.dobell.doschool.tools;

import org.json.JSONObject;

import us.dobell.doschool.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class ActivityGongGaoDetail extends Activity {
	Handler handler;
	String s;
	TextView t1, t2, t3;
	JSONObject o;

	public void back(View view) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		handler = new Handler();
		setContentView(R.layout.tools_activity_gongaoetail);
		Intent it = getIntent();
		t1 = (TextView) findViewById(R.id.tools_activity_gongaoetail_text1);
		t2 = (TextView) findViewById(R.id.tools_activity_gongaoetail_text2);
		t3 = (TextView) findViewById(R.id.tools_activity_gongaoetail_text3);

		t1.setText(it.getStringExtra("name"));
		t2.setText(it.getStringExtra("text"));
		t3.setText(it.getStringExtra("time"));

	}

}
