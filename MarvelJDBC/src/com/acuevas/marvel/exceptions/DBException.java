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

	DBErrors error;

	public DBException(DBErrors error) {
		this.error = error;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString() {
		return error.message;
	}

}
