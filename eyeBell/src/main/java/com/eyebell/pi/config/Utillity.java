package com.eyebell.pi.config;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("piUtillity")
public class Utillity {


	public AtomicInteger appender = new AtomicInteger(0);



	@Autowired
	private DatagramSocket clientSocket;

	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


	public int cameraON()
	{
		int rand = new Random().nextInt();
		if (openBrowser("https://103.206.248.236:8443/bell.html?home="+rand))
			return rand;
		else
			return -1;
	}
	
	public boolean openBrowser(String args) 
	{
		String url = args;// "http://www.google.com";
		String os = System.getProperty("os.name").toLowerCase();
		System.out.println("OS found is : " + os);
		Runtime rt = Runtime.getRuntime();
		try {
			if (os.indexOf("win") >= 0) {
				// this doesn't support showing urls in the form of "page.html#nameLink"
				rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
			} else if (os.indexOf("mac") >= 0) {
				rt.exec("open " + url);
			} else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
				// Do a best guess on unix until we get a platform independent way
				// Build a list of browsers to try, in this order.
				String[] browsers = { "chromium", "mozilla","chrome" };
				// Build a command string which looks like "browser1 "url" || browser2 "url"
				// ||..."
				StringBuffer cmd = new StringBuffer();
				for (int i = 0; i < browsers.length; i++) {
					cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \"" + url + "\" ");
				}
				System.out.println("command formed : " + cmd);
				rt.exec(new String[] { "sh", "-c", cmd.toString() });
			} else {
				System.out.println("mo OS found");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public int cameraOFF()
	{
		System.out.println("camera is now off");
		String downCommand = "/home/pi/eyeBell/down.sh";
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec(downCommand);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void UDP_SEND(String IP, int PORT, String BUFF, boolean islength) {
		try {
			String strFinal = null;
			int len = BUFF.length();
			byte[] bt = null;
			if (islength) {
				if ((len - 5) >= 10000)
					strFinal = (len - 5) + "";
				else if ((len - 5) >= 1000)
					strFinal = "0" + (len - 5);
				else if ((len - 5) >= 100)
					strFinal = "00" + (len - 5);
				else if ((len - 5) >= 10)
					strFinal = "000" + (len - 5);
				else
					strFinal = "0000" + (len - 5);
				bt = BUFF.getBytes();
				byte[] bt1 = strFinal.getBytes();
				for (int i = 0; i < bt1.length; i++)
					bt[i] = bt1[i];
			} else {
				bt = BUFF.getBytes();
			}
			InetAddress IPAddress = InetAddress.getByName(IP);
			DatagramPacket sendPacket = new DatagramPacket(bt, bt.length);
			String byte_to_String = new String(bt);
			sendPacket.setData(bt);
			sendPacket.setAddress(IPAddress);
			sendPacket.setPort(PORT);
			this.clientSocket.send(sendPacket);
			System.out.println("UDP_SEND:" + byte_to_String + ", IP:" + IP + ", PORT:" + PORT);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public String sendPost(String url, String data, String contextType) {
		return sendPost(url, data, contextType, 1000, 1000);
	}

	public static String sendPost(String url, String data, String contextType, int readTimeOut, int connectTimeOut) {
		Exception E = null;
		DataOutputStream wr = null;
		BufferedReader in = null;
		int status = -10;
		String result = null;
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("POST");
			con.setRequestProperty("content-type", contextType);
			con.setRequestProperty("authorization", "key=AIzaSyBsdpJOCYibn5dRR5-mpSLIUSOLtH1owN4");
			con.setDoOutput(true);
			con.setReadTimeout(readTimeOut);
			con.setConnectTimeout(connectTimeOut);
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(data);
			wr.flush();
			wr.close();

			status = con.getResponseCode();
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			result = response.toString();
		} catch (SocketTimeoutException e) {
			// Read Time Out
			status = -20;
			E = e;
		} catch (ConnectException e) {
			// Connection Time Out
			status = -30;
			E = e;
		} catch (SocketException e) {
			// Connection Reset
			status = -40;
			E = e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			E = e;
		} finally {
			try {
				if (wr != null) {
					wr.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		if (E != null) {
			System.out.println("sendPost - Status:Exception" + E.getMessage() + ", URL:" + url + ", XML:" + data);
		} else
			System.out.println("sendPost - Status:" + status + ", URL:" + url + ", DATA:" + data + ", Response: " + result);
		return result;
	}

	public String callUrl(String url) {
		return callUrl(url, 1000, 1000);
	}

	public static String callUrl(String URL_Str, int readTimeOut, int connectTimeOut) {
		BufferedReader BR = null;
		Exception E = null;
		String result = null;
		int status = -200;
		try {
			URL url = new URL(URL_Str);
			HttpURLConnection theURLconn = (HttpURLConnection) url.openConnection();
			theURLconn.setConnectTimeout(readTimeOut);
			theURLconn.setReadTimeout(connectTimeOut);
			status = theURLconn.getResponseCode();
			BR = new BufferedReader(new InputStreamReader(theURLconn.getInputStream()));
			String show = "";
			String toprt = "";
			StringBuffer response = new StringBuffer();
			while ((show = BR.readLine()) != null) {
				response.append(show.trim());
			}
			result = response.toString();
		} catch (SocketTimeoutException e) {
			// Read Time Out
			status = -20;
			E = e;
		} catch (ConnectException e) {
			// Connection Time Out
			status = -30;
			E = e;
		} catch (SocketException e) {
			// Connection Reset
			status = -40;
			E = e;
		} catch (Exception e) {
			//logger.error(e.getMessage(), e);
			E = e;
		} finally {
			try {
				BR.close();
			} catch (Exception e) {
				//logger.error(e.getMessage(), e);
			}
		}
		if (E != null) {
			//logger.info("sendPost - Status:Exception" + E.getMessage() + ", URL:" + URL_Str, E);
		} else 
			System.out.println("sendPost - Status:" + status + ", URL:" + URL_Str + ", Response: " + result);

		return result;
		
	}

	public String sendReceive(String tid, String sendData, String ip, int port) {
		DatagramSocket socket = null;
		String response = null;
		try {
			socket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(ip);
			sendData = sendData.replaceAll("<selfPort>", "" + socket.getLocalPort());
			DatagramPacket sendPacket = new DatagramPacket(sendData.getBytes(), sendData.length(), IPAddress, port);
			socket.send(sendPacket);
			//logger.info("UDP_SEND:" + sendData + ", IP:" + ip + ", PORT:" + port);
			byte[] reciveData = new byte[1024];
			DatagramPacket recivePacket = new DatagramPacket(reciveData, reciveData.length);
			socket.setSoTimeout(10000);
			socket.receive(recivePacket);
			socket.close();
			response = new String(recivePacket.getData());

		} catch (SocketTimeoutException e) {
			response = "00000###" + tid + "#<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><RES><TID>" + tid
					+ "</TID><RESULT>FAIL</RESULT><REASON>RECEIVE TIME OUT (I was waiting for 10 seconds)</REASON></RES>#";
		} catch (Exception e) {
			response = "00000###" + tid + "#<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><RES><TID>" + tid
					+ "</TID><RESULT>FAIL</RESULT><REASON>" + e.getMessage() + "</REASON></RES>#";
			//logger.error(e.getMessage(), e);
		} finally {
			if (socket != null)
				socket.close();
		}
		//logger.info("Response Recieved from Ucip:" + response);
		return response;
	}

	public synchronized String newTid(String tid) {
		if (appender.get() == 999) {
			appender.set(0);
		}
		//String new_tid = appConfig.getServerMode() + System.currentTimeMillis() + appender.getAndIncrement();
		//logger.debug("new tid [" + new_tid + "] old tid [" + tid + "]");
		return null;
	}

	public String getDateTime(int minutes) {
		LocalDateTime time = LocalDateTime.now().plusMinutes(minutes);
		return time.format(dateTimeFormatter);
	}

	public String getDateTimeSeconds(int seconds) {
		LocalDateTime time = LocalDateTime.now().plusSeconds(seconds);
		return time.format(dateTimeFormatter);
	}


	public String getTimeStampForIn(int hours) {
		LocalDateTime now = LocalDateTime.now();
		now = now.plusHours(hours);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
		String formatDateTime = (now.format(formatter).replaceAll(" ", "T")) + "+0530";
		return formatDateTime;
	}
}
