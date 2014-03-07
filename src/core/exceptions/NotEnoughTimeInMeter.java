package core.exceptions;

import core.enums.RequestType;

@SuppressWarnings("serial")
public class NotEnoughTimeInMeter extends Exception {
	public NotEnoughTimeInMeter(RequestType requestedtype, int requestedtime, int actualmetertime) {
		super(String.format("Not enough time in meter; request type : %s; requested time : %s; actual meter time : %s", requestedtype.toString(), String.valueOf(requestedtime), String.valueOf(actualmetertime)));
	}
}
