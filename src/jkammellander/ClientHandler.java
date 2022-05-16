package jkammellander;

import jkammellander.questions.Question;
import jkammellander.questions.QuestionHandler;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    private ArrayList<Question> questions = new ArrayList<>();
    private int answered = 0;
    private Object lock;

    // This is the constructor for the ClientHandler class. It is creating a new ClientHandler object.
    public ClientHandler(Socket socket, Object lock) {
        try {
            // Creating a new ClientHandler object.
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);

            // Assigning the lock object to the lock variable.
            this.lock = lock;

            // Sending a message to all the clients that are connected to the server.
            broadcastMessage("SERVER: " + this.clientUsername + " has connected🔌");

            // Getting the questions from the QuestionHandler class and adding them to the questions ArrayList.
            QuestionHandler questionHandler = new QuestionHandler();
            for (int index : questionHandler.getQuestionIndex()) {
                this.questions.add( questionHandler.getQuestions().get(index));
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    // Sending the questions to the client and checking if the answer is correct.
    @Override
    public void run() {
        // Notifying all the threads that are waiting on the lock object.
        synchronized (lock) {
            lock.notifyAll();
        }
        String clientAnswer;
        try {
                // Sending the questions to the client and checking if the answer is correct.
                for (Question question : this.questions) {
                    this.bufferedWriter.write(question.question());
                    this.bufferedWriter.newLine();
                    this.bufferedWriter.flush();
                    clientAnswer = bufferedReader.readLine();
                    if (clientAnswer.equals(question.answer())) {
                        this.answered++;
                    }
                }
                // Sending a message to all the clients that are connected to the server.
                broadcastMessage(clientUsername + " has answered " + this.answered + "/3 questions!");
                this.bufferedWriter.write("$\n");
                this.bufferedWriter.flush();
                // Closing the socket, bufferedReader and bufferedWriter.
                closeEverything(socket, bufferedReader, bufferedWriter);
            } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * For each clientHandler in the clientHandlers list, if the clientHandler's clientUsername is not equal to the
     * clientUsername of the clientHandler that called this function, then write the messageToSend to the clientHandler's
     * bufferedWriter and flush it.
     *
     * @param messageToSend The message to send to all clients.
     */
    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler: clientHandlers) {
            try {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    /**
     * Remove the client handler from the list of client handlers and broadcast a message to all the other clients that
     * this client has left.
     */
    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left 🥲!");
    }

    /**
     * Close everything that can be closed.
     *
     * @param socket The socket that is connected to the client.
     * @param bufferedReader This is the input stream that reads the data from the client.
     * @param bufferedWriter This is the writer that will be used to send messages to the client.
     */
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
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
}
