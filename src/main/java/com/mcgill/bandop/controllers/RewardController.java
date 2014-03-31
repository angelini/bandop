package com.mcgill.bandop.controllers;

import com.mcgill.bandop.Reward;
import com.mcgill.bandop.Visualization;
import com.mcgill.bandop.exceptions.BadRequestException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/rewards")
public class RewardController extends ApplicationController{

    @POST
    @Path("{design}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response incrementSuccess(@PathParam("design") String idString, Reward reward) {
        if (!reward.isValid()) {
            throw new BadRequestException("Reward must be between 0 and 1");
        }

        try {
            int id = Integer.parseInt(idString);
            getWorker().pushDesignResult(id, reward.getValue());

            Visualization.sendReward(getDB(), id, reward.getValue());

            return Response.status(Response.Status.CREATED).build();

        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid ID");
        }
    }

}
