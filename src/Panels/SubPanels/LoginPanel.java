package Panels.SubPanels;

import Components.ButtonManager;
import Components.LabelManager;
import Components.TextFieldManager;
import DiseaseDB.DataBaseConnection;
//import TestsForGUI.TestUserFrame;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginPanel extends JPanel {

    private JPanel mainPanel;
    private CardLayout layout;

    private LabelManager label;
    private TextFieldManager text;
    private ButtonManager button;

    // *** Constructor *** //
    public LoginPanel(JPanel mainPanel, CardLayout layout) { // Start of the constructor

        this.mainPanel = mainPanel;
        this.layout = layout;

        label = new LabelManager();
        text = new TextFieldManager();
        button = new ButtonManager();

        label.getUserNameLabel().setForeground(Color.LIGHT_GRAY);
        label.getUserNameLabel().setFont(new Font("SansSerif", Font.PLAIN, 17));
        label.getPasswordLabel().setForeground(Color.LIGHT_GRAY);
        label.getPasswordLabel().setFont(new Font("SansSerif", Font.PLAIN, 17));

        // *** Set up the panel *** //
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,5,10,5);
        gbc.anchor = GridBagConstraints.CENTER;

        // *** Add the Labels and set their positions *** //
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(label.getUserNameLabel(), gbc);

        gbc.gridy = 1;
        this.add(label.getPasswordLabel(), gbc);

        // *** Add the TextFields and set their positions *** //
        gbc.gridx = 1;
        gbc.gridy = 0;
        text.getUserNameTextField().setPreferredSize(new Dimension(200,30));
        this.add(text.getUserNameTextField(), gbc);

        gbc.gridy = 1;
        text.getPasswordTextField().setPreferredSize(new Dimension(200,30));
        text.getPasswordTextField().addActionListener(e -> login());
        this.add(text.getPasswordTextField(), gbc);

        // *** Add the Buttons and set their positions *** //
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        button.getLoginButton().setPreferredSize(new Dimension(100,30));
        button.getLoginButton().addActionListener(e -> login());
        this.add(button.getLoginButton(), gbc);


    } // End of the constructor

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon img = new ImageIcon(getClass().getResource("/LoginBG.jpg"));
        g.drawImage(img.getImage(), 0, 0,getWidth(),getHeight(), null);
    }


    // *** Method to identify the user *** //
    private void login(){ // Start of the login method
        String userName = text.getUserNameTextField().getText();
        try (Connection connection = DataBaseConnection.getConnection()){
            String password = String.valueOf(text.getPasswordTextField().getPassword());

            String selectQuery = ("SELECT role_id FROM users WHERE password = ? AND username = ?");
            PreparedStatement selectStm = connection.prepareStatement(selectQuery);
            selectStm.setString(1,password);
            selectStm.setString(2,userName);

            ResultSet rs = selectStm.executeQuery();
            if (rs.next()){
                int role_id = rs.getInt(1);

                if (role_id == 1){
                   layout.show(mainPanel, "Admin");
                }else if (role_id == 2){
                   layout.show(mainPanel, "Analyst");
                }else {
                    layout.show(mainPanel, "User");
                }
            }else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } // End of the login method
}
