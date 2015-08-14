package nl.pwiddershoven.script.service.script;

import java.util.*;

import javax.script.*;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import nl.pwiddershoven.script.service.ScriptConfiguration;
import nl.pwiddershoven.script.service.script.module.JsModule;
import nl.pwiddershoven.script.service.script.module.feed.FeedBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScriptExecutor {
    private static final String SCRIPT_WRAPPER = "(function() { %s; })()";

    private ScriptEngine jsEngine;
    private Map<String, JsModule> jsModules = new HashMap<>();

    public ScriptExecutor() {
        NashornScriptEngineFactory scriptEngineFactory = new NashornScriptEngineFactory();
        jsEngine = scriptEngineFactory.getScriptEngine(new NashornClassFilter());

        Bindings bindings = jsEngine.getBindings(ScriptContext.ENGINE_SCOPE);
        bindings.put("__ctx", new JsContext());

        try {
            jsEngine.eval("require = function(moduleName) { return __ctx.require(moduleName); };");
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public Object execute(ScriptConfiguration scriptConfiguration) {
        try {
            ScriptContext ctx = new SimpleScriptContext();
            ctx.setBindings(jsEngine.getBindings(ScriptContext.ENGINE_SCOPE), ScriptContext.ENGINE_SCOPE);

            Object result = jsEngine.eval(String.format(SCRIPT_WRAPPER, scriptConfiguration.processingScript), ctx);

            if (result instanceof ScriptObjectMirror)
                result = MarshalingHelper.unwrap((ScriptObjectMirror) result);

            if (result instanceof FeedBuilder)
                result = ((FeedBuilder) result).build();

            return result;
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public class JsContext {
        public Object require(String moduleName) {
            JsModule module = jsModules.get(moduleName);
            if (module == null)
                return new RuntimeException("Module not found");

            return module;
        }
    }

    @Autowired
    public void setJsContexts(Set<JsModule> jsModules) {
        for (JsModule module : jsModules) {
            this.jsModules.put(module.name(), module);
        }
    }
}
