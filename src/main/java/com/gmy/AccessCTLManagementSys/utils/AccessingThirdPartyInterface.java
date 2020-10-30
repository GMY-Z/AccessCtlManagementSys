package com.gmy.AccessCTLManagementSys.utils;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * AccessingThirdPartyInterface class
 * 获取第三方接口
 * @author  ZhangJunwei
 * @date 2018/09/21
 */
public class AccessingThirdPartyInterface {
    String url;
    public String getUrl() { return url; }
    public void setUrl(String url) {  this.url = url; }

    public JSONObject getAccessResult(String url) {
        JSONObject result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                //获取响应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String content = EntityUtils.toString(entity);
                    result = JSONObject.fromObject(content);
                    return  result;
                }
            } finally {
                response.close();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  result;
    }

    public static String httppost(String url ,String dataSource){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        httpPost.setHeader("Accept", "application/json");    //接收报文类型
        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");   //发送报文类型
        if (dataSource != null && !"".equals(dataSource)) {
            StringEntity entity = new StringEntity(dataSource, "UTF-8");
            entity.setContentEncoding("UTF-8");
            httpPost.setEntity(entity);
        }
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity, "UTF-8");
                return jsonString;
            } else {
                System.out.println(state);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "fail";
    }
}
