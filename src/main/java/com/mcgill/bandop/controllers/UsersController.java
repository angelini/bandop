package com.mcgill.bandop.controllers;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcgill.bandop.exceptions.BadRequestException;
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
		try {
			int id = Integer.parseInt(idString);
			return User.loadUser(getDB(), id);

		} catch (NumberFormatException e) {
			throw new BadRequestException("Invalid ID");
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser(User user) {
		if (user.getId() != 0) {
			throw new BadRequestException("Use PUT to update model");
		}

		if (user.getEmail() == null || user.getPassword() == null) {
			throw new BadRequestException("Email and Password required");
		}

		user.setPassword(encrypt(user.getPassword()));
		user.save(getDB());

		getWorker().addUser(user.getId());

		return Response.status(Response.Status.CREATED).build();
	}

}
