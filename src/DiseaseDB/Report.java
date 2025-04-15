package DiseaseDB;


import Components.TableManager;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import  java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import javax.swing.JOptionPane;

public class Report {

    private int id;
    private int userId;
    private int diseaseId;
    private int countryId;
    private String comments;
    private Date reportDate;

    private TableManager myTable;
    private static DefaultTableModel myModel;

    public Report(int id, int userId, int diseaseId, int countryId, String comments, Date reportDate) {
        this.id = id;
        this.userId = userId;
        this.diseaseId = diseaseId;
        this.countryId = countryId;
        this.comments = comments;
        this.reportDate = reportDate;
    }

    public Report(TableManager table) {
        this.myTable = table;
        myModel = new DefaultTableModel(new String[]{"Report ID", "User ID", "Disease ID", "Country ID", "Comments", "Report Date/Time"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0 || column == 5) {
                    return false;
                }
                return true;
            }
        };
        this.myTable.setModel(myModel);
    }

    /* ===================== Start of Add Report ==================== */
    public boolean addReport(int userId, int diseaseId, int countryId, String comments) {
        boolean validInput = true;
        try (Connection connection = DataBaseConnection.getConnection()) {
            String insertQuery = "INSERT INTO reports " +
                    "(user_id, disease_id, country_id, comments, report_date) " +
                    "VALUES " +
                    "(?, ?, ?, ?, CURRENT_TIMESTAMP)";
            PreparedStatement insertStm = connection.prepareStatement(insertQuery);
            insertStm.setInt(1, userId);
            insertStm.setInt(2, diseaseId);
            insertStm.setInt(3, countryId);
            insertStm.setString(4, comments);
            insertStm.executeUpdate();
            insertStm.close();

            viewInsertedReport();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error while adding the report", "Error", JOptionPane.ERROR_MESSAGE);
            validInput = false;
            e.printStackTrace();
        }
        return validInput;
    }
    /* ===================== End of Add Report ==================== */

    /* ================ Start of Import Csv to Database =================== */
    public void importCsvToReports(File file) throws SQLException {
        Connection connection = null;
        int dateCount = 0;
        int count = 0;
        try (CSVReader csvReader = new CSVReader(new FileReader(file))){
            connection = DataBaseConnection.getConnection();
            connection.setAutoCommit(false);
            String insertQuery = ("INSERT INTO reports " +
                    "(user_id, disease_id, country_id, comments, report_date) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?)");

            String[] nextRecord;
            csvReader.readNext();

            PreparedStatement insertStm = connection.prepareStatement(insertQuery);

            insertStm.clearBatch();
            while ((nextRecord = csvReader.readNext()) != null){
                int userId = Integer.parseInt(nextRecord[0]);
                int diseaseId = Integer.parseInt(nextRecord[1]);
                int countryId = Integer.parseInt(nextRecord[2]);
                String comments = nextRecord[3];
                String csvDateTime = nextRecord[4];
                LocalDateTime dateTime = null;
                Timestamp sqlDateTime = null;

                String[] formats = {"dd/MM/yyyy HH:mm:ss", "dd/MM/yyyy hh:mm:ss a",
                        "dd-MM-yyyy HH:mm:ss", "dd-MM-yyyy hh:mm:ss a",
                        "MM/dd/yyyy HH:mm:ss", "MM/dd/yyyy hh:mm:ss a",
                        "MM-dd-yyyy HH:mm:ss", "MM-dd-yyyy hh:mm:ss a",
                        "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd hh:mm:ss a",
                        "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd hh:mm:ss a"};
                for (String format : formats){
                    try {
                        DateTimeFormatter inputFormats = DateTimeFormatter.ofPattern(format).withLocale(Locale.US);
                        dateTime = LocalDateTime.parse(csvDateTime, inputFormats);
                        dateCount++;
                        break;
                    } catch (DateTimeParseException ex){
                    }
                }
                try {
                    if (dateTime == null) {
                        JOptionPane.showMessageDialog(null, "Invalid date format in line " + dateCount + " of CSV file.", "Error", JOptionPane.ERROR_MESSAGE);
                        connection.rollback();
                        return;
                    }
                    sqlDateTime = Timestamp.valueOf(dateTime);
                }catch (NullPointerException e){
                    JOptionPane.showMessageDialog(null, "Unexpected error while processing date in line " + dateCount, "Error", JOptionPane.ERROR_MESSAGE);
                    connection.rollback();
                    return;
                }


                insertStm.setInt(1,userId);
                insertStm.setInt(2,diseaseId);
                insertStm.setInt(3,countryId);
                insertStm.setString(4,comments);
                insertStm.setTimestamp(5,sqlDateTime);
                insertStm.addBatch();
                count++;

            }

            int[] updatedRows = insertStm.executeBatch();
            for (int row : updatedRows){
                if (row == Statement.EXECUTE_FAILED) {
                    connection.rollback();
                    JOptionPane.showMessageDialog(null, "Data import failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            connection.commit();
            connection.close();
            insertStm.close();
            JOptionPane.showMessageDialog(null, "Data imported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        }catch (SQLException e) {
            if (e.getClass().equals(BatchUpdateException.class)){
                String foreignKey = e.getMessage();
                if (foreignKey.contains("report_user_FK")) {
                    JOptionPane.showMessageDialog(null, "Foreign key error: No User with given ID exists in line " + count + "of CSV file.", "Error", JOptionPane.ERROR_MESSAGE);
                    connection.rollback();
                } else if (foreignKey.contains("report_disease_FK")) {
                    JOptionPane.showMessageDialog(null, "Foreign key error: No Disease with given ID exists in line " + count + "of CSV file.", "Error", JOptionPane.ERROR_MESSAGE);
                    connection.rollback();
                }else if (foreignKey.contains("report_country_FK")) {
                    JOptionPane.showMessageDialog(null, "Foreign key error: No Country with given ID exists in line " + count + "of CSV file.", "Error", JOptionPane.ERROR_MESSAGE);
                    connection.rollback();
                }
            }else {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "SQL Error during import", "Error", JOptionPane.ERROR_MESSAGE);
                connection.rollback();
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "File Error: Unable to read the file.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (CsvValidationException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "CSV Validation Error: Invalid data in the file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* ================ End of Import Csv to Database =================== */

    /* ===================== Start of Update Report's All Fields ==================== */
    public boolean updateReportAllFields(int id, int userId, int diseaseId, int countryId, String comments) {
        boolean validInput = true;
        try (Connection connection = DataBaseConnection.getConnection()) {
            String updateQuery = "UPDATE reports SET " +
                    "user_id = ?, " +
                    "disease_id = ?, " +
                    "country_id = ?, " +
                    "comments = ? " +
                    "WHERE ID = ?";
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setInt(1, userId);
            updateStm.setInt(2, diseaseId);
            updateStm.setInt(3, countryId);
            updateStm.setString(4, comments);
            updateStm.setInt(5, id);
            updateStm.executeUpdate();
            updateStm.close();

            viewReportById(id);
        } catch (SQLException e) {
            if (e.getClass().equals(SQLIntegrityConstraintViolationException.class)) {
                String foreignKey = e.getMessage();

                if (foreignKey.contains("report_user_FK")){
                    JOptionPane.showMessageDialog(null, "Constraint violation.There is no user with ID: " + userId, "Constraint Error", JOptionPane.ERROR_MESSAGE);
                    validInput = false;
                }else if (foreignKey.contains("report_disease_FK")){
                    JOptionPane.showMessageDialog(null,"Constraint violation.There is no disease with ID: " + diseaseId, "Constraint Error", JOptionPane.ERROR_MESSAGE);
                    validInput = false;
                }else if (foreignKey.contains("report_country_FK")){
                    JOptionPane.showMessageDialog(null,"Constraint violation.There is no country with ID: " + countryId, "Constraint Error", JOptionPane.ERROR_MESSAGE);
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
    /* ===================== End of Update Report's All Fields ==================== */

    /* ===================== Start of Update Report's User Id ==================== */
    public void updateReportUserId(int id, int userId) {
        try (Connection connection = DataBaseConnection.getConnection()) {
            String updateQuery = "UPDATE reports SET user_id = ? WHERE ID = ?";
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setInt(1, userId);
            updateStm.setInt(2, id);
            updateStm.executeUpdate();
            updateStm.close();

            viewReportById(id);
        } catch (SQLException e) {
            if (e.getClass().equals(SQLIntegrityConstraintViolationException.class)) {
                JOptionPane.showMessageDialog(null, "Constraint violation while updating user ID: " + e.getMessage(), "Constraint Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error while updating user ID: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        }
    }
    /* ===================== End of Update Report's User Id ==================== */

    /* ===================== Start of Update Report's Disease Id ==================== */
    public void updateReportDiseaseId(int id, int diseaseId) {
        try (Connection connection = DataBaseConnection.getConnection()) {
            String updateQuery = "UPDATE reports SET disease_id = ? WHERE ID = ?";
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setInt(1, diseaseId);
            updateStm.setInt(2, id);
            updateStm.executeUpdate();
            updateStm.close();

            viewReportById(id);
        } catch (SQLException e) {
            if (e.getClass().equals(SQLIntegrityConstraintViolationException.class)) {
                JOptionPane.showMessageDialog(null, "Constraint violation while updating disease ID: " + e.getMessage(), "Constraint Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error while updating disease ID: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        }
    }
    /* ===================== End of Update Report's Disease Id ==================== */

    /* ===================== Start of Update Report's Country Id ==================== */
    public void updateReportCountryId(int id, int countryId) {
        try (Connection connection = DataBaseConnection.getConnection()) {
            String updateQuery = "UPDATE reports SET country_id = ? WHERE ID = ?";
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setInt(1, countryId);
            updateStm.setInt(2, id);
            updateStm.executeUpdate();
            updateStm.close();

            viewReportById(id);
        } catch (SQLException e) {
            if (e.getClass().equals(SQLIntegrityConstraintViolationException.class)) {
                JOptionPane.showMessageDialog(null, "Constraint violation while updating country ID: " + e.getMessage(), "Constraint Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error while updating country ID: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        }
    }
    /* ===================== End of Update Report's Country Id ==================== */

    /* ===================== Start of Update Report's Comments ==================== */
    public void updateReportComments(int id, String comments) {
        try (Connection connection = DataBaseConnection.getConnection()) {
            String updateQuery = "UPDATE reports SET comments = ? WHERE ID = ?";
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setString(1, comments);
            updateStm.setInt(2, id);
            updateStm.executeUpdate();
            updateStm.close();

            viewReportById(id);
        } catch (SQLException e) {
            if (e.getClass().equals(SQLIntegrityConstraintViolationException.class)) {
                JOptionPane.showMessageDialog(null, "Constraint violation while updating comments: " + e.getMessage(), "Constraint Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error while updating comments: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        }
    }
    /* ===================== Start of Update Report's Comments ==================== */

    /* ===================== Start of Update Report's Date ==================== */
    public void updateReportReportDate(int id, Date reportDate) {
        try (Connection connection = DataBaseConnection.getConnection()) {
            String updateQuery = "UPDATE reports SET report_date = ? WHERE ID = ?";
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setDate(1, reportDate);
            updateStm.setInt(2, id);
            updateStm.executeUpdate();
            updateStm.close();

            viewReportById(id);
        } catch (SQLException e) {
            if (e.getClass().equals(SQLIntegrityConstraintViolationException.class)) {
                JOptionPane.showMessageDialog(null, "Constraint violation while updating report date: " + e.getMessage(), "Constraint Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error while updating report date: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        }
    }
    /* ===================== End of Update Report's Date ==================== */

    /* ===================== Start of Delete Report ==================== */
    public boolean deleteReport(int id) {
        boolean validInput = true;
        try (Connection connection = DataBaseConnection.getConnection()) {
            String deleteQuery = ("DELETE FROM reports " +
                    "WHERE ID = ?");
            PreparedStatement deleteStm = connection.prepareStatement(deleteQuery);
            deleteStm.setInt(1, id);
            deleteStm.executeUpdate();
            deleteStm.close();
            System.out.println("Delete Successful");

            viewAllReports();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error while deleting report", "Error", JOptionPane.ERROR_MESSAGE);
            validInput = false;
            e.printStackTrace();
        }
        return validInput;
    }
    /* ===================== End of Delete Report ==================== */

    /* ===================== Start of View All Report ==================== */
    public void viewAllReports() {
        try (Connection connection = DataBaseConnection.getConnection()) {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM reports");

            myModel.setRowCount(0);
            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("report_date");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/mm/yyyy HH:mm:ss");
                String formattedDate = timestamp.toLocalDateTime().format(formatter);
                myModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getInt("user_id"),
                        rs.getInt("disease_id"),
                        rs.getInt("country_id"),
                        rs.getString("comments"),
                        formattedDate
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error while viewing all reports: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    /* ===================== End of View All Report ==================== */

    /* ===================== Start of View Last Inserted Report ==================== */
    public void viewInsertedReport() {
        try (Connection connection = DataBaseConnection.getConnection()) {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM reports ORDER BY ID DESC LIMIT 1");

            if (rs.next()) {
                myModel.setRowCount(0);
                myModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getInt("user_id"),
                        rs.getInt("disease_id"),
                        rs.getInt("country_id"),
                        rs.getString("comments"),
                        rs.getDate("report_date")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error while viewing last inserted report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    /* ===================== End of View Last Inserted Report ==================== */

    /* ===================== Start of View Report by Id ==================== */
    public void viewReportById(int id) {
        try (Connection connection = DataBaseConnection.getConnection()) {
            String selectQuery = ("SELECT * FROM reports WHERE ID = ?");
            PreparedStatement selectStm = connection.prepareStatement(selectQuery);
            selectStm.setInt(1, id);

            ResultSet rs = selectStm.executeQuery();

            if (rs.next()) {
                myModel.setRowCount(0);
                myModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getInt("user_id"),
                        rs.getInt("disease_id"),
                        rs.getInt("country_id"),
                        rs.getString("comments"),
                        rs.getDate("report_date")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error while viewing report by ID: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    /* ===================== Start of View Report by Id ==================== */

    /* ================== Start of Search Report ID =================== */
    public void searchReport(int id){ // Start of SearchCountry

        try (Connection connection = DataBaseConnection.getConnection()) {
            String selectQuery = ("SELECT * FROM reports " +
                    "WHERE ID = ?");

            PreparedStatement selectStm = connection.prepareStatement(selectQuery);
            selectStm.setInt(1,id);

            ResultSet rs = selectStm.executeQuery();

            if(rs.next()) {
                Timestamp timestamp = rs.getTimestamp("report_date");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/mm/yyyy HH:mm:ss");
                String formattedDate = timestamp.toLocalDateTime().format(formatter);
                myModel.setRowCount(0);
                myModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getInt("user_id"),
                        rs.getInt("disease_id"),
                        rs.getInt("country_id"),
                        rs.getString("comments"),
                        formattedDate
                });
            }else {
                JOptionPane.showMessageDialog(null, "Data not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /* ================== End of Search Report ID =================== */

}
