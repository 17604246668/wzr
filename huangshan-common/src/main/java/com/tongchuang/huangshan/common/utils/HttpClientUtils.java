package com.tongchuang.huangshan.common.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p> Description: (封装http请求工具类) </p>
 */
public class HttpClientUtils {

    public static final Integer HTTP_SUCCESS_CODE = 200;

    /**
     * <p> ClassName: HttpClientUtils </p>
     * <p> Name:  </>
     * <p> Description: (发送带参数的http get请求) </>
     * @params: [url 链接, param url参数]
     * @return: java.lang.String
     */
    public static String doGet(String url, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == HTTP_SUCCESS_CODE) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
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
        return resultString;
    }

    /**
     * <p> ClassName: HttpClientUtils </p>
     * <p> Name: doGet </>
     * <p> Description: (发送httpget请求，参数直接拼接在url中) </>
     * @params: [url] url
     * @return: java.lang.String
     */
    public static String doGet(String url) {
        return doGet(url, null);
    }

    /**
     * <p> ClassName: HttpClientUtils </p>
     * <p> Name: doPost </>
     * <p> Description: (发送带参数的http post请求) </>
     * @params: [url 请求链接, param 请求参数]
     * @return: java.lang.String
     */
    public static String doPost(String url, Map<String, String> param) {
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig)
                .build();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
                    .build();
            httpPost.setConfig(requestConfig);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultString;
    }

    /**
     * <p> ClassName: HttpClientUtils </p>
     * <p> Name: doPost </>
     * <p> Description: (发送不带参数的http post请求) </>
     * @params: [url 请求链接]
     * @return: java.lang.String
     */
    public static String doPost(String url) {
        return doPost(url, null);
    }

    /**
     * <p> ClassName: HttpClientUtils </p>
     * <p> Name: doPostJson </>
     * <p> Description: (发送http post请求并且参数格式为json) </>
     * @params: [url 请求链接, json json字符串格式]
     * @return: java.lang.String
     */
    public static String doPostJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultString;
    }
}
