package com.kx.stock.cache.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.kx.metadata.service.IMetadataService;
import com.kx.stock.cache.IStockCacheService;
import com.kx.util.ST;
@Service("stockCacheService")
public class StockCacheServiceImpl implements IStockCacheService {
	@Autowired
	IMetadataService metadataService;
	@Override
	public void loadCache() {
		// TODO Auto-generated method stub
		loadAllStock();
	}
	public void loadAllStock(){
		List<Map<String, Object>> list = metadataService.queryForList("SELECT * from stock_record");
		for(Map<String, Object> map:list){
			JSONObject stockJson = (JSONObject)JSONObject.toJSON(map);
			ST.allStock.put(map.get("STOCK_NO_BUFF").toString(), stockJson);
		}
	}
}
