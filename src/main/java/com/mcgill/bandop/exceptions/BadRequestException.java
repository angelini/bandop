package com.mcgill.bandop.exceptions;

import javax.ws.rs.core.Response;

public class BadRequestException extends JSONWebException {

	private static final long serialVersionUID = 1L;

	public BadRequestException() {
		super(Response.Status.BAD_REQUEST);
	}

	public BadRequestException(String message) {
		super(Response.Status.BAD_REQUEST, message);
	}

}
