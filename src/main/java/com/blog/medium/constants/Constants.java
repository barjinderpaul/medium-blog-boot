package com.blog.medium.constants;

public enum Constants {
    PREFIX_FOR_VIEWS("/WEB-INF/"),
    VIEWS_EXTENSION(".jsp");

    public final String value;

    Constants(String value){
        this.value = value;
    }
}
