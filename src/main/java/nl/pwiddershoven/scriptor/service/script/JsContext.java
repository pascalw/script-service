package nl.pwiddershoven.scriptor.service.script;

import nl.pwiddershoven.scriptor.service.script.module.JsModule;

public interface JsContext {
    JsModule require(String moduleName);

    <T> T getAttribute(String attributeName, Class<T> attributeClass);
}
