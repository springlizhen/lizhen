package com.chinags.layer.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * JAVA 模拟HTTP请求， 支持使用doGet、doPost请求。doPost支持Map<Key,Value>、JOSN模式数据
 * 
 * @author tool
 *
 */
public class HttpUtil {



    /**
     * doGet请求
     * 
     * @return
     */
    public static String doGet(String url, String token) {
        String strResult = "";
        try {
            HttpClient client = new DefaultHttpClient();
            // 发送get请求
            HttpGet request = new HttpGet(url);
            request.setHeader("Authorization", "Bearer " + token);
            HttpResponse response = client.execute(request);
            int code = response.getStatusLine().getStatusCode();
            /** 请求发送成功，并得到响应 **/
            if (code == HttpStatus.SC_OK) {
                /** 读取服务器返回过来的json字符串数据 **/
                strResult = EntityUtils.toString(response.getEntity());
                return strResult;
            }
            strResult = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strResult;
    }
    public static String doPost(String url, Map<String, Object> params) {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        //httpPost.setHeader("Authorization", "Bearer " + token);
        //MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        HttpEntity entity = MultipartEntityBuilder.create()
                // .addPart("bin", bin)
                //  .addPart("comment", comment)
                .addPart("username",  new StringBody((String)params.get("username"), ContentType.TEXT_PLAIN))
                .addPart("password", new StringBody((String)params.get("password"), ContentType.TEXT_PLAIN))
                .build();
        //builder.addTextBody("json", params.toString(), ContentType.MULTIPART_FORM_DATA);
        //HttpEntity entity = builder.build();

        //String charSet = "UTF-8";
        //StringEntity entity = new StringEntity(JSONObject.toJSONString(params), charSet);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;

        try {
            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity);
                return jsonString;
            } else {
                //logger.error("请求返回:" + state + "(" + url + ")");
            }
        } catch (IOException e) {
            //logger.error("httpclient.execute(httpPost) 请求返回异常");
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
