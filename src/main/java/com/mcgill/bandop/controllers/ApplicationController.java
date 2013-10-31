package com.mcgill.bandop.controllers;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import com.mcgill.bandop.Database;

public class ApplicationController {

	@Context
	UriInfo uriInfo;

	@Context
	Request request;

	@Context
	ServletContext context;

	public Database getDB() {
		return (Database) context.getAttribute("db");
	}

}
