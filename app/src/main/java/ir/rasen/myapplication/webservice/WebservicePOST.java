package ir.rasen.myapplication.webservice;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ServerAnswer;

/**
 * Created by android on 12/1/2014.
 */
public class WebservicePOST {
    HttpClient httpclient;
    HttpPost httpPost;
    JSONObject jsonParams;

    public WebservicePOST(String url) {
        httpclient = new DefaultHttpClient();
        httpPost = new HttpPost(url);
        jsonParams = new JSONObject();
    }

    public void addParam(String paramName, String paramValue) throws Exception {
        if (paramValue != null)
            jsonParams.put(paramName, paramValue);
        else
            jsonParams.put(paramName, "");
    }

    private HttpResponse run(HttpPost httpPost) throws Exception {
        HttpResponse httpResponse = null;
        StringEntity params = new StringEntity(jsonParams.toString());
        httpPost.setEntity(params);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Accept", "application/json");

        try {
            httpResponse = httpclient.execute(httpPost);
        } catch (Exception e) {
            String s = e.getMessage();
        }

        return httpResponse;
    }

    public ServerAnswer execute() throws Exception {
        HttpResponse httpResponse = run(httpPost);
        return ServerAnswer.get(httpResponse);
    }

    public ServerAnswer executeList() throws Exception {
        HttpResponse httpResponse = run(httpPost);
        return ServerAnswer.getList(httpResponse);
    }


}
