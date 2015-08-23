package nl.pwiddershoven.scriptor.service.script;

import java.util.Set;

import jdk.nashorn.api.scripting.ClassFilter;

import com.google.common.collect.Sets;

public class NashornClassFilter implements ClassFilter {
    private static final Set<String> CLASS_WHITELIST = Sets.newHashSet(
            "java.util.Date",
            "java.lang.String",
            "java.util.Locale",
            "java.util.stream.Collectors",
            "java.text.SimpleDateFormat");

    @Override
    public boolean exposeToScripts(String clazz) {
        return CLASS_WHITELIST.contains(clazz);
    }
}
