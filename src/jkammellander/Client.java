package jkammellander;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    // The constructor of the Client class. It is called when a new Client object is created.
    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    /**
     * The function takes in a username and a socket, and then it sends the username to the server, and then it waits for
     * the user to type in a message, and then it sends the message to the server
     */
    public void sendMessage() {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner scanner = new Scanner(System.in);

            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                // bufferedWriter.write(username + ": " + messageToSend);
                bufferedWriter.write(messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    /**
     * It creates a new thread that listens for messages from the server and prints them to the console
     */
    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;

                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine();

                        // Checking if the message from the server is equal to $, and if it is, then it exits the program.
                        if (msgFromGroupChat.equals("$")) {
                            System.exit(0);
                        }
                        System.out.println(msgFromGroupChat);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }

            }
        }).start();
    }

    /**
     * Close everything that's not null.
     *
     * @param socket The socket that is connected to the server.
     * @param bufferedReader This is the input stream that we will use to read the data from the server.
     * @param bufferedWriter This is the output stream that we will use to send data to the server.
     */
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        // Asking the user to enter a username, and then it stores the username in the variable username.
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username 🥸: ");
        String username =  scanner.nextLine();

        // It creates a new socket that connects to the server, and then it creates a new Client object that takes in the
        // socket and the username.
        Socket socket = new Socket("localhost",1234);
        Client client = new Client(socket, username);

        // It creates a new thread that listens for messages from the server and prints them to the console.
        client.listenForMessage();
        client.sendMessage();

    }

}
