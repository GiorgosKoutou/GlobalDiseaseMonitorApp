package DiseaseDB;

import Components.TableManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class Continent {

    private int continent_id;
    private String continent_name;

    private TableManager myTable;
    private DefaultTableModel myModel;

    public Continent(int continent_id, String continent_name) {
        this.continent_id = continent_id;
        this.continent_name = continent_name;
    }

    public Continent(TableManager myTableManager) {
        this.myTable = myTableManager;
        myModel = new DefaultTableModel(new String[]{"Continent ID", "Continent Name"}, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0){
                    return false;
                }
                return true;
            }
        };
        myTable.setModel(myModel);
    }

    /* ================== Start of Add Contninent =================== */
    public boolean addContinent(String continent_name) { // Start of AddCountry
        boolean validInput = true;
        try (Connection connection = DataBaseConnection.getConnection()) {
            String insertQuery = ("INSERT INTO continents " +
                    "(continent_name) " +
                    "VALUES " +
                    "(?)");
            PreparedStatement insertStm = connection.prepareStatement(insertQuery);
            insertStm.setString(1, continent_name);
            insertStm.executeUpdate();

            viewContinents();

        } catch (SQLException e) {
            e.printStackTrace();
            String duplicateContinentName = "Duplicate entry '" + continent_name + "' for key 'continents.continent_name_UNIQUE'";
            if (e.getMessage().equals(duplicateContinentName)) {
                JOptionPane.showMessageDialog(null, "Continent name already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                validInput = false;
            }else if (e.getClass().equals(SQLIntegrityConstraintViolationException.class)) {
                JOptionPane.showMessageDialog(null, "Error occurred during the addition", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
                validInput = false;
            } else {
                JOptionPane.showMessageDialog(null, "SQL Error: Unable to add continent.", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }

        }
        return validInput;
    }
    /* ================== End of Add Contninent =================== */

    /*
     * *** Method to update all fields of a country record ***
     */
    /* ================= Start of Update Continent's Name ============= */
    public boolean continentNameUpdate(int id, String continent_name) {
        boolean validInput = true;
        try (Connection connection = DataBaseConnection.getConnection()) {

            String updateQuery = ("UPDATE continents " +
                    "SET continent_name = ? " +
                    "WHERE ID = ?");
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setString(1, continent_name);
            updateStm.setInt(2, id);
            updateStm.executeUpdate();

            viewContinents();

        } catch (SQLException e) {
            if (e.getClass().equals(SQLIntegrityConstraintViolationException.class)) {
                JOptionPane.showMessageDialog(null, "Continent name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                viewContinents();
                validInput = false;
            }else{
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "SQL Error: Unable to update continent name.", "Error", JOptionPane.ERROR_MESSAGE);
                validInput = false;
            }

        }
        return validInput;
    }
    /* ================= Start of Update Continent's Name ============= */

    /*
     * *** Method to delete a country record ***
     */
    /* =============== Start of Delete Continent ============== */
    public boolean deleteContinent(int id) {
        boolean validInput = true;
        try (Connection connection = DataBaseConnection.getConnection()) {
            String deleteQuery = ("DELETE FROM continents " +
                    "WHERE ID = ?");
            PreparedStatement deleteStm = connection.prepareStatement(deleteQuery);
            deleteStm.setInt(1, id);
            deleteStm.executeUpdate();
            deleteStm.close();

            viewContinents();

        } catch (SQLException e) {
            if (e.getClass().equals(SQLIntegrityConstraintViolationException.class)) {
                JOptionPane.showMessageDialog(null, "SQL Constraint Error during delete: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                validInput = false;
            }else {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "SQL Error: Unable to delete continent.", "Error", JOptionPane.ERROR_MESSAGE);
                validInput = false;
            }
        }
        return validInput;
    }
    /* =============== End of Delete Continent ============== */

    /* ================= Start of View Continents ==================== */
    public void viewContinents() {
        try (Connection connection = DataBaseConnection.getConnection()) {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM continents");

            myModel.setRowCount(0);
            while (rs.next()) {
                myModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("continent_name")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "SQL Error: Unable to view continents.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* ================= End of View Continents ==================== */

    /* ================== Start of View The Last Inserted Continent =================== */
    public void viewInsertedContinent() {
        try (Connection connection = DataBaseConnection.getConnection()) {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * " +
                    "FROM continents " +
                    "ORDER BY ID DESC " +
                    "LIMIT 1");

            if (rs.next()) {
                myModel.setRowCount(0);
                myModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("continent_name")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "SQL Error: Unable to view the last inserted continent.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* ================== End of View The Last Inserted Continent =================== */

    /* ================== Start of View Continent By ID =================== */
    public void viewContinentById(int id) { // Start of ViewCountryById
        try (Connection connection = DataBaseConnection.getConnection()) {
            String selectQuery = ("SELECT * FROM continents WHERE ID = ?");
            PreparedStatement selectStm = connection.prepareStatement(selectQuery);
            selectStm.setInt(1, id);
            selectStm.execute();

            ResultSet rs = selectStm.getResultSet();

            if (rs.next()) {
                myModel.setRowCount(0);
                myModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("continent_name")
                });
            } else {
                JOptionPane.showMessageDialog(null, "Country ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "SQL Error: Unable to view continent by ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* ================== End of View Continent By ID =================== */

    /* ================== Start of SearchContinent =================== */
    public void searchContinent(String continent_name){ // Start of SearchCountry

        try (Connection connection = DataBaseConnection.getConnection()) {
            String selectQuery = ("SELECT * FROM continents WHERE LOWER(continent_name) = LOWER(?)");

            PreparedStatement selectStm = connection.prepareStatement(selectQuery);
            selectStm.setString(1, continent_name);

            ResultSet rs = selectStm.executeQuery();

            if(rs.next()) {
                myModel.setRowCount(0);
                myModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("continent_name")
                });
            }else {
                JOptionPane.showMessageDialog(null, "Country not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /* ================== End of SearchCountry =================== */
}
