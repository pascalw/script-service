package nl.pwiddershoven.script.service.script.module;

import nl.pwiddershoven.script.service.script.ScriptExecutor;

public interface JsModuleProvider {
    String name();

    JsModule module(ScriptExecutor.JsContext jsContext);

    interface JsModule {}
}
