package com.kx.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
public class HttpClient {
	static final String CHARSET = "UTF-8"; 
	RequestConfig defaultRequestConfig;
	CloseableHttpClient httpClient;
	public HttpClient(){
 		defaultRequestConfig= RequestConfig.custom().setSocketTimeout(120*1000).setConnectTimeout(120*1000).setConnectionRequestTimeout(120*1000).setStaleConnectionCheckEnabled(true).build();
 		httpClient= HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
 	}
	/**
     * POST��ʽ����http����
     */
	public JSONObject postForm(String url,String content) {
		
		StringBuffer stringBuffer =  new StringBuffer();
		// 创建默认的httpClient实例.  
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
		// 创建httppost  
		HttpPost httppost = new HttpPost(url);
		// 创建参数队列  
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("data", content));
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			System.out.println("executing request " + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					stringBuffer.append(EntityUtils.toString(entity, "UTF-8"));
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
//			JOptionPane.showMessageDialog(null, "无法连接服务器", "message", JOptionPane.OK_OPTION);
//			e.printStackTrace();
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return JSONObject.parseObject(stringBuffer.toString());
	}
    public String httppost(String url,Map<String,String> dataMap){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost post = new HttpPost(url);          //�������ϱ�����ĳ������������
            //���������б�
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            for(Entry<String, String> entry:dataMap.entrySet()){
            	list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
//            list.add(new BasicNameValuePair("j_username", "admin"));
//            list.add(new BasicNameValuePair("j_password", "admin"));
            //url��ʽ����
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list,"UTF-8");
            post.setEntity(uefEntity);
            System.out.println("POST ����...." + post.getURI());
            //ִ������
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            try{
                HttpEntity entity = httpResponse.getEntity();
                if (null != entity){
                    System.out.println("-------------------------------------------------------");
                    System.out.println(EntityUtils.toString(uefEntity));
                    System.out.println("-------------------------------------------------------");
                	return EntityUtils.toString(uefEntity);
//                	entity.getContent()
                }
            } finally{
                httpResponse.close();
            }
             
        } catch( UnsupportedEncodingException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try{
            	if (httpClient != null){
            		httpClient.close();
                }            
            } catch(Exception e){
                e.printStackTrace();
            }
        }
         return null;
    }
	public String sendGet(String httpurl,String charset) throws IOException{
		String content ="";
		URL url = new URL(httpurl);
		URLConnection conn = url.openConnection(); 
		conn.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 5.0; Windows XP; DigExt)");
		//10S ��ʱ
		conn.setConnectTimeout(10000);
		InputStream is = conn.getInputStream(); 
		byte[] bts = new byte[2048]; 
		ByteArrayOutputStream bout = new ByteArrayOutputStream(); 
		int n; 
		while ((n = is.read(bts)) != -1) { 
			content+= new String(bts,charset);
			bts = new byte[2048]; 
		}
		return content;
	}
	public String httpGet(String httpurl,String charset){
		CloseableHttpClient httpclient = HttpClients.createDefault();
//		httpclient.getParams().setIntParameter("http.socket.timeout", 10000);
        try {  
            // ����httpget.    
            HttpGet httpget = new HttpGet(httpurl);  
            // ִ��get����.    
            CloseableHttpResponse response = httpclient.execute(httpget);  
            try {  
                // ��ȡ��Ӧʵ��    
                HttpEntity entity = response.getEntity();  
                // ��ӡ��Ӧ״̬    
                if (entity != null) {  
                    // ��ӡ��Ӧ���ݳ���    
                   // System.out.println("Response content length: " + entity.getContentLength());  
                    // ��ӡ��Ӧ����    
                  //  System.out.println("Response content: " + EntityUtils.toString(entity));  
                	return EntityUtils.toString(entity);
                }  
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
           // e.printStackTrace();  
        } catch (ParseException e) {  
           // e.printStackTrace();  
        } catch (IOException e) {  
           // e.printStackTrace();  
        } finally {  
            // �ر�����,�ͷ���Դ    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        } 
        return null;
	}
	public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // �򿪺�URL֮�������
            URLConnection conn = realUrl.openConnection();
            // ����ͨ�õ���������
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // ����POST�������������������
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // ��ȡURLConnection�����Ӧ�������
            out = new PrintWriter(conn.getOutputStream());
            // �����������
            out.print(param);
            // flush������Ļ���
            out.flush();
            // ����BufferedReader����������ȡURL����Ӧ
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),CHARSET));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("���� POST ��������쳣��"+e);
            e.printStackTrace();
        }
        //ʹ��finally�����ر��������������
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }   
	public static void main(String[] args) {
		HttpClient httpClient = new HttpClient();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("data", "123132");
		String content = httpClient.httpGet("http://stock.gtimg.cn/data/view/bdrank.php?&t=01/averatio&p=1&o=0&l=200&v=list_data", "gbk");
		System.out.println(content);
		content = content.substring(content.indexOf("data:'")+6,content.lastIndexOf("'"));

	
	}
}
