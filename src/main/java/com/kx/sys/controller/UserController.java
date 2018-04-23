package com.kx.sys.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.kx.metadata.controller.CommResult;
import com.kx.sys.service.IUserService;

@Controller
@RequestMapping("user")
public class UserController extends CommResult{
	@Autowired
	IUserService userService;
	@RequestMapping(value="login",method=RequestMethod.POST)
	public void login(HttpServletRequest request,HttpSession session,HttpServletResponse response,@RequestParam("data") String data) {
		JSONObject resultJson = userService.login(JSONObject.parseObject(data));
		if(resultJson.getBooleanValue("result")){
			session.setAttribute("user", resultJson.getJSONObject("data"));
			//request.getSession().setAttribute("user", resultJson.getJSONObject("data"));
		}
		responseOutWithJson(response, resultJson);
	}
	@RequestMapping(value="session")
	public void session(HttpServletRequest request,HttpSession session,HttpServletResponse response) {
		responseOutWithJson(response, JSONObject.toJSONString(session.getAttribute("user")));
	}
}
