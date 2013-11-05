package com.mcgill.bandop.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class JSONWebException extends WebApplicationException {

	private static final long serialVersionUID = 1L;

	public JSONWebException(Status status) {
		super(Response.status(status)
				.entity(new Error(status.getReasonPhrase()))
				.type(MediaType.APPLICATION_JSON)
				.build());
	}

	public JSONWebException(Status status, String message) {
		super(Response.status(status)
				.entity(new Error(message))
				.type(MediaType.APPLICATION_JSON)
				.build());
	}

}
