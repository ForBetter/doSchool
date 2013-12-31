package us.dobell.doschool.user;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.doschool.ActivityMain;
import us.dobell.doschool.R;
import us.dobell.doschool.base.Values;
import us.dobell.xtools.XDatabase;
import us.dobell.xtools.XImageView;
import us.dobell.xtools.XPagePlus;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

public class PageCards extends XPagePlus<Card> {

	public PageCards(Context context) {
		super(context, false);
	}

	@Override
	public View getSpecialView() {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		((TextView) titleMidLayout.getChildAt(0)).setText("名片夹");
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_card, null);
		}
		((XImageView) convertView.findViewById(R.id.itemCardHeadXimg))
				.setImageURL(getXObject(position).head);
		((TextView) convertView.findViewById(R.id.itemCardNickTxt))
				.setText(getXObject(position).nick);
		((TextView) convertView.findViewById(R.id.itemCardIntroduceTxt))
				.setText(getXObject(position).introduce);
		return convertView;
	}

	@Override
	public ArrayList<Card> loadDatabase(int lastId, int rollType, int objCount) {
		return UserDatabase.userCardList(lastId, rollType, objCount);
	}

	@Override
	public ArrayList<Card> loadServer(int lastId, int rollType, int objCount) {
		JSONObject jObj = UserServer.userCardList(Values.User.me.id, lastId,
				rollType, objCount);
		try {
			ArrayList<Card> cardList = new ArrayList<Card>();
			JSONArray jArr = jObj.getJSONArray("data");
			for (int i = 0; i < jArr.length(); i++) {
				cardList.add(new Card(jArr.getJSONObject(i)));
			}
			return cardList;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void saveToDatabase(Card x) {
		UserDatabase.userCardSet(x);
	}

	@Override
	public void deleteFromDatabase(Card x) {
		UserDatabase.userCardDelete(x.id, XDatabase.DELETE_ONE);
	}

	@Override
	public void itemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("id", getXObject(arg2).id);
			PageCard pageCard = new PageCard(getContext());
			pageCard.setInfo(jObj);
			ActivityMain.addXPage(pageCard);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
