package jkammellander;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
                Socket socket = serverSocket.accept();
                System.out.println("A new client has conencted 🥳 !");
                ClientHandler clienthandler = new ClientHandler(socket);

                Thread thread = new Thread(clienthandler);
                thread.start();
            }

        } catch (IOException e) {
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
