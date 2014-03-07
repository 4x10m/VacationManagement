package core.exceptions;

import java.util.Date;

@SuppressWarnings("serial")
public class RequestBegginDateBeforeEndDateException extends Exception {
	public RequestBegginDateBeforeEndDateException(Date concerneddate) {
		super(String.format("Impossible to create a request where end date is before beggin date : %s", concerneddate));
	}
}
