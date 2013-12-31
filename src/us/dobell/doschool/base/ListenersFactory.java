package us.dobell.doschool.base;

import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.doschool.ActivityMain;
import us.dobell.doschool.R;
import us.dobell.doschool.microblog.At;
import us.dobell.doschool.microblog.Microblog;
import us.dobell.doschool.microblog.PageComment;
import us.dobell.doschool.microblog.PagePerson;
import us.dobell.doschool.microblog.PageWrite;
import us.dobell.doschool.user.Apply;
import us.dobell.doschool.user.PageCard;
import us.dobell.doschool.user.User;
import us.dobell.xtools.XPage;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;

public class ListenersFactory {

	public static class BaseFactory {
		public static OnClickListener getHeadMenuListener() {
			return new OnClickListener() {

				@Override
				public void onClick(View v) {
					JSONObject jObj = new JSONObject();
					try {
						jObj.put("id", Values.User.me.id);
						XPage xPage = new PageCard(v.getContext());
						xPage.setInfo(jObj);
						ActivityMain.addXPage(xPage);
					} catch (JSONException e) {
					}
				}
			};
		}
	}

	public static class MicroblogFactory {

		public static OnClickListener getApplyListener(final Apply apply) {
			return new OnClickListener() {

				@Override
				public void onClick(View v) {
					JSONObject jObj = new JSONObject();
					XPage xPage = null;
					try {
						switch (v.getId()) {
						case R.id.itemApplyUsrHeadXimg:
						case R.id.itemApplyUsrNickTxt:
							jObj.put("id", apply.usrId);
							xPage = new PagePerson(v.getContext());
							xPage.setInfo(jObj);
							ActivityMain.addXPage(xPage);
							break;
						case R.id.itemApplyOperatePositiveBtn:
							break;
						case R.id.itemApplyOperateNegativeBtn:
							break;
						default:
							break;
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			};
		}

		public static OnClickListener getAtListener(final At at) {
			return new OnClickListener() {

				@Override
				public void onClick(View v) {
					JSONObject jObj = new JSONObject();
					XPage xPage = null;
					try {
						switch (v.getId()) {
						case R.id.itemAtUserNickTxt:
						case R.id.itemAtUserHeadXImg:
							jObj.put("id", at.usrId);
							xPage = new PagePerson(v.getContext());
							xPage.setInfo(jObj);
							ActivityMain.addXPage(xPage);
							break;
						default:
							break;
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			};
		}

		public static OnClickListener getPersonListener(final User user) {
			return new OnClickListener() {

				@Override
				public void onClick(View v) {
					JSONObject jObj = new JSONObject();
					XPage xPage = null;
					try {
						switch (v.getId()) {
						case R.id.viewPersonCardBtn:
							if (user.card == 2) {
								jObj.put("id", user.id);
								xPage = new PageCard(v.getContext());
								xPage.setInfo(jObj);
								ActivityMain.addXPage(xPage);
							} else if (user.card == 0) {

							}
							break;
						default:
							break;
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			};
		}

		public static OnClickListener getMicroblogListener(final Microblog mblog) {
			return new OnClickListener() {

				@Override
				public void onClick(View v) {
					JSONObject jObj = new JSONObject();
					XPage xPage = null;
					try {
						switch (v.getId()) {
						case R.id.viewNormalMicroblogUserHeadXImg:
						case R.id.viewNormalMicroblogUserNickTxt:
							jObj.put("id", mblog.usrId);
							xPage = new PagePerson(v.getContext());
							xPage.setInfo(jObj);
							break;
						case R.id.viewNormalMicroblogCommentBtn:
							jObj.put("objId", mblog.usrId);
							jObj.put("rootCommentId", 0);
							jObj.put("rootMblogId", mblog.id);
							xPage = new PageComment(v.getContext());
							xPage.setInfo(jObj);
							break;
						case R.id.viewNormalMicroblogTransmitBtn:
							jObj.put("tranMblogId", mblog.root == 0 ? 0
									: mblog.id);
							jObj.put("rootMblogId", mblog.root == 0 ? mblog.id
									: mblog.root);
							xPage = new PageWrite(v.getContext());
							xPage.setInfo(jObj);
						default:
							break;
						}
						ActivityMain.addXPage(xPage);
					} catch (Exception e) {
					}
				}
			};
		}
	}
}

class HandleFriendTask extends AsyncTask<Void, Void, Void> {

	@Override
	protected Void doInBackground(Void... params) {
		return null;
	}

}