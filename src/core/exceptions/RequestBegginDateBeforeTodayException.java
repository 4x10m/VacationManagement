package core.exceptions;

import java.util.Date;

@SuppressWarnings("serial")
public class RequestBegginDateBeforeTodayException extends Exception {
	public RequestBegginDateBeforeTodayException(Date concerneddate) {
		super(String.format("impossible to create a request where beggin date is before or equal to today : %s", concerneddate));
	}
}
