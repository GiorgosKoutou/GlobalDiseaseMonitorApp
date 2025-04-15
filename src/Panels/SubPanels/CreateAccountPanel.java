package Panels.SubPanels;

import Components.ButtonManager;
import Components.LabelManager;
import Components.TextFieldManager;
import DiseaseDB.User;

import javax.swing.*;
import java.awt.*;

public class CreateAccountPanel extends JPanel {

    private LabelManager label;
    private TextFieldManager textField;
    private ButtonManager button;

    public CreateAccountPanel() {

        label = new LabelManager();
        textField = new TextFieldManager();
        button = new ButtonManager();

        label.getUserNameLabel().setForeground(Color.LIGHT_GRAY);
        label.getUserNameLabel().setFont(new Font("SansSerif", Font.PLAIN, 17));
        label.getPasswordLabel().setForeground(Color.LIGHT_GRAY);
        label.getPasswordLabel().setFont(new Font("SansSerif", Font.PLAIN, 17));


        textField.getUserNameTextField().setPreferredSize(new Dimension(200,30));
        textField.getUserNameTextField().setMaximumSize(new Dimension(200,30));
        textField.getPasswordTextField().setPreferredSize(new Dimension(200,30));
        textField.getPasswordTextField().setMaximumSize(new Dimension(200,30));

        textField.getPasswordTextField().addActionListener(e -> createAccount());
        button.getCreateButton().addActionListener(e -> createAccount());

        this.setPreferredSize(new Dimension(400,400));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,5,10,5);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridy = 0;
        gbc.gridx = 0;
        this.add(label.getUserNameLabel(),gbc);

        gbc.gridx = 1;
        this.add(textField.getUserNameTextField(),gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        this.add(label.getPasswordLabel(),gbc);

        gbc.gridx = 1;
        this.add(textField.getPasswordTextField(),gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        this.add(button.getCreateButton(),gbc);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon img = new ImageIcon(getClass().getResource("/LoginBG.jpg"));
        g.drawImage(img.getImage(), 0, 0,getWidth(),getHeight(), null);
    }

    private void createAccount(){
        String userName = textField.getUserNameTextField().getText();
        String password = String.valueOf(textField.getPasswordTextField().getPassword());

        User.createUser(userName,password);
        textField.getUserNameTextField().setText("");
        textField.getPasswordTextField().setText("");
    }
}
