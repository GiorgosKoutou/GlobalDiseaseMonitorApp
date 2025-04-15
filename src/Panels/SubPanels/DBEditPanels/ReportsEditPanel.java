package Panels.SubPanels.DBEditPanels;

import Components.*;
import DiseaseDB.Report;
import DiseaseDB.DataBaseConnection;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.sql.*;

public class ReportsEditPanel extends JPanel { // Start of ReportsEditPanel class
    
    private String comments;
    private int reportId;
    private int column;
    private int userID;
    private int diseaseID;
    private int countryID;
    private String userComment;

    private ButtonManager button;
    private TextFieldManager text;
    private LabelManager label;
    private ComboBoxManager comboBox;

    private TableManager myTable;
    private DefaultTableModel myModel;
    private JScrollPane scrollPane;
    private Report data;
    private JFileChooser fileChooser;

    private JPanel tablePanel;

    public ReportsEditPanel() { // Start of constructor
        this.setLayout(new BorderLayout());

        button = new ButtonManager();
        text = new TextFieldManager();
        label = new LabelManager();
        comboBox = new ComboBoxManager();

        myModel = new DefaultTableModel();
        myTable = new TableManager(myModel);
        scrollPane = new JScrollPane(myTable);
        data = new Report(myTable);
        fileChooser = new JFileChooser();

        this.add(northPanel(), BorderLayout.NORTH);
        this.add(centerPanel(), BorderLayout.CENTER);
        this.add(southPanel(), BorderLayout.SOUTH);

    } // End of constructor

    /* ================== Start of northPanel =================== */
    private JPanel northPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setPreferredSize(new Dimension(this.getWidth(), 50));
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));

        JLabel title = new JLabel("Reports Table Management");
        title.setFont(new Font("SansSerif", Font.PLAIN, 30));
        title.setForeground(Color.WHITE);

        text.getSearchTextField().setPreferredSize(new Dimension(120, 25));
        text.getSearchTextField().setMaximumSize(new Dimension(120, 25));

        button.getSearchButton().addActionListener(e -> searchData());
        text.getSearchTextField().addActionListener(e -> searchData());
        text.getSearchTextField().setToolTipText("Enter Report's ID");

        panel.add(Box.createRigidArea(new Dimension(5, 10)));
        panel.add(title);
        panel.add(Box.createHorizontalGlue());
        panel.add(button.getSearchButton());
        panel.add(text.getSearchTextField());

        return panel;
    }
    /* ================== End of northPanel =================== */

    /* ================== Start of centerPanel =================== */
    private JPanel centerPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 100 , 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 10;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.weightx = 1;

        label.getUserIDLabel().setFont(new Font("SansSerif", Font.PLAIN, 20));
        label.getDiseaseIDLabel().setFont(new Font("SansSerif", Font.PLAIN, 20));
        label.getCountryIDLabel().setFont(new Font("SansSerif", Font.PLAIN, 20));
        label.getCommentLabel().setFont(new Font("SansSerif", Font.PLAIN, 20));

        comboBox.getUserIDComboBox().setPreferredSize(new Dimension(500, 50));
        comboBox.getUserIDComboBox().setMaximumSize(new Dimension(500, 50));
        comboBox.getDiseaseIdComboBox().setPreferredSize(new Dimension(500, 50));
        comboBox.getDiseaseIdComboBox().setMaximumSize(new Dimension(500, 50));
        comboBox.getCountryIdComboBox().setPreferredSize(new Dimension(500, 50));
        comboBox.getCountryIdComboBox().setMaximumSize(new Dimension(500, 50));
        userIdComboBoxPopupMenuListener();
        diseaseIdComboBoxPopupMenuListener();
        countryIdComboBoxPopupMenuListener();

        text.getDiseaseDescriptionTextArea().setPreferredSize(new Dimension(500, 50));
        text.getDiseaseDescriptionTextArea().setMaximumSize(new Dimension(500, 50));

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(label.getUserIDLabel(), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(comboBox.getUserIDComboBox(), gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(label.getDiseaseIDLabel(), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(comboBox.getDiseaseIdComboBox(), gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(label.getCountryIDLabel(), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(comboBox.getCountryIdComboBox(), gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(label.getCommentLabel(), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(text.getDiseaseDescriptionTextArea(), gbc);


        return panel;
    }
    /* ================== End of centerPanel =================== */

    /* ================== Start of southPanel =================== */
    private JPanel southPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(this.getWidth(), 450));
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(this.getWidth(), 50));
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));

        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        button.getAddButton().addActionListener(e -> addData());
        button.getViewButton().addActionListener(e -> viewData());
        button.getUpdteButton().addActionListener(e -> updateData(reportId,userID,diseaseID,countryID,userComment,column));
        button.getRemoveButton().addActionListener(e -> deleteData());
        button.getClearButton().addActionListener(e -> clearData());
        button.getImportButton().addActionListener(e -> {
            try {
                importCSV();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        buttonPanel.add(button.getViewButton());
        buttonPanel.add(button.getAddButton());
        buttonPanel.add(button.getUpdteButton());
        buttonPanel.add(button.getRemoveButton());
        buttonPanel.add(button.getClearButton());
        buttonPanel.add(button.getImportButton());

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        return mainPanel;
    }
    /* ================== End of southPanel =================== */

    /* ================= Start of Clear Fields method ================ */
    private void clearFields() {
        comboBox.getUserIDComboBox().setSelectedItem(null);
        comboBox.getDiseaseIdComboBox().setSelectedItem(null);
        comboBox.getCountryIdComboBox().setSelectedItem(null);
        text.getDiseaseDescriptionTextArea().setText(null);
    }
    /* ================= End of Clear Fields method ================ */

    /* ================= Start of Search Country Methos ============= */
    private void searchData() {
        String searchId = text.getSearchTextField().getText();
        setCellEditor();
        if  (searchId.strip().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter an ID", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            reportId = Integer.parseInt(searchId);
            data.searchReport(reportId);
            myTable.setStyle();
            scrollPane.setVisible(true);
            tablePanel.revalidate();
            tablePanel.repaint();
        } catch (NumberFormatException ex1) {
            JOptionPane.showMessageDialog(this, "Enter a valid ID", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
    /* ================= End of Search Country Methos ============= */

    /* ================= Start of View Data Method ============ */
    private void viewData() {
        setCellEditor();
        data.viewAllReports();
        myTable.setStyle();
        scrollPane.setVisible(true);
        tablePanel.revalidate();
        tablePanel.repaint();
    }
    /* ================= End of View Data Method ============ */

    /* ================= Start of Add Data Method =============== */
    private void addData() {
        String selectedUserID, selectedDiseaseID, selectedCountryID, userComment;
        try {
            selectedUserID = String.valueOf(comboBox.getUserIDComboBox().getSelectedItem());
            if (selectedUserID == null || selectedUserID.strip().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a valid User ID", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            selectedDiseaseID = String.valueOf(comboBox.getDiseaseIdComboBox().getSelectedItem());
            if (selectedDiseaseID == null || selectedDiseaseID.strip().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a valid Disease ID", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            selectedCountryID = String.valueOf(comboBox.getCountryIdComboBox().getSelectedItem());
            if (selectedCountryID == null || selectedCountryID.strip().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a valid Country ID", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            userComment = text.getDiseaseDescriptionTextArea().getText();
            if (userComment == null || userComment.strip().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please provide a comment", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int userID, diseaseID, countryID;
            try {
                userID = Integer.parseInt(selectedUserID.split("-> ")[0]);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid User ID", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                diseaseID = Integer.parseInt(selectedDiseaseID.split("-> ")[0]);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Disease ID", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                countryID = Integer.parseInt(selectedCountryID.split("-> ")[0]);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Country ID", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int choice = JOptionPane.showOptionDialog(this,
                    "Are you sure you want to add this report?",
                    "Confirmation", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    null);
            if (choice == JOptionPane.YES_OPTION) {
                boolean isValid = data.addReport(userID,diseaseID,countryID,userComment);
                if (isValid) {
                    data.addReport(userID,diseaseID,countryID,userComment);
                    myTable.setStyle();
                    scrollPane.setVisible(true);
                    tablePanel.revalidate();
                    tablePanel.repaint();
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Report added successfully", "Success", JOptionPane.PLAIN_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Operation canceled", "Canceled", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* ================= End of Add Data Method =============== */

    /* ================ Start of Import Data with Csv ============= */
    private void importCSV() throws SQLException {

        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
        fileChooser.setFileFilter(filter);
        int option = fileChooser.showDialog(null,"Select CSV File");
        File file = fileChooser.getSelectedFile();

        if (option == JFileChooser.APPROVE_OPTION && file.getName().endsWith(".csv")) {
            data.importCsvToReports(file);
            viewData();
        }else if (option == JFileChooser.CANCEL_OPTION){

        }else {
            JOptionPane.showMessageDialog(this, "Please select a valid CSV file", "Error", JOptionPane.ERROR_MESSAGE);
            fileChooser.showOpenDialog(null);
        }
    }
    /* ================ End of Import Data with Csv ============= */

    /* ================= Start of Update Data Method ============ */
    private void updateData(int reportId, int userID, int diseaseID, int countryID, String userComment, int column) {
        if (column == 0) {
            JOptionPane.showMessageDialog(null, "Select a cel to edit", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showOptionDialog(null,
                "Do you want to proceed with update?",
                "Warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);
        if (choice == JOptionPane.YES_OPTION) {
            boolean input = data.updateReportAllFields(reportId,userID,diseaseID,countryID,userComment);
            if (input){
                myTable.setStyle();
                scrollPane.setVisible(true);
                tablePanel.revalidate();
                tablePanel.repaint();
                JOptionPane.showMessageDialog(this, "Update was succesfull", "Succes", JOptionPane.PLAIN_MESSAGE);
            }
        }else {
            JOptionPane.showMessageDialog(this, "Update was canceled", "Canceled", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    /* ================= End of Update Data Method ============ */

    /* ================= Start of Delete Data Method ============== */
    private void deleteData() {
        try {
            int row = myTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Pick a row to delete", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            reportId = (int) myTable.getModel().getValueAt(row, 0);
            int choice = JOptionPane.showOptionDialog(null,
                    "Do you want to delete the selected row?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    null);
            if (choice == JOptionPane.YES_OPTION) {
                boolean input = data.deleteReport(reportId);
                if (input) {
                    JOptionPane.showMessageDialog(this, "Data Deleted Succefully", "Succes", JOptionPane.PLAIN_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Delete Operation Canceled", "Cancel", JOptionPane.PLAIN_MESSAGE);
            }
            myTable.setStyle();
            scrollPane.setVisible(true);
            tablePanel.revalidate();
            tablePanel.repaint();

        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while attempting to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    /* ================= End of Delete Data Method ============== */

    /* ================= Start of Set Cell Editor Method =========== */
    private void setCellEditor() {
        DefaultCellEditor cellEditor = new DefaultCellEditor(new JTextField());
        cellEditor.addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                int row = myTable.getSelectedRow();
                column = myTable.getSelectedColumn();

                if (row >= 0) {
                    reportId = (int) myTable.getModel().getValueAt(row, 0);
                    Object userIdValue = myTable.getModel().getValueAt(row, 1);
                    try {
                        if (userIdValue instanceof String) {
                            String userIdStr = ((String) userIdValue).trim();
                            userID = Integer.parseInt(userIdStr);
                        } else if (userIdValue instanceof Integer) {
                            userID = (Integer) userIdValue;
                        }
                    } catch (NumberFormatException ex1) {
                        JOptionPane.showMessageDialog(null, "Enter a valid User ID", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    Object diseaseIdValue = myTable.getModel().getValueAt(row, 2);
                    try {
                        if (diseaseIdValue instanceof String) {
                            String diseaseIdStr = ((String) diseaseIdValue).trim();
                            diseaseID = Integer.parseInt(diseaseIdStr);
                        } else if (diseaseIdValue instanceof Integer) {
                            diseaseID = (Integer) diseaseIdValue;
                        }
                    } catch (NumberFormatException ex2) {
                        JOptionPane.showMessageDialog(null, "Enter a valid Disease ID", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    Object countryIdValue = myTable.getModel().getValueAt(row, 3);
                    try {
                        if (countryIdValue instanceof String) {
                            String countryIdStr = ((String) countryIdValue).trim();
                            countryID = Integer.parseInt(countryIdStr);
                        } else if (countryIdValue instanceof Integer) {
                            countryID = (Integer) countryIdValue;
                        }
                    } catch (NumberFormatException ex3) {
                        JOptionPane.showMessageDialog(null, "Enter a valid Country ID", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    String editedComment = (String) myTable.getModel().getValueAt(row, 4);
                    if (editedComment.matches(".*[<>|].*")) {
                        JOptionPane.showMessageDialog(null, "Remove invalid characters from the comment", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        userComment = editedComment.strip();
                    }
                }

            }

            @Override
            public void editingCanceled(ChangeEvent e) {
            }
        });
        myTable.getColumnModel().getColumn(1).setCellEditor(cellEditor);
        myTable.getColumnModel().getColumn(2).setCellEditor(cellEditor);
        myTable.getColumnModel().getColumn(3).setCellEditor(cellEditor);
        myTable.getColumnModel().getColumn(4).setCellEditor(cellEditor);
    }
    /* ================= End of Set Cell Editor Method =========== */

    /* ================= Sta/rt of Clear Data Method ============== */
    private void clearData() {
        clearFields();
        scrollPane.setVisible(false);
        myModel.setRowCount(0);
        tablePanel.revalidate();
        tablePanel.repaint();
    }
    /* ================= End of Clear Data Method ============== */

    /* ================== Start of loadUsersIds ================*/
    private void loadUsersIds() {
        try(Connection connection = DataBaseConnection.getConnection()) {

            comboBox.getUserIDComboBox().removeAllItems();
            String query = "SELECT ID, username FROM users";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = String.valueOf(resultSet.getInt("ID"));
                String name = resultSet.getString("username");
                comboBox.getUserIDComboBox().addItem(id + "-> " + name);
            }

        }catch (SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load User IDs", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* ================== End of loadUsersIds ================*/

    /* ================== Start of loadCountryIds =================== */
    private void loadCountryIds(){
        try(Connection connection = DataBaseConnection.getConnection()) {

            comboBox.getCountryIdComboBox().removeAllItems();
            String query = "SELECT ID, country_name FROM countries";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = String.valueOf(resultSet.getInt("ID"));
                String name = resultSet.getString("country_name");
                comboBox.getCountryIdComboBox().addItem(id + "-> " + name);
            }

        } catch (SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load Country IDs", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* ================== End of loadCountryIds =================== */

    /* ================== Start of loadDiseaseIds =================*/
    private void loadDiseaseIds() {
        try (Connection connection = DataBaseConnection.getConnection()) {

            String query = "SELECT ID, name FROM diseases";
            comboBox.getDiseaseIdComboBox().removeAllItems();

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = String.valueOf(resultSet.getInt("ID"));
                String name = resultSet.getString("name");
                comboBox.getDiseaseIdComboBox().addItem(id + "-> " + name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load Disease IDs", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* ================== End of loadDiseaseIds =================*/

    /* ================ Start of popupMenuListeners ==============*/

    private void searchComboboxPopUpMenuListener(){
        comboBox.getIdComboBox().addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                loadUsersIds();
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });
    }
    private void userIdComboBoxPopupMenuListener() { // Start of userIdComboBoxPopupMenuListener
        comboBox.getUserIDComboBox().addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                loadUsersIds();
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });
    } // End of userIdComboBoxPopupMenuListener

    private void diseaseIdComboBoxPopupMenuListener() { // Start of diseaseIdComboBoxPopupMenuListener
        comboBox.getDiseaseIdComboBox().addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                loadDiseaseIds();
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });
    } // End of diseaseIdComboBoxPopupMenuListener

    private void countryIdComboBoxPopupMenuListener() { // Start of countryIdComboBoxPopupMenuListener
        comboBox.getCountryIdComboBox().addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                loadCountryIds();
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });
    } // End of countryIdComboBoxPopupMenuListener
    /* ================= End of popupMenuListeners ==============*/


} // End of ReportsEditPanel class
