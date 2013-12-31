package us.dobell.doschool.microblog;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.xtools.XObject;
import us.dobell.xtools.XString;

public class Microblog extends XObject {
	public static final int TYPE_PLAZA = 1;
	public static final int TYPE_FRIEND = 2;
	public static final int TYPE_PERSON = 3;
	public static final int TYPE_SELF = 4;

	public int usrId;
	public String usrNick;
	public String usrHead;
	public long time;
	public XString content;
	public int root;
	public ArrayList<String> images;
	public int comment;
	public int transmit;

	public Microblog(int usrId, String usrNick, String usrHead, int id,
			long time, XString content, int root, ArrayList<String> images,
			int comment, int transmit) {
		super.id = id;
		this.usrId = usrId;
		this.usrNick = usrNick;
		this.usrHead = usrHead;
		this.time = time;
		this.content = content;
		this.root = root;
		this.images = images;
		this.comment = comment;
		this.transmit = transmit;
	}

	public Microblog(JSONObject jObj) throws JSONException {
		super.id = jObj.getInt("id");
		this.usrId = jObj.getInt("usrId");
		this.usrNick = jObj.getString("usrNick");
		this.usrHead = jObj.getString("usrHead");
		this.time = jObj.getLong("time");
		this.content = new XString(jObj.getJSONObject("content"));
		this.root = jObj.getInt("root");
		this.images = new ArrayList<String>();
		JSONArray jArr = jObj.getJSONArray("images");
		if (jArr != null) {
			for (int i = 0; i < jArr.length(); i++) {
				images.add(jArr.getString(i));
			}
		}
		this.comment = jObj.getInt("comment");
		this.transmit = jObj.getInt("transmit");
	}
}
