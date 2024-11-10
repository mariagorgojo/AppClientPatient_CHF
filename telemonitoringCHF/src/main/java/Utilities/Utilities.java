/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utilities {
    
    private static BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
    
    // Read console inputs 
    		public static int readInteger() {
	        int num = 0;
	        boolean ok = false;
	        do {
	            try {
	            	num = Integer.parseInt(r.readLine());
	                if (num < 0) {
	                    ok = false;
	                    System.out.print("You didn't type a valid number.");
	                } else {
	                    ok = true;
	                }
	            } catch (IOException e) {
	                e.getMessage();
	            } catch (NumberFormatException nfe) {
	            	System.out.print("You didn't type a valid number!");
	            }
	        } while (!ok);

	        return num;
	    }

		
		public static String readString() {
	        String text = null;
	        boolean ok = false;
	        do {
	            try {
	            	text = r.readLine();
	                if (!text.isEmpty()) {
	                    ok = true;
	                } else {
	                    System.out.println("Empty string, please try again:");
	                }
	            } catch (IOException e) {

	            }
	        } while (!ok);

	        return text;
	    }
                
                
                
                // Validates the format of the DNI
                
                 public static boolean validateDNI(String id) {	
			 
			 boolean ok=true;
                         
			 if (id.length() != 9) {
				 System.out.println("Invalid DNI, try again");
				 ok=false;
			 
		            return ok;
		        }

		        for (int i = 0; i < 8; i++) {
		            if (!Character.isDigit(id.charAt(i))) {
		            	ok=false;
		            	System.out.println("Invalid DNI, try again");
		                return ok;
		            }
		        }
		            String num = id.substring(0, 8);

		        String validLeters = "TRWAGMYFPDXBNJZSQVHLCKE";
		        int indexLeter = Integer.parseInt(num) % 23;
		        char valid = validLeters.charAt(indexLeter);
		        
		        if (id.toUpperCase().charAt(8) != valid ) {
		        	System.out.println("Invalid DNI, try again");
		        	ok=false;
		            return ok;
		        }
		       
		        
		        return ok;
		    }
                 
                 public static boolean validateEmail(String email) {
                    
                    String emailpattern = "^[A-Za-z0-9+_.-]+@(.+)$";

                    // Check if the email matches the pattern
                    return email != null && email.matches(emailpattern);
                }

                 
                 
               
    
    
}
