package DiseaseDB;

import Components.TableManager;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DiseaseCases { // Start of DiseaseCases class
    private int id;
    private int diseaseId;
    private int countryId;
    private int cases;
    private int deaths;
    private Date caseReportDate;

    private TableManager myTable;
    private static DefaultTableModel myModel;

    /* *** Constructors *** */
    public DiseaseCases(int id, int diseaseId, int countryId, int cases, int deaths, Date caseReportDate) {
        this.id = id;
        this.diseaseId = diseaseId;
        this.countryId = countryId;
        this.cases = cases;
        this.deaths = deaths;
        this.caseReportDate = caseReportDate;
    }

    public DiseaseCases(TableManager table) {
        this.myTable = table;
        myModel = new DefaultTableModel(new String[]{"ID", "Disease ID", "Country ID", "Cases", "Deaths", "Case Report Date"}, 0) {
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

    /* ================= Start Of Add Disease Case Method =================== */
    public boolean addDiseaseCase(int diseaseId, int countryId, int cases, int deaths, Date caseReportDate) {
        boolean validInput = true;
        try (Connection connection = DataBaseConnection.getConnection()) {
            String insertQuery = ("INSERT INTO disease_cases " +
                    "(disease_id, country_id, cases, deaths, case_report_date) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?)");
            PreparedStatement insertStm = connection.prepareStatement(insertQuery);
            insertStm.setInt(1, diseaseId);
            insertStm.setInt(2, countryId);
            insertStm.setInt(3, cases);
            insertStm.setInt(4, deaths);
            insertStm.setDate(5, caseReportDate);
            insertStm.executeUpdate();
            insertStm.close();

            viewInsertedDiseaseCase();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "SQL Error: Unable to add Report Case.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return validInput;
    }
    /* ================= End Of Add Disease Case Method =================== */

    /* ================ Start of Import Csv to Database =================== */
    public void importCsvToDiseaseCases(File file) throws SQLException {
        Connection connection = null;
        int count = 0;
        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            connection = DataBaseConnection.getConnection();

            connection.setAutoCommit(false);

            String insertQuery = "INSERT INTO disease_cases " +
                    "(disease_id, country_id, cases, deaths, case_report_date) " +
                    "VALUES (?, ?, ?, ?, ?)";

            String[] nextRecord;
            csvReader.readNext();

            try (PreparedStatement insertStm = connection.prepareStatement(insertQuery)) {

                while ((nextRecord = csvReader.readNext()) != null) {
                    int diseaseId = Integer.parseInt(nextRecord[0]);
                    int countryId = Integer.parseInt(nextRecord[1]);
                    int cases = Integer.parseInt(nextRecord[2]);
                    int deaths = Integer.parseInt(nextRecord[3]);
                    String csvDate = nextRecord[4];

                    java.util.Date date = null;
                    String[] formats = {"dd/MM/yyyy", "dd-MM-yyyy", "MM/dd/yyyy", "MM-dd-yyyy", "yyyy/MM/dd", "yyyy-MM-dd"};
                    for (String format : formats) {
                        try {
                            DateFormat inputFormats = new SimpleDateFormat(format);
                            date = inputFormats.parse(csvDate);
                            break;
                        } catch (ParseException e) {
                        }
                    }

                    if (date == null) {
                        JOptionPane.showMessageDialog(null, "Invalid date format for row: " + String.join(",", nextRecord), "Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    java.sql.Date sqlDate = new Date(date.getTime());

                    insertStm.setInt(1, diseaseId);
                    insertStm.setInt(2, countryId);
                    insertStm.setInt(3, cases);
                    insertStm.setInt(4, deaths);
                    insertStm.setDate(5, sqlDate);
                    insertStm.addBatch();
                    count++;
                }

                insertStm.executeBatch();
                connection.commit();
                connection.close();
                insertStm.close();
                JOptionPane.showMessageDialog(null, "Data imported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            if (e.getClass().equals(BatchUpdateException.class)){
                String foreignKey = e.getMessage();
                if (foreignKey.contains("case_disease_id")) {
                    JOptionPane.showMessageDialog(null, "Foreign key error: No Disease with given ID exists in line " + count + " of CSV file.", "Error", JOptionPane.ERROR_MESSAGE);
                    connection.rollback();
                } else if (foreignKey.contains("case_country_id")) {
                    JOptionPane.showMessageDialog(null, "Foreign key error: No Country with given ID exists in line " + count + " of CSV file.", "Error", JOptionPane.ERROR_MESSAGE);
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

    /* ================== Start of Update Disease Case All Fields ============== */
    public boolean updateDiseaseCaseAllFields(int id, int diseaseId, int countryId, int cases, int deaths, String caseReportDate) {
        boolean validInput = true;
        try (Connection connection = DataBaseConnection.getConnection()) {
            String updateQuery = ("UPDATE disease_cases SET " +
                    "disease_id = ?, " +
                    "country_id = ?, " +
                    "cases = ?, " +
                    "deaths = ?, " +
                    "case_report_date = ? " +
                    "WHERE ID = ?");

            DateFormat inputFormats = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date date = inputFormats.parse(caseReportDate);
            java.sql.Date sqlDate = new Date(date.getTime());

            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setInt(1, diseaseId);
            updateStm.setInt(2, countryId);
            updateStm.setInt(3, cases);
            updateStm.setInt(4, deaths);
            updateStm.setDate(5, sqlDate);
            updateStm.setInt(6, id);
            updateStm.executeUpdate();
            updateStm.close();

            viewDiseaseCaseById(id);

        } catch (SQLException | ParseException | NullPointerException e) {
            if (e.getClass().equals(ParseException.class)) {
                JOptionPane.showMessageDialog(null, "Invalid date format!", "Error", JOptionPane.ERROR_MESSAGE);
                validInput = false;

            }else if (e.getClass().equals(NullPointerException.class)) {
                JOptionPane.showMessageDialog(null, "Please fill the date", "Error", JOptionPane.ERROR_MESSAGE);
                validInput = false;

            } else if (e.getClass().equals(SQLIntegrityConstraintViolationException.class)) {
                String foreignKey = e.getMessage();

                if (foreignKey.contains("case_disease_id")){
                    JOptionPane.showMessageDialog(null,"There is no Disease with ID: " + diseaseId, "Error", JOptionPane.ERROR_MESSAGE);
                    validInput = false;

                }else if (foreignKey.contains("case_country_id")){
                    JOptionPane.showMessageDialog(null, "There is no country with ID: " + countryId, "Error", JOptionPane.ERROR_MESSAGE);
                    validInput = false;
                }
            } else {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "SQL Error during update", "Error", JOptionPane.ERROR_MESSAGE);
                validInput = false;
            }
        }
        return validInput;
    }

    /* ================== End of Update Disease Case All Fields ============== */

    /* ================== Start of Update Disease Case Disease ID ============== */
    public void updateDiseaseCaseDiseaseId(int id, int diseaseId) { // Start of updateDiseaseCaseDiseaseId
        try (Connection connection = DataBaseConnection.getConnection()){
            String updateQuery = ("UPDATE disease_cases SET disease_id = ? WHERE ID = ?");
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setInt(1, diseaseId);
            updateStm.setInt(2, id);
            updateStm.executeUpdate();
            updateStm.close();

            viewDiseaseCaseById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /* ================== End of Update Disease Case Disease ID ============== */

    /* ================== Start of Update Disease Case Country ID ============== */
    public void updateDiseaseCaseCountryId(int id, int countryId) { // Start of updateDiseaseCaseCountryId
        try (Connection connection = DataBaseConnection.getConnection()){
            String updateQuery = ("UPDATE disease_cases SET country_id = ? WHERE ID = ?");
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setInt(1, countryId);
            updateStm.setInt(2, id);
            updateStm.executeUpdate();
            updateStm.close();

            viewDiseaseCaseById(id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /* ================== End of Update Disease Case Country ID ============== */

    /* ================== Start of Update Disease Case Cases ============== */
    public void updateDiseaseCaseCases(int id, int cases) {
        try (Connection connection = DataBaseConnection.getConnection()){
            String updateQuery = ("UPDATE disease_cases SET cases = ? WHERE ID = ?");
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setInt(1, cases);
            updateStm.setInt(2, id);
            updateStm.executeUpdate();
            updateStm.close();

            viewDiseaseCaseById(id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /* ================== End of Update Disease Case Cases ============== */

    /* ================== Start of Update Disease Case Deaths ============== */
    public void updateDiseaseCaseDeaths(int id, int deaths) {
        try (Connection connection = DataBaseConnection.getConnection()){
            String updateQuery = ("UPDATE disease_cases SET deaths = ? WHERE ID = ?");
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setInt(1, deaths);
            updateStm.setInt(2, id);
            updateStm.executeUpdate();
            updateStm.close();

            viewDiseaseCaseById(id);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /* ================== End of Update Disease Case Deaths ============== */

    /* ================== Start of Update Disease Case Case Report Date ============== */
    public void updateDiseaseCaseCaseReportDate(int id, Date caseReportDate) {
        try (Connection connection = DataBaseConnection.getConnection()){
            String updateQuery = ("UPDATE disease_cases SET case_report_date = ? WHERE ID = ?");
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            updateStm.setDate(1, caseReportDate);
            updateStm.setInt(2, id);
            updateStm.executeUpdate();
            updateStm.close();

            viewDiseaseCaseById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /* ================== End of Update Disease Case Case Report Date ============== */

    /* ================== Start of Delete Disease Cases ============== */
    public void deleteDiseaseCase(int id) { // Start of deleteDiseaseCase
        try (Connection connection = DataBaseConnection.getConnection()){
            String deleteQuery = ("DELETE FROM disease_cases " +
                                    "WHERE ID = ?");
            PreparedStatement deleteStm = connection.prepareStatement(deleteQuery);
            deleteStm.setInt(1, id);
            deleteStm.executeUpdate();
            deleteStm.close();

            viewAllDiseaseCases();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /* ================== End of Delete Disease Cases ============== */

    /* ================== Start of View All Disease Cases ============== */
    public void viewAllDiseaseCases() { // Start of viewDiseaseCases
        try (Connection connection = DataBaseConnection.getConnection()){
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM disease_cases");

            myModel.setRowCount(0);
            while (rs.next()) {
                Date inputDate = rs.getDate("case_report_date");
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String formatedDate = dateFormat.format(inputDate);
                myModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getInt("disease_id"),
                        rs.getInt("country_id"),
                        rs.getInt("cases"),
                        rs.getInt("deaths"),
                        formatedDate
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /* ================== End of View All Disease Cases ============== */

    /* ================== Start of View Inserted Disease Case ============== */
    public void viewInsertedDiseaseCase(){ // Start of ViewInsertedDiseaseCase
        try (Connection connection = DataBaseConnection.getConnection()){
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * " +
                                                "FROM disease_cases " +
                                                "ORDER BY ID DESC " +
                                                "LIMIT 1");
            if (rs.next()) {
                myModel.setRowCount(0);
                Date inputDate = rs.getDate("case_report_date");
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String formatedDate = dateFormat.format(inputDate);
                myModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getInt("disease_id"),
                        rs.getInt("country_id"),
                        rs.getInt("cases"),
                        rs.getInt("deaths"),
                        formatedDate
                });
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    /* ================== End of View Inserted Disease Case ============== */

    /* ================== Start of View Disease Cases By Id ============== */
    public void viewDiseaseCaseById(int id){
        try (Connection connection = DataBaseConnection.getConnection()){
            String selectQuery = ("SELECT * FROM disease_cases WHERE ID = ?");
            PreparedStatement selectStm = connection.prepareStatement(selectQuery);
            selectStm.setInt(1,id);
            selectStm.execute();

            ResultSet rs = selectStm.getResultSet();
            if (rs.next()) {
                myModel.setRowCount(0);
                Date inputDate = rs.getDate("case_report_date");
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String formatedDate = dateFormat.format(inputDate);
                myModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getInt("disease_id"),
                        rs.getInt("country_id"),
                        rs.getInt("cases"),
                        rs.getInt("deaths"),
                        formatedDate
                });
            }


        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    /* ================== End of View Disease Cases By Id ============== */

    /* ================== Start of Search Disease Case ID =================== */
    public void searchDiseaseCase(int id){ // Start of SearchCountry

        try (Connection connection = DataBaseConnection.getConnection()) {
            String selectQuery = ("SELECT * FROM disease_cases " +
                                    "WHERE disease_id = ?");

            PreparedStatement selectStm = connection.prepareStatement(selectQuery);
            selectStm.setInt(1,id);

            ResultSet rs = selectStm.executeQuery();

            if(rs.next()) {
                myModel.setRowCount(0);
                myModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("country_name"),
                        rs.getInt("continent_id"),
                        rs.getLong("population")
                });
            }else {
                JOptionPane.showMessageDialog(null, "Data not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /* ================== End of Search Disease Case ID =================== */

} // End of DiseaseCases class
