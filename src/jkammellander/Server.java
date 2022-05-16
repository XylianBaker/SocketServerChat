package jkammellander;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {

    // Creating a private variable called serverSocket of type ServerSocket.
    private ServerSocket serverSocket;

    // A constructor that takes a ServerSocket as a parameter.
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * > The server will keep accepting new clients until the server socket is closed
     */
    public void startServer() {
        try {

            while (!serverSocket.isClosed()) {
                // Creating a new socket and printing out a message to the console.
                Socket socket = serverSocket.accept();
                System.out.println("A new client has conencted 🥳 !");

                // It creates a new object called lock, creates a new ClientHandler object and passes the socket and lock
                // to it.
                // Then it creates a new thread and passes the clienthandler to it.
                Object lock = new Object();
                ClientHandler clienthandler = new ClientHandler(socket, lock);
                Thread thread = new Thread(clienthandler);
                thread.start();

                // It's waiting for the lock to be notified.
                synchronized (lock) {
                    lock.wait();
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the server socket if it's not null.
     */
    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        // It creates a new server socket on port 1234 and then creates a new server object and calls the startServer
        // method on it.
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();

    }
}
