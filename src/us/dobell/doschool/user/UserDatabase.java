package us.dobell.doschool.user;

import java.util.ArrayList;

import us.dobell.xtools.XDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class UserDatabase extends XDatabase {
	public static final String TAG = "Database";

	public static void userSet(User user) {
		ContentValues values = new ContentValues();
		values.put("id", user.id);
		values.put("nick", user.nick);
		values.put("head", user.head);
		values.put("card", user.card);
		values.put("friend", user.friend);
		if (userGet(user.id) == null) {
			xInsert("user", null, values);
		} else {
			xUpdate("user", values, "id=?", new String[] { "" + user.id });
		}
	}

	public static User userGet(int objId) {
		Cursor cursor = xQuery("select * from user where id=?",
				new String[] { "" + objId });
		User user = null;
		if (cursor.moveToNext()) {
			user = new User(cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getString(cursor.getColumnIndex("nick")),
					cursor.getString(cursor.getColumnIndex("head")),
					cursor.getInt(cursor.getColumnIndex("card")),
					cursor.getInt(cursor.getColumnIndex("friend")));
		}
		cursor.close();
		return user;
	}

	public static ArrayList<User> userSearch(String objNick) {
		ArrayList<User> searchResult = new ArrayList<User>();
		Cursor cursor = xQuery("select * from user where nick like ?",
				new String[] { objNick });
		while (cursor.moveToNext()) {
			User user = new User(cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getString(cursor.getColumnIndex("nick")),
					cursor.getString(cursor.getColumnIndex("head")),
					cursor.getInt(cursor.getColumnIndex("card")),
					cursor.getInt(cursor.getColumnIndex("friend")));
			searchResult.add(user);
		}
		cursor.close();
		return searchResult;
	}

	public static void userCardSet(Card card) {
		ContentValues values = new ContentValues();
		values.put("id", card.id);
		values.put("nick", card.nick);
		values.put("head", card.head);
		values.put("card", card.card);
		values.put("friend", card.friend);
		values.put("fId", card.fId);
		values.put("name", card.name);
		values.put("phone", card.phone);
		values.put("qq", card.qq);
		values.put("mail", card.mail);
		values.put("introduce", card.introduce);
		if (userCardGet(card.id) == null) {
			xInsert("userCard", null, values);
		} else {
			xUpdate("userCard", values, "id=?", new String[] { "" + card.id });
		}
	}

	public static void userDelete(int usrId) {
		xDelete("user", "id=?", new String[] { "" + usrId });
	}

	public static Card userCardGet(int usrId) {
		Cursor cursor = xQuery("select * from userCard where id = ?",
				new String[] { "" + usrId });
		Card card = null;
		if (cursor.moveToNext()) {
			card = new Card(cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getString(cursor.getColumnIndex("nick")),
					cursor.getString(cursor.getColumnIndex("head")),
					cursor.getInt(cursor.getColumnIndex("card")),
					cursor.getInt(cursor.getColumnIndex("friend")),
					cursor.getString(cursor.getColumnIndex("fId")),
					cursor.getString(cursor.getColumnIndex("name")),
					cursor.getString(cursor.getColumnIndex("phone")),
					cursor.getString(cursor.getColumnIndex("qq")),
					cursor.getString(cursor.getColumnIndex("mail")),
					cursor.getString(cursor.getColumnIndex("introduce")));
		}
		cursor.close();
		return card;
	}

	public static ArrayList<Card> userCardList(int lastId, int rollType,
			int objCount) {
		ArrayList<Card> cardList = new ArrayList<Card>();
		Cursor cursor;
		if (rollType == 0) {
			cursor = xQuery("select * from userCard order by id desc", null);
		} else if (rollType == 1) {
			cursor = xQuery(
					"select * from userCard where id > ? order by id desc",
					new String[] { "" + lastId });
			int move = cursor.getCount() - objCount;
			while (cursor.moveToNext() && move-- > 0)
				;
		} else {
			cursor = xQuery(
					"select * from userCard where id < ? order by id desc",
					new String[] { "" + lastId });
		}
		while (cursor.moveToNext() && objCount-- > 0) {
			Card card = new Card(cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getString(cursor.getColumnIndex("nick")),
					cursor.getString(cursor.getColumnIndex("head")),
					cursor.getInt(cursor.getColumnIndex("card")),
					cursor.getInt(cursor.getColumnIndex("friend")),
					cursor.getString(cursor.getColumnIndex("fId")),
					cursor.getString(cursor.getColumnIndex("name")),
					cursor.getString(cursor.getColumnIndex("phone")),
					cursor.getString(cursor.getColumnIndex("qq")),
					cursor.getString(cursor.getColumnIndex("mail")),
					cursor.getString(cursor.getColumnIndex("introduce")));
			cardList.add(card);
		}
		cursor.close();
		return cardList;
	}

	public static void userCardDelete(int cardId, int delType) {

	}

	public static void userApplySet(Apply apply) {
		ContentValues values = new ContentValues();
		values.put("usrId", apply.usrId);
		values.put("usrNick", apply.usrNick);
		values.put("usrHead", apply.usrHead);
		values.put("id", apply.id);
		values.put("time", apply.time);
		values.put("state", apply.state);
		values.put("reason", apply.reason);
		if (userApplyGet(apply.id) == null) {
			xInsert("userApply", null, values);
		} else {
			xUpdate("userApply", values, "id=?", new String[] { "" + apply.id });
		}
	}

	public static Apply userApplyGet(int applyId) {
		Cursor cursor = xQuery("select * from userApply where id = ?",
				new String[] { "" + applyId });
		Apply apply = null;
		if (cursor.moveToNext()) {
			apply = new Apply(cursor.getInt(cursor.getColumnIndex("usrId")),
					cursor.getString(cursor.getColumnIndex("usrNick")),
					cursor.getString(cursor.getColumnIndex("usrHead")),
					cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getLong(cursor.getColumnIndex("time")),
					cursor.getInt(cursor.getColumnIndex("state")),
					cursor.getString(cursor.getColumnIndex("reason")));
		}
		cursor.close();
		return apply;
	}

	public static ArrayList<Apply> userApplyList(int lastId, int rollType,
			int objCount) {
		ArrayList<Apply> applyList = new ArrayList<Apply>();
		Cursor cursor;
		if (rollType == 0) {
			cursor = xQuery("select * from userApply order by id desc", null);
		} else if (rollType == 1) {
			cursor = xQuery(
					"select * from userApply where id > ? order by id desc",
					new String[] { "" + lastId });
			int move = cursor.getCount() - objCount;
			while (cursor.moveToNext() && move-- > 0)
				;
		} else {
			cursor = xQuery(
					"select * from userApply where id < ? order by id desc",
					new String[] { "" + lastId });
		}
		while (cursor.moveToNext() && objCount-- > 0) {
			Apply apply = new Apply(cursor.getInt(cursor
					.getColumnIndex("usrId")), cursor.getString(cursor
					.getColumnIndex("usrNick")), cursor.getString(cursor
					.getColumnIndex("usrHead")), cursor.getInt(cursor
					.getColumnIndex("id")), cursor.getLong(cursor
					.getColumnIndex("time")), cursor.getInt(cursor
					.getColumnIndex("state")), cursor.getString(cursor
					.getColumnIndex("reason")));
			applyList.add(apply);
		}
		cursor.close();
		return applyList;
	}

	public static void userApplyDelete(int applyId, int delType) {
	}

	public static ArrayList<User> userFriendList() {
		ArrayList<User> friends = new ArrayList<User>();
		Cursor cursor = xQuery("select * from user where friend = 3", null);
		Log.d(TAG, "测试好友列表");
		while (cursor.moveToNext()) {
			Log.d(TAG, "test");
			User user = new User(cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getString(cursor.getColumnIndex("nick")),
					cursor.getString(cursor.getColumnIndex("head")),
					cursor.getInt(cursor.getColumnIndex("card")),
					cursor.getInt(cursor.getColumnIndex("friend")));
			friends.add(user);
		}
		cursor.close();
		return friends;
	}
}
