package com.kx.stock.reptile;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.kx.listener.SpringContextHolder;
import com.kx.metadata.service.IMetadataService;
import com.kx.util.DateTimeUtil;
import com.kx.util.ST;

public class StockViewThread extends ReptileTools implements Runnable {
	IMetadataService metadataService= SpringContextHolder.getBean("metadataService");
	private List<String> stockList = new ArrayList<String>();
	public StockViewThread(List<String> stockList) {
		this.stockList = stockList;
	}
	@Override
	public void run() {
		String stock_no_buff="";
		for(String tag:stockList){
			stock_no_buff+=tag+",";
		}
		stock_no_buff = stock_no_buff.substring(0,stock_no_buff.length()-1);
		while(true) {
			try {
				reptile(stock_no_buff);
				if(DateTimeUtil.misBetween(DateTimeUtil.parseDate("15:30:00",DateTimeUtil.DEFAULT_TIME_FORMAT), DateTimeUtil.getCurDate(DateTimeUtil.DEFAULT_TIME_FORMAT))>=0){
					break;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void reptile(String stocks) throws Exception {
		String	content = HttpGet("http://qt.gtimg.cn/q="+stocks);
		
		List<String> sqls = new ArrayList<>();
		for(String sing:content.split(";")){
			System.out.println(sing);
		
			String tag_stock_buff = sing.trim().substring(2,10);
			System.out.println("http://qt.gtimg.cn/q=ff_"+tag_stock_buff);
			sing = sing.substring(sing.indexOf("\"")+1, sing.length()-2);
			String [] stock = sing.split("~");
			String fundFlow = HttpGet("http://qt.gtimg.cn/q=ff_"+tag_stock_buff);
//			System.out.println("http://qt.gtimg.cn/q=ff_"+tag_stock_buff);
			if(StringUtils.isNotEmpty(fundFlow) && fundFlow.indexOf("~")>0){
				fundFlow = fundFlow.substring(fundFlow.indexOf("\"")+1, fundFlow.length()-2);
				String [] fundArray = fundFlow.split("~");
				ST.allStock.getJSONObject(tag_stock_buff).put("FUND_MAIN_INFLOW", fundArray[1]);
				ST.allStock.getJSONObject(tag_stock_buff).put("FUND_MAIN_OUTFLOW", fundArray[2]);
				ST.allStock.getJSONObject(tag_stock_buff).put("FUND_MAIN_PURE", fundArray[3]);
				ST.allStock.getJSONObject(tag_stock_buff).put("FUND_MAIN_PURE_RATIO", fundArray[4]);
				ST.allStock.getJSONObject(tag_stock_buff).put("FUND_RETAIL_INFLOW", fundArray[5]);
				ST.allStock.getJSONObject(tag_stock_buff).put("FUND_RETAIL_OUTFLOW", fundArray[6]);
				ST.allStock.getJSONObject(tag_stock_buff).put("FUND_RETAIL_PURE", fundArray[7]);
				ST.allStock.getJSONObject(tag_stock_buff).put("FUND_RETAIL_PURE_RATIO", fundArray[8]);
				ST.allStock.getJSONObject(tag_stock_buff).put("FUND_TOTAL_TRADE", fundArray[9]);
			}
				ST.allStock.getJSONObject(tag_stock_buff).put("STOCK_NAME", stock[1]);
				ST.allStock.getJSONObject(tag_stock_buff).put("LIMIT_UP", stock[47]);
				ST.allStock.getJSONObject(tag_stock_buff).put("LIMIT_DOWN", stock[48]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_TOTAL_PRICE", stock[37]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_TOTAL_NUM", stock[36]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TURNOVER_RATE", stock[38]);
				ST.allStock.getJSONObject(tag_stock_buff).put("HIGH_PRICE", stock[33]);
				ST.allStock.getJSONObject(tag_stock_buff).put("LOW_PRICE", stock[34]);
				ST.allStock.getJSONObject(tag_stock_buff).put("CUR_PRICE", stock[3]);
				ST.allStock.getJSONObject(tag_stock_buff).put("LIMIT_OPEN", stock[5]);
				ST.allStock.getJSONObject(tag_stock_buff).put("UPS_DOWNS_RATE", stock[32]);
				ST.allStock.getJSONObject(tag_stock_buff).put("UPS_DOWNS_PRICE", stock[31]);
				ST.allStock.getJSONObject(tag_stock_buff).put("OUTSIZE", stock[7]);
				ST.allStock.getJSONObject(tag_stock_buff).put("INNERSIZE", stock[8]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_BUY_PRICE_1", stock[9]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_BUY_NUMBER_1", stock[10]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_BUY_PRICE_2", stock[11]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_BUY_NUMBER_2", stock[12]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_BUY_PRICE_3", stock[13]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_BUY_NUMBER_3", stock[14]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_BUY_PRICE_4", stock[15]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_BUY_NUMBER_4", stock[16]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_BUY_PRICE_5", stock[17]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_BUY_NUMBER_5", stock[18]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_SALE_PRICE_1", stock[19]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_SALE_NUMBER_1", stock[20]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_SALE_PRICE_2", stock[21]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_SALE_NUMBER_2", stock[22]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_SALE_PRICE_3", stock[23]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_SALE_NUMBER_3", stock[24]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_SALE_PRICE_4", stock[25]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_SALE_NUMBER_4", stock[26]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_SALE_PRICE_5", stock[27]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TRADE_SALE_NUMBER_5", stock[28]);
				//ST.allStock.getJSONObject(tag_stock_buff).put("LATELY_TRADE", stock[29]);
				ST.allStock.getJSONObject(tag_stock_buff).put("TOTAL_MARKET_VALUE", stock[45]);
				ST.allStock.getJSONObject(tag_stock_buff).put("CIRCULATE_MARKET_VALUE", stock[44]);
				ST.allStock.getJSONObject(tag_stock_buff).put("REFERENCE_AMPLITUDE", stock[43]);
				ST.allStock.getJSONObject(tag_stock_buff).put("PE", stock[39].length()==0?"0.0":stock[39]);
				ST.allStock.getJSONObject(tag_stock_buff).put("PB", stock[46].length()==0?"0.0":stock[46]);
				ST.allStock.getJSONObject(tag_stock_buff).put("UPDATETIME", DateTimeUtil.currentTime(DateTimeUtil.DEFAULT_DATETIME_FORMAT_SEC));
				ST.allStock.getJSONObject(tag_stock_buff).put("CREATETIME", DateTimeUtil.currentTime(DateTimeUtil.DEFAULT_DATE_FORMAT));
				for(String key:ST.allStock.getJSONObject(tag_stock_buff).keySet()){
					if(ST.allStock.getJSONObject(tag_stock_buff).getString(key).length()==0){
						ST.allStock.getJSONObject(tag_stock_buff).put(key, "0");
					}
				}
				sqls.add(metadataService.getsql(ST.allStock.getJSONObject(tag_stock_buff), "stock_record","STOCK_NO",ST.allStock.getJSONObject(tag_stock_buff).getString("STOCK_NO")));
	}
		metadataService.batchUpdate(sqls.toArray(new String[sqls.size()]));
	}
}
