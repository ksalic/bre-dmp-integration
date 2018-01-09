package com.example.dmp.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.onehippo.cms7.targeting.Visitor;

/**
 * @version "\$Id$" kenan
 */
@Path("/integration/dmp")
@Produces(value = {MediaType.APPLICATION_JSON})
@Consumes(value = {MediaType.APPLICATION_JSON})
public interface DMPClient {


    @POST
    @Path("/{id}/{partner}")
    public void post(@PathParam("id") String id, @PathParam("partner") String foo, JsonNode data);


    @POST
    @Path("/{id}")
    public Visitor getVisitor(@PathParam("id") String id);


}
