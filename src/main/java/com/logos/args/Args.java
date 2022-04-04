package com.logos.args;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class Args {

    static Map<Class<?>, OptionParser> PARSER_MAP = new HashMap<>();

    static {
        PARSER_MAP.put(boolean.class, OptionParsers.bool());
        PARSER_MAP.put(int.class, OptionParsers.unary(0, Integer::valueOf));
        PARSER_MAP.put(String.class, OptionParsers.unary("", String::valueOf));
        PARSER_MAP.put(String[].class, OptionParsers.list(new String[]{}, String[]::new, String::valueOf));
        PARSER_MAP.put(Integer[].class, OptionParsers.list(new Integer[]{}, Integer[]::new, Integer::valueOf));
    }

    public static <T> T parse(Class<T> optionsClass, String... args) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        List<String> argumentList = asList(args);
        Constructor<?> constructor = optionsClass.getDeclaredConstructors()[0];
        Object[] results = Arrays.stream(constructor.getParameters()).map(parameter -> parseArgs(argumentList, parameter)).toArray();
        return (T) constructor.newInstance(results);
    }

    private static Object parseArgs(List<String> argumentList, Parameter parameter) {
        return getParser(parameter).parser(argumentList, parameter.getAnnotation(Option.class).value());
    }

    private static OptionParser getParser(Parameter parameter) {
        return PARSER_MAP.get(parameter.getType());
    }
}
