package ir.rasen.myapplication.webservice;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;

import ir.rasen.myapplication.helper.ServerAnswer;


/**
 * Created by android on 12/1/2014.
 */
public class WebserviceGET {
    HttpClient httpclient;
    HttpGet httpGet;


    public WebserviceGET(String url, ArrayList<String> paramsList) {
        httpclient = new DefaultHttpClient();
        if (paramsList != null) {
            for (String param : paramsList) {
                url += "/" + param;
            }
        }
        httpGet = new HttpGet(url);

    }

    public ServerAnswer execute() throws Exception {
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpclient.execute(httpGet);
        } catch (Exception e) {

        }
        return ServerAnswer.get(httpResponse);
    }

    public ServerAnswer executeList() throws Exception {
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpclient.execute(httpGet);
        } catch (Exception e) {

        }

        return ServerAnswer.getList(httpResponse);
    }


}
