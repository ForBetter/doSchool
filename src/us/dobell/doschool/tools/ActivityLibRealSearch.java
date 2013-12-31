package us.dobell.doschool.tools;

import us.dobell.doschool.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

/**
 * 暂时被抛弃的一个类...因为蛋疼的手机版图书馆的bug。。
 * 
 * @author Ma Ganxuan
 * 
 */

public class ActivityLibRealSearch extends Activity {
	private EditText edit;
	private Button bt;
	private RadioGroup rg;
	private String type = "title";
	boolean hasMeasured = false;
	RelativeLayout ll;

	public void back(View view) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tools_activity_lib_real_search);
		edit = (EditText) findViewById(R.id.tools_activity_real_search_edit);
		bt = (Button) findViewById(R.id.tools_activity_real_search_bt);
		rg = (RadioGroup) findViewById(R.id.tools_activity_real_search_radiogroup);
		ll = (RelativeLayout) findViewById(R.id.tools_activity_real_search_layout);

		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == group.getChildAt(0).getId()) {
					type = "title";
				} else if (checkedId == group.getChildAt(1).getId()) {
					type = "author";
				} else if (checkedId == group.getChildAt(2).getId()) {
					type = "keyword";
				}
				Log.i("real", type);

			}
		});
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(ActivityLibRealSearch.this,
						ActivityLibSearch.class);
				it.putExtra("type", type);
				it.putExtra("word", edit.getText().toString().trim());
				ActivityLibRealSearch.this.startActivity(it);
			}
		});

		edit.setFocusable(true);

	}

}
