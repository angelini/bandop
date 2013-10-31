package com.mcgill.bandop.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class DatabaseException extends WebApplicationException {

	private static final long serialVersionUID = 1L;

	public DatabaseException() {
		super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());

		printStackTrace(System.err);
	}

	public DatabaseException(String message) {
		super(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(message).type("text/plain").build());

		printStackTrace(System.err);
	}

}
