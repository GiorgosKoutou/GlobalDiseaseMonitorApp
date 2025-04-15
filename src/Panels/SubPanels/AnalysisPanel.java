package Panels.SubPanels;

import Components.*;
import DiseaseDB.DataBaseConnection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;


public class AnalysisPanel extends JPanel { // Start of AnalysisPanel class

    private String countryName;
    private String continentName;
    private String diseaseName;
    private String startDateFormatted;
    private String endDateFormatted;

    private ButtonManager button;
    private LabelManager label;
    private ComboBoxManager comboBox;
    private DateChooserManager dChooser;
    private TableManager myTable;

    private DefaultTableModel model;
    private JScrollPane scrollPane;

    private ChartPanel barChartPanel;
    private DefaultCategoryDataset dataset;
    private JFreeChart barChart;

    public AnalysisPanel() { // Start of constructor
        this.setLayout(new BorderLayout());


        this.add(northPanel(), BorderLayout.NORTH);
        this.add(westPanel(), BorderLayout.WEST);
        this.add(centerPanel(), BorderLayout.CENTER);
    } // End of constructor

    /* =================== Start of northPanel =================== */
    private JPanel northPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(),
                "Disease Monitoring and Analysis",
                TitledBorder.CENTER,
                TitledBorder.CENTER,
                new Font("SansSerif", Font.PLAIN, 30),
                Color.WHITE
        ));
        panel.setPreferredSize(new Dimension(this.getWidth(), 90));
        return panel;
    }
    /* =================== End of northPanel =================== */

    /* =================== Start of westPanel =================== */
    private JPanel westPanel() { // Start of westPanel

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.setPreferredSize(new Dimension(330, this.getHeight()));

        button = new ButtonManager();
        label = new LabelManager();
        comboBox = new ComboBoxManager();
        dChooser = new DateChooserManager();

        /** Start of ComboBox Listeners **/
        updateComboBox(String.valueOf(comboBox.getTotalPerContinentPerPeriod().getSelectedItem()), comboBox.getTotalPerCountryPerPeriod());
        comboBox.getTotalPerContinentPerPeriod().addActionListener(e -> {
            String selection = String.valueOf(comboBox.getTotalPerContinentPerPeriod().getSelectedItem());
            updateComboBox(selection, comboBox.getTotalPerCountryPerPeriod());
        });

        updateComboBox(String.valueOf(comboBox.getContinentReportPerPeriod().getSelectedItem()), comboBox.getCountryReportPerPeriod());
        comboBox.getContinentReportPerPeriod().addActionListener(e -> {
            String selection = String.valueOf(comboBox.getContinentReportPerPeriod().getSelectedItem());
            updateComboBox(selection, comboBox.getCountryReportPerPeriod());
        });

        updateComboBox(String.valueOf(comboBox.getContinentAvgPerPeriod().getSelectedItem()), comboBox.getCountryAvgPerPeriod());
        comboBox.getContinentAvgPerPeriod().addActionListener(e -> {
            String selection = String.valueOf(comboBox.getContinentAvgPerPeriod().getSelectedItem());
            updateComboBox(selection, comboBox.getCountryAvgPerPeriod());
        });
        /** End of ComboBox Listeners **/

        /** Start of Button Listeners **/
        button.getViewContinentTotalButton().addActionListener(e -> {
            viewContinentTotal();
            scrollPane.setVisible(true);
            scrollPane.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEmptyBorder(),
                    "Total Cases/Deaths in " + continentName,
                    TitledBorder.CENTER,
                    TitledBorder.CENTER,
                    new Font("SansSerif", Font.PLAIN, 20),
                    Color.WHITE
            ));
            barChartPanel.setVisible(true);
            revalidate();
            repaint();
        });
        button.getViewCountryTotalButton().addActionListener(e -> {
            viewCountryTotal();
            scrollPane.setVisible(true);
            scrollPane.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEmptyBorder(),
                    "Total Cases/Deaths in " + countryName,
                    TitledBorder.CENTER,
                    TitledBorder.CENTER,
                    new Font("SansSerif", Font.PLAIN, 20),
                    Color.WHITE
            ));
            barChartPanel.setVisible(true);
            revalidate();
            repaint();
        });

        button.getViewTotalPerPeriodButton().addActionListener(e -> {
            viewTotalPerPeriod();
            scrollPane.setVisible(true);
            scrollPane.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEmptyBorder(),
                    "Total Cases/Deaths in " + countryName + " from " +
                    startDateFormatted + " to " + endDateFormatted,
                    TitledBorder.CENTER,
                    TitledBorder.CENTER,
                    new Font("SansSerif", Font.PLAIN, 20),
                    Color.WHITE
            ));
            barChartPanel.setVisible(true);
            revalidate();
            repaint();
        });

        button.getViewReportPerPeriodButton().addActionListener(e -> {
            viewReportsPerPeriod();
            scrollPane.setVisible(true);
            barChartPanel.setVisible(true);
            revalidate();
            repaint();
        });

        button.getViewAvgPerPeriodButton().addActionListener(e -> {
            viewAvgPerPeriod();
            scrollPane.setVisible(true);
            barChartPanel.setVisible(true);
            revalidate();
            repaint();
        });

        button.getRefreshButton().addActionListener(e -> {
            loadContinents();
            loadCountries();
            loadDiseases();
        });
        /** End of Button Listeners **/

        /** Start of Total Case/Deaths per Continent **/
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(label.getContinentTotal(), gbc);

        gbc.gridy = 1;
        panel.add(comboBox.getTotalPerContinent(), gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(button.getViewContinentTotalButton(), gbc);
        /** End of Total Cases/Deaths per Continent **/

        /** Start of Total Cases/Deaths per country **/
        gbc.gridy = 3;
        panel.add(label.getCountryTotal(), gbc);

        gbc.gridy = 4;
        panel.add(comboBox.getTotalPerCountry(), gbc);

        gbc.gridy = 5;
        panel.add(button.getViewCountryTotalButton(), gbc);
        /**  End of Total Cases/Deaths per Country **/

        /** Start of Total Cases/Deaths Over a Selected Period **/
        gbc.gridy = 6;
        panel.add(label.getTotalPerPeriod(), gbc);

        gbc.gridy = 7;
        gbc.gridwidth = 1;
        panel.add(comboBox.getTotalPerContinentPerPeriod(), gbc);

        gbc.gridx = 1;
        panel.add(comboBox.getTotalPerCountryPerPeriod(), gbc);

        gbc.gridy = 8;
        gbc.gridx = 0;
        panel.add(dChooser.getStartDateforTotal(), gbc);

        gbc.gridx = 1;
        panel.add(dChooser.getEndDateForTotal(), gbc);

        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(button.getViewTotalPerPeriodButton(), gbc);
        /** End of Total Cases/Deaths Over a Selected Period **/


        /** Start of Reported Cases/Deaths per Disease Over a Selected Period **/
        gbc.gridy = 10;
        panel.add(label.getReportPerPeriod(), gbc);

        gbc.gridy = 11;
        gbc.gridwidth = 1;
        panel.add(comboBox.getContinentReportPerPeriod(), gbc);

        gbc.gridx = 1;
        panel.add(comboBox.getCountryReportPerPeriod(), gbc);

        gbc.gridy = 12;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(comboBox.getDiseasePerPeriod(), gbc);

        gbc.gridy = 13;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(dChooser.getStartDateForReport(), gbc);

        gbc.gridx = 1;
        panel.add(dChooser.getEndDateForReport(), gbc);

        gbc.gridy = 14;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(button.getViewReportPerPeriodButton(), gbc);
        /** End of Reported Cases/Deaths perDisease Over a Selected Period **/

        /** Start of Average Cases/Deaths Over a Selected Period **/
        gbc.gridy = 15;
        panel.add(label.getAveragePerPeriod(), gbc);

        gbc.gridy = 16;
        gbc.gridwidth = 1;
        panel.add(comboBox.getContinentAvgPerPeriod(), gbc);

        gbc.gridx = 1;
        panel.add(comboBox.getCountryAvgPerPeriod(), gbc);

        gbc.gridy = 17;
        gbc.gridx = 0;
        panel.add(dChooser.getStartDateForAvg(), gbc);

        gbc.gridx = 1;
        panel.add(dChooser.getEndDateForAvg(), gbc);

        gbc.gridy = 18;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(button.getViewAvgPerPeriodButton(), gbc);
        /** End of Average Cases/Deaths Over a Selected Period **/

        /** Start of Refresh Button **/
        gbc.gridy = 19;
        panel.add(button.getRefreshButton(), gbc);
        /** End of Refresh Button **/

        return panel;
    }
    /* =================== End of westPanel =================== */

    /* ================== Start of centerPanel =================== */
    private JPanel centerPanel() { // Start of centerPanel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setPreferredSize(new Dimension(this.getWidth(), 200));
        model = new DefaultTableModel(new String[]{"Disease Name", "Total Cases", "Total Deaths"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        myTable = new TableManager(model);
        myTable.setCellSelectionEnabled(false);
        myTable.setRowSelectionAllowed(false);
        myTable.setColumnSelectionAllowed(false);
        myTable.setFocusable(false);
        scrollPane = new JScrollPane(myTable);
        scrollPane.setVisible(false);
        tablePanel.add(scrollPane);

        dataset = new DefaultCategoryDataset();
        barChart = ChartFactory.createBarChart(
                "Disease Analysis",
                "Diseases",
                "Cases",
                dataset
        );

        barChartPanel = new ChartPanel(barChart);
        barChartPanel.setVisible(false);

        panel.add(barChartPanel, BorderLayout.CENTER);
        panel.add(tablePanel, BorderLayout.NORTH);

        return panel;
    }
    /* ================== End of centerPanel =================== */

    /* ================== Start of viewCountryTotal =================== */
    private void viewCountryTotal() {
        try (Connection connection = DataBaseConnection.getConnection()) {
            countryName = String.valueOf(comboBox.getTotalPerCountry().getSelectedItem());

            String viewDataQuery = ("SELECT diseases.name, " +
                    "SUM(disease_cases.cases) AS Total_Cases, " +
                    "SUM(disease_cases.deaths) AS Total_Deaths " +
                    "FROM countries " +
                    "JOIN disease_cases ON countries.ID = disease_cases.country_id " +
                    "JOIN diseases ON disease_cases.disease_id = diseases.ID " +
                    "WHERE countries.country_name = ? " +
                    "GROUP BY diseases.name " +
                    "ORDER BY Total_Cases DESC");
            PreparedStatement statement = connection.prepareStatement(viewDataQuery);
            statement.setString(1, countryName);

            ResultSet rs = statement.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString(1),
                        rs.getInt(2),
                        rs.getInt(3)
                });
                String diseaseName = rs.getString(1);
                int totalCases = rs.getInt(2);
                int totalDeaths = rs.getInt(3);
                dataset.addValue(totalCases, "Total_Cases", diseaseName);
                dataset.addValue(totalDeaths, "Total_Deaths", diseaseName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /* ================== End of viewCountryTotal =================== */

    /* ================== Start of viewContinentTotal =================== */
    private void viewContinentTotal() {
        try (Connection connection = DataBaseConnection.getConnection()) {
            continentName = String.valueOf(comboBox.getTotalPerContinent().getSelectedItem());
            if (continentName.equals("Worldwide")) {

                String query = ("SELECT diseases.name, SUM(disease_cases.cases) AS Total_Cases," +
                        "SUM(disease_cases.deaths) AS Total_Deaths " +
                        "FROM continents " +
                        "JOIN countries ON continents.ID = countries.continent_id " +
                        "JOIN disease_cases ON countries.ID = disease_cases.country_id " +
                        "JOIN diseases ON disease_cases.disease_id = diseases.ID " +
                        "GROUP BY diseases.name " +
                        "ORDER BY Total_Cases DESC");

                Statement statement = connection.createStatement();

                ResultSet rs = statement.executeQuery(query);

                model.setRowCount(0);
                dataset.clear();
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString(1),
                            rs.getInt(2),
                            rs.getInt(3)
                    });

                    String diseaseName = rs.getString(1);
                    int totalCases = rs.getInt(2);
                    int totalDeaths = rs.getInt(3);
                    dataset.addValue(totalCases, "Total_Cases", diseaseName);
                    dataset.addValue(totalDeaths, "Total_Deaths", diseaseName);
                }


            } else {
                String query = ("SELECT diseases.name, " +
                        "SUM(disease_cases.cases) AS Total_Cases, " +
                        "SUM(disease_cases.deaths) AS Total_Deaths " +
                        "FROM continents " +
                        "JOIN countries ON continents.ID = countries.continent_id " +
                        "JOIN disease_cases ON countries.ID = disease_cases.country_id " +
                        "JOIN diseases ON disease_cases.disease_id = diseases.ID " +
                        "WHERE continents.continent_name = ? " +
                        "GROUP BY diseases.name " +
                        "ORDER BY Total_Cases DESC");

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, continentName);

                ResultSet rs = statement.executeQuery();
                model.setRowCount(0);
                dataset.clear();
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString(1),
                            rs.getInt(2),
                            rs.getInt(3)
                    });

                    String diseaseName = rs.getString(1);
                    int totalCases = rs.getInt(2);
                    int totalDeaths = rs.getInt(3);
                    dataset.addValue(totalCases, "Total_Cases", diseaseName);
                    dataset.addValue(totalDeaths, "Total_Deaths", diseaseName);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /* ================== End of viewContinentTotal =================== */

    /* ================== Start of viewTotalPerPeriod =================== */
    private void viewTotalPerPeriod() {
        try (Connection connection = DataBaseConnection.getConnection()) {
            continentName = String.valueOf(comboBox.getTotalPerContinentPerPeriod().getSelectedItem());
            countryName = String.valueOf(comboBox.getTotalPerCountryPerPeriod().getSelectedItem());

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date startDate = dChooser.getStartDateforTotal().getDate();
            java.util.Date endDate = dChooser.getEndDateForTotal().getDate();
            startDateFormatted = format.format(startDate);
            endDateFormatted = format.format(endDate);

            java.sql.Date sqlStartdate = new java.sql.Date(startDate.getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());

            String query = ("SELECT diseases.name AS Disease_Name, SUM(disease_cases.cases) AS Total_Cases, SUM(disease_cases.deaths) AS Total_Deaths " +
                            "FROM continents " +
                            "JOIN countries ON continents.ID = countries.continent_id " +
                            "JOIN disease_cases ON countries.ID = disease_cases.country_id " +
                            "JOIN diseases ON disease_cases.disease_id = diseases.ID " +
                            "WHERE continents.continent_name = ? " +
                                 "    AND countries.country_name = ? " +
                                "       AND disease_cases.case_report_date BETWEEN ? AND ?" +
                            "GROUP BY Disease_Name " +
                            "ORDER BY Total_Cases DESC");

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, continentName);
            statement.setString(2, countryName);
            statement.setDate(3, sqlStartdate);
            statement.setDate(4, sqlEndDate);

            ResultSet rs = statement.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "No data found", "Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                model.setRowCount(0);
                dataset.clear();
                do {
                    model.addRow(new Object[]{
                            rs.getString(1),
                            rs.getInt(2),
                            rs.getInt(3)
                    });
                    dataset.addValue(rs.getInt(2), "Total_Cases", rs.getString(1));
                    dataset.addValue(rs.getInt(3), "Total_Deaths", rs.getString(1));

                } while (rs.next());
            }

        } catch (SQLException | NullPointerException e) {
            if (e.getClass() == NullPointerException.class) {
                JOptionPane.showMessageDialog(null, "Please Select Start/End Date", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database Connection Failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    /* ================== End of viewTotalPerPeriod =================== */

    /* ================== Start of viewReportsPerPeriod =================== */
    private void viewReportsPerPeriod() { // Start of viewCountryTotalPerPeriod
        try (Connection connection = DataBaseConnection.getConnection()) {
            continentName = String.valueOf(comboBox.getContinentReportPerPeriod().getSelectedItem());
            countryName = String.valueOf(comboBox.getTotalPerCountryPerPeriod().getSelectedItem());
            diseaseName = String.valueOf(comboBox.getDiseasePerPeriod().getSelectedItem());

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date startDate = dChooser.getStartDateForReport().getDate();
            java.util.Date endDate = dChooser.getEndDateForReport().getDate();
            startDateFormatted = format.format(startDate);
            endDateFormatted = format.format(endDate);

            java.sql.Date sqlStartdate = new java.sql.Date(startDate.getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());


            String query = ("SELECT disease_cases.cases AS Cases, disease_cases.deaths AS Deaths, disease_cases.case_report_date AS Report_Date " +
                    "FROM continents " +
                    "JOIN countries ON continents.ID\t= countries.continent_id " +
                    "JOIN disease_cases ON countries.ID = disease_cases.country_id " +
                    "JOIN diseases ON disease_cases.disease_id = diseases.ID " +
                    "WHERE continents.continent_name = ? " +
                    "AND countries.country_name = ? " +
                    "AND diseases.name = ? " +
                    "AND disease_cases.case_report_date BETWEEN ? AND ? " +
                    "ORDER BY countries.country_name, Report_Date");

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, continentName);
            statement.setString(2, countryName);
            statement.setString(3, diseaseName);
            statement.setDate(4, sqlStartdate);
            statement.setDate(5, sqlEndDate);

            ResultSet rs = statement.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "No data found", "Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                model.setRowCount(0);
                dataset.clear();
                myTable.getColumnModel().getColumn(0).setHeaderValue("Total_Cases");
                myTable.getColumnModel().getColumn(1).setHeaderValue("Total_Deaths");
                myTable.getColumnModel().getColumn(2).setHeaderValue("Report_Date");
                dataset.clear();
                do {
                    model.addRow(new Object[]{
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getDate(3)
                    });
                    dataset.addValue(rs.getInt(1), "Total_Cases", rs.getDate(3));
                    dataset.addValue(rs.getInt(2), "Total_Deaths", rs.getDate(3));

                } while (rs.next());
            }
        } catch (SQLException | NullPointerException e) {
            if (e.getClass() == NullPointerException.class) {
                JOptionPane.showMessageDialog(null, "Please Select Start/End Date", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database Connection Failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
   /* ================== End of viewReportsPerPeriod =================== */

    /* ================== Start of viewAvgPerPeriod =================== */
    private void viewAvgPerPeriod() { // Start of viewAvgPerPeriod
        try (Connection connection = DataBaseConnection.getConnection()) {
            continentName = String.valueOf(comboBox.getContinentAvgPerPeriod().getSelectedItem());
            countryName = String.valueOf(comboBox.getCountryAvgPerPeriod().getSelectedItem());

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date startDate = dChooser.getStartDateForAvg().getDate();
            java.util.Date endDate = dChooser.getEndDateForAvg().getDate();
            startDateFormatted = format.format(startDate);
            endDateFormatted = format.format(endDate);

            java.sql.Date sqlStartdate = new java.sql.Date(startDate.getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());

            String query = ("SELECT diseases.name, CAST(ROUND(AVG(disease_cases.cases)) AS SIGNED) AS Average_Cases," +
                    "CAST(ROUND(AVG(disease_cases.deaths)) AS SIGNED) AS Average_Deaths " +
                    "FROM continents " +
                    "JOIN countries ON continents.ID = countries.continent_id " +
                    "JOIN disease_cases ON countries.ID = disease_cases.country_id " +
                    "JOIN diseases ON disease_cases.disease_id = diseases.ID " +
                    "WHERE continents.continent_name = ? " +
                    "AND countries.country_name = ? " +
                    "AND disease_cases.case_report_date BETWEEN ? AND ? " +
                    "GROUP BY diseases.name\n" +
                    "ORDER BY 2 DESC");

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, continentName);
            statement.setString(2, countryName);
            statement.setDate(3, sqlStartdate);
            statement.setDate(4, sqlEndDate);

            ResultSet rs = statement.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "No data found", "Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                model.setRowCount(0);
                dataset.clear();
                myTable.getColumnModel().getColumn(0).setHeaderValue("Disease Name");
                myTable.getColumnModel().getColumn(1).setHeaderValue("Average Cases");
                myTable.getColumnModel().getColumn(2).setHeaderValue("Average Deaths");
                dataset.clear();
                do {
                    model.addRow(new Object[]{
                            rs.getString(1),
                            rs.getInt(2),
                            rs.getInt(3)
                    });
                    dataset.addValue(rs.getInt(2), "Average Cases", rs.getString(1));
                    dataset.addValue(rs.getInt(3), "Average Deaths", rs.getString(1));

                } while (rs.next());
            }
        } catch (SQLException | NullPointerException e) {
            if (e.getClass() == NullPointerException.class) {
                JOptionPane.showMessageDialog(null, "Please Select Start/End Date", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database Connection Failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    /* ================== End of viewAvgPerPeriod =================== */

    /*  ================== Start of loadCountries =================== */
    private void loadCountries() { // Start of loadCountries
        try (Connection connection = DataBaseConnection.getConnection()) {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT country_name FROM countries");

            comboBox.getTotalPerCountry().removeAllItems();
            comboBox.getTotalPerCountryPerPeriod().removeAllItems();
            comboBox.getCountryAvgPerPeriod().removeAllItems();
            while (rs.next()) {
                comboBox.getTotalPerCountry().addItem(rs.getString("country_name"));
                comboBox.getTotalPerCountryPerPeriod().addItem(rs.getString("country_name"));
                comboBox.getCountryAvgPerPeriod().addItem(rs.getString("country_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*  ================== End of loadCountries =================== */

    /*  ================== Start of loadDiseases =================== */
    private void loadDiseases() { // Start of loadDiseases
        try (Connection connection = DataBaseConnection.getConnection()) {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT name FROM diseases");
            comboBox.getDiseasePerPeriod().removeAllItems();
            while (rs.next()) {
                comboBox.getDiseasePerPeriod().addItem(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*  ================== End of loadDiseases =================== */

    /*  ================== Start of loadContinents =================== */
    private void loadContinents() { // Start of loadContinents
        try (Connection connection = DataBaseConnection.getConnection()) {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT continent_name FROM continents");

            comboBox.getTotalPerContinent().removeAllItems();
            comboBox.getTotalPerContinentPerPeriod().removeAllItems();
            comboBox.getContinentReportPerPeriod().removeAllItems();
            comboBox.getContinentAvgPerPeriod().removeAllItems();
            while (rs.next()) {
                comboBox.getTotalPerContinent().addItem(rs.getString("continent_name"));
                comboBox.getTotalPerContinentPerPeriod().addItem(rs.getString("continent_name"));
                comboBox.getContinentReportPerPeriod().addItem(rs.getString("continent_name"));
                comboBox.getContinentAvgPerPeriod().addItem(rs.getString("continent_name"));
            }
            comboBox.getTotalPerContinent().addItem("Worldwide");
            comboBox.getTotalPerContinentPerPeriod().addItem("Worldwide");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*  ================== End of loadContinents =================== */

    /*  ================== Start of updateComboBox =================== */
    private void updateComboBox(String continentName, JComboBox comboBox) { // Start of updateCountryBox

        comboBox.removeAllItems();

        if (continentName.equals("Africa")) {
            comboBox.addItem("Nigeria");
            comboBox.addItem("Egypt");
            comboBox.addItem("South Africa");
            comboBox.addItem("Ghana");
        } else if (continentName.equals("Asia")) {
            comboBox.addItem("China");
            comboBox.addItem("India");
            comboBox.addItem("Russia");
        } else if (continentName.equals("Europe")) {
            comboBox.addItem("Greece");
            comboBox.addItem("Germany");
            comboBox.addItem("France");
            comboBox.addItem("Italy");
        } else if (continentName.equals("North America")) {
            comboBox.addItem("United States");
            comboBox.addItem("Canada");
        } else if (continentName.equals("South America")) {
            comboBox.addItem("Brazil");
        }
    }
    /*  ================== End of updateComboBox =================== */

} // End of AnalysisPanel class



