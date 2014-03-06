package console;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InputScanner {
	private static Scanner reader = new Scanner(System.in);
	
	public static int getInt() {
		int result = 0;
		
		while(true) {
			try {
				result = reader.nextInt();
				
				break;
			}
			catch(InputMismatchException e) { }
		}
		
		return result;
	}
	
	public static int getIntBetween(int min, int max) {
		int result = 0;
		
		while(true) {
			try {
				result = reader.nextInt();
				
				if(result >= min || result <= max) {
					break;
				}
			}
			catch(InputMismatchException e) { }
		}
		
		return result;
	}
	
	public static Date getDate() {
		Date result = null;
		
		while(true) {
			try {
				String[] temp = reader.nextLine().split("/");
				
				Calendar tempcalendar = new GregorianCalendar();
				tempcalendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(temp[0]));
				tempcalendar.set(Calendar.MONTH, Integer.valueOf(temp[1]));
				tempcalendar.set(Calendar.YEAR, Integer.valueOf(temp[2]));
				
				result = tempcalendar.getTime();
				
				break;
			}
			catch(Exception e) { }
		}
		
		return result;
	}
	
	public static void getNext() {
		reader.nextLine();
	}
}
