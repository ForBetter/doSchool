package us.dobell.doschool.base;

import us.dobell.doschool.R;
import us.dobell.xtools.XImageView;
import us.dobell.xtools.XMenu;
import us.dobell.xtools.XPage;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Menu extends XMenu {
	public static final int MAX_PAGE_COUNT = 20;

	private MenuGroups menuGroups;

	public Menu(Context context) {
		this(context, null);
	}

	public Menu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public Menu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		View headMenu = LayoutInflater.from(context).inflate(
				R.layout.view_menu_head, null);
		((XImageView) headMenu.findViewById(R.id.viewHeadMenuHeadXImg))
				.setImageURL(Values.User.me.head);
		headMenu.setOnClickListener(ListenersFactory.BaseFactory
				.getHeadMenuListener());
		((TextView) headMenu.findViewById(R.id.viewHeadMenuNick))
				.setText(Values.User.me.nick);
		getXHideMenuLayout().addView(headMenu,
				LinearLayout.LayoutParams.MATCH_PARENT, 200);
		menuGroups = new MenuGroups(getContext());
		getXHideMenuLayout().addView(menuGroups);
	}

	public void addNews(int groupIndex, int childIndex) {
		menuGroups.addNews(groupIndex, childIndex);
	}

	public XPage getTopXPage() {
		return (XPage) getXContentLayout().getChildAt(getXPageCount() - 1);
	}

	public int getXPageCount() {
		return getXContentLayout().getChildCount();
	}

	public int getRunningXPageCount() {
		int count = 0;
		int testWidth = 0;
		int xPageCount = getXPageCount();
		while (xPageCount > 0) {
			testWidth += ((XPage) getXContentLayout().getChildAt(--xPageCount))
					.measureXPageWidth();
			count++;
			if (testWidth >= getScreenWidth()) {
				break;
			}
		}
		return count;
	}

	private int getScreenWidth() {
		return getContext().getResources().getDisplayMetrics().widthPixels;
	}

	public void addXPage(XPage xPage) {
		int xPageCount = getXPageCount();
		int xPageWidth = xPage.measureXPageWidth();
		int screenWidth = getScreenWidth();
		if (xPageCount > 0) {
			if (xPageCount < MAX_PAGE_COUNT) {
				int runBefore = getRunningXPageCount();
				getXContentLayout().addView(xPage, xPageWidth,
						LinearLayout.LayoutParams.MATCH_PARENT);
				xPage.onCreate();
				xPage.onResume();
				xPageCount++;
				int runAfter = getRunningXPageCount();
				for (int i = xPageCount - runBefore - 1; i < xPageCount
						- runAfter; i++) {
					((XPage) getXContentLayout().getChildAt(i)).onPause();
				}
				int x = -screenWidth;
				for (int i = 0; i < xPageCount; i++) {
					x += ((XPage) getXContentLayout().getChildAt(i))
							.measureXPageWidth();
				}
				if (x > 0) {
					scrollXContentTo(measureXMenuWidth() + x);
				} else {
					scrollXContentTo(measureXMenuWidth());
				}
			} else {
				setFirstXPage(xPage);
			}
		}
	}

	public void setFirstXPage(XPage xPage) {
		int runCount = getRunningXPageCount();
		int xPageCount = getXPageCount();
		LinearLayout xContentLayout = getXContentLayout();
		while (xPageCount > 0) {
			if (runCount-- > 0) {
				((XPage) xContentLayout.getChildAt(xPageCount - 1)).onPause();
			}
			((XPage) xContentLayout.getChildAt(--xPageCount)).onDestory();
		}
		xContentLayout.removeAllViews();
		xContentLayout.addView(xPage, xPage.measureXPageWidth(),
				LinearLayout.LayoutParams.MATCH_PARENT);
		xPage.onCreate();
		xPage.onResume();
		int x = xPage.measureXPageWidth() - getScreenWidth();
		if (x > 0) {
			scrollXContentTo(measureXMenuWidth() + x);
		} else {
			scrollXContentTo(measureXMenuWidth());
		}
	}

	public void delXPage() {
		int xPageCount = getXPageCount();
		LinearLayout xContentLayout = getXContentLayout();
		XPage xPage = (XPage) xContentLayout.getChildAt(xPageCount - 1);
		if (xPageCount > 1) {
			int x = xContentLayout.getWidth() - xPage.measureXPageWidth()
					- getScreenWidth();
			if (x > 0) {
				scrollXContentTo(measureXMenuWidth() + x);
			} else {
				scrollXContentTo(measureXMenuWidth());
			}
			int runBefore = getRunningXPageCount();
			xPage.onPause();
			xPage.onDestory();
			xContentLayout.removeViewAt(--xPageCount);
			int runAfter = getRunningXPageCount();
			for (int i = xPageCount - runBefore; i < xPageCount - runAfter + 1; i++) {
				((XPage) xContentLayout.getChildAt(i)).onResume();
			}
		}
	}

	/**
	 * 该部分的代码写的有点乱。实现的功能是向左滑动时判断是否应该移除，以及移除最后的几个页面
	 */
	@Override
	protected void computeXMenuState(int xScrollX) {
		LinearLayout xContentLayout = getXContentLayout();
		if (xScrollX < measureXMenuWidth()) {
			super.computeXMenuState(xScrollX);
		} else {
			xScrollX += getScreenWidth();
			int index = getXPageCount();
			int runBefore = getRunningXPageCount();
			int xContentWidth = xContentLayout.getWidth() + measureXMenuWidth();
			while (index > 0) {
				XPage xPage = (XPage) xContentLayout.getChildAt(--index);
				xContentWidth -= xPage.measureXPageWidth();
				if (xContentWidth > xScrollX) {
					if (runBefore-- > 0) {
						xPage.onPause();
					}
					xPage.onDestory();
					xContentLayout.removeViewAt(index);
				} else {
					if (xContentWidth + xPage.measureXPageWidth() / 2 > xScrollX) {
						if (runBefore-- > 0) {
							xPage.onPause();
						}
						xPage.onDestory();
						xContentLayout.removeViewAt(index);
					} else {
						scrollXContentTo(xContentWidth);
					}
					int resume = getRunningXPageCount() - runBefore;
					int xPageCount = xContentLayout.getChildCount();
					for (int i = 0; i < resume; i++) {
						((XPage) xContentLayout.getChildAt(xPageCount - 1 - i))
								.onResume();
					}
					break;
				}
			}
		}
	}

	@Override
	public void closeXMenu() {
		super.closeXMenu();
		int runCount = getRunningXPageCount();
		int xPageCount = getXPageCount();
		LinearLayout xContentLayout = getXContentLayout();
		while (xPageCount > 1) {
			XPage xPage = (XPage) xContentLayout.getChildAt(--xPageCount);
			if (runCount-- > 0) {
				xPage.onPause();
			}
			xPage.onDestory();
			xContentLayout.removeView(xPage);
		}
	}

}
