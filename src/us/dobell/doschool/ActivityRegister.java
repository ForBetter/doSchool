package us.dobell.doschool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.doschool.base.Values;
import us.dobell.doschool.user.UserServer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ActivityRegister extends Activity {
	public static final String TAG = "ActivityRegister";

	private boolean hasHead;
	private String funId, funPasswd, usrName, usrNick, usrPasswd;

	private EditText funIdEdtxt, funPasswdEditText, nickEdtxt, usrPasswdEdtxt,
			usrPasswdRecheckEdtxt;
	private Button registerBtn, backBtn;
	private ImageView headImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		headImg = (ImageView) findViewById(R.id.activityRegisterHeadImg);
		funIdEdtxt = (EditText) findViewById(R.id.activityRegisterFunIdEdtxt);
		funPasswdEditText = (EditText) findViewById(R.id.activityRegisterFunPasswdEdtxt);
		nickEdtxt = (EditText) findViewById(R.id.activityRegisterNickEdtxt);
		usrPasswdEdtxt = (EditText) findViewById(R.id.activityRegisterUsrPasswdEdtxt);
		usrPasswdRecheckEdtxt = (EditText) findViewById(R.id.activityRegisterUsrPasswdRecheckEdtxt);
		registerBtn = (Button) findViewById(R.id.activityRegisterRegisterBtn);
		backBtn = (Button) findViewById(R.id.activityRegisterBackBtn);

		ClickListener clickListener = new ClickListener();
		registerBtn.setOnClickListener(clickListener);
		backBtn.setOnClickListener(clickListener);
		headImg.setOnClickListener(clickListener);
	}

	@Override
	protected void onActivityResult(int id, int resultCode, Intent data) {
		super.onActivityResult(id, resultCode, data);
		if (data == null) {
			return;
		}
		File uploadDir = new File(Environment.getExternalStorageDirectory()
				+ Values.UPLOAD_FOLDER);
		uploadDir.mkdirs();
		File outFile = new File(Environment.getExternalStorageDirectory()
				+ Values.UPLOAD_FOLDER, "head");
		Log.d(TAG, Environment.getExternalStorageDirectory()
				+ Values.UPLOAD_FOLDER + "/head");
		if (id == 0) {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(data.getData(), "image/*");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 300);
			intent.putExtra("outputY", 300);
			intent.putExtra("output", Uri.fromFile(outFile));
			startActivityForResult(intent, 1);
			return;
		}
		InputStream is = null;
		Bitmap bmp = null;
		try {
			is = new FileInputStream(outFile);
			bmp = BitmapFactory.decodeStream(is);
		} catch (FileNotFoundException e) {
			Toast.makeText(this, "未找到图片", Toast.LENGTH_SHORT).show();
			bmp = null;
		} catch (OutOfMemoryError e) {
			Toast.makeText(this, "你上传的图片过大", Toast.LENGTH_SHORT).show();
			bmp = null;
		}
		if (bmp == null) {
			return;
		}
		hasHead = true;
		headImg.setImageBitmap(bmp);
	}

	class ClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activityRegisterRegisterBtn:
				funId = funIdEdtxt.getText().toString();
				funPasswd = funPasswdEditText.getText().toString();
				usrNick = nickEdtxt.getText().toString();
				if (usrNick.length() < 2 || usrNick.length() > 10) {
					Toast.makeText(ActivityRegister.this, "昵称应在2～10个字",
							Toast.LENGTH_SHORT).show();
					return;
				}
				usrPasswd = usrPasswdEdtxt.getText().toString();
				if (usrPasswd.length() < 3) {
					Toast.makeText(ActivityRegister.this, "您的密码太短",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (!usrPasswd.equals(usrPasswdRecheckEdtxt.getText()
						.toString())) {
					Toast.makeText(ActivityRegister.this, "两次密码不一致",
							Toast.LENGTH_SHORT).show();
					return;
				}
				new RegisterTask().execute();
				break;
			case R.id.activityRegisterHeadImg:
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					Toast.makeText(ActivityRegister.this, "储存卡不可用",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					Intent intent = new Intent(Intent.ACTION_PICK, null);
					intent.setDataAndType(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							"image/*");
					startActivityForResult(intent, 0);
				}
				break;
			default:
				finish();
				break;
			}
		}
	}

	class RegisterTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			registerBtn.setEnabled(false);
		}

		@Override
		protected Integer doInBackground(Void... arg0) {
			JSONObject jObj = null;
			try {
				jObj = UserServer.userCheck(funId, funPasswd);
				if (jObj.getInt("code") != 0x00000010) {
					return jObj.getInt("code");
				}
				usrName = jObj.getString("data");
				String headName = "";
				if (hasHead) {
					jObj = UserServer.userUploadHead(Environment
							.getExternalStorageDirectory()
							+ Values.UPLOAD_FOLDER + "/head");
					if (jObj.getInt("code") == 0x00000000) {
						headName = jObj.getString("data");
					}
				}
				jObj = UserServer.userRegister(funId, usrName, usrNick,
						usrPasswd, headName);
				if (jObj.getInt("code") == 0x00002010) {
					SharedPreferences sp = getSharedPreferences("autoLogin",
							MODE_PRIVATE);
					sp.edit().putString("funId", funId).commit();
					sp.edit().putString("usrPasswd", usrPasswd).commit();
				}
				return jObj.getInt("code");
			} catch (JSONException e) {
				e.printStackTrace();
				return -1;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			registerBtn.setEnabled(true);
			switch (result) {
			case 0x00001000:
				Toast.makeText(ActivityRegister.this, "注册成功，" + usrName + "同学",
						Toast.LENGTH_SHORT).show();
				us.dobell.doschool.base.Values.User.saveAutoLoginInfo(
						ActivityRegister.this, funId, usrPasswd);
				Intent intent = new Intent();
				intent.setClass(ActivityRegister.this, ActivityLogin.class);
				startActivity(intent);
				finish();
				break;
			case 0x00000011:
				Toast.makeText(ActivityRegister.this, "该用户不是安徽大学学生",
						Toast.LENGTH_SHORT).show();
				break;
			case 0x00000012:
			case 0x00001001:
				Toast.makeText(ActivityRegister.this, "该用户已经注册过",
						Toast.LENGTH_SHORT).show();
				break;
			case 0x0000100f:
			case 0x0000000f:
			case 0x0000001f:
				Toast.makeText(ActivityRegister.this, "服务器异常",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(ActivityRegister.this, "网络连接失败",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}
}
