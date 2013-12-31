package us.dobell.doschool.tools;

import us.dobell.doschool.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ActivityLib extends Activity {
	private Button search;
	private ImageButton login, left;
	private EditText edit, pw;
	private TextView head;
	Dialog alertDialog;

	public void back(View view) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_activity_lib);
		login = (ImageButton) findViewById(R.id.tools_activity_lib_bt);
		left = (ImageButton) findViewById(R.id.tools_top_leftbutton);
		search = (Button) findViewById(R.id.tools_activity_lib_search);
		edit = (EditText) findViewById(R.id.tools_activity_lib_editText);
		pw = new EditText(this);
		head = (TextView) findViewById(R.id.tools_top_text);
		head.setText("图书馆");
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(ActivityLib.this,
						ActivityLibSearch.class);
				it.putExtra("word", edit.getText().toString().trim());
				// 不管设置成什么都不会影响搜索结果= = bug
				it.putExtra("type", "title");
				ActivityLib.this.startActivity(it);
			}
		});

		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (alertDialog != null)
					alertDialog.show();
				else {
					alertDialog = new AlertDialog.Builder(ActivityLib.this)
							.setTitle("请输入你的图书馆登陆密码")
							.setView(pw)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub

											Intent it = new Intent(
													ActivityLib.this,
													ActivityLibMyBook.class);
											it.putExtra("password", pw
													.getText().toString()
													.trim());
											ActivityLib.this.startActivity(it);

										}
									}).setNegativeButton("取消", null).create();
					alertDialog.show();
				}
			}
		});

	}

}
