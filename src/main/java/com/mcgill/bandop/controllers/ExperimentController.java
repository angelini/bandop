package com.mcgill.bandop.controllers;

import com.mcgill.bandop.exceptions.BadRequestException;
import com.mcgill.bandop.models.Design;
import com.mcgill.bandop.models.Experiment;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/experiments")
public class ExperimentController extends ApplicationController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Experiment> listExperiments() {
        int userId = AuthController.getLoggedInUser(getCookies(), getEncryptor());
        return Experiment.loadExperiments(getDB(), userId);
    }

    @GET @Path("{experiment}")
    @Produces(MediaType.APPLICATION_JSON)
    public Experiment getExperiment(@PathParam("experiment") String idString) {
        int userId = AuthController.getLoggedInUser(getCookies(), getEncryptor());

        try {
            int id = Integer.parseInt(idString);
            return Experiment.loadExperiment(getDB(), id);

        } catch(NumberFormatException e) {
            throw new BadRequestException("Invalid ID");
        }
    }

}
