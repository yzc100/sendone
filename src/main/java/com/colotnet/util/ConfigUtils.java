package com.colotnet.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtils {

    private static String     propPath = File.separator + "config.properties";
//	private static String     propPath = GetProjectRealPath.getPath("config.properties");
			
	
    private static Properties prop     = null;

    static {
    	System.out.println(propPath);
        prop = new Properties();
       
//        ClassLoader.getSystemResourceAsStream(propPath);
        try {
        	 InputStream in =new FileInputStream(new File(propPath));
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String keyName) {
        return prop.getProperty(keyName);
    }

    public static String getProperty(String keyName, String defaultValue) {
        return prop.getProperty(keyName, defaultValue);
    }

}
