package us.dobell.xtools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 此类将该由线程池加任务队列实现，现有的用AsyncTask实现的将被废弃
 * 
 * @author xxx
 * 
 */
public class XImageView extends ImageView {
	public static final String TAG = "XImageView";

	// 图片的网络URL
	private String imageURL;

	// 判断图片是否被成功下载到
	private boolean isImageLoaded;

	// 如果SD卡不可用则自动设置为禁用SD卡缓存。
	// 设置为true的话会将下载到的图片存入SD卡中。
	// 设置为false的话不会将下载到的图片存入SD卡中。
	private static boolean externalStorageCache;

	// 储存SD卡缓存的文件夹名,如果这个文件夹不存在，则会被创建.
	private static String cacheFolderName;

	// 储存SD卡缓存的文件夹的绝对路径，如果SD卡不可用则会设置为null
	private static String cacheFolderPath;

	// 在图片下载完成时会调用该接口中的函数,onImageLoaded(boolean isImageLoaded);
	// 如果图片下载成功的话则传入参数为true，否则传入参数false
	private OnImageLoadedListener onImageLoadedListener;

	// 在调用loadImage()函数时会将该成员实例化。
	// 加载图片的时候优先加载内存中的缓存（如果存在），
	// 其次加载SD卡（如果可用）中的缓存（如果存在），
	// 如果内存和SD卡（如果可用）中都没有发现缓存,则会从网络上下载图片
	private LoadImageTask loadImageTask;

	// 在loadImageTask完成时并且成功获得图片后会将改成员实例化。
	// 内存中已经存在的相同URL的缓存会被替换。
	// 内存中不存在该URL的缓存的话则会被创建。
	// 如果SD卡（如果可用）中已经存在相同的URL的话，新的图片并不会替换原来的
	// 如果SD卡（如果可用）中不存在该URL的缓存的话，创建该缓存。
	private SaveImageTask saveImageTask;

	// 在调用deleteAllCache()函数时会将改成员实例化。在SD卡中的缓存将会全部被删除
	private static DeleteCacheTask deleteCacheTask;

	// 储存内存缓存的hash表
	private static HashMap<String, SoftReference<Bitmap>> bitmapCache;

	static {
		XImageView.externalStorageCache = true;
		XImageView.cacheFolderName = "/doBell/cache";
		XImageView.bitmapCache = new HashMap<String, SoftReference<Bitmap>>();
		XImageView.deleteCacheTask = null;
	}
	{
		this.imageURL = null;
		this.isImageLoaded = false;
		this.onImageLoadedListener = null;
		this.loadImageTask = null;
		this.saveImageTask = null;
	}

	public XImageView(Context context) {
		super(context);
	}

	public XImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public XImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 获得SD卡缓存的文件夹的绝对路径，如果缓存文件夹不存在则会被创建
	 * 
	 * @return SD卡缓存的文件夹的绝对路径，如果SD卡出问题了则会
	 */
	private static String getCacheFolderPath() {
		String filePath = "";
		String rootpath = "";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			rootpath = Environment.getExternalStorageDirectory().toString();
		} else {
			XImageView.externalStorageCache = false;
			return null;
		}
		filePath = rootpath + XImageView.cacheFolderName;
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return filePath;
	}

	/**
	 * 根据图片的URL从SD卡中获取图片的缓存
	 * 
	 * @param imageURL
	 *            图片的URL
	 * @return 图片的缓存，如果没有发现该图片的缓存的话则返回空值。
	 */
	private static Bitmap getBitmapFromExternalStorage(String imageURL) {
		String fileName = changeImageURLToName(imageURL);
		if (XImageView.cacheFolderPath == null) {
			XImageView.cacheFolderPath = getCacheFolderPath();
			if (XImageView.cacheFolderPath == null) {
				XImageView.externalStorageCache = false;
				return null;
			}
		}
		File file = new File(XImageView.cacheFolderPath, fileName);
		if (file.exists()) {
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(file);
				return BitmapFactory.decodeStream(inputStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (OutOfMemoryError e) {
				// TODO: handle exception
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 将图片的URL转换成他们的文件名
	 * 
	 * @param imageURL
	 *            图片的URL
	 * @return 转换后的文件名
	 */
	private static String changeImageURLToName(String imageURL) {
		try {
			imageURL = imageURL.substring(imageURL.lastIndexOf("?") + 1);
		} catch (Exception e) {
		}
		String name = imageURL.replaceAll(":", "_");
		name = name.replaceAll("//", "_");
		name = name.replaceAll("/", "_");
		name = name.replaceAll("=", "_");
		name = name.replaceAll(",", "_");
		name = name.replaceAll("&", "_");
		name = name.replace(".", "_");
		return name;
	}

	/**
	 * 设置一张图片的网络位置,设置URL之后立刻下载图片
	 * 
	 * @param imageURL
	 *            图片的网络位置
	 */
	public void setImageURL(String imageURL) {
		if (imageURL == null || imageURL.length() == 0) {
			this.setImageBitmap(null);
			if (XImageView.this.onImageLoadedListener != null) {
				XImageView.this.onImageLoadedListener.onImageLoaded(false);
			}
			XImageView.this.isImageLoaded = false;
		}
		this.isImageLoaded = false;
		this.imageURL = imageURL;
		if (this.loadImageTask == null
				|| this.loadImageTask.getStatus() != AsyncTask.Status.RUNNING) {
			this.loadImageTask = new LoadImageTask();
			this.loadImageTask.execute();
		}
	}

	/**
	 * 取消当前正在加载的图片的加载
	 */
	public void cancelLoadImage() {
		if (this.loadImageTask != null) {
			this.loadImageTask.cancel(true);
			this.loadImageTask = null;
		}
	}

	/**
	 * 图片是否被下载到。
	 * 
	 * @return 如果下载到了返回true，如果下载失败返回false
	 */
	public boolean isImageLoaded() {
		return this.isImageLoaded;
	}

	/**
	 * 获取缓存文件的绝对路径
	 * 
	 * @return 缓存文件的绝对路径，如果缓存文件不存在则返回null
	 */
	public String getCacheFileFullPath() {
		File f = new File(XImageView.cacheFolderPath
				+ XImageView.changeImageURLToName(this.imageURL));
		if (f.exists()) {
			return f.getAbsolutePath();
		} else {
			return null;
		}
	}

	/**
	 * 为XImageView设置图片加载的监听器，当图片加载完了之后就会调用监听器里的函数
	 * 
	 * @param onImageLoadedListener
	 *            要设置的监听器
	 */
	public void setOnImageLoadedListener(
			OnImageLoadedListener onImageLoadedListener) {
		this.onImageLoadedListener = onImageLoadedListener;
	}

	/**
	 * 启用SD卡缓存的话会将下载到的图片存入SD卡中。如果沒有SD卡或SD卡出问题了导致SD卡不可用则会自动设置为禁用SD卡缓存
	 */
	public static void enableExternalStorageCache() {
		XImageView.externalStorageCache = true;
	}

	/**
	 * 禁用SD卡缓存的话不会将下载到的图片存入SD卡中
	 */
	public static void disableExternalStorageCache() {
		XImageView.externalStorageCache = false;
	}

	/**
	 * 刪除SD卡及内存中的所有缓存
	 */
	public static void deleteAllCache(Context ctx) {
		if (XImageView.deleteCacheTask == null
				|| XImageView.deleteCacheTask.getStatus() != AsyncTask.Status.RUNNING) {
			XImageView.deleteCacheTask = new DeleteCacheTask(ctx);
			XImageView.deleteCacheTask.execute();
		}
	}

	/**
	 * 设置缓存文件夹的名字，最好是英文名，最好以"/"开头
	 * 
	 * @param folderName
	 *            文件夹的名字
	 */
	public static void setCacheFolderName(String folderName) {
		if (folderName == null) {
			XImageView.cacheFolderName = "/xCache";
			return;
		}
		if (!folderName.startsWith("/")) {
			folderName = "/" + folderName;
		}
		if (folderName.endsWith("/")) {
			folderName = folderName.substring(0, folderName.length() - 1);
		}
		if (folderName.length() == 0) {
			folderName = "/xCache";
		}
		XImageView.cacheFolderName = folderName;
	}

	/**
	 * 获取图片缓存文件夹的名字
	 * 
	 * @return 图片缓存所在的文件夹的名字
	 */
	public static String getCacheFodlerName() {
		return XImageView.cacheFolderName;
	}

	/**
	 * 加载图片的异步线程类，优先加载内存中的缓存，其次加载SD卡中的缓存，内存和SD卡中都没有缓存则从网络上下载图片
	 * 
	 * @author xxx
	 * 
	 */
	private class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(Void... params) {
			Bitmap bitmap = null;
			// 从内存中获取缓存的图片
			if (XImageView.bitmapCache.get(XImageView.this.imageURL) != null) {
				bitmap = XImageView.bitmapCache.get(XImageView.this.imageURL)
						.get();
				if (bitmap != null) {
					Log.d(TAG, "从内存中加载图片");
					return bitmap;
				}
			}
			// 若内存中没有图片的缓存，则查找SD卡中的缓存
			if (XImageView.externalStorageCache) {
				bitmap = getBitmapFromExternalStorage(XImageView.this.imageURL);
				if (bitmap != null) {
					Log.d(TAG, "从SD卡中加载图片");
					return bitmap;
				}
			}
			Log.d(TAG, "从网络下载图片");
			// 内存和SD卡中都没有缓存，从网络上下载图片
			URL url = null;
			InputStream inputStream = null;
			HttpURLConnection urlConnection = null;
			try {
				url = new URL(XImageView.this.imageURL);
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.setConnectTimeout(10000);
				inputStream = urlConnection.getInputStream();
				bitmap = BitmapFactory.decodeStream(inputStream);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (OutOfMemoryError oom) {
				oom.printStackTrace();
				return null;
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
						inputStream = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (urlConnection != null) {
					urlConnection.disconnect();
					urlConnection = null;
				}
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			setImageBitmap(result);
			if (result != null) {
				if (externalStorageCache) {
					if (XImageView.this.saveImageTask == null
							|| XImageView.this.saveImageTask.getStatus() != AsyncTask.Status.RUNNING) {
						new SaveImageTask().execute(result);
					}
				}
				if (XImageView.this.onImageLoadedListener != null) {
					XImageView.this.onImageLoadedListener.onImageLoaded(true);
				}
				XImageView.this.isImageLoaded = true;
			} else {
				if (XImageView.this.onImageLoadedListener != null) {
					XImageView.this.onImageLoadedListener.onImageLoaded(false);
				}
				XImageView.this.isImageLoaded = false;
			}
		}
	}

	/**
	 * 將图片缓存到SD卡的异步线程类，如果SD卡中已经存在缓存的话则直接返回
	 * 
	 * @author xxx
	 * 
	 */
	private class SaveImageTask extends AsyncTask<Bitmap, Void, Void> {

		@Override
		protected Void doInBackground(Bitmap... params) {
			XImageView.bitmapCache.put(imageURL, new SoftReference<Bitmap>(
					params[0]));
			if (XImageView.cacheFolderPath == null) {
				XImageView.cacheFolderPath = getCacheFolderPath();
				if (getCacheFolderPath() == null) {
					XImageView.externalStorageCache = false;
					return null;
				}
			}
			String name = changeImageURLToName(XImageView.this.imageURL);
			File file = new File(XImageView.cacheFolderPath, name);
			if (file.exists()) {
				return null;
			}
			OutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(file);
				params[0].compress(CompressFormat.PNG, 100, outputStream);
				outputStream.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				outputStream = null;
			}
			return null;
		}
	}

	/**
	 * 删除所有的缓存的异步线程类，删除内存和SD卡上的所有的缓存
	 * 
	 * @author xxx
	 * 
	 */
	private static class DeleteCacheTask extends AsyncTask<Void, Void, Void> {
		Context ctx;

		public DeleteCacheTask(Context ctx) {
			this.ctx = ctx;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Toast.makeText(ctx, "正在删除缓存，请耐心等待", Toast.LENGTH_SHORT).show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if (XImageView.cacheFolderPath == null) {
				XImageView.cacheFolderPath = getCacheFolderPath();
				if (getCacheFolderPath() == null) {
					XImageView.externalStorageCache = false;
					return null;
				}
			} else {
				File cacheFolder = new File(XImageView.cacheFolderPath);
				if (cacheFolder.exists()) {
					File[] files = cacheFolder.listFiles();
					for (File f : files) {
						f.delete();
					}
				}
			}
			XImageView.bitmapCache.clear();
			System.gc();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Toast.makeText(ctx, "已成功删除緩存", Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}
	}

	/**
	 * 图片下载的监听器，当图片下载人物完成时会调用该接口对象的onImageLoaded()方法
	 * 
	 * @author xxx
	 * 
	 */
	public interface OnImageLoadedListener {
		/**
		 * 如果图片下载成功的时候调用
		 * 
		 * @param isImageLoaded
		 *            如果图片成功下载到则传入true，如果图片下载失败则传入false
		 */
		public void onImageLoaded(boolean isImageLoaded);
	}
}
