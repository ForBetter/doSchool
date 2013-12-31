package us.dobell.doschool.tools;

import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 一个保存了用于在课程表界面中显示的view及详细数据的类
 * 
 * @author Ma Ganxuan
 * 
 */

public class Project {

	public int getToWeek() {
		return toWeek;
	}

	public int getDs() {
		return ds;
	}

	public String getAddress() {
		return address;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public int getFromWeek() {
		return fromWeek;
	}

	public int getStartTime() {
		return startTime;
	}

	public int getTimes() {
		return times;
	}

	public int getColor() {
		return color;
	}

	public int getEndTime() {
		return endTime;
	}

	public int getStartWeek() {
		return startWeek;
	}

	public void setFromWeek(int fromWeek) {
		this.fromWeek = fromWeek;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public void setStartWeek(int startWeek) {
		this.startWeek = startWeek;
	}

	/**
	 * 我需要一个值确定他从什么时候开始计算单双周
	 */
	private static Calendar BEGINDAY = Calendar.getInstance();

	private Calendar nowDay;

	private int ds;

	private int color = Color.GREEN;
	/**
	 * 开始周和结束周
	 */
	private int fromWeek = 1, toWeek = 18;
	/**
	 * 开始时间
	 */
	private int startTime = 2;
	/**
	 * 持续时间
	 */
	private int times = 2;

	private int endTime;
	/**
	 * 第几周的课
	 */
	private int startWeek = 1;

	/**
	 * 课程名称
	 */
	private String name = "明理";
	/**
	 * 上课地点
	 */
	private String address = "襄阳";
	/**
	 * 显示的view
	 */
	private TextView text;

	private String teacherName = "";

	public Project(int color, int fromWeek, int toWeek, int startTime,
			int times, int startWeek, String name, String address,
			String teacherName, int ds) {
		super();
		Log.i("sscolor", "" + color);
		this.color = color;
		this.fromWeek = fromWeek;
		this.toWeek = toWeek;
		this.startTime = startTime;
		this.times = times;
		this.startWeek = startWeek;
		this.name = name;
		this.address = address;
		this.teacherName = teacherName;
		this.nowDay = Calendar.getInstance();
		this.ds = ds;
	}

	public void setContext(final Context context) {
		text = new TextView(context);
		text.setGravity(Gravity.CENTER);
		// 固定文字宽度
		// 文字高度固定，依据持续时间判断
		// text.setMinLines(1*times);
		// text.setMaxLines(1*times);
		text.setText(name + "&" + address);
		text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(context, ActivityKCBDetail.class);
				it.putExtra("fromWeek", Project.this.fromWeek);
				it.putExtra("toWeek", Project.this.toWeek);
				it.putExtra("startTime", Project.this.startTime);
				it.putExtra("times", Project.this.times);
				it.putExtra("startWeek", Project.this.startWeek);
				it.putExtra("name", Project.this.name);
				it.putExtra("address", Project.this.address);
				it.putExtra("teacherName", Project.this.teacherName);
				context.startActivity(it);
				Log.i("kcb", "wawa");
			}
		});

	}

	/**
	 * 当单双周不对应的时候返回一个space View
	 * 
	 * @param c
	 * @return
	 */
	public View getView(View c) {

		// if(nowDay.get(nowDay - BEGINDAY ) 这里可以添加逻辑,如果单双周不对应 返回一个space

		if (c != null)
			return c;
		else {
			return text;
		}
	}

}
