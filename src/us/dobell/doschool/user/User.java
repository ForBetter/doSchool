package us.dobell.doschool.user;

import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.xtools.XObject;

public class User extends XObject {
	public static final String TAG = "User";

	public String nick;
	public String head;
	public int card;
	public int friend;

	public User(int id, String nick, String head, int card, int friend) {
		super.id = id;
		this.nick = nick;
		this.head = head;
		switch (card) {
		case 2:
			this.card = 4;
			break;
		case 1:
			this.card = 3;
			break;
		case -1:
			this.card = 1;
			break;
		case -2:
			this.card = 2;
			break;
		default:
			this.card = 0;
			break;
		}
		switch (friend) {
		case 2:
			this.friend = 4;
			break;
		case 1:
			this.friend = 3;
			break;
		case -1:
			this.friend = 1;
			break;
		case -2:
			this.friend = 2;
			break;
		default:
			this.friend = 0;
			break;
		}
	}

	public User(JSONObject jObj) throws JSONException {
		super.id = jObj.getInt("id");
		this.nick = jObj.getString("nick");
		this.head = jObj.getString("head");
		this.card = jObj.getInt("card");
		this.friend = jObj.getInt("friend");
		switch (this.card) {
		case 2:
			this.card = 4;
			break;
		case 1:
			this.card = 3;
			break;
		case -1:
			this.card = 1;
			break;
		case -2:
			this.card = 2;
			break;
		default:
			this.card = 0;
			break;
		}
		switch (this.friend) {
		case 2:
			this.friend = 4;
			break;
		case 1:
			this.friend = 3;
			break;
		case -1:
			this.friend = 1;
			break;
		case -2:
			this.friend = 2;
			break;
		default:
			this.friend = 0;
			break;
		}
	}
}
