package nl.pwiddershoven.script.service.script.module.date;

import java.text.SimpleDateFormat;
import java.util.Locale;

import nl.pwiddershoven.script.service.script.module.JsModule;

import org.springframework.stereotype.Component;

@Component
public class DateModule implements JsModule {
    @Override
    public String name() {
        return "date";
    }

    public SimpleDateFormat parser(String format, String localeString) {
        Locale locale = Locale.forLanguageTag(localeString);
        return new SimpleDateFormat(format, locale);
    }
}
