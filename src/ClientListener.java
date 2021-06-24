import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ClientListener implements Runnable {
    public ConnectionPool connectionPool;
    private Connection connection;
    private Socket socket;
    private Db db;


    public ClientListener(Socket socket, ConnectionPool connectionPool) {
        this.connectionPool= connectionPool;
        this.socket = socket;
        try {
            this.connection = connectionPool.getConnection();
            db = new Db(connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void run() {
        try (
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                Scanner scanner = new Scanner(socket.getInputStream())
        ) {
            while (scanner.hasNextLine()) {
                String key = scanner.nextLine();
                Gson gson = new Gson();
                String jsonStaff = null;
                String jsonSick = null;
                String jsonCheckRegistration = null;
                String time_visit;
                boolean result;
                Sick sick;
                Staff staff;
                List<Sick> sicks;
                List<Staff> staffs;
                switch (key) {
                    case MyRequestCode.KEY_PUT_SICK:
                        jsonSick = scanner.nextLine();
                        sick = gson.fromJson(jsonSick, Sick.class);
                        db.saveSick(sick);
                        break;
                    case MyRequestCode.KEY_PUT_STAFF:
                        jsonStaff = scanner.nextLine();
                        staff = gson.fromJson(jsonStaff, Staff.class);
                        db.saveStaff(staff);
                        break;
                    case MyRequestCode.KEY_GET_SICKS:
                        sicks = db.getSicks();
                        jsonSick = gson.toJson(sicks);
                        printWriter.println(jsonSick);
                        printWriter.flush();
                        break;
                    case MyRequestCode.KEY_GET_STAFFS:
                        staffs = db.getStaffs();
                        jsonStaff = gson.toJson(staffs);
                        printWriter.println(jsonStaff);
                        printWriter.flush();
                        break;
                    case MyRequestCode.KEY_CHECK_REGISTRATION:
                        CheckRegistration checkRegistration = db.getCheckRegistration();
                        jsonCheckRegistration = gson.toJson(checkRegistration);
                        printWriter.println(jsonCheckRegistration);
                        printWriter.flush();
                        break;
                    case MyRequestCode.KEY_GET_SICKS_WHO_RECORDED_TO_YOU:
                        jsonStaff = scanner.nextLine();
                        staff = gson.fromJson(jsonStaff, Staff.class);
                        sicks = db.getSicksWhoRecordedToYou(staff);
                        jsonSick = gson.toJson(sicks);
                        printWriter.println(jsonSick);
                        printWriter.flush();
                        break;
                    case MyRequestCode.KEY_DELETE_SICK:
                        time_visit = scanner.nextLine();
                        result = db.deleteSick(time_visit);
                        printWriter.println(result);
                        printWriter.flush();
                        break;
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }

        connectionPool.returnConnection(connection);
    }
}
