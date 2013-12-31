package us.dobell.doschool.user;

import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.xtools.XObject;
import android.util.Log;

public class Card extends XObject {
	public String nick;
	public String head;
	public int card;
	public int friend;
	public String fId;
	public String name;
	public String phone;
	public String qq;
	public String mail;
	public String introduce;

	public Card(int id, String nick, String head, int card, int friend,
			String fId, String name, String phone, String qq, String mail,
			String introduce) {
		super.id = id;
		this.nick = nick;
		this.head = head;
		Log.d("xx", "" + card);
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
		Log.d("xx", "" + friend);
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
		this.fId = fId;
		this.name = name;
		this.phone = phone;
		this.qq = qq;
		this.mail = mail;
		this.introduce = introduce;
	}

	public Card(JSONObject jObj) throws JSONException {
		super.id = jObj.getInt("id");
		this.nick = jObj.getString("nick");
		this.head = jObj.getString("head");
		Log.d("xx", "" + card);
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
		Log.d("xx", "" + friend);
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
		this.fId = jObj.getString("fId");
		this.name = jObj.getString("name");
		this.phone = jObj.getString("phone");
		this.qq = jObj.getString("qq");
		this.mail = jObj.getString("mail");
		this.introduce = jObj.getString("introduce");
	}
}
