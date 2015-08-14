package nl.pwiddershoven.script.service.script;

import java.util.Set;

import javax.script.*;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import nl.pwiddershoven.script.service.ScriptConfiguration;
import nl.pwiddershoven.script.service.script.context.JsContext;
import nl.pwiddershoven.script.service.script.context.feed.FeedBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScriptExecutor {
    private static final String SCRIPT_WRAPPER = "(function() { %s; })()";

    private ScriptEngine jsEngine;

    public ScriptExecutor() {
        NashornScriptEngineFactory scriptEngineFactory = new NashornScriptEngineFactory();
        jsEngine = scriptEngineFactory.getScriptEngine(new NashornClassFilter());
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

    @Autowired
    public void setJsContexts(Set<JsContext> jsContexts) {
        Bindings bindings = jsEngine.getBindings(ScriptContext.ENGINE_SCOPE);

        for (JsContext ctx : jsContexts) {
            bindings.put(ctx.moduleName(), ctx);
        }
    }
}
