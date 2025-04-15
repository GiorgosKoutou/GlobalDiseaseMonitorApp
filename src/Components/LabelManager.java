package Components;

import javax.swing.*;
import java.awt.*;

/**
 * Manages and provides access to JLabel objects for displaying labels on the user interface.
 */
public class LabelManager { // Start of LabelManager class

    static {
        UIManager.put("Label.font", new Font("SansSerif", Font.BOLD, 13));
    }

    /* *** Labels for Analysis Panel *** */
    private JLabel continentTotal;
    private JLabel countryTotal;
    private JLabel totalPerPeriod;
    private JLabel reportPerPeriod;
    private JLabel averagePerPeriod;
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    /* ==================================== */

    /* *** Labels For Edit Country Panel *** */
    private JLabel countryNameLabel;
    private JLabel continentIDLabel;
    private JLabel populationLabel;
    /* ==================================== */

    /* *** Labels For Edit Continent Panel *** */
    private JLabel continentNameLabel;
    /* ===================================== */

    /* *** Labels For Edit Disease Panel ****/
    private JLabel diseaseNameLabel;
    private JLabel diseaseDescriptionLabel;
    private JLabel diseaseDiscoverDateLabel;
    /* =====================================*/

    /* *** Labels For Edit Disease Cases and Report Panel ****/
    private JLabel userIDLabel;
    private JLabel diseaseIDLabel;
    private JLabel countryIDLabel;
    private JLabel commentLabel;
    private JLabel casesLabel;
    private JLabel deathsLabel;
    private JLabel dateLabel;
    /* ===================================== */

    /* *** Label For Edit User Panel ****/
    private JLabel roleIdLabel;
    /* ================================== */


    /**
     * Constructs a LabelManager, initializing all the JLabel objects with their respective text.
     */
    public LabelManager() { // Start of constructor
        /* *** Labels for Analysis Panel *** */
        continentTotal = new JLabel("Total Cases/Deaths Per Continent");
        countryTotal = new JLabel("Total Cases/Deaths Per Country");
        totalPerPeriod = new JLabel("Total Cases/Deaths  Over a Selected Period");
        reportPerPeriod = new JLabel("Repoprted Cases/Deaths per Disease Over a Period");
        averagePerPeriod = new JLabel("Average Cases/Deaths Over a Selected Period");
        /* ================================================================================ */

        /* *** Labels For Edit Country Panel *** */
        countryNameLabel = new JLabel("Country Name:");
        continentIDLabel = new JLabel("Continent ID:");
        populationLabel = new JLabel("Population:");
        /* ================================================ */

        /* *** Labels For Edit Continent Panel *** */
        continentNameLabel = new JLabel("Continent Name:");
        /* ================================================ */

        /* *** Labels For Edit Disease Panel ****/
        diseaseNameLabel = new JLabel("Disease Name:");
        diseaseDescriptionLabel = new JLabel("Disease Description:");
        diseaseDiscoverDateLabel = new JLabel("Disease Discover Date:");
        /* ================================================ */

        /* *** Labels For Edit Disease Cases and Reports Panel **** */
        userIDLabel = new JLabel("User ID:");
        diseaseIDLabel = new JLabel("Disease ID:");
        countryIDLabel = new JLabel("Country ID:");
        commentLabel = new JLabel("Comment:");
        casesLabel = new JLabel("Cases:");
        deathsLabel = new JLabel("Deaths:");
        dateLabel = new JLabel("Report Date:");
        /* ================================================ */

        /*** Label For Edit User Panel ****/
        roleIdLabel = new JLabel("Role ID:");
        /* ================================================ */

        /* *** Labels For Login Panel *** */
        userNameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");
        /* ================================================ */
    } // End of constructor

    /* *** Getters for Analysis Panel *** */
    public JLabel getAveragePerPeriod() {
        return averagePerPeriod;
    }

    public JLabel getReportPerPeriod() {
        return reportPerPeriod;
    }


    public JLabel getTotalPerPeriod() {
        return totalPerPeriod;
    }

    public JLabel getCountryTotal() {
        return countryTotal;
    }


    public JLabel getContinentTotal() {
        return continentTotal;
    }
    /* ==================================== */

    /* *** Getters for Edit Country Panel *** */
    public JLabel getCountryNameLabel() {
        return countryNameLabel;
    }

    public JLabel getContinentIDLabel() {
        return continentIDLabel;
    }

    public JLabel getPopulationLabel() {
        return populationLabel;
    }
    /* ==================================== */

    /* *** Getters for Edit Continent Panel *** */
    public JLabel getContinentNameLabel() {
        return continentNameLabel;
    }
    /* ==================================== */

    /* *** Getters for Edit Disease Panel *****/

    public JLabel getDiseaseNameLabel() {
        return diseaseNameLabel;
    }

    public JLabel getDiseaseDescriptionLabel() {
        return diseaseDescriptionLabel;
    }

    public JLabel getDiseaseDiscoverDateLabel() {
        return diseaseDiscoverDateLabel;
    }
    /* ====================================*/

    /* *** Getters for Edit Disease Cases and Reports Panel *** */

    public JLabel getUserIDLabel() {
        return userIDLabel;
    }

    public JLabel getDiseaseIDLabel() {
        return diseaseIDLabel;
    }

    public JLabel getCommentLabel() {
        return commentLabel;
    }

    public JLabel getCountryIDLabel() {
        return countryIDLabel;
    }

    public JLabel getCasesLabel() {
        return casesLabel;
    }

    public JLabel getDeathsLabel() {
        return deathsLabel;
    }

    public JLabel getDateLabel() {
        return dateLabel;
    }
    /* ================================================== */

    /* *** Getters for Edit User Panel *** */

    public JLabel getRoleIdLabel() {
        return roleIdLabel;
    }
    /* ================================================== */

   /* *** Getters for Login Panel *** */
    public JLabel getUserNameLabel() {
        return userNameLabel;
    }

    /**
     * Gets the JLabel for "Password:".
     */
    public JLabel getPasswordLabel() {
        return passwordLabel;
    }
    /* ================================================== */

} // End of LabelManager class
