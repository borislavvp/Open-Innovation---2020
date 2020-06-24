package com.kigalimedicine.resource;

import com.kigalimedicine.model.dto.InsuranceDto;
import com.kigalimedicine.model.dto.InventoryItemDto;
import com.kigalimedicine.service.InsuranceService;
import io.quarkus.security.identity.SecurityIdentity;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirements;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/insurance")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecuritySchemes(
        value = {@SecurityScheme(securitySchemeName = "bearer", type = SecuritySchemeType.HTTP,
                scheme = "bearer", bearerFormat = "jwt")})
public class InsuranceResource {

    @Inject
    InsuranceService insuranceService;

    @Inject
    SecurityIdentity securityIdentity;

    @GET
    @APIResponses(value = {
            @APIResponse(
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY, implementation = InsuranceDto.class)))
            ,
            @APIResponse(
                    description = "Unauthorized",
                    responseCode = "401"
            )
    })
    @SecurityRequirements(value = {@SecurityRequirement(name = "bearer", scopes = {})})
    public Response getInsurances(@QueryParam("page") @DefaultValue("0") Integer page, @QueryParam("search") @DefaultValue("") String search) {
        try {
            return Response.ok(insuranceService.getInsurances(search, page)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(401).build();
        }
    }

    @POST
    @SecurityRequirements(value = {@SecurityRequirement(name = "bearer", scopes = {})})
    @APIResponses(value = {
            @APIResponse(
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = InsuranceDto.class)))
            ,
            @APIResponse(
                    description = "Unauthorized",
                    responseCode = "401"
            )
    })
    public Response addInsurance(@Valid InsuranceDto insurance) {
        try {
            return Response.ok(insuranceService.addInsurance(insurance)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(401).build();
        }
    }

    @PUT
    @Path("{insuranceId}")
    @SecurityRequirements(value = {@SecurityRequirement(name = "bearer", scopes = {})})
    @APIResponses(value = {
            @APIResponse(
                    description = "OK"
            )
            ,
            @APIResponse(
                    description = "Unauthorized",
                    responseCode = "401"
            )
    })
    public Response updateInsurance(@PathParam("insuranceId") Long insuranceId, @Valid InsuranceDto insurance) {
        try {
            insuranceService.updateInsurance(insuranceId, insurance);
            return Response.status(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(401).build();
        }
    }

    @DELETE
    @Path("{insuranceId}")
    @SecurityRequirements(value = {@SecurityRequirement(name = "bearer", scopes = {})})
    @APIResponses(value = {
            @APIResponse(
                    description = "OK",
                    responseCode = "200"
            ),
            @APIResponse(
                    description = "Unauthorized",
                    responseCode = "401"
            )
    })
    public Response deleteInsurance(@PathParam("insuranceId") Long insuranceId) {
        try {
            insuranceService.deleteInsurance(insuranceId);
            return Response.status(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(401).build();
        }
    }

    @DELETE
    @Path("{insuranceId}/{pharmacyId}")
    @SecurityRequirements(value = {@SecurityRequirement(name = "bearer", scopes = {})})
    @APIResponses(value = {
            @APIResponse(
                    description = "OK",
                    responseCode = "200"
            ),
            @APIResponse(
                    description = "Unauthorized",
                    responseCode = "401"
            )
    })
    public Response removeInsuranceFromPharmacy(@PathParam("insuranceId") Long insuranceId, @PathParam("pharmacyId") Long pharmacyId) {
        try {
            insuranceService.removeInsuranceFromPharmacy(insuranceId, pharmacyId, securityIdentity.getPrincipal().getName());
            return Response.status(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(401).build();
        }
    }



    @POST
    @Path("{insuranceId}/{pharmacyId}")
    @SecurityRequirements(value = {@SecurityRequirement(name = "bearer", scopes = {})})
    @APIResponses(value = {
            @APIResponse(
                    description = "OK")
            ,
            @APIResponse(
                    description = "Unauthorized",
                    responseCode = "401"
            )
    })
    public Response addInsuranceToPharmacy(@PathParam("insuranceId") Long insuranceId, @PathParam("pharmacyId") Long pharmacyId) {
        try {
            insuranceService.addInsuranceToPharmacy(insuranceId, pharmacyId, securityIdentity.getPrincipal().getName());
            return Response.status(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(401).build();
        }
    }
}
