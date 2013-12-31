package us.dobell.doschool.tools;

import java.util.ArrayList;
import java.util.List;

import us.dobell.doschool.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityWaiMai extends Activity {
	ListView list;
	List<String> data, names, phones;
	LayoutInflater inflater;
	TextView head;

	public void back(View view) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_activity_waimai);
		inflater = LayoutInflater.from(this);
		list = (ListView) findViewById(R.id.tools_activity_waimai_list);

		head = (TextView) findViewById(R.id.tools_top_text);
		head.setText("外卖信息");
		init();
		list.setAdapter(new MyListAdapter());
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent it = new Intent(ActivityWaiMai.this,
						ActivityWaiMaiDetail.class);
				it.putExtra("data", data.get(arg2));
				it.putExtra("name", names.get(arg2));
				it.putExtra("phone", phones.get(arg2));
				startActivity(it);
			}
		});
	}

	class MyListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final int p = position;
			View v = inflater.inflate(R.layout.tools_waimai_item, null);
			TextView t;
			ImageView im;
			t = (TextView) v.findViewById(R.id.tools_waimai_item_text);
			im = (ImageView) v.findViewById(R.id.tools_waimai_item_image);
			t.setText(names.get(position));
			im.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Uri telUri = Uri.parse("tel:" + phones.get(p));
					Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
					startActivity(intent);
				}
			});
			return v;
		}

	}

	private void init() {
		data = new ArrayList<String>();
		names = new ArrayList<String>();
		phones = new ArrayList<String>();

		names.add("楚味占记");
		phones.add("13965068537");
		data.add("单品名	单价（元）\n鸭脖	5.0/个\n蜜脖	5.0/个\n鸭头	3.5/个\n蜜头	3.5/个\n蜜肫	3.0/个\n鸭腿	6.0/个\n锁骨	20.7/斤\n蜜锁骨	20.7/斤\n孜然锁骨	20.7/斤\n心心相连	2.0/串\n蜜翅	24.7/斤\n鸭爪	33.7/斤\n蜜爪	33.7/斤\n鸭肚	35.7/斤\n鸭肠	35.7/斤\n鸭舌	98.7/斤\n占记棒棒鸡	23.7/斤\n爆炒香辣鸡尖	25.7/斤\n荷香鹅	25.7/斤\n香辣虾	35.7/斤\n占记肺片	38.7/斤\n红油凤爪	25.7/斤\n红油鸡尖	25.7/斤\n烧烤鱿鱼	33.7/斤\n卤牛肉	45.7/斤\n藕片	8.7/斤\n蜜卤花生米	10.7/斤\n香辣豆干	8.7/斤\n川辣海带丝	8.7/斤\n海白菜	8.7/斤\n腐竹	8.7/斤\n");

		names.add("鼎鼎煲仔饭");
		phones.add("15856957959");
		data.add("无菜单");

		names.add("广东潮汕生滚靓粥(请发短信)");
		phones.add("13856935376");
		data.add("\n加料一元加鸡蛋一元，外卖三份起送\n皮蛋廋肉粥        4.5\n木耳瘦肉粥        4.5\n木耳火腿粥        4.5\n海带瘦肉粥        4.5\n海带火腿粥        4.5\n海带皮蛋粥        4.5\n玉米瘦肉粥        4.5\n玉米火腿粥        4.5\n胡萝卜瘦肉粥      4.5\n番茄瘦肉粥        4.5\n腌菜瘦肉粥        4.5\n蘑菇瘦肉粥        4.5\n青瓜瘦肉粥        4.5\n橄榄瘦肉粥        4.5\n紫菜瘦肉粥        4.5\n猪肝粥            4.5\n红枣粥            4.5\n鸡丝皮蛋粥        4.5\n香菇瘦肉粥          5\n瘦肉火腿粥          5\n蘑菇火腿粥          5\n蘑菇鸡丝粥          5\n蘑菇猪肝粥          5\n猪肝皮蛋粥          5\n猪肝瘦肉粥          5\n鸡蛋玉米粥          5\n鸡蛋火腿粥          5\n鸡蛋瘦肉粥          5\n番茄鸡蛋粥          5\n香菇皮蛋粥          5\n香菇火腿粥          5\n香菇鸡丝粥          5\n山药瘦肉粥          5\n皮蛋粥              4\n鸡蛋粥              4\n青菜瘦肉粥          4\n胡萝卜粥          3.5\n青瓜粥            3.5\n青菜白粥            3\n玉米甜粥          4.5\n香蕉甜粥          4.5\n鸡蛋甜粥          4.5\n红枣甜粥          4.5\n菠萝甜粥          4.5\n苹果甜粥          4.5\n雪梨甜粥          4.5\n葡萄甜粥            5\n银耳甜粥            5\n山楂甜粥            5\n哈密瓜甜粥          5\n原味牛奶甜粥        5\n玉米鸡蛋甜粥        5\n");

		names.add("锅巴饭");
		phones.add("13866196639");
		data.add("无菜单");

		names.add("荷香美食馆");
		phones.add("13083089836");
		data.add("火腿肠                  价格（元）\n青瓜火腿肠蒸饭           6\n萝卜干火腿肠蒸饭         6\n酸豆角火腿肠蒸饭         6\n\n肉饼                 价格（元）\n咸鱼肉饼蒸饭             7\n梅菜肉饼蒸饭             7\n萝卜干肉饼蒸饭           7\n香菇肉饼蒸饭             7\n\n腊肠                 价格（元）\n红枣腊肠蒸饭             7\n豆腐腊肠蒸饭             7\n梅菜腊肠蒸饭             7\n萝卜干腊肠蒸饭           7\n\n红烧鸡块             价格（元）\n红枣滑鸡蒸饭             8\n香菇滑鸡蒸饭             8\n红椒滑鸡蒸饭             8\n梅菜滑鸡蒸饭             8\n萝卜干滑鸡蒸饭           8\n\n双腊（腊肉、腊肠）蒸饭   9\n双肠（香肠、火腿肠）蒸饭 8\n\n猪耳                 价格（元）\n梅菜猪耳蒸饭             8\n老干妈猪耳蒸饭（辣）     8\n萝卜干猪耳蒸饭           8\n酸豆角猪耳蒸饭           8\n\n鸡尖                 价格（元）\n梅菜鸡尖蒸饭             7\n青瓜鸡尖蒸饭             7\n萝卜干鸡尖蒸饭           7\n老干妈鸡尖蒸饭（辣）     7\n\n五花肉               价格（元）\n香菇五花肉蒸饭           8\n青瓜五花肉蒸饭           8\n梅菜五花肉蒸饭           8\n老干妈五花肉蒸饭（辣）   8\n\n里脊                 价格（元）\n梅菜里脊蒸饭             8\n酸豆角里脊蒸饭           8\n酸菜里脊蒸饭             8\n青瓜里脊蒸饭             8\n\n肥肠                 价格（元）\n梅菜肥肠蒸饭             9\n老干妈肥肠蒸饭           9\n酸豆角肥肠蒸饭           9\n\n牛肉                 价格（元）\n青瓜牛肉蒸饭             8\n豆腐牛肉蒸饭             8\n酸豆角牛肉蒸饭           8\n老干妈牛肉蒸饭（辣）     8\n梅菜牛肉蒸饭             8\n香菇牛肉蒸饭             8\n\n腊鸭                 价格（元）\n萝卜干腊鸭蒸饭           8\n豆腐腊鸭蒸饭             8\n梅菜腊鸭蒸饭             8\n酸豆角腊鸭蒸饭           8\n\n腊肉                 价格（元）\n老干妈腊肉蒸饭（辣）     9\n萝卜干腊肉蒸饭           9\n梅菜腊肉蒸饭             9\n酸笋腊肉蒸饭             9\n\n红烧排骨             价格（元）\n红椒排骨蒸饭             9\n青瓜排骨蒸饭             9\n香菇排骨蒸饭             9\n梅菜排骨蒸饭             9\n酸豆角排骨蒸饭           9\n萝卜干排骨蒸饭           9\n\n田鸡                价格（元）\n红椒田鸡蒸饭            11\n老干妈田鸡蒸饭          11\n酸菜田鸡蒸饭            11\n梅菜田鸡蒸饭            11\n\n烧鸭                 价格（元）\n红枣烧鸭蒸饭             9\n酸菜烧鸭蒸饭             9\n青瓜烧鸭蒸饭             9\n梅菜烧鸭蒸饭             9\n");

		names.add("华莱士");
		phones.add("18855179199");
		data.add("无菜单，另有座机0551-62351097");

		names.add("太和板面");
		phones.add("18714821457");
		data.add("板面     5/6 元\n大盘鸡   30/40 元(送面两份，由于食品本身原因，不送外卖）\n阜阳格拉条)");

		names.add("沙县小吃（20元起送）");
		phones.add("18715020384");
		data.add("特色小吃                 价格（元）\n美味蒸饺                         4\n香脆混沌（小大）               4/6\n花生酱拌面（小大）             4/6\n葱油拌面（小大）               4/6\n爽口水饺                         6\n肉丝面                           6\n鸡蛋面                           6\n青菜面                           4\n酸菜面                           4\n\n营销汤面                  价格（元）\n特色牛肉面                       7\n鸭腿面                          10\n鸡腿面                           8\n大排面                           8\n大肉面                           8\n乌鸡汤面                         8\n土鸡汤面                         8\n排骨汤面                         8\n猪肝瘦肉鸭肫面                  10\n猪肝瘦肉鸭肫米粉                10\n\n滋补炖汤                 价格（元）\n茶树菇炖排骨汤                   6\n萝卜炖排骨汤                     6\n香菇炖土鸡汤                     6\n花旗参炖乌鸡汤                   6\n\n营养汤混沌               价格（元）\n排骨汤混沌                      10\n土鸡汤混沌                      10\n乌鸡汤混沌                      10\n\n营养套餐                 价格（元）\n肉丝饭                          11\n鸡腿饭                          10\n大肉饭                          10\n肉丝饭                           9\n大排饭                          10\n排骨汤饭                        10\n土鸡汤饭                        10\n乌鸡汤饭                        10\n\n盖浇饭                   价格（元）\n青椒肉丝盖浇饭                   7\n雪菜肉丝盖浇饭                   7\n西红柿炒鸡蛋盖浇饭               7\n青椒牛肉盖浇饭                  10\n\n美味炒类                 价格（元）\n蛋炒饭                           6\n肉丝炒饭                         7\n牛肉炒饭                         8\n蛋炒饭                           6\n肉丝炒面                         7\n牛肉炒面                         8\n蛋炒米粉                         7\n肉丝炒米粉                       7\n牛肉炒米粉                       8\n炸饺                             6\n炸混沌                           6\n");

		names.add("土耳其烤肉");
		phones.add("15209865427");
		data.add("烤肉夹馍        4/5 元\n烤肉鸡蛋夹馍    5/6 元\n烤肉汉堡        5/7 元\n烤肉鸡蛋汉堡    6/8 元\n煎饼烤肉卷      6/8/10 元\n烤肉鸡蛋卷      8/10 元\n烤肉面条        7/8/10 元\n烤肉蛋炒面      8/10 元\n烤肉米饭        7/8/10 元\n烤肉蛋炒饭      8/10 元\n一盒烤肉        12/15/20 元\n");

		names.add("一品香排骨饭");
		phones.add("13856995449");
		data.add("排骨饭类 汤类     价格（元）\n香菇排骨饭              9\n金针菇排骨饭            9\n平菇排骨饭              9\n茶树菇排骨饭            9\n木耳排骨饭              9\n冬瓜排骨饭              9\n海带排骨饭              9\n萝卜排骨饭              9\n玉米排骨饭              9\n莲藕排骨饭              9\n香菇排骨+大排          14\n金针菇排骨+大排        14\n茶树菇排骨+大排        14\n木耳排骨+大排          14\n冬瓜排骨+大排          14\n海带排骨+大排          14\n萝卜排骨+大排          14\n玉米排骨+大排          14\n莲藕排骨+大排          14\n\n大排饭类 汤类     价格（元）\n香菇大排饭              9\n金针菇大排饭            9\n平菇大排饭              9\n茶树菇大排饭            9\n木耳大排饭              9\n冬瓜大排饭              9\n海带大排饭              9\n萝卜大排饭              9\n玉米大排饭              9\n莲藕大排饭              9\n\n排骨荷叶蒸饭      价格（元）\n香菇排骨蒸饭            9\n梅菜排骨蒸饭            9\n酸豆角排骨蒸饭          9\n云耳排骨蒸饭            9\n青瓜排骨蒸饭            9\n酸菜排骨蒸饭            9\n\n大排荷叶蒸饭      价格（元）\n香菇大排蒸饭            9\n梅菜大排蒸饭            9\n酸豆角大排蒸饭          9\n云耳大排蒸饭            9\n萝卜干大排蒸饭          9\n\n五花肉荷叶蒸饭    价格（元）\n梅菜五花肉蒸饭          8\n咸肉五花肉蒸饭          8\n豆腐五花肉蒸饭          8\n青瓜五花肉蒸饭          8\n酸菜五花肉蒸饭          8\n\n烧鸭荷叶蒸饭      价格（元）\n红枣烧鸭蒸饭            8\n云耳烧鸭蒸饭            8\n青瓜烧鸭蒸饭            8\n酸豆角烧鸭蒸饭          8\n酸菜烧鸭蒸饭            8\n\n鸡腿荷叶蒸饭      价格（元）\n梅菜鸡腿蒸饭            8\n香菇鸡腿蒸饭            8\n萝卜干鸡腿蒸饭          8\n\n牛肉荷叶蒸饭      价格（元）\n青瓜牛肉蒸饭            9\n豆腐牛肉蒸饭            9\n酸豆角牛肉蒸饭          9\n梅菜牛肉蒸饭            9\n咸鱼牛肉蒸饭            9\n肥肠荷叶蒸饭      价格（元）\n香菇非常蒸饭            8\n梅菜非常蒸饭            8\n酸豆角肥肠蒸饭          8\n萝卜干肥肠蒸饭          8\n\n鸡翅荷叶蒸饭      价格（元）\n梅菜鸡翅蒸饭            8\n酸豆角鸡翅蒸饭          8\n香菇鸡翅蒸饭            8\n酸菜鸡翅蒸饭            8\n萝卜干鸡翅蒸饭          8\n肉饼荷叶蒸饭      价格（元）\n香菇肉饼蒸饭            6\n梅菜肉饼蒸饭            6\n咸鱼肉饼蒸饭            6\n\n腊肠荷叶蒸饭      价格（元）\n红枣腊肠蒸饭            8\n云耳腊肠蒸饭            8\n梅菜腊肠蒸饭            8\n豆腐腊肠蒸饭            8\n酸菜腊肠蒸饭            8\n萝卜干腊肠蒸饭          8\n\n火腿荷叶蒸饭      价格（元）\n梅菜火腿肠蒸饭          6\n萝卜干火腿肠蒸饭        6\n酸豆角火腿肠蒸饭        6\n青瓜火腿肠蒸饭          6\n\n滑鸡荷叶蒸饭      价格（元）\n香菇滑鸡蒸饭            8\n云耳滑鸡蒸饭            8\n梅菜滑鸡蒸饭            8\n酸菜滑鸡蒸饭            8\n\n腊鸭荷叶蒸饭      价格（元）\n豆腐腊鸭蒸饭            8\n梅菜腊鸭蒸饭            8\n萝卜干腊鸭蒸饭          8\n酸豆角腊鸭蒸饭          8\n咸鱼腊鸭蒸饭            8\n\n里脊荷叶蒸饭      价格（元）\n梅菜里脊蒸饭            7\n云耳里脊蒸饭            7\n酸菜里脊蒸饭            7\n青瓜里脊蒸饭            7\n酸豆角里脊蒸饭          7\n\n鸡尖荷叶蒸饭      价格（元）\n梅菜鸡尖蒸饭            7\n酸菜鸡尖蒸饭            7\n酸豆角鸡尖蒸饭          7\n萝卜干鸡尖蒸饭          7\n");

		names.add("战斗鸡排");
		phones.add("15256089025");
		data.add("食品名              单价（元）\n超大战斗鸡排        11\n香酥脆骨             7\n黄金腿排            12\n爆浆起司鸡排        13\n泡菜鸡排            12\n香辣肉骨团           8\n战斗鸡米花           8\n香酥软骨条           6\n甜不辣               5\n米雪糕               6\n战斗鸡块             7\n普罗旺斯迷迭翅       8\n脆皮全翅             7\n法式玫瑰香鸡腿       6\n台湾香肠             5\n超Q墨鱼丸            3\n玉米可乐饼           5\n紫薯球               5\n甘梅地瓜条           7\n吮指凤颈             8\n意大利芝士条         8\n\n套餐系列             价格（元）\n鸡米花+墨鱼丸        10\n鸡米花+可乐          10\n两个鸡腿             11\n两个全翅             11\n鸡米花+紫薯球+可乐   15\n鸡排+可乐（酸梅汁）  13\n鸡排+两串墨鱼丸      16\n鸡排+鸡米花+可乐     20\n鸡排+鸡块+紫薯球     20\n鸡米花+全翅+鸡块     20\n\n饮品               价格（元）\n原味奶茶           中4  大5\n麦香奶茶           中4  大5\n草莓奶茶           中4  大5\n香芋奶茶           中4  大5\n巧克力奶茶         中4  大5\n咖啡               中4  大5\n金桔百香果汁       中4  大5\n苹果汁             中4  大5\n酸梅汁             中4  大5\n柳橙汁             中4  大5\n西柚汁             中4  大5\n芒果汁             中4  大5\n菠萝汁             中4  大5\n");

		names.add("重庆万友面馆");
		phones.add("13856080156");
		data.add("花刀面类   价格\n花刀素面   5元\n花刀牛肉   7元\n花刀牛肚   6元\n花刀肥肠   6元\n花刀蹄筋   6元\n花刀杂酱   6元\n雪菜肉丝   6元\n土豆肉丝   6元\n豆角肉沫   6元\n花刀鸡丝   6元\n花刀鸡爪   6元\n花刀鸡翅   7元\n花刀鸡腿   9元\n花刀鸡杂   7元\n\n凉面类     价格\n牛肉凉面   6元\n肥肠凉面   5元\n蹄筋凉面   5元\n牛肚凉面   5元\n杂酱凉面   5元\n豆角肉沫   5元\n榨菜肉丝   5元\n素凉面     4元\n\n面类      价格\n红油素面   4元\n红烧牛肉   6元\n红烧肥肠   5元\n红烧蹄筋   5元\n红烧牛肚   5元\n雪菜肉丝   5元\n榨菜肉丝   5元\n番茄鸡蛋   5元\n鸡丝       5元\n鸡翅       6元\n鸡腿       8元\n鸡杂       6元\n \n粉丝、米线类    价格\n素粉丝/米线     4元\n杂酱粉丝/米线   6元\n肉牛粉丝/米线   6元\n肥肠粉丝/米线   6元\n蹄筋粉丝/米线   6元\n\n炒饭类         价格\n鸡蛋炒饭       5元\n扬州炒饭       6元\n杂酱炒饭       6元\n火腿炒饭       5元\n牛肉炒饭       6元\n肥肠炒饭       6元\n蹄筋炒饭       6元\n牛肚炒饭       6元\n豆角肉沫炒饭   6元\n榨菜肉丝炒饭   6元\n雪菜肉丝炒饭   6元\n\n炒面类         价格\n土豆丝炒面     5元\n肥肠炒面       6元\n蹄筋炒面       6元\n牛肚炒面       6元\n青椒肉丝炒面   6元\n土豆肉丝炒面   6元\n榨菜肉丝炒面   6元\n雪菜肉丝炒面   6元\n青椒鸡蛋炒面   6元\n火腿肠炒面     6元\n牛肉炒面       7元\n\n盖饭类         价格\n泡椒鸡杂       10元\n红烧牛肉       9元\n回锅肉         8元\n红烧蹄筋       8元\n红烧牛肚       8元\n青椒肉丝       7元\n土豆肉丝       7元\n豆角肉沫       7元\n榨菜肉丝       7元\n木耳肉丝       7元\n番茄鸡蛋       6元\n木耳鸡蛋       6元\n火腿肠         6元\n酸辣土豆丝     6元\n\n酸辣粉类         价格\n素酸辣粉          5元\n牛肉酸辣粉        7元\n牛肚酸辣粉        7元\n肥肠酸辣粉        7元\n蹄筋酸辣粉        7元\n榨菜肉丝酸辣粉    7元\n雪菜肉丝酸辣粉    7元\n豆角肉沫酸辣粉    7元\n");

	}
}
