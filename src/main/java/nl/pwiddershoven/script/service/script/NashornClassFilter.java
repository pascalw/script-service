package nl.pwiddershoven.script.service.script;

import java.util.Set;

import jdk.nashorn.api.scripting.ClassFilter;

import com.google.common.collect.Sets;

public class NashornClassFilter implements ClassFilter {
    private static final Set<String> CLASS_WHITELIST = Sets.newHashSet(
            "java.util.Date",
            "java.lang.Object",
            "java.lang.String",
            "java.util.ArrayList",
            "java.util.HashMap",
            "java.util.Locale",
            "java.util.stream.Collectors",
            "java.text.SimpleDateFormat",
            "org.jsoup.nodes.Document",
            "org.jsoup.select.Elements");

    @Override
    public boolean exposeToScripts(String clazz) {
        return CLASS_WHITELIST.contains(clazz);
    }
}
