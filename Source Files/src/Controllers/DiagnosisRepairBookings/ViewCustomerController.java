/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.DiagnosisRepairBookings;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jackilrajnicant
 */
public class ViewCustomerController implements Initializable {
    @FXML
    private TextField TcustomerType;
    @FXML
    private TextField TcustomerCompanyName;
    @FXML
    private TextField TcustomerFName;
    @FXML
    private TextField TcustomerSName;
    @FXML
    private TextField TcustomerAddLine1;
    @FXML
    private TextField TcustomerAddLine2;
    @FXML
    private TextField TcustomerCounty;
    @FXML
    private TextField TcustomerPostCode;
    @FXML
    private TextField TcustomerPhoneNo;
    @FXML
    private Button closeBtn;
    
    private Stage prevStage;
    
    private DRBooking booking;
    
    private BookingAppointment appointment;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    public void closeWindow(){
        Stage sb = (Stage)closeBtn.getScene().getWindow();
        sb.close();
    }
    
    public void setStage(Stage stage){
        prevStage = stage;
    }
    
    public void setBooking(DRBooking booking){
        this.booking = booking;
    }
    public DRBooking getBooking(){
        return booking;
    }
    
    public BookingAppointment getAppointment() {
        return appointment;
    }

    public void setAppointment(BookingAppointment appointment) {
        this.appointment = appointment;
        booking = appointment.getBooking();      
    }
    
    public void displayData(){
        DRBooking b = booking;
        Vehicle v = b.getVehicle();
        CustomerAccount ca = v.getCustomer(); 
        
        String type="";
        
        if(ca.getTypeID()==1){
            type="Businesses";
            TcustomerCompanyName.setText(ca.getCompanyName());
            TcustomerFName.setText("N.A");
            TcustomerSName.setText("N.A");
        }else{
            type="Private";
            TcustomerCompanyName.setText("N.A");
            TcustomerFName.setText(ca.getFirstName());
            TcustomerSName.setText(ca.getLastName());
        }
        
        TcustomerType.setText(type);
        TcustomerAddLine1.setText(ca.getAddressLine1());
        TcustomerAddLine2.setText(ca.getAddressLine2());
        TcustomerCounty.setText(ca.getCounty());
        TcustomerPostCode.setText(ca.getPostCode());
        TcustomerPhoneNo.setText(ca.getPhoneNumber());
    }
    
}
