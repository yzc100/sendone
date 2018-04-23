package com.poi;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.alibaba.fastjson.JSONObject;

public class ExcelDemo {
	public static void main(String[] args) throws IOException {
		File file = new File("D:\\cw.xls");
	    InputStream is=null;;
	    HSSFWorkbook hssfWorkbook =null;
	    FileOutputStream out = null;   

        FileOutputStream outSTr = null;   

        BufferedOutputStream Buff=null;   

        FileWriter fw = null; 
		try {
			is = new FileInputStream(file);
			out = new FileOutputStream(new File("D:\\cw.txt"));   

			hssfWorkbook= new HSSFWorkbook( is);   
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			
		} catch (IOException e) {
		}  
		
		for(int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++){
		      HSSFSheet hssfSheet = hssfWorkbook.getSheetAt( numSheet);  
		      if(hssfSheet == null){  
		        continue;  
		      }  
		      for(int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++){ 
		        HSSFRow hssfRow = hssfSheet.getRow( rowNum);  
		        if(hssfRow == null){  
		          continue;  
		        }
		        JSONObject dataJson = new JSONObject();
		        // 循环列Cell
		        String mi_name=hssfRow.getCell(1).getStringCellValue();
		        String mi_no=hssfRow.getCell(0).getStringCellValue();
		        String ic_id = hssfRow.getCell(2).getStringCellValue();
		        if(ic_id.substring(0,1).equals("0")){
		        	ic_id = ic_id.substring(1,ic_id.length());
		        }
//		        for(int cellNum = 0; cellNum <= 3; cellNum++){
//		          HSSFCell hssfCell = hssfRow.getCell( cellNum); 
//		          System.out.println(hssfCell.getStringCellValue());
//		        }
		        String member_sql="INSERT INTO TF_MEMBERINFO(MI_ID,MI_NO,MI_NAME,MI_STATUS,CREATETIME,UPDATETIME) VALUES('"+mi_no+"','"+mi_no+"','"+mi_name+"','0','2016-09-18 16:00:00','2016-09-18 16:00:00');\r\n";
		        String card_sql = "INSERT INTO TF_CARDINFO(IC_ID,MI_ID,IC_TYPE,IC_STATUS,CREATETIME,UPDATETIME) VALUES('"+ic_id+"','"+mi_no+"','0','0','2016-09-18 16:00:00','2016-09-18 16:00:00');\r\n";
		        String account_sql_a = "INSERT INTO TF_MEMBER_ACCOUNT_RECORD(ACCOUNT_ID,BALANCE,ACCOUNT_STATUS,MI_ID,ACCOUNT_TYPE,CREATETIME,UPDATETIME) VALUES('account-"+UUID.randomUUID()+"','0','0','"+mi_no+"','0','2016-09-18 16:00:00','2016-09-18 16:00:00');\r\n";
		        String account_sql_b = "INSERT INTO TF_MEMBER_ACCOUNT_RECORD(ACCOUNT_ID,BALANCE,ACCOUNT_STATUS,MI_ID,ACCOUNT_TYPE,CREATETIME,UPDATETIME) VALUES('account-"+UUID.randomUUID()+"','0','0','"+mi_no+"','1','2016-09-18 16:00:00','2016-09-18 16:00:00');\r\n";
		        out.write(member_sql.getBytes());
		        out.write(card_sql.getBytes());
		        out.write(account_sql_a.getBytes());
		        out.write(account_sql_b.getBytes());
		      }
		}
		out.flush();
		out.close();
	}
}
