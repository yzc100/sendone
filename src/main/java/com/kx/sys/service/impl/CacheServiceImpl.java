package com.kx.sys.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kx.metadata.service.IMetadataService;
import com.kx.sys.service.ICacheService;
import com.kx.util.OperateProperties;
import com.kx.util.CDP;
import com.kx.util.ST;
import com.sun.org.apache.bcel.internal.generic.NEW;
@Service("cacheService")
public class CacheServiceImpl implements ICacheService{
	@Autowired
	IMetadataService metadataService;
	static String MYSQL_FIND_ANALYZECONFIG = "SELECT * FROM ANALYZE_CONFIG";
	static String MYSQL_FIND_ALLTABLES = "select table_name from information_schema.tables where table_schema='cdp'";
	@Override
	public void loadCache() {
		CDP.tableColumns = getTableColumns();
		//CDP.dictionaryJson = getDictionary();
		CDP.menuJson = getMenu();
		CDP.dsConfig = loadProperties("config.properties");
	}
	public JSONObject getTableColumns(){
		JSONObject jsonbJson =  new JSONObject();
		List<Map<String, Object>> tables = metadataService.queryForList(MYSQL_FIND_ALLTABLES);
		for(Map<String, Object> map:tables){
			jsonbJson.put(map.get("table_name").toString(), new JSONObject());
			jsonbJson.getJSONObject(map.get("table_name").toString()).put("columns", new JSONObject());
			jsonbJson.getJSONObject(map.get("table_name").toString()).put("primarykey", "");
			List<Map<String, Object>> columns = metadataService.queryForList("show columns from "+ map.get("table_name"));
			for(Map<String, Object> columnMap : columns){
				if(null!=columnMap.get("Key") && "PRI".equals(columnMap.get("Key"))){
				jsonbJson.getJSONObject(map.get("table_name").toString()).put("primarykey", columnMap.get("Field"));
				}
				if(!jsonbJson.getJSONObject(map.get("table_name").toString()).getJSONObject("columns").containsKey(columnMap.get("Field").toString())){
					jsonbJson.getJSONObject(map.get("table_name").toString()).getJSONObject("columns").put(columnMap.get("Field").toString(),new JSONObject());
				}
				if(columnMap.get("Type").toString().indexOf("char")>=0){
					jsonbJson.getJSONObject(map.get("table_name").toString()).getJSONObject("columns").getJSONObject(columnMap.get("Field").toString()).put("datatype",0);
					
				}else if(columnMap.get("Type").toString().indexOf("date")==0){
					jsonbJson.getJSONObject(map.get("table_name").toString()).getJSONObject("columns").getJSONObject(columnMap.get("Field").toString()).put("datatype",1);
				}else if(columnMap.get("Type").toString().indexOf("int")>=0){
					jsonbJson.getJSONObject(map.get("table_name").toString()).getJSONObject("columns").getJSONObject(columnMap.get("Field").toString()).put("datatype",2);
				}
				jsonbJson.getJSONObject(map.get("table_name").toString()).getJSONObject("columns").getJSONObject(columnMap.get("Field").toString()).put("isnull", columnMap.get("Null")==null?"":columnMap.get("Null").toString());
				jsonbJson.getJSONObject(map.get("table_name").toString()).getJSONObject("columns").getJSONObject(columnMap.get("Field").toString()).put("keyvalue", columnMap.get("Key")==null?"":columnMap.get("Key").toString());
				jsonbJson.getJSONObject(map.get("table_name").toString()).getJSONObject("columns").getJSONObject(columnMap.get("Field").toString()).put("default", columnMap.get("Default")==null?"":columnMap.get("Default").toString());
			}
		}
		return jsonbJson;
	}
	public JSONObject getDictionary(){
		JSONObject dataJson = new JSONObject();
		List<Map<String, Object>> dictionaryType_list = metadataService.queryForList("SELECT * FROM DICTIONARY_TYPE_RECORD");
		List<Map<String, Object>> dictionary_listList = metadataService.queryForList("SELECT * FROM DICTIONARY_RECORD");
		for(Map<String, Object> map:dictionaryType_list){
			dataJson.put(map.get("DY_CODE").toString(), new JSONObject());
			dataJson.getJSONObject(map.get("DY_CODE").toString()).put("name", map.get("DY_NAME"));
			for(Map<String, Object> dMap:dictionary_listList){
				if(map.get("DY_ID").equals(dMap.get("DY_ID"))){
					dataJson.getJSONObject(map.get("DY_CODE").toString()).put(dMap.get("D_CODE").toString(),dMap.get("D_NAME"));
				}
			}
		}
		return dataJson;
	}
	public JSONObject getMenu() {
		JSONObject dataJson = new JSONObject();
		List<Map<String, Object>> list = metadataService.queryForList("SELECT * FROM menu_record");
		for(Map<String, Object> map:list){
			if(Integer.parseInt(map.get("ME_PID").toString())==0){
				dataJson.put(map.get("ME_ID").toString(), new JSONObject());
				dataJson.getJSONObject(map.get("ME_ID").toString()).put("name", map.get("ME_NAME"));
				dataJson.getJSONObject(map.get("ME_ID").toString()).put("url", map.get("ME_URL"));
				dataJson.getJSONObject(map.get("ME_ID").toString()).put("css", map.get("ME_CSS"));
				dataJson.getJSONObject(map.get("ME_ID").toString()).put("img", map.get("ME_IMAGE"));
				dataJson.getJSONObject(map.get("ME_ID").toString()).put("level", map.get("ME_LEVEL"));
				dataJson.getJSONObject(map.get("ME_ID").toString()).put("child",new JSONObject());
				for(Map<String, Object> map2:list){
					if(map2.get("ME_PID").equals(map.get("ME_ID"))){
						dataJson.getJSONObject(map.get("ME_ID").toString()).getJSONObject("child").put(map2.get("ME_ID").toString(), new JSONObject());
						dataJson.getJSONObject(map.get("ME_ID").toString()).getJSONObject("child").getJSONObject(map2.get("ME_ID").toString()).put("name", map2.get("ME_NAME"));
						dataJson.getJSONObject(map.get("ME_ID").toString()).getJSONObject("child").getJSONObject(map2.get("ME_ID").toString()).put("url", map2.get("ME_URL"));
						dataJson.getJSONObject(map.get("ME_ID").toString()).getJSONObject("child").getJSONObject(map2.get("ME_ID").toString()).put("css", map2.get("ME_CSS"));
						dataJson.getJSONObject(map.get("ME_ID").toString()).getJSONObject("child").getJSONObject(map2.get("ME_ID").toString()).put("img", map2.get("ME_IMAGE"));
						dataJson.getJSONObject(map.get("ME_ID").toString()).getJSONObject("child").getJSONObject(map2.get("ME_ID").toString()).put("level", map2.get("ME_LEVEL"));
						dataJson.getJSONObject(map.get("ME_ID").toString()).getJSONObject("child").getJSONObject(map2.get("ME_ID").toString()).put("child", new JSONObject());
						for(Map<String, Object> map3:list){
							if(map3.get("ME_PID").equals(map2.get("ME_ID"))){
								dataJson.getJSONObject(map.get("ME_ID").toString()).getJSONObject("child").getJSONObject(map2.get("ME_ID").toString()).getJSONObject("child").put(map3.get("ME_ID").toString(), new JSONObject());
								dataJson.getJSONObject(map.get("ME_ID").toString()).getJSONObject("child").getJSONObject(map2.get("ME_ID").toString()).getJSONObject("child").getJSONObject(map3.get("ME_ID").toString()).put("name", map3.get("ME_NAME"));
								dataJson.getJSONObject(map.get("ME_ID").toString()).getJSONObject("child").getJSONObject(map2.get("ME_ID").toString()).getJSONObject("child").getJSONObject(map3.get("ME_ID").toString()).put("url", map3.get("ME_URL"));
								dataJson.getJSONObject(map.get("ME_ID").toString()).getJSONObject("child").getJSONObject(map2.get("ME_ID").toString()).getJSONObject("child").getJSONObject(map3.get("ME_ID").toString()).put("css", map3.get("ME_CSS"));
								dataJson.getJSONObject(map.get("ME_ID").toString()).getJSONObject("child").getJSONObject(map2.get("ME_ID").toString()).getJSONObject("child").getJSONObject(map3.get("ME_ID").toString()).put("img", map3.get("ME_IMAGE"));
								dataJson.getJSONObject(map.get("ME_ID").toString()).getJSONObject("child").getJSONObject(map2.get("ME_ID").toString()).getJSONObject("child").getJSONObject(map3.get("ME_ID").toString()).put("level", map3.get("ME_LEVEL"));
								dataJson.getJSONObject(map.get("ME_ID").toString()).getJSONObject("child").getJSONObject(map2.get("ME_ID").toString()).getJSONObject("child").getJSONObject(map3.get("ME_ID").toString()).put("child", new JSONObject());
							}
						}
					}
				}
			}
		}
		return dataJson;
	}
	public JSONObject loadProperties(String propertiesFileName){
		Properties p = new Properties();
		try {
		p = OperateProperties.loadProperties(propertiesFileName, OperateProperties.BY_CLASSLOADER);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (JSONObject)JSONObject.toJSON(p);
	}
	public JSONObject loadImportValueRule(){
		List<Map<String, Object>> importTaskList = metadataService.queryForList("select * from tf_importvaluerule");
		JSONObject jsonObject = new JSONObject();
		for(Map<String, Object> map:importTaskList){
			if(jsonObject.get(map.get("IVR_TASKTYPE"))!=null){
				jsonObject.getJSONArray(map.get("IVR_TASKTYPE").toString()).add(map);
			}else{
				JSONArray jsonArray = new JSONArray();
				jsonArray.add(map);
				jsonObject.put(map.get("IVR_TASKTYPE").toString(),jsonArray );
			}
		}
		return jsonObject;
	}
}
