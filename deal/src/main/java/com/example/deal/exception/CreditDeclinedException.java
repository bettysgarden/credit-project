package com.example.deal.exception;

import java.util.List;

public class CreditDeclinedException extends BaseCustomException {
    public static final String CREDIT_DECLINED_ERROR = "CREDIT_DECLINED_ERROR";

    public CreditDeclinedException(String message, List<String> errors) {
        super(CREDIT_DECLINED_ERROR, message, errors);
    }
}

