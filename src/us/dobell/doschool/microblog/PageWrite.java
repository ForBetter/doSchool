package us.dobell.doschool.microblog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import us.dobell.doschool.ActivityMain;
import us.dobell.doschool.R;
import us.dobell.doschool.base.Values;
import us.dobell.xtools.XEditText;
import us.dobell.xtools.XPage;
import us.dobell.xtools.XString;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PageWrite extends XPage {
	public static final int MAX_WIDTH = 800;
	public static final int MAX_HEIGHT = 1600;
	private int tranMblogId;
	private int rootMblogId;

	int imageNum;
	XEditText xEditText;
	LinearLayout imageLayout;
	ImageButton addBtn;
	LinearLayout.LayoutParams params;
	ClickListener clickListener;

	public PageWrite(Context context) {
		super(context);
	}

	@Override
	public void onCreate() {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.page_write, null);
		titleRightBtn.setBackgroundColor(Color.TRANSPARENT);
		titleRightBtn.setImageResource(R.drawable.ic_btn_send);
		contentLayout.addView(view);
		xEditText = (XEditText) contentLayout
				.findViewById(R.id.pageWriteContentEdtxt);
		imageLayout = (LinearLayout) contentLayout
				.findViewById(R.id.pageWriteImageLayout);
		addBtn = new ImageButton(getContext());
		addBtn.setImageResource(R.drawable.image_btn_add);
		addBtn.setBackgroundColor(Color.TRANSPARENT);
		addBtn.setScaleType(ScaleType.FIT_XY);
		params = new LinearLayout.LayoutParams(measureXPageWidth() / 3,
				measureXPageWidth() / 3);
		clickListener = new ClickListener();
		addBtn.setLayoutParams(params);
		addBtn.setOnClickListener(clickListener);
		titleRightBtn.setOnClickListener(clickListener);
		imageLayout.addView(addBtn);
		imageNum = 0;
		try {
			JSONObject jObj = getInfo();
			this.tranMblogId = jObj.optInt("tranMblogId");
			this.rootMblogId = jObj.optInt("rootMblogId");
			xEditText.setXString(new XString(jObj.getJSONObject("content")));
		} catch (Exception e) {
		}
	}

	public int getImageNum() {
		return imageNum;
	}

	public void addImage(Bitmap bmp) {
		double w = bmp.getWidth();
		double h = bmp.getHeight();
		if (w / MAX_WIDTH > 1 || h / MAX_HEIGHT > 1) {
			double scale = Math.max(w / MAX_WIDTH, h / MAX_HEIGHT);
			w = w / scale;
			h = h / scale;
			bmp = Bitmap.createScaledBitmap(bmp, (int) w, (int) h, true);
		}
		ImageView imageView = new ImageView(getContext());
		imageView.setImageBitmap(bmp);
		imageView.setOnClickListener(clickListener);
		imageLayout.addView(imageView, imageNum, new LayoutParams(
				measureXPageWidth() / 3, measureXPageWidth() / 3));
		imageNum++;
		File microblogImage = new File(
				Environment.getExternalStorageDirectory()
						+ Values.UPLOAD_FOLDER, "microblogImage" + imageNum);
		try {
			bmp.compress(CompressFormat.JPEG, 100, new FileOutputStream(
					microblogImage));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v == addBtn) {
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				((Activity) getContext()).startActivityForResult(intent,
						ActivityMain.CODE_MICROBLOG_IMAGE_ADD);
			} else if (v == titleRightBtn) {
				new SendMicroblogTask().execute();
			} else {
				imageLayout.removeView(v);
				imageNum--;
			}
		}
	}

	class SendMicroblogTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			JSONArray jArr = new JSONArray();
			for (int i = 0; i < imageNum; i++) {
				try {
					publishProgress(i, 0);
					JSONObject jObj = MicroblogServer
							.microblogUploadImage(Environment
									.getExternalStorageDirectory()
									+ Values.UPLOAD_FOLDER
									+ "/microblogImage"
									+ (i + 1));
					jArr.put(jObj.getString("data"));
				} catch (Exception e) {
					publishProgress(i, 1);
				}
			}
			try {
				publishProgress(3, 0);
				MicroblogServer.microblogSend(Values.User.me.id, tranMblogId,
						rootMblogId, xEditText.getXString().toJSONObject(),
						jArr);
				publishProgress(3, 2);
			} catch (Exception e) {
				publishProgress(3, 1);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			if (values[0] < 3) {
				if (values[1] == 0) {
					Toast.makeText(getContext(), "正在上传图片" + (values[0] + 1),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getContext(),
							"图片" + (values[0] + 1) + "上传失败", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				if (values[1] == 0) {
					Toast.makeText(getContext(), "正在发送微博", Toast.LENGTH_SHORT)
							.show();
				} else if (values[1] == 1) {
					Toast.makeText(getContext(), "微博发表失败", Toast.LENGTH_SHORT)
							.show();
				} else {
					if (ActivityMain.getXPageCount() > 1) {
						ActivityMain.delXPage();
					} else {
						ActivityMain.setFirstXPage(new PagePlaza(getContext()));
					}
				}
			}
		}
	}
}
