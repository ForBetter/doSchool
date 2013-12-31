package us.dobell.doschool.base;

import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.doschool.ActivityMain;
import us.dobell.doschool.R;
import us.dobell.doschool.microblog.Microblog;
import us.dobell.doschool.microblog.MicroblogServer;
import us.dobell.doschool.user.PageCard;
import us.dobell.doschool.user.User;
import us.dobell.doschool.user.UserServer;
import us.dobell.xtools.XImageView;
import us.dobell.xtools.XPage;
import us.dobell.xtools.XUtils;
import android.os.AsyncTask;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewsFactoryLazy {

	public static class MicroblogFactory {

		public static void getPersonView(View emptyView, int userId) {
			new GetPersonTask(emptyView, userId).execute();
		}

		public static void getMicroblogView(View emptyView, int mblogId) {
			new GetMicroblogViewTask(emptyView, mblogId).execute();
		}

		public static void getRootMicroblogView(View emptyView, int rootMblogId) {
			new GetRootMicroblogTask(emptyView, rootMblogId).execute();
		}
	}

}

class GetPersonTask extends AsyncTask<Void, Void, Integer> {
	public static final String TAG = "GetPersonTask";
	private View emptyView;
	private int userId;
	private User user;

	public GetPersonTask(View emptyView, int userId) {
		this.emptyView = emptyView;
		this.userId = userId;
	}

	@Override
	protected Integer doInBackground(Void... params) {
		JSONObject jObj = UserServer.userGet(Values.User.me.id, userId);
		try {
			user = new User(jObj.getJSONObject("data"));
		} catch (Exception e) {
		}
		return jObj.optInt("code");
	}

	@Override
	protected void onPostExecute(Integer result) {
		try {
			((XImageView) emptyView.findViewById(R.id.viewPersonHeadXimg))
					.setImageURL(user.head);
			((TextView) emptyView.findViewById(R.id.viewPersonNickTxt))
					.setText(user.nick);
			final Button cardBtn = (Button) emptyView
					.findViewById(R.id.viewPersonCardBtn);
			if (user.card == 4) {
				((LinearLayout.LayoutParams) cardBtn.getLayoutParams()).weight = 2;
				cardBtn.setText("我的名片");
				cardBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							JSONObject jObj = new JSONObject();
							jObj.put("id", user.id);
							XPage xPage = new PageCard(v.getContext());
							xPage.setInfo(jObj);
							ActivityMain.addXPage(xPage);
						} catch (Exception e) {
						}
					}
				});
			} else {
				cardBtn.setText(user.card == 0 ? "发送名片" : "名片已发");
				if (user.card == 1) {
					cardBtn.setEnabled(false);
				} else {
					cardBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							new SendCardTask(cardBtn, userId).execute();
						}
					});
				}
			}
		} catch (Exception e) {
			Log.d(TAG, "用户微博主页获取失败");
		}
	}
}

class SendCardTask extends AsyncTask<Void, Void, Integer> {
	Button btn;
	int objId;

	public SendCardTask(Button btn, int objId) {
		this.btn = btn;
		this.objId = objId;
	}

	@Override
	protected void onPreExecute() {
		btn.setText("正在发送");
		btn.setEnabled(false);
	}

	@Override
	protected Integer doInBackground(Void... params) {
		JSONObject jObj = UserServer.userCardSend(Values.User.me.id, objId);
		return jObj.optInt("code");
	}

	@Override
	protected void onPostExecute(Integer result) {
		if (result == 0x00002000) {
			btn.setText("名片已发");
		} else {
			btn.setText("发送名片");
			btn.setEnabled(true);
			Toast.makeText(btn.getContext(), "名片发送失败", Toast.LENGTH_SHORT)
					.show();
		}
	}
}

class GetMicroblogViewTask extends AsyncTask<Void, Void, Void> {
	public static final String TAG = "GetMicroblogTask";
	View emptyView;
	int mblogId;
	Microblog mblog;

	public GetMicroblogViewTask(View emptyView, int mblogId) {
		this.emptyView = emptyView;
		this.mblogId = mblogId;
	}

	@Override
	protected Void doInBackground(Void... params) {
		JSONObject jObj = MicroblogServer.microblogGet(Values.User.me.id,
				mblogId);
		try {
			mblog = new Microblog(jObj.getJSONObject("data"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		try {
			LinearLayout imagesLayout = (LinearLayout) emptyView
					.findViewById(R.id.viewNormalMicroblogImagesLayout);
			((ViewGroup) emptyView)
					.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			OnClickListener listener = ListenersFactory.MicroblogFactory
					.getMicroblogListener(mblog);
			if (mblog.images != null && mblog.images.size() != 0) {
				for (int i = 0; i < mblog.images.size(); i++) {
					XImageView ximg = new XImageView(emptyView.getContext());
					LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					l.weight = 1;
					ximg.setImageURL(mblog.images.get(i));
					ximg.setScaleType(ScaleType.FIT_CENTER);
					ximg.setAdjustViewBounds(true);
					ximg.setLayoutParams(l);
					imagesLayout.addView(ximg);
				}
			} else {
				imagesLayout.setVisibility(View.GONE);
			}
			XImageView userHeadXImg = (XImageView) emptyView
					.findViewById(R.id.viewNormalMicroblogUserHeadXImg);
			userHeadXImg.setOnClickListener(listener);
			userHeadXImg.setImageURL(mblog.usrHead);
			TextView userNickTxt = ((TextView) emptyView
					.findViewById(R.id.viewNormalMicroblogUserNickTxt));
			userNickTxt.setOnClickListener(listener);
			userNickTxt.setText(mblog.usrNick);
			TextView timeTxt = (TextView) emptyView
					.findViewById(R.id.viewNormalMicroblogTimeTxt);
			timeTxt.setText(XUtils.getTime(mblog.time));
			TextView contentTxt = ((TextView) emptyView
					.findViewById(R.id.viewNormalMicroblogContentTxt));
			contentTxt.setMovementMethod(LinkMovementMethod.getInstance());
			contentTxt.setText(mblog.content.toSpannableString());
			Button commentBtn = (Button) emptyView
					.findViewById(R.id.viewNormalMicroblogCommentBtn);
			commentBtn.setOnClickListener(listener);
			Button transmitBtn = (Button) emptyView
					.findViewById(R.id.viewNormalMicroblogTransmitBtn);
			transmitBtn.setOnClickListener(listener);
			commentBtn.setText("评论(" + mblog.comment + ")");
			transmitBtn.setText("转发(" + mblog.transmit + ")");
			LinearLayout rootLayout = (LinearLayout) emptyView
					.findViewById(R.id.viewNormalMicroblogRootLayout);
			if (mblog.root != 0) {
				View rootMblogView = LayoutInflater
						.from(emptyView.getContext()).inflate(
								R.layout.view_microblog_root, null);
				rootLayout.addView(rootMblogView);
				ViewsFactoryLazy.MicroblogFactory.getRootMicroblogView(
						rootMblogView, mblog.root);
			} else {
				rootLayout.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			Log.d(TAG, "微博视图加载失败");
		}
	}
}

class GetRootMicroblogTask extends AsyncTask<Void, Void, Void> {
	public static final String TAG = "GetRootMicroblogTask";
	private View emptyView;
	private int rootMblogId;
	private Microblog rootMblog;

	public GetRootMicroblogTask(View emptyView, int rootMblogId) {
		this.emptyView = emptyView;
		this.rootMblogId = rootMblogId;
	}

	@Override
	protected Void doInBackground(Void... params) {
		JSONObject jObj = MicroblogServer.microblogGet(Values.User.me.id,
				rootMblogId);
		try {
			rootMblog = new Microblog(jObj.getJSONObject("data"));
		} catch (Exception e1) {
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		try {
			((ViewGroup) emptyView)
					.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			OnClickListener listener = ListenersFactory.MicroblogFactory
					.getMicroblogListener(rootMblog);
			TextView userNickTxt = (TextView) emptyView
					.findViewById(R.id.viewRootMicroblogUserNickTxt);
			userNickTxt.setOnClickListener(listener);
			userNickTxt.setText(rootMblog.usrNick);
			((TextView) emptyView.findViewById(R.id.viewRootMicroblogTimeTxt))
					.setText(XUtils.getTime(rootMblog.time));
			TextView rootContentTxt = (TextView) emptyView
					.findViewById(R.id.viewRootMicroblogContentTxt);
			rootContentTxt.setMovementMethod(LinkMovementMethod.getInstance());
			rootContentTxt.setText(rootMblog.content.toSpannableString());
			LinearLayout rootImagesLayout = (LinearLayout) emptyView
					.findViewById(R.id.viewRootMicroblogImagesLayout);
			if (rootMblog.images != null && rootMblog.images.size() != 0) {
				rootImagesLayout.setWeightSum(3);
				for (int i = 0; i < rootMblog.images.size(); i++) {
					XImageView ximg = new XImageView(emptyView.getContext());
					LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					l.weight = 1;
					ximg.setImageURL(rootMblog.images.get(i));
					ximg.setScaleType(ScaleType.FIT_CENTER);
					ximg.setAdjustViewBounds(true);
					ximg.setLayoutParams(l);
					rootImagesLayout.addView(ximg);
				}
			} else {
				rootImagesLayout.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			Log.d(TAG, "根微博加载失败");
		}
	}
}