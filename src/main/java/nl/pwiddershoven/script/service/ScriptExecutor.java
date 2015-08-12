package nl.pwiddershoven.script.service;

import javax.script.*;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScriptExecutor {
    private static final String SCRIPT_WRAPPER = "(function() { %s; })()";

    private ScriptEngine jsEngine;
    private PageFetcher pageFetcher;

    public ScriptExecutor() {
        NashornScriptEngineFactory scriptEngineFactory = new NashornScriptEngineFactory();
        jsEngine = scriptEngineFactory.getScriptEngine(new NashornClassFilter());

        try {
            Bindings bindings = jsEngine.getBindings(ScriptContext.ENGINE_SCOPE);
            bindings.put("__ctx", new JsContext());

            jsEngine.eval("newFeed = function() { return __ctx.newFeed(); };");
            jsEngine.eval("fetchDocument = function(url) { return __ctx.fetchDocument(url); };");
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
        public FeedBuilder newFeed() {
            return new FeedBuilder();
        }

        public Document fetchDocument(String url) {
            String pageSource = pageFetcher.fetch(url);
            return Jsoup.parse(pageSource, url);
        }
    }

    @Autowired
    public void setPageFetcher(PageFetcher pageFetcher) {
        this.pageFetcher = pageFetcher;
    }
}
