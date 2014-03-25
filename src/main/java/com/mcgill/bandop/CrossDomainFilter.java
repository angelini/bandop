package com.mcgill.bandop;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

public class CrossDomainFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext creq, ContainerResponseContext cres) throws IOException {
        cres.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:8010");
        cres.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, x-requested-with");
        cres.getHeaders().add("Access-Control-Allow-Credentials", "true");
        cres.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        cres.getHeaders().add("Access-Control-Max-Age", "1209600");
    }

}
