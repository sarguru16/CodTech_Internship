import java.io.*;
import java.net.*;

// Chat client class to connect to the server and send/receive messages
public class ChatClient {
    // Server address and port number
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Connected to the chat server.");

            // Start a thread to read messages from the server
            new Thread(new ReadHandler(socket)).start();

            // Start a thread to send messages to the server
            new Thread(new WriteHandler(socket)).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inner class to handle reading messages from the server
    private static class ReadHandler implements Runnable {
        private Socket socket; // Socket for communication with the server
        private BufferedReader in; // Input stream to receive messages from the server

        // Constructor to initialize input stream
        public ReadHandler(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String serverMessage;

                // Continuously read messages from the server
                while ((serverMessage = in.readLine()) != null) {
                    System.out.println(serverMessage); // Print received message
                }
            } catch (IOException e) {
                System.out.println("Disconnected from the server.");
            }
        }
    }

    // Inner class to handle writing messages to the server
    private static class WriteHandler implements Runnable {
        private Socket socket; // Socket for communication with the server
        private PrintWriter out; // Output stream to send messages to the server
        private BufferedReader consoleInput; // Input stream to read user input from the console

        // Constructor to initialize output stream and console input
        public WriteHandler(Socket socket) {
            this.socket = socket;
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                consoleInput = new BufferedReader(new InputStreamReader(System.in));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String userInput;

                // Continuously read user input and send it to the server
                while ((userInput = consoleInput.readLine()) != null) {
                    out.println(userInput);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}