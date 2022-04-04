package com.logos.args;

import java.util.List;

interface OptionParser<T> {
    T parser(List<String> argumentList, String flg);
}
