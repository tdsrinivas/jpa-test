package com.srini.controller;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchCriteria {
    public enum Operation {EQUAL, IN};

    private final String key;
    private final Operation operation;
    private final Object value;
}
