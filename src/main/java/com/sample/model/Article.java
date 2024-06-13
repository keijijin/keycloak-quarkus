package com.sample.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Article {
    private Long id;
    private String title;
    private String content;
    private boolean isPremium;

}
