package com.kx.cache.redis;

import org.apache.ibatis.annotations.Delete;

public interface IRedisService {
	public boolean add (String key, String value);
	public String get (String key);
	public boolean update(String key,String value);
	public abstract long delete(String key);
	/**
     * 通过正则匹配keys
     * 
     * @param pattern
     * @return
     */
    public abstract void setkeys(String pattern);

    /**
     * 检查key是否已经存在
     * 
     * @param key
     * @return
     */
    public abstract boolean exists(String key);

    /**
     * 清空redis 所有数据
     * 
     * @return
     */
    public abstract String flushDB();
    /**
     * 查看redis里有多少数据
     */
    public abstract long dbSize();
}
