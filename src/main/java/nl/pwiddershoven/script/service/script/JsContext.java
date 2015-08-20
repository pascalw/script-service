package nl.pwiddershoven.script.service.script;

import nl.pwiddershoven.script.service.script.module.JsModule;

public interface JsContext {
    JsModule require(String moduleName);

    <T> T getAttribute(String attributeName, Class<T> attributeClass);
}
