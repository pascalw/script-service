package nl.pwiddershoven.scriptor.service.script;

import javax.ws.rs.container.ContainerRequestContext;

public class Script {
    public final String id;
    public final String code;
    public final ContainerRequestContext requestContext;

    public Script(String id, String code, ContainerRequestContext requestContext) {
        this.id = id;
        this.code = code;
        this.requestContext = requestContext;
    }
}
