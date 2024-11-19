import java.sql.*;
import java.util.Scanner;

public class AlbumDB{

    private static final String DB_URL = "jdbc:mysql://localhost:3306/album";
    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = "rootroot"; // Replace with your MySQL password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connected to the database.");

            while (true) {
                System.out.println("\nChoose an option:");
                System.out.println("1. Display all records");
                System.out.println("2. Add a new record");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline

                switch (choice) {
                    case 1:
                        displayRecords(connection);
                        break;
                    case 2:
                        addRecord(connection, scanner);
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayRecords(Connection connection) {
        String query = "SELECT * FROM album";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("\nRecords in the album table:");
            System.out.printf("%-5s %-30s %-30s %-20s %-15s%n", "ID", "Title", "Artist", "Label", "Release Date");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String artist = resultSet.getString("artist");
                String label = resultSet.getString("label");
                Date releaseDate = resultSet.getDate("released");

                System.out.printf("%-5d %-30s %-30s %-20s %-15s%n", id, title, artist, label, releaseDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addRecord(Connection connection, Scanner scanner) {
        System.out.print("Enter ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        System.out.print("Enter Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter Artist: ");
        String artist = scanner.nextLine();

        System.out.print("Enter Label: ");
        String label = scanner.nextLine();

        System.out.print("Enter Release Date (YYYY-MM-DD): ");
        String releaseDate = scanner.nextLine();

        String query = "INSERT INTO album (id, title, artist, label, release_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, artist);
            preparedStatement.setString(4, label);
            preparedStatement.setDate(5, Date.valueOf(releaseDate));

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Record added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}