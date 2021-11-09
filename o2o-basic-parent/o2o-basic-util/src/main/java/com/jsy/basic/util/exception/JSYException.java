package com.jsy.basic.util.exception;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
public class JSYException extends RuntimeException implements Serializable {
	private Integer code;
    private String message;
	
	public JSYException(Integer code, String message) {
		super(message);
		this.code = code;
        this.message = message;
	}

    public JSYException() {
        this(JSYError.INTERNAL);
    }

    public JSYException(JSYError error) {
		super(error.getMessage());
		this.code = error.getCode();
		this.message=error.getMessage();
	}

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }


    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    @Override
    public String toString() {
        return "JSYException(code=" + this.getCode() + ",      message"+this.message+")";
    }
}
