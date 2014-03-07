package core.exceptions;

import java.util.Date;

@SuppressWarnings("serial")
public class RequestDateIntervalDurationException extends Exception {
	public RequestDateIntervalDurationException(Date date1, Date date2) {
		super(String.format("Dates interval must be bigger than 1 day : %s\t%s", date1, date2));
	}
}
