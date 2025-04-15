package Panels.SubPanels.DBEditPanels;

import Components.*;
import DiseaseDB.DiseaseCases;
import DiseaseDB.DataBaseConnection;
import com.toedter.calendar.JDateChooser;

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

public class EditDiseaseCasesPanel extends JPanel {
    
    private int diseaseCaseId;
    private int diseaseId;
    private int countryId;
    private int cases;
    private int deaths;
    private String tableDate;
    private java.sql.Date javaDate;
    int column;

    private ButtonManager button;
    private TextFieldManager text;
    private LabelManager label;
    private JDateChooser date;
    private ComboBoxManager comboBox;

    private JFileChooser fileChooser;
    private TableManager myTable;
    private DefaultTableModel myModel;
    private DiseaseCases data;
    private JScrollPane scrollPane;

    private JPanel tablePanel;

    public EditDiseaseCasesPanel() {
        this.setLayout(new BorderLayout());

        button = new ButtonManager();
        text = new TextFieldManager();
        label = new LabelManager();
        date = new JDateChooser();
        comboBox = new ComboBoxManager();

        fileChooser = new JFileChooser();
        myModel = new DefaultTableModel();
        myTable = new TableManager(myModel);
        data = new DiseaseCases(myTable);
        scrollPane = new JScrollPane(myTable);

        this.add(northPanel(), BorderLayout.NORTH);
        this.add(centerPanel(), BorderLayout.CENTER);
        this.add(southPanel(), BorderLayout.SOUTH);
    }

    /* ================== Start of northPanel =================== */
    private JPanel northPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setPreferredSize(new Dimension(this.getWidth(), 50));
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));

        JLabel title = new JLabel("Disease Cases Table Management");
        title.setFont(new Font("SansSerif", Font.PLAIN, 30));
        title.setForeground(Color.WHITE);

        text.getSearchTextField().setPreferredSize(new Dimension(120, 25));
        text.getSearchTextField().setMaximumSize(new Dimension(120, 25));

        button.getSearchButton().addActionListener(e -> searchData());
        text.getSearchTextField().addActionListener(e -> searchData());
        text.getSearchTextField().setToolTipText("Enter Disease Case's ID");

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
        gbc.ipady = 17;
        gbc.insets = new Insets(10, 10, 5, 0);
        gbc.weightx = 1;


        label.getDiseaseIDLabel().setFont(new Font("SansSerif", Font.PLAIN, 20));
        label.getCountryIDLabel().setFont(new Font("SansSerif", Font.PLAIN, 20));
        label.getCasesLabel().setFont(new Font("SansSerif", Font.PLAIN, 20));
        label.getDeathsLabel().setFont(new Font("SansSerif", Font.PLAIN, 20));
        label.getDateLabel().setFont(new Font("SansSerif", Font.PLAIN, 20));

        comboBox.getDiseaseIdComboBox().setPreferredSize(new Dimension(500, 80));
        comboBox.getDiseaseIdComboBox().setMaximumSize(new Dimension(500, 80));
        comboBox.getDiseaseIdComboBox().setLightWeightPopupEnabled(false);
        comboBox.getCountryIdComboBox().setPreferredSize(new Dimension(500, 80));
        comboBox.getCountryIdComboBox().setMaximumSize(new Dimension(500, 80));
        comboBox.getCountryIdComboBox().setLightWeightPopupEnabled(false);
        diseaseIdComboBoxPopupMenuListener();
        countryIdComboBoxPopupMenuListener();

        text.getDiseaseCasesTextField().setPreferredSize(new Dimension(500, 50));
        text.getDiseaseCasesTextField().setMaximumSize(new Dimension(500, 50));
        text.getDiseaseDeathsTextField().setPreferredSize(new Dimension(500, 50));
        text.getDiseaseDeathsTextField().setMaximumSize(new Dimension(500, 50));

        date.setPreferredSize(new Dimension(500, 50));
        date.setMaximumSize(new Dimension(500, 50));
        date.getComponent(0).setPreferredSize(new Dimension(50, 50));
        date.getComponent(0).setMaximumSize(new Dimension(50, 50));
        date.getComponent(1).setPreferredSize(new Dimension(450, 50));
        date.getComponent(1).setMaximumSize(new Dimension(450, 50));
        date.setDateFormatString("dd/MM/yyyy");

        gbc.gridy = 0;
        gbc.gridx = 0;
//        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(label.getDiseaseIDLabel(), gbc);

        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(comboBox.getDiseaseIdComboBox(), gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
//        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(label.getCountryIDLabel(), gbc);

        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(comboBox.getCountryIdComboBox(), gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
//        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(label.getCasesLabel(), gbc);

        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(text.getDiseaseCasesTextField(), gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
//        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(label.getDeathsLabel(), gbc);

        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(text.getDiseaseDeathsTextField(), gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
//        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(label.getDateLabel(), gbc);

        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(date, gbc);

        return panel;
    }
    /* ================== End of centerPanel =================== */

    /* ================== Start of southPanel =================== */
    private JPanel southPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(this.getWidth(), 400));
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(this.getWidth(), 50));
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);


        button.getViewButton().addActionListener(e -> viewData());
        button.getAddButton().addActionListener(e -> addData());
        button.getUpdteButton().addActionListener(e -> updateData(diseaseCaseId,diseaseId,countryId,cases,deaths,tableDate,column));
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
    private void diseaseIdComboBoxPopupMenuListener() {// Start of diseaseIdComboBoxPopupMenuListener
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

    /* ================= Start of Clear Fields method ================ */
    private void clearFields() {
        comboBox.getDiseaseIdComboBox().setSelectedItem(null);
        comboBox.getCountryIdComboBox().setSelectedItem(null);
        text.getDiseaseCasesTextField().setText(null);
        text.getDiseaseDeathsTextField().setText("");
        text.getDiseaseDescriptionTextArea().setText("");
        date.setDate(null);
    }
    /* ================= End of Clear Fields method ================ */

    /* ================= Start of Search Disease Case Method ============= */
    private void searchData() {
        String searchId = text.getSearchTextField().getText();
        setCellEditor();
        if  (searchId.strip().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter an ID", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
            try {
                diseaseCaseId = Integer.parseInt(searchId);
                data.searchDiseaseCase(diseaseId);
                myTable.setStyle();
                scrollPane.setVisible(true);
                tablePanel.revalidate();
                tablePanel.repaint();
            } catch (NumberFormatException ex1) {
                JOptionPane.showMessageDialog(this, "Enter a valid ID", "Warning", JOptionPane.WARNING_MESSAGE);
            }
    }
    /* ================= End of Search Disease Case Method ============= */

    /* ================= Start of View Data Method ============ */
    private void viewData() {
        setCellEditor();
        data.viewAllDiseaseCases();
        myTable.setStyle();
        scrollPane.setVisible(true);
        tablePanel.revalidate();
        tablePanel.repaint();
    }
    /* ================= End of View Data Method ============ */

    /* ================= Start of Add Data Method =============== */
    private void addData() {
        String selectedDiseaseID;
        String selectedCountryID;
        String textCases;
        String textDeaths;

        try {

            selectedDiseaseID = String.valueOf(comboBox.getDiseaseIdComboBox().getSelectedItem());
            if (selectedDiseaseID == null || selectedDiseaseID.strip().isEmpty()) {
                return;
            }
            try {
                diseaseId = Integer.parseInt(selectedDiseaseID.split("-> ")[0]);
            } catch (NumberFormatException ex1) {
                JOptionPane.showMessageDialog(this, "Select a Disease ID", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            selectedCountryID = String.valueOf(comboBox.getCountryIdComboBox().getSelectedItem());
            if (selectedCountryID == null || selectedCountryID.strip().isEmpty()) {
                return;
            }
            try {
                countryId = Integer.parseInt(selectedCountryID.split("-> ")[0]);
            } catch (NumberFormatException ex1) {
                JOptionPane.showMessageDialog(this, "Select a Country ID", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }


            textCases = text.getDiseaseCasesTextField().getText();
            if (textCases == null || textCases.strip().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cases cannot be Empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                cases = Integer.parseInt(textCases);
            } catch (NumberFormatException ex2) {
                JOptionPane.showMessageDialog(this, "Invalid numeric input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            textDeaths = text.getDiseaseDeathsTextField().getText();
            if (textDeaths == null || textDeaths.strip().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Deaths cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                deaths = Integer.parseInt(textDeaths);
            } catch (NumberFormatException ex3) {
                JOptionPane.showMessageDialog(this, "Invalid numeric input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            if (date.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Select a valid Date", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }else{
                javaDate = new java.sql.Date(date.getDate().getTime());
            }

            if (!selectedDiseaseID.strip().isEmpty() && !selectedCountryID.strip().isEmpty()
                    && !textCases.strip().isEmpty() && !textDeaths.strip().isEmpty() && javaDate != null) {
                int choice = JOptionPane.showOptionDialog(this,
                        "Are you sure for the addition? ",
                        "Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        null,
                        null);
                if (choice == JOptionPane.YES_OPTION) {
                    data.addDiseaseCase(diseaseId, countryId, cases, deaths, (Date) javaDate);
                    myTable.setStyle();
                    scrollPane.setVisible(true);
                    tablePanel.revalidate();
                    tablePanel.repaint();
                    JOptionPane.showMessageDialog(this, "Country added Successfully", "Success", JOptionPane.PLAIN_MESSAGE);
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Operation canceled", "Canceled", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occured", "Error", JOptionPane.ERROR_MESSAGE);
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
            data.importCsvToDiseaseCases(file);
            viewData();
        }else if (option == JFileChooser.CANCEL_OPTION){

        }else {
            JOptionPane.showMessageDialog(this, "Please select a valid CSV file", "Error", JOptionPane.ERROR_MESSAGE);
            fileChooser.showOpenDialog(null);
        }
    }
    /* ================ End of Import Data with Csv ============= */

    /* ================= Start of Update Data Method ============ */
    private void updateData(int id, int diseaseId, int countryId, int cases, int deaths, String updateDate,int column) {
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
            boolean input = data.updateDiseaseCaseAllFields(id,diseaseId,countryId,cases,deaths,updateDate);
            if(input){
                JOptionPane.showMessageDialog(this, "Update was succesfull", "Succes", JOptionPane.PLAIN_MESSAGE);
                myTable.setStyle();
                scrollPane.setVisible(true);
                tablePanel.revalidate();
                tablePanel.repaint();
            }else {
                JOptionPane.showMessageDialog(this, "Update Canceled", "Cancel", JOptionPane.PLAIN_MESSAGE);
            }

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
            int id = (int) myTable.getModel().getValueAt(row, 0);
            int choice = JOptionPane.showOptionDialog(null,
                    "Do you want to delete the selecte row?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    null);
            if (choice == JOptionPane.YES_OPTION) {
                data.deleteDiseaseCase(id);
                JOptionPane.showMessageDialog(this, "Delete was Succefull", "Succes", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Delete Canceled", "Cancel", JOptionPane.PLAIN_MESSAGE);
            }
            myTable.setStyle();
            scrollPane.setVisible(true);
            tablePanel.revalidate();
            tablePanel.repaint();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Pick a row to delete", "Error", JOptionPane.ERROR_MESSAGE);
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
                    diseaseCaseId = (int) myTable.getModel().getValueAt(row, 0);

                    Object diseaseIdValue = myTable.getModel().getValueAt(row, 1);
                    try {
                        if (diseaseIdValue instanceof String) {
                            String diseaseIdStr = ((String) diseaseIdValue).strip();
                            diseaseId = Integer.parseInt(diseaseIdStr);
                        } else if (diseaseIdValue instanceof Integer) {
                            diseaseId = (Integer) diseaseIdValue;
                        }
                    } catch (NumberFormatException ex1) {
                        JOptionPane.showMessageDialog(null, "Enter a valid Disease ID", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    Object countryIdValue = myTable.getModel().getValueAt(row, 2);
                    try {
                        if (countryIdValue instanceof String) {
                            String countryIdStr = ((String) countryIdValue).strip();
                            countryId = Integer.parseInt(countryIdStr);
                        } else if (countryIdValue instanceof Integer) {
                            countryId = (Integer) countryIdValue;
                        }
                    } catch (NumberFormatException ex2) {
                        JOptionPane.showMessageDialog(null, "Enter Valid Country ID", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    Object casesValue = myTable.getModel().getValueAt(row, 3);
                    try {
                        if (casesValue instanceof String) {
                            String casesStr = ((String) casesValue).strip();
                            cases = Integer.parseInt(casesStr);
                        } else if (casesValue instanceof Integer) {
                            cases = (Integer) casesValue;
                        }
                    } catch (NumberFormatException ex3) {
                        JOptionPane.showMessageDialog(null, "Enter Valid Cases count", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    Object deathsValue = myTable.getModel().getValueAt(row, 4);
                    try {
                        if (deathsValue instanceof String) {
                            String deathsStr = ((String) deathsValue).strip();
                            deaths = Integer.parseInt(deathsStr);
                        } else if (deathsValue instanceof Integer) {
                            deaths = (Integer) deathsValue;
                        }
                    } catch (NumberFormatException ex4) {
                        JOptionPane.showMessageDialog(null, "Enter Valid Deaths Count", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    Object discoverDateValue = myTable.getModel().getValueAt(row, 5);
                    try {
                        if (discoverDateValue instanceof String) {
                            tableDate = ((String) discoverDateValue).trim();
                        } else if (discoverDateValue instanceof java.util.Date) {
                            date.setDate((java.util.Date) discoverDateValue);
                        }
                    } catch (Exception ex1) {
                        ex1.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Enter A Valid Date", "Error", JOptionPane.ERROR_MESSAGE);
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
        myTable.getColumnModel().getColumn(5).setCellEditor(cellEditor);
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

} // End of EditDiseaseCasesPanel class








