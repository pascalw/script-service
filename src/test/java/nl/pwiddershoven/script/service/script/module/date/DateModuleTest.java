package nl.pwiddershoven.script.service.script.module.date;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.junit.Test;

public class DateModuleTest {
    private DateModule dateModule = new DateModule();

    @Test
    public void getParser_builds_SimpleDateFormat_with_provided_pattern_and_locale() {
        SimpleDateFormat df = dateModule.parser("dd MMMM yyyy", "en_US");

        assertEquals(new SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("en_US")), df);
    }

}