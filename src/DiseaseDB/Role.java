package DiseaseDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Role { // Start of Role class

    private int id;
    private String name;

    private Connection connection;

    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role(Connection connection) {
        this.connection = connection;
    }

    public void viewRoles() { // Start of viewRoles
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM roles");
            while (rs.next()) {
                System.out.println("ID-> " + rs.getInt("ID"));
                System.out.println("Role_Name-> " + rs.getString("name"));
                System.out.println("=".repeat(20));
            }
            rs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }// End of viewRoles

} // End of Role class

