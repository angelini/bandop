package com.mcgill.bandop.controllers;

import com.mcgill.bandop.exceptions.BadRequestException;
import com.mcgill.bandop.models.Algorithm;
import com.mcgill.bandop.models.Experiment;
import com.mcgill.bandopshared.RedisKeys;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

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
            return Experiment.loadExperiment(getDB(), userId, id);

        } catch(NumberFormatException e) {
            throw new BadRequestException("Invalid ID");
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createExperiment(Experiment experiment) {
        int userId = AuthController.getLoggedInUser(getCookies(), getEncryptor());

        if (experiment.getId() != 0) {
            throw new BadRequestException("Use PUT to update model");
        }

        if (experiment.getName() == null) {
            throw new BadRequestException("Name required");
        }

        if (experiment.getAlgorithm().getType() == 0) {
            throw new BadRequestException("Algorithm type required");
        }

        experiment.setUserId(userId);
        experiment.save(getDB());

        Algorithm algo = experiment.getAlgorithm();

        getWorker().addExperiment(experiment.getId(), algo.getType(), algo.getConfig());

        return Response.status(Response.Status.CREATED).build();
    }

}
