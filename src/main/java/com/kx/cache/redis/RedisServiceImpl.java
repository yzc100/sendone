package com.kx.cache.redis;

import java.util.Set;

import org.apache.poi.util.SystemOutLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


public class RedisServiceImpl implements IRedisService {
	public final static String CAHCENAME = "niitcache";// 缓存名  
    public final static int CAHCETIME = 60;// 默认缓存时间 60S  
    public final static int CAHCEHOUR = 60 * 60;// 默认缓存时间 1hr  
    public final static int CAHCEDAY = 60 * 60 * 24;// 默认缓存时间 1Day  
    public final static int CAHCEWEEK = 60 * 60 * 24 * 7;// 默认缓存时间 1week  
    public final static int CAHCEMONTH = 60 * 60 * 24 * 7 * 30;// 默认缓存时间 1month  
    
    @Autowired  
    private RedisTemplate<String, String> redisTemplate;  
    
   
    public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}
	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	public boolean add(final String key,final String value){
    	
    	try {
    		final byte[] bkey = key.getBytes();  
            final byte[] bvalue = value.getBytes();  
            boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
                @Override  
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {  
                    return connection.setNX(bkey, bvalue);  
                }  
            });  
            return result;  
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
    }
    public String get(final String key){
    	byte[] result = redisTemplate.execute(new RedisCallback<byte[]>() {  
            @Override  
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {  
                return connection.get(key.getBytes());  
            }  
        });  
        if (result == null) {  
            return null;  
        }  
        return new String(result); 
    }
    public long delete(final String key){
    	 return redisTemplate.execute(new RedisCallback() {
             public Long doInRedis(RedisConnection connection) throws DataAccessException {
                 long result = 0;
                 for (int i = 0; i < key.length(); i++) {
                     result = connection.del(key.getBytes());
                 }
                 return result;
             }
         });
    }
    /** 
     * 模糊删除key 
     *  
     * @param pattern 
     */  
    public void deleteCacheWithPattern(String pattern) {  
        Set<String> keys = redisTemplate.keys(pattern);  
        redisTemplate.delete(keys);  
    }  
  
    /** 
     * 清空所有缓存 
     */  
    public void clearCache() {  
        deleteCacheWithPattern("reptile" + "|*");  
    }
	@Override
	public boolean update(String key, String value) {
		try {
    		final byte[] bkey = key.getBytes();  
            final byte[] bvalue = value.getBytes();  
            boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
                @Override  
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {  
                	connection.set(bkey, bvalue);  
                    return true;
                }  
            });  
            return result;  
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	@Override
	public void setkeys(String pattern) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean exists(final String key) {
		return redisTemplate.execute(new RedisCallback() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.exists(key.getBytes());
            }
        });
	}
	@Override
	public String flushDB() {
		return redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
	}
	@Override
	public long dbSize() {
		return redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
	}
	  
}
