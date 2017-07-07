package com.redhat.training.jb421;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * A JAX-RS Resource interface where we define the RESTful web service, using the JAX-RS annotations.
 * 
 * This REST service supports json as the data format.
 */
@Path("/orders/")
@Consumes(value = "application/json")
@Produces(value = "application/json")
public interface RestOrderService {

    @GET
    @Path(value="/shipAddress/{id}")
    Response shipAddress(@PathParam("id") Integer id);

    @GET
    @Path(value="/bookTitles/{id}")
    Response bookTitles(@PathParam("id") Integer id);

}
