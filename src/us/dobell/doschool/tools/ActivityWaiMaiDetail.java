package us.dobell.doschool.tools;

import us.dobell.doschool.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class ActivityWaiMaiDetail extends Activity {
	TextView t1, t2, t3, t4;
	TextView title;
	ImageButton imbt;

	public void back(View view) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_activity_waimaidetail);
		Intent it = getIntent();
		title = (TextView) findViewById(R.id.tools_top_text);
		t2 = (TextView) findViewById(R.id.tools_activity_waimai_text2);
		t4 = (TextView) findViewById(R.id.tools_activity_waimai_text4);
		imbt = (ImageButton) findViewById(R.id.tools_activity_waimai_callbt);
		title.setText(it.getStringExtra("name"));
		t2.setText(it.getStringExtra("phone"));
		t4.setText(it.getStringExtra("data"));
		final String tel = it.getStringExtra("phone");
		imbt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri telUri = Uri.parse("tel:" + tel);
				Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
				startActivity(intent);

			}
		});

	}

}
