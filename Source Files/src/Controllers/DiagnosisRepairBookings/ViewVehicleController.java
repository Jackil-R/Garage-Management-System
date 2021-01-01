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
public class ViewVehicleController implements Initializable {
    @FXML
    private TextField TvechicleMake;
    @FXML
    private TextField TvechicleModel;
    @FXML
    private TextField TvechicleReg;
    @FXML
    private TextField TvechicleMileage;
    @FXML
    private TextField TvechicleEngineSize;
    @FXML
    private TextField TvechicleColour;
    @FXML
    private TextField TvechicleFuelType;
    @FXML
    private TextField TvechicleLastBooking;
    @FXML
    private TextField TvechicleWarrenty;
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
        
        TvechicleMake.setText(v.getMake());
        TvechicleModel.setText(v.getModel());
        TvechicleReg.setText(v.getRegistration());
        TvechicleMileage.setText(Integer.toString(v.getMileage()));
        TvechicleEngineSize.setText(v.getEngineSize());
        TvechicleColour.setText(v.getColour());
        TvechicleFuelType.setText(v.getFuelType());
        String ServiceDate = "";
        if (v.getLastService() == null) {
            ServiceDate = "N/A";
        } else {
            ServiceDate = v.getLastService().getTime().toString();
        }
        TvechicleLastBooking.setText(ServiceDate);
        
        if(v.getWarranty()==0){
            TvechicleWarrenty.setText("No Warranty");
        }else{
            TvechicleWarrenty.setText(v.getWarrantyCompany());
        }
        
        
    }
    
}
