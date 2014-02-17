package com.mcgill.bandop.controllers;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcgill.bandop.exceptions.BadRequestException;
import com.mcgill.bandop.models.Design;
import com.mcgill.bandop.models.Experiment;

public class DesignController extends ApplicationController {

    private Experiment experiment;

    public void init(Experiment experiment) {
        this.experiment = experiment;
    }

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Design> listDesigns() {
		List<Design> designs = Design.loadDesigns(getDB(), experiment.getId());

		for (Design design : designs) {
			design.loadStats(getWorker());
		}

		return designs;
	}

	@GET @Path("{design}")
	@Produces(MediaType.APPLICATION_JSON)
	public Design getDesign(@PathParam("design") String idString) {
		try {
			int id = Integer.parseInt(idString);
			Design design = Design.loadDesign(getDB(), id);

			design.loadStats(getWorker());
			return design;

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

		design.setExperimentId(experiment.getId());
		design.save(getDB());

		getWorker().addDesign(userId, design.getId());

		return Response.status(Response.Status.CREATED).build();
	}

	@PUT @Path("{design}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Design updateDesign(@PathParam("design") String idString, Design design) {
		int userId = AuthController.getLoggedInUser(getCookies(), getEncryptor());

		if (design.getName() == null) {
			throw new BadRequestException("Name required");
		}

		try {
			int id = Integer.parseInt(idString);

			design.setId(id);
			design.setExperimentId(experiment.getId());
			design.save(getDB());

			return design;

		} catch (NumberFormatException e) {
			throw new BadRequestException("Invalid ID");
		}
	}

}
