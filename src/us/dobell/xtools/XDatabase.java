package us.dobell.xtools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class XDatabase {
	public static final String TAG = "XDatabase";
	public static final String DB_NAME = "doSchool";
	public static final int DB_VERSION = 1;

	public static final int LOAD_COUNT = 10;

	public static final int LOAD_OLD = -1;
	public static final int LOAD_START = 0;
	public static final int LOAD_NEW = 1;

	public static final int LOAD_FAILED = -1;
	public static final int LOAD_OVER = 0;
	public static final int LOAD_SUCCESSFUL = 1;

	public static final int DELETE_ONE = 0;
	public static final int DELETE_ALL = 1;

	private static SQLiteDatabase database;

	public static final void openDatabase(Context context, int usrId) {
		XDatabase.database = new XDBHelper(context, usrId)
				.getWritableDatabase();
	}

	public static final void closeDatabase() {
		if (XDatabase.database != null) {
			XDatabase.database.close();
		}
	}

	public static final long xInsert(String table, String nullColumnHack,
			ContentValues values) {
		return database.insert(table, nullColumnHack, values);
	}

	public static final int xDelete(String table, String whereClause,
			String[] whereArgs) {
		return database.delete(table, whereClause, whereArgs);
	}

	public static final int xUpdate(String table, ContentValues values,
			String whereClause, String[] whereArgs) {
		return database.update(table, values, whereClause, whereArgs);
	}

	public static final Cursor xQuery(String sql, String[] selectionArgs) {
		return database.rawQuery(sql, selectionArgs);
	}

	private static final class XDBHelper extends SQLiteOpenHelper {

		public XDBHelper(Context context, int usrId) {
			super(context, DB_NAME + usrId + ".db", null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("create table user(id int primary key,nick varchar,head varchar,card int,friend int);");
			db.execSQL("create table userCard(id int primary key,nick varchar,head varchar,card int,friend int,fId varchar,name varchar,gender varchar,phone varchar,qq varchar,mail varchar,introduce varchar);");
			db.execSQL("create table userApply(usrId int,usrNick varchar,usrHead varchar,id int primary key,time long,state int,reason varchar);");
			db.execSQL("create table microblog(usrId int,usrNick varchar,usrHead varchar,id int primary key,time long,content varchar,root int,images varchar,comment int,transmit int);");
			db.execSQL("create table microblogComment(usrId int,usrNick varchar,usrHead varchar,objId int,objNick varchar,rootMblogId int,rootCommentId int,id int primary key,time long,content varchar);");
			db.execSQL("create table microblogAt(usrId int,usrNick varchar,usrHead varchar,id int,time long,type int,mblogId int,newContent varchar,oldContent varchar);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}
}
