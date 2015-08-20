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
    @Path("/execute")
    public Response execute(ScriptConfigurationDTO scriptConfigurationDTO, @Context ContainerRequestContext requestContext) {
        ScriptConfiguration scriptConfiguration = scriptConfigurationDTO.toScriptConfiguration();
        return doExecute(scriptConfiguration, requestContext);
    }

    @GET
    @AuthenticationNotRequired
    @Path("/executions/{id}")
    public Object getConfiguration(@PathParam("id") String id, @Context ContainerRequestContext requestContext, @Context UriInfo uriInfo) {
        ScriptConfiguration scriptConfiguration = findConfigurationOr404(id);
        checkValidAuth(scriptConfiguration, uriInfo);

        return doExecute(scriptConfiguration, requestContext);
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
