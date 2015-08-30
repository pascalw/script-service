package nl.pwiddershoven.scriptor.controller;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;

import nl.pwiddershoven.scriptor.config.AuthenticationNotRequired;
import nl.pwiddershoven.scriptor.repository.EndpointConfigurationRepository;
import nl.pwiddershoven.scriptor.service.EndpointConfiguration;
import nl.pwiddershoven.scriptor.service.script.ScriptExecutor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/")
public class EndpointController {
    private final Logger logger = Logger.getLogger(EndpointController.class);

    @Autowired
    private ScriptExecutor scriptExecutor;

    @Autowired
    private EndpointConfigurationRepository endpointConfigurationRepository;

    @POST
    @Path("/endpointPreview")
    public Response execute(EndpointConfigurationDTO endpointConfigurationDTO, @Context ContainerRequestContext requestContext) {
        EndpointConfiguration endpointConfiguration = endpointConfigurationDTO.toEndpointConfiguration();
        return doExecute(endpointConfiguration, requestContext);
    }

    @GET
    @AuthenticationNotRequired
    @Path("/endpoints/{id}")
    public Object getExecuteById(@PathParam("id") String id, @Context ContainerRequestContext requestContext) {
        return doExecuteWithAuthCheck(id, requestContext);
    }

    @POST
    @AuthenticationNotRequired
    @Path("/endpoints/{id}")
    public Object postExecuteById(@PathParam("id") String id, @Context ContainerRequestContext requestContext) {
        return doExecuteWithAuthCheck(id, requestContext);
    }

    @PUT
    @AuthenticationNotRequired
    @Path("/endpoints/{id}")
    public Object putExecuteById(@PathParam("id") String id, @Context ContainerRequestContext requestContext) {
        return doExecuteWithAuthCheck(id, requestContext);
    }

    @PATCH
    @AuthenticationNotRequired
    @Path("/endpoints/{id}")
    public Object patchExecuteById(@PathParam("id") String id, @Context ContainerRequestContext requestContext) {
        return doExecuteWithAuthCheck(id, requestContext);
    }

    @DELETE
    @AuthenticationNotRequired
    @Path("/endpoints/{id}")
    public Object deleteExecuteById(@PathParam("id") String id, @Context ContainerRequestContext requestContext) {
        return doExecuteWithAuthCheck(id, requestContext);
    }

    private void checkValidAuth(EndpointConfiguration endpointConfiguration, UriInfo uriInfo) {
        if (endpointConfiguration.accessToken == null)
            return;

        String token = uriInfo.getQueryParameters().getFirst("token");
        if (token == null || !token.equals(endpointConfiguration.accessToken))
            throw new WebApplicationException(401);
    }

    private EndpointConfiguration findConfigurationOr404(String id) {
        EndpointConfiguration endpointConfiguration = endpointConfigurationRepository.find(id);

        if (endpointConfiguration == null)
            throw new WebApplicationException(404);
        return endpointConfiguration;
    }

    private Response doExecuteWithAuthCheck(String id, ContainerRequestContext requestContext) {
        EndpointConfiguration endpointConfiguration = findConfigurationOr404(id);
        checkValidAuth(endpointConfiguration, requestContext.getUriInfo());

        return doExecute(endpointConfiguration, requestContext);
    }

    private Response doExecute(EndpointConfiguration endpointConfiguration, ContainerRequestContext requestContext) {
        long start = System.currentTimeMillis();
        Object result = scriptExecutor.execute(endpointConfiguration.processingScript, requestContext);
        long end = System.currentTimeMillis();

        logger.info("Processing took " + (end - start));

        if (result instanceof Response)
            return ensureMediaType((Response) result, endpointConfiguration);

        return Response.ok()
                .type(endpointConfiguration.contentType)
                .entity(result)
                .build();
    }

    private Response ensureMediaType(Response response, EndpointConfiguration endpointConfiguration) {
        if (response.getMediaType() == null)
            response = Response.fromResponse(response)
                    .type(endpointConfiguration.contentType)
                    .build();

        return response;
    }
}
