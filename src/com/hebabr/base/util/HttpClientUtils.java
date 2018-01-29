package com.hebabr.base.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils {
	
    private static RequestConfig requestConfig = RequestConfig.custom()  
            .setSocketTimeout(15000)  
            .setConnectTimeout(15000)  
            .setConnectionRequestTimeout(15000)  
            .build();  
      
    private static HttpClientUtils instance = null;  
    private HttpClientUtils(){}  
    public static HttpClientUtils getInstance(){  
        if (instance == null) {  
            instance = new HttpClientUtils();  
        }  
        return instance;  
    }
	
	public static String sendHttpGet(String httpUrl) {  
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求  
        return sendHttpGet(httpGet);  
    } 
	
    /** 
     * 发送Get请求 
     * @param httpPost 
     * @return 
     */  
    private static String sendHttpGet(HttpGet httpGet) {  
        CloseableHttpClient httpClient = null;  
        CloseableHttpResponse response = null;  
        HttpEntity entity = null;  
        String responseContent = null;  
        try {  
            // 创建默认的httpClient实例.  
            httpClient = HttpClients.createDefault();  
            httpGet.setConfig(requestConfig);  
            // 执行请求  
            response = httpClient.execute(httpGet);  
            entity = response.getEntity();  
            responseContent = EntityUtils.toString(entity, "UTF-8");  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                // 关闭连接,释放资源  
                if (response != null) {  
                    response.close();  
                }  
                if (httpClient != null) {  
                    httpClient.close();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return responseContent;  
    }
    
    public static void main(String[] args) {
    	System.out.println(sendHttpGet("https://api.weixin.qq.com/sns/jscode2session?appid=wx7ae0c608fbc66e0f&secret=472214f356a074f7c8fe1f56ca523c87&js_code=013lZfBR1MdNH61U8cBR1pKvBR1lZfB1&grant_type=authorization_code"));
    }

}
