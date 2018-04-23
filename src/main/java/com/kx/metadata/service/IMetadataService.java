package com.kx.metadata.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface IMetadataService {
	public boolean execute(String sql);
	public List<Map<String,Object>> queryForList(String sql);
	public Map<String, Object> queryForMap(String sql);
	public int queryForInt(String sql);
	public String getsql(JSONObject dataMap,String tableName);
	public String getsql(JSONObject dataMap,String tableName,String priKey,String priValue);
	public boolean batchUpdate(String [] sqls);
	public String getPrimaryValue(String tableName);
	public boolean wirteSynRecord(JSONObject jsonObject);
	public boolean wirteUploadRecord(JSONObject jsonObject);
//	public boolean wirteExceptionRecord(JSONObject paramsJson);
}
