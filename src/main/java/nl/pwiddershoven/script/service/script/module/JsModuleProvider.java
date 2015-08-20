package nl.pwiddershoven.script.service.script.module;

import nl.pwiddershoven.script.service.script.JsContext;

public interface JsModuleProvider {
    String name();

    JsModule module(JsContext jsContext);
}
