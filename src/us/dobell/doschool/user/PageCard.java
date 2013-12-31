package us.dobell.doschool.user;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.doschool.R;
import us.dobell.doschool.base.Values;
import us.dobell.xtools.XImageView;
import us.dobell.xtools.XPage;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PageCard extends XPage {
	public static final String TAG = "PageCard";
	public static final int CODE_CARD_HEAD_PICK = 0x00002000;
	public static final int CODE_CARD_HEAD_CUT = 0x00002001;

	private boolean isEdit;
	public boolean isHeadChanged;
	private int userId;
	private Card card;

	public XImageView headXimg;
	private TextView nameTxt, nickTxt, phoneTxt, qqTxt, mailTxt, introduceTxt;
	private EditText nickEdtxt, phoneEdtxt, qqEdtxt, mailEdtxt, introduceEdtxt;

	public PageCard(Context context) {
		super(context);
	}

	@Override
	public void onCreate() {
		TextView titleTxt = new TextView(getContext());
		titleTxt.setTextSize(24);
		titleTxt.setText("名片");
		titleRightBtn.setOnClickListener(new RightBtnListener());
		titleRightBtn.setImageResource(R.drawable.ic_btn_send);
		titleMidLayout.addView(titleTxt,
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		try {
			JSONObject jObj = getInfo();
			userId = jObj.getInt("id");
			card = UserDatabase.userCardGet(jObj.getInt("id"));
			initDisplay();
		} catch (Exception e) {
			new RefreshTask().execute();
		}
	}

	@Override
	public void onResume() {
		new RefreshTask().execute();
	}

	private void initDisplay() {
		isEdit = false;
		View v = LayoutInflater.from(getContext()).inflate(
				R.layout.page_card_show, null);
		headXimg = (XImageView) v.findViewById(R.id.pageShowCardHeadXImg);
		headXimg.setImageURL(card.head);
		nameTxt = (TextView) v.findViewById(R.id.pageShowCardNameTxt);
		nameTxt.setText(card.name);
		nickTxt = (TextView) v.findViewById(R.id.pageShowCardNickTxt);
		nickTxt.setText(card.nick);
		phoneTxt = (TextView) v.findViewById(R.id.pageShowCardPhoneTxt);
		phoneTxt.setText(card.phone);
		qqTxt = (TextView) v.findViewById(R.id.pageShowCardQqTxt);
		qqTxt.setText(card.qq);
		mailTxt = (TextView) v.findViewById(R.id.pageShowCardMailTxt);
		mailTxt.setText(card.mail);
		introduceTxt = (TextView) v.findViewById(R.id.pageShowCardIntroduceTxt);
		introduceTxt.setText(card.introduce);
		contentLayout.removeAllViews();
		contentLayout.addView(v);
	}

	private void initEdit() {
		isEdit = true;
		View v = LayoutInflater.from(getContext()).inflate(
				R.layout.page_card_edit, null);
		headXimg = (XImageView) v.findViewById(R.id.pageEditCardHeadXImg);
		headXimg.setOnClickListener(new HeadListener());
		headXimg.setImageURL(card.head);
		nameTxt = (TextView) v.findViewById(R.id.pageEditCardNameTxt);
		nameTxt.setText(card.name);
		nickEdtxt = (EditText) v.findViewById(R.id.pageCardEditNickEdtxt);
		nickEdtxt.setText(card.nick);
		phoneEdtxt = (EditText) v.findViewById(R.id.pageCardEditPhoneEdtxt);
		phoneEdtxt.setText(card.phone);
		qqEdtxt = (EditText) v.findViewById(R.id.pageCardEditQqEdtxt);
		qqEdtxt.setText(card.qq);
		mailEdtxt = (EditText) v.findViewById(R.id.pageCardEditMailEdtxt);
		mailEdtxt.setText(card.mail);
		introduceEdtxt = (EditText) v
				.findViewById(R.id.pageCardEditIntroduceEdtxt);
		introduceEdtxt.setText(card.introduce);
		contentLayout.removeAllViews();
		contentLayout.addView(v);
	}

	class RightBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (card.id == Values.User.me.id) {
				if (!isEdit) {
					initEdit();
				} else {
					if (nickEdtxt.getText().toString().length() < 2
							|| nickEdtxt.getText().toString().length() > 10) {
						Toast.makeText(getContext(), "昵称长度应在2-10个字",
								Toast.LENGTH_SHORT).show();
						return;
					}
					new UpdateTask().execute();
				}
			}
		}
	}

	class HeadListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (isEdit) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					Toast.makeText(getContext(), "储存卡不可用", Toast.LENGTH_SHORT)
							.show();
					return;
				} else {
					Intent intent = new Intent(Intent.ACTION_PICK, null);
					intent.setDataAndType(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							"image/*");
					((Activity) getContext()).startActivityForResult(intent,
							CODE_CARD_HEAD_PICK);
				}
			}
		}
	}

	class RefreshTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			JSONObject jObj = UserServer.userCardGet(Values.User.me.id, userId);
			try {
				card = new Card(jObj.getJSONObject("data"));
			} catch (JSONException e) {
			}
			return jObj.optInt("code");
		}

		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
			case 0x00002010:
				initDisplay();
				break;
			case 0x00002011:
				Toast.makeText(getContext(), "用户不存在", Toast.LENGTH_SHORT)
						.show();
				break;
			case 0x00002012:
				Toast.makeText(getContext(), "对方用户不存在", Toast.LENGTH_SHORT)
						.show();
				break;
			case 0x00002013:
				Toast.makeText(getContext(), "没有权限查看该用户的名片", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				Toast.makeText(getContext(), "网络连接出错", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	}

	class UpdateTask extends AsyncTask<Void, Void, Integer> {
		Card temp;

		@Override
		protected void onPreExecute() {
			temp = new Card(Values.User.me.id, nickEdtxt.getText().toString(),
					"", card.card, card.friend, card.fId, card.name, phoneEdtxt
							.getText().toString(),
					qqEdtxt.getText().toString(), mailEdtxt.getText()
							.toString(), introduceEdtxt.getText().toString());
		}

		@Override
		protected Integer doInBackground(Void... params) {
			String s = "";
			if (isHeadChanged) {
				File uploadDir = new File(
						Environment.getExternalStorageDirectory()
								+ Values.UPLOAD_FOLDER);
				uploadDir.mkdirs();
				try {
					JSONObject jObj = UserServer.userUploadHead(Environment
							.getExternalStorageDirectory()
							+ Values.UPLOAD_FOLDER + "/head");
					s = jObj.getString("data");
				} catch (Exception e) {
				}
				if (s == null) {
					s = "";
				}
			}
			JSONObject jObj = UserServer.userCardUpdate(temp.id, temp.nick,
					s == "" ? card.head : s, temp.phone, temp.qq, temp.mail,
					temp.introduce);
			if (jObj.optInt("code") == 0x00002030) {
				try {
					card = new Card(jObj.optJSONObject("data"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return jObj.optInt("code");
		}

		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
			case 0x00002030:
				UserDatabase.userCardSet(card);
				initDisplay();
				break;
			case 0x00002031:
				Toast.makeText(getContext(), "用户不存在", Toast.LENGTH_SHORT)
						.show();
				break;
			case 0x00002032:
				Toast.makeText(getContext(), "昵称不合法", Toast.LENGTH_SHORT)
						.show();
				break;
			case 0x0000203f:
				Toast.makeText(getContext(), "服务器异常", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				Toast.makeText(getContext(), "网络连接出错", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	}
}
