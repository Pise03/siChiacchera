package com.itismeucci;
import java.io.*;
import java.net.*;
import java.util.*;

public class ServerListener{
    // creato vettore dove mi salvo tutti i socket dei client
    HashMap<String, Socket> handler = new HashMap<String, Socket>();
    // String nomeUtente = null;
    ServerSocket server = null;
    Socket client = null;
    String stringaRicevuta = null;
    String stringaModificata = null;
    BufferedReader inDalClient;
    DataOutputStream outVersoClient;

    // costruttore
    public ServerListener() {
    }

    public void aggiungiSocket(String nomeUtente, Socket Client) throws Exception {
        handler.put(nomeUtente, Client);
        for (String i : handler.keySet()) {
            if (i != nomeUtente) {
                outVersoClient = new DataOutputStream(handler.get(i).getOutputStream());
                outVersoClient.writeBytes(nomeUtente + " è entrato nella chat." + '\n');
            }
          }
    }

    public void sendAll(String messaggio, String mittente) throws Exception {
        for (String i : handler.keySet()) {
            if (i != mittente) {
                outVersoClient = new DataOutputStream(handler.get(i).getOutputStream());
                outVersoClient.writeBytes(mittente + " ha scritto:" + messaggio + '\n');
            }
          }
    }

    public void sendOne(String messaggio, String mittente, String destinatario) throws Exception {
        for (String x : handler.keySet()) {
            if (x.equals(destinatario)) {
                outVersoClient = new DataOutputStream(handler.get(x).getOutputStream());
                outVersoClient.writeBytes(mittente + " ha scritto (in privato):" + messaggio + '\n');
            }
        }
    }

    public void remove(String nome) throws Exception{
        handler.remove(nome);

        for (Socket socket : handler.values()) {
            outVersoClient = new DataOutputStream(socket.getOutputStream());
            outVersoClient.writeBytes(nome + " è uscito dalla chat." + '\n');
        }
    }

    public boolean verify(String nome, Socket client1) throws Exception{
            if (handler.containsKey(nome)) {
                outVersoClient = new DataOutputStream(client1.getOutputStream());
                outVersoClient.writeBytes("Errore: il nome è gia stato inserito. Sceglierne un altro." + '\n');
                return false;
            }
        
        return true;
    }

}
