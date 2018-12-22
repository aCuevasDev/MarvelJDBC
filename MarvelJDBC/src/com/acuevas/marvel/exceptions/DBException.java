package com.acuevas.marvel.exceptions;

@SuppressWarnings("serial")
public class DBException extends Exception {

	public enum DBErrors {
		DOESNT_EXIST("The object requested doesn't exist in the database.", 1);
		private String message;
		private int code;

		private DBErrors(String message, int code) {
			this.message = message;
			this.code = code;
		}

	}

	private int errorCode;

	public DBException(DBErrors error) {
		super(error.message);
		this.errorCode = error.code;
	}

	public DBException(Exception e) {
		super(e);
		if (e.getClass().equals(DBException.class))
			errorCode = ((DBException) e).getErrorCode();
		else
			errorCode = 995;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return "ERRORCODE: " + errorCode + " " + super.getMessage();
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

}
