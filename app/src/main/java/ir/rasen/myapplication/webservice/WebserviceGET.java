package ir.rasen.myapplication.webservice;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;

import helper.ServerAnswer;


/**
 * Created by android on 12/1/2014.
 */
public class WebserviceGET {
    HttpClient httpclient;
    HttpGet httpGet;


    public WebserviceGET(String url, ArrayList<String> paramsList) {
        httpclient = new DefaultHttpClient();
        for (String param: paramsList){
            url += "/"+param;
        }
        httpGet = new HttpGet(url);

    }

    public ServerAnswer execute() throws Exception {
        HttpResponse httpResponse = httpclient.execute(httpGet);

        return ServerAnswer.get(httpResponse);
    }

    public ServerAnswer executeList() throws Exception {
        HttpResponse httpResponse = httpclient.execute(httpGet);

        return ServerAnswer.getList(httpResponse);
    }


}
