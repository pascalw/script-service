package nl.pwiddershoven.script.service.script.module;

public interface JsModuleProvider {
    String name();

    JsModule module();

    interface JsModule {}
}
