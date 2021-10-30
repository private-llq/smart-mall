package com.jsy.basic.util.exception;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
public class JSYException extends RuntimeException implements Serializable {
	private Integer code;
	
	public JSYException(Integer code, String message) {
		super(message);
		this.code = code;
	}

    public JSYException() {
        this(JSYError.INTERNAL);
    }

    public JSYException(JSYError error) {
		super(error.getMessage());
		this.code = error.getCode();
	}

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
    @Override
    public String toString() {
        return "JSYException(code=" + this.getCode() + ")";
    }
}
