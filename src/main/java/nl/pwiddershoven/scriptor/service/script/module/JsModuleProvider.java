package nl.pwiddershoven.scriptor.service.script.module;

import nl.pwiddershoven.scriptor.service.script.JsContext;

public interface JsModuleProvider {
    String name();

    JsModule module(JsContext jsContext);
}
