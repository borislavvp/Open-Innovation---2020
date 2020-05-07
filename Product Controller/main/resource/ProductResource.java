package com.kigalimedicine.resource;

import com.kigalimedicine.model.dto.PharmacyDto;
import com.kigalimedicine.model.dto.ProductDto;
import com.kigalimedicine.service.ProductService;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import io.quarkus.security.identity.SecurityIdentity;
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
import javax.ws.rs.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecuritySchemes(
        value = {@SecurityScheme(securitySchemeName = "bearer", type = SecuritySchemeType.HTTP,
                scheme = "bearer", bearerFormat = "jwt")})
public class ProductResource {

    @Inject
    ProductService productService;

    @Inject
    SecurityIdentity securityIdentity;


    @GET
    @APIResponses(value = {
            @APIResponse(
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY, implementation = ProductDto.class)))
            ,
            @APIResponse(
                    description = "Unauthorized",
                    responseCode = "401"
            )
    })
    public Response getProduct(@QueryParam("search") String searchResult, @QueryParam("page") @DefaultValue("0") Integer page) {
        try {
            return Response.ok(productService.getProduct(searchResult, page)).build();
        } catch (Exception e) {
            return Response.status(401).build();
        }
    }

    @POST
    @APIResponses(value = {
            @APIResponse(
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ProductDto.class)))
            ,
            @APIResponse(
                    description = "Unauthorized",
                    responseCode = "401"
            )
    })
    @SecurityRequirements(value = {@SecurityRequirement(name = "bearer", scopes = {})})
    public Response createProduct(ProductDto productDto) {
        try {
            return Response.ok(productService.createProduct(productDto, securityIdentity.getPrincipal().getName())).build();
        } catch (Exception e) {
            return Response.status(401).build();
        }
    }

    @Path("{productId}/pharmacies")
    @GET
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
    public Response getPharmacies(@PathParam("productId") Long productId) {
        try {
            return Response.ok(productService.getPharmacies(productId)).build();
        } catch (Exception e) {
            return Response.status(401).build();
        }
    }

    @Path("{productId}")
    @PUT
    @SecurityRequirements(value = {@SecurityRequirement(name = "bearer", scopes = {})})
    @APIResponses(value = {
            @APIResponse(
                    description = "Modified",
                    responseCode = "200"
            ),
            @APIResponse(
                    description = "Unauthorized",
                    responseCode = "401"
            )
    })
    public Response updateProduct(@PathParam("productId") Long productId, ProductDto product) {
        try {
            productService.updateProduct(productId, product);
            return Response.status(204).build();
        } catch (Exception e) {
            return Response.status(401).build();
        }
    }

    @Path("{productId}")
    @DELETE
    @SecurityRequirements(value = {@SecurityRequirement(name = "bearer", scopes = {})})
    @APIResponses(value = {
            @APIResponse(
                    description = "Deleted",
                    responseCode = "200"
            ),
            @APIResponse(
                    description = "Unauthorized",
                    responseCode = "401"
            )
    })
    public Response deleteProduct(@PathParam("productId") Long productId) {
        try {
            productService.deleteProduct(productId);
            return Response.status(204).build();
        } catch (Exception e) {
            return Response.status(401).build();
        }
    }
}