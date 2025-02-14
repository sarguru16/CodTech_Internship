TASK 3

Server:

  1) Listens for incoming client connections on a specified port.

  2) Creates a new thread (ClientHandler) for each connected client.

  3) Broadcasts messages received from one client to all other clients.

Client:

  ** Connects to the server using the server's address and port.

Uses two threads:

  One to read messages from the server and display them.

  Another to read user input from the console and send it to the server.
