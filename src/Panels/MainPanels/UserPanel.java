package Panels.MainPanels;

import Panels.SubPanels.MapPanel;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel{

    public UserPanel() {
        this.setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("World Map", new MapPanel());

        this.add(tabbedPane, BorderLayout.CENTER);
    }
}
