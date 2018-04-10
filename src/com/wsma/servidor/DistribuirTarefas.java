package com.wsma.servidor;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class DistribuirTarefas implements Runnable {

    private ExecutorService threadPool;
    private Socket socket;
    private ServidorTarefas servidor;

    public DistribuirTarefas(ExecutorService threadPool, Socket socket, ServidorTarefas servidor) {
        this.threadPool = threadPool;
        this.socket = socket;
        this.servidor = servidor;
    }

    @Override
    public void run() {

        System.out.println("Distribuindo as tarefas para o cliente" + socket);

        try {
            Scanner entradaCliente  = new Scanner(socket.getInputStream());
            PrintStream saidaCliente = new PrintStream(socket.getOutputStream());

            while (entradaCliente.hasNextLine()) {
                String comando = entradaCliente.nextLine();
                System.out.println("Comando recebido " + comando);

                switch (comando) {
                    case "c1" : {
                        saidaCliente.println("Confirmação do comando c1");
                        ComandoC1 c1 = new ComandoC1(saidaCliente);
                        this.threadPool.execute(c1);
                        break;
                    }
                    case "c2" : {
                        saidaCliente.println("Confirmação do comando c2");
                        ComandoC2ChamaWS c2WS = new ComandoC2ChamaWS(saidaCliente);
                        ComandoC2AcessaBanco c2Banco = new ComandoC2AcessaBanco(saidaCliente);
                        Future<String> futureWS = this.threadPool.submit(c2WS);
                        Future<String> futureBanco = this.threadPool.submit(c2Banco);

                        JuntaResultadosFutureWSFutureBanco juntaResultados =
                                new JuntaResultadosFutureWSFutureBanco(futureWS, futureBanco, saidaCliente);

                        this.threadPool.submit(juntaResultados);

                        break;
                    }
                    case "fim" : {
                        saidaCliente.println("Desligando o servidor");
                        servidor.parar();
                        break;
                    }
                    default: {
                        saidaCliente.println("Comando não encontrado");
                    }
                }

                System.out.println(comando);
            }

            saidaCliente.close();
            entradaCliente.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
