package us.dobell.doschool.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class MRemoteServer {

	public static final String TAG = "RemoteServer";
	public static final String doSchool = "http://1.doschool.duapp.com/";
	public static final int SUCCESS = 1;
	public static final int NETWORK_ERROR = 0;
	public static final int LOGICAL_ERROR = -1;
	public static final int ROLL_REFRESH = 1;
	public static final int ROLL_FIRST = 0;
	public static final int ROLL_LOAD = -1;

	/**
	 * public String getAddress() { return address; } public String getName(){
	 * return name; } public int getFromWeek() { return fromWeek; } public int
	 * getStartTime() { return startTime; } public int getTimes() { return
	 * times; } public int getEndTime() { return endTime; } public int
	 * getStartWeek() { return startWeek; }
	 * 
	 * @param id
	 * @param list
	 */

	// return-->[{"title":"\u201c\u9605\u8bfb\u7ecf\u5178\u201d\u7cfb\u5217\u6d3b\u52a8\u7b2c\u5341\u4e03\u573a\u2014\u2014\u4f5c\u5bb6\u4e13\u573a","time":"2013-09-11 09:54:49","link":"\/n\/2013-09-11\/261242.shtml

	public static List<List<String>> getGongao(Context context, Handler handler) {
		List<List<String>> list = new ArrayList<List<String>>();

		String ss = MRemoteServer.post("gonggao/getData", "");

		try {
			JSONArray ary = new JSONArray(ss);
			for (int i = 0; i < ary.length(); i++) {

				list.add(new ArrayList<String>());
				JSONObject o = ary.getJSONObject(i);
				list.get(i).add(o.getString("title"));
				list.get(i).add(o.getString("text"));
				list.get(i).add(o.getString("add_date"));
				list.get(i).add(o.getString("position"));

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return list;
	}

	public static List<List<String>> getXSJZ_1(Context context, Handler handler) {
		List<List<String>> list = new ArrayList<List<String>>();

		String ss = MRemoteServer.post("tools/xsjz_1", "");

		try {
			JSONArray ary = new JSONArray(ss);
			for (int i = 0; i < ary.length(); i++) {

				list.add(new ArrayList<String>());
				JSONObject o = ary.getJSONObject(i);
				list.get(i).add(o.getString("title"));
				list.get(i).add(o.getString("time"));
				list.get(i).add(o.getString("link"));

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return list;
	}

	public static void deleteKCBListFromDB(String id) {
		SQLiteDatabase db;
		try {
			db = SQLiteDatabase
					.openOrCreateDatabase(
							Environment.getExternalStorageDirectory()
									+ "/kcb.db", null);
		} catch (Exception e) {
			return;
		}
		try {
			db.execSQL("DROP TABLE " + id);
		} catch (Exception e) {
			db.close();
			e.printStackTrace();
		}
		db.close();
	}

	public static void saveKCBListFromDB(String id, List<Project> list) {
		SQLiteDatabase db;
		Cursor cursor;

		try {
			db = SQLiteDatabase
					.openOrCreateDatabase(
							Environment.getExternalStorageDirectory()
									+ "/kcb.db", null);
			cursor = db
					.rawQuery(
							"select name from sqlite_master where type='table' order by name",
							null);
		} catch (Exception e) {
			return;
		}
		boolean isExist = false;
		while (cursor.moveToNext()) {
			if (id.equals(cursor.getString(0)))
				isExist = true;
		}
		if (!isExist) {
			db.execSQL("create table " + id + "(name varchar ,"
					+ "teacherName varchar ," + "addname varchar,"
					+ "fromWeek  tinyint," + "toWeek tinyint,"
					+ "startWeek tinyint," + "startTime tinyint,"
					+ "times tinyint," + "endTime tinyint," + "color int,"
					+ "ds tinyint )");
		}
		for (int i = 0; i < list.size(); i++) {
			db.execSQL("insert into " + id + " values ('"
					+ list.get(i).getName() + "','" + // 0
					list.get(i).getTeacherName() + "','" + // 1
					list.get(i).getAddress() + "','" + // 2
					list.get(i).getFromWeek() + "','" + // 3
					list.get(i).getToWeek() + "','" + // 4
					list.get(i).getStartWeek() + "','" + // 5
					list.get(i).getStartTime() + "','" + // 6
					list.get(i).getTimes() + "','" + // 7
					list.get(i).getEndTime() + "','" + // 8
					list.get(i).getColor() + "','" + // 9
					list.get(i).getDs() + "')"); // 10
		}
		cursor.close();
		db.close();
	}

	public static List<Project> getKCBListFromDB(String id) {
		SQLiteDatabase db;
		Cursor cursor;
		try {
			db = SQLiteDatabase
					.openOrCreateDatabase(
							Environment.getExternalStorageDirectory()
									+ "/kcb.db", null);
			cursor = null;
		} catch (Exception e) {
			return null;
		}
		try {
			cursor = db.rawQuery("select * from " + id, null);
		} catch (Exception e) {
			db.close();
			return null;
		}
		List<Project> list = new ArrayList<Project>();
		while (cursor.moveToNext()) {
			list.add(new Project(cursor.getInt(9), cursor.getInt(3), cursor
					.getInt(4), cursor.getInt(6), cursor.getInt(7), cursor
					.getInt(5), cursor.getString(0), cursor.getString(2),
					cursor.getString(1), cursor.getInt(10)));
		}
		cursor.close();
		db.close();

		return list;
	}

	public static List<Project> getKCBList(String id, String pwd,
			Context context, Handler handler) {
		Calendar now = Calendar.getInstance();

		String ss;
		String xh = id, pw = pwd, xn = "2013-2014", xq = "1";

		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH);
		if (month + 1 > 7) {
			xn = "" + year + "-" + (year + 1);
			xq = "1";
		} else {
			xn = "" + (year - 1) + "-" + (year);
			xq = "2";
		}

		alert(context, "正在连接..", Toast.LENGTH_LONG, handler);
		Log.i("year", xn);
		ss = post("tools/getClass", "xh=" + xh + "&pw=" + pw + "&xn=" + xn
				+ "&xq=" + xq);

		if (ss == null) {
			alert(context, "超时！正在第一次重连..", Toast.LENGTH_LONG, handler);
			ss = post("tools/getClass", "xh=" + xh + "&pw=" + pw + "&xn=" + xn
					+ "&xq=" + xq);
		}

		if (ss == null) {
			alert(context, "超时！正在第二次重连..", Toast.LENGTH_LONG, handler);
			ss = post("tools/getClass", "xh=" + xh + "&pw=" + pw + "&xn=" + xn
					+ "&xq=" + xq);
		}
		if (ss == null) {
			alert(context, "超时！请检查网络环境！", Toast.LENGTH_LONG, handler);
			return null;
		}

		List<Project> list = new ArrayList<Project>();
		String subname = "", teacher = "";
		int t;
		/*
		 * int clo[] = { R.drawable.kcb_pro_bc1, R.drawable.kcb_pro_bc2,
		 * R.drawable.kcb_pro_bc3, R.drawable.kcb_pro_bc4,
		 * R.drawable.kcb_pro_bc5, R.drawable.kcb_pro_bc6, };
		 */

		int clo[] = { 0x008cbf, 0x8fa9d6, 0xf0e63b, 0x8cff92, 0xff9999,
				0xd5d5d5, 0xfff100, };

		try {
			JSONObject fo = new JSONObject(ss);
			// 抛出异常
			if (!fo.getString("msg").equals("SUCCESS")) {

				MRemoteServer.alert(context, "请检查密码是否正确！", 1, handler);
				return null;
			}
			JSONArray ary = fo.getJSONArray("kb");
			for (int i = 0; i < ary.length(); i++) {
				JSONObject o = ary.getJSONObject(i);
				subname = (String) o.get("subname");
				teacher = (String) o.get("teacher");
				int dsWeek;
				JSONObject o2 = o.getJSONObject("info");
				t = o2.getInt("times");
				Log.i("hhhh", "111");
				Log.i("hhhh", t + " nnnn " + subname);
				for (int j = 0; j < t; j++) {
					JSONArray aryWD = o2.getJSONArray("weekday");
					JSONArray aryJC = o2.getJSONArray("jc");
					JSONArray arywk = o2.getJSONArray("week");
					// 还没加
					JSONArray aryds = o2.getJSONArray("ds_week");
					dsWeek = aryds.getInt(j);
					JSONArray aryar = o2.getJSONArray("area");
					if (j > 0
							&& list.get(list.size() - 1).getStartWeek() == aryWD
									.getInt(j)
							&& list.get(list.size() - 1).getAddress()
									.equals(aryar.getString(j))) {
						int l = aryJC.getJSONArray(j).length();

						// 后一个元素加入
						list.get(list.size() - 1).setEndTime(
								aryJC.getJSONArray(j).getInt(l - 1));
						list.get(list.size() - 1).setTimes(
								list.get(list.size() - 1).getEndTime()
										- list.get(list.size() - 1)
												.getStartTime() + 1);
						Log.i("hhhh",
								j + "  " + subname + ":"
										+ list.get(list.size() - 1).getTimes());
					} else {
						int l = aryJC.getJSONArray(j).length();
						list.add(new Project(clo[i % 6], arywk.getJSONArray(j)
								.getInt(0), arywk.getJSONArray(j).getInt(1),
								aryJC.getJSONArray(j).getInt(0), aryJC
										.getJSONArray(j).getInt(l - 1)
										- aryJC.getJSONArray(j).getInt(0) + 1,
								aryWD.getInt(j), subname, aryar.getString(j),
								teacher, dsWeek));
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		saveKCBListFromDB(id, list);
		getKCBListFromDB(id);
		return list;
	}

	public static int NEXT_PAGE = 1;

	public static List<List<String>> getLibSearchList(String word, String type,
			int page, Context context, Handler handler) {
		List<List<String>> list = new ArrayList<List<String>>();
		int count;

		alert(context, "正在连接..", Toast.LENGTH_LONG, handler);
		String ss = post("tools/lib_Booksearch", "word=" + word + "&type="
				+ type + "&page=" + page);
		// Log.i("remoetserver",""+ss);

		if (ss == null) {
			alert(context, "超时！正在第一次重连..", Toast.LENGTH_LONG, handler);
			ss = post("tools/lib_Booksearch", "word=" + word + "&type=" + type
					+ "&page=" + page);
		}
		if (ss == null) {
			alert(context, "超时！正在第二次重连..", Toast.LENGTH_LONG, handler);
			ss = post("tools/lib_Booksearch", "word=" + word + "&type=" + type
					+ "&page=" + page);
		}
		if (ss == null) {
			alert(context, "超时！请检查网络环境！", Toast.LENGTH_LONG, handler);
			return null;
		}

		try {
			JSONObject jsob;
			jsob = new JSONObject(ss);
			count = jsob.getInt("count");
			JSONArray jsary = jsob.getJSONArray("info");

			for (int i = 0; i < count; i++) {
				ArrayList<String> t = new ArrayList<String>();
				// list.add(new ArrayList<String>());
				JSONObject o = jsary.getJSONObject(i);
				t.add(o.getString("name"));
				t.add(o.getString("author"));
				t.add(o.getString("req_number"));
				t.add(o.getString("publisher"));
				t.add(o.getString("pub_date"));
				t.add(o.getString("link"));
				list.add(t);
			}
			NEXT_PAGE = jsob.getInt("next_page");
			Log.i("tttt", "" + NEXT_PAGE);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	public static List<String> getAddressAndStatus(String link,
			Context context, Handler handler) {
		List<String> list = new ArrayList<String>();
		alert(context, "正在连接..", Toast.LENGTH_LONG, handler);
		String s = post("tools/lib_bookinfo", "link=" + link);
		if (s == null) {
			alert(context, "超时！正在第一次重连..", Toast.LENGTH_LONG, handler);
			s = post("tools/lib_bookinfo", "link=" + link);
		}
		if (s == null) {
			alert(context, "超时！正在第二次重连..", Toast.LENGTH_LONG, handler);
			s = post("tools/lib_bookinfo", "link=" + link);
		}
		if (s == null) {
			alert(context, "超时！请检查网络环境！", Toast.LENGTH_LONG, handler);
			return null;
		}

		try {
			JSONArray jary = new JSONArray(s);
			for (int i = 0; i < jary.length(); i++) {
				JSONObject o = jary.getJSONObject(i);
				list.add(o.getString("address"));
				list.add(o.getString("statue"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return list;
	}

	/**
	 * 
	 * @param type
	 *            值为1则返回借阅的没有还，为2则是归还过的
	 * @param pw
	 * @return
	 */
	public static List<List<String>> getMyBook(int type, String xh, String pw,
			Context context, Handler handler) {
		List<List<String>> list = new ArrayList<List<String>>();
		alert(context, "正在连接", Toast.LENGTH_LONG, handler);
		String ss = post("tools/lib_jysearch", "jyorgh=" + type + "&xh=" + xh
				+ "&pw=" + pw);
		if (ss == null) {
			alert(context, "超时！正在第一次重连..", Toast.LENGTH_LONG, handler);
			ss = post("tools/lib_jysearch", "jyorgh=" + type + "&xh=" + xh
					+ "&pw=" + pw);
		}
		if (ss == null) {
			alert(context, "超时！正在第二次重连..", Toast.LENGTH_LONG, handler);
			ss = post("tools/lib_jysearch", "jyorgh=" + type + "&xh=" + xh
					+ "&pw=" + pw);
		}
		if (ss == null) {
			alert(context, "超时！请检查网络环境！", Toast.LENGTH_LONG, handler);
			return null;
		}

		try {
			JSONObject o = new JSONObject(ss);
			if (!o.getString("msg").equals("SUCCEES")) {
				alert(context, "请检查密码是否正确！", Toast.LENGTH_LONG, handler);
				return null;
			}
			JSONArray ary = o.getJSONArray("info");
			if (ary.length() == 0) {
				alert(context, "无借阅记录！", Toast.LENGTH_LONG, handler);
				return null;
			}

			for (int i = 0; i < ary.length(); i++) {
				list.add(new ArrayList<String>());
				JSONObject ob = ary.getJSONObject(i);
				list.get(i).add(ob.getString("book_name"));
				list.get(i).add(ob.getString("address"));
				list.get(i).add(ob.getString("ISBN"));
				list.get(i).add(ob.getString("outtime"));
				list.get(i).add(ob.getString("intime"));
				list.get(i).add(ob.getString("next_times"));
			}

		} catch (JSONException e) {
			return null;
		}
		return list;

	}

	private static void alert(final Context context, final String word,
			final int time, Handler handler) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(context, word, time).show();
			}
		});
	}

	public static String post(String function, String params) {
		HttpURLConnection connection = null;
		try {
			Log.d(TAG, "call---->" + doSchool + function + ".php");
			Log.d(TAG, "with---->" + params);
			URL url = new URL(doSchool + function + ".php");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setConnectTimeout(15000);
			connection.setReadTimeout(15000);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			OutputStreamWriter streamWriter = new OutputStreamWriter(
					connection.getOutputStream(), "UTF-8");
			streamWriter.write(params);
			streamWriter.flush();
			streamWriter.close();

			BufferedReader bufferReader = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String returnString = "";
			String temp;
			while ((temp = bufferReader.readLine()) != null) {
				returnString += temp;
			}
			bufferReader.close();
			Log.d(TAG, "return-->" + returnString);
			if (returnString.startsWith("\ufeff")) {
				returnString = returnString.substring(1);
			}
			return returnString;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, "error--->");
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

	}
}
