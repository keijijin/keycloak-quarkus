package com.sample.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Article {
    private Long id;
    private String title;
    private String content;
    private boolean isPremium;
}
