package com.acuevas.marvel.exceptions;

@SuppressWarnings("serial")
public class CommandException extends Exception {

	public enum CommandErrors {
		INCORRECT_NUM_ARGUMENTS("[ Wrong number of arguments ]", 200), USER_EXISTS("[ User already exists ]", 201),
		SUPERHERO_NOEXISTS("[ There isn't a superhero with that name ]", 202),
		USER_OR_PSWRD_INCORRECT("[ User or password incorrect ]", 203), WRONG_DIRECTION("[ Wrong direction ]", 204),
		WRONG_COMMAND("[ Wrong command ]", 205);

		private String message;
		private int code;

		CommandErrors(String error, int code) {
			this.message = error;
			this.code = code;
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * @return the code
		 */
		public int getCode() {
			return code;
		}

	}

	CommandErrors error;

	public CommandException(CommandErrors error) {
		this.error = error;
	}

}
