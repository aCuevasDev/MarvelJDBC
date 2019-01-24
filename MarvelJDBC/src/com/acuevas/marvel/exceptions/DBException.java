package com.acuevas.marvel.exceptions;

@SuppressWarnings("serial")
public class DBException extends Exception {

	public enum DBErrors {
		DOESNT_EXIST("The object requested doesn't exist in the database.", 100),
		COULD_NOT_UPDATE("Could not update the database", 101),
		USER_ALREADY_EXISTS("User is already registered. ", 102),
		PLACE_WITH_NO_VILLAINS("This place has no villains ", 103);
		private String message;
		private int code;

		private DBErrors(String message, int code) {
			this.message = message;
			this.code = code;
		}

	}

	private int errorCode;
	private String nameOfObject = "";

	public DBException(DBErrors error) {
		super(error.message);
		this.errorCode = error.code;
	}

	public DBException(DBErrors error, String name) {
		super(error.message);
		this.errorCode = error.code;
		this.nameOfObject = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return "ERRORCODE: " + errorCode + " " + super.getMessage() + " " + nameOfObject;
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

}
