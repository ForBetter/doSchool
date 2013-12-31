package us.dobell.doschool.microblog;

import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.xtools.XObject;
import us.dobell.xtools.XString;

public class At extends XObject {
	public static final String TAG = "At";
	public static final int TYPE_MICROBLOG_AT = 1;
	public static final int TYPE_MICROBLOG_COMMENT = 2;
	public static final int TYPE_MICROBLOG_TRANSMIT = 3;
	public static final int TYPE_MICROBLOG_DELETE = 4;
	public static final int TYPE_COMMENT_AT = 5;
	public static final int TYPE_COMMENT_COMMENT = 6;
	public static final int TYPE_COMMENT_DELETE = 7;

	public int usrId;
	public String usrNick;
	public String usrHead;
	public long time;
	public int type;
	public int mblogId;
	public XString oldContent;
	public XString newContent;

	public At(int id, int usrId, String usrNick, String usrHead, long time,
			int type, int mblogId, XString oldContent, XString newContent) {
		super.id = id;
		this.usrId = usrId;
		this.usrNick = usrNick;
		this.usrHead = usrHead;
		this.time = time;
		this.type = type;
		this.mblogId = mblogId;
		this.oldContent = oldContent;
		this.newContent = newContent;
	}

	public At(JSONObject jObj) throws JSONException {
		super.id = jObj.getInt("id");
		this.usrId = jObj.getInt("usrId");
		this.usrNick = jObj.getString("usrNick");
		this.usrHead = jObj.getString("usrHead");
		this.time = jObj.getLong("time");
		this.type = jObj.getInt("type");
		this.mblogId = jObj.getInt("mblogId");
		this.oldContent = new XString(jObj.getJSONObject("oldContent"));
		this.newContent = new XString(jObj.getJSONObject("newContent"));
	}
}
