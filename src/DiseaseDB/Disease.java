package DiseaseDB;

import Components.TableManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Disease { // Start of Disease class
    private int id;
    private String name;
    private String description;
    private Date discoveryDate;

    private TableManager myTable;
    private DefaultTableModel myModel;


    public Disease(int in_id, String in_name, String in_description, Date in_discoveryDate) {
        this.id = in_id;
        this.name = in_name;
        this.description = in_description;
        this.discoveryDate = in_discoveryDate;
    }

    public Disease(TableManager myTable) {
        this.myTable = myTable;
        myModel = new DefaultTableModel(new String[]{"ID", "Disease Name", "Disease Description", "Disease Discovery Date"}, 0);
        myTable.setModel(myModel);
    }

    /*
    * *** Method to add a disease record to the database ***
    */
    /* ================= Start of Add Disease Method =============== */
    public boolean addDisease(String name, String description, Date discoveryDate){
        boolean validInput = true;
        try (Connection connection = DataBaseConnection.getConnection()){
            String insertQuery = ("INSERT INTO diseases " +
                                    "(name, description, discovery_date) " +
                                    "VALUES " +
                                    "(?, ?, ?)");

            PreparedStatement insertStm = connection.prepareStatement(insertQuery);
            insertStm.setString(1,name);
            insertStm.setString(2,description);
            insertStm.setDate(3, discoveryDate);
            insertStm.executeUpdate();
            insertStm.close();

            viewAllDiseases();
        } catch (SQLException e){
            e.printStackTrace();
            String duplicateCountryname = "Duplicate entry '" + name + "' for key 'diseases.name_UNIQUE'";
            if (e.getMessage().equals(duplicateCountryname)){
                JOptionPane.showMessageDialog(null, "Country name already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                validInput = false;
            }else if (e.getClass().equals(SQLIntegrityConstraintViolationException.class)){
                JOptionPane.showMessageDialog(null, "Error occured during the addition", "Error", JOptionPane.ERROR_MESSAGE);
                validInput = false;
            }else {
                JOptionPane.showMessageDialog(null, "SQL Error: Unable to add continent.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return validInput;
    }
    /* ================= End of Add Disease Method =============== */

    /*
    * *** Method to update all fields of a disease record ***
    */
    /* ==================== Start of Update all disease fields ================ */
    public boolean diseaseAllFieldsUpdate(int id, String name, String desc, String discoveryDate){
        boolean validInput = true;
        try (Connection connection = DataBaseConnection.getConnection()){
            String updateQuery = ("UPDATE diseases " +
                                    "SET name = ?, " +
                                    "description = ?, " +
                                    "discovery_date = ? " +
                                  "WHERE ID = ?");

            DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date date = inputFormat.parse(discoveryDate);
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setString(1, name);
            updateStm.setString(2,desc);
            updateStm.setDate(3,sqlDate);
            updateStm.setInt(4,id);
            updateStm.executeUpdate();

            viewDiseaseById(id);

        }catch (SQLException | ParseException e){
            if (e.getClass().equals(ParseException.class)){
                JOptionPane.showMessageDialog(null, "Invalid date format!The right format is: dd/mm/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
                validInput = false;
            }else {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "SQL Error during update", "Error", JOptionPane.ERROR_MESSAGE);
                validInput = false;
            }
        }
        return validInput;
    }
    /* ==================== End of Update all disease fields ================ */

    /*
    * *** Method to update the name of a disease record ***
    */
    /* =================== Start of Disease Name Update ==================== */
    public void diseaseNameUpdate(int id, String name){
        try (Connection connection = DataBaseConnection.getConnection()){
            String updateQuery = ("UPDATE diseases " +
                                     "SET name = ? " +
                                   "WHERE ID = ?");

            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setString(1, name);
            updateStm.setInt(2,id);
            updateStm.executeUpdate();
            updateStm.close();
            System.out.println("Update Complete");

            viewDiseaseById(id);

        }catch (SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "SQL Error during update", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* =================== End of Disease Name Update ==================== */

    /*
    * *** Method to update the description of a disease record ***
    */
    /* ================== Start of Disease Desc Update ================== */
    public void diseaseDescUpdate(int id, String desc){ // Start of DiseaseDescUpdate
        try (Connection connection = DataBaseConnection.getConnection()){
            String updateQuery = ("UPDATE diseases SET description = ? " +
                                    "WHERE ID = ?");

            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setString(1, desc);
            updateStm.setInt(2,id);
            updateStm.executeUpdate();
            updateStm.close();

            viewDiseaseById(id);

        }catch (SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "SQL Error during update", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* ================== End of Disease Desc Update ================== */

    /*
    * *** Method to update the discovery date of a disease record ***
    */
    /* ================ Start of Disease Discovery Date Update ============== */
    public void diseaseDiscoveryDateUpdate(int id, Date discoveryDate){ // Start of DiseaseDiscoveryDateUpdate
        try (Connection connection = DataBaseConnection.getConnection()){
            String updateQuery = ("UPDATE diseases SET discovery_date = ? " +
                                    "WHERE ID = ?");

            java.sql.Date sqlDate = new java.sql.Date(discoveryDate.getTime());

            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setDate(1, sqlDate);
            updateStm.setInt(2,id);
            updateStm.executeUpdate();
            updateStm.close();

            viewDiseaseById(id);;

        }catch (SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "SQL Error during update", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* ================ End of Disease Discovery Date Update ============== */

    /*
    * *** Method to delete a disease record by ID ***
    */
    /* ============ Start of Delete a disease ================ */
    public boolean deleteDisease(int id){
        boolean validInput = true;
        try (Connection connection = DataBaseConnection.getConnection()){
            String deleteQuery = ("DELETE FROM diseases " +
                                    "WHERE ID = ?");
            PreparedStatement deleteStm = connection.prepareStatement(deleteQuery);
            deleteStm.setInt(1,id);
            deleteStm.executeUpdate();
            deleteStm.close();

            viewAllDiseases();

        } catch (SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "SQL Error: Unable to delete country.", "Error", JOptionPane.ERROR_MESSAGE);
            validInput = false;
        }
        return validInput;
    }
    /* ============ End of Delete a disease ================ */

    /*
    *  *** Method to view all disease records from the database ***
    */
    /* ================ Start of View all diseases =========== */
    public void viewAllDiseases() {
        try (Connection connection = DataBaseConnection.getConnection()){
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM diseases");

            myModel.setRowCount(0);
            while (rs.next()) {
                Date date = rs.getDate("discovery_date");
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = dateFormat.format(date);
                myModel.addRow(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                       formattedDate
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "SQL Error during the upload of the data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* ================ End of View all diseases =========== */

    /*
    * *** Method to view the last inserted disease record ***
    */
    /* ================ Start of view inserted disease ========== */
    public void viewInsertedDisease() {
        try ( Connection connection = DataBaseConnection.getConnection()){
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * " +
                                                "FROM diseases " +
                                                "ORDER BY ID DESC " +
                                                "LIMIT 1");

            if(rs.next()) {
                myModel.setRowCount(0);
                Date date = rs.getDate("discovery_date");
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = dateFormat.format(date);
                myModel.addRow(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        formattedDate
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "SQL Error during fetching inserted country", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* ================ End of view inserted disease ========== */

    /*
    * *** Method to view a specific disease record from the database ***
    */
    /* ============ Strat of view Disease by id ============= */
    public void viewDiseaseById(int id){
        try ( Connection connection = DataBaseConnection.getConnection()){
            String selectQuery = ("SELECT * FROM diseases WHERE ID = ?");
            PreparedStatement selectStm = connection.prepareStatement(selectQuery);
            selectStm.setInt(1,id);
            selectStm.execute();

            ResultSet rs = selectStm.getResultSet();

            if(rs.next()) {
                myModel.setRowCount(0);
                Date date = rs.getDate("discovery_date");
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = dateFormat.format(date);
                myModel.addRow(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        formattedDate
                });
            }

        }catch (SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "SQL Error during fetching by ID", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* ============ End of view Disease by id ============= */

    /*
     *** Method for search a disease by name
     */
    /* ============ Start of search disease ============== */
    public void searchDisease (String  diseaseName){
        try (Connection connection = DataBaseConnection.getConnection()) {
            String selectQuery = ("SELECT * FROM diseases WHERE LOWER(name) = LOWER(?)");

            PreparedStatement selectStm = connection.prepareStatement(selectQuery);
            selectStm.setString(1,diseaseName);

            ResultSet rs = selectStm.executeQuery();

            if(rs.next()) {
                myModel.setRowCount(0);
                myModel.addRow(new Object[]{
                       rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDate(4)
                });
            }else {
                JOptionPane.showMessageDialog(null, "Disease not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "SQL Error during search", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* ============ End of search disease ============== */

} // End of Disease class
