package us.dobell.doschool.microblog;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.xtools.XDatabase;
import us.dobell.xtools.XString;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class MicroblogDatabase extends XDatabase {

	public static void microblogSet(Microblog microblog) {
		try {
			ContentValues values = new ContentValues();
			values.put("usrId", microblog.usrId);
			values.put("usrNick", microblog.usrNick);
			values.put("usrHead", microblog.usrHead);
			values.put("id", microblog.id);
			values.put("time", microblog.time);
			values.put("content", microblog.content.toJSONObject().toString());
			values.put("root", microblog.root);
			JSONArray jArr = new JSONArray();
			for (String s : microblog.images) {
				jArr.put(s);
			}
			values.put("images", jArr.toString());
			values.put("comment", microblog.comment);
			values.put("transmit", microblog.transmit);
			if (microblogGet(microblog.id) == null) {
				xInsert("microblog", null, values);
			} else {
				xUpdate("microblog", values, "id=?", new String[] { ""
						+ microblog.id });
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static Microblog microblogGet(int microblogId) throws JSONException {
		Cursor cursor = xQuery("select * from microblog where id=?",
				new String[] { "" + microblogId });
		Microblog microblog = null;
		if (cursor.moveToNext()) {
			ArrayList<String> images = new ArrayList<String>();
			JSONArray jArr = new JSONArray(cursor.getString(cursor
					.getColumnIndex("images")));
			for (int i = 0; i < jArr.length(); i++) {
				images.add(jArr.getString(i));
			}
			microblog = new Microblog(cursor.getInt(cursor
					.getColumnIndex("usrId")), cursor.getString(cursor
					.getColumnIndex("usrNick")), cursor.getString(cursor
					.getColumnIndex("usrHead")), cursor.getInt(cursor
					.getColumnIndex("id")), cursor.getLong(cursor
					.getColumnIndex("time")), new XString(new JSONObject(
					cursor.getString(cursor.getColumnIndex("content")))),
					cursor.getInt(cursor.getColumnIndex("root")), images,
					cursor.getInt(cursor.getColumnIndex("comment")),
					cursor.getInt(cursor.getColumnIndex("transmit")));
		}
		cursor.close();
		return microblog;
	}

	public static ArrayList<Microblog> microblogList(int objId,
			int contentType, int lastId, int rollType, int objCount) {
		ArrayList<Microblog> microblogList = new ArrayList<Microblog>();
		Cursor cursor = null;
		switch (rollType) {
		case 1:
			if (contentType == Microblog.TYPE_PLAZA) {
				cursor = xQuery(
						"select * from microblog where id > ? order by id desc",
						new String[] { "" + lastId });
			} else if (contentType == Microblog.TYPE_FRIEND) {
				cursor = xQuery(
						"select * from microblog where microblog.id > ? and microblog.usrId in (select user.id from user where friend=3) order by id desc",
						new String[] { "" + lastId });
			} else {
				Log.d(TAG, "获取微博列表" + objId);
				cursor = xQuery(
						"select * from microblog where usrId = ? and id > ? order by id desc",
						new String[] { "" + objId, "" + lastId });
			}
			int move = cursor.getCount() - objCount;
			while (cursor.moveToNext() && move-- > 0)
				;
			break;
		case -1:
			if (contentType == Microblog.TYPE_PLAZA) {
				cursor = xQuery(
						"select * from microblog where id < ? order by id desc",
						new String[] { "" + lastId });
			} else if (contentType == Microblog.TYPE_FRIEND) {
				cursor = xQuery(
						"select * from microblog where microblog.id < ? and microblog.usrId in (select user.id from user where friend=3) order by id desc",
						new String[] { "" + lastId });
			} else {
				Log.d(TAG, "获取微博列表" + objId);
				cursor = xQuery(
						"select * from microblog where usrId = ? and id < ? order by id desc",
						new String[] { "" + objId, "" + lastId });
			}
			break;
		default:
			if (contentType == Microblog.TYPE_PLAZA) {
				cursor = xQuery("select * from microblog order by id desc",
						null);
			} else if (contentType == Microblog.TYPE_FRIEND) {
				cursor = xQuery(
						"select * from microblog where microblog.usrId in (select user.id from user where friend=3) order by id desc",
						null);
			} else {
				Log.d(TAG, "获取微博列表" + objId);
				cursor = xQuery(
						"select * from microblog where usrId = ?  order by id desc",
						new String[] { "" + objId });
			}
			break;
		}
		while (cursor.moveToNext() && objCount-- > 0) {
			try {
				ArrayList<String> images = new ArrayList<String>();
				JSONArray jArr = new JSONArray(cursor.getString(cursor
						.getColumnIndex("images")));
				for (int i = 0; i < jArr.length(); i++) {
					images.add(jArr.getString(i));
				}
				Microblog microblog = new Microblog(cursor.getInt(cursor
						.getColumnIndex("usrId")), cursor.getString(cursor
						.getColumnIndex("usrNick")), cursor.getString(cursor
						.getColumnIndex("usrHead")), cursor.getInt(cursor
						.getColumnIndex("id")), cursor.getLong(cursor
						.getColumnIndex("time")), new XString(new JSONObject(
						cursor.getString(cursor.getColumnIndex("content")))),
						cursor.getInt(cursor.getColumnIndex("root")), images,
						cursor.getInt(cursor.getColumnIndex("comment")),
						cursor.getInt(cursor.getColumnIndex("transmit")));
				microblogList.add(microblog);
			} catch (JSONException e) {
			}
		}
		cursor.close();
		return microblogList;
	}

	public static void microblogDelete(int microblogId, int delType) {
	}

	public static void microblogCommentSet(Comment comment) {
		try {
			ContentValues values = new ContentValues();
			values.put("id", comment.id);
			values.put("usrId", comment.usrId);
			values.put("usrNick", comment.usrNick);
			values.put("usrHead", comment.usrHead);
			values.put("objId", comment.objId);
			values.put("objNick", comment.objNick);
			values.put("rootMblogId", comment.rootMblogId);
			values.put("rootCommentId", comment.rootCommentId);
			values.put("time", comment.time);
			values.put("content", comment.content.toJSONObject().toString());
			if (microblogCommentGet(comment.id) == null) {
				xInsert("microblogComment", null, values);
			} else {
				xUpdate("microblogComment", values, "id=?", new String[] { ""
						+ comment.id });
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static Comment microblogCommentGet(int commentId) {
		Cursor cursor = xQuery("select * from microblogComment where id=?",
				new String[] { "" + commentId });
		Comment comment = null;
		if (cursor.moveToNext()) {
			try {
				comment = new Comment(cursor.getInt(cursor
						.getColumnIndex("usrId")), cursor.getString(cursor
						.getColumnIndex("usrNick")), cursor.getString(cursor
						.getColumnIndex("usrHead")), cursor.getInt(cursor
						.getColumnIndex("objId")), cursor.getString(cursor
						.getColumnIndex("objNick")), cursor.getInt(cursor
						.getColumnIndex("rootMblogId")), cursor.getInt(cursor
						.getColumnIndex("rootCommentId")), cursor.getInt(cursor
						.getColumnIndex("id")), cursor.getLong(cursor
						.getColumnIndex("time")), new XString(new JSONObject(
						cursor.getString(cursor.getColumnIndex("content")))));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		cursor.close();
		return comment;
	}

	public static ArrayList<Comment> microblogCommentList(int rootMblogId,
			int lastId, int rollType, int objCount) {
		ArrayList<Comment> commentList = new ArrayList<Comment>();
		Cursor cursor;
		if (rollType == 0) {
			cursor = xQuery(
					"select * from microblogComment where rootMblogId = ? order by id desc",
					new String[] { "" + rootMblogId });
		} else if (rollType == 1) {
			cursor = xQuery(
					"select * from microblogComment where rootMblogId = ? and id > ? order by id desc",
					new String[] { "" + rootMblogId, "" + lastId });
			int move = cursor.getCount() - objCount;
			while (cursor.moveToNext() && move-- > 0)
				;
		} else {
			cursor = xQuery(
					"select * from microblogComment where rootMblogId = ? and id < ? order by id desc",
					new String[] { "" + rootMblogId, "" + lastId });
		}
		while (cursor.moveToNext() && objCount-- > 0) {
			try {
				Comment comment = new Comment(cursor.getInt(cursor
						.getColumnIndex("usrId")), cursor.getString(cursor
						.getColumnIndex("usrNick")), cursor.getString(cursor
						.getColumnIndex("usrHead")), cursor.getInt(cursor
						.getColumnIndex("objId")), cursor.getString(cursor
						.getColumnIndex("objNick")), cursor.getInt(cursor
						.getColumnIndex("rootMblogId")), cursor.getInt(cursor
						.getColumnIndex("rootCommentId")), cursor.getInt(cursor
						.getColumnIndex("id")), cursor.getLong(cursor
						.getColumnIndex("time")), new XString(new JSONObject(
						cursor.getString(cursor.getColumnIndex("content")))));
				commentList.add(comment);
			} catch (JSONException e) {
			}
		}
		cursor.close();
		return commentList;
	}

	public static void microblogCommentDelete(int commentId, int delType) {

	}

	public static void microblogAtSet(At at) {
		try {
			ContentValues values = new ContentValues();
			values.put("id", at.id);
			values.put("usrId", at.usrId);
			values.put("usrNick", at.usrNick);
			values.put("usrHead", at.usrHead);
			values.put("time", at.time);
			values.put("type", at.type);
			values.put("mblogId", at.mblogId);
			values.put("oldContent", at.oldContent.toJSONObject().toString());
			values.put("newContent", at.newContent.toJSONObject().toString());
			if (microblogAtGet(at.id) == null) {
				xInsert("microblogAt", null, values);
			} else {
				xUpdate("microblogAt", values, "id=?", new String[] { ""
						+ at.id });
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static At microblogAtGet(int atId) {
		Cursor cursor = xQuery("select * from microblogAt where id=?",
				new String[] { "" + atId });
		At at = null;
		if (cursor.moveToNext()) {
			try {
				at = new At(cursor.getInt(cursor.getColumnIndex("id")),
						cursor.getInt(cursor.getColumnIndex("usrId")),
						cursor.getString(cursor.getColumnIndex("usrNick")),
						cursor.getString(cursor.getColumnIndex("usrHead")),
						cursor.getLong(cursor.getColumnIndex("time")),
						cursor.getInt(cursor.getColumnIndex("type")),
						cursor.getInt(cursor.getColumnIndex("mblogId")),
						new XString(new JSONObject(cursor.getString(cursor
								.getColumnIndex("oldContent")))), new XString(
								new JSONObject(cursor.getString(cursor
										.getColumnIndex("newContent")))));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		cursor.close();
		return at;
	}

	public static ArrayList<At> microblogAtList(int lastId, int rollType,
			int objCount) {
		ArrayList<At> atList = new ArrayList<At>();
		Cursor cursor;
		if (rollType == 0) {
			cursor = xQuery("select * from microblogAt order by id desc", null);
		} else if (rollType == 1) {
			cursor = xQuery(
					"select * from microblogAt where id > ? order by id desc",
					new String[] { "" + lastId });
			int move = cursor.getCount() - objCount;
			while (cursor.moveToNext() && move-- > 0)
				;
		} else {
			cursor = xQuery(
					"select * from microblogAt where id < ? order by id desc",
					new String[] { "" + lastId });
		}
		while (cursor.moveToNext() && objCount-- > 0) {
			try {
				At at = new At(cursor.getInt(cursor.getColumnIndex("id")),
						cursor.getInt(cursor.getColumnIndex("usrId")),
						cursor.getString(cursor.getColumnIndex("usrNick")),
						cursor.getString(cursor.getColumnIndex("usrHead")),
						cursor.getLong(cursor.getColumnIndex("time")),
						cursor.getInt(cursor.getColumnIndex("type")),
						cursor.getInt(cursor.getColumnIndex("mblogId")),
						new XString(new JSONObject(cursor.getString(cursor
								.getColumnIndex("oldContent")))), new XString(
								new JSONObject(cursor.getString(cursor
										.getColumnIndex("newContent")))));
				atList.add(at);
			} catch (JSONException e) {
			}
		}
		cursor.close();
		return atList;
	}

	public static void microblogAtDelete(int atId, int delType) {

	}
}
