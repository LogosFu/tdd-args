package com.logos.args;

import com.logos.args.exception.NoParamsException;
import com.logos.args.exception.TooMayParamsException;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

class OptionParsers {

    public static OptionParser<Boolean> bool() {
        return (argumentList, flg) -> values(argumentList, flg, 0).isPresent();
    }

    public static <T> OptionParser<T> unary(T defaultValue, Function<String, T> parserFunc) {
        return (argumentList, flg) -> values(argumentList, flg, 1).map(it -> parserFunc.apply(it.get(0))).orElse(defaultValue);
    }

    public static <T> OptionParser<T[]> list(T[] defaultValue, IntFunction<T[]> arrayInit, Function<String, T> parserFunc) {
        return (argumentList, flg) -> values(argumentList, flg).map(list ->
                list.stream().map(parserFunc).toArray(arrayInit)).orElse(defaultValue);
    }

    private static Optional<List<String>> values(List<String> argumentList, String flg) {
        int indexOfFlg = argumentList.indexOf("-" + flg);
        return indexOfFlg == -1 ? Optional.empty() : Optional.of(values(argumentList, indexOfFlg));
    }

    private static Optional<List<String>> values(List<String> argumentList, String flg, int argsSize) {
        int indexOfFlg = argumentList.indexOf("-" + flg);
        if (indexOfFlg == -1) {
            return Optional.empty();
        } else {
            List<String> values = values(argumentList, indexOfFlg);
            if (values.size() > argsSize) throw new TooMayParamsException(flg);
            if (values.size() < argsSize) throw new NoParamsException(flg);
            return Optional.of(values);
        }
    }

    private static List<String> values(List<String> argumentList, int indexOfFlg) {
        int nextIndex = IntStream.range(indexOfFlg + 1, argumentList.size())
                .filter(i -> argumentList.get(i).matches("^-[a-zA-Z-]+$")).findFirst().orElse(argumentList.size());

        return argumentList.subList(indexOfFlg + 1, nextIndex);
    }
}
