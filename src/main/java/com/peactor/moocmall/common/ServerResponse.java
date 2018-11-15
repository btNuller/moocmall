package com.peactor.moocmall.common;

import java.io.Serializable;

public class ServerResponse<T> implements Serializable {

    private int status;

    private String msg;

    private T data;


}
