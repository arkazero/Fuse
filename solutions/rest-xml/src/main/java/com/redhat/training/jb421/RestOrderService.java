package com.redhat.training.jb421;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.redhat.training.jb421.model.OrderItem;

/**
 * A JAX-RS Resource interface where we define the RESTful web service, using the JAX-RS annotations.
 * 
 * This REST service supports xml as the data format.
 */
@Path("/orders/")
@Consumes(value = "application/xml")
@Produces(value = "application/xml")
public interface RestOrderService {

    

    /**
     * The PUT update order operation
     */
    @PUT
    Response updateOrder(@QueryParam("id") Integer orderId,OrderItem orderItem);

    /**
     * The POST create order operation
     */
    @POST
    Response createOrder(@QueryParam("id") Integer orderId);

    /**
     * The DELETE cancel order operation
     */
    @DELETE
    Response cancelOrder(@QueryParam("id") Integer orderId);
}
