package com.kx.util;

import com.alibaba.fastjson.JSONObject;
/**
 * ϵͳ������
 * @author yang
 *
 */
import com.sun.xml.internal.ws.message.stream.PayloadStreamReaderMessage;
public class CDP {
	public static JSONObject tableColumns = new JSONObject(); // ���ݿ��ṹ 
	public static JSONObject dictionaryJson = new JSONObject();  // ϵͳ�����ֵ�
	public static JSONObject menuJson = new JSONObject();  // ϵͳ�˵�
	public static JSONObject dsConfig = new JSONObject();
	public static JSONObject cloudRechargeJson =  new JSONObject();
	public static JSONObject cddl = new JSONObject();
	public static int httptimeout=120;
	public static JSONObject payOrder = new JSONObject();
}
