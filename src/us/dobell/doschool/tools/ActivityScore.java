package us.dobell.doschool.tools;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import us.dobell.doschool.R;
import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityScore extends Activity {
	int screenW = 480;
	Runnable getScoreRun;
	Handler handler, h2;
	ListView list;
	String json;
	String pjjd;
	List<List<String>> data;
	LayoutInflater inflater;
	MOnTouchListener imgOnTouchListener;
	MOnScrollListener listOnScrollListener;
	ImageView img;
	boolean down = false;
	RotateAnimation am2, am;

	public void back(View view) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_activity_cjcx);
		am2 = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF,
				0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		am2.setDuration(1000);
		am2.setFillAfter(true);

		am = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF,
				0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		am.setDuration(1000);
		am.setFillAfter(true);

		handler = new Handler();
		h2 = new Handler();
		// json =
		// "[{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u5e38\\u7528\\u8f6f\\u4ef6\",\"xf\":\"1.0\",\"jd\":\"2.3\",\"point\":\"70\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u5e38\\u7528\\u8f6f\\u4ef6\\u5b9e\\u9a8c\",\"xf\":\"1.0\",\"jd\":\"4\",\"point\":\"\\u4f18\\u79c0\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u5927\\u5b66\\u751f\\u804c\\u4e1a\\u751f\\u6daf\\u89c4\\u5212\",\"xf\":\"1.0\",\"jd\":\"4\",\"point\":\"\\u4f18\\u79c0\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u5927\\u5b66\\u82f1\\u8bed\\uff08\\u4e00\\uff09\",\"xf\":\"3.0\",\"jd\":\"2.8\",\"point\":\"77\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u5927\\u5b66\\u8bed\\u6587\",\"xf\":\"2.0\",\"jd\":\"1.3\",\"point\":\"63\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u9ad8\\u7b49\\u6570\\u5b66A\\uff08\\u4e00\\uff09\",\"xf\":\"4.0\",\"jd\":\"1.3\",\"point\":\"60\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u9ad8\\u7ea7\\u8bed\\u8a00\\u7a0b\\u5e8f\\u8bbe\\u8ba1\",\"xf\":\"3.0\",\"jd\":\"2.8\",\"point\":\"77\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u9ad8\\u7ea7\\u8bed\\u8a00\\u7a0b\\u5e8f\\u8bbe\\u8ba1\\u5b9e\\u9a8c\",\"xf\":\"1.0\",\"jd\":\"3.6\",\"point\":\"87\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u8ba1\\u7b97\\u673a\\u5bfc\\u8bba\",\"xf\":\"1.0\",\"jd\":\"3.4\",\"point\":\"\\u826f\\u597d\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u519b\\u4e8b\\u7406\\u8bba\",\"xf\":\"1.0\",\"jd\":\"2.3\",\"point\":\"74\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u7fbd\\u6bdb\\u7403-\\u4ff1\\u4e50\\u90e8\\u5236\",\"xf\":\"1.0\",\"jd\":\"2.7\",\"point\":\"\\u4e2d\\u7b49\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u4e2d\\u56fd\\u8fd1\\u73b0\\u4ee3\\u53f2\\u7eb2\\u8981\",\"xf\":\"2.0\",\"jd\":\"4\",\"point\":\"90\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u8db3\\u7403-\\u4ff1\\u4e50\\u90e8\\u5236\",\"xf\":\"1.0\",\"jd\":\"3.4\",\"point\":\"\\u826f\\u597d\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u5927\\u5b66\\u7269\\u7406B\\uff08\\u4e0a\\uff09\",\"xf\":\"3.0\",\"jd\":\"1.3\",\"point\":\"60\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u5927\\u5b66\\u82f1\\u8bed\\uff08\\u4e8c\\uff09\",\"xf\":\"3.0\",\"jd\":\"3.2\",\"point\":\"80\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u7535\\u8def\\u4e0e\\u6a21\\u7535\",\"xf\":\"3.0\",\"jd\":\"0\",\"point\":\"33\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u7535\\u8def\\u4e0e\\u6a21\\u7535\\u5b9e\\u9a8c\",\"xf\":\"0.5\",\"jd\":\"2.7\",\"point\":\"\\u4e2d\\u7b49\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u9ad8\\u7b49\\u6570\\u5b66A\\uff08\\u4e8c\\uff09\",\"xf\":\"4.0\",\"jd\":\"1.3\",\"point\":\"61\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u9ad8\\u7ea7\\u8bed\\u8a00\\u8bfe\\u7a0b\\u8bbe\\u8ba1\",\"xf\":\"1.0\",\"jd\":\"3.4\",\"point\":\"\\u826f\\u597d\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u6c47\\u7f16\\u8bed\\u8a00\\u7a0b\\u5e8f\\u8bbe\\u8ba1\",\"xf\":\"2.0\",\"jd\":\"3.2\",\"point\":\"84\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u6c47\\u7f16\\u8bed\\u8a00\\u7a0b\\u5e8f\\u8bbe\\u8ba1\\u5b9e\\u9a8c\",\"xf\":\"1.0\",\"jd\":\"3.4\",\"point\":\"\\u826f\\u597d\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u601d\\u60f3\\u9053\\u5fb7\\u4fee\\u517b\\u4e0e\\u6cd5\\u5f8b\\u57fa\\u7840\",\"xf\":\"3.0\",\"jd\":\"4\",\"point\":\"93\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u7b97\\u6cd5\\u8ba1\\u7b97\\u53ca\\u5176\\u9ad8\\u6548\\u5b9e\\u73b0\",\"xf\":\"2.0\",\"jd\":\"4\",\"point\":\"\\u4f18\\u79c0\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u7b97\\u6cd5\\u8bbe\\u8ba1\\u4e0e\\u5206\\u6790\",\"xf\":\"2.0\",\"jd\":\"3.4\",\"point\":\"\\u826f\\u597d\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u7fbd\\u6bdb\\u7403-\\u4ff1\\u4e50\\u90e8\\u5236\",\"xf\":\"1.0\",\"jd\":\"0\",\"point\":\"\\u4e0d\\u53ca\\u683c\"}]";
		json = "{\"msg\":\"SUCCESS\",\"cj_info\":[{\"xn\":\"2011-2012\",\"xq\":\"1\",\"subname\":\"\\u5e38\\u7528\\u8f6f\\u4ef6\",\"xf\":\"1.0\",\"jd\":\"2.8\",\"point\":\"78\"},{\"xn\":\"2011-2012\",\"xq\":\"1\",\"subname\":\"\\u5e38\\u7528\\u8f6f\\u4ef6\\u5b9e\\u9a8c\",\"xf\":\"1.0\",\"jd\":\"3.4\",\"point\":\"\\u826f\\u597d\"},{\"xn\":\"2011-2012\",\"xq\":\"1\",\"subname\":\"\\u5927\\u5b66\\u751f\\u804c\\u4e1a\\u751f\\u6daf\\u89c4\\u5212\",\"xf\":\"1.0\",\"jd\":\"4\",\"point\":\"\\u4f18\\u79c0\"},{\"xn\":\"2011-2012\",\"xq\":\"1\",\"subname\":\"\\u5927\\u5b66\\u82f1\\u8bed\\uff08\\u4e00\\uff09\",\"xf\":\"3.0\",\"jd\":\"2.3\",\"point\":\"71\"},{\"xn\":\"2011-2012\",\"xq\":\"1\",\"subname\":\"\\u5927\\u5b66\\u8bed\\u6587\",\"xf\":\"2.0\",\"jd\":\"1.3\",\"point\":\"61\"},{\"xn\":\"2011-2012\",\"xq\":\"1\",\"subname\":\"\\u9ad8\\u7b49\\u6570\\u5b66A\\uff08\\u4e00\\uff09\",\"xf\":\"4.0\",\"jd\":\"0\",\"point\":\"39\"},{\"xn\":\"2011-2012\",\"xq\":\"1\",\"subname\":\"\\u9ad8\\u7ea7\\u8bed\\u8a00\\u7a0b\\u5e8f\\u8bbe\\u8ba1\",\"xf\":\"3.0\",\"jd\":\"3.2\",\"point\":\"81\"},{\"xn\":\"2011-2012\",\"xq\":\"1\",\"subname\":\"\\u9ad8\\u7ea7\\u8bed\\u8a00\\u7a0b\\u5e8f\\u8bbe\\u8ba1\\u5b9e\\u9a8c\",\"xf\":\"0.5\",\"jd\":\"4\",\"point\":\"90\"},{\"xn\":\"2011-2012\",\"xq\":\"1\",\"subname\":\"\\u8ba1\\u7b97\\u673a\\u5bfc\\u8bba\",\"xf\":\"1.0\",\"jd\":\"3.4\",\"point\":\"\\u826f\\u597d\"},{\"xn\":\"2011-2012\",\"xq\":\"1\",\"subname\":\"\\u519b\\u4e8b\\u7406\\u8bba\",\"xf\":\"  1.0\",\"jd\":\"2.3\",\"point\":\"74\"},{\"xn\":\"2011-2012\",\"xq\":\"1\",\"subname\":\"\\u6392\\u7403\\u2014\\u7537\\u3001\\u5973\\uff08\\u521d\\u7ea7\\u73ed\\uff09\",\"xf\":\"1.0\",\"jd\":\"3.4\",\"point\":\"\\u826f\\u597d\"},{\"xn\":\"2011-2012\",\"xq\":\"1\",\"subname\":\"\\u5546\\u8d38\\u82f1\\u8bed\\u542c\\u8bf4\",\"xf\":\"2.0\",\"jd\":\"2.8\",\"point\":\"77\"},{\"xn\":\"2011-2012\",\"xq\":\"1\",\"subname\":\"\\u601d\\u60f3\\u9053\\u5fb7\\u4fee\\u517b\\u4e0e\\u6cd5\\u5f8b\\u57fa\\u7840\",\"xf\":\"3.0\",\"jd\":\"3.6\",\"point\":\"88\"},{\"xn\":\"2011-2012\",\"xq\":\"2\",\"subname\":\"\\u5927\\u5b66\\u7269\\u7406B\\uff08\\u4e0a\\uff09\",\"xf\":\"3.0\",\"jd\":\"1.3\",\"point\":\"61\"},{\"xn\":\"2011-2012\",\"xq\":\"2\",\"subname\":\"\\u5927\\u5b66\\u82f1\\u8bed\\uff08\\u4e8c\\uff09\",\"xf\":\"3.0\",\"jd\":\"2.8\",\"point\":\"76\"},{\"xn\":\"2011-2012\",\"xq\":\"2\",\"subname\":\"\\u7535\\u8def\\u539f\\u7406\",\"xf\":\"2.5\",\"jd\":\"1.3\",\"point\":\"62\"},{\"xn\":\"2011-2012\",\"xq\":\"2\",\"subname\":\"\\u7535\\u8def\\u539f\\u7406\\u5b9e\\u9a8c\",\"xf\":\"0.5\",\"jd\":\"3.4\",\"point\":\"\\u826f\\u597d\"},{\"xn\":\"2011-2012\",\"xq\":\"2\",\"subname\":\"\\u9ad8\\u7b49\\u6570\\u5b66A\\uff08\\u4e8c\\uff09\",\"xf\":\"4.0\",\"jd\":\"2.3\",\"point\":\"70\"},{\"xn\":\"2011-2012\",\"xq\":\"2\",\"subname\":\"\\u9ad8\\u7ea7\\u8bed\\u8a00\\u7a0b\\u5e8f\\u8bbe\\u8ba1\\u8bfe\\u7a0b\\u8bbe\\u8ba1\",\"xf\":\"0.5\",\"jd\":\"4\",\"point\":\"92\"},{\"xn\":\"2011-2012\",\"xq\":\"2\",\"subname\":\"\\u6c47\\u7f16\\u8bed\\u8a00\\u7a0b\\u5e8f\\u8bbe\\u8ba1\",\"xf\":\"3.0\",\"jd\":\"1.3\",\"point\":\"62\"},{\"xn\":\"2011-2012\",\"xq\":\"2\",\"subname\":\"\\u6c47\\u7f16\\u8bed\\u8a00\\u7a0b\\u5e8f\\u8bbe\\u8ba1\\u5b9e\\u9a8c\",\"xf\":\"1.0\",\"jd\":\"2.3\",\"point\":\"70\"},{\"xn\":\"2011-2012\",\"xq\":\"2\",\"subname\":\"\\u7bee\\u7403\\u2014\\u7537\\uff08\\u521d\\u7ea7\\u73ed\\uff09\",\"xf\":\"1.0\",\"jd\":\"4\",\"point\":\"\\u4f18\\u79c0\"},{\"xn\":\"2011-2012\",\"xq\":\"2\",\"subname\":\"\\u5510\\u738b\\u6f2b\\u8bdd\",\"xf\":\"2.0\",\"jd\":\"3.6\",\"point\":\"87\"},{\"xn\":\"2011-2012\",\"xq\":\"2\",\"subname\":\"\\u4e2d\\u56fd\\u8fd1\\u73b0\\u4ee3\\u53f2\\u7eb2\\u8981\",\"xf\":\"2.0\",\"jd\":\"3.6\",\"point\":\"85\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u5927\\u5b66\\u7269\\u7406B\\uff08\\u4e0b\\uff09\",\"xf\":\"3.0\",\"jd\":\"0\",\"point\":\"53\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u5927\\u5b66\\u7269\\u7406\\u5b9e\\u9a8cB\",\"xf\":\"2.0\",\"jd\":\"2.3\",\"point\":\"72\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u5927\\u5b66\\u82f1\\u8bed\\uff08\\u4e09\\uff09\",\"xf\":\"3.0\",\"jd\":\"2.3\",\"point\":\"73\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u9ad8\\u7b49\\u6570\\u5b66A\\uff08\\u4e09\\uff09\",\"xf\":\"4.0\",\"jd\":\"1.3\",\"point\":\"60\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u7bee\\u7403-\\u4ff1\\u4e50\\u90e8\\u5236\",\"xf\":\"1.0\",\"jd\":\"4\",\"point\":\"\\u4f18\\u79c0\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u79bb\\u6563\\u6570\\u5b66\\uff08\\u4e0a\\uff09\",\"xf\":\"4.0\",\"jd\":\"2.3\",\"point\":\"72\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u9a6c\\u514b\\u601d\\u4e3b\\u4e49\\u57fa\\u672c\\u539f\\u7406\",\"xf\":\"3.0\",\"jd\":\"2.8\",\"point\":\"75\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u6570\\u5b57\\u903b\\u8f91\",\"xf\":\"3.0\",\"jd\":\"3.2\",\"point\":\"80\"},{\"xn\":\"2012-2013\",\"xq\":\"1\",\"subname\":\"\\u6570\\u5b57\\u903b\\u8f91\\u5b9e\\u9a8c\",\"xf\":\"1.0\",\"jd\":\"3.4\",\"point\":\"\\u826f\\u597d\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u5927\\u5b66\\u7269\\u7406B\\uff08\\u4e0b\\uff09\",\"xf\":\"3.0\",\"jd\":\"1.8\",\"point\":\"68\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u5927\\u5b66\\u82f1\\u8bed\\uff08\\u56db\\uff09\",\"xf\":\"3.0\",\"jd\":\"1.8\",\"point\":\"68\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u9ad8\\u7b49\\u6570\\u5b66A\\uff08\\u4e00\\uff09\",\"xf\":\"4.0\",\"jd\":\"3.2\",\"point\":\"83\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u8ba1\\u7b97\\u673a\\u7ec4\\u6210\\u4e0e\\u4f53\\u7cfb\\u7ed3\\u6784\",\"xf\":\"4.0\",\"jd\":\"2.8\",\"point\":\"76\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u8ba1\\u7b97\\u673a\\u7ec4\\u6210\\u4e0e\\u4f53\\u7cfb\\u7ed3\\u6784\\uff08\\u4e00\\uff09\\u5b9e\\u9a8c\",\"xf\":\"0.5\",\"jd\":\"3.4\",\"point\":\"\\u826f\\u597d\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u79bb\\u6563\\u6570\\u5b66\\uff08\\u4e0b\\uff09\",\"xf\":\"4.0\",\"jd\":\"1.8\",\"point\":\"67\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u9762\\u5411\\u5bf9\\u8c61\\u7a0b\\u5e8f\\u8bbe\\u8ba1\",\"xf\":\"2.0\",\"jd\":\"3.6\",\"point\":\"88\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u9762\\u5411\\u5bf9\\u8c61\\u7a0b\\u5e8f\\u8bbe\\u8ba1\\u5b9e\\u9a8c\",\"xf\":\"1.0\",\"jd\":\"3.6\",\"point\":\"86\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u6570\\u636e\\u7ed3\\u6784\",\"xf\":\"3.0\",\"jd\":\"1.8\",\"point\":\"65\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u6570\\u636e\\u7ed3\\u6784\\u5b9e\\u9a8c\",\"xf\":\"1.0\",\"jd\":\"2.7\",\"point\":\"\\u4e2d\\u7b49\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u6570\\u636e\\u5e93\\u539f\\u7406\",\"xf\":\"3.0\",\"jd\":\"3.6\",\"point\":\"86\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u6570\\u636e\\u5e93\\u539f\\u7406\\u5b9e\\u9a8c\",\"xf\":\"0.5\",\"jd\":\"3.2\",\"point\":\"84\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u6570\\u5b66\\u5efa\\u6a21\\uff08\\u63d0\\u9ad8\\u73ed\\uff09\",\"xf\":\"2.0\",\"jd\":\"3.2\",\"point\":\"81\"},{\"xn\":\"2012-2013\",\"xq\":\"2\",\"subname\":\"\\u7b97\\u6cd5\\u8bbe\\u8ba1\\u4e0e\\u5206\\u6790\",\"xf\":\"2.0\",\"jd\":\"4\",\"point\":\"\\u4f18\\u79c0\"}],\"pjjd\":\"2.61\"}";
		list = (ListView) findViewById(R.id.tools_cjcx_list);
		img = (ImageView) findViewById(R.id.tools_cjcx_morebt_bt);
		data = getJSONData(json);
		list.setAdapter(new MListAdapter());
		listOnScrollListener = new MOnScrollListener();
		// list.setOnScrollListener(listOnScrollListener);
		inflater = LayoutInflater.from(this);

	}

	class MOnScrollListener implements OnScrollListener {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			if (!down && firstVisibleItem + visibleItemCount == totalItemCount)
				down = true;
			if (down && firstVisibleItem + visibleItemCount < totalItemCount) {
				down = false;
				img.setVisibility(View.VISIBLE);
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						img.startAnimation(am);
					}
				});
			}
			// Log.i("aaa"," "+firstVisibleItem + " "+visibleItemCount +" "+
			// totalItemCount);
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub

		}

	}

	class ScoreRunnable implements Runnable {
		View v;

		ScoreRunnable(View v) {
			this.v = v;
		}

		public void run() {
			final RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v
					.getLayoutParams();
			for (; lp.leftMargin < screenW;) {
				lp.leftMargin += 5;
				lp.rightMargin -= 5;
				handler.post(new Runnable() {

					@Override
					public void run() {
						v.setLayoutParams(lp);
					}
				});

				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			handler.post(new Runnable() {

				@Override
				public void run() {
					v.setVisibility(View.INVISIBLE);
				}
			});

		}
	}

	class MOnTouchListener implements OnTouchListener {
		float bx;
		boolean noMove = true;
		int position;

		MOnTouchListener(int p) {
			position = p;
		}

		@Override
		public boolean onTouch(View v, MotionEvent e) {
			// TODO Auto-generated method stub
			switch (e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				bx = e.getX();
				Log.i("mooo", "" + bx);
				break;
			case MotionEvent.ACTION_MOVE:
				Log.i("mooo", "" + e.getX());
				if (e.getX() - bx > 20 && noMove) {
					getScoreRun = new ScoreRunnable(v);
					new Thread(getScoreRun).start();
					noMove = !noMove;
					data.get(position).set(6, "getted");
				}

				break;
			case MotionEvent.ACTION_UP:
				noMove = !noMove;
				break;
			}

			return true;
		}

	}

	class MListAdapter implements ListAdapter {

		@Override
		public int getCount() {
			return data.size() + 2;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public int getItemViewType(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (position == data.size()) {
				Log.i("aaa", "xx");
				View v = inflater.inflate(R.layout.tools_cjcx_morebt, null);

				final ImageView img2 = (ImageView) v
						.findViewById(R.id.tools_cjcx_morebt_bt);

				h2.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						img2.startAnimation(am2);

						Log.i("yaya", "aa");
					}
				});

				return v;

			}

			if (position == data.size() + 1) {
				// 没有数据,暂时不做
				TextView xqjd, xnjd, zjd, pm;
				ImageView tp = (ImageView) findViewById(R.id.tools_cjcx_more_tp);
				View v = inflater.inflate(R.layout.tools_cjcx_more, null);
				xqjd = (TextView) v.findViewById(R.id.tools_cjcx_more_xqjd);
				xnjd = (TextView) v.findViewById(R.id.tools_cjcx_more_xnjd);
				zjd = (TextView) v.findViewById(R.id.tools_cjcx_more_zjd);
				pm = (TextView) v.findViewById(R.id.tools_cjcx_more_paiming);

				return v;
			}

			View v = inflater.inflate(R.layout.tools_cjcx_list_item, null);
			TextView name, cj, score;
			ImageView img;
			img = (ImageView) v.findViewById(R.id.tools_cjcx_getbt);
			name = (TextView) v.findViewById(R.id.tools_cjcx_item_name);
			cj = (TextView) v.findViewById(R.id.tools_cjcx_item_text_cj);
			score = (TextView) v.findViewById(R.id.tools_cjcx_score);

			if (data.get(position).get(0).equals("seg")) {
				img.setVisibility(View.GONE);
				name.setText("学年" + data.get(position + 1).get(0));
				name.setTextSize(15);
				cj.setTextSize(15);
				cj.setText("学期" + data.get(position + 1).get(1));
				score.setVisibility(View.GONE);
			} else {
				// img.setVisibility(View.GONE);

				if (data.get(position).get(6).equals("notgetted")) {
					img.setOnTouchListener(new MOnTouchListener(position));
				} else {
					img.setVisibility(View.GONE);
				}

				name.setText(data.get(position).get(2));
				name.setTextSize(22);
				cj.setTextSize(22);
				cj.setVisibility(View.GONE);
				score.setTextSize(22);
				score.setText(data.get(position).get(5));
			}

			return v;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {

		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {

		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			return false;
		}

	}

	private List<List<String>> getJSONData(String s) {
		List<List<String>> dataList = new ArrayList<List<String>>();
		try {
			JSONObject jso = new JSONObject(s);
			JSONArray ary = jso.getJSONArray("cj_info");
			boolean flag = true;
			int position;
			// 反向加入，保证最新的课程在前
			for (int i = ary.length() - 1; i >= 0; i--) {
				dataList.add(new ArrayList<String>());
				position = dataList.size() - 1;
				// xq不一样则添加一个标志
				if (i == ary.length() - 1
						|| !ary.getJSONObject(i)
								.getString("xq")
								.equals(ary.getJSONObject(i + 1)
										.getString("xq"))) {
					if (position != 0)
						flag = false;
					dataList.get(position).add("seg");
					dataList.add(new ArrayList<String>());
					position = dataList.size() - 1;
				}
				dataList.get(position)
						.add(ary.getJSONObject(i).getString("xn"));
				dataList.get(position)
						.add(ary.getJSONObject(i).getString("xq"));
				dataList.get(position).add(
						ary.getJSONObject(i).getString("subname"));
				dataList.get(position)
						.add(ary.getJSONObject(i).getString("xf"));
				dataList.get(position)
						.add(ary.getJSONObject(i).getString("jd"));
				dataList.get(position).add(
						ary.getJSONObject(i).getString("point"));

				if (flag)
					dataList.get(position).add("notgetted");
				else
					dataList.get(position).add("getted");

			}
			pjjd = jso.getString("pjjd");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < dataList.size(); i++) {
			Log.i("data", dataList.get(i).get(0));
		}

		return dataList;
	}

}
