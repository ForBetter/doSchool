package us.dobell.doschool.microblog;

import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.doschool.ActivityMain;
import us.dobell.doschool.R;
import us.dobell.doschool.base.Values;
import us.dobell.xtools.XEditText;
import us.dobell.xtools.XPage;
import us.dobell.xtools.XString;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class PageComment extends XPage {
	private XEditText xEditText;

	private int objId;
	private int rootMblogId;
	private int rootCommentId;

	public PageComment(Context context) {
		super(context);
	}

	@Override
	public void onCreate() {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.page_comment, null);
		contentLayout.addView(view);
		titleRightBtn.setOnClickListener(new BtnListener());
		titleRightBtn.setImageResource(R.drawable.ic_btn_send);
		xEditText = (XEditText) contentLayout
				.findViewById(R.id.pageCommentContentEdtxt);
		try {
			JSONObject jObj = getInfo();
			this.objId = jObj.getInt("objId");
			this.rootCommentId = jObj.getInt("rootCommentId");
			this.rootMblogId = jObj.getInt("rootMblogId");
			xEditText.setXString(new XString(jObj.getJSONObject("content")));
		} catch (Exception e) {
		}
		if (rootCommentId != 0) {
			((TextView) contentLayout.findViewById(R.id.pageCommentTipTxt))
					.setText("回复评论");
		} else {
			((TextView) contentLayout.findViewById(R.id.pageCommentTipTxt))
					.setText("回复微博");
		}
	}

	class BtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			new SendCommentTask().execute();
		}
	}

	class SendCommentTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			try {
				MicroblogServer.microblogCommentSend(Values.User.me.id, objId,
						rootMblogId, rootCommentId, xEditText.getXString()
								.toJSONObject());
				return 1;
			} catch (JSONException e) {
				return 0;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == 0) {
				return;
			}
			if (ActivityMain.getXPageCount() > 1) {
				ActivityMain.delXPage();
			} else {
				ActivityMain.setFirstXPage(new PagePlaza(getContext()));
			}
		}
	}
}
