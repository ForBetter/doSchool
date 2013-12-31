package us.dobell.xtools;

import us.dobell.doschool.R;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class XListView extends ScrollView {
	public static final String TAG = "XListView";

	public static final int LOAD_UP_HEIGHT = 100;
	public static final int LOAD_DOWN_HEIGHT = 100;
	public static final int LOAD_BUFFER = 3;
	public static final int LOAD_UP_TIP_DELAY = 500;

	public static final int LOAD_UP_WANT = 0;
	public static final int LOAD_UP_CAN = 1;
	public static final int LOADING_UP = 2;
	public static final int LOAD_UP_SUCCESSFUL = 3;
	public static final int LOAD_UP_FAILED = 4;

	public static final int LOAD_DOWN_SUCCESSFUL = 0;
	public static final int LOADING_DOWN = 1;
	public static final int LOAD_FAILED = 2;
	public static final int LOAD_OVER = 3;

	public boolean isInitial;

	private int pointY, scrollY, lastPointY, distance;
	private int loadUpHeight;

	private LinearLayout xContent;
	private XTipView xLoadUpView, xLoadDownView;
	private ListView xList;

	private XLoadUpListener xLoadUpListener;
	private XLoadDownListener xLoadDownListener;

	public XListView(Context context) {
		super(context);
		init(null, 0);
	}

	public XListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public XListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle) {
		isInitial = true;
		loadUpHeight = LOAD_UP_HEIGHT;
		setVerticalScrollBarEnabled(false);
		xContent = new LinearLayout(getContext());
		xContent.setOrientation(LinearLayout.VERTICAL);
		xLoadUpView = new XTipView(getContext());
		xLoadDownView = new XTipView(getContext());
		xList = new ListView(getContext());
		xList.addFooterView(xLoadDownView);
		xList.setCacheColorHint(Color.TRANSPARENT);
		xList.setOnScrollListener(new LoadDownListener());
		getViewTreeObserver().addOnGlobalLayoutListener(new XLayoutListener());
	}

	public ListView getXList() {
		return xList;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		pointY = (int) ev.getY();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastPointY = pointY;
			break;
		case MotionEvent.ACTION_MOVE:
			if (xList.getFirstVisiblePosition() == 0
					&& xList.getChildAt(0) != null
					&& xList.getChildAt(0).getTop() == 0
					&& ev.getY() > lastPointY + 5
					&& xLoadUpView.getStatus() != LOADING_UP) {
				Log.d(TAG, "截断手势");
				return true;
			}
			break;
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		pointY = (int) ev.getY();
		scrollY = getScrollY();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_MOVE:
			distance = pointY - lastPointY;
			scrollBy(0, -distance);
			if (scrollY < loadUpHeight) {
				xLoadUpView.loadUpCan();
			} else {
				xLoadUpView.loadUpWant();
			}
			lastPointY = pointY;
			break;
		case MotionEvent.ACTION_UP:
			if (scrollY < loadUpHeight) {
				smoothScrollTo(0, loadUpHeight);
				xLoadUpView.loadingUp();
				if (xLoadUpListener != null) {
					xLoadUpListener.onLoadUp();
				}
			} else {
				smoothScrollTo(0, 2 * loadUpHeight);
			}
			break;
		default:
			break;
		}
		return false;
	}

	public void loadUpSuccessful() {
		xLoadUpView.loadUpSuccessful();
		xLoadUpView.postDelayed(new Runnable() {
			@Override
			public void run() {
				smoothScrollTo(0, 2 * loadUpHeight);
				xLoadUpView.loadUpWant();
			}
		}, LOAD_UP_TIP_DELAY);
	}

	public void loadUpFailed() {
		xLoadUpView.loadUpFailed();
		xLoadUpView.postDelayed(new Runnable() {
			@Override
			public void run() {
				smoothScrollTo(0, 2 * loadUpHeight);
				xLoadUpView.loadUpWant();
			}
		}, LOAD_UP_TIP_DELAY);
	}

	public void loadingDown() {
		xLoadDownView.loadingDown();
	}

	public void loadSuccessful() {
		xLoadDownView.loadDownSuccessful();
	}

	public void loadDownFailed() {
		xLoadDownView.loadDownFailed();
	}

	public void loadDownOver() {
		xLoadDownView.loadDownOver();
	}

	public void setXLoadUpListener(XLoadUpListener xLoadUpListener) {
		this.xLoadUpListener = xLoadUpListener;
	}

	public void setXLoadDownListener(XLoadDownListener xLoadDownListener) {
		this.xLoadDownListener = xLoadDownListener;
	}

	class LoadDownListener implements OnScrollListener {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (firstVisibleItem + visibleItemCount > totalItemCount
					- LOAD_BUFFER
					&& xLoadDownView.getStatus() == LOAD_DOWN_SUCCESSFUL) {
				if (xLoadDownListener != null) {
					xLoadDownView.loadingDown();
					xLoadDownListener.onLoadDown();
				}
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

	}

	class XLayoutListener implements OnGlobalLayoutListener {

		@Override
		public void onGlobalLayout() {
			if (isInitial) {
				isInitial = false;
				final int xWidth = getWidth();
				final int xHeight = getHeight();
				xContent.addView(new View(getContext()), xWidth, loadUpHeight);
				xContent.addView(xLoadUpView, xWidth, loadUpHeight);
				xContent.addView(xList, xWidth, xHeight);
				xLoadDownView.setMinimumWidth(xWidth);
				xLoadDownView.setMinimumHeight(loadUpHeight);
				addView(xContent);
				post(new Runnable() {
					@Override
					public void run() {
						smoothScrollTo(0, 2 * loadUpHeight);
					}
				});
			}
		}
	}

	private class XTipView extends LinearLayout {

		private int status;
		private ViewSwitcher tipSwitcher;
		private TextView tipTxt;
		private ImageView tipArrow;

		private Animation downToUpAnimation, upToDownAnimation;

		public XTipView(Context context) {
			super(context);
			setGravity(Gravity.CENTER);
			addView(LayoutInflater.from(context).inflate(
					R.layout.xlistview_tipview, null));
			tipSwitcher = (ViewSwitcher) findViewById(R.id.tipSwitcher);
			tipArrow = (ImageView) findViewById(R.id.tipArrow);
			tipTxt = (TextView) findViewById(R.id.tipTxt);
			downToUpAnimation = new RotateAnimation(0, -180,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			downToUpAnimation.setDuration(200);
			downToUpAnimation.setFillAfter(true);
			upToDownAnimation = new RotateAnimation(-180, 0,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			upToDownAnimation.setDuration(200);
			upToDownAnimation.setFillAfter(true);
		}

		public int getStatus() {
			return status;
		}

		public void loadUpWant() {
			if (status != LOAD_UP_WANT) {
				tipTxt.setText("下拉刷新");
				tipSwitcher.setVisibility(View.VISIBLE);
				tipSwitcher.setDisplayedChild(0);
				if (status == LOAD_UP_CAN) {
					tipArrow.startAnimation(upToDownAnimation);
				}
				status = LOAD_UP_WANT;
			}
		}

		public void loadUpCan() {
			if (status != LOAD_UP_CAN) {
				tipTxt.setText("释放刷新");
				tipSwitcher.setVisibility(View.VISIBLE);
				tipSwitcher.setDisplayedChild(0);
				if (status == LOAD_UP_WANT) {
					tipArrow.startAnimation(downToUpAnimation);
				}
				status = LOAD_UP_CAN;
			}
		}

		public void loadingUp() {
			if (status != LOADING_UP) {
				tipTxt.setText("正在刷新");
				tipArrow.clearAnimation();
				tipSwitcher.setVisibility(View.VISIBLE);
				tipSwitcher.setDisplayedChild(1);
				status = LOADING_UP;
			}
		}

		public void loadUpSuccessful() {
			if (status != LOAD_UP_SUCCESSFUL) {
				tipTxt.setText("刷新成功");
				tipSwitcher.setVisibility(View.VISIBLE);
				tipSwitcher.setDisplayedChild(0);
				status = LOAD_UP_SUCCESSFUL;
			}
		}

		public void loadUpFailed() {
			if (status != LOAD_UP_FAILED) {
				tipTxt.setText("刷新失败");
				tipSwitcher.setVisibility(View.VISIBLE);
				tipSwitcher.setDisplayedChild(0);
				status = LOAD_UP_FAILED;
			}
		}

		public void loadingDown() {
			if (status != LOADING_DOWN) {
				tipTxt.setText("正在加载");
				tipSwitcher.setVisibility(View.VISIBLE);
				tipSwitcher.setDisplayedChild(1);
				status = LOADING_DOWN;
			}
		}

		public void loadDownSuccessful() {
			if (status != LOAD_DOWN_SUCCESSFUL) {
				tipTxt.setText("准备加载");
				tipSwitcher.setVisibility(View.GONE);
				status = LOAD_DOWN_SUCCESSFUL;
			}
		}

		public void loadDownFailed() {
			if (status != LOAD_FAILED) {
				tipTxt.setText("加载失败，点击重试");
				tipSwitcher.setVisibility(View.GONE);
				status = LOAD_FAILED;
				setOnClickListener(new XReloadListener());
			}
		}

		public void loadDownOver() {
			if (status != LOAD_OVER) {
				tipTxt.setText("没有更多了");
				tipSwitcher.setVisibility(View.GONE);
				status = LOAD_OVER;
				setOnClickListener(new XReloadListener());
			}
		}

		private class XReloadListener implements View.OnClickListener {
			@Override
			public void onClick(View v) {
				setOnClickListener(null);
				if (xLoadDownListener != null) {
					Log.d(TAG, "正在重试加载");
					xLoadDownView.loadingDown();
					xLoadDownListener.onLoadDown();
				}
			}
		}
	}

	public interface XLoadUpListener {
		public void onLoadUp();
	}

	public interface XLoadDownListener {
		public void onLoadDown();
	}
}