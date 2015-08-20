package nl.pwiddershoven.script.service.script.module.date;

import java.text.SimpleDateFormat;
import java.util.Locale;

import nl.pwiddershoven.script.service.script.ScriptExecutor;
import nl.pwiddershoven.script.service.script.module.JsModuleProvider;

import org.springframework.stereotype.Component;

@Component
public class DateModuleProvider implements JsModuleProvider {
    public static class DateModule implements JsModule {
        public SimpleDateFormat parser(String format, String localeString) {
            Locale locale = Locale.forLanguageTag(localeString);
            return new SimpleDateFormat(format, locale);
        }
    }

    private DateModule module = new DateModule();

    @Override
    public String name() {
        return "date";
    }

    @Override
    public DateModule module(ScriptExecutor.JsContext jsContext) {
        return module;
    }
}
