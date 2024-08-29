package com.example.demo.cache.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.function.Supplier;

@Builder
@Data
public class HotLoadingObject {

    private boolean isEnabled;
    private Supplier<Map<String, Object>> valueExtractor;

}

