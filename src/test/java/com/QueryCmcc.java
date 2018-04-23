package com;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.colotnet.util.SSLClient;
import com.colotnet.util.SignUtils;
import com.kx.util.CDP;

public class QueryCmcc {
	public static void main(String[] args) throws Exception {
		DefaultHttpClient sslClient = new SSLClient();
		HttpPost postMethod = new HttpPost("https://gzwkzftest.cmbc.com.cn/payment-gate-web/gateway/api/backTransReq");
		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("requestNo","20161231171753"));
		nvps.add(new BasicNameValuePair("version", "V1.0"));
		nvps.add(new BasicNameValuePair("transId", "10"));
		nvps.add(new BasicNameValuePair("merNo", "850440053991148"));
		nvps.add(new BasicNameValuePair("orderDate", "20161231171753"));
		nvps.add(new BasicNameValuePair("orderNo",  "20161231171753"));
//		nvps.add(new BasicNameValuePair("openid",openJson.getString("openid")));
//		
//		nvps.add(new BasicNameValuePair("subChnlMerNo", "100000002182"));
//		
//		nvps.add(new BasicNameValuePair("subMerNo", "1000018"));
//		nvps.add(new BasicNameValuePair("subMerName", "454"));
//		nvps.add(new BasicNameValuePair("returnUrl", "xb.rfidstar.cn/k-occ/wxpay/notify"));
//		nvps.add(new BasicNameValuePair("notifyUrl", "xb.rfidstar.cn/k-occ/wxpay/notify"));
//		nvps.add(new BasicNameValuePair("transAmt", "1"));
//		nvps.add(new BasicNameValuePair("commodityName", "2342"));
		nvps.add(new BasicNameValuePair("signature", SignUtils.signData(nvps)));
		postMethod.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		System.out.println(new UrlEncodedFormEntity(nvps, "UTF-8")
				.toString() + ">>>>");
		HttpResponse resp = sslClient.execute(postMethod);
		String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
	}
}
