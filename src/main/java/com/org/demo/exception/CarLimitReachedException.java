package com.org.demo.exception;

public class CarLimitReachedException extends RuntimeException {

    public CarLimitReachedException(String message) {
        super(message);
    }
}
