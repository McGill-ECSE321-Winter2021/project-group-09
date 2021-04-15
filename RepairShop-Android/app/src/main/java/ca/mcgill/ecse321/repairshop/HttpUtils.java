package ca.mcgill.ecse321.repairshop;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpHeaders;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

public class HttpUtils {

    // This is the same as in the tutorial

    public static final String DEFAULT_BASE_URL = "https://repairshop-backend-ecse321-09.herokuapp.com/";

    private static String baseUrl;
    private static final AsyncHttpClient client = new AsyncHttpClient();

    static {
        baseUrl = DEFAULT_BASE_URL;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static void setBaseUrl(String baseUrl) {
        HttpUtils.baseUrl = baseUrl;
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    // Get request with token in header
    public static void get(Context context, String url, String token, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        BasicHeader[] headers = new BasicHeader[]{ new BasicHeader("token", token)};
        client.get(context, getAbsoluteUrl(url), headers, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(Context context, String url, String token, JSONObject body, AsyncHttpResponseHandler responseHandler) throws UnsupportedEncodingException {
        BasicHeader[] headers = new BasicHeader[]{ new BasicHeader("token", token)};
        StringEntity stringEntity = new StringEntity(body.toString());
        client.post(context, getAbsoluteUrl(url), headers, stringEntity, "application/json", responseHandler);
    }
    
    public static void postWithBody(Context context, String url, JSONObject body, AsyncHttpResponseHandler responseHandler) throws UnsupportedEncodingException {
        StringEntity stringEntity = new StringEntity(body.toString());
        client.post(context, getAbsoluteUrl(url), stringEntity, "application/json", responseHandler);
    }

    public static void getByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return baseUrl + relativeUrl;
    }

}