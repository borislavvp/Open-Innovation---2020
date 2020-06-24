package com.kigalimedicine.resource;

import com.kigalimedicine.model.dto.PharmacyDto;
import com.kigalimedicine.model.tomtom.SearchResponse;
import com.kigalimedicine.service.LocationService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/location")
public class LocationResource {

    @Inject
    @RestClient
    LocationService locationService;

    @ConfigProperty(name = "tomtom.apiKey")
    String locationApiKey;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = SearchResponse.class))
            )
    })
    public Response getLocation(@QueryParam("q") String query) {
        SearchResponse searchResponse = locationService.getLocation(query, locationApiKey, "POI,PAD", 3, 4);
        System.out.println(searchResponse);
        return Response.ok(searchResponse).build();
    }

}
