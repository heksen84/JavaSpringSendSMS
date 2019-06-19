/*
 * --------------------------------------
 *  Класс для отправки СМС сообщений
 *  через интерфейс REST Kcell
 * --------------------------------------
 */
package com.example.sms2017;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

/*
 * ===================================  
 *	REST МЕТОДЫ
 *  / messages
 *  / batches
 *  / senders
 *  / users
 * ===================================
 */
public class SMS 
{		
	private static String base_url		 	= "https://api.kcell.kz/app/smsgw/rest/v2/"; // базовый url
	private static String url_messages 		= base_url + "messages/"; 					 // адрес для отправки сообщений
	private static String url_batches 		= base_url + "batches/";					 // адрес для отправки батчей
	private static String username 			= "EEC";									 // логин
	private static String password			= "!@#eec123";								 // пароль
	private static HttpURLConnection con	= null;
	private static JSONObject json			= null;

	/*
	 * --------------------------------------------------	 
	 * Получить случайное число	 
	 * --------------------------------------------------
	 */
	public static String getId() {
		Random generator = new Random();
		int i = generator.nextInt(999999999);
		String s = String.valueOf(i);
		return s;
	}
	
	public static String convertToUTF8(String s) {
		String out=null;
		try {
			out=new String(s.getBytes("UTF-8"), "ISO-8859-1");
		}
		catch (java.io.UnsupportedEncodingException e) {
			return null;
		}
		return out;
	}
		 
	/*
	 * --------------------------------------------------	 
	 * POST /messages
	 * Создание нового сообщения на отправку	 
	 * --------------------------------------------------
	 */
	
	public static String SendMessage(String number, String text, String schedule)  {																					 																			
		//Date date  = null;
		//String 	sсhedule_string = null;
		
		/*if ( schedule.length() > 0 ) 
		{ 	
			// Если строка не пустая - применить шедулер		
			DateFormat format = new SimpleDateFormat( "E MMM dd yyyy HH:mm:ss", Locale.ENGLISH  );		
			try 
			{					
				date = format.parse(schedule);				
			} catch (ParseException e2) 
			{		
				e2.printStackTrace();
			}				
			
			// Дату в календарь
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);				
			
			int year	= calendar.get(Calendar.YEAR);
			int month	= calendar.get(Calendar.MONTH)+1;
			int day	 	= calendar.get(Calendar.DAY_OF_MONTH);			
			int hours 	= calendar.get(Calendar.HOUR_OF_DAY);
			int minutes	= calendar.get(Calendar.MINUTE);
			int seconds	= calendar.get(Calendar.SECOND);
						
			DecimalFormat four 	= new DecimalFormat("#0000");
			DecimalFormat two 	= new DecimalFormat("#00");																		
									
			sсhedule_string = four.format(year)+"-"+two.format(month)+"-"+two.format(day)+" "+two.format(hours)+":"+two.format(minutes)+":"+two.format(seconds);
			System.out.println("Дата и время: "+ sсhedule_string);
		}*/
						
		json = new JSONObject();
		
		try 
		{						
			json.put("client_message_id", 	getId());
			json.put("sender", 				"INFO_EEC");			
			json.put("recipient", 			number);
			json.put("time_bounds", 		"ad99");
			json.put("message_text",		text);
			
			//json.put("sсhedule", 			"2017-09-05 14:47:00");			
			// *** ОТПРАВКА ПО ШЕДУЛЕРУ ***
			// *** если указаны дата и время, то запланировать сообщение ***
			/*if ( schedule.length() > 0 ) 
			{	
				json.put("sсhedule", sсhedule_string);
				json.put("priority", "0");
			}*/
			
		} 
		catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		String jsonData = json.toString();	
		
		HttpPost post = new HttpPost(url_messages);
		String auth=new StringBuffer(username).append(":").append(password).toString();		
		byte[] encodedAuth=Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		
		post.setHeader("AUTHORIZATION",  authHeader);
	    post.setHeader("Content-Type", 	"application/json;charset=utf-8");
	    post.setHeader("Accept", 		"application/json, text/plain, */*");	    
	    
	    try {
	    	HttpResponse response=null;
	        String line = "";
	        StringBuffer result = new StringBuffer();
	        post.setEntity(new StringEntity(jsonData));
	        HttpClient client = HttpClientBuilder.create().build();
	        response = client.execute(post);
	        System.out.println("Post parameters: " + jsonData );
	        System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
	        
	        // Невозможно отправить сообщение
	        if ( response.getStatusLine().getStatusCode() != 201 ) 
	        	return "Невозможно отправить сообщение. Код: " + response.getStatusLine().getStatusCode();
	        
	        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	        
	        while ((line = reader.readLine()) != null){ 
	        	result.append(line); 
	        }

	        // Вывести данные в консоль
	        System.out.println(result.toString());	        
	        
	        // Записать данные в файл
	        try {
			    PrintWriter writer = new PrintWriter("messages.txt", "UTF-8");
			    writer.println(result.toString());		    
			    writer.close();
			} catch (IOException e) 
	        {
				System.out.println("PrintWriter error!");
			}
	    }
	    catch (UnsupportedEncodingException e) {
            System.out.println("error while encoding api url : "+e);
        }
        catch (IOException e) {
            System.out.println("ioException occured while sending http request : "+e);
        }
        catch(Exception e) {
            System.out.println("exception occured while sending http request : "+e);
        }
        finally {
            post.releaseConnection();
        }	   	   

		return "Сообщение отправлено!";
	}
	
	/*
	 * ----------------------------------------------------	 
	 * GET /messages/:message_id
	 * Получение информации о сообщении по идентификатору
	 * message_id (генерируется на стороне SMSGW)
	 * ----------------------------------------------------
	 */
	
	public static String GetMessageInfo(String message_id) {		
		URL myurl = null;
		StringBuilder content;
		
		try {
			myurl = new URL(url_messages+message_id);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	    try {
			con = (HttpURLConnection) myurl.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	     
	     try {
			con.setRequestMethod("GET");			
			String auth=new StringBuffer(username).append(":").append(password).toString();
			byte[] encodedAuth=Base64.getEncoder().encode(auth.getBytes(Charset.forName("UTF-8")));
			String authHeader = "Basic " + new String(encodedAuth);
			con.setRequestProperty("authorization", authHeader);
			// вывести код ответа в консоль
			System.out.println(con.getResponseCode());
			if (con.getResponseCode() != 200) return ""; // нет данных 
			
		} catch (ProtocolException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	     	    	     	     	     	     
	     try {	    	 
	    	 BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));	    	 
	    	 String line;	    	 
	    	 content = new StringBuilder();
	            
	         while ((line = in.readLine()) != null) {
	                content.append(line);
	         }
	    	 
	         System.out.println(content.toString());
	         return content.toString();
	    	 
		 } catch (IOException e) {	
			e.printStackTrace();
		 }	
	     
	     return "";
	}		
	
	/*
	 * --------------------------------------------------	 
	 * POST /batches
	 * Отправка набора сообщений	
	 * --------------------------------------------------
	 */		
	public static String SendButchMessages(String data) {		
		// парсинг входящих данных ( json строка )
		Object document  		= Configuration.defaultConfiguration().jsonProvider().parse(data);
		String text		 		= JsonPath.read(document, "$.text");
		String numbers   		= JsonPath.read(document, "$.numbers.*").toString();						
		String []phone_number 	= numbers.replace("[", "").replace("]", "").replace(" ", "").replace("\"", "").split(",");						
		
		// Вывожу текст
		System.out.println(text);

		// Вывожу номера
		for (int i=0; i < phone_number.length; i++) System.out.println(phone_number[i]);
		JSONObject obj = new JSONObject();		
		List<Map<String,String>> map_list = new ArrayList<Map<String,String>>();
				
		for (String cn: phone_number) {
			Map<String, String> cm = new HashMap<String, String>();			
			cm.put("client_message_id", getId());
			cm.put("sender", 			"INFO_EEC");
			cm.put("recipient", 		cn);
			cm.put("time_bounds", 		"ad99");
			cm.put("message_text", text );
			map_list.add(cm); // добавляю в мапку
		}																		
		try 
		{						
			obj.put("messages", map_list);
			System.out.println(obj);
			
			// ------------[ ОТПРАВИТЬ ПАЧКУ СООБЩЕНИЙ ]------------
			System.out.println("Отправляем post запрос!");			
			HttpPost post = new HttpPost(url_batches);
			String auth = new StringBuffer(username).append(":").append(password).toString();
			byte[] encodedAuth=Base64.getEncoder().encode(auth.getBytes(Charset.forName("UTF-8")));
			String authHeader = "Basic " + new String(encodedAuth);
			
			post.setHeader("Authorization",  authHeader);
			post.setHeader("Content-Type", 	"application/json; charset=UTF-8");
		    post.setHeader("Accept", 		"application/json, text/plain, */*");			    		  		    
		    		    
		    try {
		    	HttpResponse response = null;
		        String line = "";
		        StringBuffer result = new StringBuffer();		        		        
		        
		        post.setEntity(new StringEntity(obj.toString()));
		        		       		        		        		        
		        HttpClient client = HttpClientBuilder.create().build();
		        response = client.execute(post);
		        
		        System.out.println("Post parameters: " + obj.toString() );
		        System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
		        
		        // Невозможно отправить сообщение
		        if ( response.getStatusLine().getStatusCode() != 201 ) 
		        	return "Невозможно отправить сообщение. Код: " + response.getStatusLine().getStatusCode();
		        
		        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		        while ((line = reader.readLine()) != null){ 
		        	result.append(line); 
		        }

		        // Вывести данные в консоль
		        System.out.println(result.toString());	        
		        
		        // Записать данные в файл
		        try {
				    PrintWriter writer = new PrintWriter("messages_batch.txt", "UTF-8");
				    writer.println(result.toString());		    
				    writer.close();
				} catch (IOException e) {
					System.out.println("PrintWriter error!");
				}		        
		    }
		    catch (UnsupportedEncodingException e){
	            System.out.println("error while encoding api url : "+e);
	        }
	        catch (IOException e){
	            System.out.println("ioException occured while sending http request : "+e);
	        }
	        catch(Exception e){
	            System.out.println("exception occured while sending http request : "+e);
	        }
	        finally{
	            post.releaseConnection();
	        }		    		    		    		   		    			    		    
		} catch (JSONException e) {
			e.printStackTrace();
		}
		    				
		return "Сообщение отправлено!";
	}
	
	/*
	 * --------------------------------------------------	 
	 * GET /batches/:batch_id
	 * Получение информации о пачке сообщений	 
	 * --------------------------------------------------
	 */
	
	public static String GetBatchInfo(String batch_id) {				
		
		// вывожу параметр в консоль
		System.out.println(batch_id);
		
		URL myurl = null;
		StringBuilder content;
		
		try {
			myurl = new URL(url_batches + batch_id);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	    try {
			con = (HttpURLConnection) myurl.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	     
	     try {
			con.setRequestMethod("GET");			
			String auth=new StringBuffer(username).append(":").append(password).toString();
			byte[] encodedAuth=Base64.getEncoder().encode(auth.getBytes(Charset.forName("utf-8")));
			String authHeader = "Basic " + new String(encodedAuth);
			con.setRequestProperty("authorization", authHeader);
			con.setRequestProperty("Content-Type",  "application/json;charset=utf-8");
			con.setRequestProperty("Accept", 		"application/json,text/plain,*/*");	
			// вывести код ответа в консоль
			System.out.println(con.getResponseCode());
			if (con.getResponseCode() != 200) return ""; // нет данных 
			
		} catch (ProtocolException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	     	    	     	     	     	     
	     try {	    	 
	    	 BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));	    	 
	    	 String line;	    	 
	    	 content = new StringBuilder();
	            
	         while ((line = in.readLine()) != null) {
	                content.append(line);
	         }
	    	 
	         System.out.println(content.toString());
	         return content.toString();
	    	 
		 } catch (IOException e) {	
			e.printStackTrace();
		 }	
	     
	     return ""; // default
	}	
}
