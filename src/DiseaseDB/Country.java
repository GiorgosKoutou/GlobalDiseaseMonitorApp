package DiseaseDB;

import Components.TableManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class Country { // Start of Countries class

    private int id;
    private String countryName;
    private String continentName;
    private long population;

    private TableManager myTable;
    private static DefaultTableModel myModel;

    // *** Constructors *** //
    public Country(int id, String countryName, String continentName, long population) {
        this.id = id;
        this.countryName = countryName;
        this.continentName = continentName;
        this.population = population;
    }

    public Country(TableManager table) {
        this.myTable = table;
        this.myModel = new DefaultTableModel(new String[]{"ID", "Country Name", "Continent ID", "Population"}, 0) {
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

    /* ================== Start of Add Country =================== */
    public boolean addCountry(String countryName, int continentID, long population) { // Start of AddCountry
        boolean validInput = true;
        try (Connection connection = DataBaseConnection.getConnection()) {
            String insertQuery = ("INSERT INTO countries " +
                    "(country_name, continent_id, population) " +
                    "VALUES " +
                    "(?, ?, ?)");
            PreparedStatement insertStm = connection.prepareStatement(insertQuery);
            insertStm.setString(1, countryName);
            insertStm.setInt(2, continentID);
            insertStm.setLong(3, population);
            insertStm.executeUpdate();

            viewInsertedCountry();

        } catch (SQLException e) {
            e.printStackTrace();
            String duplicateCountryname = "Duplicate entry '" + countryName + "' for key 'countries.country_name_UNIQUE'";
            if (e.getMessage().equals(duplicateCountryname)) {
                JOptionPane.showMessageDialog(null, "Country name already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                validInput = false;
            } else if (e.getClass().equals(SQLIntegrityConstraintViolationException.class)) {
                JOptionPane.showMessageDialog(null, "Error occurred during the addition", "Error", JOptionPane.ERROR_MESSAGE);
                validInput = false;
            } else {
                JOptionPane.showMessageDialog(null, "SQL Error: Unable to add country.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return validInput;
    }
    /* ================== End of Add Country =================== */

    /* ================= Start of Update Country's All Fields ============= */
    public boolean countryAllFieldsUpdate(int id, String countryName, int continentId, long population) {
        boolean validInput = true;
        try (Connection connection = DataBaseConnection.getConnection()) {

            String updateQuery = ("UPDATE countries " +
                    "SET country_name = ?, " +
                    "continent_id = ?, " +
                    "population = ? " +
                    "WHERE ID = ?");
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setString(1, countryName);
            updateStm.setInt(2, continentId);
            updateStm.setLong(3, population);
            updateStm.setInt(4, id);
            updateStm.executeUpdate();

            viewCountries();

        } catch (SQLException e) {
            if (e.getClass().equals(SQLIntegrityConstraintViolationException.class)) {
                String message = e.getMessage();

                if (message.equals("Column 'country_name' cannot be null")) {
                    JOptionPane.showMessageDialog(null, "Country name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    validInput = false;
                }else {
                    JOptionPane.showMessageDialog(null, "SQL Constraint Error during update. There is no Continent with ID: " + continentId ,"Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println(e.getMessage());
                    validInput = false;
                }
            } else {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "SQL Error during update: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                validInput = false;
            }
        }
        return validInput;
    }
    /* ================= Start of Update Country's All Fields ============= */

    /* ================= Start of CountryNameUpdate ============= */
    public void countryNameUpdate(int id, String countryName) {
        try (Connection connection = DataBaseConnection.getConnection()) {
            String updateQuery = ("UPDATE countries " +
                    "SET country_name = ? " +
                    "WHERE ID = ?");
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setString(1, countryName);
            updateStm.setInt(2, id);
            updateStm.executeUpdate();

            viewCountryById(id);

        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getClass().equals(SQLIntegrityConstraintViolationException.class)) {
                JOptionPane.showMessageDialog(null, "SQL Constraint Error during name update: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "SQL Error during name update: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    /* ================= End of CountryNameUpdate ============= */

    /* ================= Start of ContinentIdUpdate ============= */
    public void continentIdUpdate(int id, int continentId) {
        try (Connection connection = DataBaseConnection.getConnection()) {
            String updateQuery = ("UPDATE countries " +
                    "SET continent_id = ? " +
                    "WHERE ID = ?");
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setInt(1, continentId);
            updateStm.setInt(2, id);
            updateStm.executeUpdate();
            updateStm.close();

            viewCountryById(id);

        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getClass().equals(SQLIntegrityConstraintViolationException.class)) {
                JOptionPane.showMessageDialog(null, "SQL Constraint Error during continent update: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "SQL Error during continent update: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    /* ================= End of ContinentIdUpdate ============= */

    /* ================= Start of PopulationUpdate ============= */
    public void populationUpdate(int id, long population) {
        try (Connection connection = DataBaseConnection.getConnection()) {
            String updateQuery = ("UPDATE countries " +
                    "SET population = ? " +
                    "WHERE ID = ?");
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setLong(1, population);
            updateStm.setInt(2, id);
            updateStm.executeUpdate();
            updateStm.close();

            viewCountryById(id);

        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getClass().equals(SQLIntegrityConstraintViolationException.class)) {
                JOptionPane.showMessageDialog(null, "SQL Constraint Error during population update: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "SQL Error during population update: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    /* ================= End of PopulationUpdate ============= */

    /* ================= Start of DeleteCountry ============= */
    public boolean deleteCountry(int id) {
        boolean validInput = true;
        try (Connection connection = DataBaseConnection.getConnection()) {
            String deleteQuery = ("DELETE FROM countries " +
                    "WHERE ID = ?");
            PreparedStatement deleteStm = connection.prepareStatement(deleteQuery);
            deleteStm.setInt(1, id);
            deleteStm.executeUpdate();
            deleteStm.close();
            System.out.println("Delete Successful");

            viewCountries();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "SQL Error: Unable to delete continent.", "Error", JOptionPane.ERROR_MESSAGE);
            validInput = false;
        }
        return validInput;
    }
    /* ================= End of DeleteCountry ============= */

    /* ================= Start of ViewCountries ============= */
    public void viewCountries() {
        try (Connection connection = DataBaseConnection.getConnection()) {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM countries");

            myModel.setRowCount(0);
            while (rs.next()) {
                myModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("country_name"),
                        rs.getInt("continent_id"),
                        rs.getLong("population")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "SQL Error during the upload of the data", "Error", JOptionPane.ERROR_MESSAGE);

        }
    }
    /* ================= End of ViewCountries ============= */

    /* ================= Start of ViewInsertedCountry ============= */
    public void viewInsertedCountry() {
        try (Connection connection = DataBaseConnection.getConnection()) {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * " +
                    "FROM countries " +
                    "ORDER BY ID DESC " +
                    "LIMIT 1");

            if (rs.next()) {
                myModel.setRowCount(0);
                myModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("country_name"),
                        rs.getInt("continent_id"),
                        rs.getLong("population")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "SQL Error during fetching inserted country", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* ================= End of ViewInsertedCountry ============= */

    /* ================= Start of ViewCountryById ============= */
    public void viewCountryById(int id) {
        try (Connection connection = DataBaseConnection.getConnection()) {
            String selectQuery = ("SELECT * FROM countries WHERE ID = ?");
            PreparedStatement selectStm = connection.prepareStatement(selectQuery);
            selectStm.setInt(1, id);
            selectStm.execute();

            ResultSet rs = selectStm.getResultSet();

            if (rs.next()) {
                myModel.setRowCount(0);
                myModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("country_name"),
                        rs.getInt("continent_id"),
                        rs.getLong("population")
                });
            } else {
                JOptionPane.showMessageDialog(null, "Country ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "SQL Error during fetching by ID", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* ================= End of ViewCountryById ============= */

    /* ================= Start of SearchCountry ============= */
    public void searchCountry(String countryName) {
        try (Connection connection = DataBaseConnection.getConnection()) {
            String selectQuery = ("SELECT * FROM countries WHERE LOWER(country_name) = LOWER(?)");

            PreparedStatement selectStm = connection.prepareStatement(selectQuery);
            selectStm.setString(1, countryName);

            ResultSet rs = selectStm.executeQuery();

            if (rs.next()) {
                myModel.setRowCount(0);
                myModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("country_name"),
                        rs.getInt("continent_id"),
                        rs.getLong("population")
                });
            } else {
                JOptionPane.showMessageDialog(null, "Country not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "SQL Error during search", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* ================= End of SearchCountry ============= */
} // End of Country class
