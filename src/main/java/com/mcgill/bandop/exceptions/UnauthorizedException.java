package com.mcgill.bandop.exceptions;

import javax.ws.rs.core.Response;

public class UnauthorizedException extends JSONWebException {

	private static final long serialVersionUID = 1L;

	public UnauthorizedException() {
		super(Response.Status.UNAUTHORIZED);
	}

	public UnauthorizedException(String message) {
		super(Response.Status.UNAUTHORIZED, message);
	}

}
