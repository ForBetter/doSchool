package us.dobell.xtools;

public class XUtils {
	public static final String TAG = "XUtils";
	public static final int INCREASE = 0;
	public static final int DECREASE = 1;

	public static String getTime(long time) {
		long now = System.currentTimeMillis();
		if (now - time < 0) {
			return "来自未来";
		} else {
			float temp = now - time;

			temp /= 1000;
			if (temp < 60) {
				return (int) temp + "秒前";
			}
			temp /= 60;
			if (temp < 60) {
				return (int) temp + "分钟前";
			}
			temp /= 60;
			if (temp < 24) {
				return (int) temp + "小时前";
			}
			temp /= 24;
			if (temp < 30) {
				return (int) temp + "天前";
			}
			if (temp < 365) {
				return (int) temp / 30 + "个月前";
			}
			return "刚刚";
		}
	}
}
