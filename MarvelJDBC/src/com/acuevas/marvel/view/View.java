package com.acuevas.marvel.view;

public class View {

	public enum ViewMessage {
		USER_REGISTERED("User registered."), GOODBYE("Goodbye!");

		private String message;

		private ViewMessage(String message) {
			this.message = message;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return message;
		}

	}

	public enum ViewError {
		CRITICAL_ERROR("A critical error ocurred, program is going to close.");

		private String message;

		private ViewError(String message) {
			this.message = message;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return super.toString();
		}

	}

	public static void printError(String error) {
		System.err.println(error);
	}

	public static void printError(ViewError error) {
		System.err.println(error);
	}

	public static void printMessage(String message) {
		System.out.println(message);
	}

	public static void printMessage(ViewMessage message) {
		System.out.println(message);
	}
}
