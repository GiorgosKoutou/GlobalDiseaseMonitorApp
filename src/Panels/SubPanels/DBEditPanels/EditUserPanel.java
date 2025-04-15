package Panels.SubPanels.DBEditPanels;

import Components.*;
import DiseaseDB.*;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class EditUserPanel extends JPanel {

    private int userId;
    private String userName;
    private String password;
    private int roleId;

    private int column;

    private ButtonManager button;
    private TextFieldManager text;
    private LabelManager label;
    private ComboBoxManager comboBox;

    private TableManager myTable;
    private DefaultTableModel myModel;
    private JScrollPane scrollPane;
    private User data;

    private JPanel tablePanel;

    public EditUserPanel() { // Start of constructor
        this.setLayout(new BorderLayout());

        button = new ButtonManager();
        text = new TextFieldManager();
        label = new LabelManager();
        comboBox = new ComboBoxManager();

        myModel = new DefaultTableModel();
        myTable = new TableManager(myModel);
        scrollPane = new JScrollPane(myTable);
        data = new User(myTable);

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

        JLabel title = new JLabel("User Table Management");
        title.setFont(new Font("SansSerif", Font.PLAIN, 30));
        title.setForeground(Color.WHITE);

        text.getSearchTextField().setPreferredSize(new Dimension(120, 25));
        text.getSearchTextField().setMaximumSize(new Dimension(120, 25));

        button.getSearchButton().addActionListener(e -> searchData());
        text.getSearchTextField().addActionListener(e -> searchData());
        text.getSearchTextField().setToolTipText("Enter User's ID");


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
        panel.setBorder(BorderFactory.createEmptyBorder(0, 50 , 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 17;
        gbc.insets = new Insets(10, 10, 5, 0);
        gbc.weightx = 1;

        label.getUserNameLabel().setFont(new Font("SansSerif", Font.PLAIN, 20));
        label.getPasswordLabel().setFont(new Font("SansSerif", Font.PLAIN, 20));
        label.getRoleIdLabel().setFont(new Font("SansSerif", Font.PLAIN, 20));

        text.getUserNameTextField().setPreferredSize(new Dimension(500, 50));
        text.getUserNameTextField().setMaximumSize(new Dimension(500, 50));
        text.getEnterPasswordTextField().setPreferredSize(new Dimension(500, 50));
        text.getEnterPasswordTextField().setMaximumSize(new Dimension(500, 50));

        comboBox.getIdComboBox().setPreferredSize(new Dimension(500, 50));
        comboBox.getIdComboBox().setMaximumSize(new Dimension(500, 50));
        comboBox.getIdComboBox().addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                loadRoleIds();
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
        panel.add(label.getUserNameLabel(), gbc);

        gbc.gridx = 1;
        panel.add(text.getUserNameTextField(), gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(label.getPasswordLabel(), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(text.getEnterPasswordTextField(), gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(label.getRoleIdLabel(), gbc);

        gbc.gridx = 1;
        panel.add(comboBox.getIdComboBox(), gbc);


        return panel;
    }
    /* ================== End of centerPanel =================== */

    private JPanel southPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(this.getWidth(), 550));
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(this.getWidth(), 50));
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));
        buttonPanel.add(button.getViewButton());
        buttonPanel.add(button.getAddButton());
        buttonPanel.add(button.getUpdteButton());
        buttonPanel.add(button.getRemoveButton());
        buttonPanel.add(button.getClearButton());

        button.getViewButton().addActionListener(e -> viewData());
        button.getAddButton().addActionListener(e -> addData());
        button.getUpdteButton().addActionListener(e -> updateData(userId, userName, password, roleId, column));
        button.getRemoveButton().addActionListener(e -> deleteData());
        button.getClearButton().addActionListener(e -> clearData());

        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);


        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        return mainPanel;
    }
    /* ================== End of southPanel =================== */

    /* ================== Start of loadUserIds =================== */
    private void loadRoleIds(){
        try (Connection connection = DataBaseConnection.getConnection()){

            comboBox.getIdComboBox().removeAllItems();
            String query = "SELECT ID, name FROM roles";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = String.valueOf(resultSet.getInt("ID"));
                String name = resultSet.getString("name");
                comboBox.getIdComboBox().addItem(id + "-> " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Failed to load User IDs","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    /* ================== End of loadUserIds =================== */

    /* ================= Start of Clear Fields method ================ */
    private void clearFields() {
        text.getUserNameTextField().setText(null);
        comboBox.getIdComboBox().setSelectedItem(null);
        text.getEnterPasswordTextField().setText(null);
    }
    /* ================= End of Clear Fields method ================ */

    /* ================= Start of Search Country Method ============= */
    private void searchData() {
        userName = text.getSearchTextField().getText().strip();
        setCellEditor();

        if (userName.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter a valid name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            data.searchUser(userName);
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
        data.viewUsers();
        myTable.setStyle();
        scrollPane.setVisible(true);
        tablePanel.revalidate();
        tablePanel.repaint();
    }
    /* ================= End of View Data Method ============ */

    /* ================= Start of Add Data Method =============== */
    private void addData() {
        String selectedID;
        try {
            userName = text.getUserNameTextField().getText();
            password = text.getEnterPasswordTextField().getText();

            selectedID = String.valueOf(comboBox.getIdComboBox().getSelectedItem());
            if (selectedID == null || selectedID.strip().isEmpty()) {
                return;
            }
            try {
                roleId = Integer.parseInt(selectedID.split("-> ")[0]);
            } catch (NumberFormatException ex1) {
                JOptionPane.showMessageDialog(this, "Select a Role ID", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (userName == null || userName.strip().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter a  User Name", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (password == null || password.strip().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter a  Password", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!userName.strip().isEmpty() && !selectedID.strip().isEmpty() && !password.strip().isEmpty()) {
                int choice = JOptionPane.showOptionDialog(this,
                        "Are you sure for the addition? ",
                        "Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        null,
                        null);
                if (choice == JOptionPane.YES_OPTION) {
                    boolean input = data.addUser(userName, password, roleId);
                    if (!input) {
                        myTable.setStyle();
                        scrollPane.setVisible(true);
                        tablePanel.revalidate();
                        tablePanel.repaint();
                        JOptionPane.showMessageDialog(this, "User added Successfully", "Success", JOptionPane.PLAIN_MESSAGE);
                        clearFields();
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
    private void updateData(int id, String userName, String password, int roleId, int column) {
        if (column == 0) {
            JOptionPane.showMessageDialog(null, "Select a cell to edit", "Error", JOptionPane.ERROR_MESSAGE);
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
            boolean input = data.updateUserAllFields(id, userName, password, roleId);
            if (input) {
                JOptionPane.showMessageDialog(this, "Update was successful", "Success", JOptionPane.PLAIN_MESSAGE);
                myTable.setStyle();
                scrollPane.setVisible(true);
                tablePanel.revalidate();
                tablePanel.repaint();
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
            userId = (int) myTable.getModel().getValueAt(row, 0);
            int choice = JOptionPane.showOptionDialog(this,
                    "Do you want to delete the selected row?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    null);
            if (choice == JOptionPane.YES_OPTION) {
                boolean input = data.deleteUser(userId);
                if (input) {
                    JOptionPane.showMessageDialog(this, "User Deleted Successfully", "Success", JOptionPane.PLAIN_MESSAGE);
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
                    userId = (int) myTable.getModel().getValueAt(row, 0);
                    String editedUserName = (String) myTable.getModel().getValueAt(row, 1);
                    if (editedUserName.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Enter a User Name", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        userName = editedUserName.strip();
                    }
                    Object passwordValue = myTable.getModel().getValueAt(row, 2);
                    if (passwordValue instanceof String) {
                        password = ((String) passwordValue).strip();
                    }

                    Object roleIdValue = myTable.getModel().getValueAt(row, 3);
                    try {
                        if (roleIdValue instanceof String) {
                            String roleIdStr = ((String) roleIdValue).trim();
                            roleId = Integer.parseInt(roleIdStr);
                        } else if (roleIdValue instanceof Integer) {
                            roleId = (Integer) roleIdValue;
                        }
                    } catch (NumberFormatException ex1) {
                        ex1.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Enter a valid Role ID", "Error", JOptionPane.ERROR_MESSAGE);
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

} // End of EditUserPanel class
