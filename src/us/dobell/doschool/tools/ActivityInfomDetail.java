package us.dobell.doschool.tools;

import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.doschool.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class ActivityInfomDetail extends Activity {
	Handler handler;
	String s;
	TextView text;
	JSONObject o;

	public void back(View view) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		handler = new Handler();
		setContentView(R.layout.tools_activity_informdetail);
		text = (TextView) findViewById(R.id.tools_activity_informdetail_text);
		new Thread() {
			public void run() {
				s = MRemoteServer.post("tools/xsjz_2", "link="
						+ getIntent().getStringExtra("link"));

				if (s == null) {
					finish();
					return;
				}

				try {
					o = new JSONObject(s);
					if (!o.getString("msg").equals("SUCCESS")) {
						finish();
						return;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					finish();
					return;
				}

				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							text.setText(o.getString("article"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							finish();
							return;
						}
					}
				});
			}
		}.start();
	}

}
