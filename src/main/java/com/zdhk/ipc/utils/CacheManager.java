package com.zdhk.ipc.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
public class CacheManager {
    @SuppressWarnings("rawtypes")
    private static Map<String, CacheData> cache = new ConcurrentHashMap<>();

    /**
     * 启动定时任务清理过期缓存
     */
    static {
		/*Timer t = new Timer();
		t.schedule(new ClearTimerTask(cache), 0, 1*24*60*60*1000);*/

        //每一小时，定期删除内存中新门海的token
        //Timer t = new Timer();
        //t.schedule(new ClearMenheyTokenTask(cache), 0, 1*1*60*60*1000);
    }

    /**
     * 设置缓存，不过期
     * @param key
     * @param t
     */
    public static <T> void set(String key, T t) {
        cache.put(key, new CacheData<>(t, 0));
    }

    /**
     * 设置缓存原子化操作
     * @param key
     * @param t
     */
    public static <T> void setIfAbsent(String key,T t){
        cache.putIfAbsent(key, new CacheData<>(t,0));
    }


    /**
     * 设置缓存，指定过期时间expire(单位毫秒)
     * @param key
     * @param t
     * @param expire 过期时间
     */
    public static <T> void set(String key, T t, long expire) {
        cache.put(key, new CacheData<>(t, expire));
    }

    /**
     * 根据key获取指定缓存
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        CacheData<T> data = cache.get(key);
        if(null == data) {
            return null;
        }
        if(data.isExpire()) {
            remove(key);
            return null;
        }
        return data.getData();
    }

    /**
     * 移除指定key缓存
     * @param key
     */
    public static void remove(String key) {
        cache.remove(key);
    }

    /**
     * 移除所有缓存
     */
    public static void removeAll() {
        cache.clear();
    }

    private static class CacheData<T> {
        // 缓存数据
        private T data;
        // 过期时间(单位，毫秒)
        private long expireTime;

        public CacheData(T t, long expire) {
            this.data = t;
            if(expire <= 0) {
                this.expireTime = 0L;
            } else {
                this.expireTime = Calendar.getInstance().getTimeInMillis() + expire;
            }
        }

        /**
         * 判断缓存数据是否过期
         * @return true表示过期，false表示未过期
         */
        public boolean isExpire() {
            if(expireTime <= 0) {
                return false;
            }
            if(expireTime > Calendar.getInstance().getTimeInMillis()) {
                return false;
            }
            return true;
        }

        public T getData() {
            return data;
        }
    }


    /**
     * 清理过期数据定时任务
     */
    private static class ClearTimerTask extends TimerTask {

        @SuppressWarnings("rawtypes")
        Map<String, CacheData> cache;

        @SuppressWarnings("rawtypes")
        public ClearTimerTask(Map<String, CacheData> cache) {
            this.cache = cache;
        }

        @Override
        public void run() {
            Set<String> keys = cache.keySet();
            for(String key : keys) {
                CacheData<?> data = cache.get(key);
                if(data.expireTime <= 0) {
                    continue;
                }
                if(data.expireTime > Calendar.getInstance().getTimeInMillis()) {
                    continue;
                }
                cache.remove(key);
            }
        }
    }


    /**
     * 清理新门海过期的Token定时任务
     */
	private static class ClearMenheyTokenTask extends TimerTask {

		@SuppressWarnings("rawtypes")
		Map<String, CacheData> cache;

		@SuppressWarnings("rawtypes")
		public ClearMenheyTokenTask(Map<String, CacheData> cache) {
			this.cache = cache;
		}

		@Override
		public void run() {
			CacheData<?> data = cache.get("Token_Menhey");
			if(null!=data){
				if(data.expireTime<=0){
					return;
				}
				if(data.expireTime > Calendar.getInstance().getTimeInMillis()) {
					return;
				}
				cache.remove("Token_Menhey");
				log.info("Token");
				//TODO 或者不删除直接更新token
			}
		}
	}
}
