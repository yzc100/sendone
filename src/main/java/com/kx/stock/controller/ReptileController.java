package com.kx.stock.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.kx.cache.redis.RedisServiceImpl;
import com.kx.metadata.controller.CommResult;
import com.kx.stock.service.IReptileService;

import redis.clients.jedis.Jedis;

@Controller
@RequestMapping("reptile")
public class ReptileController extends CommResult{
	@Autowired
	IReptileService reptileService;
	@Autowired
	RedisServiceImpl redisService;
	
	@RequestMapping(value="run",method=RequestMethod.POST)
	public void runReptileTask(HttpServletRequest request,HttpServletResponse response,@RequestParam("data") String data){
		responseOutWithJson(response,reptileService.runReptileTask(JSONObject.parseObject(data)));
	}
	@RequestMapping(value="hand",method=RequestMethod.GET)
	public void test(HttpServletRequest request,HttpServletResponse response){
		reptileService.autoReptile();
	}
	@RequestMapping(value="load",method=RequestMethod.GET)
	public void loadYesterDayStock(HttpServletRequest request,HttpServletResponse response){
		reptileService.loadYesterDayStock();
	}
}
