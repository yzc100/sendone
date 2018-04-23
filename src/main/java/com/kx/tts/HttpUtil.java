package com.kx.tts;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;
import  org.slf4j.Logger;
import  org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;
@SuppressWarnings("restriction")
public class HttpUtil {
    static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    /*
     * 计算MD5+BASE64
     */
    public static String MD5Base64(byte[] s) throws UnsupportedEncodingException {
        if (s == null){
            return null;
        }
        String encodeStr = "";
        //string 编码必须为utf-8
        MessageDigest mdTemp;
        try {
            mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(s);
            byte[] md5Bytes = mdTemp.digest();
            BASE64Encoder b64Encoder = new BASE64Encoder();
            encodeStr = b64Encoder.encode(md5Bytes);
            /* java 1.8以上版本支持
            Encoder encoder = Base64.getEncoder();
            encodeStr = encoder.encodeToString(md5Bytes);
            */
        } catch (Exception e) {
            throw new Error("Failed to generate MD5 : " + e.getMessage());
        }
        return encodeStr;
    }
    /*
     * 计算 HMAC-SHA1
     */
    public static String HMACSha1(String data, String key) {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = (new BASE64Encoder()).encode(rawHmac);
            /*java 1.8以上版本支持
            Encoder encoder = Base64.getEncoder();
            result = encoder.encodeToString(rawHmac);
            */
        } catch (Exception e) {
            throw new Error("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }
    /*
     * 等同于javaScript中的 new Date().toUTCString();
     */
    public static String toGMTString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.UK);
        df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }
    /*
     * 发送POST请求
     */
    public static HttpResponse sendAsrPost(byte[] audioData, String audioFormat, int sampleRate, String url,String ak_id, String ak_secret) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        HttpResponse response = new HttpResponse();
        try {
            URL realUrl = new URL(url);
            /*
             * http header 参数
             */
            String method = "POST";
            String accept = "application/json";
            String content_type = "audio/"+audioFormat+";samplerate="+sampleRate;
            int length = audioData.length;
            String date = toGMTString(new Date());
            // 1.对body做MD5+BASE64加密
            String bodyMd5 = MD5Base64(audioData);
            String md52 = MD5Base64(bodyMd5.getBytes());
            String stringToSign = method + "\n" + accept + "\n" + md52 + "\n" + content_type + "\n" + date ;
            // 2.计算 HMAC-SHA1
            String signature = HMACSha1(stringToSign, ak_secret);
            // 3.得到 authorization header
            String authHeader = "Dataplus " + ak_id + ":" + signature;
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", accept);
            conn.setRequestProperty("content-type", content_type);
            conn.setRequestProperty("date", date);
            conn.setRequestProperty("Authorization", authHeader);
            conn.setRequestProperty("Content-Length", String.valueOf(length));
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            OutputStream stream = conn.getOutputStream();
            // 发送请求参数
            stream.write(audioData);
            // flush输出流的缓冲
            stream.flush();
            stream.close();
            response.setStatus(conn.getResponseCode());
            // 定义BufferedReader输入流来读取URL的响应
            if (response.getStatus() ==200){
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }else {
                in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            if (response.getStatus() == 200){
                response.setResult(result);
                response.setMassage("OK");
            }else {
                response.setMassage(result);
            }
            System.out.println("post response status code: ["+response.getStatus()+"], response massage : ["+response.getMassage()+"] ,result :["+response.getResult()+"]");
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return response;
    }
    /*
     * 发送POST请求
     */
    public static HttpResponse sendTtsPost(String textData,String audioType, String audioName,String url,String ak_id, String ak_secret) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        HttpResponse response = new HttpResponse();
        try {
            URL realUrl = new URL(url);
            /*
             * http header 参数
             */
            String method = "POST";
            String content_type = "text/plain";
            String accept = "audio/"+audioType+",application/json";
            int length = textData.length();
            String date = toGMTString(new Date());
            // 1.对body做MD5+BASE64加密
            String bodyMd5 = MD5Base64(textData.getBytes("UTF-8"));
            String stringToSign = method + "\n" + accept + "\n" + bodyMd5 + "\n" + content_type + "\n" + date ;
            // 2.计算 HMAC-SHA1
            String signature = HMACSha1(stringToSign, ak_secret);
            // 3.得到 authorization header
            String authHeader = "Dataplus " + ak_id + ":" + signature;
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", accept);
            conn.setRequestProperty("content-type", content_type);
            conn.setRequestProperty("date", date);
            conn.setRequestProperty("Authorization", authHeader);
            conn.setRequestProperty("Content-Length", String.valueOf(length));
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            OutputStream stream = conn.getOutputStream();
            // 发送请求参数
            stream.write(textData.getBytes("UTF-8"));
            // flush输出流的缓冲
            stream.flush();
            stream.close();
            response.setStatus(conn.getResponseCode());
            // 定义BufferedReader输入流来读取URL的响应
            InputStream is = null;
            String line = null;
            if (response.getStatus() ==200){
                is=conn.getInputStream();
            }else {
                in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            }
            FileOutputStream fileOutputStream = null;
            File ttsFile = new File(audioName+"."+audioType);
            fileOutputStream = new FileOutputStream(ttsFile);
            byte[] b=new byte[1024];
            int len=0;
            while(is!=null&&(len=is.read(b))!=-1){  //先读到内存
                fileOutputStream.write(b, 0, len);
            }
            if (response.getStatus() == 200){
                response.setResult(result);
                response.setMassage("OK");
                System.out.println("post response status code: ["+response.getStatus()+"], generate tts audio file :" + audioName+"."+audioType);
            }else {
                response.setMassage(result);
                System.out.println("post response status code: ["+response.getStatus()+"], response massage : ["+response.getMassage()+"]");
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return response;
    }
}
