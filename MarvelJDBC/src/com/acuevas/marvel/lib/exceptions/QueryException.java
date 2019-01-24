package com.acuevas.marvel.lib.exceptions;

@SuppressWarnings("serial")
public class QueryException extends Exception {

	public enum QueryError {
		WHERE_BEFORE("where() must be executed before this call"), NOT_SUPPORTED("inserted type is not supported:"),
		NOT_WHERE_BEFORE("Wrong usage of where()"), INNER_BEFORE("innerJoin() must be executed before this call");

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

	public QueryException(QueryError error, Object comparator) {
		super(error.message + " " + comparator.getClass());
	}

	public QueryException(Throwable cause) {
		super(cause);
	}
}
