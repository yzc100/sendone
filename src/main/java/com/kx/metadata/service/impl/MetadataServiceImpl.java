package com.kx.metadata.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.kx.metadata.dao.IMetadataDao;
import com.kx.metadata.service.IMetadataService;
import com.kx.util.CDP;
@Service("metadataService")
public class MetadataServiceImpl implements IMetadataService {
	@Autowired
	IMetadataDao metadataDao;
	@Override
	public boolean execute(String sql) {
		return metadataDao.execute(sql);
	}
	@Override
	public List<Map<String, Object>> queryForList(String sql) {
		return metadataDao.queryForList(sql);
	}

	@Override
	public Map<String, Object> queryForMap(String sql) {
		
		return metadataDao.queryForMap(sql);
	}

	@Override
	public int queryForInt(String sql) {
		return metadataDao.queryForInt(sql);
	}
	public String getDataByType(String tableName,String columnName,JSONObject dataJson){
		String tempValue = "";
//		System.out.println(tableName +"------"+ columnName +"---"+dataJson);
		switch (CDP.tableColumns.getJSONObject(tableName).getJSONObject("columns").getJSONObject(columnName.toUpperCase()).getIntValue("datatype")) {
		case 0:
			tempValue = dataJson.get(columnName)==null?"":dataJson.getString(columnName);
			tempValue = "'"+tempValue+"',";
			break;
		case 1:
			tempValue = dataJson.get(columnName)==null?"":dataJson.getString(columnName);
			tempValue = "'"+tempValue+"',";
			break;
		case 2:
			tempValue = dataJson.get(columnName)==null?"0":dataJson.getString(columnName);
			tempValue =	tempValue+",";
		default:
			break;
		}
		return tempValue;
	}
	@Override
	public String getsql(JSONObject jsonObject, String tableName) {
		StringBuffer startBuffer = new StringBuffer();
	    StringBuffer endBuffer = new StringBuffer();
	    startBuffer.append("INSERT INTO " + tableName + "(");
	    endBuffer.append(" VALUES(");
	    for (String filedKey : jsonObject.keySet()) {
	      startBuffer.append(filedKey + ",");
	      endBuffer.append(getDataByType(tableName, filedKey, jsonObject));
	    }
	    return startBuffer.toString().substring(0, startBuffer.length() - 1) + ") " + endBuffer.toString().substring(0, endBuffer.length() - 1) + ")";
	}

	@Override
	public String getsql(JSONObject dataJson, String tableName,
			String priKey, String priValue) {
 		StringBuffer startBuffer = new StringBuffer(" UPDATE "+ tableName + " SET ");
 		String tempValue = "";
		keep:for(String columnName:dataJson.keySet()){
			if(columnName.equals(CDP.tableColumns.getJSONObject(tableName).getString("primarykey")))
			continue keep;
			startBuffer.append(columnName+"="+getDataByType(tableName, columnName, dataJson));
			
		}
		return startBuffer.toString().substring(0, startBuffer.length() - 1) +" WHERE "+priKey+"='"+priValue+"'";
	}

	@Override
	public boolean batchUpdate(String[] sqls) {
		return metadataDao.batch(sqls);
	}
	@Override
	public String getPrimaryValue(String tableName) {
		return metadataDao.getPrimaryValue(tableName);
	}
	@Override
	public boolean wirteSynRecord(JSONObject jsonObject) {
		try {
			Map<String, Object> synRecordMap = metadataDao.queryForMap("SELECT SEQNO FROM TF_SYN_RECORD WHERE MACHINE_NO='"+jsonObject.getString("machine_no")+"' AND DATA_TYPE='"+jsonObject.getString("data_type")+"'");
			metadataDao.execute("UPDATE TF_SYN_RECORD SET AGENT_LAST_UPDATETIME='"+jsonObject.getString("updatetime")+"' WHERE SEQNO='"+synRecordMap.get("SEQNO")+"'");
//			if(jsonObject.getIntValue("total_record")>0){
//				metadataDao.execute("INSERT INTO TF_SYN_LOG_RECORD (SEQNO,MSG,DATA_INFO,SYN_ID,TOTAL_RECORD) VALUES('"+metadataDao.getPrimaryValue("tf_syn_log_record")+"','"+jsonObject.getString("msg")+"','"+jsonObject.getString("data")+"','"+synRecordMap.get("SEQNO")+"','"+jsonObject.getString("total_record")+"')");
//			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}
	@Override
	public boolean wirteUploadRecord(JSONObject jsonObject) {
		try {
			Map<String, Object> uploadMap = metadataDao.queryForMap("SELECT * FROM ");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

}
