package com.imooc.enums;

public enum CategoryLevel {
    LEVEL1(1, "一级分类"),
    LEVEL2(2, "二级分类"),
    LEVEL3(3, "三级分类");

    public final Integer type;
    public final String value;

    CategoryLevel(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
