package us.dobell.doschool.user;

import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.xtools.XObject;

public class Apply extends XObject {
	public static final String TAG = "Apply";
	public static final int HANDLE_NOT = 1;
	public static final int HANDLE_NEGATIVE = 2;
	public static final int HANDLE_POSITIVE = 3;

	public int usrId;
	public String usrNick;
	public String usrHead;
	public long time;
	public int state;
	public String reason;

	public Apply(int usrId, String usrNick, String usrHead, int id, long time,
			int state, String reason) {
		super.id = id;
		this.usrId = usrId;
		this.usrNick = usrNick;
		this.usrHead = usrHead;
		this.time = time;
		this.state = state;
		this.reason = reason;
	}

	public Apply(JSONObject jObj) throws JSONException {
		super.id = jObj.getInt("id");
		this.usrId = jObj.getInt("usrId");
		this.usrNick = jObj.getString("usrNick");
		this.usrHead = jObj.getString("usrHead");
		this.time = jObj.getLong("time");
		this.state = jObj.getInt("state");
		this.reason = jObj.getString("reason");
	}
}
