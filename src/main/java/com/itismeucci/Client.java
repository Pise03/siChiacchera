package com.itismeucci;

import java.io.*;
import java.net.*;

public class Client {
    String nomeServer = "localhost";
    int portaServer = 6789;
    Socket miosocket;
    BufferedReader tastiera;
    String stringaUtente;
    DataOutputStream outVersoServer;
    ClientListener listener;

    int conta = 0;

    public Socket connetti() throws Exception{

        System.out.println("Ingresso nella chat");
        try {

            tastiera = new BufferedReader(new InputStreamReader(System.in));

            miosocket = new Socket(nomeServer, portaServer);

            outVersoServer = new DataOutputStream(miosocket.getOutputStream());
            
            listener = new ClientListener(miosocket);

        } catch (UnknownHostException e) {
            System.err.println("Host sconosciuto");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante la connessione");
            System.exit(1);
        }
        return miosocket;
    }

    public void comunica() throws Exception{
        listener.start();
        for (;;) {
            try {
                if (conta == 0) {
                    System.out.print("Inserisci nome utente: ");
                    stringaUtente = tastiera.readLine();
                    outVersoServer.writeBytes(stringaUtente + '\n');
                    conta++;
                } else {
                    
                    

                    System.out.print("Messaggio: ");
                    stringaUtente = tastiera.readLine();

                    // la spedisco al server
                    System.out.println("Invio messaggio...");
                    outVersoServer.writeBytes(stringaUtente + '\n');

                }

            } catch (Exception e) {
                System.out.println("e.getMessage()");
                System.out.println("Errore durante la comunicazione con il server!");
                System.exit(1);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.connetti();
        client.comunica();
    }
}