package Panels.MainPanels;

import Panels.SubPanels.AnalysisPanel;
import Panels.SubPanels.MapPanel;

import javax.swing.*;
import java.awt.*;

public class AnalystPanel extends JPanel {

    static {
        UIManager.put("TabbedPane.tabAreaInsets", new Insets(10, 20, 10, 20));
        UIManager.put("TabbedPane.tabInsets", new Insets(10, 20, 10, 20));
        UIManager.put("TabbedPane.tabAreaBackground", Color.GRAY);
        UIManager.put("TabbedPane.tabAreaHighlight", Color.WHITE);
        UIManager.put("TabbedPane.darkShadow", Color.BLACK);
        UIManager.put("TabbedPane.selectColor", Color.WHITE);
    }

    public AnalystPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        this.setLayout(new BorderLayout());

        tabbedPane.addTab("World Map", new MapPanel());
        tabbedPane.addTab("Disease Analysis", new AnalysisPanel());

        this.add(tabbedPane, BorderLayout.CENTER);
    }
}
