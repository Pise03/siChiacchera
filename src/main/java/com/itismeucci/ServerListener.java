package com.itismeucci;
import java.util.*;

public class ServerListener{
    // creato vettore dove mi salvo tutti i socket dei client
    HashMap<String, ServerThread> handler = new HashMap<String, ServerThread>();

    // costruttore
    public ServerListener() {
    }

    public void aggiungiSocket(String nomeUtente, ServerThread thread) throws Exception {
        handler.put(nomeUtente, thread);

        for (String i : handler.keySet()) {
            if (i != nomeUtente) {
                handler.get(i).messaggia(nomeUtente + " e' entrato nella chat.");
            }
          }
    }

    public void sendAll(String messaggio, String mittente) throws Exception {
        for (String i : handler.keySet()) {
            if (i != mittente) {
                handler.get(i).messaggia(mittente + " ha scritto:" + messaggio);
            }
          }
    }

    public void sendOne(String messaggio, String mittente, String destinatario) throws Exception {
        for (String x : handler.keySet()) {
            if (x.equals(destinatario)) {
                handler.get(x).messaggia(mittente + " ha scritto (in privato):" + messaggio);
            }
        }
    }

    public void remove(String nome) throws Exception{
        handler.remove(nome);

        for (ServerThread thread : handler.values()) {
            thread.messaggia(nome + " e' uscito dalla chat.");
        }
    }

    public boolean verify(String nome, ServerThread thread) throws Exception{
            if (handler.containsKey(nome)) {
                thread.messaggia("Errore: il nome e' gia stato inserito. Sceglierne un altro.");
                return false;
            }
        
        return true;
    }



}
