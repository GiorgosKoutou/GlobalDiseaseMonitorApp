package Panels.MainPanels;

import Components.ButtonManager;
import Panels.SubPanels.CreateAccountPanel;
import Panels.SubPanels.LoginPanel;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {

    private CardLayout mainCardLayout;
    private ButtonManager button;


    public MainPanel() {

        button = new ButtonManager();
        mainCardLayout = new CardLayout();
        this.setLayout(mainCardLayout);

        this.add(centerPanel(), "Create/Login");
        this.add(new AdminPanel(), "Admin");
        this.add(new AnalystPanel(), "Analyst");
        this.add(new UserPanel(), "User");

        mainCardLayout.show(this, "Create/Login");
    }

    private JPanel centerPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
        westPanel.setPreferredSize(new Dimension(200, 500));
        westPanel.setBackground(Color.DARK_GRAY);
        westPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JPanel centerPanel = new JPanel();
        CardLayout centerCardLayout = new CardLayout();
        centerPanel.setLayout(centerCardLayout);
        centerPanel.add(new LoginPanel(this, mainCardLayout), "Login");
        centerPanel.add(new CreateAccountPanel(), "CreateAccount");


        button.getLoginButton().setPreferredSize(new Dimension(100, 40));
        button.getLoginButton().setMaximumSize(new Dimension(100, 40));

        button.getCreateButton().setPreferredSize(new Dimension(150, 40));
        button.getCreateButton().setMaximumSize(new Dimension(150, 40));

        westPanel.add(Box.createRigidArea(new Dimension(50, 300)));
        westPanel.add(button.getLoginButton());
        button.getLoginButton().addActionListener(e -> centerCardLayout.show(centerPanel, "Login"));
        westPanel.add(Box.createRigidArea(new Dimension(50, 50)));
        westPanel.add(button.getCreateButton());
        button.getCreateButton().addActionListener(e -> centerCardLayout.show(centerPanel, "CreateAccount"));


        mainPanel.add(westPanel, BorderLayout.WEST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        return mainPanel;
    }
}
