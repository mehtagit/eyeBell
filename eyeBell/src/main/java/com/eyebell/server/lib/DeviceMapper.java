package com.eyebell.server.lib;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.eyebell.util.Utillity;
import com.eyebell.pojo.Request;

@Component
public class DeviceMapper 
{

	private HashMap<Session,String> piSessions = new HashMap<Session,String>();
	private HashMap<String,Session> piToSessions = new HashMap<String,Session>();

	//private HashMap<>
	private Connection connection;
	
	@Autowired @Qualifier("utillity")
	private Utillity utillity;
	
	@PostConstruct
	public void postConstruct()
	{
		connection = utillity.getConnection();
		System.out.println("got conection in device ["+connection+"]");
	}
	
	public void saveSession(Session session,String piId)
	{
		piSessions.put(session, piId);
		piToSessions.put(piId, session);
	}
	
	public String removeSession(Session session)
	{
		piToSessions.remove(piSessions.get(session));
		return piSessions.remove(session);
	}
	
	public int getSessionCount()
	{
		return piSessions.size();
	}
	
	public String getPiId(Session session)
	{
		return piSessions.get(session);
	}
	
	public Session getSessionfromPiId(String piId)
	{
		return piToSessions.get(piId);
	}
	
	public boolean saveNewDevice(Request request)
	{
		if(isValidPi(request))
		{
			// insert or update
			if(isDeviceAlreadyMapped(request.getPiId(), request.getMsisdn()))
			{
				return updatePiMsisdnMapping(request.getPiId(), request.getData().get("fcm_id"));
			}
			else
			{
				return mapPiMsisdn(request);
			}
		}
		else
		{
			System.out.println("can't save device ["+request.getMsisdn()+"] with pi ["+request.getPiId()+"] as this pi is not registered");
			return false;
		}
	}
	
	public boolean isDeviceAlreadyMapped(String piId, String msisdn)
	{
		String query = "select MSISDN from PI_MSISDN_MAPPING where PI_ID='"+piId+"'";
		try{
			String msisdnDB = null;
			Statement ST = connection.createStatement();
			ResultSet RS = ST.executeQuery(query);
			while(RS.next())
			{
				msisdnDB = RS.getString(1);
			}
			System.out.println("isDeviceAlreadyMapped  query ["+query+"] input msisdn["+msisdn+"] db msisdn ["+msisdnDB+"]");
			ST.close();
			RS.close();
			if(msisdnDB == null)
				return false;
			else
			{
				if(msisdn.equals(msisdnDB))
				{
					return true;
				}
				else
				{
					System.out.println("some big issue ............................................................................");
					return false;
				}
			}
			
		}catch (Exception e) {
			System.out.println("Exception in isDeviceAlreadyMapped query["+query+"] Exception ["+e+"]");
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean mapPiMsisdn(Request request)
	{
		String query = "INSERT INTO PI_MSISDN_MAPPING(PI_ID,MSISDN,FCM_ID) values('"+request.getPiId()+"','"+request.getMsisdn()+"','"+request.getData().get("fcm_id")+"')";
		try{
			Statement ST = connection.createStatement();
			int result = ST.executeUpdate(query);
			System.out.println("mapPiMsisdn  query ["+query+"] status["+result+"]");
			ST.close();
			return true;
			
		}catch (Exception e) {
			System.out.println("Exception in mapPiMsisdn query["+query+"] Exception ["+e+"]");
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updatePiMsisdnMapping(String piId, String fcmId)
	{
		String query = "update PI_MSISDN_MAPPING set FCM_ID='"+fcmId+"' where PI_ID='"+piId+"'";
		try{
			Statement ST = connection.createStatement();
			int result = ST.executeUpdate(query);
			System.out.println("updatePiMsisdnMapping  query ["+query+"] status["+result+"]");
			ST.close();
			return true;
			
		}catch (Exception e) {
			System.out.println("Exception in updatePiMsisdnMapping query["+query+"] Exception ["+e+"]");
			e.printStackTrace();
			return false;
		}
	}
	
	public String getPiIdFromMsisdn(String msisdn)
	{
		String query = "select PI_ID from PI_MSISDN_MAPPING where MSISDN='"+msisdn+"'";
		try{
			String piId = null;
			Statement ST = connection.createStatement();
			ResultSet RS = ST.executeQuery(query);
			while(RS.next())
			{
				piId = RS.getString(1);
			}
			System.out.println("getPiIdFromMsisdn  query ["+query+"] msisdn["+msisdn+"] piId ["+piId+"]");
			ST.close();
			RS.close();
			return piId;
		}catch (Exception e) {
			System.out.println("Exception in getPiIdFromMsisdn query["+query+"] Exception ["+e+"]");
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean updateStatusOfPi(String piId, String status)
	{
		String query = "update PI_REGISTER set IS_CONNECTED='"+status+"' where PI_ID='"+piId+"'";
		try{
			Statement ST = connection.createStatement();
			int result = ST.executeUpdate(query);
			System.out.println("updateStatusOfPi  query ["+query+"] status["+result+"]");
			ST.close();
			return true;
			
		}catch (Exception e) {
			System.out.println("Exception in updateStatusOfPi query["+query+"] Exception ["+e+"]");
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isValidPi(Request request)
	{
		String query = "select count(1) from PI_REGISTER where PI_ID='"+request.getPiId()+"'";
		try{
			int count = 0;
			Statement ST = connection.createStatement();
			ResultSet RS = ST.executeQuery(query);
			while(RS.next())
			{
				count = RS.getInt(1);
			}
			System.out.println("isValidPi  query ["+query+"] status["+count+"]");
			ST.close();
			RS.close();
			if(count>0)
			return true;
			else
				return false;
			
		}catch (Exception e) {
			System.out.println("Exception in isValidPi query["+query+"] Exception ["+e+"]");
			e.printStackTrace();
			return false;
		}
	}
	
	public String getCurrentStatusOfPi(Request request)
	{
		String query = "select IS_CONNECTED from PI_REGISTER where PI_ID='"+request.getPiId()+"'";
		return null;
	}
	
	public String getFcmId(Request request)
	{
		String query = "select FCM_ID from PI_MSISDN_MAPPING where PI_ID='"+request.getPiId()+"'";
		try{
			String fcmId = "";
			Statement ST = connection.createStatement();
			ResultSet RS = ST.executeQuery(query);
			while(RS.next())
			{
				fcmId = RS.getString(1);
			}
			System.out.println("getFcmId  query ["+query+"] status["+fcmId+"]");
			ST.close();
			RS.close();
			return fcmId;
			
		}catch (Exception e) {
			System.out.println("Exception in getFcmId query["+query+"] Exception ["+e+"]");
			e.printStackTrace();
			return null;
		}
		
	}
}
