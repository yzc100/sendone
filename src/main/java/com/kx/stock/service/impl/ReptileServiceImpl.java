package com.kx.stock.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.SystemOutLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.kx.cache.redis.RedisServiceImpl;
import com.kx.metadata.service.IMetadataService;
import com.kx.stock.cache.IStockCacheService;
import com.kx.stock.reptile.StockViewThread;
import com.kx.stock.service.IReptileService;
import com.kx.util.CDP;
import com.kx.util.DateTimeUtil;
import com.kx.util.HttpClient;
import com.kx.util.JsonUtil;
import com.kx.util.ST;

import redis.clients.jedis.Jedis;
@Service("reptileService")
public class ReptileServiceImpl implements IReptileService {
	@Autowired
	IMetadataService metadataService;
	@Autowired
	IStockCacheService stockCacheService;
	@Autowired
	RedisServiceImpl redisService;

	@Override
	public synchronized JSONObject runReptileTask(JSONObject params) {
	return null;
	}
	@Override
	public void autoReptile() {
		try{
			//check whether open market
			if(checkOpenMarket()){
			   //back data
				//init data
				initData();
				//reptile new Data
//				reptileNewStock();
//				reptileGnPlate();
				stockCacheService.loadAllStock();
				List<Map<String, Object>> stockList = metadataService.queryForList("SELECT STOCK_NO,STOCK_NO_BUFF FROM stock_record");
				int k = 0;
				StringBuffer stBuffer = new StringBuffer();
				List<String> stocks = new ArrayList<>();
				for(int i=0;i<stockList.size();i++){
					stocks.add(stockList.get(i).get("STOCK_NO_BUFF").toString());
					if(i%20==0 || i== (stockList.size()-1)) {
						StockViewThread stockViewThread = new StockViewThread(stocks);
						Thread thread = new Thread(stockViewThread);
						thread.start();
						stocks =new ArrayList<>();
					}
				}
				//stCache.loadCache();
//				loadReptileTask();
				
//				MarketReptile  marketReptile = new MarketReptile();
//				Thread market_thread = new Thread(marketReptile);
//				market_thread.start();
//				PlateReptile plateReptile = new PlateReptile();
//				Thread plate_thread = new Thread(plateReptile);
//				plate_thread.start();
			}
	}catch(Exception e){
		e.printStackTrace();
	}
		
	}
	public JSONObject loadReptileTask() {
		redisService.flushDB();
		JSONObject jsonbJson =  new JSONObject();
		List<Map<String, Object>> list = metadataService.queryForList("SELECT * FROM reptile_task_record");
		List<Map<String,Object>> yesterdayStockList = metadataService.queryForList("select * from stock_record_h where UPDATETIME= (select max(UPDATETIME) maxtime from stock_record_h)");
		for(Map<String,Object> s_stock:yesterdayStockList) {
			redisService.update("ys"+s_stock.get("STOCK_NO"), ((JSONObject)JSONObject.toJSON(s_stock)).toJSONString());
		}
		for(Map<String,Object> map:list){
			JSONObject taskJson = new JSONObject();
			taskJson = (JSONObject)JSONObject.toJSON(map);
			taskJson.put("stocklist", new JSONObject());
			List<Map<String, Object>> stockTradeConfig = metadataService.queryForList("SELECT * FROM trade_detail_config where stock_no in (select STOCK_NO from reptile_task_relation where TASK_ID='"+taskJson.getString("TASK_ID")+"')");
			for(Map<String, Object> tradeMap:stockTradeConfig){
				JSONObject tradeJson =  (JSONObject)JSONObject.toJSON(tradeMap);
				taskJson.getJSONObject("stocklist").put(tradeJson.getString("STOCK_NO_BUFF"), tradeJson);
			}
//			System.out.println(taskJson.toJSONString());
			redisService.update(map.get("TASK_ID").toString(), taskJson.toJSONString());
//			ST.reptileTimeArray.add(map.get("TASK_ID").toString());
//			try {
//				FileOperation.writeTxtFile(taskJson.toJSONString(),new File("D://cache.txt"));
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		
		return jsonbJson;
	}
	public void loadYesterDayStock() {
		List<Map<String,Object>> yesterdayStockList = metadataService.queryForList("select * from stock_record_h where UPDATETIME= (select max(UPDATETIME) maxtime from stock_record_h)");
		for(Map<String,Object> s_stock:yesterdayStockList) {
			redisService.update("ys"+s_stock.get("STOCK_NO"), ((JSONObject)JSONObject.toJSON(s_stock)).toJSONString());
		}
	}
	
	
	
	public void reptileGnPlate(){
		JSONObject datajson = new JSONObject();
		String v_plate ="";
		String  result = httpHelp("http://stock.gtimg.cn/data/view/bdrank.php?&t=02/averatio&p=1&o=0&l=500&v=list_data", "gb18030");
		metadataService.execute("delete from plate_stock_relation");
		if(StringUtils.isNotEmpty(result)){
			 datajson = JSONObject.parseObject(result.substring(result.indexOf("{"),result.lastIndexOf("}")+1));
			 String st_array = "";
			 for(String stock_no_buff:datajson.getString("data").split(",")){
				 st_array +="'"+stock_no_buff+"',";
				 List<Map<String, Object>> list = metadataService.queryForList("select * from plate_record where PLATE_NO_BUFF='"+stock_no_buff+"'");
				 if(null==list || list.size() == 0){
					 metadataService.execute("insert into plate_record(PLATE_NO,PLATE_NO_BUFF,PLATE_TYPE) values('"+stock_no_buff+"','"+stock_no_buff+"','2')");
				 }
				 JSONObject v_plateJSON = new JSONObject();
				 v_plate = httpHelp("http://stock.gtimg.cn/data/index.php?appn=rank&t=pt"+stock_no_buff.substring(4, stock_no_buff.length())+"/chr&p=1&o=0&l=500&v=list_data", "gb18030");
				 v_plateJSON =JSONObject.parseObject(v_plate.substring(v_plate.indexOf("{"),v_plate.lastIndexOf("}")+1));
				 for(String v_tag:v_plateJSON.getString("data").split(",")){
					 metadataService.execute("insert into plate_stock_relation(PLATE_NO,STOCK_NO) values('"+stock_no_buff+"','"+v_tag+"')");
				 }
			 }
//			System.out.println(st_array);
		}
		result = httpHelp("http://stock.gtimg.cn/data/view/bdrank.php?&t=01/averatio&p=1&o=0&l=500&v=list_data", "gb18030");
		if(StringUtils.isNotEmpty(result)){
			 datajson = JSONObject.parseObject(result.substring(result.indexOf("{"),result.lastIndexOf("}")+1));
			 String st_array = "";
			 for(String stock_no_buff:datajson.getString("data").split(",")){
				 st_array +="'"+stock_no_buff+"',";
				 List<Map<String, Object>> list = metadataService.queryForList("select * from plate_record where PLATE_NO_BUFF='"+stock_no_buff+"'");
				 if(null==list || list.size() == 0){
					 metadataService.execute("insert into plate_record(PLATE_NO,PLATE_NO_BUFF,PLATE_TYPE) values('"+stock_no_buff+"','"+stock_no_buff+"','1')");
				 }
				 JSONObject v_plateJSON = new JSONObject();
				 v_plate = httpHelp("http://stock.gtimg.cn/data/index.php?appn=rank&t=pt"+stock_no_buff.substring(4, stock_no_buff.length())+"/chr&p=1&o=0&l=500&v=list_data", "gb18030");
				 v_plateJSON =JSONObject.parseObject(v_plate.substring(v_plate.indexOf("{"),v_plate.lastIndexOf("}")+1));
				 for(String v_tag:v_plateJSON.getString("data").split(",")){
					 metadataService.execute("insert into plate_stock_relation(PLATE_NO,STOCK_NO) values('"+stock_no_buff+"','"+v_tag+"')");
				 }
			 }
//			System.out.println(st_array);
		}
		
	}
	public  boolean checkOpenMarket(){
		String result = httpHelp("http://qt.gtimg.cn/q=sh000001","gb18030");
		if(StringUtils.isNotEmpty(result)){
			if(result.split("~")[30].indexOf(DateTimeUtil.currentTime("yyyyMMdd"))==0){
				
				return true;
			}
				return false;
		}else{
			
			return false;
		}
	}
	public void reptileNewStock(){
		 String htmlContent = httpHelp("http://stock.gtimg.cn/data/index.php?appn=rank&t=ranka/chr&p=1&o=0&l=60&v=list_data","utf-8");
		 JSONObject datajson = new JSONObject();
		 datajson = JSONObject.parseObject(htmlContent.substring(htmlContent.indexOf("{"),htmlContent.lastIndexOf("}")+1));
		 int total = datajson.getIntValue("total");
		 echo:for(int i=1;i<=total;i++){
			 htmlContent = httpHelp("http://stock.gtimg.cn/data/index.php?appn=rank&t=ranka/chr&p="+i+"&o=0&l=60&v=list_data","utf-8");
			 datajson = JSONObject.parseObject(htmlContent.substring(htmlContent.indexOf("{"),htmlContent.lastIndexOf("}")+1));
			 String st_array = "";
			 for(String stock_no_buff:datajson.getString("data").split(",")){
				 st_array +="'"+stock_no_buff+"',";
			 }
			 List<Map<String,Object>> stockList = metadataService.queryForList("SELECT * FROM stock_record WHERE STOCK_NO_BUFF IN("+st_array.substring(0, st_array.length()-1)+")");
			 Map<String, String> stock_current_Map =  new HashMap<>();
			 if(stockList!=null && stockList.size()>0){
				 for(Map<String,Object> stockMap: stockList){
					 stock_current_Map.put(stockMap.get("STOCK_NO_BUFF").toString(), stockMap.get("STOCK_NO_BUFF").toString());
				 }
			 }
			 for(String stock_no_buff:datajson.getString("data").split(",")){
				 if(!stock_current_Map.containsKey(stock_no_buff)){
				 JSONObject stockJson = new JSONObject();
//				 stockJson.put("SEQNO", metadataService.getPrimaryValue("stock_record"));
				 stockJson.put("STOCK_NO_BUFF", stock_no_buff);
				 stockJson.put("STOCK_NO", stock_no_buff.substring(2,stock_no_buff.length()));
				 metadataService.execute(metadataService.getsql(stockJson, "stock_record"));
				// metadataService.execute("INSERT INTO trade_detail_config(STOCK_NO,STOCK_NO_BUFF) VALUES('"+stock_no_buff.substring(2,stock_no_buff.length())+"','"+stock_no_buff+"')");
				 //metadataService.execute("INSERT INTO stock_spi_record(STOCK_NO,STOCK_NO_BUFF) VALUES('"+stock_no_buff.substring(2,stock_no_buff.length())+"','"+stock_no_buff+"')");
				 }
		 }
		 }
}
	public void initData() throws Exception{
		metadataService.execute("delete from reptile_task_record");
		metadataService.execute("delete from reptile_task_relation");
		metadataService.execute("delete from trade_detail_config");
		String st = "UPDATE stock_record SET";
		for(String key:CDP.tableColumns.getJSONObject("stock_record").getJSONObject("columns").keySet()){
			if(!key.equals("STOCK_NO") && !key.equals("STOCK_NAME")&& !key.equals("STOCK_NO_BUFF")){
				if(key.equals("CREATETIME") || key.equals("UPDATETIME")){
					st +=" "+key+"='"+DateTimeUtil.currentTime(DateTimeUtil.DEFAULT_DATETIME_FORMAT_SEC)+"',";
				}else{
					st +=" "+key+"='0',";
				}
				
			}
			
		}
		metadataService.execute(st.substring(0,st.length()-1));
		
	}
	public  String httpHelp(String url,String charset){
		HttpClient httpClient = new HttpClient();
		String content =null;
		for(int i=0;i<10;i++){
			if(i!=0){
			try {
//				if(url.indexOf("qt2")==-1){
//				url.replace("qt", "qt2");
//				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			try {
//				System.out.println(url);
				content = httpClient.httpGet(url, "gb18030").trim();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(content!=null){
				break;
			}
		}
		return content;
	}

}
