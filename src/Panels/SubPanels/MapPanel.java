package Panels.SubPanels;

import DiseaseDB.DataBaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class MapPanel extends JPanel{ // Start of class

    public MapPanel() { // Start of constructor

        this.setLayout(null);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                handleMouseOver(e);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setToolTipText(null);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                handleMouseOver(e);
            }
        });
    } // End of constructor

    // *** PaintComponent Method *** //
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon img = new ImageIcon(getClass().getResource("/Map.jpg"));
        g.drawImage(img.getImage(), 0, 0,getWidth(),getHeight(), null);

        // *** America Pins *** //
        pin(g, 0.15, 0.3, 30, 30); // Canada Point
        pin(g, 0.18, 0.4, 30, 30); // US Point
        pin(g, 0.32, 0.65, 30, 30); // Brazil Point

        // *** Europe Pins *** //
        pin(g, 0.49, 0.35, 20, 20); // Germany Point
        pin(g, 0.47, 0.37, 20, 20); // France Point
        pin(g, 0.50, 0.39, 20, 20); // Italy Point
        pin(g, 0.525, 0.41, 20, 20); // Greece Point

        // *** Asia Pins *** //
        pin(g, 0.73, 0.43, 30, 30); // China Point
        pin(g, 0.75, 0.28, 30, 30); // Russia Point
        pin(g, 0.67, 0.49, 30, 30); // India Point

        // *** Africa Pins *** //
        pin(g, 0.545, 0.485, 20, 20); // Egypt Point
        pin(g, 0.485, 0.57, 20, 20); // Nigeria Point
        pin(g, 0.53, 0.76, 20, 20); // South Africa Point
        pin(g, 0.57, 0.615, 18, 18); // Kenya Point

        // *** Oceania Pins *** //
        pin(g, 0.83, 0.73, 30, 30); // Australia Point
        pin(g, 0.925, 0.83, 17, 17); // New Zealand Point

    }

    // *** Method to draw and set position of pins *** //
    private void pin(Graphics g, double relX, double relY, int width, int height) {
        g.setColor(Color.WHITE);
        g.drawOval((int) (getWidth() * relX), (int) (getHeight() * relY), width, height);
        g.setColor(Color.BLACK);
        g.fillOval((int) (getWidth() * relX), (int) (getHeight() * relY), width, height);
    }

    // *** Method to check if mouse is over a pin and display the data *** //
    private void handleMouseOver(MouseEvent e) { // Start of handleMouseOver method
        Point mousePos = e.getPoint();
        // *** America Pins *** //
        if (isMouseOverPin(mousePos, 0.15, 0.3, 30, 30)) {
            showToolTipData("Canada", mousePos);
        } else if (isMouseOverPin(mousePos, 0.18, 0.4, 30, 30)) {
            showToolTipData("United States", mousePos);
        } else if (isMouseOverPin(mousePos, 0.32, 0.65, 30, 30)) {
            showToolTipData("Brazil", mousePos);

        // *** Europe Pins *** //
        } else if (isMouseOverPin(mousePos, 0.49, 0.35, 20, 20)) {
            showToolTipData("Germany", mousePos);
        } else if (isMouseOverPin(mousePos, 0.47, 0.37, 20, 20)) {
            showToolTipData("France", mousePos);
        } else if (isMouseOverPin(mousePos, 0.50, 0.39, 20, 20)) {
            showToolTipData("Italy", mousePos);
        } else if (isMouseOverPin(mousePos, 0.525, 0.41, 20, 20)) {
            showToolTipData("Greece", mousePos);

        // *** Asia Pins *** //
        } else if (isMouseOverPin(mousePos, 0.73, 0.43, 30, 30)) {
            showToolTipData("China", mousePos);
        } else if (isMouseOverPin(mousePos, 0.75, 0.28, 30, 30)) {
            showToolTipData("Russia", mousePos);
        } else if (isMouseOverPin(mousePos, 0.67, 0.49, 30, 30)) {
            showToolTipData("India", mousePos);

        // *** Africa Pins *** //
        } else if (isMouseOverPin(mousePos, 0.545, 0.485, 20, 20)) {
            showToolTipData("Egypt", mousePos);
        } else if (isMouseOverPin(mousePos, 0.485, 0.57, 20, 20)) {
            showToolTipData("Nigeria", mousePos);
        } else if (isMouseOverPin(mousePos, 0.53, 0.76, 20, 20)) {
            showToolTipData("South Africa", mousePos);
        } else if (isMouseOverPin(mousePos, 0.57, 0.615, 18, 18)) {
            showToolTipData("Kenya", mousePos);

        // *** Oceania Pins *** //
        } else if (isMouseOverPin(mousePos, 0.83, 0.73, 30, 30)) {
            showToolTipData("Australia", mousePos);
        } else if (isMouseOverPin(mousePos, 0.925, 0.83, 17, 17)) {
            showToolTipData("New Zealand", mousePos);
        } else {
            setToolTipText(null);
        }
    }

    // *** Method to check if mouse is over a pin *** //
    private boolean isMouseOverPin(Point mousePos, double relX, double relY, int width, int height) { // Start of isMouseOverPin method
        int pinX = (int) (getWidth() * relX);
        int pinY = (int) (getHeight() * relY);
        return mousePos.x >= pinX && mousePos.x <= pinX + width && mousePos.y >= pinY && mousePos.y <= pinY + height;
    } // End of isMouseOverPin method

    // *** Method to show tooltip data *** //
    private void showToolTipData(String name, Point mousePos) { // Start of showToolTipData method
        try (Connection connection = DataBaseConnection.getConnection()) {
            String diseasesPerCountry = ("SELECT  diseases.name AS Disease_Name, " +
                    "SUM(disease_cases.cases) AS Total_Cases, " +
                    "SUM(disease_cases.deaths) AS Total_Deaths " +
                    "FROM countries " +
                    "JOIN disease_cases ON countries.ID = disease_cases.country_id " +
                    "JOIN diseases ON disease_cases.disease_id = diseases.ID " +
                    "WHERE countries.country_name = ? " +
                    "GROUP BY diseases.ID, countries.ID " +
                    "ORDER BY Total_Cases DESC");
            PreparedStatement statement = connection.prepareStatement(diseasesPerCountry);
            statement.setString(1, name);
            statement.execute();

            ResultSet resultSet = statement.getResultSet();

            boolean foundData = false;
            StringBuilder tooltipText = new StringBuilder("<html> <table border = '1'> <caption><b>" + name + "</b></caption> ");
            tooltipText.append("<tr><th>Disease</th><th>Total Cases</th><th>Total Deaths</th></tr>");
            while (resultSet.next()) {
                foundData = true;
                String diseaseName = resultSet.getString("Disease_Name");
                int totalCases = resultSet.getInt("Total_Cases");
                int totalDeaths = resultSet.getInt("Total_Deaths");

                tooltipText.append("<tr><td>" + diseaseName + "</td><td>" + totalCases + "</td><td>" + totalDeaths + "</td></tr>");
            }
            tooltipText.append("</table></html>");
            if (!foundData) {
                this.setToolTipText("<html><i><b> No data available for "  + name + "</b></i></html>" );
            }else {
                this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                this.setToolTipText(tooltipText.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    } // End of showToolTipData method

} // End of class
