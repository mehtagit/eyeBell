package com.eyebell.server.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.media.ExtendedCachingControl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sun.net.httpserver.HttpHandler;
import com.eyebell.pojo.Request;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

@Component
public class Receiver implements HttpHandler
{
	@Autowired
	DeviceMapper deviceMapper;
	@Override
	public void handle(HttpExchange exchange) throws IOException 
	{
		URI requestURI = exchange.getRequestURI();
		String path = exchange.getRequestURI().getPath();
		InputStream in = exchange.getRequestBody();
		StringBuilder out = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));	
			String line = null;
			while ((line = br.readLine()) != null) {
	            out.append(line);
	        }

		} catch (Exception e) {
			e.printStackTrace();
		}
	    String data = out.toString();
	    data = data.trim();
	    data = data.substring(5, (data.length()-1));
	    System.out.println("before decoeed  [ "+data+" ] ");
	    data = URLDecoder.decode(data);
	    System.out.println("received from client ["+data+"]");
	    Request request  = new Gson().fromJson(data, Request.class);
	    switch (request.getAction())
	    {
		case REGISTER_NEW_DEVICE:
			System.out.println("LOGIN from app : "+data);
			String fcmid = request.getData().get("fcm_id");
			System.out.println("I have received fcm id : "+fcmid);
			deviceMapper.getMappingDb().get(request.getPiId()).setDeviceId(fcmid);;
			break;				
		
		default:
			System.out.println("unable to understand the request");
			break;
		}
	    
        String response = "OK";
        exchange.sendResponseHeaders(200, response.getBytes().length);        
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

	
	
}
