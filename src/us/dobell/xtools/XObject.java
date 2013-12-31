package us.dobell.xtools;

import org.json.JSONException;
import org.json.JSONObject;

public class XObject {
	public int id;

	public void fromJSONObject(JSONObject jObj) throws JSONException {
		this.id = jObj.getInt("id");
	}

	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObj = new JSONObject();
		jObj.put("id", id);
		return jObj;
	}

	@Override
	public String toString() {
		return "" + id;
	}

}
