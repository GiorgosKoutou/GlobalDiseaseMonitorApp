package Components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TableManager extends JTable {

    static {
        UIManager.put("TableHeader.cellBorder", BorderFactory.createMatteBorder(1, 0, 1, 1, Color.LIGHT_GRAY));
    }

    private static DefaultTableCellRenderer cellRenderer;

    public TableManager(DefaultTableModel model) {
        this.setModel(model);
        cellRenderer = new DefaultTableCellRenderer();
        setStyle();
    }


    public void setStyle() {

        this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        this.setShowGrid(true);
        this.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        this.getTableHeader().setBackground(Color.DARK_GRAY);
        this.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.LIGHT_GRAY));
        this.setGridColor(Color.LIGHT_GRAY);

        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < this.getColumnCount(); i++) {
            this.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }


}

