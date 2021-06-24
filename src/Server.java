import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Server {
    String url = "jdbc:postgresql://localhost/hospital-db?user=postgres&password=1234";

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            ConnectionPool connectionPool = new ConnectionPool(url);
            System.out.println("start");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("connection");
                new Thread(new ClientListener(socket, connectionPool)).start();
                System.out.println("continue");
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
