package com.trandonsystems;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trandonsystems.database.UserDAL;
import com.trandonsystems.model.User;

public class SimpleJavaApp {

	final static Logger log = Logger.getLogger(SimpleJavaApp.class);
	
	private static boolean quit(String inst) {
		if(inst.equalsIgnoreCase("bye") || inst.equalsIgnoreCase("exit") || inst.equalsIgnoreCase("quit") || inst.equalsIgnoreCase("exit")
				|| inst.equalsIgnoreCase("x") || inst.equalsIgnoreCase("q")) {
			return true;
		} else {
			return false;
		}
	}
	
    public static void main(String[] args) {

        System.out.println("Test Instructions ");
        System.out.println("		1. Test Simple Program");
        System.out.println("		2. Test Environment variable");
        System.out.println("		3. Test logging");
        System.out.println("		4. Test DB");
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
        	break;
        case "3":
//        	DOMConfigurator.configure("properties/log4j.xml");
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
    		break;
        case "4":
        	// Test DB Access
    		User user = UserDAL.getBySQL(1);
    		
    		Gson gson = new GsonBuilder().setPrettyPrinting().create();
    		System.out.println("User: " + gson.toJson(user));		
        	break;
        default:
        	break;
        }

        System.out.println("Program Terminated.");
    }

}
