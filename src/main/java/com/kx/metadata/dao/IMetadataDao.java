package com.kx.metadata.dao;

import java.util.List;
import java.util.Map;

public interface IMetadataDao {
	public boolean execute(String sql);
	public List<Map<String,Object>> queryForList(String sql);
	public Map<String, Object> queryForMap(String sql);
	public int queryForInt(String sql);
	public boolean batch(String [] sqls);
	public String getPrimaryValue(String tableName);
}
