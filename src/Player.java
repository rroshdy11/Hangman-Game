import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;


public class Player {

                private static final String DB_URL = "jdbc:sqlite:User.db";

                public static void main(String[] args) {
                        // Connect to the database
                        try (Connection conn = DriverManager.getConnection(DB_URL)) {
                                System.out.println("Connected to SQLite database");
                                // Register a new user
                                registerUser(conn, "johndoe" ,"John Doe","password");
                                // Login as an existing user
                                login(conn, "johndoe", "password");
                        } catch (SQLException e) {
                                System.out.println(e.getMessage());
                        }
                }

                private static void registerUser(Connection conn, String name, String username, String password)
                        throws SQLException {
                        String sql = "INSERT INTO User(username,name, password) VALUES(?,?,?)";
                        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                                pstmt.setString(1, username);
                                pstmt.setString(2, name);
                                pstmt.setString(3, password);
                                pstmt.executeUpdate();
                        } catch (SQLException e) {
                                if (e.getErrorCode() == 19) { // SQLITE_CONSTRAINT (unique constraint failed)
                                        System.out.println("Error: Username is already reserved");
                                } else {
                                        throw e;
                                }
                        }
                }

                private static void login(Connection conn, String username, String password) throws SQLException {
                        String sql = "SELECT * FROM User WHERE username = ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                                pstmt.setString(1, username);
                                ResultSet rs = pstmt.executeQuery();

                                if (!rs.next()) {
                                        System.out.println("Error: Username not found");
                                        return;
                                }

                                if (!rs.getString("password").equals(password)) {
                                        System.out.println("Error: Invalid password");
                                        return;
                                }
                        }
                }
        }






