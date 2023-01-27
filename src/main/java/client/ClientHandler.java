package client;

import java.io.*;
import java.net.Socket;
import java.util.List;

import Main.TransferObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import enums.Commands;

public class ClientHandler {

    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 8989;
    private Socket clientSocket;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;

    private static ClientHandler SINGLECLIENT;

    private ClientHandler() throws IOException {

        try {
            System.out.println("server connecting....");
            clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            System.out.println("connection established....");

            BufferedReader BRuh = new BufferedReader(new InputStreamReader(System.in));
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ClientHandler getInstance() throws IOException {
        if (SINGLECLIENT == null) {
            SINGLECLIENT = new ClientHandler();
        }
        return SINGLECLIENT;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public <T> boolean sendObject(T object, Commands command) {
        TransferObject<T> transferObject = new TransferObject<T>();
        return transferObject.sendObjectTo(object, command, oos);
    }

    public TransferObject getObject() {
        TransferObject transferObject = new TransferObject();
        transferObject = transferObject.getObjectFrom(ois);

        return transferObject;
    }


    public <T> boolean sendList(List<T> objects, Commands command) throws JsonProcessingException {
        TransferObject<T> transferObject = new TransferObject<T>();
        return transferObject.sendList(objects, command, oos);
    }

    public List<TransferObject> getList() {
        TransferObject transferObject = new TransferObject();
        return transferObject.getList(ois);
    }
}
