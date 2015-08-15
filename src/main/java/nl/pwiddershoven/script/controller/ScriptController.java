package nl.pwiddershoven.script.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
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
public class ScriptController {
    private final Logger logger = Logger.getLogger(ScriptController.class);

    static class ScriptConfigurationDTO {
        public String id;

        @NotNull
        public String script;

        @NotNull
        public String contentType;

        public String accessToken;
    }

    @Autowired
    private ScriptExecutor scriptExecutor;

    @Autowired
    private ScriptConfigurationRepository scriptConfigurationRepository;

    @POST
    @Path("/execute")
    public Response execute(ScriptConfigurationDTO scriptConfigurationDTO) {
        ScriptConfiguration scriptConfiguration = buildScriptConfiguration(scriptConfigurationDTO);
        return doExecute(scriptConfiguration);
    }

    @POST
    @Path("/configs")
    public Response createConfiguration(@Valid ScriptConfigurationDTO scriptConfigurationDTO) {
        String id = scriptConfigurationRepository.save(buildScriptConfiguration(scriptConfigurationDTO));
        return Response.created(getLocation(id)).build();
    }

    @PUT
    @Path("/configs/{id}")
    public void updateConfiguration(@PathParam("id") String id, ScriptConfigurationDTO scriptConfigurationDTO) {
        scriptConfigurationRepository.update(id, buildScriptConfiguration(scriptConfigurationDTO));
    }

    private ScriptConfiguration buildScriptConfiguration(ScriptConfigurationDTO scriptConfigurationDTO) {
        return new ScriptConfiguration(scriptConfigurationDTO.script, scriptConfigurationDTO.contentType, scriptConfigurationDTO.accessToken);
    }

    @GET
    @Path("/configs")
    public List<ScriptConfigurationDTO> getScriptConfigurations(
            @PathParam("id") String id,
            @DefaultValue("1") @QueryParam("page") int page,
            @DefaultValue("10") @QueryParam("perPage") int perPage) {
        int offset = (page - 1) * perPage;
        List<ScriptConfiguration> scriptConfigurations = scriptConfigurationRepository.findAll(offset, perPage);

        return scriptConfigurations.stream()
                .map(this::buildDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/configs/{id}")
    public ScriptConfigurationDTO getScriptConfiguration(@PathParam("id") String id) {
        ScriptConfiguration scriptConfiguration = findConfigurationOr404(id);
        return buildDTO(scriptConfiguration);
    }

    @DELETE
    @Path("/configs/{id}")
    public void removeScriptConfiguration(@PathParam("id") String id) {
        scriptConfigurationRepository.remove(id);
    }

    @GET
    @AuthenticationNotRequired
    @Path("/executions/{id}")
    public Object getConfiguration(@PathParam("id") String id, @Context UriInfo uriInfo) {
        ScriptConfiguration scriptConfiguration = findConfigurationOr404(id);
        checkValidAuth(scriptConfiguration, uriInfo);

        return doExecute(scriptConfiguration);
    }

    private void checkValidAuth(ScriptConfiguration scriptConfiguration, UriInfo uriInfo) {
        if (scriptConfiguration.accessToken == null)
            return;

        String token = uriInfo.getQueryParameters().getFirst("token");
        if (token == null || !token.equals(scriptConfiguration.accessToken))
            throw new WebApplicationException(401);
    }

    private ScriptConfiguration findConfigurationOr404(@PathParam("id") String id) {
        ScriptConfiguration scriptConfiguration = scriptConfigurationRepository.find(id);

        if (scriptConfiguration == null)
            throw new WebApplicationException(404);
        return scriptConfiguration;
    }

    private ScriptConfigurationDTO buildDTO(ScriptConfiguration scriptConfiguration) {
        ScriptConfigurationDTO scriptConfigurationDTO = new ScriptConfigurationDTO();

        scriptConfigurationDTO.id = scriptConfiguration.id;
        scriptConfigurationDTO.script = scriptConfiguration.processingScript;
        scriptConfigurationDTO.contentType = scriptConfiguration.contentType;
        scriptConfigurationDTO.accessToken = scriptConfiguration.accessToken;

        return scriptConfigurationDTO;
    }

    private Response doExecute(ScriptConfiguration scriptConfiguration) {
        long start = System.currentTimeMillis();
        Object result = scriptExecutor.execute(scriptConfiguration);
        long end = System.currentTimeMillis();

        logger.info("Processing took " + (end - start));

        return Response.ok()
                .type(scriptConfiguration.contentType)
                .entity(result)
                .build();
    }

    private URI getLocation(String id) {
        try {
            return new URI("/config/" + id);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
