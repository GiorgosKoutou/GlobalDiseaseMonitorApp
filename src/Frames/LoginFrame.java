package Frames;

import Panels.MainPanels.MainPanel;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JMenuBar menuBar;
    private JMenu optionsMenu;
    private JMenuItem logoutMenuItem;
    private JMenuItem exitMenuItem;

    public LoginFrame() {
        super("Global Disease Analysis & Monitoring Hub");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500,200,500,500);
        setContentPane(new MainPanel());

        menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        optionsMenu = new JMenu("Options");
        menuBar.add(optionsMenu);

        logoutMenuItem = new JMenuItem("Logout");
        logoutMenuItem.addActionListener(e -> logout());
        optionsMenu.add(logoutMenuItem);

        optionsMenu.addSeparator();

        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> exit());
        optionsMenu.add(exitMenuItem);


        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private void exit(){
        int choice = JOptionPane.showOptionDialog(this,
                "Are you sure you want to exit? ",
                "Confirmation", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);
        if (choice == JOptionPane.YES_OPTION){
            JOptionPane.showMessageDialog(this, "Thank you for using our application!GoodBye!");
            System.exit(0);
        }
    }

    private void logout(){
        int choice = JOptionPane.showOptionDialog(this,
                "Are you sure you want to logout? ",
                "Confirmation", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);
        if (choice == JOptionPane.YES_OPTION){
            CardLayout layout = (CardLayout) getContentPane().getLayout();
            layout.show(getContentPane(), "Create/Login");
        }
    }
}
