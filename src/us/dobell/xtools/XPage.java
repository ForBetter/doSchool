package us.dobell.xtools;

import org.json.JSONObject;

import us.dobell.doschool.ActivityMain;
import us.dobell.doschool.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * 这是一个带顶部标题栏的View,放入Menu中使用可以做到在一个activity中平滑切换多个视图，不用跳转
 * 在构造时会先后调用setTitleMidLayout
 * ，setTitleLeftBtn，setTitleRightBtn，setContentLayout函数
 * 来设置该视图顶部标题栏中的具体内容，子类可以覆写这些方法来定制该View
 * 
 * @author xxx
 * 
 */
public class XPage extends LinearLayout {
	public static final String TAG = "XPage";

	private JSONObject info;
	public LinearLayout titleMidLayout, contentLayout;
	public ImageButton titleLeftBtn, titleRightBtn;

	public XPage(Context context) {
		super(context);
		setOrientation(LinearLayout.VERTICAL);
		addView(LayoutInflater.from(context).inflate(R.layout.view_xpage, null),
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		titleMidLayout = (LinearLayout) findViewById(R.id.viewXPageTitleMidLayout);
		titleLeftBtn = (ImageButton) findViewById(R.id.viewXPageTitleLeftBtn);
		titleRightBtn = (ImageButton) findViewById(R.id.viewXPageTitleRightBtn);
		contentLayout = (LinearLayout) findViewById(R.id.viewXpageContent);
		titleLeftBtn.setOnClickListener(new TitleLeftBtnListener());
	}

	public void setInfo(JSONObject info) {
		this.info = info;
	}

	public JSONObject getInfo() {
		return this.info;
	}

	public void onCreate() {
	}

	public void onResume() {
	}

	public void onPause() {
	}

	public void onDestory() {
	}

	public int measureXPageWidth() {
		return getContext().getResources().getDisplayMetrics().widthPixels;
	}

	class TitleLeftBtnListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (ActivityMain.isXMenuOpen()) {
				ActivityMain.closeXMenu();
			} else {
				ActivityMain.openXMenu();
			}
		}
	}
}
