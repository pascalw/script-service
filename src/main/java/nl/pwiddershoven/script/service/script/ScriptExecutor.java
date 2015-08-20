package nl.pwiddershoven.script.service.script;

import java.util.*;

import javax.script.*;
import javax.ws.rs.container.ContainerRequestContext;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import nl.pwiddershoven.script.service.ScriptConfiguration;
import nl.pwiddershoven.script.service.script.module.JsModuleProvider;
import nl.pwiddershoven.script.service.script.module.ScriptExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScriptExecutor {
    private static final String SCRIPT_WRAPPER = "(function() { %s; })()";

    private ScriptEngine jsEngine;
    private Map<String, JsModuleProvider> jsModuleProviders = new HashMap<>();

    public ScriptExecutor() {
        NashornScriptEngineFactory scriptEngineFactory = new NashornScriptEngineFactory();
        jsEngine = scriptEngineFactory.getScriptEngine(new NashornClassFilter());
    }

    public Object execute(ScriptConfiguration scriptConfiguration) {
        return execute(scriptConfiguration, null);
    }

    public Object execute(ScriptConfiguration scriptConfiguration, ContainerRequestContext requestContext) {
        try {
            // create a fresh new scope for each script
            ScriptContext ctx = new SimpleScriptContext();

            Bindings bindings = jsEngine.createBindings();
            bindings.put("quit", null);
            bindings.put("exit", null);

            bindings.put("__ctx", new JsContext(requestContext));
            bindings.put("require", jsEngine.eval("function(moduleName) { return __ctx.require(moduleName); };", ctx));

            ctx.setBindings(bindings, ScriptContext.ENGINE_SCOPE);

            Object result = jsEngine.eval(String.format(SCRIPT_WRAPPER, scriptConfiguration.processingScript), ctx);

            if (result instanceof ScriptObjectMirror)
                result = MarshalingHelper.unwrap((ScriptObjectMirror) result);

            return result;
        } catch (ScriptException e) {
            throw new ScriptExecutionException(e);
        }
    }

    public class JsContext {
        public final ContainerRequestContext request;

        public JsContext(ContainerRequestContext requestContext) {
            this.request = requestContext;
        }

        public Object require(String moduleName) {
            JsModuleProvider moduleProvider = jsModuleProviders.get(moduleName);
            if (moduleProvider == null)
                throw new ScriptExecutionException(String.format("Module '%s' not found", moduleName));

            return moduleProvider.module(this);
        }
    }

    @Autowired
    public void setJsModuleProviders(Set<JsModuleProvider> jsModuleProviders) {
        for (JsModuleProvider moduleProvider : jsModuleProviders) {
            this.jsModuleProviders.put(moduleProvider.name(), moduleProvider);
        }
    }
}
