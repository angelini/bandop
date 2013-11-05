package com.mcgill.bandop.exceptions;

import javax.ws.rs.core.Response;

public class DatabaseException extends JSONWebException {

	private static final long serialVersionUID = 1L;

	public DatabaseException() {
		super(Response.Status.INTERNAL_SERVER_ERROR);
		printStackTrace(System.err);
	}

	public DatabaseException(String message) {
		super(Response.Status.INTERNAL_SERVER_ERROR, message);
		printStackTrace(System.err);
	}

}
