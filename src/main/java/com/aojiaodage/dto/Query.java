package com.aojiaodage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Query {
    private long current = 1;
    private long size = 10;
    private String keyword;
}
