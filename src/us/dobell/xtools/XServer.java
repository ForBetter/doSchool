package us.dobell.xtools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * 服务器助手类
 * 
 * @author xxx
 * 
 */
public class XServer {
	public static final String TAG = "XServer";
	public static final String SERVER_URL = "http://1.doschool.duapp.com";

	public static final int NETWORK_ERROR = -1;

	public static final int LOAD_COUNT = 10;

	public static final int LOAD_OLD = -1;
	public static final int LOAD_START = 0;
	public static final int LOAD_NEW = 1;

	public static final int LOAD_FAILED = -1;
	public static final int LOAD_OVER = 0;
	public static final int LOAD_SUCCESSFUL = 1;

	public static JSONObject format(JSONObject jObj) {
		try {

			int code = Integer.decode(jObj.getString("code"));
			jObj.put("code", code);
			return jObj;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			return networkError();
		}
	}

	public static JSONObject networkError() {
		JSONObject jObj = null;
		try {
			jObj = new JSONObject();
			jObj.put("code", NETWORK_ERROR);
		} catch (JSONException e) {
		}
		return jObj;
	}

	protected static String httpPost(String folder, String function,
			String params) {
		HttpURLConnection connection = null;
		try {
			Log.d(TAG, "call---->" + SERVER_URL + "/" + folder + "/" + function
					+ ".php");
			Log.d(TAG, "with---->" + params);
			URL url = new URL(SERVER_URL + "/" + folder + "/" + function
					+ ".php");
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

	protected static String httpFile(String folder, String function,
			String filePath) {
		// 换行
		String lineEnd = "\r\n";
		// 附加短线
		String twoHyphens = "--";
		// 分界符
		String boundary = "----WebKitFormBoundarydyVd4OxlaM9Ud3oP";
		String fileName = filePath.substring(filePath.lastIndexOf("/"));

		HttpURLConnection connection = null;
		try {
			Log.d(TAG, "call---->" + SERVER_URL + "/" + folder + "/" + function
					+ ".php");
			Log.d(TAG, "with---->" + filePath);
			URL url = new URL(SERVER_URL + "/" + folder + "/" + function
					+ ".php");
			// 打开文件
			FileInputStream fileInputStream = new FileInputStream(new File(
					filePath));
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			// 设置附加参数
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			connection.setConnectTimeout(15000);
			connection.setReadTimeout(15000);
			// 新建流
			DataOutputStream dos = new DataOutputStream(
					connection.getOutputStream());
			// 写文件块信息
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
					+ fileName + "\"" + lineEnd);
			dos.writeBytes("Content-Type: image/jpeg" + lineEnd + lineEnd);
			// 写文件
			int bytesAvailable = fileInputStream.available();
			byte[] buffer = new byte[bytesAvailable];
			int bytesRead = fileInputStream.read(buffer, 0, bytesAvailable);
			while (bytesRead > 0) {
				dos.write(buffer, 0, bytesAvailable);
				bytesAvailable = fileInputStream.available();
				bytesRead = fileInputStream.read(buffer, 0, bytesAvailable);
			}
			fileInputStream.close();
			dos.writeBytes(lineEnd);
			// 写结束分界符
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			// 更新关闭流
			dos.flush();
			dos.close();
			// 开始链接并取得返回数据
			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			StringBuffer sb2 = new StringBuffer();
			String tmp = null;
			while ((tmp = br.readLine()) != null) {
				sb2.append(tmp);
			}
			br.close();
			Log.d(TAG, "return-->" + sb2);
			return sb2.toString();
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
