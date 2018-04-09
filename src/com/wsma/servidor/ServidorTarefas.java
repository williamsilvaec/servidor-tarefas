package com.wsma.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServidorTarefas {

    private ExecutorService threadPool;
    private ServerSocket servidor;
    private AtomicBoolean estaRodando;

    public ServidorTarefas() throws IOException {
        System.out.println("--- Iniciando servidor ---");
        this.servidor = new ServerSocket(12344);
        this.threadPool = Executors.newCachedThreadPool();
        this.estaRodando = new AtomicBoolean(true);
    }

    public void rodar() throws IOException {

        while (estaRodando.get()) {
            Socket socket = servidor.accept();
            System.out.println("Aceitando novo cliente na porta " + socket.getPort());

            DistribuirTarefas distribuirTarefas = new DistribuirTarefas(socket, this);
            threadPool.execute(distribuirTarefas);
        }
    }

    public void parar() throws IOException {
        estaRodando.set(false);
        servidor.close();
        threadPool.shutdown();
    }

    public static void main(String[] args) throws IOException {
        ServidorTarefas servidor = new ServidorTarefas();
        servidor.rodar();
        servidor.parar();
    }
}
