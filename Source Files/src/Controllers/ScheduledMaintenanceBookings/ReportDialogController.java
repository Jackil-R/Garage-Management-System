/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.ScheduledMaintenanceBookings;

import static java.awt.SystemColor.text;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.GLOBAL;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

/**
 * FXML Controller class
 *
 * @author Julian
 */
public class ReportDialogController implements Initializable {

    public Button btnCreateReport;
    public ComboBox cbResult;
    public TextArea taDescription;
    public TextField tfMileage;
    private Booking booking;
    private Stage prevStage;
    public Label msg;
    private boolean prevReport = false;

    public void setPrevStage(Stage prevStage) {
        this.prevStage = prevStage;
    }
    BookingHandler dbH = new BookingHandler();

    public void setBooking(Booking booking,boolean prev) {
        this.booking = booking;
        if (prev) {msg.setText("You will be overwriting a previous report!");
        prevReport = true;}
        
        }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        String[] results = {"SUCCESS","FAIL"};
        
        cbResult.getItems().addAll(results);
        
        // TODO
        tfMileage.textProperty().addListener(new ChangeListener<String>() {
            
    @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue.matches("\\d*")) {
                  try {
          int value = Integer.parseInt(newValue);
    } catch (NumberFormatException e) {
            //DO NOTHING
    } // END OF TRY CATCH
             } else {
            tfMileage.setText(oldValue);
        }
    }
});
    }    
    
    
    public void createServiceReport(){
        
        Validator v = new Validator();
        
        if (booking == null)
            Dialogs.create()
                             .owner(prevStage)
                             .title("Error!")
                             .masthead(null)
                             .message("There has been a fatal error, close this dialog and try again.")
                             .showInformation();
    
        boolean check = false;
        
        try {
        check =  v.checkServiceReport(cbResult.getSelectionModel().getSelectedItem().toString(),
                        tfMileage.getText(),
                        taDescription.getText(),
                        booking);
        } catch (java.lang.IllegalArgumentException e) { Dialogs.create()
                             .owner(prevStage)
                             .title("Error!")
                             .masthead(null)
                             .message("You must fill all fields in!")
                             .showInformation();
        
        return; }
        
        catch (NullPointerException e) {
            Dialogs.create()
                             .owner(prevStage)
                             .title("Error!")
                             .masthead(null)
                             .message("You must fill all fields in!")
                             .showInformation();
            
            return;}
        
        if (!(
               check
                )) 
        {
            Dialogs.create()
                             .owner(prevStage)
                             .title("Error!")
                             .masthead(null)
                             .message("The following errors occured: "+v.getError())
                             .showInformation();
         return;   
        }
             
        
        ServiceReport sr = new ServiceReport();
        
        sr.setBookingsID(booking.getID());
        
        String result = cbResult.getSelectionModel().getSelectedItem().toString();
        
        switch (result) {
            case "SUCCESS" : sr.setResult(true); break;
            case "FAIL" : sr.setResult(false); break;
            default : sr.setResult(true); break;
        }
        
        String desc = taDescription.getText();
        
        if (!sr.isResult())
        {
            if (desc.length() < 16) {
        Dialogs.create()
                             .owner(prevStage)
                             .title("Error!")
                             .masthead(null)
                             .message("Please enter a valid description as to why this maintanence failed (Min 16 Characters)")
                             .showInformation();
            return;}
        }
        
        if (desc.length() < 2) desc = "N/A";
       // desc = v.removeSQL(desc);
        
        sr.setDescription(desc);
        
        int mil = Integer.parseInt(tfMileage.getText());
        
        sr.setMileage(mil);
        
        int hours = booking.getEndDate().getTime().getHours()-booking.getStartDate().getTime().getHours();
        
        sr.setHours(hours);
        
        if (prevReport)
            dbH.updateServiceReport(sr);
        else
        {

            Action response = Dialogs.create()
        .owner(prevStage)
        .title("Service Report Confirmation")
        .masthead("Customer will be billed for: £"+dbH.getBookingPricebyID(booking.getBookingType())
        + ". Vehicle "+booking.getBookingTypeName()+" record will also be updated.")
        .message("Are you sure you want to create this report?")
        .actions(Dialog.ACTION_YES, Dialog.ACTION_NO)
        .showConfirm();

        if (response == Dialog.ACTION_YES) {
            GLOBAL.ScheduledPointer.refreshAgenda();
            dbH.insertServiceReport(sr);
            dbH.createCustomerBill(booking);
            GLOBAL.ScheduledPointer.refreshAgenda();
            if (booking.getBookingType() == 1)
                dbH.updateLastService(booking);
            else 
               if (booking.getBookingType() == 2)
                dbH.updateMOTExpire(booking); 
            booking.setBookingNotes(sr.getDescription());
            booking.setPartList(dbH.getServicePartsBySRID(dbH.getServiceReportByBookingID(booking.getID()).getID()));
        } else {
             return;
            }
        
        }
        
        Dialogs.create()
                             .owner(prevStage)
                             .title("Success!")
                             .masthead(null)
                             .message("Report Succesfully Created!")
                             .showInformation();
        
        closeWindow();
    
    }
    
     public void closeWindow(){
    Stage sb = (Stage) btnCreateReport.getScene().getWindow();
    sb.close();
    }
    
}
