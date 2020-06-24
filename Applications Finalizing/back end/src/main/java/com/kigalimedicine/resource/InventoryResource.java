package com.kigalimedicine.resource;

import com.kigalimedicine.model.dto.InventoryItemDto;
import com.kigalimedicine.model.dto.PharmacyDto;
import com.kigalimedicine.service.InventoryService;
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

@Path("/inventory")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecuritySchemes(
        value = {@SecurityScheme(securitySchemeName = "bearer", type = SecuritySchemeType.HTTP,
                scheme = "bearer", bearerFormat = "jwt")})
public class InventoryResource {

    @Inject
    InventoryService inventoryService;

    @Inject
    SecurityIdentity securityIdentity;

    @GET
    @Path("{pharmacyId}")
    @APIResponses(value = {
            @APIResponse(
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY, implementation = InventoryItemDto.class)))
            ,
            @APIResponse(
                    description = "Unauthorized",
                    responseCode = "401"
            )
    })
    @SecurityRequirements(value = {@SecurityRequirement(name = "bearer", scopes = {})})
    public Response getPharmacyInventory(@PathParam("pharmacyId") Long pharmacyId, @QueryParam("page") @DefaultValue("0") Integer page, @QueryParam("search") @DefaultValue("") String search, @QueryParam("quantityOrderAsc") @DefaultValue("true") Boolean quantityOrderAsc) {
        try {
            return Response.ok(inventoryService.getPharmacyInventory(pharmacyId, page, securityIdentity.getPrincipal().getName(), search, quantityOrderAsc)).build();
        } catch (Exception e) {
            return Response.status(401).build();
        }
    }

    @POST
    @Path("{pharmacyId}/{productId}")
    @SecurityRequirements(value = {@SecurityRequirement(name = "bearer", scopes = {})})
    @APIResponses(value = {
            @APIResponse(
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = InventoryItemDto.class)))
            ,
            @APIResponse(
                    description = "Unauthorized",
                    responseCode = "401"
            )
    })
    public Response additem(@PathParam("pharmacyId") Long pharmacyId, @PathParam("productId") Long productId) {
        try {
            return Response.ok(inventoryService.additem(pharmacyId, productId, securityIdentity.getPrincipal().getName())).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(401).build();
        }
    }

    @PUT
    @Path("{inventoryItemId}")
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
    public Response updateItem(@PathParam("inventoryItemId") Long inventoryItemId, @Valid InventoryItemDto item) {
        try {
            inventoryService.updateItem(inventoryItemId, item, securityIdentity.getPrincipal().getName());
            return Response.status(200).build();
        } catch (Exception e) {
            return Response.status(401).build();
        }
    }

    @DELETE
    @Path("{pharmacyId}/{productId}")
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
    public Response deleteItem(@PathParam("pharmacyId") Long pharmacyId, @PathParam("productId") Long productId) {
        try {
            inventoryService.deleteItem(pharmacyId, productId, securityIdentity.getPrincipal().getName());
            return Response.status(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(401).build();
        }
    }
    
}
