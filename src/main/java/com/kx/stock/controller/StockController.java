package com.kx.stock.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kx.stock.thread.ReptileThread;

@Controller
@RequestMapping("stock")
public class StockController {
	@RequestMapping("reptile")
	public void reptile(HttpServletRequest request,HttpServletResponse response) {
		for(int i=0;i<30;i++){
			ReptileThread reptileThread = new ReptileThread(i);
			Thread thread = new Thread(reptileThread);
			thread.start();
			}
		
		
	}
	
}
