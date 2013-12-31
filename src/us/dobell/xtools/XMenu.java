package us.dobell.xtools;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * 这是一个左侧边菜单导航视图,视图分为两层，上层为内容显示视图。 下层即隐藏在内容视图后面的是菜单。 当内容视图一直向右滑动，左边的菜单就会显现出来
 * 
 * @author xxx
 */
public class XMenu extends FrameLayout {
	public static final String TAG = "XMenu";

	/**
	 * 菜单默认的打开宽度
	 */
	public static final int DEFAULT_XMENU_WIDTH = 300;
	/**
	 * 菜单的手势区域的默认宽度
	 */
	public static final int DEFAULT_XZONE_WIDTH = 50;
	/**
	 * 菜单的手势区域的默认的离顶部的宽度
	 */
	public static final int DEFAULT_XZONE_MARGIN_TOP = 100;
	/**
	 * 菜单的手势区域的默认的离底部的宽度
	 */
	public static final int DEFAULT_XZONE_MARGIN_BOTTOM = 0;

	/**
	 * 菜单的打开宽度
	 */
	private int xMenuWidth;
	/**
	 * 菜单的手势区域的宽度
	 */
	private int xZoneWidth;
	/**
	 * 菜单的手势区域离顶部的高度
	 */
	private int xZoneMarginTop;
	/**
	 * 菜单的手势区域里底部的高度
	 */
	private int xZoneMarginBottom;

	/**
	 * 底层菜单所在布局
	 */
	private XLinearLayout xHideMenuLayout;

	/**
	 * 顶层菜单所在布局
	 */
	private LinearLayout xSlideMenuLayout;

	/**
	 * 内容显示布局
	 */
	private XLinearLayout xContentLayout;

	/**
	 * 滚动布局，该滚动布局只接受在手势区域内的手势,其他手势则会被分发到子控件
	 */
	private XScrollView xScrollView;

	/**
	 * 滚动布局的内容布局，里面放着顶层菜单所在的布局，和内容显示布局
	 */
	private LinearLayout xScrollViewContent;

	/**
	 * 菜单是否正在初始化
	 */
	private boolean isInitial;

	/**
	 * 监听菜单状态变化的监听器
	 */
	private OnXMenuStateChangedListener xListener;

	public XMenu(Context context) {
		super(context);
		init(null, 0);
	}

	public XMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public XMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	/**
	 * 初始化菜单
	 * 
	 * @param attrs
	 * @param defStyle
	 */
	private void init(AttributeSet attrs, int defStyle) {
		isInitial = true;
		xMenuWidth = measureXMenuWidth();
		xZoneWidth = measureXZoneWidth();
		xZoneMarginTop = measureXZoneMarginTop();
		xZoneMarginBottom = measureXZoneMarginBottom();

		xHideMenuLayout = new XLinearLayout(getContext());
		xSlideMenuLayout = new LinearLayout(getContext());
		xContentLayout = new XLinearLayout(getContext());
		xContentLayout.setBackgroundColor(Color.WHITE);
		xScrollView = new XScrollView(getContext());
		xScrollViewContent = new LinearLayout(getContext());

		xHideMenuLayout.setOrientation(LinearLayout.VERTICAL);
		xSlideMenuLayout.setOrientation(LinearLayout.VERTICAL);
		xContentLayout.setOrientation(LinearLayout.HORIZONTAL);
		xScrollView.setHorizontalFadingEdgeEnabled(false);
		xScrollView.setVerticalFadingEdgeEnabled(false);
		xScrollView.setHorizontalScrollBarEnabled(false);
		xScrollViewContent.setOrientation(LinearLayout.HORIZONTAL);

		getViewTreeObserver().addOnGlobalLayoutListener(new XLayoutListener());

	}

	protected int measureXMenuWidth() {
		return (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.618);
	}

	protected int measureXZoneWidth() {
		return DEFAULT_XZONE_WIDTH;
	}

	protected int measureXZoneMarginTop() {
		return DEFAULT_XZONE_MARGIN_TOP;
	}

	protected int measureXZoneMarginBottom() {
		return DEFAULT_XZONE_MARGIN_BOTTOM;
	}

	protected void computeXMenuState(int xScrollX) {
		if (xScrollX < xMenuWidth / 2) {
			openXMenu();
		} else {
			closeXMenu();
		}
	}

	public void scrollXContentTo(final int x) {
		xScrollView.post(new Runnable() {
			@Override
			public void run() {
				xScrollView.smoothScrollTo(x, 0);
			}
		});
	}

	public void openXMenu() {
		xScrollView.smoothScrollTo(0, 0);
		Log.d(TAG, "菜单打开");
		if (xListener != null) {
			xListener.onOpen();
		}
	}

	public void closeXMenu() {
		xScrollView.smoothScrollTo(xMenuWidth, 0);
		Log.d(TAG, "菜单关闭");
		if (xListener != null) {
			xListener.onClose();
		}
	}

	public final boolean isXMenuOpen() {
		return xScrollView.getScrollX() < xMenuWidth;
	}

	public final LinearLayout getXHideMenuLayout() {
		return xHideMenuLayout;
	}

	public final LinearLayout getXSlideMenuLayout() {
		return xSlideMenuLayout;
	}

	public final LinearLayout getXContentLayout() {
		return xContentLayout;
	}

	public final void setOnXMenuStateChangedListener(
			OnXMenuStateChangedListener xlistener) {
		this.xListener = xlistener;
	}

	public interface OnXMenuStateChangedListener {
		public void onScroll();

		public void onClose();

		public void onOpen();
	}

	final class XLayoutListener implements OnGlobalLayoutListener {

		@Override
		public void onGlobalLayout() {
			if (!isInitial) {
				return;
			}
			isInitial = false;
			final int width = getMeasuredWidth();
			final int height = getMeasuredHeight();
			if (xMenuWidth < 0) {
				xMenuWidth = 0;
			}
			if (xMenuWidth > width) {
				xMenuWidth = width;
			}
			if (xZoneWidth < 0) {
				xZoneWidth = 0;
			}
			if (xZoneWidth > width) {
				xZoneWidth = width;
			}
			if (xZoneMarginTop < 0) {
				xZoneMarginTop = 0;
			}
			if (xZoneMarginTop > height) {
				xZoneMarginTop = height;
			}
			if (xZoneMarginBottom < 0) {
				xZoneMarginBottom = 0;
			}
			if (xZoneMarginBottom > height) {
				xZoneMarginBottom = height;
			}
			// xMenuWidth = (int) (width * 0.618);
			addView(xHideMenuLayout, xMenuWidth, height);
			xScrollViewContent.addView(xSlideMenuLayout, xMenuWidth, height);
			xContentLayout.setMinimumWidth(width);
			xScrollViewContent.addView(xContentLayout,
					LinearLayout.LayoutParams.WRAP_CONTENT, height);
			xScrollView.addView(xScrollViewContent);
			addView(xScrollView, width, height);
			post(new Runnable() {
				@Override
				public void run() {
					closeXMenu();
				}
			});
		}
	}

	final class XScrollView extends HorizontalScrollView {
		private boolean shouldScroll;
		private float lastPointX;
		private int distanceX;

		public XScrollView(Context context) {
			super(context);
		}

		public XScrollView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public XScrollView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}

		@Override
		public boolean onInterceptTouchEvent(MotionEvent event) {
			float pointX = event.getX();
			float pointY = event.getY();
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (pointY < xZoneMarginTop
						|| pointY > getHeight() - xZoneMarginBottom) {
					shouldScroll = false;
					return false;
				}
				if ((!isXMenuOpen() && pointX < xZoneWidth)
						|| (isXMenuOpen() && pointX > xMenuWidth)) {
					Log.d(TAG, "截获手势(" + pointX + "," + pointY + ")");
					shouldScroll = true;
					return true;
				} else {
					shouldScroll = false;
					return false;
				}
			} else {
				return false;
			}
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float pointX = event.getX();
			if (shouldScroll) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastPointX = pointX;
					break;
				case MotionEvent.ACTION_MOVE:
					distanceX = (int) (lastPointX - pointX);
					lastPointX = pointX;
					xScrollView.scrollBy(distanceX, 0);
					if (xListener != null) {
						xListener.onScroll();
					}
					break;
				case MotionEvent.ACTION_UP:
					computeXMenuState(xScrollView.getScrollX());
					break;
				default:
					break;
				}
				return true;
			} else {
				return false;
			}
		}
	}

	final class XLinearLayout extends LinearLayout {

		public XLinearLayout(Context context) {
			super(context);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			Log.d(TAG, "事件(" + event.getX() + "," + event.getY() + ")被终结");
			return true;
		}

	}
}
