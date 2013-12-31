package us.dobell.doschool.base;

import android.content.Context;
import android.content.SharedPreferences;

public class Values {

	public static final int NO_ID = -1;

	public static final String UPLOAD_FOLDER = "/doSchool/upload";

	public static class User {
		public static us.dobell.doschool.user.User me;

		public static String getCurrentUserFunId(Context context) {
			SharedPreferences sp = context.getSharedPreferences("autoLogin",
					Context.MODE_PRIVATE);
			return sp.getString("funId", null);
		}

		public static void saveAutoLoginInfo(Context context, String funId,
				String usrPasswd) {
			SharedPreferences sp = context.getSharedPreferences("autoLogin",
					Context.MODE_PRIVATE);
			sp.edit().putString("funId", funId).commit();
			sp.edit().putString("usrPasswd", usrPasswd).commit();
		}

		public static String[] getAutoLoginInfo(Context context) {
			SharedPreferences sp = context.getSharedPreferences("autoLogin",
					Context.MODE_PRIVATE);
			String[] info = new String[2];
			info[0] = sp.getString("funId", null);
			info[1] = sp.getString("usrPasswd", null);
			return info;
		}

		public static void clearAutoLoginInfo(Context context) {
			SharedPreferences sp = context.getSharedPreferences("autoLogin",
					Context.MODE_PRIVATE);
			sp.edit().clear().commit();
		}

		public static void saveFunPasswd(Context context, String funId,
				String funPasswd) {
			SharedPreferences sp = context.getSharedPreferences(funId,
					Context.MODE_PRIVATE);
			sp.edit().putString("funPasswd", funPasswd).commit();
		}

		public static String getFunPasswd(Context context, String funId) {
			SharedPreferences sp = context.getSharedPreferences(funId,
					Context.MODE_PRIVATE);
			return sp.getString("funPasswd", null);
		}

		public static void saveUsrPasswd(Context context, String funId,
				String appPasswd) {
			SharedPreferences sp = context.getSharedPreferences(funId,
					Context.MODE_PRIVATE);
			sp.edit().putString("appPasswd", appPasswd).commit();
		}

		public static String getUsrPasswd(Context context, String funId) {
			SharedPreferences sp = context.getSharedPreferences(funId,
					Context.MODE_PRIVATE);
			return sp.getString("appPasswd", null);
		}
	}

	public static class Message {
		public static String devId;

	}
}
