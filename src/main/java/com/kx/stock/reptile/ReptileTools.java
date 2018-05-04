package com.kx.stock.reptile;

import com.kx.util.HttpClient;

public class ReptileTools {
	public String HttpGet(String url){
		HttpClient httpClient = new HttpClient();
		String content =null;
		while(true) {
			try {
				content = httpClient.httpGet(url, "gb18030").trim();
				if(content!=null) { break;}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return content;
	}
}
