package us.dobell.doschool.microblog;

import org.json.JSONArray;
import org.json.JSONObject;

import us.dobell.xtools.XServer;

/**
 * 编号0x001 微博子系统网络接口
 * 
 * @author xxx
 * 
 */
public class MicroblogServer extends XServer {
	public static final String TYPE = "microblog";

	/**
	 * 通用模块0x00
	 */

	/**
	 * 编号0x0010000 上传微博图片
	 * 
	 * @param imagePath
	 *            微博图片在本地的完整路径
	 * @return <h1>微博图片的名称</h1>
	 * 
	 *         <pre>
	 * 0x00100000	成功
	 * 	"imageNameOnServer"
	 * 0x0010000f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject microblogUploadImage(String imagePath) {
		try {
			return format(new JSONObject(httpFile(TYPE, "MicroblogUploadImage",
					imagePath)));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 微博模块0x01
	 */

	/**
	 * 编号0x0010100 发表或转发微博
	 * 
	 * @param usrId
	 *            用户应用程序ID
	 * @param tranMblogId
	 *            间接转发的微博ID
	 * @param rootMblogId
	 *            根微博ID
	 * @param mblogContent
	 *            微博内容，附上提及的事物的信息列表
	 * @param mblogImages
	 *            微博的图片名称数组
	 * @return <h1>这条微博的详细内容</h1>
	 * 
	 *         <pre>
	 * 0x00101000	成功
	 * 	{
	 * 		"usrId":10001,
	 * 		"usrNick":"XX",
	 * 		"usrHead":"http://headPath",
	 * 		"id":100004,
	 * 		"time":5141432513,
	 * 		"content":
	 * 		{
	 * 			"string":"我擦勒，转发微博",
	 * 			"spans":[]
	 * 		},
	 * 		"root":0,
	 * 		"images":
	 * 		[
	 * 			"http://imagePath1",
	 * 			"http://imagePath2",
	 * 			"http://imagePath3"
	 * 		],
	 * 		"comment":124,
	 * 		"transmit":568
	 * 	}
	 * 0x00101001	用户不存在
	 * 	""
	 * 0x00101002	内容格式不正确
	 * 	""
	 * 0x00101003	图片数量超过限制，或者图片数组格式不正确
	 * 	""
	 * 0x0010100f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject microblogSend(int usrId, int tranMblogId,
			int rootMblogId, JSONObject mblogContent, JSONArray mblogImages) {
		try {
			return format(new JSONObject(httpPost(TYPE, "MicroblogSend",
					"usrId=" + usrId + "&tranMblogId=" + tranMblogId
							+ "&rootMblogId=" + rootMblogId + "&mblogContent="
							+ mblogContent.toString() + "&mblogImages="
							+ mblogImages.toString())));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 编号0x0010101 获取单条微博的详细内容
	 * 
	 * @param usrId
	 *            用户的应用程序ID，在这个函数中暂时没用，不过肯定会有用到的时候的
	 * @param mblogId
	 *            微博的ID
	 * @return <h1>单条微博的详细内容</h1>
	 * 
	 *         <pre>
	 * 0x00101010	成功
	 * 	{
	 * 		"usrId":10001,
	 * 		"usrNick":"XX",
	 * 		"usrHead":"http://dafkjqhtrjwr",
	 * 		"id":100004,
	 * 		"time":5141432513,
	 * 		"content":
	 * 		{
	 * 			"string":"我擦勒，转发微博",
	 * 			"spans":[]
	 * 		},
	 * 		"root":100003,
	 * 		"images":[],
	 * 		"comment":124,
	 * 		"transmit":568
	 * 	}
	 * 0x00101011	微博不存在
	 * 	""
	 * 0x0010101f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject microblogGet(int usrId, int mblogId) {
		try {
			return format(new JSONObject(httpPost(TYPE, "MicroblogGet", "usrId"
					+ usrId + "&mblogId=" + mblogId)));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 编号0x0010102 获取微博列表
	 * 
	 * @param usrId
	 *            用户的应用程序ID
	 * @param objId
	 *            微博列表的类型
	 * @param lastId
	 *            基准微博ID，如果基准微博不存在，则以ID上离基准微博ID最近的符合要求的微博做为基准微博
	 * @param rollType
	 *            滚动类型
	 *            [1=向新的微博滚动，获取比id=lastId的微博更新的count条微博],[0=不滚动，获取最新的count条微博
	 *            ],[-1= 向旧的微博滚动，获取比id=lastId的微博更旧的count条微博]
	 * @param objCount
	 *            获取消息的个数，仅供参考，如果实际个数不够要按实际的数目给，不能报错
	 * @return <h1>微博的详细内容列表</h1>
	 * 
	 *         <pre>
	 * 0x00101020	成功
	 * 	[
	 * 		{
	 * 			"usrId":10001,
	 * 			"usrNick":"XX",
	 * 			"usrHead":"http://headPath",
	 * 			"id":100004,
	 * 			"time":5141432513,
	 * 			"content":
	 * 			{
	 * 				"string":"哈哈哈！",
	 * 				"spans":[]
	 * 			},
	 * 			"root":0,
	 * 			"images":
	 * 			[
	 * 				"http://flkashfjqwrh",
	 * 				"http://sfqwrbxcmznxafasf",
	 * 				"http://fnxncbzmncbsdad"
	 * 			],
	 * 			"comment":1,
	 * 			"transmit":6
	 * 		}
	 * 	]
	 * 0x00101021	用户不存在
	 * 	""
	 * 0x00101022	滚动类型不合法
	 * 	""
	 * 0x00101023	获取内容类型不合法
	 * 	""
	 * 0x0010102f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject microblogList(int usrId, int objId,
			int contentType, int lastId, int rollType, int objCount) {
		try {
			return format(new JSONObject(httpPost(TYPE, "MicroblogList",
					"usrId=" + usrId + "&frdId=" + objId + "&contentType="
							+ contentType + "&lastId=" + lastId + "&rollType="
							+ rollType + "&objCount=" + objCount)));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 编号0x0010103 删除某条微博（对于这条微博我觉得将其内容置成“已删除”比较好，或者在获取一条不存在的微博的时候返回一条无意义微博亦可)
	 * 
	 * @param usrId
	 *            用户应用程序ID
	 * @param mblogId
	 *            微博ID
	 * @param delType
	 *            删除方式 [0=单条删除，删除id为blogId的微博及其所有评论、at消息等],[1=级联删除，
	 *            删除id为blogId的微博以及所有转发该微博的微博及其所有的评论，at消息等]
	 * @return <h1>无</h1>
	 * 
	 *         <pre>
	 * 0x00101030	成功
	 * 	""
	 * 0x00101031	用户不存在
	 * 	""
	 * 0x00101032	微博不存在
	 * 	""
	 * 0x00101033	用户权限不足(拥有权限的用户有：微博主人、权限狗)
	 * 	""
	 * 0x0010103f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject microblogDelete(int usrId, int mblogId, int delType) {
		return networkError();
	}

	/**
	 * 评论模块0x02
	 */

	/**
	 * 编号0x0010200 发布一条评论
	 * 
	 * @param usrId
	 *            用户的应用程序ID
	 * @param objId
	 *            对方用户的应用程序ID
	 * @param rootMblogId
	 *            评论所在的微博的ID
	 * @param rootCommentId
	 *            评论所在的评论的ID,为0时则表示该评论为根评论
	 * @param commentContent
	 *            评论的内容，附上提及的事物的信息列表
	 * @return <h1>评论的详细内容</h1>
	 * 
	 *         <pre>
	 * 0x00102000	成功
	 * 	{
	 * 		"usrId":10004,
	 * 		"usrNick":"motherFucker",
	 * 		"usrHead":"http://dasfqqrjwrewtjh",
	 * 		"objId":10005,
	 * 		"objNick":"shit!!",
	 * 		"rootMblogId":10004,
	 * 		"rootCommentId":10045,
	 * 		"id":12345,
	 * 		"time":1249585123,
	 * 		"content":
	 * 		{
	 * 			"string":"实例评论@XX"，
	 * 			"spans":
	 * 			[
	 * 				{
	 * 					"start":4,
	 * 					"end":7,
	 * 					"where":0x00101,
	 * 					"id":10000
	 * 				}
	 * 			]
	 * 		}
	 * 	}
	 * 0x00102001	用户不存在
	 * 	""
	 * 0x00102002	对方用户不存在
	 * 	""
	 * 0x00102003	根微博不存在
	 * 	""
	 * 0x00102005	内容的格式不合法
	 * 	""
	 * 0x0010200f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject microblogCommentSend(int usrId, int objId,
			int rootMblogId, int rootCommentId, JSONObject commentContent) {
		try {
			return format(new JSONObject(httpPost(TYPE, "MicroblogCommentSend",
					"usrId=" + usrId + "&objId=" + objId + "&rootMblogId="
							+ rootMblogId + "&rootCommentId=" + rootCommentId
							+ "&commentContent=" + commentContent.toString())));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 编号0x0010201 获取一条评论的详细内容
	 * 
	 * @param commentId
	 *            评论的ID
	 * @return <h1>评论的详细内容</h1>
	 * 
	 *         <pre>
	 * 0x00102010	成功
	 * 	{
	 * 		"usrId":10004,
	 * 		"usrNick":"motherFucker",
	 * 		"usrHead":"http://dasfqqrjwrewtjh",
	 * 		"objId":10005,
	 * 		"objNick":"shit!!",
	 * 		"rootMblogId":10004,
	 * 		"rootCommentId":10045,
	 * 		"id":12345,
	 * 		"time":1249585123,
	 * 		"content":
	 * 		{
	 * 			"string":"实例评论@XX"，
	 * 			"spans":
	 * 			[
	 * 				{
	 * 					"start":4,
	 * 					"end":7,
	 * 					"where":0x00101,
	 * 					"id":10000
	 * 				}
	 * 			]
	 * 		}
	 * 	}
	 * 0x00102011	评论不存在
	 * 	""
	 * 0x0010201f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject microblogCommentGet(int commentId) {
		try {
			return format(new JSONObject(httpPost(TYPE, "MicroblogCommentGet",
					"commentId=" + commentId)));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 编号0x0010202 获取某个微博的评论列表
	 * 
	 * @param rootBlogId
	 *            评论列表所在的微博的ID
	 * @param lastId
	 *            基准的评论ID
	 * @param rollType
	 *            滚动类型 [1=向新的评论滚动，获取比id=lastId的评论更新的N条评论],[0=不滚动，获取最新的N条评论],[-1=
	 *            向旧的评论滚动，获取比id=lastId的评论更旧的N条评论]
	 * @param objCount
	 *            获取消息的个数，仅供参考，如果实际个数不够要按实际的数目给，不能报错
	 * @return <h1>评论的详细的内容的数组</h1>
	 * 
	 *         <pre>
	 * 0x00102020	成功
	 * 	[
	 * 		{
	 * 			"usrId":10004,
	 * 			"usrNick":"motherFucker",
	 * 			"usrHead":"http://dasfqqrjwrewtjh",
	 * 			"objId":10005,
	 * 			"objNick":"shit!!",
	 * 			"rootMblogId":10004,
	 * 			"rootCommentId":10045,
	 * 			"id":12345,
	 * 			"time":1249585123,
	 * 			"content":
	 * 			{
	 * 				"string":"实例评论@XX"，
	 * 				"spans":
	 * 				[
	 * 					{
	 * 						"start":4,
	 * 						"end":7,
	 * 						"where":0x00101,
	 * 						"id":10000
	 * 					}
	 * 				]
	 * 			}
	 * 		}
	 * 	]
	 * 0x00102021	微博不存在
	 * 	""
	 * 0x00102022	滚动类型不合法
	 * 	""
	 * 0x0010202f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject microblogCommentList(int rootMblogId, int lastId,
			int rollType, int objCount) {
		try {
			return format(new JSONObject(
					httpPost(TYPE, "MicroblogCommentList", "rootMblogId="
							+ rootMblogId + "&lastId=" + lastId + "&rollType="
							+ rollType + "&objCount=" + objCount)));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 编号0x0010203 删除一条评论
	 * 
	 * @param usrId
	 *            用户的应用程序ID
	 * @param commentId
	 *            要删除的评论ID（删除评论时删除其子评论）
	 * @return <h1>无</h1>
	 * 
	 *         <pre>
	 * 0x00102030	成功
	 * 	""
	 * 0x00102031	用户不存在
	 * 	""
	 * 0x00102032	评论不存在
	 * 	""
	 * 0x00102033	用户没有权限（有权限的用户:微博主人、评论主人、权限狗）
	 * 	""
	 * 0x0010203f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject microblogCommentDelete(int usrId, int commentId) {
		return networkError();
	}

	/**
	 * 0x03 at模块
	 */

	/**
	 * 编号0x0010300 获取一条at的详细内容
	 * 
	 * @param atId
	 *            消息的ID
	 * @return <h1>一条@消息的详细内容</h1>
	 * 
	 *         <pre>
	 * 0x00103000	成功
	 * 	{
	 * 		"usrId":10001,
	 * 		"usrNick":"XX",
	 * 		"usrHead":"http://headPath",
	 * 		"id":1234,
	 * 		"time":1234567890,
	 * 		"type":1,
	 * 		"state":1,
	 * 		"mblogId":10003,
	 * 		"oldContent:
	 * 		{
	 * 			"string":"这里是旧的内容，放被评论的微博、被转发的微博、被评论的评论、at别人的微博、at别人的评论的内容"，
	 * 			"spans":[]
	 * 		},
	 * 		"newContent":
	 * 		{
	 * 			"string":"这里是新的内容，放回复别人的内容（回复微博），回复别人的内容（回复评论），也可以是转发理由，at别人时newContent留空"，
	 * 			"spans":[]
	 * 		}
	 * 	}
	 * 0x00103001	消息不存在
	 * 	""
	 * 0x0010300f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject microblogAtGet(int atId) {
		try {
			return format(new JSONObject(httpPost(TYPE, "MicroblogAtGet",
					"atId=" + atId)));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 编号0x0010301 获取收到的@的列表
	 * 
	 * @param usrId
	 *            用户的应用程序ID
	 * @param lastId
	 *            基准消息的ID,如果基准消息不存在，则以ID上离基准消息ID最近的符合要求的消息做为基准消息
	 * @param rollType
	 *            滚动类型
	 *            [1=向新的消息滚动，获取比id=lastId的消息更新的count条消息],[0=不滚动，获取最新的count条]
	 *            ,[-1= 向旧的消息滚动，获取比id=lastId的消息更旧的count条消息]
	 * @param objCount
	 *            获取消息的个数，仅供参考，如果实际个数不够要按实际的数目给，不能报错
	 * @return <h1>收到的@的列表</h1>
	 * 
	 *         <pre>
	 * 0x00103010	成功
	 * 	{
	 * 		"usrId":10001,
	 * 		"usrNick":"XX",
	 * 		"usrHead":"http://headPath",
	 * 		"id":1234,
	 * 		"time":1234567890,
	 * 		"type":1,
	 * 		"state":1,
	 * 		"mblogId":10003,
	 * 		"oldContent:
	 * 		{
	 * 			"string":"这里是旧的内容，放被评论的微博、被转发的微博、被评论的评论、at别人的微博、at别人的评论的内容"，
	 * 			"spans":[]
	 * 		},
	 * 		"newContent":
	 * 		{
	 * 			"string":"这里是新的内容，放回复别人的内容（回复微博），回复别人的内容（回复评论），也可以是转发理由，at别人时newContent留空"，
	 * 			"spans":[]
	 * 		}
	 * 	}
	 * 0x00103011	用户不存在
	 * 	""
	 * 0x00103012	滚动类型不合法
	 * 	""
	 * 0x00103019	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject microblogAtList(int usrId, int lastId,
			int rollType, int objCount) {
		try {
			return format(new JSONObject(httpPost(TYPE, "MicroblogAtList",
					"usrId=" + usrId + "&lastId=" + lastId + "&rollType="
							+ rollType + "&objCount=" + objCount)));
		} catch (Exception e) {
			return networkError();
		}
	}
}
