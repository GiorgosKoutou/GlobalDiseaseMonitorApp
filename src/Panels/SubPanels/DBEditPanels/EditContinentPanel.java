package Panels.SubPanels.DBEditPanels;

import Components.ButtonManager;
import Components.LabelManager;
import Components.TableManager;
import Components.TextFieldManager;
import DiseaseDB.Continent;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EditContinentPanel extends JPanel { // Start of EditContinentPanel class

    private int continentId;
    private String continentName;
    private int column;

    private ButtonManager button;
    private TextFieldManager text;
    private LabelManager label;

    private TableManager myTable;
    private DefaultTableModel myModel;
    private JScrollPane scrollPane;
    private Continent data;

    private JPanel tablePanel;

    public EditContinentPanel() {

        button = new ButtonManager();
        text = new TextFieldManager();
        label = new LabelManager();

        myModel = new DefaultTableModel();
        myTable = new TableManager(myModel);
        scrollPane = new JScrollPane(myTable);
        data = new Continent(myTable);

        this.setLayout(new BorderLayout());

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

        JLabel title = new JLabel("Continent Table Management");
        title.setFont(new Font("SansSerif", Font.PLAIN, 30));
        title.setForeground(Color.WHITE);

        text.getSearchTextField().setPreferredSize(new Dimension(120, 25));
        text.getSearchTextField().setMaximumSize(new Dimension(120, 25));

        button.getSearchButton().addActionListener(e -> searchData());
        text.getSearchTextField().addActionListener(e -> searchData() );
        text.getSearchTextField().setToolTipText("Enter Continent's Name");

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
        panel.setBorder(BorderFactory.createEmptyBorder(0, 50, 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 16;
        gbc.ipady = 10;
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.weightx = 1;

        label.getContinentNameLabel().setFont(new Font("SansSerif", Font.PLAIN, 20));

        text.getContinentNameTextField().setPreferredSize(new Dimension(500, 50));

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(label.getContinentNameLabel(), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(text.getContinentNameTextField(), gbc);


        return panel;
    }
    /* ================== End of centerPanel =================== */

    private JPanel southPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(this.getWidth(), 600));
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(this.getWidth(), 50));
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        button.getViewButton().addActionListener(e -> viewData());
        button.getAddButton().addActionListener(e -> addData());
        button.getUpdteButton().addActionListener(e -> updateData(continentId, continentName, column));
        button.getRemoveButton().addActionListener(e -> deleteData());
        button.getClearButton().addActionListener(e -> clearData());

        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

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

    /* ================= Start of Clear Fields method ================ */
    private void clearFields() {
        text.getContinentNameTextField().setText("");
    }
    /* ================= End of Clear Fields method ================ */

    /* ================= Start of Search Country Method ============= */
    private void searchData() {
        continentName = text.getSearchTextField().getText().strip();
        setCellEditor();

        if (continentName.matches(".*\\d.*") || continentName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            data.searchContinent(continentName);
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
        data.viewContinents();
        myTable.setStyle();
        scrollPane.setVisible(true);
        tablePanel.revalidate();
        tablePanel.repaint();
    }
    /* ================= End of View Data Method ============ */

    /* ================= Start of Add Data Method =============== */
    private void addData() {
        continentName = text.getContinentNameTextField().getText();
        try {
            continentName = text.getContinentNameTextField().getText();

            if (continentName == null || continentName.strip().isEmpty() || continentName.matches(".*\\d.*")) {
                JOptionPane.showMessageDialog(this, "Enter a valid Name", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!continentName.strip().isEmpty()) {
                int choice = JOptionPane.showOptionDialog(this,
                        "Are you sure for the addition? ",
                        "Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        null,
                        null);
                if (choice == JOptionPane.YES_OPTION) {
                    boolean input = data.addContinent(continentName);
                    if (input){
                        myTable.setStyle();
                        scrollPane.setVisible(true);
                        tablePanel.revalidate();
                        tablePanel.repaint();
                        JOptionPane.showMessageDialog(this, "Continent added Successfully", "Success", JOptionPane.PLAIN_MESSAGE);
                        clearFields();
                    }
                }else{
                    JOptionPane.showMessageDialog(this, "Operation canceled", "Canceled", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* ================= End of Add Data Method =============== */

    /* ================= Start of Update Data Method ============ */
    private void updateData(int id, String continentName, int column) {
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
            boolean input = data.continentNameUpdate(continentId,continentName);
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
                JOptionPane.showMessageDialog(this, "Pick a row to delete", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            continentId = (int) myTable.getModel().getValueAt(row, 0);
            int choice = JOptionPane.showOptionDialog(this,
                    "Do you want to delete the selected row?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    null);
            if (choice == JOptionPane.YES_OPTION) {
                boolean input = data.deleteContinent(continentId);
                if (input){
                    JOptionPane.showMessageDialog(this, "Data Deleted Successfully", "Success", JOptionPane.PLAIN_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Delete Operation Canceled", "Cancel", JOptionPane.INFORMATION_MESSAGE);
            }
            myTable.setStyle();
            scrollPane.setVisible(true);
            tablePanel.revalidate();
            tablePanel.repaint();
    }catch(ArrayIndexOutOfBoundsException e){
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
                    continentId = (int) myTable.getModel().getValueAt(row, 0);
                    String editedContinentName = (String) myTable.getModel().getValueAt(row, 1);
                    if (editedContinentName.matches(".*\\d.*")) {
                        JOptionPane.showMessageDialog(null, "Enter A Valid Continent Name", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (editedContinentName.strip().isEmpty() || editedContinentName == null) {
                        JOptionPane.showMessageDialog(null, "Continent Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);

                    }else{
                        continentName = editedContinentName.strip();
                    }
                }
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
            }
        });
        myTable.getColumnModel().getColumn(1).setCellEditor(cellEditor);
    }
    /* ================= End of Set Cell Editor Method =========== */
    
    /* ================= Start of Clear Data Method ============== */
    private void clearData() {
        clearFields();
        scrollPane.setVisible(false);
        myModel.setRowCount(0);
        tablePanel.revalidate();
        tablePanel.repaint();
    }
    /* ================= End of Clear Data Method ============== */


} // End of EditContinentPanel class
