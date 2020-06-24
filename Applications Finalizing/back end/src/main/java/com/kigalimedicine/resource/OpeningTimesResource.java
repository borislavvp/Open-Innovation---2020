package com.kigalimedicine.resource;

import com.kigalimedicine.model.dto.OpeningTimeDto;
import com.kigalimedicine.service.OpeningTimeService;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirements;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/pharmacy/{pharmacyId}/opening-times")
public class OpeningTimesResource {

    @PathParam("pharmacyId")
    public String pharmacyId;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SecurityRequirements(value = {@SecurityRequirement(name = "bearer", scopes = {})})
    @APIResponses(value = {
            @APIResponse(
                    description = "Added",
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = OpeningTimeDto.class))),
            @APIResponse(
                    description = "Unauthorized",
                    responseCode = "401"
            ),
            @APIResponse(
                    description = "Not found",
                    responseCode = "404"
            )
    })
    public Response addOpeningTime(OpeningTimeDto openingTimeDto, @PathParam("pharmacyId") String pharmacyId) {
        try {
            return Response.ok(openingTimeService.addOpeningTime(openingTimeDto, pharmacyId)).build();
        }
        catch (NullPointerException | NumberFormatException e) {
            return Response.status(404).build();
        }
    }


    @Inject
    OpeningTimeService openingTimeService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SecurityRequirements(value = {@SecurityRequirement(name = "bearer", scopes = {})})
    @APIResponses(value = {
            @APIResponse(
                    description = "OK",
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY, implementation = OpeningTimeDto.class))),
            @APIResponse(
                    description = "Unauthorized",
                    responseCode = "401"
            ),
            @APIResponse(
                    description = "Not found",
                    responseCode = "404"
            )
    })
    public Response getOpeningTimes(@PathParam("pharmacyId") String pharmacyId) {
        try {
            return Response.ok(openingTimeService.getOpeningTimes(pharmacyId)).build();
        }
        catch (NullPointerException | NumberFormatException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }
    }

    @Path("/{openingTimeId}")
    @DELETE
    @SecurityRequirements(value = {@SecurityRequirement(name = "bearer", scopes = {})})
    @APIResponses(value = {
            @APIResponse(
                    description = "Deleted",
                    responseCode = "204"
            ),
            @APIResponse(
                    description = "Unauthorized",
                    responseCode = "401"
            ),
            @APIResponse(
                    description = "Not found",
                    responseCode = "404"
            )
    })
    public Response deleteOpeningTime(@PathParam("openingTimeId") String openingTimeId) {
        try {
            openingTimeService.deleteOpeningTime(openingTimeId);
            return Response.status(204).build();
        }
        catch (NullPointerException | NumberFormatException e) {
            return Response.status(400).build();
        }

    }
}
