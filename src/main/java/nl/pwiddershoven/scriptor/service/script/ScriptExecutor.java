package nl.pwiddershoven.scriptor.service.script;

import java.util.*;

import javax.script.*;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import nl.pwiddershoven.scriptor.service.script.module.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScriptExecutor {
    private static final String SCRIPT_WRAPPER = "(function() { %s; })()";

    private ScriptEngine jsEngine;
    private Map<String, JsModuleProvider> jsModuleProviders = new HashMap<>();

    @Autowired
    public ScriptExecutor(Set<JsModuleProvider> jsModuleProviders) {
        NashornScriptEngineFactory scriptEngineFactory = new NashornScriptEngineFactory();
        jsEngine = scriptEngineFactory.getScriptEngine(new NashornClassFilter());

        for (JsModuleProvider moduleProvider : jsModuleProviders) {
            this.jsModuleProviders.put(moduleProvider.name(), moduleProvider);
        }
    }

    public Object execute(String script) {
        return execute(new Script(null, script, null));
    }

    public Object execute(Script script) {
        try {
            // create a fresh new scope for each script
            ScriptContext ctx = new SimpleScriptContext();

            Bindings bindings = jsEngine.createBindings();
            bindings.put("quit", null);
            bindings.put("exit", null);

            bindings.put("__ctx", new JsContext(script));
            bindings.put("require", jsEngine.eval("function(moduleName) { return __ctx.require(moduleName); };", ctx));

            ctx.setBindings(bindings, ScriptContext.ENGINE_SCOPE);

            Object result = jsEngine.eval(String.format(SCRIPT_WRAPPER, script.code), ctx);

            if (result instanceof ScriptObjectMirror)
                result = MarshalingHelper.unwrap((ScriptObjectMirror) result);

            return result;
        } catch (ScriptException e) {
            throw new ScriptExecutionException(e);
        }
    }

    public class JsContext implements nl.pwiddershoven.scriptor.service.script.JsContext {
        private final Map<String, Object> attributes = new HashMap<>();

        public JsContext(Script script) {
            attributes.put("id", script.id);
            attributes.put("request", script.requestContext);
        }

        @Override
        public JsModule require(String moduleName) {
            JsModuleProvider moduleProvider = jsModuleProviders.get(moduleName);
            if (moduleProvider == null)
                throw new ScriptExecutionException(String.format("Module '%s' not found", moduleName));

            return moduleProvider.module(this);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T getAttribute(String attributeName, Class<T> attributeClass) {
            return (T) attributes.get(attributeName);
        }
    }
}
