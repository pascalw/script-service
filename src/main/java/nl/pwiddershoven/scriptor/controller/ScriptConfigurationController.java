package nl.pwiddershoven.scriptor.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nl.pwiddershoven.scriptor.repository.ScriptConfigurationRepository;
import nl.pwiddershoven.scriptor.service.ScriptConfiguration;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Consumes(MediaType.APPLICATION_JSON)
@Path("/configs")
public class ScriptConfigurationController {
    private final Logger logger = Logger.getLogger(ScriptConfigurationController.class);

    @Autowired
    private ScriptConfigurationRepository scriptConfigurationRepository;

    @POST
    public Response createConfiguration(@Valid ScriptConfigurationDTO scriptConfigurationDTO) {
        String id = scriptConfigurationRepository.save(scriptConfigurationDTO.toScriptConfiguration());
        return Response.created(getLocation(id)).build();
    }

    @PUT
    @Path("/{id}")
    public void updateConfiguration(@PathParam("id") String id, ScriptConfigurationDTO scriptConfigurationDTO) {
        scriptConfigurationRepository.update(id, scriptConfigurationDTO.toScriptConfiguration());
    }

    @GET
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
    @Path("/{id}")
    public ScriptConfigurationDTO getScriptConfiguration(@PathParam("id") String id) {
        ScriptConfiguration scriptConfiguration = findConfigurationOr404(id);
        return buildDTO(scriptConfiguration);
    }

    @DELETE
    @Path("/{id}")
    public void removeScriptConfiguration(@PathParam("id") String id) {
        scriptConfigurationRepository.remove(id);
    }

    private URI getLocation(String id) {
        try {
            return new URI("/config/" + id);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private ScriptConfigurationDTO buildDTO(ScriptConfiguration scriptConfiguration) {
        ScriptConfigurationDTO scriptConfigurationDTO = new ScriptConfigurationDTO();

        scriptConfigurationDTO.id = scriptConfiguration.id;
        scriptConfigurationDTO.script = scriptConfiguration.processingScript;
        scriptConfigurationDTO.contentType = scriptConfiguration.contentType;
        scriptConfigurationDTO.accessToken = scriptConfiguration.accessToken;

        return scriptConfigurationDTO;
    }

    private ScriptConfiguration findConfigurationOr404(String id) {
        ScriptConfiguration scriptConfiguration = scriptConfigurationRepository.find(id);

        if (scriptConfiguration == null)
            throw new WebApplicationException(404);

        return scriptConfiguration;
    }
}
