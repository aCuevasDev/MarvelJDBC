package com.acuevas.marvel.exceptions;

@SuppressWarnings("serial")
public class QueryException extends Exception {

	public enum QueryError {
		WHERE_BEFORE("where() must be executed before this method can be called");

		private String message;

		QueryError(String string) {
			message = string;
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}
	}

	public QueryException(QueryError error) {
		super(error.message);
	}
}
