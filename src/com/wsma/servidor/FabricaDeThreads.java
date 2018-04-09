package com.wsma.servidor;

import java.util.concurrent.ThreadFactory;

public class FabricaDeThreads implements ThreadFactory {

    private static int numero;

    @Override
    public Thread newThread(Runnable tarefa) {
        Thread thread = new Thread(tarefa, "Thread Servidor Tarefas " + numero);
        numero++;
        thread.setUncaughtExceptionHandler(new TratadorDeExcecao());
        thread.setDaemon(true); // thread de serviço
        return thread;
    }
}
