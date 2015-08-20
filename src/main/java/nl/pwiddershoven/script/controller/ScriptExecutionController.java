package nl.pwiddershoven.script.controller;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;

import nl.pwiddershoven.script.config.AuthenticationNotRequired;
import nl.pwiddershoven.script.repository.ScriptConfigurationRepository;
import nl.pwiddershoven.script.service.ScriptConfiguration;
import nl.pwiddershoven.script.service.script.ScriptExecutor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class ScriptExecutionController {
    private final Logger logger = Logger.getLogger(ScriptExecutionController.class);

    @Autowired
    private ScriptExecutor scriptExecutor;

    @Autowired
    private ScriptConfigurationRepository scriptConfigurationRepository;

    @POST
    @Path("/executions")
    public Response execute(ScriptConfigurationDTO scriptConfigurationDTO, @Context ContainerRequestContext requestContext) {
        ScriptConfiguration scriptConfiguration = scriptConfigurationDTO.toScriptConfiguration();
        return doExecute(scriptConfiguration, requestContext);
    }

    @GET
    @AuthenticationNotRequired
    @Path("/executions/{id}")
    public Object getExecuteById(@PathParam("id") String id, @Context ContainerRequestContext requestContext) {
        return doExecuteWithAuthCheck(id, requestContext);
    }

    @POST
    @AuthenticationNotRequired
    @Path("/executions/{id}")
    public Object postExecuteById(@PathParam("id") String id, @Context ContainerRequestContext requestContext) {
        return doExecuteWithAuthCheck(id, requestContext);
    }

    @PUT
    @AuthenticationNotRequired
    @Path("/executions/{id}")
    public Object putExecuteById(@PathParam("id") String id, @Context ContainerRequestContext requestContext) {
        return doExecuteWithAuthCheck(id, requestContext);
    }

    @PATCH
    @AuthenticationNotRequired
    @Path("/executions/{id}")
    public Object patchExecuteById(@PathParam("id") String id, @Context ContainerRequestContext requestContext) {
        return doExecuteWithAuthCheck(id, requestContext);
    }

    @DELETE
    @AuthenticationNotRequired
    @Path("/executions/{id}")
    public Object deleteExecuteById(@PathParam("id") String id, @Context ContainerRequestContext requestContext) {
        return doExecuteWithAuthCheck(id, requestContext);
    }

    private void checkValidAuth(ScriptConfiguration scriptConfiguration, UriInfo uriInfo) {
        if (scriptConfiguration.accessToken == null)
            return;

        String token = uriInfo.getQueryParameters().getFirst("token");
        if (token == null || !token.equals(scriptConfiguration.accessToken))
            throw new WebApplicationException(401);
    }

    private ScriptConfiguration findConfigurationOr404(String id) {
        ScriptConfiguration scriptConfiguration = scriptConfigurationRepository.find(id);

        if (scriptConfiguration == null)
            throw new WebApplicationException(404);
        return scriptConfiguration;
    }

    private Response doExecuteWithAuthCheck(String id, ContainerRequestContext requestContext) {
        ScriptConfiguration scriptConfiguration = findConfigurationOr404(id);
        checkValidAuth(scriptConfiguration, requestContext.getUriInfo());

        return doExecute(scriptConfiguration, requestContext);
    }

    private Response doExecute(ScriptConfiguration scriptConfiguration, ContainerRequestContext requestContext) {
        long start = System.currentTimeMillis();
        Object result = scriptExecutor.execute(scriptConfiguration, requestContext);
        long end = System.currentTimeMillis();

        logger.info("Processing took " + (end - start));

        return Response.ok()
                .type(scriptConfiguration.contentType)
                .entity(result)
                .build();
    }
}
