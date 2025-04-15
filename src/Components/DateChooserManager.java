package Components;

import com.toedter.calendar.JDateChooser;

public class DateChooserManager extends JDateChooser {

    /* *** Date Choosers For Analysis Panel **** */
    private JDateChooser startDateforTotal;
    private JDateChooser endDateForTotal;

    private JDateChooser startDateForAvg;
    private JDateChooser endDateForAvg;

    private JDateChooser startDateForReport;
    private JDateChooser endDateForReport;
    /* ============================================== */

    public DateChooserManager(){

        /* *** Date Choosers For Analysis Panel **** */
        startDateforTotal = new JDateChooser();
        endDateForTotal = new JDateChooser();

        startDateForReport = new JDateChooser();
        endDateForReport = new JDateChooser();

        startDateForAvg = new JDateChooser();
        endDateForAvg = new JDateChooser();
        /* ========================================= */
        setFormat();
    }

    private void setFormat(){
        this.startDateforTotal.setDateFormatString("dd-MM-yyyy");
        this.endDateForTotal.setDateFormatString("dd-MM-yyyy");

        this.startDateForReport.setDateFormatString("dd-MM-yyyy");
        this.endDateForReport.setDateFormatString("dd-MM-yyyy");

        this.startDateForAvg.setDateFormatString("dd-MM-yyyy");
        this.endDateForAvg.setDateFormatString("dd-MM-yyyy");
    }

    public JDateChooser getStartDateForAvg() {
        return startDateForAvg;
    }

    public JDateChooser getEndDateForAvg() {
        return endDateForAvg;
    }

    public JDateChooser getStartDateforTotal() {
        return startDateforTotal;
    }

    public JDateChooser getEndDateForTotal() {
        return endDateForTotal;
    }

    public JDateChooser getStartDateForReport() {
        return startDateForReport;
    }

    public JDateChooser getEndDateForReport() {
        return endDateForReport;
    }


}
