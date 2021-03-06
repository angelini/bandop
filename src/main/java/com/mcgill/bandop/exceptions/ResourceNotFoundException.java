package com.mcgill.bandop.exceptions;

import javax.ws.rs.core.Response;

public class ResourceNotFoundException extends JSONWebException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException() {
		super(Response.Status.UNAUTHORIZED);
	}

	public ResourceNotFoundException(String message) {
		super(Response.Status.UNAUTHORIZED, message);
	}

}
