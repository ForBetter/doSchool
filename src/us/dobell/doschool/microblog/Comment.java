package us.dobell.doschool.microblog;

import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.xtools.XObject;
import us.dobell.xtools.XString;

public class Comment extends XObject {
	public int usrId;
	public String usrNick;
	public String usrHead;
	public int objId;
	public String objNick;
	public int rootMblogId;
	public int rootCommentId;
	public long time;
	public XString content;

	public Comment(int usrId, String usrNick, String usrHead, int objId,
			String objNick, int rootMblogId, int rootCommentId, int id,
			long time, XString content) {
		super.id = id;
		this.usrId = usrId;
		this.usrNick = usrNick;
		this.usrHead = usrHead;
		this.objId = objId;
		this.objNick = objNick;
		this.rootMblogId = rootMblogId;
		this.rootCommentId = rootCommentId;
		this.time = time;
		this.content = content;
	}

	public Comment(JSONObject jObj) throws JSONException {
		super.id = jObj.getInt("id");
		this.usrId = jObj.getInt("usrId");
		this.usrNick = jObj.getString("usrNick");
		this.usrHead = jObj.getString("usrHead");
		this.objId = jObj.getInt("objId");
		this.objNick = jObj.getString("objNick");
		this.rootMblogId = jObj.getInt("rootMblogId");
		this.rootCommentId = jObj.getInt("rootCommentId");
		this.time = jObj.getLong("time");
		this.content = new XString(jObj.getJSONObject("content"));
	}
}