package us.dobell.doschool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import us.dobell.doschool.base.Menu;
import us.dobell.doschool.base.Values;
import us.dobell.doschool.microblog.PagePlaza;
import us.dobell.doschool.microblog.PageWrite;
import us.dobell.doschool.user.PageCard;
import us.dobell.doschool.user.UserDatabase;
import us.dobell.xtools.XDatabase;
import us.dobell.xtools.XPage;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class ActivityMain extends Activity {
	public static final String TAG = "ActivityMain";
	public static final int CODE_MICROBLOG_IMAGE_ADD = 0x00101001;

	private static Menu mainMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		XDatabase.openDatabase(this, Values.User.me.id);
		UserDatabase.userSet(Values.User.me);
		setContentView(R.layout.activity_main);
		mainMenu = (Menu) findViewById(R.id.activityMainMenu);
		ActivityMain.setFirstXPage(new PagePlaza(this));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		XDatabase.closeDatabase();
	}

	public static boolean isXMenuOpen() {
		return mainMenu.isXMenuOpen();
	}

	public static void closeXMenu() {
		mainMenu.closeXMenu();
	}

	public static void openXMenu() {
		mainMenu.openXMenu();
	}

	public static void addNews(int groupIndex, int childIndex) {
		Log.d(TAG, mainMenu == null ? "空" : "非空");
		mainMenu.addNews(groupIndex, childIndex);
	}

	public static int getXPageCount() {
		return mainMenu.getXPageCount();
	}

	public static void setFirstXPage(XPage xPage) {
		mainMenu.setFirstXPage(xPage);
	}

	public static void addXPage(XPage xPage) {
		mainMenu.addXPage(xPage);
	}

	public static void delXPage() {
		mainMenu.delXPage();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mainMenu.getXPageCount() > 1) {
			mainMenu.delXPage();
			return true;
		} else {
			return super.onKeyUp(keyCode, event);
		}
	}

	@Override
	protected void onActivityResult(int id, int resultCode, Intent data) {
		super.onActivityResult(id, resultCode, data);
		if (data == null) {
			return;
		}
		File uploadFolder = new File(Environment.getExternalStorageDirectory()
				+ Values.UPLOAD_FOLDER);
		uploadFolder.mkdirs();
		switch (id) {
		case PageCard.CODE_CARD_HEAD_PICK:
			File headFile = new File(Environment.getExternalStorageDirectory()
					+ Values.UPLOAD_FOLDER, "head");
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(data.getData(), "image/*");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 300);
			intent.putExtra("outputY", 300);
			intent.putExtra("output", Uri.fromFile(headFile));
			startActivityForResult(intent, PageCard.CODE_CARD_HEAD_CUT);
			break;
		case PageCard.CODE_CARD_HEAD_CUT:
			File head = new File(Environment.getExternalStorageDirectory()
					+ Values.UPLOAD_FOLDER, "head");
			InputStream is = null;
			Bitmap bmp = null;
			try {
				is = new FileInputStream(head);
				bmp = BitmapFactory.decodeStream(is);
				File out = new File(Environment.getExternalStorageDirectory()
						+ Values.UPLOAD_FOLDER, "head");
				bmp.compress(CompressFormat.JPEG, 100,
						new FileOutputStream(out));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				Toast.makeText(this, "未找到图片", Toast.LENGTH_SHORT).show();
				bmp = null;
			} catch (OutOfMemoryError e) {
				Toast.makeText(this, "你上传的图片过大", Toast.LENGTH_SHORT).show();
				bmp = null;
			}
			if (bmp == null) {
				return;
			}
			((PageCard) (mainMenu.getTopXPage())).headXimg.setImageBitmap(bmp);
			((PageCard) (mainMenu.getTopXPage())).isHeadChanged = true;
			break;
		case CODE_MICROBLOG_IMAGE_ADD:
			try {
				Log.d(TAG, "正在添加微博图片");
				Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(data.getData()));
				Log.d(TAG, "正在添加微博图片");
				((PageWrite) (mainMenu.getTopXPage())).addImage(bitmap);
			} catch (FileNotFoundException e) {
				Toast.makeText(this, "未找到图片", Toast.LENGTH_SHORT).show();
				return;
			} catch (OutOfMemoryError e) {
				Toast.makeText(this, "你上传的图片过大", Toast.LENGTH_SHORT).show();
				return;
			}
			break;
		default:
			break;
		}
	}
}
