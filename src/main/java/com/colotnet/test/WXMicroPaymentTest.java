package com.colotnet.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.colotnet.util.ConfigUtils;
import com.colotnet.util.SSLClient;
import com.colotnet.util.SignUtils;

/**
 * 类WXMicroPaymentTest.java的实现描述：微信扫码支付
 * @author tulu 2016年5月9日 下午4:32:06
 */
public class WXMicroPaymentTest {
    public static void main(String[] args) throws Exception {
        DefaultHttpClient httpClient = new SSLClient();
        HttpPost postMethod = new HttpPost(ConfigUtils.getProperty("trans_url"));
        List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
        nvps.add(new BasicNameValuePair("requestNo", new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())));//
        nvps.add(new BasicNameValuePair("version", "V1.0"));
        nvps.add(new BasicNameValuePair("productId", "0106"));//支付模式
        nvps.add(new BasicNameValuePair("transId", "10"));
        nvps.add(new BasicNameValuePair("merNo", ConfigUtils.getProperty("merchant_no")));//商户号填我们商户号
        nvps.add(new BasicNameValuePair("chnlsubmerno","100000002182"));//你们的商户号
        nvps.add(new BasicNameValuePair("orderDate", new SimpleDateFormat("yyyyMMdd").format(new Date())));//商户日期
        nvps.add(new BasicNameValuePair("orderNo", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));//订单号
        nvps.add(new BasicNameValuePair("authCode","130106681829829937"
        		+ ""
        		+ ""
        		+ ""
        		+ ""
        		+ ""));
        nvps.add(new BasicNameValuePair("notifyUrl", "www.baidu.com")); //回调接口
        nvps.add(new BasicNameValuePair("transAmt", "5"));//金额
        nvps.add(new BasicNameValuePair("subMerNo", "0000012"));//subMerNo
        
        nvps.add(new BasicNameValuePair("commodityName", "test"));//商品名称
        nvps.add(new BasicNameValuePair("subMerName", "test"));//收款商户名称或分店名称等
        nvps.add(new BasicNameValuePair("signature", SignUtils.signData(nvps)));
        try {
            postMethod.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            HttpResponse resp = httpClient.execute(postMethod);
            String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
            int statusCode = resp.getStatusLine().getStatusCode();
            if (200 == statusCode) {
                boolean signFlag = SignUtils.verferSignData(str);
                if (!signFlag) {
                    System.out.println("验签失败");
                    return;
                }
                System.out.println("验签成功");
                return;
            }
            System.out.println("返回错误码:" + statusCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
