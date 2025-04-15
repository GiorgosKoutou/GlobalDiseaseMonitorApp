package Panels.SubPanels;

import Components.ButtonManager;
import Panels.SubPanels.DBEditPanels.*;

import javax.swing.*;
import java.awt.*;

public class EditDataPanel extends JPanel {

    private ButtonManager button;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public EditDataPanel() { //Start of constructor
        this.setLayout(new BorderLayout());

        this.add(westPanel(), BorderLayout.WEST);
        this.add(centerPanel(), BorderLayout.CENTER);

    } //End of constructor

    /* ================= Start of westPanel =================== */
    private JPanel westPanel() {

        JPanel panel = new JPanel();
        panel.setBackground(Color.DARK_GRAY);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(300, this.getHeight()));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        JLabel title = new JLabel("Choose A Table");
        title.setFont(new Font("SansSerif", Font.PLAIN, 30));
        title.setForeground(Color.WHITE);

        button = new ButtonManager();

        setButtonStyle();

        button.getCountryTableButton().addActionListener(e -> cardLayout.show(cardPanel, "Country"));
        button.getContinentTableButton().addActionListener(e -> cardLayout.show(cardPanel, "Continent"));
        button.getDiseaseTableButton().addActionListener(e -> cardLayout.show(cardPanel, "DiseaseTable"));
        button.getDiseaseCasesTableButton().addActionListener(e -> cardLayout.show(cardPanel, "DiseaseCasesTable"));
        button.getReportsTableButton().addActionListener(e -> cardLayout.show(cardPanel, "ReportsTable"));
        button.getUsersTableButton().addActionListener(e -> cardLayout.show(cardPanel, "UsersTable"));

        panel.add(Box.createRigidArea(new Dimension(100, 20)));
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 60)));
        panel.add(button.getCountryTableButton());
        panel.add(Box.createRigidArea(new Dimension(0, 60)));
        panel.add(button.getContinentTableButton());
        panel.add(Box.createRigidArea(new Dimension(0, 60)));
        panel.add(button.getDiseaseTableButton());
        panel.add(Box.createRigidArea(new Dimension(0, 60)));
        panel.add(button.getDiseaseCasesTableButton());
        panel.add(Box.createRigidArea(new Dimension(0, 60)));
        panel.add(button.getReportsTableButton());
        panel.add(Box.createRigidArea(new Dimension(0, 60)));
        panel.add(button.getUsersTableButton());

        return panel;
    }
    /* ================== End of westPanel =================== */


    /* ================= Start of centerPanel =================== */
    private JPanel centerPanel() {

        cardPanel = new JPanel();
        cardPanel.setBackground(Color.GRAY);
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        cardPanel.add(new EditCountryPanel(), "Country");
        cardPanel.add(new EditContinentPanel(), "Continent");
        cardPanel.add(new EditDiseaseTable(), "DiseaseTable");
        cardPanel.add(new EditDiseaseCasesPanel(), "DiseaseCasesTable");
        cardPanel.add(new ReportsEditPanel(), "ReportsTable");
        cardPanel.add(new EditUserPanel(), "UsersTable");

        return cardPanel;
    }
    /* ================== End of centerPanel =================== */

    /* ================== Start of Butonns Stryle ============== */
    private void setButtonStyle() {
        button.getCountryTableButton().setPreferredSize(new Dimension(180, 40));
        button.getCountryTableButton().setMaximumSize(new Dimension(180, 40));
        button.getContinentTableButton().setPreferredSize(new Dimension(180, 40));
        button.getContinentTableButton().setMaximumSize(new Dimension(180, 40));
        button.getDiseaseTableButton().setPreferredSize(new Dimension(180, 40));
        button.getDiseaseTableButton().setMaximumSize(new Dimension(180, 40));
        button.getDiseaseCasesTableButton().setPreferredSize(new Dimension(190, 40));
        button.getDiseaseCasesTableButton().setMaximumSize(new Dimension(190, 40));
        button.getReportsTableButton().setPreferredSize(new Dimension(180, 40));
        button.getReportsTableButton().setMaximumSize(new Dimension(180, 40));
        button.getUsersTableButton().setPreferredSize(new Dimension(180, 40));
        button.getUsersTableButton().setMaximumSize(new Dimension(180, 40));
    }
    /* ================== End of Butonns Stryle ============== */
}
