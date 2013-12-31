package us.dobell.xtools;

import java.util.ArrayList;
import java.util.HashSet;

import us.dobell.doschool.R;
import us.dobell.doschool.user.User;
import us.dobell.doschool.user.UserDatabase;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class XEditText extends EditText {
	public static final String TAG = "XEditText";

	public static final int TYPE_USER = 0X00001;

	private static HashSet<Character> triggers = new HashSet<Character>();
	{
		triggers.add('@');
	}

	private XString xString;
	private boolean shouldWork;

	public XEditText(Context context) {
		this(context, null);
	}

	public XEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.shouldWork = true;
		this.xString = new XString("");
		addTextChangedListener(new XWatcher());
	}

	public void setXString(XString xString) {
		this.xString = xString;
		shouldWork = false;
		Log.d(TAG, "xString为" + xString.toString());
		setText(xString.string, BufferType.NORMAL);
		setEnabled(false);
		setEnabled(true);
		shouldWork = true;
	}

	public XString getXString() {
		return xString;
	}

	private ArrayList<XSpan> copySpans() {
		ArrayList<XSpan> xSpans = new ArrayList<XSpan>();
		for (XSpan xSpan : xString.spans) {
			xSpans.add(xSpan.copy());
		}
		return xSpans;
	}

	private void sortSpans(ArrayList<XSpan> xSpanList) {
		for (int i = 0; i < xSpanList.size(); i++) {
			int maxEnd = 0;
			int maxIndex = -1;
			for (int j = i; j < xSpanList.size(); j++) {
				if (xSpanList.get(j).end > maxEnd) {
					maxEnd = xSpanList.get(j).end;
					maxIndex = j;
				}
			}
			XSpan temp = xSpanList.get(maxIndex);
			xSpanList.set(maxIndex, xSpanList.get(i));
			xSpanList.set(i, temp);
		}
	}

	private int indexSpans(ArrayList<XSpan> xSpanList, int type, int id) {
		for (int i = 0; i < xSpanList.size(); i++) {
			if (xSpanList.get(i).type == type && xSpanList.get(i).id == id) {
				return i;
			}
		}
		return -1;
	}

	private ArrayList<XSpan> whatsNew(ArrayList<XSpan> tempSpans) {
		ArrayList<XSpan> newList = new ArrayList<XSpan>();
		for (XSpan xSpan : tempSpans) {
			if (indexSpans(xString.spans, xSpan.type, xSpan.id) == -1) {
				newList.add(xSpan.copy());
			}
		}
		return newList;
	}

	private ArrayList<XSpan> whatsMiss(ArrayList<XSpan> tempSpans) {
		ArrayList<XSpan> missList = new ArrayList<XSpan>();
		for (XSpan xSpan : xString.spans) {
			if (indexSpans(tempSpans, xSpan.type, xSpan.id) == -1) {
				missList.add(xSpan.copy());
			}
		}
		return missList;
	}

	class XWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (shouldWork) {
				Log.d(TAG, " start:" + start + " before:" + before + " count=:"
						+ count);
				xString = xString.cutOff(start, before);
				xString = xString.addOn(start,
						new XString(s.subSequence(start, start + count)
								.toString()));
				Log.d(TAG, "xString变为" + xString.toString());
				if (count == 1 && triggers.contains(s.charAt(start))) {
					new XDialog(getContext(), start, s.charAt(start)).show();
				}
			}
		}
	}

	class XDialog extends AlertDialog {
		int type;
		int startIndex;
		ArrayList<XSpan> tempSpans;
		ArrayList<? extends XObject> list;
		BtnListener dialogListener;
		ListView listView;
		ListAdapter listAdapter;
		ListListener listListener;

		protected XDialog(Context context, int startIndex, char c) {
			super(context);
			switch (c) {
			case '@':
				setTitle("选择要@的好友");
				type = TYPE_USER;
				list = UserDatabase.userFriendList();
				break;
			default:
				break;
			}
			this.startIndex = startIndex;
			this.tempSpans = copySpans();
			this.dialogListener = new BtnListener();
			setButton(BUTTON_POSITIVE, "确定", dialogListener);
			setButton(BUTTON_NEGATIVE, "取消", dialogListener);
			listView = new ListView(getContext());
			listView.setAdapter(listAdapter = new ListAdapter());
			listView.setOnItemClickListener(listListener = new ListListener());
			setView(listView);
		}

		public String getXSpanContent(int id) {
			XObject xObject = null;
			for (XObject xObj : list) {
				if (xObj.id == id) {
					xObject = xObj;
					break;
				}
			}
			if (xObject == null) {
				return null;
			}
			switch (type) {
			case TYPE_USER:
				return "@" + ((User) xObject).nick;
			default:
				return "";
			}
		}

		class ListAdapter extends BaseAdapter {

			@Override
			public int getCount() {
				return list.size();
			}

			@Override
			public Object getItem(int position) {
				return list.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				switch (type) {
				case TYPE_USER:
					if (convertView == null) {
						convertView = LayoutInflater.from(getContext())
								.inflate(R.layout.item_xselector, null);
						((LinearLayout) convertView
								.findViewById(R.id.itemXSelectorLayout))
								.addView(LayoutInflater.from(getContext())
										.inflate(R.layout.item_user, null));
					}
					User user = (User) list.get(position);
					((XImageView) convertView
							.findViewById(R.id.itemUserHeadXimg))
							.setImageURL(user.head);
					((TextView) convertView.findViewById(R.id.itemUserNickTxt))
							.setText(user.nick);
					((CheckBox) convertView
							.findViewById(R.id.itemXSelectorCkbox))
							.setChecked(indexSpans(tempSpans, TYPE_USER,
									user.id) != -1);
					break;
				default:
					break;
				}
				return convertView;
			}
		}

		class ListListener implements OnItemClickListener {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				CheckBox checkBox = (CheckBox) arg1
						.findViewById(R.id.itemXSelectorCkbox);
				if (checkBox.isChecked()) {
					Log.d(TAG, "移除XSpan" + list.get(arg2).id);
					checkBox.setChecked(false);
					tempSpans.remove(indexSpans(tempSpans, TYPE_USER,
							list.get(arg2).id));
				} else {
					Log.d(TAG, "新增XSpan" + list.get(arg2).id);
					checkBox.setChecked(true);
					tempSpans.add(new XSpan(0, 0, type, list.get(arg2).id));
				}
			}
		}
	}

	class BtnListener implements
			android.content.DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			XDialog xDialog = ((XDialog) dialog);
			if (which == DialogInterface.BUTTON_POSITIVE) {
				// 由于在触发召唤对话框的时候是用某个字符触发的，所以要移除该字符
				xString = xString.cutOff(xDialog.startIndex, 1);
				// 查看哪些旧的召唤物被移除了,在xString中移除它们
				ArrayList<XSpan> missList = whatsMiss(xDialog.tempSpans);
				sortSpans(missList);
				for (XSpan xSpan : missList) {
					if (xSpan.end <= xDialog.startIndex) {
						xDialog.startIndex -= xSpan.end - xSpan.start;
					}
					xString = xString.cutOff(xSpan.start, xSpan.end
							- xSpan.start);
				}
				// 查看新增了哪些召唤物，在xString的最后添加它们
				ArrayList<XSpan> newList = whatsNew(xDialog.tempSpans);
				XString xStr = new XString("");
				for (XSpan xSpan : newList) {
					xStr = xStr.addXSpan(xSpan,
							xDialog.getXSpanContent(xSpan.id));
				}
				// 让XEditText显示修改后的内容，注意不要让XWatcher工作
				setXString(xString.addOn(xDialog.startIndex, xStr));
				setSelection(xDialog.startIndex + xStr.string.length());
			} else {
				dialog.dismiss();
			}
		}
	}
}
