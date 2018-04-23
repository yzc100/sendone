package com.kx.sys.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.CDATA;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kx.metadata.controller.CommResult;
import com.kx.util.CDP;

@Controller
@RequestMapping("dictionary")
public class DictionaryController extends CommResult{
	@RequestMapping(value="list",method=RequestMethod.GET)
	public void list(HttpServletRequest request,HttpServletResponse response){
		responseOutWithJson(response, CDP.dictionaryJson);
	}
}
