package us.dobell.doschool.user;

import org.json.JSONObject;

import us.dobell.xtools.XServer;

/**
 * 编号0x000 账户系统
 * 
 * @author xxx
 * 
 */
public class UserServer extends XServer {
	public static final String TAG = "UserServer";
	public static final String TYPE = "user";

	/**
	 * 通用模块0x00
	 */

	/**
	 * 编号0x0000000 上传用户头像
	 * 
	 * @param headPath
	 *            用户头像在本地的完整路径
	 * @return <h1>头像名称</h1>
	 * 
	 *         <pre>
	 * 0x00000000	成功
	 * 	"headName"
	 * 0x0000000f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject userUploadHead(String headPath) {
		try {
			return format(new JSONObject(httpFile(TYPE, "UserUploadHead",
					headPath)));
		} catch (Exception e1) {
			return networkError();
		}
	}

	/**
	 * 编号0000001 检查一个用户是否是安徽大学的学生
	 * 
	 * @param funId
	 *            用户的教务系统ID
	 * @param funPasswd
	 *            用户的教务系统密码
	 * @return <h1>用户的真实姓名</h1>
	 * 
	 *         <pre>
	 * 0x00000010	成功
	 * 	"用户真实姓名"
	 * 0x00000011	该用户不是安徽大学的学生
	 * 	""
	 * 0x00000012	该用户已经注册
	 * 	""
	 * 0x0000001f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject userCheck(String funId, String funPasswd) {
		try {
			return format(new JSONObject(httpPost(TYPE, "UserCheck", "funId="
					+ funId + "&funPasswd=" + funPasswd)));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 用户模块0x01
	 */

	/**
	 * 编号0x0000100 注册一个新的用户
	 * 
	 * @param funId
	 *            用户的教务系统ID
	 * @param usrName
	 *            用户的真实姓名
	 * @param usrNick
	 *            用户的昵称
	 * @param usrPasswd
	 *            用户的应用程序密码
	 * @param usrHead
	 *            用户的头像名称
	 * @return <h1>用户的应用程序ID</h1>
	 * 
	 *         <pre>
	 * 0x00001000	成功
	 * 	10001
	 * 0x00001001	用户已经注册过
	 * 	-1
	 * 0x0000100f	服务器异常
	 * 	-1
	 * </pre>
	 */
	public static JSONObject userRegister(String funId, String usrName,
			String usrNick, String usrPasswd, String usrHead) {
		try {
			return format(new JSONObject(httpPost(TYPE, "UserRegister",
					"funId=" + funId + "&usrName=" + usrName + "&usrNick="
							+ usrNick + "&usrPasswd=" + usrPasswd + "&usrHead="
							+ usrHead)));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 编号0x0000101 用户登录
	 * 
	 * @param funId
	 *            用户的教务系统ID
	 * @param usrPasswd
	 *            用户的应用程序密码
	 * @return <h1>用户的基本信息</h1>
	 * 
	 *         <pre>
	 * 0x00001010	成功
	 * 	{
	 * 		"id":10001,
	 * 		"nick":"XX",
	 * 		"head":"http://headPath",
	 * 		"card":4,
	 * 		"friend":4
	 * 	}
	 * 0x00001011	用户不存在
	 * 	""
	 * 0x00001012	用户名或密码不正确
	 * 	""
	 * 0x0000101f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject userLogin(String funId, String usrPasswd) {
		try {
			return format(new JSONObject(httpPost(TYPE, "UserLogin", "funId="
					+ funId + "&usrPasswd=" + usrPasswd)));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 编号0x0000102 获取用户的基本信息
	 * 
	 * @param usrId
	 *            己方的应用程序ID
	 * @param objId
	 *            对方的应用程序ID
	 * @return <h1>对方的基本信息</h1>
	 * 
	 *         <pre>
	 * 0x00001020	成功
	 * 	{
	 * 		"id":10001,
	 * 		"nick":"XX",
	 * 		"head":"头像地址",
	 * 		"card":2,
	 * 		"friend":1
	 * 	}
	 * 0x00001021	己方不存在对方不存在
	 * 	""
	 * 0x00001022	己方不存在对方存在
	 * 	""
	 * 0x0000102f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject userGet(int usrId, int objId) {
		try {
			return format(new JSONObject(httpPost(TYPE, "UserGet", "usrId="
					+ usrId + "&objId=" + objId)));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 编号0x0000103 搜索用户的信息
	 * 
	 * @param usrId
	 *            己方应用程序ID
	 * @param objNick
	 *            对方昵称
	 * @return <h1>对方的基本信息数组</h1>
	 * 
	 *         <pre>
	 * 0x00001030	正常
	 * 	[	
	 * 		{
	 * 			"id":10004,
	 * 			"nick":"MLGB",
	 * 			"head":"http://headPath",
	 * 			"card":0,
	 * 			"friend":0
	 * 		}
	 * 	]
	 * 0x00001031	己方用户不存在
	 * 	""
	 * 0x0000103f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject userSearch(int usrId, String objNick) {
		try {
			return format(new JSONObject(httpPost(TYPE, "UserSearch", "usrId="
					+ usrId + "&objNick=" + objNick)));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 名片模块0x02
	 */

	/**
	 * 编号0x0000200 发送用户名片
	 * 
	 * @param usrId
	 *            己方应用程序ID
	 * @param objId
	 *            对方应用程序ID
	 * @return <h1>两个用户的最新的名片关系数值</h1>
	 * 
	 *         <pre>
	 * 0x00002000	成功
	 * 	1
	 * 0x00002001	己方不存在对方不存在
	 * 	-1
	 * 0x00002002	己方不存在对方存在
	 * 	-1
	 * 0x00002003	己方存在对方不存在
	 * 	-1
	 * 0x00002004	已经发送过名片
	 * 3
	 * 0x0000200f	服务器异常
	 * 	-1
	 * </pre>
	 */
	public static JSONObject userCardSend(int usrId, int objId) {
		try {
			return format(new JSONObject(httpPost(TYPE, "UserCardGet", "usrId="
					+ usrId + "&objId=" + objId)));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 编号0x0000201 获取用户名片
	 * 
	 * @param usrId
	 *            己方应用程序ID
	 * @param objId
	 *            对方应用程序ID
	 * @return <h1>对方的名片</h1>
	 * 
	 *         <pre>
	 * 0x00002010	成功
	 * 	{
	 * 		"id":10001,
	 * 		"nick":"XX",
	 * 		"head":"头像地址",
	 * 		"card":2,
	 * 		"friend":2,
	 * 		"fId":"E01114062",
	 * 		"name":"徐晓晓",
	 * 		"phone":"15555196690",
	 * 		"qq":"923414630",
	 * 		"mail":"xerxes7.xx@gmail.com",
	 * 		"introduce":"i am xxx"
	 * 	}
	 * 0x00002011	己方用户不存在
	 * 	""
	 * 0x00002012	对方用户不存在
	 * 	""
	 * 0x00002013	己方用户没有权限
	 * 	""
	 * 0x0000201f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject userCardGet(int usrId, int objId) {
		try {
			return format(new JSONObject(httpPost(TYPE, "UserCardGet", "usrId="
					+ usrId + "&objId=" + objId)));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 编号0x0000202 获取用户收到的名片的列表
	 * 
	 * @param usrId
	 *            用户的应用程序ID
	 * @param lastId
	 *            基准人物ID，如果基准人物不存在，则以ID离基准人物最近的人物作为基准任务
	 * @param rollType
	 *            滚动类型 [1=向新的名片滚动，获取比id=lastId的名片更新的objCount条名片],[0=不滚动，
	 *            获取最新的objCount条],[-1= 向旧的名片滚动，获取比id=lastId的名片更旧的objCount条名片]
	 * @param objCount
	 *            获取消息的个数，仅供参考，如果实际个数不够要按实际的数目给，不能报错
	 * @return <h1>用户收到的名片列表</h1>
	 * 
	 *         <pre>
	 * 0x00002020	成功
	 * 	[
	 * 		{
	 * 			"id":10001,
	 * 			"nick":"XX",
	 * 			"head":"头像地址",
	 * 			"card":2,
	 * 			"friend":2,
	 * 			"fId":"E01114062",
	 * 			"name":"徐晓晓",
	 * 			"phone":"15555196690",
	 * 			"qq":"923414630",
	 * 			"mail":"xerxes7.xx@gmail.com",
	 * 			"introduce":"i am xxx"
	 * 		}
	 * 	]
	 * 0x00002021	用户不存在
	 * 	""
	 * 0x00002022	滚动类型不合法
	 * 	""
	 * 0x0000202f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject userCardList(int usrId, int lastId, int rollType,
			int objCount) {
		try {
			return format(new JSONObject(httpPost(TYPE, "UserCardList",
					"usrId=" + usrId + "&lastId=" + lastId + "&rollType="
							+ rollType + "&objCount=" + objCount)));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 编号0x0000203 更改用户名片
	 * 
	 * @param usrId
	 *            用户的应用程序ID
	 * @param usrNick
	 *            用户的昵称
	 * @param usrHead
	 *            用户的头像地址
	 * @param usrPhone
	 *            用户的手机号码
	 * @param usrQq
	 *            用户的qq号码
	 * @param usrMail
	 *            用户的邮箱地址
	 * @param usrIntroduce
	 *            用户的自我介绍
	 * @return <h1>用户最新的名片</h1>
	 * 
	 *         <pre>
	 * 0x00002030	成功
	 * 	{
	 * 		"id":10001,
	 * 		"nick":"XX",
	 * 		"head":"头像地址",
	 * 		"card":4,
	 * 		"friend":4,
	 * 		"fId":"E01114062",
	 * 		"name":"徐晓晓",
	 * 		"phone":"15555196690",
	 * 		"qq":"923414630",
	 * 		"mail":"xerxes7.xx@gmail.com",
	 * 		"introduce":"i am xxx"
	 * 	}
	 * 0x00002031	用户不存在
	 * 	""
	 * 0x00002032	昵称不符合规范
	 * 	""
	 * 0x0000203f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject userCardUpdate(int usrId, String usrNick,
			String usrHead, String usrPhone, String usrQq, String usrMail,
			String usrIntroduce) {
		try {
			return format(new JSONObject(httpPost(TYPE, "UserCardUpdate",
					"usrId=" + usrId + "&usrNick=" + usrNick + "&usrHead="
							+ usrHead + "&usrPhone=" + usrPhone + "&usrQq="
							+ usrQq + "&usrMail=" + usrMail + "&usrIntroduce="
							+ usrIntroduce)));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 好友模块0x03
	 */

	/**
	 * 编号0x0000300 发送好友申请
	 * 
	 * @param usrId
	 *            己方应用程序ID
	 * @param objId
	 *            对方应用程序ID
	 * @param applyReason
	 *            己方申请理由
	 * @return <h1>两人的最新的好友关系数值</h1>
	 * 
	 *         <pre>
	 * 0x00003000	成功
	 * 	3
	 * 0x00003001	己方用户不存在
	 * 	-1
	 * 0x00003002	对方用户不存在
	 * 	-1
	 * 0x00003003	已经发送过好友申请
	 * 	1
	 * 0x00003009	服务器异常
	 * 	-1
	 * </pre>
	 */
	public static JSONObject userApplySend(int usrId, int objId,
			String applyReason) {
		try {
			return format(new JSONObject(httpPost(TYPE, "UserApplySend",
					"usrId=" + usrId + "&objId=" + objId + "&applyReason="
							+ applyReason)));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 编号0x0000301 获取一条好友申请消息
	 * 
	 * @param applyId
	 *            消息ID
	 * @return <h1>一条好友申请消息的详细内容</h1>
	 * 
	 *         <pre>
	 * 0x00003010	成功
	 * {
	 * 		"usrId":10004,
	 * 		"usrNick":"XX",
	 * 		"usrHead":"http://dashqwehkjj",
	 * 		"id":1234,
	 * 		"time":1243545123,
	 * 		"state":3,
	 * 		"reason":"这里是申请的理由"
	 * 	}
	 * 0x00003011	消息不存在
	 * 	""
	 * 0x0000301f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject userApplyGet(int applyId) {
		try {
			return format(new JSONObject(httpPost(TYPE, "UserApplySend",
					"applyId=" + applyId)));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 编号0x0000302 获取收到的好友申请列表
	 * 
	 * @param usrId
	 *            用户的应用程序ID
	 * @param lastId
	 *            基准的消息ID
	 * @param rollType
	 *            滚动类型 [1=向新的消息滚动，获取比id=lastId的消息更新的objCount条消息],[0=不滚动，
	 *            获取最新的objCount条],[-1= 向旧的消息滚动，获取比id=lastId的消息更旧的objCount条消息]
	 * @param objCount
	 *            获取消息的个数，仅供参考，如果实际个数不够要按实际的数目给，不能报错
	 * @return <h1>收到的好友申请的列表</h1>
	 * 
	 *         <pre>
	 * 0x00003020	成功
	 * 	[
	 * 		{
	 * 			"usrId":10004,
	 * 			"usrNick":"XX",
	 * 			"usrHead":"http://dashqwehkjj",
	 * 			"id":1234,
	 * 			"time":1243545123,
	 * 			"state":3,
	 * 			"reason":"这里是申请的理由"
	 * 		}
	 * 	]
	 * 0x00003021	用户不存在
	 * 	""
	 * 0x00003022	滚动类型不合法
	 * 	""
	 * 0x0000302f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject userApplyList(int usrId, int lastId, int rollType,
			int objCount) {
		try {
			return format(new JSONObject(httpPost(TYPE, "UserApplyList",
					"usrId=" + usrId + "&lastId=" + lastId + "&rollType="
							+ rollType + "&objCount=" + objCount)));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 编号0x0000303 处理好友关系
	 * 
	 * @param applyId
	 *            对方应用程序ID
	 * @param handleCode
	 *            关系处理值 [0=取消好友关系，清空申请],[1=成为好友]
	 * @return <h1>对方用户的简要信息</h1>
	 * 
	 *         <pre>
	 * 0x00003030	成功
	 * 	{
	 * 		"id":10001,
	 * 		"nick":"XX",
	 * 		"head":"头像地址",
	 * 		"card":2,
	 * 		"friend":1
	 * 	}
	 * 0x00003031	handleCode不合法
	 * 	""
	 * 0x0000303f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject userApplyHandle(int applyId, int handleCode) {
		try {
			return format(new JSONObject(httpPost(TYPE, "UserApplyHandle",
					"applyId=" + applyId + "&handleCode=" + handleCode)));
		} catch (Exception e) {
			return networkError();
		}
	}

	/**
	 * 编号0x0000304 获取好友列表
	 * 
	 * @param usrId
	 *            用户应用程序ID
	 * @return <h1>用户好友基本信息数组</h1>
	 * 
	 *         <pre>
	 * 0x00003040	成功
	 * 	[
	 * 		{
	 * 			"id":10001,
	 * 			"nick":"XX",
	 * 			"head":"http://daflhwrjwhkda",
	 * 			"card":0,
	 * 			"friend":3
	 * 		}
	 * 	]
	 * 0x00003041	用户不存在
	 * 	""
	 * 0x0000304f	服务器异常
	 * 	""
	 * </pre>
	 */
	public static JSONObject userFriendList(int usrId) {
		try {
			return format(new JSONObject(httpPost(TYPE, "UserFriendList",
					"usrId=" + usrId)));
		} catch (Exception e) {
			return networkError();
		}
	}
}
