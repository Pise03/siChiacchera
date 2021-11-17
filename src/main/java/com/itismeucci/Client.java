package com.itismeucci;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.*;
import javax.swing.*;

public class Client implements ActionListener {
    String nomeServer = "localhost";
    int portaServer = 6789;
    Socket miosocket;
    BufferedReader tastiera;
    String stringaUtente;
    String stringaRicevutaDalServer;
    DataOutputStream outVersoServer;

    int conta = 0;

    // componenti GUI
    JFrame frame;
    JPanel panel;
    JLabel label;
    JTextField textField;
    JButton button;

    public Client() {
        inserimentoNomeUtente();
    }

    public void inserimentoNomeUtente() {
        frame = new JFrame();

        label = new JLabel("Inserire nome utente");
        label.setBounds(240, 180, 400, 30);
        label.setFont(new Font("Itim", Font.BOLD, 18));

        textField = new JTextField();
        textField.setBounds(220, 240, 180, 30);
        textField.setFont(new Font("Itim", Font.BOLD, 14));

        button = new JButton("Inserisci");
        button.setBounds(240, 280, 140, 30);
        button.setFont(new Font("Itim", Font.BOLD, 14));
        button.addActionListener(this);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(720, 480));
        panel.add(label);
        panel.add(textField);
        panel.add(button);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("siChiacchera");
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public Socket connetti() {
        System.out.println("Ingresso nella chat");
        try {

            tastiera = new BufferedReader(new InputStreamReader(System.in));

            miosocket = new Socket(nomeServer, portaServer);

            outVersoServer = new DataOutputStream(miosocket.getOutputStream());

        } catch (UnknownHostException e) {
            System.err.println("Host sconosciuto");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante la connessione");
            System.exit(1);
        }
        return miosocket;
    }

    public void comunica() {
        for (;;) {
            try {
                if (conta == 0) {
                    System.out.print("Inserisci nome utente: ");
                    stringaUtente = tastiera.readLine();
                    outVersoServer.writeBytes(stringaUtente + '\n');
                    conta++;
                } else {
                    ClientListener listener = new ClientListener(miosocket);
                    listener.start();

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

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(button)) {
            String nomeUtente = textField.getText();
            try {
                outVersoServer.writeBytes(nomeUtente + '\n');
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            conta++;
            // Chiudo la finestra per l'inserimento del nome utente
            frame.dispose();
        }

    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connetti();
        client.comunica();
    }
}