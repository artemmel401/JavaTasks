package ru.artem.NauJava.util;

public final class QueryParamUtils {

    private QueryParamUtils() {
    }

    public static Long resolveId(Long snakeCaseValue, Long camelCaseValue) {
        return snakeCaseValue != null ? snakeCaseValue : camelCaseValue;
    }
}
