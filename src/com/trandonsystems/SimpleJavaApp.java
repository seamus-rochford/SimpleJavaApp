package com.trandonsystems;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trandonsystems.services.JavaMailServices;
import com.trandonsystems.database.UserDAL;
import com.trandonsystems.model.User;

import java.io.IOException;

import javax.json.*;


public class SimpleJavaApp {

	final static Logger log = Logger.getLogger(SimpleJavaApp.class);
	final static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	private static boolean quit(String inst) {
		if(inst.equalsIgnoreCase("bye") || inst.equalsIgnoreCase("exit") || inst.equalsIgnoreCase("quit") || inst.equalsIgnoreCase("exit")
				|| inst.equalsIgnoreCase("x") || inst.equalsIgnoreCase("q")) {
			return true;
		} else {
			return false;
		}
	}
	
	private static void checkEnvVar(String inst) {
    	// To set an environment variable from command prompt> setx <<name>> "<<value>>"
    	// Important: You must open a new command prompt window to check this value
    	// to check from command prompt> echo %name%
    	while (true) {
    		System.out.print("Enter environment variable to check(e(x)it or (q)uit) to quit: ");
    		inst = System.console().readLine();
        	if (quit(inst)) {
        		break;
        	}
    		System.out.println("Value: " + System.getenv(inst));
    	}
	}
	
	private static void testEmail() {
		log.info("Test Emails started ... ");
		
    	System.out.print("Enter email: ");
    	String email = System.console().readLine();    		
		
    	System.out.print("Enter email subject: ");
    	String emailSubject = System.console().readLine();    		
		
    	System.out.print("Enter email body: ");
    	String emailBody = System.console().readLine();
    	
        try {        	 
            log.info("Initialize emailer ");
			JavaMailServices.initializeEmailer();
 
			log.info("Send email");
			JavaMailServices.sendMail(email, emailSubject, false, emailBody);
 
            log.info("Email sent - check email to see if you received email");	        	
       } catch (Exception ex) {
            log.error("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }		

        log.info(" ... Test Emails Terminated");	  		
	}
	
	private static void testLogging(String inst) {
    	while (true) {
    		System.out.println("Set logging level: ");
    		System.out.println("	(T)race: ");
    		System.out.println("	(D)ebug: ");
    		System.out.println("	(I)nfo: ");
    		System.out.println("	(W)arn: ");
    		System.out.println("	(E)rror: ");
    		System.out.println("	(F)atal: ");
			System.out.print("Set logging level (e(x)it or (q)uit) to quit: ");
			
    		inst = System.console().readLine();
			if (quit(inst)) {
        		break;
        	}
			
        	switch (inst.toUpperCase()) {
        	case "T":
        		log.setLevel(Level.TRACE);
        		break;
	    	case "D":
	    		log.setLevel(Level.DEBUG);
	    		break;
	        case "I":
	       		log.setLevel(Level.INFO);
	       		break;
			case "W":
				log.setLevel(Level.WARN);
				break;
			case "E": 
				log.setLevel(Level.ERROR);
				break;
			
			default: 
				log.setLevel(Level.FATAL);
				break;
			}
			
			log.trace("This is trace message ");
			log.debug("This is debug message ");
			log.info("This is info message ");
			log.warn("This is warn message ");
			log.error("This is error message");
			log.fatal("This is fatal message");
    	}		
	}
	
	private static void testDBAccess() {
		User user = UserDAL.getBySQL(1);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println("User: " + gson.toJson(user));			
	}
	
	private static void loginAPI() {
    	PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
    	CloseableHttpClient httpClient = HttpClients.custom()
    			.setConnectionManager(connManager)
    			.build();
    	
    	String url = "http://localhost:8080/BriteBin/api/user/login";
    	HttpPost httpPost = new HttpPost(url);
    	httpPost.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
    	
    	JsonObject reqBody = Json.createObjectBuilder()
    			.add("email", "serochfo@gmail.com")
    			.add("password", "seamus")
    			.build();
    	
    	httpPost.setEntity(new StringEntity(reqBody.toString(), ContentType.APPLICATION_JSON));
    	
    	try {
    		HttpResponse response = httpClient.execute(httpPost);
    		
    		System.out.println("\nHttp Response: " + response.toString());
    		
    		int respStatus = response.getStatusLine().getStatusCode();
    		System.out.println("\nResponse Code: " + respStatus);
    		
    		String resultStr = EntityUtils.toString(response.getEntity());
    		System.out.println("\nResponse Body: " + resultStr);
    		
    	} catch (ClientProtocolException ex) {
    		log.error("HTTP Client Protocol Error: " + ex.getMessage());
    	} catch (IOException exIO) {
    		log.error("HTTP IO Error: " + exIO.getMessage());
    	} 		
	}
	
	private static void sendSMS() {
		
    	PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
    	CloseableHttpClient httpClient = HttpClients.custom()
    			.setConnectionManager(connManager)
    			.build();
    	
    	String url = "http://multi.mobile-gw.com:9000/v1/omni/message";
    	HttpPost httpPost = new HttpPost(url);
    	httpPost.addHeader(HttpHeaders.AUTHORIZATION, "Basic ZGVtbzc3NzduOj83Jm1OYnE2");
    	httpPost.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
    	httpPost.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
    	
    	JsonArray channels = Json.createArrayBuilder()
    			.add("SMS")
    			.build();
    	
    	JsonObject phoneNo = Json.createObjectBuilder()
    			.add("phoneNumber", "35387264637")
    			.build();
    	JsonArray destinations = Json.createArrayBuilder()
    			.add(phoneNo)
    			.build();
    	
    	JsonObject smsSender = Json.createObjectBuilder()
    			.add("sender", "BriteBin")
    			.add("text", "test message from britebin")
    			.build();
    	
    	JsonObject reqBody = Json.createObjectBuilder()
    			.add("channels", channels)
    			.add("destinations", destinations)
    			.add("transactionId", "1782")
    			.add("dir", true)
    			.add("dlrUrl", "http://161.35.32.177:8080/BriteBin/api/sms")
    			.add("tag", "bin full alert")
    			.add("sms", smsSender)
    			.build();
    	System.out.println("\nRequest Body: " + reqBody.toString());
    	
    	httpPost.setEntity(new StringEntity(reqBody.toString(), ContentType.APPLICATION_JSON));
    	
    	try {
    		HttpResponse response = httpClient.execute(httpPost);
    		
    		System.out.println("\nHttp Response: " + response.toString());
    		
    		int respStatus = response.getStatusLine().getStatusCode();
    		System.out.println("\nResponse Code: " + respStatus);
    		
    		String resultStr = EntityUtils.toString(response.getEntity());
    		System.out.println("\nResponse Body: " + resultStr);
    		
    	} catch (ClientProtocolException ex) {
    		log.error("HTTP Client Protocol Error: " + ex.getMessage());
    	} catch (IOException exIO) {
    		log.error("HTTP IO Error: " + exIO.getMessage());
    	} 		
	}
	
	private static void sendMultipleEmails() {
		log.info("Test Emails started ... ");
		
    	System.out.print("Enter email: ");
    	String email = System.console().readLine();    		
		
    	System.out.print("Enter email subject: ");
    	String emailSubject = System.console().readLine();    		
		
    	System.out.print("Enter email body: ");
    	String emailBody = System.console().readLine();
    	
    	System.out.print("Enter no. emails to send: ");
    	int noEmails = Integer.parseInt(System.console().readLine());
    	
    	try {        	 
            log.info("Initialize emailer ");
			JavaMailServices.initializeEmailer();
 
			for (int i = 0; i < noEmails; i++) {
				try {
					log.info("Send email: " + i);
					JavaMailServices.sendMail(email, emailSubject + ' ' + i, false, emailBody);
		            log.info("Email sent - check email to see if you received email");	        	
				} catch (Exception ex) {
		            log.error("Server exception: " + ex.getMessage());					
				}
			}
 
       } catch (Exception ex) {
            log.error("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }		

        log.info(" ... Test Multiple Emails Terminated");		
	}
	
    public static void main(String[] args) {

        System.out.println("Test Instructions ");
        System.out.println("		1. Test Simple Program");
        System.out.println("		2. Test Environment variable");
        System.out.println("		3. Test logging");
        System.out.println("		4. Test DB");
        System.out.println("		5. Test Email");
        System.out.println("		6. Test Login API call");
        System.out.println("		7. Test Send SMS");
        System.out.println("		8. Show Working directory");
        System.out.println("        9. Send multiple emails");
        System.out.println("		0. Exit");
        System.out.print("Input option: ");

        String inst = "";
    	inst = System.console().readLine();

        switch (inst) {
        case "1":
            while(true) {
            	System.out.print("Enter any text (e(x)it or (q)uit) to quit: ");
            
            	inst = System.console().readLine();
            	System.out.println(inst);
            	if (quit(inst)) {
            		break;
            	}
            }
        	break;
        case "2":
        	checkEnvVar(inst);
        	
        	break;
        case "3":
        	testLogging(inst);
        	
    		break;
        case "4":
        	// Test DB Access
        	testDBAccess();
        	
        	break;
        case "5":
        	// Test Email
        	testEmail();
        	
            break;
            
        case "6":
        	loginAPI();
        	
        	break;
        case "7":
        	sendSMS();
        	
        	break;
        case "8":
        	System.out.println("Working Directory: " + System.getProperty("user.dir"));
        case "9":
        	sendMultipleEmails();
        	
        	break;
        default:
        	break;
        }

        System.out.println("Program Terminated.");
    }

}
