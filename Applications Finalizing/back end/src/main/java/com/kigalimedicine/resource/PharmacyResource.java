package com.kigalimedicine.resource;

import com.kigalimedicine.model.dto.PharmacyDto;
import com.kigalimedicine.model.dto.PharmacyMemberDto;
import com.kigalimedicine.service.PharmacyService;
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

@Path("/pharmacy")
@SecuritySchemes(
        value = {@SecurityScheme(securitySchemeName = "bearer", type = SecuritySchemeType.HTTP,
                scheme = "bearer", bearerFormat = "jwt")})
public class PharmacyResource {

    @Inject
    PharmacyService pharmacyService;

    @Inject
    SecurityIdentity securityIdentity;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = PharmacyDto.class)))
    public Response getAllPharmacies(@QueryParam("page") @DefaultValue("0") Integer page,
                                     @QueryParam("showNotApproved") @DefaultValue("true") Boolean showNotApproved,
                                     @QueryParam("search") @DefaultValue("") String searchParam) {
        try {
            return Response.ok(pharmacyService.getAllPharmacies(page, searchParam, showNotApproved)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(401).build();
        }
    }

    @GET
    @Path("{pharmacyId}")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PharmacyDto.class))
            ),
            @APIResponse(
                    description = "Not found",
                    responseCode = "404"
            )
    })

    public Response getPharmacy(@PathParam("pharmacyId") String pharmacyId) {
        try {
            return Response.ok(pharmacyService.getPharmacyById(Long.parseLong(pharmacyId))).build();
        } catch (NullPointerException e) {
            return Response.status(404).build();
        }
    }

    @Path("owned")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SecurityRequirements(value = {@SecurityRequirement(name = "bearer", scopes = {})})
    @APIResponses(value = {
            @APIResponse(
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY, implementation = PharmacyDto.class)))
            ,
            @APIResponse(
                    description = "Unauthorized",
                    responseCode = "401"
            )
    })
    public Response getOwnedPharmacies(@QueryParam("page") @DefaultValue("0") Integer page,
                                       @QueryParam("search") String searchParam) {
        return Response.ok(pharmacyService.getOwnedPharmacies(securityIdentity.getPrincipal().getName(), page, searchParam)).build();

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SecurityRequirements(value = {@SecurityRequirement(name = "bearer", scopes = {})})
    @APIResponses(value = {
            @APIResponse(
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PharmacyDto.class)))
            ,
            @APIResponse(
                    description = "Unauthorized",
                    responseCode = "401"
            )
    })
    public Response createPharmacy(@Valid PharmacyDto pharmacyDto) {
        if (securityIdentity.getPrincipal().getName().isEmpty()) {
            return Response.status(401).build();
        }
        return Response.ok(pharmacyService.createPharmacy(pharmacyDto, securityIdentity.getPrincipal().getName())).build();
    }

    @Path("{pharmacyId}/member")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(
                    description = "Created",
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PharmacyMemberDto.class))),
            @APIResponse(
                    description = "Unauthorized",
                    responseCode = "401"
            ),
            @APIResponse(
                    description = "Not found",
                    responseCode = "404"
            )
    })
    @SecurityRequirements(value = {@SecurityRequirement(name = "bearer", scopes = {})})
    public Response addPharmacyMember(@PathParam("pharmacyId") String pharmacyId, PharmacyMemberDto pharmacyMemberDto) {
        //TODO: check if user owns pharmacy or is admin
        try {
            return Response.ok(pharmacyService.addPharmacyMember(pharmacyId, pharmacyMemberDto)).build();
        } catch (NullPointerException ignored) {
            return Response.status(404).build();
        }
    }

    @Path("{pharmacyId}/member")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SecurityRequirements(value = {@SecurityRequirement(name = "bearer", scopes = {})})
    @APIResponses(value = {
            @APIResponse(
                    description = "OK",
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY, implementation = PharmacyMemberDto.class))),
            @APIResponse(
                    description = "Unauthorized",
                    responseCode = "401"
            ),
            @APIResponse(
                    description = "Not found",
                    responseCode = "404"
            )
    })
    public Response getPharmacyMembers(@PathParam("pharmacyId") String pharmacyId) {
        //Todo: check if user has access to this pharmacy
        try {
            return Response.ok(pharmacyService.getPharmacyMembers(pharmacyId)).build();
        } catch (NullPointerException ignored) {
            return Response.status(404).build();
        }
    }

    @Path("{pharmacyId}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SecurityRequirements(value = {@SecurityRequirement(name = "bearer", scopes = {})})
    @APIResponses(value = {
            @APIResponse(
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PharmacyDto.class)))
            ,
            @APIResponse(
                    description = "Unauthorized",
                    responseCode = "401"
            ),
            @APIResponse(
                    description = "Not found",
                    responseCode = "404"
            )
    })
    public Response editPharmacy(@Valid PharmacyDto pharmacyDto, @PathParam("pharmacyId") String pharmacyId) {
        //TODO: check if user is owner or admin.
        try {
            return Response.ok(pharmacyService.editPharmacy(pharmacyId, pharmacyDto)).build();
        } catch (NullPointerException ignored) {
            return Response.status(404).build();
        }
    }

    @Path("{pharmacyId}")
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
    public Response deletePharmacy(@PathParam("pharmacyId") String pharmacyId) {
        //TODO: check if user is owner or admin
        try {
            pharmacyService.deletePharmacy(pharmacyId);
        } catch (NullPointerException ignored) {
            return Response.status(404).build();
        }
        return Response.noContent().build();
    }
}
