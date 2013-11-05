package com.mcgill.bandop.exceptions;

public class Error {

	private String message;

	public Error() {

	}

	public Error(String message) {
		this.setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
