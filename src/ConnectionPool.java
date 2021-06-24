import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

public class ConnectionPool {
    String connectionURL;

    static final int INITIAL_CAPACITY = 10;
    LinkedList<Connection> pool = new LinkedList<Connection>();
    public String getConnectionURL() {
        return connectionURL;
    }

    public ConnectionPool(String connectionURL) throws SQLException {
        this.connectionURL = connectionURL;

        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            pool.add(DriverManager.getConnection(connectionURL));
        }
    }

    public synchronized Connection getConnection() throws SQLException {
        if (pool.isEmpty()) {
            pool.add(DriverManager.getConnection(connectionURL));
        }
        return pool.pop();
    }

    public synchronized void returnConnection(Connection connection) {
        pool.push(connection);
    }
}
