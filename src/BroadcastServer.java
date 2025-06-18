import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class BroadcastServer {
    public static void main(String[] args) {

        String usageMessage = "Usage: broadcast-server <command>";
        String startMessage = "Usage: broadcast-server start -p <port>";
        String connectMessage = "Usage: broadcast-server connect -m <message>";
        Scanner scanner = new Scanner(System.in);
        int port = 0;
        String ip = "127.0.0.1";
        List<String> commands = List.of(new String[]{"start", "connect", "message"});

        if (args.length == 2 || args.length == 4) {

            if (args[0].equals("broadcast-server")) {
                if (commands.contains(args[1].toLowerCase())) {
                    if (args[1].equalsIgnoreCase("start")) {
                        try {

                            if (args[2].equalsIgnoreCase("-p")) {
                                port = Integer.parseInt(args[3]);
                                HostServer hostServer = new HostServer();
                                hostServer.start(port);
                                System.out.println("server started on port " + port);
                            } else {
                                System.out.println(startMessage);
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println(startMessage);
                        }
                    } else if (args[1].equalsIgnoreCase("connect")) {
                        if (args[2].equalsIgnoreCase("-p")) {
                            int connectionPort = Integer.parseInt(args[3]);
                            String message;
                            HostClient hostClient = new HostClient();
                            hostClient.startConnection(ip, connectionPort);
                            while (true) {
                                scanner.reset();
                                message = scanner.nextLine();
                                if ((message).equals("exit")) {
                                    scanner.close();
                                    hostClient.sendMessage(message);
                                    hostClient.stopConnection();
                                    break;
                                }

                                hostClient.sendMessage(message);
                            }

                        } else {
                            System.out.println(connectMessage);
                        }
                    } else {
                        System.out.println(usageMessage);
                    }
                } else {
                    System.out.println(usageMessage);
                }
            } else {
                System.out.println(usageMessage);
            }
        } else {
            System.out.println(usageMessage);
        }
    }
}

class HostServer {

    private ServerSocket serverSocket;
    static List<ClientHandler> clients = new ArrayList<>();

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("server started on port " + port);

            while (true) {
                    new ClientHandler(serverSocket.accept()).start();
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to start server.\n" + e.getMessage());
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private static int clientCounter = 1;
        private String name;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            System.out.println("successfully connected to client.");
            this.name = "client-" + clientCounter++;
            clients.add(this);
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String msg;

                while (true) {
                    msg = in.readLine();
                    if (msg == null || msg.equals("exit")) {
                        System.out.println("disconnecting client");
                        clientSocket.close();
                        clients.remove(this);
                        break;
                    }

                    System.out.println("received message from client");
                    for (ClientHandler client : clients) {
                        if (client != this) {
                            client.out.println("[" + name + "]: " + msg);
                        } else {
                            client.out.println("[you]: " + msg);
                        }
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}

class HostClient {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;


    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            System.out.println("successfully connected to server on port " + port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to start connection.\n" + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        out.println(message);
        try {
            System.out.println(in.readLine());
        } catch (IOException e) {
            throw new RuntimeException("Failed to get a reply from server.\n" + e.getMessage());
        }
    }

    public void stopConnection() {
        try {
            System.out.println("client is shutting down!");
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}

