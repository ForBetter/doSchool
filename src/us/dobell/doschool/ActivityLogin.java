package us.dobell.doschool;

import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.doschool.base.Values;
import us.dobell.doschool.message.MessageServer;
import us.dobell.doschool.user.User;
import us.dobell.doschool.user.UserServer;
import us.dobell.xtools.XServer;
import us.dobell.xtools.XService;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

public class ActivityLogin extends Activity {
	public static final long STAY_TIME = 2000;

	private String funId, usrPasswd;
	private EditText funIdEdtxt, usrPasswdEdtxt;
	private Button loginBtn, registerBtn;

	private boolean isAutoLogin;
	private long startTime;

	private LinearLayout activityAutoLoginLayout;
	private LoginTask loginTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String info[] = us.dobell.doschool.base.Values.User
				.getAutoLoginInfo(this);
		this.funId = info[0];
		this.usrPasswd = info[1];
		if (funId == null) {
			initLogin("", "");
		} else {
			initAutoLogin();
		}
	}

	private void initLogin(String funId, String usrPasswd) {
		isAutoLogin = false;
		setContentView(R.layout.activity_login_normal);
		funIdEdtxt = (EditText) findViewById(R.id.activityNormalLoginFunIdEdtxt);
		usrPasswdEdtxt = (EditText) findViewById(R.id.activityNormalLoginUsrPasswdEdtxt);
		loginBtn = (Button) findViewById(R.id.activityNormalLoginLoginBtn);
		registerBtn = (Button) findViewById(R.id.activityNormalLoginRegisterBtn);
		XClickListener xClickListener = new XClickListener();
		funIdEdtxt.setText(funId);
		usrPasswdEdtxt.setText(usrPasswd);
		loginBtn.setOnClickListener(xClickListener);
		registerBtn.setOnClickListener(xClickListener);
	}

	private void initAutoLogin() {
		isAutoLogin = true;
		setContentView(R.layout.activity_login_auto);
		activityAutoLoginLayout = (LinearLayout) findViewById(R.id.activityAutoLoginLayout);
		loginTask = new LoginTask();
		loginTask.execute();
	}

	private void loginFailed(int code) {
		String reason = null;
		switch (code) {
		case 0x00001011:
			reason = "用户不存在";
			break;
		case 0x00001012:
			reason = "用户名或密码不正确";
			break;
		case 0x0000101f:
			reason = "服务器异常";
			break;
		default:
			reason = "网络异常";
			break;
		}
		us.dobell.doschool.base.Values.User.clearAutoLoginInfo(this);
		Toast.makeText(ActivityLogin.this, reason, Toast.LENGTH_SHORT).show();
		initLogin(funId, usrPasswd);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (loginTask != null) {
				loginTask.cancel(true);
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	class XClickListener implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			if (arg0 == loginBtn) {
				findViewById(R.id.activityNormalLoginLoginBtn)
						.setEnabled(false);
				funId = funIdEdtxt.getText().toString();
				usrPasswd = usrPasswdEdtxt.getText().toString();
				loginTask = new LoginTask();
				loginTask.execute();
			} else {
				Intent intent = new Intent();
				intent.setClass(ActivityLogin.this, ActivityRegister.class);
				startActivity(intent);
			}
		}
	}

	class LoginTask extends AsyncTask<Void, Integer, JSONObject> {
		public static final int LOGIN = 1;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (loginBtn != null) {
				loginBtn.setEnabled(false);
			}
			startTime = System.currentTimeMillis();
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			JSONObject jObj = UserServer.userLogin(funId, usrPasswd);
			return jObj;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (loginBtn != null) {
				loginBtn.setEnabled(true);
			}
			if (result.optInt("code") == 0x00001010) {
				// 写自动登录文件，下次自动登录
				us.dobell.doschool.base.Values.User.saveAutoLoginInfo(
						ActivityLogin.this, funId, usrPasswd);
				// 将自己的信息存入静态变量中。
				try {
					Values.User.me = new User(result.getJSONObject("data"));
				} catch (JSONException e) {
					loginFailed(XServer.NETWORK_ERROR);
					e.printStackTrace();
					return;
				}
				PushManager.startWork(ActivityLogin.this,
						PushConstants.LOGIN_TYPE_API_KEY,
						MessageServer.MESSAGE_KEY);
				Intent serviceIntent = new Intent(ActivityLogin.this,
						XService.class);
				startService(serviceIntent);
				// 计算在自动登录页面继续停留的时间,并在时间用完之后跳转到主界面
				if (isAutoLogin
						&& System.currentTimeMillis() - startTime < STAY_TIME) {
					activityAutoLoginLayout.postDelayed(new Runnable() {

						@Override
						public void run() {
							Intent intent = new Intent();
							intent.setClass(ActivityLogin.this,
									ActivityMain.class);
							startActivity(intent);
							finish();
						}
					}, STAY_TIME + startTime - System.currentTimeMillis());
				} else {
					Intent intent = new Intent();
					intent.setClass(ActivityLogin.this, ActivityMain.class);
					startActivity(intent);
					finish();
				}
			} else {
				loginFailed(result.optInt("code"));
			}
		}
	}
}
