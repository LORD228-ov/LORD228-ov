import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Db {
    String url = "jdbc:postgresql://localhost/hospital-db?user=postgres&password=1234";
    Connection connection;


    public Db(Connection connection) {
        this.connection = connection;
    }

    public void saveSick(Sick sick) {
        String first_name = sick.getFirstName();
        String last_name = sick.getLastName();
        String time_visit = sick.getTime_visit();
        String last_name_of_service_personnel = sick.getLast_name_of_service_personnel();
        String quary0 = "SELECT * FROM sick WHERE time_visit = '"+time_visit+"'";
        String quary1 = "INSERT INTO sick (first_name, last_name, time_visit, last_name_of_service_personnel) VALUES ('" + first_name + "', '" + last_name + "', '" + time_visit + "', '" + last_name_of_service_personnel + "')";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(quary0);
            if (!resultSet.next()) {
                statement.execute(quary1);
                System.out.println("sick added!");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void saveStaff(Staff staff) {
        String first_name = staff.getFirstName();
        String last_name = staff.getLastName();
        String profession = staff.getProfession();
        String quary = "INSERT INTO staff (first_name, last_name, profession) VALUES ('" + first_name + "', '" + last_name + "', '" + profession + "')";
        try {
            Statement statement = connection.createStatement();
            statement.execute(quary);
            System.out.println("staff added!");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Sick> getSicks() {
        String quary = "SELECT * FROM sick";
        List<Sick> sicks = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(quary)
        ) {
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String time_visit = resultSet.getString("time_visit");
                String last_name_of_service_personnel = resultSet.getString("last_name_of_service_personnel");
                sicks.add(new Sick(id, firstName, lastName, time_visit, last_name_of_service_personnel));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sicks.stream()
                .sorted(Comparator.comparing(Sick::getId).thenComparing(Sick::getFirstName).thenComparing(Sick::getLastName))
                .collect(Collectors.toList());

    }

    public List<Staff> getStaffs() {
        String quary = "SELECT * FROM staff";
        List<Staff> staffs = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(quary)
        ) {
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String profession = resultSet.getString("profession");
                staffs.add(new Staff(id, firstName, lastName, profession));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return staffs.stream()
                .sorted(Comparator.comparing(Staff::getId).thenComparing(Staff::getFirstName).thenComparing(Staff::getLastName))
                .collect(Collectors.toList());
    }

    public CheckRegistration getCheckRegistration() {
        String quary = "SELECT * FROM check_registration";
        CheckRegistration checkRegistration = null;
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(quary)
        ) {
            if (resultSet.next()) {
                String login_db = resultSet.getString("login_db");
                String password_db = resultSet.getString("password_db");
                checkRegistration = new CheckRegistration(login_db, password_db);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return checkRegistration;

    }

    public List<Sick> getSicksWhoRecordedToYou(Staff staff) {
        String last_name_of_service_personnel0 = staff.getLastName();
        String quary = "SELECT * FROM sick WHERE last_name_of_service_personnel = '"+last_name_of_service_personnel0+"'";
        List<Sick> sicks = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(quary)
        ) {
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String time_visit = resultSet.getString("time_visit");
                String last_name_of_service_personnel2 = resultSet.getString("last_name_of_service_personnel");
                sicks.add(new Sick(id, firstName, lastName, time_visit, last_name_of_service_personnel2));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sicks.stream()
                .sorted(Comparator.comparing(Sick::getId).thenComparing(Sick::getFirstName).thenComparing(Sick::getLastName))
                .collect(Collectors.toList());

    }

    public boolean deleteSick(String time_visit) {
        String quary0 = "SELECT * FROM sick WHERE time_visit = '"+time_visit+"'";
        String quary1 = "DELETE FROM sick WHERE time_visit = '"+time_visit+"'";
        boolean result = false;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(quary0);
            if (resultSet.next()) {
                statement.execute(quary1);
                System.out.println("sick deleted!");
                result = true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}
