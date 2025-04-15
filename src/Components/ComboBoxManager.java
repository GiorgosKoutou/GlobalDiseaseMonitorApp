package Components;

import javax.swing.*;
import java.awt.*;

public class ComboBoxManager extends JComboBox { // Start of ComboBoxManager class

    static {
        UIManager.put("ComboBox.foreground", Color.LIGHT_GRAY);
        UIManager.put("ComboBox.setFont", new Font("SansSerif", Font.PLAIN, 12));
    }


    /* *** ComboBoxes For Analysis Panel *** */
    private JComboBox totalPerContinent;
    private JComboBox totalPerCountry;

    private JComboBox totalPerContinentPerPeriod;
    private JComboBox totalPerCountryPerPeriod;

    private JComboBox countryReportPerPeriod;
    private JComboBox continentReportPerPeriod;

    private JComboBox continentAvgPerPeriod;
    private JComboBox countryAvgPerPeriod;

    private JComboBox diseasePerPeriod;
    /* ===================================== */

    /* *** ComboBox For Editing panels *** */
    private JComboBox idComboBox;
    private JComboBox diseaseIdComboBox;
    private JComboBox countryIdComboBox;
    private JComboBox userIDComboBox;
    /* =================================== */

    public ComboBoxManager() {

        /* *** ComboBoxes For Analysis Panel *** */
        totalPerContinent = new JComboBox<>(new String[]{"Africa", "Asia", "Europe", "North America", "South America", "Worldwide"});
        totalPerCountry = new JComboBox<>(new String[]{"Greece", "Germany", "France", "Italy", "United States", "Canada", "Brazil", "China", "Russia", "India",
                "Egypt", "Nigeria", "South Africa", "Kenya", "Australia", "New Zealand"});

        totalPerContinentPerPeriod = new JComboBox<>(new String[]{"Africa", "Asia", "Europe", "North America", "South America", "Worldwide"});
        totalPerCountryPerPeriod = new JComboBox<>();

        continentReportPerPeriod = new JComboBox<>(new String[]{"Africa", "Asia", "Europe", "North America", "South America", "Worldwide"});
        countryReportPerPeriod = new JComboBox<>();

        continentAvgPerPeriod = new JComboBox<>(new String[]{"Africa", "Asia", "Europe", "North America", "South America", "Worldwide"});
        countryAvgPerPeriod = new JComboBox<>();

        diseasePerPeriod = new JComboBox<>(new String[]{"COVID-19", "Ebola", "HIV", "Influenza A", "Influenza B"});
        /* ============================================================================================================= */

        /* *** ComboBox For Editing panels *** */
        idComboBox = new JComboBox<>();
        diseaseIdComboBox = new JComboBox<>();
        countryIdComboBox = new JComboBox<>();
        userIDComboBox = new JComboBox<>();
        /* ====================================== */
    }

    /* *** Getters For Analysis Panel ComboBoxes *** */
    public JComboBox getContinentAvgPerPeriod() {
        return continentAvgPerPeriod;
    }

    public JComboBox getCountryAvgPerPeriod() {
        return countryAvgPerPeriod;
    }

    public JComboBox getCountryReportPerPeriod() {
        return countryReportPerPeriod;
    }

    public JComboBox getContinentReportPerPeriod() {
        return continentReportPerPeriod;
    }

    public JComboBox getTotalPerContinent() {
        return totalPerContinent;
    }

    public JComboBox getTotalPerCountry() {
        return totalPerCountry;
    }

    public JComboBox getTotalPerContinentPerPeriod() {
        return totalPerContinentPerPeriod;
    }

    public JComboBox getTotalPerCountryPerPeriod() {
        return totalPerCountryPerPeriod;
    }

    public JComboBox getDiseasePerPeriod() {
        return diseasePerPeriod;
    }
    /* ============================================= */

    /* *** Getters For Editing panel ComboBox *** */
    public JComboBox getIdComboBox() {
        return idComboBox;
    }

    public JComboBox getDiseaseIdComboBox() {
        return diseaseIdComboBox;
    }

    public JComboBox getCountryIdComboBox() {
        return countryIdComboBox;
    }

    public JComboBox getUserIDComboBox() {
        return userIDComboBox;
    }
    /* ============================================= */

    /* =============================================*/
} // End of ComboBoxManager class
