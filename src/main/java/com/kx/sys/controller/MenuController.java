package com.kx.sys.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kx.metadata.controller.CommResult;
import com.kx.util.CDP;

@Controller
@RequestMapping("menu")
public class MenuController extends CommResult{
	@RequestMapping("/list")
	public void list(HttpServletRequest request,HttpServletResponse response){
		responseOutWithJson(response, CDP.menuJson);
	}
}
