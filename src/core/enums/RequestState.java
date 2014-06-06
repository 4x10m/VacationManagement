package core.enums;

public enum RequestState {
	WAITFORCDSVALIDATION, // was seen neither by the cds nor by the hr
	WAITFORHRVALIDATION, // was seen and validated by cds and attempting for hr validation
	VALIDATED, // fully validated
	REFUSED // refused by the cds or the hr
}
