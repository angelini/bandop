package com.mcgill.bandop.controllers;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mcgill.bandop.models.User;

@Path("/users")
public class UsersController extends ApplicationController {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> listUsers() {
		return User.loadUsers(getDB());
	}

	@GET @Path("{user}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("user") String idString) {
		int id = Integer.parseInt(idString);
		return User.loadUser(getDB(), id);
	}

}
