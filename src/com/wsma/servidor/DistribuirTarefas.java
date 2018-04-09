package com.wsma.servidor;

import java.net.Socket;
import java.util.Scanner;

public class DistribuirTarefas implements Runnable {

    private Socket socket;

    public DistribuirTarefas(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        System.out.println("Distribuindo tarefas " + socket);

        try {
            Scanner entradaCliente  = new Scanner(socket.getInputStream());

            while (entradaCliente.hasNextLine()) {
                String comando = entradaCliente.nextLine();
                System.out.println(comando);
            }

            Thread.sleep(20000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
