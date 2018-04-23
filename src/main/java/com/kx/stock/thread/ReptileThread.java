package com.kx.stock.thread;

import java.math.BigDecimal;
import java.security.interfaces.DSAKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kx.util.HttpClient;
import com.kx.cache.redis.RedisServiceImpl;
import com.kx.listener.SpringContextHolder;
import com.kx.metadata.service.IMetadataService;
import com.kx.util.*;
public class ReptileThread implements Runnable {
//	@Autowired
//	IMetadataService metadataService;
//	@Autowired
//	RedisServiceImpl redisService;
	IMetadataService metadataService= SpringContextHolder.getBean("metadataService");
	RedisServiceImpl redisService = SpringContextHolder.getBean("redisService");
	private Log log = LogFactory.getLog(ReptileThread.class);
	private int threadnum = 0;
	public ReptileThread(int threadnum) {
		this.threadnum = threadnum;
	}
	private String taskId = new String("");
	private String status = new String("0");
	private JSONObject reptile_config_json = new JSONObject();
	public void initDB(){
		taskId = new String("");
		String status = new String("0");
		reptile_config_json = new JSONObject();
	}
	private JSONObject yesdayJson = new JSONObject();
	private JSONObject baseLineJson = new JSONObject();
	private JSONObject currentJson = new JSONObject();
	@Override
	public void run() {
		HttpClient httpClient = new HttpClient();
		JSONObject paramsJson =  new JSONObject();
		paramsJson.put("agent",CDP.dsConfig.getString("agentId")+"-"+threadnum);
		st:while (true) {
			System.out.println("ST采集器第"+threadnum+" 线程,正在采集");
			currentJson = new JSONObject();
		try {
			Thread.sleep(5000);
			
			if(StringUtils.isNotEmpty(taskId)){
				paramsJson.put("taskId", taskId);
				paramsJson.put("status", status);
				paramsJson.put("stocklist", reptile_config_json);
			
			}
			System.out.println("post:"+paramsJson.toJSONString());
			JSONObject reptileTaskJson  =httpClient.postForm("http://"+CDP.dsConfig.getString("serverIp")+":"+CDP.dsConfig.getString("serverPort")+"/cdp/reptile/run",paramsJson.toJSONString());
			if(reptileTaskJson==null || reptileTaskJson.keySet().size()==0 ){
				System.out.println("ST采集器第"+threadnum+" 线程,网络发生异常。5秒后重试");
				Thread.sleep(1000*5);
				continue st;
			}
			System.out.println(reptileTaskJson.toJSONString());
			if( !reptileTaskJson.getBooleanValue("result")){
				System.out.println("ST采集器第"+threadnum+" 线程,未获取到任务。开始休眠30秒。");
				initDB();
				Thread.sleep(1000*30);
				continue st;
			}
			System.out.println("ST采集器第"+threadnum+" 线程,取到采集任务"+reptileTaskJson.getJSONObject("data").getString("TASK_ID")+"开始准备采集。");
			taskId = reptileTaskJson.getJSONObject("data").getString("TASK_ID");
			reptile_config_json = reptileTaskJson.getJSONObject("data").getJSONObject("stocklist");
			System.out.println("000--"+reptile_config_json.keySet().size());
			//List<Map<String, Object>> reptileConfigList =  metaDataBO.queryForListMap("select trade_detail_config.* from reptile_task_relation INNER JOIN trade_detail_config on reptile_task_relation.STOCK_NO = trade_detail_config.STOCK_NO where  reptile_task_relation.TASK_ID='"+reptileTaskJson.getJSONObject("data").getString("TASK_ID")+"'");
			List<String> stock_list = new ArrayList<>();
			for(String key:reptile_config_json.keySet()){
				stock_list.add(key);
			}
			Long starttime = System.currentTimeMillis();
			System.out.println("ST采集器第"+threadnum+" 线程,开始采集量价信息。");
			repitileStock(stock_list);
			System.out.println("ST采集器第"+threadnum+" 线程,量价信息采集完成。 耗时:"+(System.currentTimeMillis()-starttime)+" ms");
			int resultnum = 0;
			echo:for(String stock_no_buff:stock_list){
				//check is need reptile data
				if(reptile_config_json.getJSONObject(stock_no_buff).getString("IS_REPTILE").equals("0")){
					System.out.println(stock_no_buff+"已采集完成--。");
					resultnum++;
					continue echo;
				}
				try {
					String stock_time = httpHelp("http://stock.gtimg.cn/data/index.php?appn=detail&action=timeline&c="+stock_no_buff);
					if(StringUtils.isNotEmpty(stock_time) && stock_time.indexOf("\"")>0){
						stock_time = stock_time.substring(stock_time.indexOf("\"")+1, stock_time.lastIndexOf("\""));
						String [] timeArray = stock_time.split("[|]");
						if(timeArray!=null && timeArray.length>0){
//							System.out.println(stock_no_buff);
//							System.out.println(reptile_config_json.getJSONObject(stock_no_buff).toJSONString());
							reptile_config_json.getJSONObject(stock_no_buff).put("TOTAL_PAGE", timeArray.length);
//							if()
							tag:for(int i=reptile_config_json.getJSONObject(stock_no_buff).getIntValue("CUR_PAGE");i<timeArray.length;i++){
								reptile_config_json.getJSONObject(stock_no_buff).put("CUR_TIME", timeArray[i]);
								//if(reptile_config_json.getJSONObject(stock_no_buff).getIntValue("PAGE_STATUS")==0){
									String page_content = httpHelp("http://stock.gtimg.cn/data/index.php?appn=detail&action=data&c="+stock_no_buff+"&p="+i);
									if(StringUtils.isNotEmpty(page_content) && page_content.indexOf("\"")>0){
										List<String> tradeSqlArray = new ArrayList<String>();
										page_content = page_content.substring(page_content.indexOf("\"")+1, page_content.lastIndexOf("\""));
										String [] pageArray = page_content.split("[|]");
										if(reptile_config_json.getJSONObject(stock_no_buff).getIntValue("PAGE_INDEX")==pageArray.length-1 && reptile_config_json.getJSONObject(stock_no_buff).getIntValue("CUR_PAGE")!=timeArray.length-1){
											reptile_config_json.getJSONObject(stock_no_buff).put("PAGE_INDEX", 0);
											reptile_config_json.getJSONObject(stock_no_buff).put("CUR_PAGE", i+1);
											continue tag;
										}
										for(int k=reptile_config_json.getJSONObject(stock_no_buff).getIntValue("PAGE_INDEX");k<pageArray.length;k++){
											JSONObject dataJson =  new JSONObject();
											dataJson.put("TRADE_TIME", pageArray[k].split("/")[1]);
											dataJson.put("TRADE_PRICE", pageArray[k].split("/")[2]);
											dataJson.put("TRADE_NUM", pageArray[k].split("/")[4]);
											dataJson.put("TRADE_TYPE", pageArray[k].split("/")[6]);
											dataJson.put("TRADE_TOTAL_PRICE",pageArray[k].split("/")[5]);
											dataJson.put("CREATETIME", DateTimeUtil.currentTime(DateTimeUtil.DEFAULT_DATE_FORMAT));
											dataJson.put("STOCK_NO", reptile_config_json.getJSONObject(stock_no_buff).getString("STOCK_NO"));
											JSONObject messageJson =  new JSONObject();
											String trade_count_sql = "";
											if(dataJson.getString("TRADE_TYPE").equals("B")){
												if(dataJson.getIntValue("TRADE_TOTAL_PRICE")<1000000){
													trade_count_sql = "BUY_1 = BUY_1+1";
												}else if(dataJson.getIntValue("TRADE_TOTAL_PRICE")>1000000 && dataJson.getIntValue("TRADE_TOTAL_PRICE")<=3000000){
													trade_count_sql = "BUY_2 = BUY_2+1";
													messageJson.put("LEVEL", 2);
												}else if(dataJson.getIntValue("TRADE_TOTAL_PRICE")>3000000 && dataJson.getIntValue("TRADE_TOTAL_PRICE")<=5000000){
													trade_count_sql = "BUY_3 = BUY_3+1";
													messageJson.put("LEVEL", 3);
												}else if(dataJson.getIntValue("TRADE_TOTAL_PRICE")>5000000 && dataJson.getIntValue("TRADE_TOTAL_PRICE")<=10000000){
													trade_count_sql = "BUY_4 = BUY_4+1";
													messageJson.put("LEVEL", 4);
												}else if(dataJson.getIntValue("TRADE_TOTAL_PRICE")>10000000){
													trade_count_sql = "BUY_5 = BUY_5+1";
													messageJson.put("LEVEL", 5);
												}
											}else if(dataJson.getString("TRADE_TYPE").equals("S")){
												if(dataJson.getIntValue("TRADE_TOTAL_PRICE")<1000000){
													trade_count_sql = "SELL_1 = SELL_1+1";
												}else if(dataJson.getIntValue("TRADE_TOTAL_PRICE")>1000000 && dataJson.getIntValue("TRADE_TOTAL_PRICE")<=3000000){
													trade_count_sql = "SELL_2 = SELL_2+1";
													messageJson.put("LEVEL", 2);
												}else if(dataJson.getIntValue("TRADE_TOTAL_PRICE")>3000000 && dataJson.getIntValue("TRADE_TOTAL_PRICE")<=5000000){
													trade_count_sql = "SELL_3 = SELL_3+1";
													messageJson.put("LEVEL", 3);
												}else if(dataJson.getIntValue("TRADE_TOTAL_PRICE")>5000000 && dataJson.getIntValue("TRADE_TOTAL_PRICE")<=10000000){
													trade_count_sql = "SELL_4 = SELL_4+1";
													messageJson.put("LEVEL", 4);
												}else if(dataJson.getIntValue("TRADE_TOTAL_PRICE")>10000000){
													trade_count_sql = "SELL_5 = SELL_5+1";
													messageJson.put("LEVEL", 5);
												}
											}
											if(StringUtils.isNotEmpty(trade_count_sql)){
											metadataService.execute("update stock_record set "+trade_count_sql+" where stock_no='"+reptile_config_json.getJSONObject(stock_no_buff).getString("STOCK_NO")+"'");
											}
											if(dataJson.getIntValue("TRADE_TOTAL_PRICE")>1000000){
												messageJson.put("STOCK_NO", reptile_config_json.getJSONObject(stock_no_buff).getString("STOCK_NO"));
												messageJson.put("TRADE_TYPE", dataJson.getString("TRADE_TYPE"));
												messageJson.put("STOCK_NAME", currentJson.getJSONObject(stock_no_buff).getString("STOCK_NAME"));
												messageJson.put("CUR_PRICE", dataJson.getString("TRADE_PRICE"));
												messageJson.put("TRADE_NUM", dataJson.getIntValue("TRADE_NUM"));
												messageJson.put("TRADE_TOTAL_PRICE", dataJson.getIntValue("TRADE_TOTAL_PRICE")/10000);
												messageJson.put("TRADE_PRICE", dataJson.getString("TRADE_PRICE"));
												messageJson.put("HIGH_PRICE", currentJson.getJSONObject(stock_no_buff).getString("HIGH_PRICE"));
												messageJson.put("LOW_PRICE", currentJson.getJSONObject(stock_no_buff).getString("LOW_PRICE"));
												messageJson.put("UPS_DOWNS_RATE", currentJson.getJSONObject(stock_no_buff).getString("UPS_DOWNS_RATE"));
												messageJson.put("REFERENCE_AMPLITUDE", currentJson.getJSONObject(stock_no_buff).getString("REFERENCE_AMPLITUDE"));
												messageJson.put("LIMIT_OPEN", currentJson.getJSONObject(stock_no_buff).getString("LIMIT_OPEN"));
												messageJson.put("TURNOVER_RATE", currentJson.getJSONObject(stock_no_buff).getString("TURNOVER_RATE"));
												messageJson.put("TRADE_TIME", dataJson.getString("TRADE_TIME"));
												messageJson.put("CREATETIME", DateTimeUtil.currentTime(DateTimeUtil.DEFAULT_DATE_FORMAT));
//												tradeSqlArray.add(metaDataBO.toSql(messageJson, "message_record"));
												metadataService.execute(metadataService.getsql(messageJson, "message_record"));
											}
											metadataService.execute(metadataService.getsql(dataJson, "trade_detail_record"));
//											tradeSqlArray.add(metaDataBO.toSql(dataJson, "trade_detail_record"));
											reptile_config_json.getJSONObject(stock_no_buff).put("PAGE_INDEX", k+1);
											if(reptile_config_json.getJSONObject(stock_no_buff).getString("CUR_TIME").indexOf(pageArray[k].split("/")[1])>0){
												//reptile_config_json.getJSONObject(stock_no_buff).put("PAGE_STATUS",1);
												if(timeArray.length!=i+1){
													reptile_config_json.getJSONObject(stock_no_buff).put("PAGE_INDEX", 0);
													reptile_config_json.getJSONObject(stock_no_buff).put("CUR_PAGE", i+1);
												}
											}
											
										} 
										
//										if(null!=tradeSqlArray && tradeSqlArray.size()>0){
//											metaDataBO.batch(tradeSqlArray.toArray(new String [tradeSqlArray.size()]));
//										}
									}else{
										continue echo;
									}
								}
							//}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue echo;
				}
//				ST.trade_array.put(stock_no_buff,"isload");
//				metaDataBO.execute("update trade_detail_config set TOTAL_PAGE=TOTAL_PAGE+1,USE_STATUS='0',TOTAL_PAGE="+reptile_config_json.getJSONObject(stock_no_buff).getIntValue("TOTAL_PAGE")+",CUR_PAGE="+reptile_config_json.getJSONObject(stock_no_buff).getIntValue("CUR_PAGE")+",PAGE_INDEX="+reptile_config_json.getJSONObject(stock_no_buff).getIntValue("PAGE_INDEX")+",CUR_TIME='"+reptile_config_json.getJSONObject(stock_no_buff).getString("CUR_TIME")+"' where STOCK_NO_BUFF ='"+stock_no_buff+"'");
//				reptile_config_json.getJSONObject(stock_no_buff).put("USE_STATUS", "0");
				if(DateTimeUtil.misBetween(DateTimeUtil.parseDate("15:00:00", DateTimeUtil.DEFAULT_TIME_FORMAT), DateTimeUtil.parseDate(DateTimeUtil.currentTime(DateTimeUtil.DEFAULT_TIME_FORMAT), DateTimeUtil.DEFAULT_TIME_FORMAT))>=0){
					reptile_config_json.getJSONObject(stock_no_buff).put("REPTILE_NUM", reptile_config_json.getJSONObject(stock_no_buff).getIntValue("REPTILE_NUM")+1);
				}
				
				if(reptile_config_json.getJSONObject(stock_no_buff).getIntValue("REPTILE_NUM")>2){
					reptile_config_json.getJSONObject(stock_no_buff).put("IS_REPTILE", "0");
					resultnum++;
				}
			}
			if(resultnum == stock_list.size()){
				status="2";
			}
			System.out.println(resultnum);
			System.out.println("任务---"+reptileTaskJson.getJSONObject("data").getString("TASK_ID")+"已经收集完成("+status+"). 耗时:"+(System.currentTimeMillis()-starttime));
		} catch (Exception e) {
			
			e.printStackTrace();
			System.out.println("reptile trade details generate exception! message:"+e.getMessage());
		}
	}
	}
	public void repitileStock(List<String> stockList){
		String stock_no_buff="";
		for(String tag:stockList){
			stock_no_buff+=tag+",";
		}
		System.out.println(stock_no_buff);
		System.out.println("http://qt.gtimg.cn/q="+stock_no_buff.substring(0,stock_no_buff.length()-1));
		String	content = httpHelp("http://qt.gtimg.cn/q="+stock_no_buff.substring(0,stock_no_buff.length()-1));
		System.out.println("4444----"+content);
		for(String sing:content.split(";")){
			String tag_stock_buff = sing.trim().substring(2,10);
			sing = sing.substring(sing.indexOf("\"")+1, sing.length()-2);
			String [] stock = sing.split("~");
			System.out.println("http://qt.gtimg.cn/q=ff_"+tag_stock_buff);
			String fundFlow = httpHelp("http://qt.gtimg.cn/q=ff_"+tag_stock_buff);
			
			JSONObject stockJson = new JSONObject();
			if(StringUtils.isNotEmpty(fundFlow) && fundFlow.indexOf("~")>0){
				fundFlow = fundFlow.substring(fundFlow.indexOf("\"")+1, fundFlow.length()-2);
				String [] fundArray = fundFlow.split("~");
				stockJson.put("FUND_MAIN_INFLOW", fundArray[1]);
				stockJson.put("FUND_MAIN_OUTFLOW", fundArray[2]);
				stockJson.put("FUND_MAIN_PURE", fundArray[3]);
				stockJson.put("FUND_MAIN_PURE_RATIO", fundArray[4]);
				stockJson.put("FUND_RETAIL_INFLOW", fundArray[5]);
				stockJson.put("FUND_RETAIL_OUTFLOW", fundArray[6]);
				stockJson.put("FUND_RETAIL_PURE", fundArray[7]);
				stockJson.put("FUND_RETAIL_PURE_RATIO", fundArray[8]);
				stockJson.put("FUND_TOTAL_TRADE", fundArray[9]);
			}
//			JSONObject stockJson = new JSONObject();
				stockJson.put("STOCK_NAME", stock[1]);
				stockJson.put("STOCK_NO", stock[2]);
				stockJson.put("LIMIT_UP", stock[47]);
				stockJson.put("LIMIT_DOWN", stock[48]);
				stockJson.put("TRADE_TOTAL_PRICE", stock[37]);
				stockJson.put("TRADE_TOTAL_NUM", stock[36]);
				stockJson.put("TURNOVER_RATE", stock[38]);
				stockJson.put("HIGH_PRICE", stock[33]);
				stockJson.put("LOW_PRICE", stock[34]);
				stockJson.put("CUR_PRICE", stock[3]);
				stockJson.put("LIMIT_OPEN", stock[5]);
				stockJson.put("UPS_DOWNS_RATE", stock[32]);
				stockJson.put("UPS_DOWNS_PRICE", stock[31]);
				stockJson.put("OUTSIZE", stock[7]);
				stockJson.put("INNERSIZE", stock[8]);
				stockJson.put("TRADE_BUY_PRICE_1", stock[9]);
				stockJson.put("TRADE_BUY_NUMBER_1", stock[10]);
				stockJson.put("TRADE_BUY_PRICE_2", stock[11]);
				stockJson.put("TRADE_BUY_NUMBER_2", stock[12]);
				stockJson.put("TRADE_BUY_PRICE_3", stock[13]);
				stockJson.put("TRADE_BUY_NUMBER_3", stock[14]);
				stockJson.put("TRADE_BUY_PRICE_4", stock[15]);
				stockJson.put("TRADE_BUY_NUMBER_4", stock[16]);
				stockJson.put("TRADE_BUY_PRICE_5", stock[17]);
				stockJson.put("TRADE_BUY_NUMBER_5", stock[18]);
				stockJson.put("TRADE_SALE_PRICE_1", stock[19]);
				stockJson.put("TRADE_SALE_NUMBER_1", stock[20]);
				stockJson.put("TRADE_SALE_PRICE_2", stock[21]);
				stockJson.put("TRADE_SALE_NUMBER_2", stock[22]);
				stockJson.put("TRADE_SALE_PRICE_3", stock[23]);
				stockJson.put("TRADE_SALE_NUMBER_3", stock[24]);
				stockJson.put("TRADE_SALE_PRICE_4", stock[25]);
				stockJson.put("TRADE_SALE_NUMBER_4", stock[26]);
				stockJson.put("TRADE_SALE_PRICE_5", stock[27]);
				stockJson.put("TRADE_SALE_NUMBER_5", stock[28]);
				//stockJson.put("LATELY_TRADE", stock[29]);
				stockJson.put("TOTAL_MARKET_VALUE", stock[45]);
				stockJson.put("CIRCULATE_MARKET_VALUE", stock[44]);
				stockJson.put("REFERENCE_AMPLITUDE", stock[43]);
				stockJson.put("PE", stock[39].length()==0?"0.0":stock[39]);
				stockJson.put("PB", stock[46].length()==0?"0.0":stock[46]);
				stockJson.put("UPDATETIME", DateTimeUtil.currentTime(DateTimeUtil.DEFAULT_DATETIME_FORMAT_SEC));
				try {
				String yesterDayStock = redisService.get("ys"+stockJson.getString("STOCK_NO"));
				if(StringUtils.isNotEmpty(yesterDayStock)){
					
						JSONObject yesterDay = JSONObject.parseObject(yesterDayStock);
						stockJson.put("AMOUNT_RATIO", new BigDecimal((float)stockJson.getIntValue("TRADE_TOTAL_NUM")/yesterDay.getIntValue("TRADE_TOTAL_NUM")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
						if(stockJson.getDoubleValue("AMOUNT_RATIO")>=1 && stockJson.getDoubleValue("AMOUNT_RATIO")<2){
							if(metadataService.queryForList("select * from robot_message where stock_no='"+stock[2]+"' and msg='交易量超过昨日'").size()==0){
								metadataService.execute("insert into robot_message values('"+stock[2]+"','交易量超过昨日','"+DateTimeUtil.currentTime(DateTimeUtil.DEFAULT_DATETIME_FORMAT_SEC)+"')");
							}
							
						}else if(Math.ceil(stockJson.getDoubleValue("AMOUNT_RATIO"))>=2){
							if(metadataService.queryForList("select * from robot_message where stock_no='"+stock[2]+"' and msg='交易量放量"+Math.ceil(stockJson.getDoubleValue("AMOUNT_RATIO"))+"倍'").size()==0){
								metadataService.execute("insert into robot_message values('"+stock[2]+"','交易量放量"+Math.ceil(stockJson.getDoubleValue("AMOUNT_RATIO"))+"倍','"+DateTimeUtil.currentTime(DateTimeUtil.DEFAULT_DATETIME_FORMAT_SEC)+"')");
							}
						}
					
					
				}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//stockJson.put("CREATETIME", DateTimeUtil.currentTime(DateTimeUtil.DEFAULT_DATE_FORMAT));
				for(String key:stockJson.keySet()){
					if(stockJson.getString(key).length()==0){
						stockJson.put(key, "0");
					}
				}
				currentJson.put(tag_stock_buff, stockJson);
				metadataService.execute(metadataService.getsql(stockJson, "stock_record","STOCK_NO",stockJson.getString("STOCK_NO")));
		}
		
	}
	public String httpHelp(String url){
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
