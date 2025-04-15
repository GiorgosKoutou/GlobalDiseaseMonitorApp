package DiseaseDB;

import Components.TableManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class User { // Start of User class

    private int id;
    private String username;
    private String password;
    private int role_id;

    private TableManager myTable;
    private DefaultTableModel myModel;

    public User(int id, String username, String password, int role_id) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role_id = role_id;
    }

    public User(TableManager myTable) {
        this.myTable = myTable;
        myModel = new DefaultTableModel(new String[]{"ID", "Username", "Password", "Role_ID"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0) {
                    return false;
                }
                return true;
            }
        };
        this.myTable.setModel(myModel);
    }

    /*
     * ***  Method to add a user record to the database ***
     */
    /* ================= Start of Add User ============== */
    public boolean addUser(String name, String password, int role_id) {
        boolean invalidInput = false;
        try (Connection connection = DataBaseConnection.getConnection()) {
            if (password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!&^*?]).+$")) {
                if (password.length() >= 10) {
                    String insertQuery = ("INSERT INTO users " +
                            "(username, password, role_id) " +
                            "VALUES " +
                            "(?, ?, ?)");
                    PreparedStatement insertStm = connection.prepareStatement(insertQuery);
                    insertStm.setString(1, name);
                    insertStm.setString(2, password);
                    insertStm.setInt(3, role_id);
                    insertStm.executeUpdate();
                    insertStm.close();

                    viewUsers();
                } else {
                    System.out.println("Password must be at least 10 characters long");
                    JOptionPane.showMessageDialog(null, "Password must be at least 10 characters", "Error", JOptionPane.ERROR_MESSAGE);
                    invalidInput = true;
                }
            } else {
                System.out.println("Invalid Format");
                JOptionPane.showMessageDialog(null, "Invalid Password Format.Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character", "Error", JOptionPane.ERROR_MESSAGE);
                invalidInput = true;
            }


        } catch (SQLException e) {
            String duplicateUsername = "Duplicate entry '" + name + "' for key 'users.username_UNIQUE'";

            if (e.getMessage().equals(duplicateUsername)) {
                JOptionPane.showMessageDialog(null, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }else {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "SQL Error: Unable to add User.", "Error", JOptionPane.ERROR_MESSAGE);
                invalidInput = true;
            }
        }
        return invalidInput;
    }
    /* ================= End of Add User ============== */

    /*
    * *** Method to update all fields of a user record ***
    */
    /* ================= Start of Update All Fields ============== */
    public boolean updateUserAllFields(int id, String name, String password, int role_id){
        boolean validInput = true;
        try (Connection connection = DataBaseConnection.getConnection()){
            if (password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!&^*?]).+$")) {
                if (password.length() >= 10) {
                    String updateQuery = "UPDATE users SET username = ?, password = ?, role_id = ? WHERE ID = ?";
                    PreparedStatement updateStm = connection.prepareStatement(updateQuery);
                    updateStm.setString(1, name);
                    updateStm.setString(2, password);
                    updateStm.setInt(3, role_id);
                    updateStm.setInt(4, id);
                    updateStm.executeUpdate();
                    updateStm.close();

                    viewUsers();
                } else {
                    JOptionPane.showMessageDialog(null, "Password must be at least 10 characters", "Error", JOptionPane.ERROR_MESSAGE);
                    validInput = false;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Password Format.Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character", "Error", JOptionPane.ERROR_MESSAGE);
                validInput = false;
            }
        }catch (SQLException e){
            String duplicateUsername = "Duplicate entry '" + name + "' for key 'users.username_UNIQUE'";

            if (e.getMessage().equals(duplicateUsername)) {
                JOptionPane.showMessageDialog(null, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                validInput = false;
            }

            if (e.getClass().equals(SQLIntegrityConstraintViolationException.class)) {
                String message = e.getMessage();
                if (message.equals("Column 'role_id' cannot be null")){
                    JOptionPane.showMessageDialog(null, "Role ID cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    validInput = false;
                }
                if (message.contains("user_role_FK")){
                    JOptionPane.showMessageDialog(null, "Role ID does not exist.\n You can choose:\n 1-> Admin\n 2-> Analyst\n 3-> User", "Error", JOptionPane.ERROR_MESSAGE);
                    validInput = false;
                }

            }else{
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "SQL Error: Unable to update user role.", "Error", JOptionPane.ERROR_MESSAGE);
                validInput = false;
            }
        }
        return validInput;
    }
    /* ================= End of Update All Fields ============== */

    /*
    * *** Method to update the username of a user record ***
    */
    /* ================= Start of Update UserName ============== */
    public void updateUserUsername(int id, String name){ // Start of updateUserUsername
        try (Connection connection = DataBaseConnection.getConnection()){
            String updateQuery = "UPDATE users SET username = ? WHERE ID = ?";
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setString(1, name);
            updateStm.setInt(2, id);
            updateStm.executeUpdate();
            updateStm.close();

            viewUserById(id);
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
            if (e.getErrorCode() == 1062){
                JOptionPane.showMessageDialog(null, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    /* ================= End of Update UserName ============== */

    /*
    * *** Method to update the password of a user record ***
    */
    /* ================= Start of Update Password ============== */
    public void updateUserPassword(int id, String password){
        try (Connection connection = DataBaseConnection.getConnection()){
            if (password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!&^*?]).+$")) {
                if (password.length() >= 10) {
                    String updateQuery = "UPDATE users SET password = ? WHERE ID = ?";
                    PreparedStatement updateStm = connection.prepareStatement(updateQuery);
                    updateStm.setString(1, password);
                    updateStm.setInt(2, id);
                    updateStm.executeUpdate();
                    updateStm.close();

                    viewUserById(id);
                } else {
                    JOptionPane.showMessageDialog(null, "Password must be at least 10 characters", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.out.println("Invalid Format");
                JOptionPane.showMessageDialog(null, "Invalid Password Format.\nPassword must contain at least 1 Lowercase Letter, ! Uppercase Letter, 1 Number, and 1 Special Character", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    /* ================= End of Update Password ============== */

    /*
    * *** Method to update the role id of a user record ***
    */
    /* ================= Start of Update User Role ============== */
    public void updateUserRole(int id, int role_id) { // Start of updateUserRole
        try (Connection connection = DataBaseConnection.getConnection()){
            String updateQuery = "UPDATE users SET role_id = ? WHERE ID = ?";
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setInt(1, role_id);
            updateStm.setInt(2, id);
            updateStm.executeUpdate();
            updateStm.close();

            viewUserById(id);
        } catch (SQLException e) {
            if (e.getClass().equals(SQLIntegrityConstraintViolationException.class)) {
                String message = e.getMessage();
                if (message.equals("Column 'role_id' cannot be null")){
                    JOptionPane.showMessageDialog(null, "Role ID cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                }
                if (message.contains("user_role_FK")){
                    JOptionPane.showMessageDialog(null, "Role ID does not exist.\n You can choose 1->Admin\n 2->Analyst\n 3->User", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }else{
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "SQL Error: Unable to update user role.", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        }
    }
    /* ================= End of Update User Role ============== */

    /*
    * *** Method to delete a user record from the database ***
    */
    /* ================= Start of Delete User ============== */
    public boolean deleteUser(int id){
        boolean validInput = true;
        try (Connection connection = DataBaseConnection.getConnection()){
            String deleteQuery = ("DELETE FROM users " +
                                    "WHERE ID = ?");
            PreparedStatement deleteStm = connection.prepareStatement(deleteQuery);
            deleteStm.setInt(1,id);
            deleteStm.executeUpdate();

            viewUsers();
        } catch (SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "SQL Error: Unable to delete User.", "Error", JOptionPane.ERROR_MESSAGE);
            validInput = false;
        }
        return validInput;
    }
    /* ================= End of Delete User ============== */

    /*
    * *** Method to View all user records from the database ***
    */
    /* ================= Start of View All Users ============== */
    public void viewUsers() { // Start of viewUsers
        try (Connection connection = DataBaseConnection.getConnection()){
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM users");

            myModel.setRowCount(0);
            while (rs.next()) {
                myModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("role_id")
                });
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    /* ================= Start of View All Users ============== */

    /*
    * *** Method to view the last inserted user record ***
    */
    /* ================= Start of View Last Inserted User ============== */
    public void viewInsertedUser(){ // Start of ViewInsertedUser
        try (Connection connection = DataBaseConnection.getConnection()){
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT ID,username, role_id " +
                                                "FROM users " +
                                                "ORDER BY ID DESC " +
                                                "LIMIT 1");

            myModel.setColumnIdentifiers(new String[]{"User ID", "Username", "Role_ID"});
            if (rs.next()){
                myModel.setRowCount(0);
                myModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("username"),
                        rs.getInt("role_id")
                });
            }else {
                JOptionPane.showMessageDialog(null, "No Users were inserted", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    /* ================= Start of View Last Inserted User ============== */

    /*
    *  *** Method to view User record by ID ***
    */
    /* ================= Start of View User By Id ============== */
    public void viewUserById(int id){
        try (Connection connection = DataBaseConnection.getConnection()){

            String selectQuery = ("SELECT ID,username, role_id FROM users WHERE ID = ?");
            PreparedStatement selectStm = connection.prepareStatement(selectQuery);
            selectStm.setInt(1,id);

            ResultSet rs = selectStm.executeQuery();

            myModel.setColumnIdentifiers(new String[]{"User ID", "Username", "Role_ID"});
            if (rs.next()){
                myModel.setRowCount(0);
                myModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("username"),
                        rs.getInt("role_id")
                });
            }else {
                JOptionPane.showMessageDialog(null, "No User was found with the given ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    /* ================= End of View User By Id ============== */

    /* ================= Start os Search User ================ */
    public void searchUser(String username){
        try (Connection connection = DataBaseConnection.getConnection()){
            String searchQuery = ("SELECT * FROM users WHERE username = ?");
            PreparedStatement statement = connection.prepareStatement(searchQuery);
            statement.setString(1,username);
            myModel.setColumnIdentifiers(new String[]{"User ID", "Username", "Role_ID"});

            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                myModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("role_id")
                });
            }else {
                JOptionPane.showMessageDialog(null, "No User was found with the given username", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    /* ================= End os Search User ================ */

    /* ================= Start of Create User =============== */
    public static void createUser(String username, String password) {
        try (Connection connection = DataBaseConnection.getConnection()) {

            if (password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!&^*?]).+$")) {
                if (password.length() >= 10) {
                    String query = ("INSERT INTO users " +
                            "(username, password, role_id) " +
                            "VALUES " +
                            "(?, ?, 6)");
                    PreparedStatement updateStm = connection.prepareStatement(query);
                    updateStm.setString(1, username);
                    updateStm.setString(2, password);
                    updateStm.executeUpdate();
                    JOptionPane.showMessageDialog(null, "User created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Password must be at least 10 characters", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Password Format.\nPassword must contain at least 1 Lowercase Letter, ! Uppercase Letter, 1 Number, and 1 Special Character", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            String duplicateUsername = "Duplicate entry '" + username + "' for key 'users.username_UNIQUE'";

            if (e.getMessage().equals(duplicateUsername)) {
                JOptionPane.showMessageDialog(null, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "SQL Error: Unable to add User.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    /* ================= End of Create User =============== */

} // End of User class
