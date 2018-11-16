package com.peactor.moocmall.common.enums;

/**
 * @program: moocmall
 * @description: code型基础枚举类
 * @author: botao
 * @create: 2018-11-16 15:34
 **/
public enum CommonEnum {
    //有效
    ON(1, "ON"),
    //失效
    OFF(0, "关");

    private final int code;
    private final String desc;

    CommonEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
