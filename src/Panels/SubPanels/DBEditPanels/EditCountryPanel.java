package Panels.SubPanels.DBEditPanels;

import Components.*;
import DiseaseDB.Country;
import DiseaseDB.DataBaseConnection;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.*;

public class EditCountryPanel extends JPanel { // Start of EditCountryPanel class

    int countryId;
    private String countryName;
    int continentID;
    long population;
    int column;


    private ButtonManager button;
    private LabelManager label;
    private TextFieldManager text;
    private ComboBoxManager comboBox;

    private TableManager myTable;
    private DefaultTableModel myModel;
    private Country data;
    private JScrollPane scrollPane;

    private JPanel tablePanel;

    public EditCountryPanel() { // Start of constructor
        this.setLayout(new BorderLayout());

        button = new ButtonManager();
        text = new TextFieldManager();
        label = new LabelManager();
        comboBox = new ComboBoxManager();

        myModel = new DefaultTableModel();
        myTable = new TableManager(myModel);
        data = new Country(myTable);
        scrollPane = new JScrollPane(myTable);

        this.add(northPanel(), BorderLayout.NORTH);
        this.add(centerPanel(), BorderLayout.CENTER);
        this.add(southPanel(), BorderLayout.SOUTH);
    } // End of constructor

    /* ================== Start of northPanel =================== */
    private JPanel northPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setPreferredSize(new Dimension(this.getWidth(), 50));
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        JLabel title = new JLabel("Country Table Management");
        title.setFont(new Font("SansSerif", Font.PLAIN, 30));
        title.setForeground(Color.WHITE);

        text.getSearchTextField().setPreferredSize(new Dimension(120, 25));
        text.getSearchTextField().setMaximumSize(new Dimension(120, 25));

        text.getSearchTextField().setToolTipText("Enter Country's Name");
        button.getSearchButton().addActionListener(e -> searchData());
        text.getSearchTextField().addActionListener(e -> searchData());


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
        panel.setBorder(BorderFactory.createEmptyBorder(0, 100, 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 10, 5, 0);
        gbc.weightx = 1;


        label.getCountryNameLabel().setFont(new Font("SansSerif", Font.PLAIN, 20));
        label.getContinentIDLabel().setFont(new Font("SansSerif", Font.PLAIN, 20));
        label.getPopulationLabel().setFont(new Font("SansSerif", Font.PLAIN, 20));

        text.getCountryNameTextField().setPreferredSize(new Dimension(500, 50));
        text.getCountryNameTextField().setMaximumSize(new Dimension(500, 50));
        text.getContinentIdTextField().setMaximumSize(new Dimension(500, 50));
        text.getPopulationTextField().setPreferredSize(new Dimension(500, 50));
        text.getPopulationTextField().setMaximumSize(new Dimension(500, 50));

        comboBox.getIdComboBox().setPreferredSize(new Dimension(500, 50));
        comboBox.getIdComboBox().setMaximumSize(new Dimension(500, 50));
        comboBox.getIdComboBox().addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                loadContinetIds();
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });

        gbc.gridy = 0;
        gbc.gridx = 0;
//        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(label.getCountryNameLabel(), gbc);

        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(text.getCountryNameTextField(), gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
//        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(label.getContinentIDLabel(), gbc);

        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(comboBox.getIdComboBox(), gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
//        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(label.getPopulationLabel(), gbc);

        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(text.getPopulationTextField(), gbc);


        return panel;
    }
    /* ================== End of centerPanel =================== */

    /* ================== Start of southPanel =================== */
    private JPanel southPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(this.getWidth(), 450));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(this.getWidth(), 50));
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);


        button.getViewButton().addActionListener(e -> viewData());
        button.getAddButton().addActionListener(e -> addData());
        button.getUpdteButton().addActionListener(e -> updateData(countryId, countryName, continentID, population, column));
        button.getRemoveButton().addActionListener(e -> deleteData());
        button.getClearButton().addActionListener(e -> clearData());

        buttonPanel.add(button.getViewButton());
        buttonPanel.add(button.getAddButton());
        buttonPanel.add(button.getUpdteButton());
        buttonPanel.add(button.getRemoveButton());
        buttonPanel.add(button.getClearButton());


        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        return mainPanel;
    }
    /* ================== End of southPanel =================== */

    /* ================= Start of loadContinetIds method ================ */
    private void loadContinetIds() {
        try (Connection connection = DataBaseConnection.getConnection()) {

            comboBox.getIdComboBox().removeAllItems();
            String query = "SELECT * FROM continents";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = String.valueOf(resultSet.getInt("ID"));
                String name = resultSet.getString("continent_name");
                comboBox.getIdComboBox().addItem(id + "-> " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load Continent IDs", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* ================= End of loadContinetIds method ================ */

    /* ================= Start of Clear Fields method ================ */
    private void clearFields() {
        text.getCountryNameTextField().setText(null);
        comboBox.getIdComboBox().setSelectedItem(null);
        text.getPopulationTextField().setText(null);
    }
    /* ================= End of Clear Fields method ================ */

    /* ================= Start of Search Country Method ============= */
    private void searchData() {
        countryName = text.getSearchTextField().getText().strip();
        setCellEditor();

        if (countryName.matches(".*\\d.*") || countryName.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter a valid name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            data.searchCountry(countryName);
            myTable.setStyle();
            scrollPane.setVisible(true);
            tablePanel.revalidate();
            tablePanel.repaint();
        }
    }
    /* ================= End of Search Country Method ============= */

    /* ================= Start of View Data Method ============ */
    private void viewData() {
        setCellEditor();
        data.viewCountries();
        myTable.setStyle();
        scrollPane.setVisible(true);
        tablePanel.revalidate();
        tablePanel.repaint();
    }
    /* ================= End of View Data Method ============ */

    /* ================= Start of Add Data Method =============== */
    private void addData() {
        String selectedID;
        String textPopulation;
        try {
            countryName = text.getCountryNameTextField().getText();

            selectedID = String.valueOf(comboBox.getIdComboBox().getSelectedItem());
            if (selectedID == null || selectedID.strip().isEmpty()) {
                return;
            }
            try {
                continentID = Integer.parseInt(selectedID.split("-> ")[0]);
            } catch (NumberFormatException ex1) {
                JOptionPane.showMessageDialog(this, "Select a Continent ID", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }


            textPopulation = text.getPopulationTextField().getText();
            if (textPopulation == null || textPopulation.strip().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please write the population", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                population = Long.parseLong(textPopulation);
            } catch (NumberFormatException ex2) {
                JOptionPane.showMessageDialog(this, "Invalid numeric input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            if (countryName == null || countryName.strip().isEmpty() || countryName.matches(".*\\d.*")) {
                JOptionPane.showMessageDialog(this, "Enter a valid Name", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!countryName.strip().isEmpty() && !selectedID.strip().isEmpty() && !textPopulation.strip().isEmpty()) {
                int choice = JOptionPane.showOptionDialog(this,
                        "Are you sure for the addition? ",
                        "Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        null,
                        null);
                if (choice == JOptionPane.YES_OPTION) {
                    boolean input = data.addCountry(countryName, continentID, population);
                    if (input){
                        myTable.setStyle();
                        scrollPane.setVisible(true);
                        tablePanel.revalidate();
                        tablePanel.repaint();
                        clearFields();
                        JOptionPane.showMessageDialog(this, "Country added Succesfully", "Success", JOptionPane.PLAIN_MESSAGE);
                    }

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

    /* ================= Start of Update Data Method ============ */
    private void updateData(int id, String countryName, int continentid, long population, int column) {
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
            boolean input = data.countryAllFieldsUpdate(id, countryName, continentid, population);
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
            int id = (int) myTable.getModel().getValueAt(row, 0);
            int choice = JOptionPane.showOptionDialog(null,
                    "Do you want to delete the selected row?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    null);
            if (choice == JOptionPane.YES_OPTION) {
               boolean input =  data.deleteCountry(id);
               if (input){
                   JOptionPane.showMessageDialog(this, "Data Deleted Succefully", "Succes", JOptionPane.PLAIN_MESSAGE);
               }else {
                   JOptionPane.showMessageDialog(this, "Data Deletion Failed", "Error", JOptionPane.ERROR_MESSAGE);
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
                    countryId = (int) myTable.getModel().getValueAt(row, 0);
                    String editedCountryName = (String) myTable.getModel().getValueAt(row, 1);
                    if (editedCountryName.matches(".*\\d.*")) {
                        JOptionPane.showMessageDialog(null, "Enter a valid country name", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (editedCountryName.strip().isEmpty() || editedCountryName == null){
                        JOptionPane.showMessageDialog(null, "Enter a valid country name", "Error", JOptionPane.ERROR_MESSAGE);

                    }else {
                        countryName = editedCountryName.strip();
                    }

                    Object continentIdValue = myTable.getModel().getValueAt(row, 2);
                    try {
                        if (continentIdValue instanceof String) {
                            String continentIdStr = ((String) continentIdValue).trim();
                            continentID = Integer.parseInt(continentIdStr);
                        } else if (continentIdValue instanceof Integer) {
                            continentID = (Integer) continentIdValue;
                        }
                    } catch (NumberFormatException ex1) {
                        JOptionPane.showMessageDialog(null, "Enter a valid ID", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    Object populationValue = myTable.getModel().getValueAt(row, 3);
                    try {
                        if (populationValue instanceof String) {
                            population = Long.parseLong((String) populationValue);
                        } else if (populationValue instanceof Long) {
                            population = (Long) populationValue;
                        }
                    } catch (NumberFormatException ex2) {
                        JOptionPane.showMessageDialog(null, "Enter Valid Population", "Error", JOptionPane.ERROR_MESSAGE);
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

} // End of Edit Country Panel class
