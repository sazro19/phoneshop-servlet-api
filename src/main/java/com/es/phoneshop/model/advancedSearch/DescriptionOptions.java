package com.es.phoneshop.model.advancedSearch;

import com.es.phoneshop.model.order.PaymentMethod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum DescriptionOptions {
    ALL_WORDS("all words"),
    ANY_WORD("any word");

    private final String value;

    DescriptionOptions(String s) {
        value = s;
    }

    public String getValue() {
        return value;
    }

    public static DescriptionOptions getEnum(String value) {
        for (DescriptionOptions v : values()) {
            if (v.getValue().equalsIgnoreCase(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }

    public static List<String> getStringOptions() {
        return Arrays.stream(DescriptionOptions.values())
                .map(DescriptionOptions::getValue)
                .collect(Collectors.toList());
    }
}
