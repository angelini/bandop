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
import com.mcgill.bandop.models.Design;

@Path("/designs")
public class DesignController extends ApplicationController {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Design> listDesigns() {
		int userId = AuthController.getLoggedInUser(getCookies(), getEncryptor());
		return Design.loadDesigns(getDB(), userId);
	}

	@GET @Path("{design}")
	@Produces(MediaType.APPLICATION_JSON)
	public Design getDesign(@PathParam("design") String idString) {
		int userId = AuthController.getLoggedInUser(getCookies(), getEncryptor());

		try {
			int id = Integer.parseInt(idString);
			return Design.loadDesign(getDB(), userId, id);

		} catch (NumberFormatException e) {
			throw new BadRequestException("Invalid ID");
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createDesign(Design design) {
		int userId = AuthController.getLoggedInUser(getCookies(), getEncryptor());

		if (design.getId() != 0) {
			throw new BadRequestException("Use PUT to update model");
		}

		if (design.getName() == null) {
			throw new BadRequestException("Name required");
		}

		design.setUserId(userId);
		design.save(getDB());

		return Response.status(Response.Status.CREATED).build();
	}

}
