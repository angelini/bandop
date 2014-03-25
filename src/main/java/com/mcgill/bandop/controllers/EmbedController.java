package com.mcgill.bandop.controllers;

import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.mcgill.bandop.exceptions.BadRequestException;
import com.mcgill.bandop.models.Design;

@Path("/embed")
public class EmbedController extends ApplicationController {

	@GET @Path("js/{experiment}")
	@Produces("application/javascript")
	public String getJsFile(@PathParam("experiment") String expString, @Context UriInfo uri) {
		try {
			int experiment = Integer.parseInt(expString);
            int id = getWorker().getDesignId(experiment, new Random(Double.doubleToLongBits(Math.random())));

			String jsFile = Design.loadJsFile(getDB(), id);
            String cssUri = uri.getAbsolutePath().toString().replaceFirst("js/\\d+$", "css/" + id);

            jsFile += ";\n" +
                      "var link = document.createElement('link');\n" +
                      "link.setAttribute('rel', 'stylesheet');\n" +
                      "link.setAttribute('type', 'text/css');\n" +
                      "link.setAttribute('href', '" + cssUri + "');\n" +
                      "\n" +
                      "document.getElementsByTagName('head')[0].appendChild(link);";

            return jsFile;

		} catch (NumberFormatException e) {
			throw new BadRequestException("Invalid ID");
		}
	}

	@GET @Path("css/{design}")
	@Produces("text/css")
	public String getCssFile(@PathParam("design") String designString) {
		try {
			int design = Integer.parseInt(designString);
			return Design.loadCssFile(getDB(), design);

		} catch (NumberFormatException e) {
			throw new BadRequestException("Invalid ID");
		}
	}

}
