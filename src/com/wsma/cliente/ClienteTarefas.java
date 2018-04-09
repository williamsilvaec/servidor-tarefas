package com.wsma.cliente;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClienteTarefas {

    public static void main(String[] args) throws Exception {

        Socket socket = new Socket("localhost", 12344);

        System.out.println("Conexão estabelecida");

        Thread threadEnviaComandos = new Thread(() -> {
            try {
                System.out.println("Pode enviar comando!");

                // saida baseado no OutputStream
                PrintStream saida = new PrintStream(socket.getOutputStream());

                //estabelecendo a leitura do teclado
                Scanner teclado = new Scanner(System.in);

                //aguardando "enter" do teclado
                while (teclado.hasNextLine()) {

                    //pegando o que foi digitado no console
                    String linha = teclado.nextLine();

                    //se for vazio, não vamos enviar nada para o servidor
                    if (linha.trim().equals("")) {
                        break;
                    }

                    //enviando para o servidor
                    saida.println(linha);
                }

                saida.close();
                teclado.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Thread threadRecebeResposta = new Thread(() -> {
            try {
                System.out.println("Recebendo dados do servidor");

                //leitura dos dados que vem do servidor
                Scanner respostaServidor = new Scanner(socket.getInputStream());

                //imprimindo resposta do servidor
                while (respostaServidor.hasNextLine()) {
                    String linha = respostaServidor.nextLine();
                    System.out.println(linha);
                }

                respostaServidor.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        threadRecebeResposta.start();
        threadEnviaComandos.start();

        threadEnviaComandos.join();

        socket.close();
    }
}
