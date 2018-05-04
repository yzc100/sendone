package com.kx.stock.service;

import com.alibaba.fastjson.JSONObject;

public interface IReptileService {
	public JSONObject runReptileTask(JSONObject paramsJson);
	public void autoReptile();
	public void loadYesterDayStock();
}
