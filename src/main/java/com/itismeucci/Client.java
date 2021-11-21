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
    String nomeUtente;
    DataOutputStream outVersoServer;
    ClientListener listener;

    // componenti GUI
    JFrame frame1;
    JFrame frame2;
    JPanel panel1;
    JPanel panel2;
    JLabel labelNomeUtente;
    JTextField textField;
    JTextField tfMessaggio;
    public static JTextArea textArea;
    JButton buttonInserisci;
    JButton buttonInvia;

    public void inserimentoNomeUtente() {
        frame1 = new JFrame();

        labelNomeUtente = new JLabel("Inserire nome utente");
        labelNomeUtente.setBounds(230, 130, 400, 30);
        labelNomeUtente.setFont(new Font("Itim", Font.BOLD, 18));
        labelNomeUtente.setForeground(Color.decode("#EEEEEE"));

        textField = new JTextField();
        textField.setBounds(240, 170, 160, 25);
        textField.setFont(new Font("Itim", Font.BOLD, 16));
        textField.setBackground(Color.decode("#EEEEEE"));

        buttonInserisci = new JButton("Inserisci");
        buttonInserisci.setBounds(270, 210, 100, 25);
        buttonInserisci.setFont(new Font("Itim", Font.BOLD, 16));
        buttonInserisci.setBackground(Color.decode("#FD7014"));
        buttonInserisci.addActionListener(this);
        buttonInserisci.addKeyListener(this);

        panel1 = new JPanel();
        panel1.setLayout(null);
        panel1.setPreferredSize(new Dimension(640, 360));
        panel1.setBackground(Color.decode("#222831"));
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
        textArea.setBounds(10, 10, 834, 425);
        textArea.setFont(new Font("Itim", Font.BOLD, 18));
        textArea.setBackground(Color.decode("#393E46"));
        textArea.setForeground(Color.decode("#EEEEEE"));
        textArea.setEditable(false);

        // per rendere la textArea scrollabile NON FUNZIONA
        JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        tfMessaggio = new JTextField();
        tfMessaggio.setBounds(10, 445, 744, 25);
        tfMessaggio.setFont(new Font("Itim", Font.BOLD, 16));
        tfMessaggio.setBackground(Color.decode("#EEEEEE"));

        buttonInvia = new JButton("Invia");
        buttonInvia.setBounds(764, 445, 80, 25);
        buttonInvia.setFont(new Font("Itim", Font.BOLD, 16));
        buttonInvia.setBackground(Color.decode("#FD7014"));
        buttonInvia.addActionListener(this);
        buttonInvia.addKeyListener(this);

        panel2 = new JPanel();
        panel2.setLayout(null);
        panel2.setPreferredSize(new Dimension(854, 480));
        panel2.setBackground(Color.decode("#222831"));
        panel2.add(buttonInvia);
        panel2.add(textArea);
        panel2.add(tfMessaggio);
        panel2.add(scroll);

        frame2.add(panel2);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setTitle("siChiacchera | Utente: " + nomeUtente);
        frame2.setResizable(false);
        frame2.pack();
        frame2.setLocationRelativeTo(null);
        frame2.setVisible(true);

        listener.start();

        //non funziona
        ServerListener serverListener = new ServerListener();
        textArea.setText(serverListener.stampaUtentiConnessi());
    }

    public Socket connetti() {
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

    public void comunica() {
        listener.start();

        for (;;) {
            try {
                if (conta == 0) {
                    System.out.print("Inserisci nome utente: ");
                    stringaUtente = tastiera.readLine();
                    outVersoServer.writeBytes(stringaUtente + '\n');
                    conta++;
                } else {
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
            nomeUtente = textField.getText();
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
            String messaggio = tfMessaggio.getText();
            tfMessaggio.setText("");

            if (messaggio.charAt(0) != '$') {
                textArea.append(nomeUtente + ": " + messaggio + "\n");
            }

            try {
                outVersoServer.writeBytes(messaggio + '\n');
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            // chiusura della chat se viene mandato il messaggio "$e"
            if (messaggio.equals("$e")) {
                frame2.dispose();
            }
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