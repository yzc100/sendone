import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.TreeMap;

import org.aspectj.weaver.NewConstructorTypeMunger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kx.util.HttpClient;


public class ZK {
	public String getSign(Map<String, String> map){
		String tempString="";
		for(Map.Entry<String, String> entry:map.entrySet()){
			tempString+=entry.getValue();
		}
		return MD5.MD5(tempString+"zizhu");
		
	}
	public void login(HttpClient httpClient,String url){
		Map<String, String> map = new TreeMap<String, String>();
		map.put("companyId", "13");
		map.put("signTime", System.currentTimeMillis()/1000+"");
		map.put("userName", "15982310486");
		map.put("psd", MD5.MD5("123456"));
		map.put("sign", getSign(map));
		System.out.println(httpClient.sendPost(url, getParams(map)));
	}
	public void synDish(HttpClient httpClient,String url){
		Map<String, String> map = new TreeMap<String, String>();
		map.put("companyId", "13");
		map.put("signTime", System.currentTimeMillis()/1000+"");
		map.put("sign", getSign(map));
		System.out.println(getParams(map));
		System.out.println(httpClient.sendPost(url, getParams(map)));
	}
	public void getCustomer(HttpClient httpClient,String url){
		Map<String, String> map = new TreeMap<String, String>();
		map.put("cardUid", "84024804"); //���?��
		map.put("cardId", "00000000000000000050"); // ��ˮ��
		map.put("signTime", System.currentTimeMillis()/1000+"");
		map.put("sign", getSign(map));
		System.out.println(httpClient.sendPost(url, getParams(map)));
//		map.put("cardUid", "2637567813"); //���?��
//		map.put("cardId", "7"); // ��ˮ��
//		map.put("signTime", System.currentTimeMillis()/1000+"");
//		map.put("sign", getSign(map));
//		System.out.println(httpClient.sendPost(url, getParams(map)));
		
	}
	public String getParams(Map<String, String> parMap){
		String params  ="";
		for(Entry<String, String> entry:parMap.entrySet()){
			params+=entry.getKey()+"="+entry.getValue()+"&";
		}
		return params.substring(0,params.length()-1);
	}
	public void consume(HttpClient httpClient,String url){
		Map<String, String> map = new TreeMap<String, String>();
		map.put("companyId", "3");
		map.put("cardUid", "1025450309");
		map.put("cardId", "8");
		map.put("signTime", System.currentTimeMillis()/1000+"");
		JSONArray dataArray = new JSONArray();
		JSONObject object = new JSONObject();
		object.put("PriceId", 8001);
		object.put("SaleCount", 1);
		object.put("Price", 15);
//		JSONObject object2 = new JSONObject();
//		object2.put("PriceId", 2);
//		object2.put("SaleCount", 1);
//		object2.put("Price", 15);
//		JSONObject object3 = new JSONObject();
//		object3.put("PriceId", 2);
//		object3.put("SaleCount", 1);
//		object3.put("Price", 15);
//		dataArray.add(object);
//		dataArray.add(object2);
//		dataArray.add(object3);
		map.put("priceJson", dataArray.toJSONString());
		map.put("sign", getSign(map));
		System.out.println(httpClient.sendPost(url, getParams(map)));
	}
	public static void main(String[] args) {
		ZK zk = new ZK();
		HttpClient httpClient = new HttpClient();
		String url="http://web.zizhukongjian.com/service/";
//		zk.login(httpClient, url+"CompanyMemberLogin");
		zk.synDish(httpClient, url+"GetPrice");
//		zk.getCustomer(httpClient, url+"GetCustomer");
//		for(int i=0;i<15;i++){
//			zk.consume(httpClient, url+"CustomerConsum");
//		}
		//System.out.println(new Date().getTime()/1000);
//		System.out.println(Long.toHexString(20000000));
	}
}
