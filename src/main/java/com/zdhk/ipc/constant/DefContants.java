package com.zdhk.ipc.constant;

public class DefContants {
	
	public final static String X_ACCESS_TOKEN = "X-Access-Token";

	/** 登录用户Shiro权限缓存KEY前缀 */
	public static String PREFIX_USER_SHIRO_CACHE  = "shiro:cache:org.jeecg.modules.shiro.authc.ShiroRealm.authorizationCache:";
	/** 登录用户Token令牌缓存KEY前缀 */
	public static final String PREFIX_USER_TOKEN  = "prefix_user_token_";
	/** Token缓存时间：3600秒即一小时 */
	public static final int  TOKEN_EXPIRE_TIME  = 3600;

}
