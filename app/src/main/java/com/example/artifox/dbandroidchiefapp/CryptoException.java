package com.example.artifox.dbandroidchiefapp;

/**
 * Created by artifox on 4/24/2015.
 */
public class CryptoException extends Exception {

    public CryptoException() {
    }

    public CryptoException(String message, Throwable throwable) {
        super(message, throwable);
        throwable.printStackTrace();
    }

}
