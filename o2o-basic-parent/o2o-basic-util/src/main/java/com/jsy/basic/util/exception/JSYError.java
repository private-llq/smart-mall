package com.jsy.basic.util.exception;

import com.jsy.basic.util.constant.ConstError;


public enum JSYError {

	UNKNOWN_EXCEPTION(102, "未知异常"),
	PARAMETER_EXCEPTION(103, "参数异常"),
	BAD_REQUEST(ConstError.BAD_REQUEST, "错误的请求"),
	UNAUTHORIZED(ConstError.UNAUTHORIZED, "未认证"),
	FORBIDDEN(ConstError.FORBIDDEN, "禁止访问"),
	NOT_FOUND(ConstError.NOT_FOUND, "页面丢失了"),
	NOT_SUPPORT_REQUEST_METHOD(ConstError.NOT_SUPPORT_REQUEST_METHOD, "不支持的请求类型"),
	REQUEST_PARAM(ConstError.REQUEST_PARAM, "请求参数错误"),
	INTERNAL(ConstError.INTERNAL, "服务器错误"),
	NOT_IMPLEMENTED(ConstError.NOT_IMPLEMENTED, "未实现"),
	GATEWAY(ConstError.GATEWAY, "网关错误"),
	DUPLICATE_KEY(ConstError.DUPLICATE_KEY, "数据库错误"),
	FUSE_DOWNGRADE(ConstError.NOT_SUPPORT_REQUEST_METHOD,"熔断降级");




	private final Integer code;
	private final String message;
	
	public Integer getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
	
	JSYError(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
}
