package ir.rasen.myapplication.webservice;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import ir.rasen.myapplication.helper.ServerAnswer;

/**
 * Created by android on 12/1/2014.
 */
public class WebservicePOST {
    HttpClient httpclient;
    HttpPost httpPost;
    ArrayList<NameValuePair> postParameters;

    public WebservicePOST(String url) {
        httpclient = new DefaultHttpClient();
        httpPost = new HttpPost(url);
        postParameters = new ArrayList<NameValuePair>();


    }

    public void addParam(String paramName, String paramValue) {
        postParameters.add(new BasicNameValuePair(paramName, paramValue));
    }

    public ServerAnswer execute() throws Exception {
        if (postParameters != null && postParameters.size() != 0)
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
        HttpResponse httpResponse = null;
        httpPost.setHeader("Content-Type","application/json");

        try {
            httpResponse = httpclient.execute(httpPost);
        }
        catch (Exception e){

        }

        return ServerAnswer.get(httpResponse);
    }

    public ServerAnswer executeList() throws Exception {
        if (postParameters != null && postParameters.size() != 0)
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
        HttpResponse httpResponse = null;
        httpPost.setHeader("Content-Type","application/json");

        try {
            httpResponse = httpclient.execute(httpPost);
        }
        catch (Exception e){

        }


        return ServerAnswer.getList(httpResponse);
    }


}
