package us.dobell.doschool.message;

import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.doschool.base.Values;
import us.dobell.xtools.XService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.baidu.android.pushservice.PushConstants;

public class MessageReceiver extends BroadcastReceiver {
	static final String TAG = "MessageReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "获取到新的推送消息");
		Log.d(TAG, "消息:" + intent);
		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
			try {
				JSONObject jObj = new JSONObject(intent.getExtras().getString(
						PushConstants.EXTRA_PUSH_MESSAGE_STRING));
				Log.d(TAG, "消息为" + jObj.toString());
				String messageId = jObj.getString("_mid");
				int userId = Integer.valueOf(jObj.getString("_uid"));
				// TODO 这里还需要根据message的type来做，根据type，目前有三个
				// 但是我觉得后续还可能更多，所以应该加入switch来做选择
				if (userId == Values.User.me.id) {
					new PushSuccessTask().execute(messageId);
					Intent mIntent = new Intent(context, XService.class);
					mIntent.putExtra("message", jObj.getJSONObject("_content")
							.toString());
					context.startService(mIntent);
					// XNotification.addNotification(context, mIntent);
					Log.v(TAG, "已经添加");
				} else {
					// TODO 这个我不好测试，但是根据Receiver的规则，好像
					// 数据操作有时间限制，所以这个地方可能需要将消息发送给某个后台，以此来返回数据
					// 另，在sdk中，我有看到向推送服务器发送消息，所以，看这个地方是否可以直接用这个方法
					// 目测用这个方法，cly需要将接口位置调整到推送服务器下，而不是在bae下
					Log.d("test", "消息不匹配，请求网络操作，告知服务器");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {
			// 处理 bind、setTags 等方法口的返回数据
			final String method = intent
					.getStringExtra(PushConstants.EXTRA_METHOD);
			final int errorCode = intent
					.getIntExtra(PushConstants.EXTRA_ERROR_CODE,
							PushConstants.ERROR_SUCCESS);
			final String content = new String(
					intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));
			Log.i(TAG, "onMessage: method: " + method);
			Log.i(TAG, "onMessage: result : " + errorCode);
			Log.i(TAG, "onMessage: content : " + content);
			try {
				Values.Message.devId = new JSONObject(content).getJSONObject(
						"response_params").getString("user_id");
				new MessageLoginTask().execute();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (intent.getAction().equals(
				PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {
		}
		Log.d(TAG, "onReceive-->End");
	}

	class MessageLoginTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			Log.d(TAG, "更新设备码" + Values.Message.devId);
			MessageServer.messageLogin(Values.User.me.id, Values.Message.devId);
			Log.d(TAG, "更新设备码成功");
			return null;
		}
	}

	class PushSuccessTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			MessageServer.messageSuccess(params[0]);
			return null;
		}

	}
}
