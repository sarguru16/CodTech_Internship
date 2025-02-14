import java.io.*;
import java.net.*;
import java.util.*;

// Chat server class to handle multiple client connections
public class ChatServer {
    // Port number on which the server will listen for client connections
    private static final int PORT = 12345;

    // Set to store all connected clients
    private static Set<ClientHandler> clientHandlers = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Chat Server is running...");

        // Try to create a server socket and accept client connections
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // Accept a new client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                // Create a new handler for the connected client
                ClientHandler clientHandler = new ClientHandler(clientSocket);

                // Add the client handler to the list of active clients
                clientHandlers.add(clientHandler);

                // Start a new thread to handle client communication
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to broadcast a message to all connected clients except the sender
    public static void broadcast(String message, ClientHandler excludeClient) {
        for (ClientHandler client : clientHandlers) {
            if (client != excludeClient) {
                client.sendMessage(message);
            }
        }
    }

    // Method to remove a client from the set when they disconnect
    public static void removeClient(ClientHandler client) {
        clientHandlers.remove(client);
        System.out.println("Client disconnected: " + client.getClientSocket());
    }

    // Inner class to handle individual client connections
    private static class ClientHandler implements Runnable {
        private Socket clientSocket; // Socket for client communication
        private PrintWriter out; // Output stream to send messages to the client
        private BufferedReader in; // Input stream to read messages from the client

        // Constructor to initialize client connection
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Getter method to retrieve the client socket
        public Socket getClientSocket() {
            return clientSocket;
        }

        @Override
        public void run() {
            try {
                String clientMessage;

                // Continuously read messages from the client
                while ((clientMessage = in.readLine()) != null) {
                    System.out.println("Received: " + clientMessage);

                    // Broadcast the received message to all other clients
                    broadcast(clientMessage, this);
                }
            } catch (IOException e) {
                System.out.println("Client disconnected unexpectedly.");
            } finally {
                try {
                    // Close the client socket when the client disconnects
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Remove the client from the active client list
                removeClient(this);
            }
        }

        // Method to send a message to this client
        public void sendMessage(String message) {
            out.println(message);
        }
    }
}