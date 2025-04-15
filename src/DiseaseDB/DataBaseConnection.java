package DiseaseDB;

import javax.swing.*;
import java.sql.*;

public class DataBaseConnection { // Start of DataBaseConnection class
    private static final String url = "jdbc:mysql://servername/disease_db";
    private static final String username = "username";
    private static final String password = "password";

    /**
     * Attempts to establish a connection to the DiseaseDB database.
     *
     * @return A Connection object if the connection is established successfully, otherwise null.
     */
    public static Connection getConnection() { // Start of getConnection
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DiseaseDataBase connection failed!");
            JOptionPane.showMessageDialog(null, "DiseaseDataBase connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    } // End of getConnection

} // End of DataBaseConnection class

