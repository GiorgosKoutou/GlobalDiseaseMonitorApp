package Panels.MainPanels;

import Panels.SubPanels.AnalysisPanel;
import Panels.SubPanels.EditDataPanel;
import Panels.SubPanels.MapPanel;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {

    public AdminPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        this.setLayout(new BorderLayout());

        tabbedPane.addTab("World Map", new MapPanel());
        tabbedPane.addTab("Disease Analysis", new AnalysisPanel());
        tabbedPane.addTab("DataBase Management", new EditDataPanel());

        this.add(tabbedPane, BorderLayout.CENTER);
    }
}
