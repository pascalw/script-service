package nl.pwiddershoven.script.service.script.module.response;

import javax.ws.rs.core.Response;

import nl.pwiddershoven.script.service.script.JsContext;
import nl.pwiddershoven.script.service.script.module.JsModule;
import nl.pwiddershoven.script.service.script.module.JsModuleProvider;

import org.springframework.stereotype.Component;

@Component
public class ResponseModuleProvider implements JsModuleProvider {
    public static class ResponseModule implements JsModule {
        public Response.ResponseBuilder builder() {
            return Response.ok();
        }
    }

    private final ResponseModule responseModule = new ResponseModule();

    @Override
    public String name() {
        return "response";
    }

    @Override
    public JsModule module(JsContext jsContext) {
        return responseModule;
    }
}
