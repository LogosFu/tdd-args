package com.logos.args;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ArgsTest {

    // TODO multi -l -p -8080 -d /usr/logs
    @Test
    void should_parse_args() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Options options = Args.parse(Options.class, "-l", "-p", "8080", "-d", "/usr/logos");
        assertThat(options.logging).isTrue();
        assertThat(options.port).isEqualTo(8080);
        assertThat(options.path).isEqualTo("/usr/logos");
    }

    //-g this is a list -d 1 2 -3 5
    @Test
    void should_parse_list_args() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        ListOptions listOptions = Args.parse(ListOptions.class, "-g", "this", "is", "a", "list", "-d", "1", "2", "-3", "5");
        assertThat(listOptions.group).isEqualTo(new String[]{"this", "is", "a", "list"});
        assertThat(listOptions.decimals).isEqualTo(new Integer[]{1, 2, -3, 5});
    }


    static record Options(@Option("l") boolean logging, @Option("p") int port, @Option("d") String path) {
    }

    static record ListOptions(@Option("g") String[] group, @Option("d") Integer[] decimals) {
    }
}