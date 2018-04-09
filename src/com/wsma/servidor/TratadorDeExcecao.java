package com.wsma.servidor;

import java.lang.Thread.UncaughtExceptionHandler;

public class TratadorDeExcecao implements UncaughtExceptionHandler {


    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("Exceção na Thread " + t.getName() + ", " + e.getMessage());
    }
}
