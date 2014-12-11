package com.androd.bugreporter.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.androd.bugreporter.MyApplication;

public class HttpServer {
	
	
	   public static String makeUriString(boolean isSsl, String urlSuffix){
	        String us = "";
	        
	        if(isSsl){
	        	us = "https" + "://" + "www.upseven.net/"+urlSuffix;
	        	//us = "https" + "://" + "10.1.12.152:8080/"+urlSuffix;
	        	//us = "https" + "://" + "www.hellokitty1.88ip.cn:1100/"+urlSuffix;
	        }else{
	        	us = "http" + "://" + "www.upseven.net/"+urlSuffix;
	        	//us = "http" + "://" + "www.hellokitty1.88ip.cn:1100/"+urlSuffix;
	        	//us = "http" + "://" + "10.1.12.152:8080/"+urlSuffix;
	        }
	        return us;
	    }
	   
	   
	/**
	 * 
	 * @param isSsl
	 * @param urlSuffix 后缀
	 * @param body
	 * @return
	 * @throws IOException
	 */
	public HttpResponse sendHttpClient(boolean isSsl, String urlSuffix, String body)
			throws IOException {
		HttpClient client = new DefaultHttpClient();
		String us = makeUriString(isSsl, urlSuffix);
		Log.i(MyApplication.TAG, "上传服务器url：" + us);
		HttpPost method = new HttpPost(URI.create(us));
		method.setHeader("Content-Type","application/json");
		//IShareApp app = MyApplication.getInstance().getApp();
		/*if(!app.getSessionId().equals("")){
			method.addHeader("Cookie", "JSESSIONID="+app.getSessionId()+"; Path=/; HttpOnly");
		}*/
		/*if(app.getUser().authority!=null && !app.getUser().authority.equals("")){
			method.addHeader("SetAuthority", URLEncoder.encode(app.getUser().authority));
		}*/
		if(body != null && !body.equals("")){
			StringEntity entity = new StringEntity(body, "UTF-8");
			method.setEntity(entity);
		}
		return client.execute(method);
	}
	
	public HttpResponse sendFile(boolean isSsl, String urlSuffix, String filePath) throws ClientProtocolException, IOException{
        HttpClient httpclient = new DefaultHttpClient();  
        //IShareApp app = MyApplication.getInstance().getApp();
		String us = makeUriString(isSsl, urlSuffix);
		HttpPost post = new HttpPost(URI.create(us));
		File file = new File(filePath);
		FileEntity fileentity;

		fileentity = new FileEntity(file, "image/jpeg;");
		fileentity.setChunked(true);
		post.setEntity(fileentity);

		post.addHeader("Content-Type", "image/jpeg;");
		/*if(app.getUser().authority!=null && !app.getUser().authority.equals("")){
			post.addHeader("SetAuthority", URLEncoder.encode(app.getUser().authority));
		}*/
		/*
		if(!app.getSessionId().equals("")){
			post.addHeader("Cookie", "JSESSIONID="+app.getSessionId()+"; Path=/; HttpOnly");
		}
		*/
		return httpclient.execute(post);
	}
	
    public static String getResponse(HttpResponse httpResponse) throws IOException, IllegalStateException{

        StringBuilder builder = new StringBuilder();
        BufferedReader bufferedReader2 = new BufferedReader(
                new InputStreamReader(httpResponse.getEntity().getContent()));

        for (String s = bufferedReader2.readLine(); s != null; s = bufferedReader2
                .readLine()) {
            builder.append(s);
        } 
        
        return builder.toString();
    }
}
