package com.ltx.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author tianxing
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SexCount(Object field, Integer count) {
}
