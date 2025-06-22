package br.edu.atitus.api_sample.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class verificaemail  {
	final static String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	final static String regexdois = "^[a-zA-Z0-9._%+-]+@(gmail\\.com|hotmail\\.com)$";
	
	public static boolean isValidEmail(String email) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches() || !email.matches(regexdois)) {
			return true;
		}
		return false;
	
	
	
	
	
	}
	
	
 
}
