package com.mcgill.bandop.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ResourceNotFoundException extends WebApplicationException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException() {
		super(Response.status(Response.Status.NOT_FOUND).build());
	}

	public ResourceNotFoundException(String message) {
		super(Response.status(Response.Status.NOT_FOUND)
				.entity(message).type("text/plain").build());
	}
}
