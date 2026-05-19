package ru.artem.NauJava.util;

import java.util.HashMap;
import java.util.Map;

public final class PatchMapUtils {

    private PatchMapUtils() {
    }

    public static Map<String, Object> normalizeKeys(Map<String, Object> updates) {
        Map<String, Object> result = new HashMap<>();
        updates.forEach((key, value) -> result.put(toCamelCase(key), value));
        return result;
    }

    public static String toCamelCase(String key) {
        if (key == null || !key.contains("_")) {
            return key;
        }
        StringBuilder sb = new StringBuilder();
        boolean upperNext = false;
        for (char c : key.toCharArray()) {
            if (c == '_') {
                upperNext = true;
            } else {
                sb.append(upperNext ? Character.toUpperCase(c) : c);
                upperNext = false;
            }
        }
        return sb.toString();
    }

    public static String getString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    public static Integer getInteger(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value instanceof String string) {
            return Integer.parseInt(string);
        }
        return null;
    }

    public static Long getLong(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value instanceof String string) {
            return Long.parseLong(string);
        }
        return null;
    }

    public static boolean getBoolean(Map<String, Object> map, String key, boolean defaultValue) {
        Object value = map.get(key);
        if (value instanceof Boolean bool) {
            return bool;
        }
        if (value instanceof String string) {
            return Boolean.parseBoolean(string);
        }
        return defaultValue;
    }
}
