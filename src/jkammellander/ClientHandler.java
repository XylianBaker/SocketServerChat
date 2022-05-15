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

    // Creating a new ArrayList of ClientHandlers, a new Socket, a new BufferedReader, a new BufferedWriter, and a new
    // String.
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
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            this.lock = lock;
            broadcastMessage("SERVER: " + this.clientUsername + " has connected🔌");

            QuestionHandler questionHandler = new QuestionHandler();
            for (int index : questionHandler.getQuestionIndex()) {
                this.questions.add( questionHandler.getQuestions().get(index));
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        synchronized (lock) {
            lock.notifyAll();
        }
        String clientAnswer;
        try {
                for (Question question : this.questions) {
                    this.bufferedWriter.write(question.question());
                    this.bufferedWriter.newLine();
                    this.bufferedWriter.flush();
                    clientAnswer = bufferedReader.readLine();
                    if (clientAnswer.equals(question.answer())) {
                        this.answered++;
                    }
                }
                broadcastMessage(clientUsername + " has answered " + this.answered + "/3 questions!");
                this.bufferedWriter.write("$\n");
                this.bufferedWriter.flush();
                closeEverything(socket, bufferedReader, bufferedWriter);
            } catch (IOException e) {
            e.printStackTrace();
        }

        /*String messageFromClient;
        while(socket.isConnected()) {
            try {
                //messageFromClient = bufferedReader.readLine();
                // broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }*/
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler: clientHandlers) {
            try {
                //if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                //}
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left 🥲!");
    }

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
