package Components;


import javax.swing.*;

/**
 * Manages and provides access to various JButton objects for different functionalities.
 */
public class ButtonManager extends JButton { //  Start of ButtonManager Class

    /* *** Button For Login and Create Account Panel *** */
    private JButton loginButton;
    private JButton createButton;

    /* *** Button For Edit Table Panel *** */
    private JButton addButton;
    private JButton removeButton;
    private JButton searchButton;
    private JButton clearButton;
    private JButton viewButton;
    private JButton updteButton;
    private JButton importButton;

    /* *** Button For Analysis Panel *** */
    private JButton refreshButton;
    private JButton viewContinentTotalButton;
    private JButton viewCountryTotalButton;
    private JButton viewTotalPerPeriodButton;
    private JButton viewReportPerPeriodButton;
    private JButton viewContinentReportButton;
    private JButton viewAvgPerPeriodButton;

    /* *** Buttons for Data Base Tables *** */
    private JButton countryTableButton;
    private JButton continentTableButton;
    private JButton diseaseTableButton;
    private JButton diseaseCasesTableButton;
    private JButton reportsTableButton;
    private JButton usersTableButton;
    private JButton rolesTableButton;

    /**
     * Constructs a new ButtonManager, initializing and configuring the buttons.
     */
    public ButtonManager() { // Start of constructor

        /* *** Button For Login and Create Account Panel *** */
        loginButton = new JButton("LOGIN");
        createButton = new JButton("CREATE ACCOUNT");

        /* *** Button For Edit Table Panel *** */
        addButton = new JButton("ADD");
        viewButton = new JButton("VIEW");
        removeButton = new JButton("REMOVE");
        searchButton = new JButton("SEARCH");
        clearButton = new JButton("CLEAR");
        updteButton = new JButton("UPDATE");
        importButton = new JButton("IMPORT CSV");

        /* *** Button For Analysis Panel *** */
        refreshButton = new JButton("Refresh Data");
        viewContinentTotalButton = new JButton("VIEW");
        viewCountryTotalButton = new JButton("VIEW");
        viewTotalPerPeriodButton = new JButton("VIEW");
        viewReportPerPeriodButton = new JButton("VIEW");
        viewContinentReportButton = new JButton("VIEW");
        viewAvgPerPeriodButton = new JButton("VIEW");


        /* *** Buttons for Data Base Tables *** */
        countryTableButton = new JButton("EDIT COUNTRIES TABLE");
        continentTableButton = new JButton("EDIT CONTINENTS TABLE");
        diseaseTableButton = new JButton("EDIT DISEASES TABLE");
        diseaseCasesTableButton = new JButton("EDIT DISEASE CASES TABLE");
        reportsTableButton = new JButton("EDIT REPORTS TABLE");
        usersTableButton = new JButton("EDIT USERS TABLE");
        rolesTableButton = new JButton("EDIT ROLES TABLE");

    } // End of constructor

    /* *** Getters for Analysis Panel buttons *** */

    public JButton getViewContinentTotalButton() {
        return viewContinentTotalButton;
    }

    public JButton getViewCountryTotalButton() {
        return viewCountryTotalButton;
    }

    public JButton getViewTotalPerPeriodButton() {
        return viewTotalPerPeriodButton;
    }

    public JButton getViewReportPerPeriodButton() {
        return viewReportPerPeriodButton;
    }

    public JButton getViewContinentReportButton() {
        return viewContinentReportButton;
    }

    public JButton getViewAvgPerPeriodButton() {
        return viewAvgPerPeriodButton;
    }

    /* *** Getters for Data Base Table buttons *** */

    public JButton getCountryTableButton() {
        return countryTableButton;
    }

    public JButton getContinentTableButton() {
        return continentTableButton;
    }

    public JButton getDiseaseTableButton() {
        return diseaseTableButton;
    }

    public JButton getDiseaseCasesTableButton() {
        return diseaseCasesTableButton;
    }

    public JButton getReportsTableButton() {
        return reportsTableButton;
    }

    public JButton getUsersTableButton() {
        return usersTableButton;
    }

    public JButton getRolesTableButton() {
        return rolesTableButton;
    }

    /* *** Getters for Edit Table Panel buttons *** */

    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getCreateButton() {
        return createButton;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getRemoveButton() {
        return removeButton;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public JButton getViewButton() {
        return viewButton;
    }

    public JButton getUpdteButton() {
        return updteButton;
    }

    public JButton getRefreshButton() {
        return refreshButton;
    }

    public JButton getImportButton() {
        return importButton;
    }
} // End of ButtonManager Class

