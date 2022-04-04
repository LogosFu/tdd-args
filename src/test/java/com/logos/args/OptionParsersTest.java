package com.logos.args;

import com.logos.args.exception.NoParamsException;
import com.logos.args.exception.TooMayParamsException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptionParsersTest {

    @Nested
    class UnaryParserTest {
        //DONE happy path
        @Test
        void should_return_object_use_when_parse_given_parser_func() {
            Object defaultValue = new Object();
            Object parsed = new Object();
            Function<String, ? super Object> parser = s -> parsed;
            Object result = OptionParsers.unary(defaultValue, parser).parser(asList("-d", "test"), "d");
            assertThat(result).isSameAs(parsed);
        }

        //DONE default value
        @Test
        void should_return_default_object_use_when_parse_given_parser_func() {
            Object defaultValue = new Object();
            Object parsed = new Object();
            Function<String, ? super Object> parser = s -> parsed;
            Object result = OptionParsers.unary(defaultValue, parser).parser(emptyList(), "d");
            assertThat(result).isSameAs(defaultValue);
        }

        //DONE too many params
        @Test
        void should_thrown_too_many_params_exception_when_parser_given_two_params() {
            Object defaultValue = new Object();
            Object parsed = new Object();
            Function<String, ? super Object> parser = s -> parsed;
            assertThatThrownBy(() -> OptionParsers.unary(defaultValue, parser).parser(asList("-d", "any", "test"), "d"))
                    .isInstanceOf(TooMayParamsException.class).message().isEqualTo("error type: d");
        }

        //DONE no params
        @Test
        void should_thrown_no_params_exception_when_parser_given_no_params() {
            Object defaultValue = new Object();
            Object parsed = new Object();
            Function<String, ? super Object> parser = s -> parsed;
            assertThatThrownBy(() -> OptionParsers.unary(defaultValue, parser).parser(List.of("-d"), "d"))
                    .isInstanceOf(NoParamsException.class).message().isEqualTo("error type: d");
        }
    }

    @Nested
    class BoolParserTest {

        // DONE bool -l a
        @Test
        void should_throw_params_too_many_exception_when_given_boolean_with_params() {
            assertThatThrownBy(() -> OptionParsers.bool().parser(asList("-l", "a"), "l")).isInstanceOf(TooMayParamsException.class);
        }

        // TODO -bool : false
        @Test
        void should_return_false_when_parser_bool_given_no_logging_flg() {

            assertFalse(OptionParsers.bool().parser(List.of(), "l"));
        }

        // TODO -bool : true
        @Test
        void should_return_true_when_parser_bool_given_logging_flg() {
            assertTrue(OptionParsers.bool().parser(asList("-l"), "l"));
        }
    }

    @Nested
    class ListParserTest {

        //DONE -g this is a list
        @Test
        void should_parser_array_when_parser_list_given_g_flg_and_some_list_arguments() {
            String[] parsed = OptionParsers.list(new String[]{}, String[]::new, String::valueOf).parser(asList("-g", "this", "is", "a", "list"), "g");
            assertThat(parsed).isEqualTo(new String[]{"this", "is", "a", "list"});
        }

        //DONE default values []
        @Test
        void should_given_default_value_given_g_need_parser_and_empty_list() {
            String[] parsed = OptionParsers.list(new String[]{}, String[]::new, String::valueOf).parser(emptyList(), "g");
            assertThat(parsed).isEqualTo(new String[]{});
        }

        @Test
        void should_parser_negative_number_when_parser_given_negative_arguments() {
            Integer[] parsed = OptionParsers.list(new Integer[]{}, Integer[]::new, Integer::valueOf).parser(asList("-g", "-1"), "g");
            assertThat(parsed).isEqualTo(new Integer[]{-1});
        }

    }
}