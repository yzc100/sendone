package com.kx.util;

import com.alibaba.fastjson.JSONObject;

public class JsonUtil {
	public static JSONObject resultData(boolean b,int type,String msg,String data){
		JSONObject resultJSON = new JSONObject();
		resultJSON.put("result", b);
		resultJSON.put("type", type);
		resultJSON.put("msg", msg);
		resultJSON.put("data", data);
		resultJSON.put("status", 200);
		return resultJSON;
	}
	public static JSONObject resultData(){
		JSONObject resultJSON = new JSONObject();
		resultJSON.put("result", true);
		resultJSON.put("type", 0);
		resultJSON.put("msg", "");
		resultJSON.put("data", "");
		resultJSON.put("status", 200);
		return resultJSON;
	}
	public static JSONObject sessionFail(){
		JSONObject resultJSON = new JSONObject();
		resultJSON.put("result", false);
		resultJSON.put("type", 0);
		resultJSON.put("msg", "登录信息过期，请重新登录。");
		resultJSON.put("data", "");
		resultJSON.put("status", 200);
		return resultJSON;
	}
	/**
	 * 
	 * @param store_id 店铺代码
	 * @param client_code 客户代码
	 * @param init_data 原始数据
	 * @param msg 备注信息
	 * @param type 类型
	 * @param level 级别
	 * @return
	 */
	public static JSONObject exceptionJson(String store_id,String client_code,String init_data,String msg,String type,String level){
		JSONObject resultJSON = new JSONObject();
		 resultJSON.put("STORE_ID",store_id );
		 resultJSON.put("EX_INIT_DATA",init_data );
		 resultJSON.put("EX_MSG", msg);
		 resultJSON.put("EX_TYPE", type);
		 resultJSON.put("CLINET_CODE", client_code);
		 resultJSON.put("EX_LEVEL", level);
		 resultJSON.put("CREATETIME", DateTimeUtil.currentTime(DateTimeUtil.DEFAULT_DATETIME_FORMAT_SEC));
		 return resultJSON;
		
	}
}
