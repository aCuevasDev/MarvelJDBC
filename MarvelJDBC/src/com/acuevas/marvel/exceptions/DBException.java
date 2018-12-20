package com.acuevas.marvel.exceptions;

@SuppressWarnings("serial")
public class DBException extends Exception {

	public enum DBErrors {
		DOESNT_EXIST("The object requested doesn't exist in the database.");
		private String message;

		private DBErrors(String message) {
			this.message = message;
		}
	}

	public DBException(DBErrors error) {
		super(error.message);
	}

	public DBException(Exception e) {
		super(e);
	}

}
