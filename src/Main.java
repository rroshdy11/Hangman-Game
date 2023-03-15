//case 1
//team 1 aph ---   (worng 6) in case of all charcters gussed  score is number of characters in the word
//team 2 --- h--   (worng 6)


// score criteria (num of chars in pharse)
// when two teams finishes their worng attempts

import java.sql.*;


public class Main {
    private static Connection conn = null;

    public static void main(String[] args) throws SQLException {

        System.out.println("Hello world!");
        //add new player to database using his name and username and password
        addNewPlayerToDB("ahmed", "ahmed", "123");


    }
    //add new player to database using his name and username and password
    public static void addNewPlayerToDB(String name, String username, String password) throws SQLException {
        connect();
        //sql statement
        String sql = "INSERT INTO User(name,username,password) VALUES('" + name + "','" + username + "','" + password + "')";
        //execute sql statement
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        disconnect();

    }


    public static void connect() {
        try {
            // db parameters
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:DB.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
        //disconnect from database
    public static void disconnect() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

