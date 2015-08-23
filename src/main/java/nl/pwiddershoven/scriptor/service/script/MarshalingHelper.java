package nl.pwiddershoven.scriptor.service.script;

import java.util.*;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class MarshalingHelper {

    public static Object unwrap(ScriptObjectMirror jso) {
        if (jso.isArray()) {
            List<Object> l = new ArrayList<>();
            for (Object o : jso.values()) {
                if (o instanceof ScriptObjectMirror) {
                    l.add(unwrap((ScriptObjectMirror) o));
                } else {
                    l.add(o);
                }
            }
            return l;
        } else {
            Map<String, Object> result = new HashMap<>();
            for (String k : jso.keySet()) {
                Object o = jso.get(k);
                if (o instanceof ScriptObjectMirror) {
                    result.put(k, unwrap((ScriptObjectMirror) o));
                } else {
                    result.put(k, o);
                }
            }
            return result;
        }
    }
}
