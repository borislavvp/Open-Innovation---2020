package com.kigalimedicine.service;

import com.kigalimedicine.model.tomtom.SearchResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@RegisterRestClient
@Path("/search/2")
public interface LocationService {

    @GET
    @Path("/search/{query}.json")
    SearchResponse getLocation(@PathParam("query") String query,
                               @QueryParam("key") String apiKey,
                               @QueryParam("idxSet") String idxSet,
                               @QueryParam("minFuzzyLevel") Integer minFuzzyLevel,
                               @QueryParam("maxFuzzyLevel") Integer maxFuzzyLevel);

}
