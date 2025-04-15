package Components;

import javax.swing.*;

/**
 * Manages and provides access to JTextField objects for user input.
 */
public class TextFieldManager extends JTextField { // Start of TextFieldManager class

    private JTextField searchTextField;

    /* *** TextFields For Edit Country Table *** */
    private JTextField countryNameTextField;
    private JTextField continentIdTextField;
    private JTextField populationTextField;
    /*============================================*/

    /* *** TextFields For Edit Continent Table ****/
    private JTextField continentNameTextField;
    /*============================================*/

    /* *** TextFields For Edit Disease Table Panel *** */
    private JTextField diseaseNameTextField;
    private JTextArea diseaseDescriptionTextArea;
    /* ===============================================*/

    /* *** TextFields For Edit Disease Cases Table Panel *** */
    private JTextField diseaseCasesTextField;
    private JTextField diseaseDeathsTextField;
    /* ===============================================*/

    /* *** TextFields For Login Panel *** */
    private JTextField userNameTextField;
    private JTextField enterPasswordTextField;
    private JPasswordField passwordTextField;
    /* ================================== */

    /**
     * Constructs a TextFieldManager, initializing all the JTextField objects.
     */
    public TextFieldManager() { // Start of constructor
        searchTextField = new JTextField();

        /* *** TextFields For Edit Country Table *** */
        countryNameTextField = new JTextField();
        continentIdTextField = new JTextField();
        populationTextField = new JTextField();
        /* =========================================*/

        /* *** TextFields For Edit Continent Table ****/
        continentNameTextField = new JTextField();
        /* =========================================*/

        /* *** TextFields For Edit Disease Table Panel *** */
        diseaseNameTextField = new JTextField();
        diseaseDescriptionTextArea = new JTextArea();
        /* ============================================== */

        /* *** TextFields For Edit Disease Cases Table Panel *** */
        diseaseCasesTextField = new JTextField();
        diseaseDeathsTextField = new JTextField();
        /* ============================================== */

        /* *** TextFields For Login Panel *** */
        userNameTextField = new JTextField();
        enterPasswordTextField = new JTextField();
        passwordTextField = new JPasswordField();
        /* ================================== */

    } // End of constructor

    /* *** Getters For Edit Country Table *** */
    public JTextField getCountryNameTextField() {
        return countryNameTextField;
    }

    public JTextField getContinentIdTextField() {
        return continentIdTextField;
    }

    public JTextField getPopulationTextField() {
        return populationTextField;
    }
    /* ========================================= */

    /* *** Getter for Edit Continent Table *** */
    public JTextField getContinentNameTextField() {
        return continentNameTextField;
    }
    /* ========================================= */

    /* *** Getter for Edit Disease Table Panel *** */

    public JTextField getDiseaseNameTextField() {
        return diseaseNameTextField;
    }

    public JTextArea getDiseaseDescriptionTextArea() {
        return diseaseDescriptionTextArea;
    }
    /* ==========================================*/

    /* *** Getters For Edit Disease Cases Table Panel *** */

    public JTextField getDiseaseCasesTextField() {
        return diseaseCasesTextField;
    }

    public JTextField getDiseaseDeathsTextField() {
        return diseaseDeathsTextField;
    }
    /* ============================================== */


    public JTextField getSearchTextField() {
        return searchTextField;
    }

    /**
     * Gets the JTextField for entering the username of the user.
     */
    public JTextField getUserNameTextField() {
        return userNameTextField;
    }

    public JTextField getEnterPasswordTextField() {
        return enterPasswordTextField;
    }

    /**
     * Gets the JTextField for entering the password of the user.
     */
    public JPasswordField getPasswordTextField() {
        return passwordTextField;
    }
} // End of TextFieldManager class
