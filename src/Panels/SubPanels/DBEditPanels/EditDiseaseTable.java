package Panels.SubPanels.DBEditPanels;

import Components.*;
import DiseaseDB.Disease;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;

public class EditDiseaseTable extends JPanel { // Start of EditDiseaseTable class

    private int diseaseId;
    private String diseaseName;
    private String diseaseDesc;
    private String discoveryDate;
    private java.util.Date javaDate;
    private int column;

    private ButtonManager button;
    private TextFieldManager text;
    private LabelManager label;
    private JDateChooser date;

    private JPanel tablePanel;

    private TableManager myTable;
    private DefaultTableModel myModel;
    private Disease data;
    private JScrollPane scrollPane;


    public EditDiseaseTable() { // Start of constructor
        this.setLayout(new BorderLayout());

        button = new ButtonManager();
        text = new TextFieldManager();
        label = new LabelManager();

        date = new JDateChooser();
        myModel = new DefaultTableModel();
        myTable = new TableManager(myModel);
        data = new Disease(myTable);
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
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));

        JLabel title = new JLabel("Disease Table Management");
        title.setFont(new Font("SansSerif", Font.PLAIN, 30));
        title.setForeground(Color.WHITE);

        text.getSearchTextField().setPreferredSize(new Dimension(120, 25));
        text.getSearchTextField().setMaximumSize(new Dimension(120, 25));

        button.getSearchButton().addActionListener(e -> searchData());
        text.getSearchTextField().addActionListener(e -> searchData());
        text.getSearchTextField().setToolTipText("Enter Disease's Name");

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
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.weightx = 1;


        label.getDiseaseNameLabel().setFont(new Font("SansSerif", Font.PLAIN, 20));
        label.getDiseaseDescriptionLabel().setFont(new Font("SansSerif", Font.PLAIN, 20));
        label.getDiseaseDiscoverDateLabel().setFont(new Font("SansSerif", Font.PLAIN, 20));

        text.getDiseaseNameTextField().setPreferredSize(new Dimension(500, 50));
        text.getDiseaseNameTextField().setMaximumSize(new Dimension(500,100 ));
        text.getDiseaseDescriptionTextArea().setPreferredSize(new Dimension(500, 100));
        text.getDiseaseDescriptionTextArea().setMaximumSize(new Dimension(500, 100));

        date.getComponent(0).setPreferredSize(new Dimension(50, 50));
        date.getComponent(0).setMaximumSize(new Dimension(50, 50));
        date.getComponent(1).setPreferredSize(new Dimension(450, 50));
        date.getComponent(1).setMaximumSize(new Dimension(450, 50));

        date.setDateFormatString("dd-MM-yyyy");
        date.setPreferredSize(new Dimension(500, 50));


        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(label.getDiseaseNameLabel(), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(text.getDiseaseNameTextField(), gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(label.getDiseaseDescriptionLabel(), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(text.getDiseaseDescriptionTextArea(), gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(label.getDiseaseDiscoverDateLabel(), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(date, gbc);


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


        button.getViewButton().addActionListener(e -> viewData());
        button.getAddButton().addActionListener(e -> addData());
        button.getUpdteButton().addActionListener(e-> updateData(diseaseId,diseaseName,diseaseDesc, discoveryDate,column));
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

    private void clearFields() {
        text.getDiseaseNameTextField().setText(null);
        text.getDiseaseDescriptionTextArea().setText(null);
        date.setDate(null);
    }
    /* ================= End of Clear Fields method ================ */

    /* ================= Start of Search Country Method ============= */
    private void searchData() {
        diseaseName = text.getSearchTextField().getText().strip();
        setCellEditor();

        if (diseaseName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            data.searchDisease(diseaseName);
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
        data.viewAllDiseases();
        myTable.setStyle();
        scrollPane.setVisible(true);
        tablePanel.revalidate();
        tablePanel.repaint();
    }
    /* ================= End of View Data Method ============ */

    /* ================= Start of Add Data Method =============== */
    private void addData() {
        String diseaseDesc;
        try {
            diseaseName = text.getDiseaseNameTextField().getText();

            if (diseaseName == null || diseaseName.strip().isEmpty() || diseaseName.matches(".*\\d.*")) {
                JOptionPane.showMessageDialog(this, "Enter a valid Disease Name", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            diseaseDesc = text.getDiseaseDescriptionTextArea().getText();
            if (diseaseDesc == null || diseaseDesc.strip().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please write a valid description", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (date.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Select a valid date", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            javaDate = new java.sql.Date(date.getDate().getTime());
            if (!diseaseName.strip().isEmpty() && !diseaseDesc.strip().isEmpty() && javaDate != null ){
                int choice = JOptionPane.showOptionDialog(this,
                        "Are you sure for the addition? ",
                        "Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        null,
                        null);
                if (choice == JOptionPane.YES_OPTION) {
                    boolean input = data.addDisease(diseaseName, diseaseDesc, (Date) javaDate);
                    if (input){
                        myTable.setStyle();
                        scrollPane.setVisible(true);
                        tablePanel.revalidate();
                        tablePanel.repaint();
                        JOptionPane.showMessageDialog(this, "Disease added Successfully", "Success", JOptionPane.PLAIN_MESSAGE);
                    }
                } else {
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
    private void updateData(int diseaseId, String diseaseName, String diseaseDesc,String discoveryDate, int column) {
        if (column == 0) {
            JOptionPane.showMessageDialog(this, "Select a cell to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showOptionDialog(
                this,
                "Do you want to proceed with update?",
                "Warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null
        );
        if (choice == JOptionPane.YES_OPTION) {
            boolean input = data.diseaseAllFieldsUpdate(diseaseId, diseaseName, diseaseDesc, discoveryDate);
            if (input) {
                JOptionPane.showMessageDialog(this, "Update was successful.", "Success", JOptionPane.PLAIN_MESSAGE);
                myTable.setStyle();
                scrollPane.setVisible(true);
                tablePanel.revalidate();
                tablePanel.repaint();
            }else{
                JOptionPane.showMessageDialog(this, "Operation Canceled.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    /* ================= End of Update Data Method ============ */

    /* ================= Start of Delete Data Method ============== */
    private void deleteData() {
        try {
            int row = myTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            diseaseId = (int) myTable.getModel().getValueAt(row, 0);
            int choice = JOptionPane.showOptionDialog(this,
                    "Do you want to delete the selected row?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Yes", "No"},
                    "No");
            if (choice == JOptionPane.YES_OPTION) {
                boolean input = data.deleteDisease(diseaseId);
                if (input) {
                    JOptionPane.showMessageDialog(this, "Row deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(this, "Data Deletion Failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Delete operation canceled.", "Canceled", JOptionPane.INFORMATION_MESSAGE);
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
                    diseaseId = (int) myTable.getModel().getValueAt(row, 0);
                    try {
                        diseaseName = (String) myTable.getModel().getValueAt(row, 1);
                        if (diseaseName.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Enter A Disease Name", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Enter A Valid Name", "Error", JOptionPane.ERROR_MESSAGE);
                    }



                    diseaseDesc = (String) myTable.getModel().getValueAt(row, 2);
                    if (diseaseDesc == null || diseaseDesc.strip().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Enter A Valid Description", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        diseaseDesc = diseaseDesc.strip();
                    }

                    Object discoverDateValue = myTable.getModel().getValueAt(row, 3);
                    try {
                        if (discoverDateValue instanceof String) {
                            discoveryDate = ((String) discoverDateValue).trim();
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

} // End of EditDiseaseTable class
