package com.gildedrose.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gildedrose.exception.InvalidItemException;
import com.gildedrose.exception.InvalidApiKeyException;
import com.gildedrose.exception.ItemOutOfStockException;

@RestControllerAdvice
public class ControllerExceptionAdvice {
    @ExceptionHandler(InvalidApiKeyException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleInvalidApiKeyException(InvalidApiKeyException e) {
    }

    @ExceptionHandler(InvalidItemException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleInvalidItemException(InvalidItemException e) {
    }

    @ExceptionHandler(ItemOutOfStockException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleItemOutOfStockException(ItemOutOfStockException e) {
    }
}
