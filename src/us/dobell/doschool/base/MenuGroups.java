package us.dobell.doschool.base;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.doschool.ActivityMain;
import us.dobell.doschool.microblog.PageAt;
import us.dobell.doschool.microblog.PagePerson;
import us.dobell.doschool.microblog.PagePlaza;
import us.dobell.doschool.microblog.PageWrite;
import us.dobell.doschool.tools.ActivityGongGao;
import us.dobell.doschool.tools.ActivityInform;
import us.dobell.doschool.tools.ActivityKCB;
import us.dobell.doschool.tools.ActivityLib;
import us.dobell.doschool.tools.ActivityWaiMai;
import us.dobell.doschool.user.PageApplys;
import us.dobell.doschool.user.PageCards;
import us.dobell.doschool.user.PageFriends;
import us.dobell.xtools.XPage;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

public class MenuGroups extends ExpandableListView {
	public static final String TAG = "MenuGroups";
	public static final int GROUP_USER = 0;
	public static final int CHILD_FRIENDS = 0;
	public static final int CHILD_CARDS = 1;
	public static final int CHILD_APPLYS = 2;

	public static final int GROUP_MICROBLOG = 1;
	public static final int CHILD_PLAZA = 0;
	public static final int CHILD_WRITE = 1;
	public static final int CHILD_PERSON = 2;
	public static final int CHILD_AT = 3;

	private ArrayList<MenuGroup> menuGroups;
	private MenuItemsAdapter menuItemAdapter;

	public MenuGroups(Context context) {
		super(context);
		initMenuGroups();
		setGroupIndicator(null);
		setOnChildClickListener(new MenuItemClikListener());
		menuItemAdapter = new MenuItemsAdapter();
		setAdapter(menuItemAdapter);
	}

	private void initMenuGroups() {
		menuGroups = new ArrayList<MenuGroups.MenuGroup>();

		MenuGroup groupUser = new MenuGroup("我的圈子");
		groupUser.addItem(new MenuItem("好友们"));
		groupUser.addItem(new MenuItem("名片夹"));
		groupUser.addItem(new MenuItem("好友申请"));
		menuGroups.add(groupUser);

		MenuGroup groupMicroBlog = new MenuGroup("我的微博");
		groupMicroBlog.addItem(new MenuItem("广场"));
		groupMicroBlog.addItem(new MenuItem("写微博"));
		groupMicroBlog.addItem(new MenuItem("主页"));
		groupMicroBlog.addItem(new MenuItem("@我的"));
		menuGroups.add(groupMicroBlog);

		MenuGroup campuslife = new MenuGroup("校园生活");
		campuslife.addItem(new MenuItem("课程表"));
		campuslife.addItem(new MenuItem("图书馆"));
		campuslife.addItem(new MenuItem("学术讲座"));
		campuslife.addItem(new MenuItem("外卖电话"));
		campuslife.addItem(new MenuItem("通知公告"));
		menuGroups.add(campuslife);

		setSelected(1, 0);
	}

	private void setSelected(int groupIndex, int itemIndex) {
		for (int i = 0; i < menuGroups.size(); i++) {
			menuGroups.get(i).isSelected = groupIndex == i;
			ArrayList<MenuItem> menuItems = menuGroups.get(i).subItems;
			for (int j = 0; j < menuItems.size(); j++) {
				menuItems.get(j).isSelected = groupIndex == i && itemIndex == j;
			}
		}
	}

	public void addNews(int group, int child) {
		menuGroups.get(group).itemNews++;
		menuGroups.get(group).getItem(child).itemNews++;
		menuItemAdapter.notifyDataSetChanged();
	}

	public void clearNews(int group, int child) {
		menuGroups.get(group).itemNews -= menuGroups.get(group).getItem(child).itemNews;
		menuGroups.get(group).getItem(child).itemNews = 0;
		menuItemAdapter.notifyDataSetChanged();
	}

	class MenuItemClikListener implements OnChildClickListener {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			clearNews(groupPosition, childPosition);
			setSelected(groupPosition, childPosition);
			menuItemAdapter.notifyDataSetChanged();
			Intent it;
			switch (groupPosition) {
			case 2:
				switch (childPosition) {
				case 1:
					it = new Intent(getContext(), ActivityLib.class);
					getContext().startActivity(it);
					break;
				case 2:
					it = new Intent(getContext(), ActivityInform.class);
					getContext().startActivity(it);
					break;
				case 3:
					it = new Intent(getContext(), ActivityWaiMai.class);
					getContext().startActivity(it);
					break;
				case 4:
					it = new Intent(getContext(), ActivityGongGao.class);
					getContext().startActivity(it);
					break;
				default:
					it = new Intent(getContext(), ActivityKCB.class);
					getContext().startActivity(it);
					break;
				}
				break;
			case 1:
				switch (childPosition) {
				case 1:
					ActivityMain.setFirstXPage(new PageWrite(getContext()));
					break;
				case 2:
					try {
						JSONObject jObj = new JSONObject();
						jObj.put("id", Values.User.me.id);
						XPage xPage = new PagePerson(getContext());
						xPage.setInfo(jObj);
						ActivityMain.setFirstXPage(xPage);
					} catch (JSONException e) {
					}
					break;
				case 3:
					ActivityMain.setFirstXPage(new PageAt(getContext()));
					break;
				default:
					ActivityMain.setFirstXPage(new PagePlaza(getContext()));
					break;
				}
				break;
			default:
				switch (childPosition) {
				case 1:
					ActivityMain.setFirstXPage(new PageCards(getContext()));
					break;
				case 2:
					ActivityMain.setFirstXPage(new PageApplys(getContext()));
					break;
				default:
					ActivityMain.setFirstXPage(new PageFriends(getContext()));
					break;
				}
				break;
			}
			return false;
		}

	}

	class MenuItemsAdapter extends BaseExpandableListAdapter {

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return menuGroups.get(groupPosition).getItem(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return getGroupId(groupPosition) * 100 + childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			return ViewsFactory.BaseFactory.getMenuGroupView(getContext(),
					menuGroups.get(groupPosition).getItem(childPosition));
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return menuGroups.get(groupPosition).subItems.size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return menuGroups.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return menuGroups.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			switch (groupPosition) {
			case 1:
				return 1;
			default:
				return 0;
			}
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			return ViewsFactory.BaseFactory.getMenuGroupView(getContext(),
					menuGroups.get(groupPosition));
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	public class MenuGroup extends MenuItem {
		ArrayList<MenuItem> subItems;

		public MenuGroup(String groupName) {
			super(groupName);
			this.subItems = new ArrayList<MenuGroups.MenuItem>();
		}

		public void addItem(MenuItem menuItem) {
			subItems.add(menuItem);
		}

		public MenuItem getItem(int index) {
			return subItems.get(index);
		}
	}

	public class MenuItem {
		String itemName;
		boolean isSelected;
		int itemNews;

		public MenuItem(String itemName) {
			this.itemName = itemName;
			this.isSelected = false;
			this.itemNews = 0;
		}
	}
}
