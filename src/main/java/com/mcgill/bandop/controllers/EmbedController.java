package com.mcgill.bandop.controllers;

import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.mcgill.bandop.exceptions.BadRequestException;
import com.mcgill.bandop.models.Design;

@Path("/embed")
public class EmbedController extends ApplicationController {

	@GET @Path("js/{design}")
	@Produces("application/javascript")
	public String getJsFile(@PathParam("design") String idString) {
		try {
			int userId = Integer.parseInt(idString);
			int id = getWorker().getDesignId(userId, generateMinuteRandom());

			return Design.loadJsFile(getDB(), id);

		} catch (NumberFormatException e) {
			throw new BadRequestException("Invalid ID");
		}
	}

	@GET @Path("css/{design}")
	@Produces("text/css")
	public String getCssFile(@PathParam("design") String idString) {
		try {
			int userId = Integer.parseInt(idString);
			int id = getWorker().getDesignId(userId, generateMinuteRandom());

			return Design.loadCssFile(getDB(), id);

		} catch (NumberFormatException e) {
			throw new BadRequestException("Invalid ID");
		}
	}

	private Random generateMinuteRandom() {
		return new Random(System.currentTimeMillis() / 60000);
	}

}
