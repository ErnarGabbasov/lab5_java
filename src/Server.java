import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Server {

    private List<Socket> clients;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public Server() {
        clients = new ArrayList<>();
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Server started on port 1234.");

            // Запускаем планировщик задач для рассылки сообщений
            Timer timer = new Timer();
            timer.schedule(new SendMessageTask(), 0, 5000); // Рассылать сообщения каждые 5 секунд

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                clients.add(clientSocket);

                // Создаем отдельный поток для каждого клиента
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SendMessageTask extends TimerTask {
        @Override
        public void run() {
            String message = "Hello, clients! Current time is " + new Date();

            // Рассылаем сообщение всем клиентам
            for (Socket client : clients) {
                try {
                    OutputStream outputStream = client.getOutputStream();
                    PrintWriter writer = new PrintWriter(outputStream);
                    writer.println(message);
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ClientHandler implements Runnable {

        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            while (true) {
                // Читаем сообщения от клиента, если необходимо
            }
        }
    }
}
