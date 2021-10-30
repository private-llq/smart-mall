package com.jsy.basic.util.exception;

import com.jsy.basic.util.constant.ConstError;

public class PaymentException extends JSYException {
	public PaymentException(Integer code, String message) {
		super(code, message);
	}

	public PaymentException() {
		super();
	}

	public PaymentException(String message) {
		super(ConstError.NORMAL, message);
	}

	public PaymentException(JSYError error) {
		super(error);
	}
}