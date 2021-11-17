package com.itismeucci;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client implements ActionListener, KeyListener {
    String nomeServer = "localhost";
    int portaServer = 6789;
    int conta = 0;
    Socket miosocket;
    BufferedReader tastiera;
    String stringaUtente;
    String stringaRicevutaDalServer;
    DataOutputStream outVersoServer;

    // componenti GUI
    JFrame frame1;
    JFrame frame2;
    JPanel panel1;
    JPanel panel2;
    JLabel labelNomeUtente;
    JTextField textField;
    JTextArea textArea;
    JButton buttonInserisci;
    JButton buttonInvia;

    public void inserimentoNomeUtente() {

        frame1 = new JFrame();

        labelNomeUtente = new JLabel("Inserire nome utente");
        labelNomeUtente.setBounds(240, 180, 400, 30);
        labelNomeUtente.setFont(new Font("Itim", Font.BOLD, 18));

        textField = new JTextField();
        textField.setBounds(220, 240, 180, 30);
        textField.setFont(new Font("Itim", Font.BOLD, 14));

        buttonInserisci = new JButton("Inserisci");
        buttonInserisci.setBounds(240, 280, 140, 30);
        buttonInserisci.setFont(new Font("Itim", Font.BOLD, 14));
        buttonInserisci.addActionListener(this);
        buttonInserisci.addActionListener(this);

        panel1 = new JPanel();
        panel1.setLayout(null);
        panel1.setPreferredSize(new Dimension(720, 480));
        panel1.add(labelNomeUtente);
        panel1.add(textField);
        panel1.add(buttonInserisci);

        frame1.add(panel1);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setTitle("siChiacchera");
        frame1.setResizable(false);
        frame1.pack();
        frame1.setLocationRelativeTo(null);
        frame1.setVisible(true);
    }

    public void chatGUI() {
        frame2 = new JFrame();

        textArea = new JTextArea();
        textArea.setBounds(10, 10, 680, 400);
        textArea.setFont(new Font("Itim", Font.PLAIN, 14));
        textArea.setBackground(Color.orange);
        textArea.setEditable(false);

        buttonInvia = new JButton("Invia");
        buttonInvia.setBounds(610, 450, 80, 20);
        buttonInvia.setFont(new Font("Itim", Font.BOLD, 14));
        buttonInvia.addActionListener(this);
        buttonInvia.addActionListener(this);

        panel2 = new JPanel();
        panel2.setLayout(null);
        panel2.setPreferredSize(new Dimension(720, 480));
        panel2.add(buttonInvia);
        panel2.add(textArea);

        frame2.add(panel2);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setTitle("siChiacchera");
        frame2.setResizable(false);
        frame2.pack();
        frame2.setLocationRelativeTo(null);
        frame2.setVisible(true);
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

    public void comunica() throws IOException {
        ClientListener listener = new ClientListener(miosocket);
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

    @Override
    public void actionPerformed(ActionEvent e) {

        // controllo se viene premuto il pulsante per l'inserimento del nome utente
        if (e.getSource().equals(buttonInserisci)) {
            String nomeUtente = textField.getText();
            try {
                outVersoServer.writeBytes(nomeUtente + '\n');
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            // Chiudo la finestra per l'inserimento del nome utente
            frame1.dispose();
            // apro la finestra della chat
            chatGUI();

        } else { // controllo se viene premuto il pulsante per l'invio dei messaggi

        }

    }

    // metodi sovrascritti dall'implementazione KeyListener
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connetti();
        // client.comunica();
        client.inserimentoNomeUtente();
    }

}