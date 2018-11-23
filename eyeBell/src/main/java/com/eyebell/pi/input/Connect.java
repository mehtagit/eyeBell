package com.eyebell.pi.input;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eyebell.pi.lib.ClientMain;

public class Connect extends HttpServlet 
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		try 
		{
			System.out.println("client sending connect request");
			PiApp.client.connectToServer();
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	

}
