package com.mcgill.bandop.controllers;

import com.mcgill.bandop.models.AlgorithmType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/algorithm_types")
public class AlgorithmTypeController extends ApplicationController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AlgorithmType> listTypes() {
        return AlgorithmType.loadTypes(getDB());
    }

}
