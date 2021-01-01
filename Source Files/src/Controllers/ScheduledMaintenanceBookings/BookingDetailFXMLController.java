/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.ScheduledMaintenanceBookings;

import Controllers.Customer.CustomerAccount;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalTimePicker;
import main.GLOBAL;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

/**
 * FXML Controller class
 *
 * @author Julians
 */
public class BookingDetailFXMLController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private Stage prevStage;
    private Booking booking;
    private BookingAppointment appointment;

    //CUSTOMERS
    public TextField editCustomerName;
    public TextField editCustomerSurname;
    public TextField editCustomerNumber;
    public TextField editCustomerAddress1;
    public TextField editCustomerAddress2;
    public TextField editCustomerCounty;
    public TextField editCustomerPostal;
    public TextField editCustomerCompany;
    //VEHICLES
    public TextField editVehicleMake;
    public TextField editVehicleModel;
    public TextField editVehicleReg;
    public TextField editVehicleMileage;
    public TextField editVehicleEngine;
    public TextField editVehicleColour;
    public TextField editVehicleFuel;
    public TextField editVehicleMOT;
    public TextField editVehicleService;
    public TextField editVehicleWarranty;
    //BOOKINGS
    public ComboBox editBookingType;
    public TextField editBookingFee;
    public DatePicker editBookingDate;
    public ComboBox editBookingMechanic;
    public LocalTimePicker editBookingTimeStart;
    public LocalTimePicker editBookingTimeEnd;
    public ComboBox editBookingBayNumber;
    public TextArea editBookingNotes;
    public TextArea editBookingParts;
    ///BUTTONS
    public Button btnCloseBooking;
    public Button btnDeleteBooking;
    public Button btnSaveBooking;
    public Button btnPrintBooking;
    public Button btnOpenCustomer;
    
    private boolean deleted = false;

    public boolean isDeleted() {
        return deleted;
    }
    
    private BookingHandler dbH = new BookingHandler();
    
    public BookingDetailFXMLController(){
        
    }
    
      @Override
    public void initialize(URL url, ResourceBundle rb) {

      editBookingTimeStart.localTimeProperty().set(LocalTime.of(0,0));
      editBookingTimeEnd.localTimeProperty().set(LocalTime.of(0,0));    
        
      editBookingType.getItems().addAll(dbH.getBookingTypes(2).toArray(new String[dbH.getBookingTypes(2).size()]));   
        
      editBookingMechanic.getItems().addAll(getMechanicNames(dbH.getAllMechanics()));    
          
            
    }   
    
        public void addListeners(){
    
        //Listener for when they change booking type
        editBookingType.valueProperty().addListener(new ChangeListener<String>() {
        @Override public void changed(ObservableValue ov, String t, String t1) {
            editBookingFee.setText(dbH.getBookingPrice(t1).toString());
            updateFreeBays();
        }    
    });
        
         //Listener for when they change start time
        editBookingTimeStart.localTimeProperty().addListener(new ChangeListener<LocalTime>() {
        @Override
        public void changed(
                ObservableValue<? extends LocalTime> observableValue,
                LocalTime oldValue, LocalTime newValue) {           
            updateFreeBays();         
        }
    });
        
        //Listener for when they change end time
        editBookingTimeEnd.localTimeProperty().addListener(new ChangeListener<LocalTime>() {
        @Override
        public void changed(
                ObservableValue<? extends LocalTime> observableValue,
                LocalTime oldValue, LocalTime newValue) {           
            updateFreeBays();         
        }
    });
        
        //EventHandler for when they change the date
       editBookingDate.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {       
               updateFreeBays();  
            }
        });
       
    }
    
    
         private void updateFreeBays() {
              DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
     
        String timeStart = editBookingDate.getValue().getDayOfMonth()+"/"+editBookingDate.getValue().getMonthValue()+"/"
                +editBookingDate.getValue().getYear()+" "+editBookingTimeStart.localTimeProperty().get().getHour()
                +":"+editBookingTimeStart.localTimeProperty().get().getMinute()+":"+editBookingTimeStart.localTimeProperty().get().getSecond();
        
        String timeEnd = editBookingDate.getValue().getDayOfMonth()+"/"+editBookingDate.getValue().getMonthValue()+"/"
                +editBookingDate.getValue().getYear()+" "+editBookingTimeEnd.localTimeProperty().get().getHour()
                +":"+editBookingTimeEnd.localTimeProperty().get().getMinute()+":"+editBookingTimeEnd.localTimeProperty().get().getSecond();
        
        try {
            
            Date startDate = formatter.parse(timeStart);
            Date endDate = formatter.parse(timeEnd);
            
            editBookingBayNumber.getItems().clear();
            ArrayList<Integer> bays = new ArrayList<Integer>();
            ArrayList<Integer> takenBays = dbH.getFreeBayNumber(startDate,endDate);
            
            //Fill Bays
            for (int i=1; i <= 6; i++) bays.add(i);
            
            //Remove Taken Bays
            for (int i=0; i <= takenBays.size()-1; i++)           
                    bays.remove(takenBays.get(i)-1);
                                            
            editBookingBayNumber.getItems().addAll(bays.toArray());                  
            
        } catch (ParseException ex) {
            Logger.getLogger(CreateBookingFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }
    
    public ArrayList<String> getMechanicNames(ArrayList<Mechanic> list){
        ArrayList<String> names = new ArrayList<String>();
        
        for (Mechanic m : list)
            names.add(m.getID()+":"+m.getFirstName()+" "+m.getLastName());
        
        
        return names;
    }
        
    public void updateDisplayFields(){
      
       Booking b = booking;
        Vehicle v = b.getVehicle();
        CustomerAccount ca = v.getCustomer(); 
        Mechanic m = b.getMechanic();   
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        
        
    editCustomerName.setText(ca.getFirstName());
    editCustomerSurname.setText(ca.getLastName());
    editCustomerNumber.setText(ca.getPhoneNumber());
    editCustomerAddress1.setText(ca.getAddressLine1());
    editCustomerAddress2.setText(ca.getAddressLine2());
    editCustomerCounty.setText(ca.getCounty());
    editCustomerPostal.setText(ca.getPostCode());
    editCustomerCompany.setText(ca.getCompanyName());
    //VEHICLES
    editVehicleMake.setText(v.getMake());
    editVehicleModel.setText(v.getModel());
    editVehicleReg.setText(v.getRegistration());
    editVehicleMileage.setText(Integer.toString(v.getMileage()));
    editVehicleEngine.setText(v.getEngineSize());
    editVehicleColour.setText(v.getColour());
    editVehicleFuel.setText(v.getFuelType());
    String MOTexpire = "";
    if (v.getMOTExpire() == null)
        MOTexpire = "N/A";
    else
        MOTexpire = v.getMOTExpire().getTime().toString();
        
    editVehicleMOT.setText(MOTexpire);
    
     String ServiceDate = "";
    if (v.getLastService() == null)
        ServiceDate = "N/A";
    else
        ServiceDate = v.getLastService().getTime().toString();
    
    editVehicleService.setText(ServiceDate);
    editVehicleWarranty.setText(v.getWarrantyCompany());
 
    //BOOKINGS
    editBookingType.setValue(b.getBookingTypeName());
    
    String fee = ""+b.getBookingFee();
    editBookingFee.setText(fee);
    
      Instant instant = b.getStartDate().toInstant();
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        LocalDate date = zdt.toLocalDate();

    editBookingDate.setValue(date);
    
    editBookingMechanic.getItems().addAll(getMechanicNames(dbH.getAllMechanics())); 
    editBookingMechanic.setValue(m.getID()+":"+m.getFirstName()+" "+m.getLastName());
    
    String timeStart = "";
    if (b.getStartDate().getTime().getHours() < 10) timeStart = "0";
    timeStart += b.getStartDate().getTime().getHours()+":";
    if (b.getStartDate().getTime().getMinutes() < 10) timeStart += "0";
    timeStart += b.getStartDate().getTime().getMinutes();
    
    LocalTime ltStart = LocalTime.parse(timeStart);
    
    editBookingTimeStart.localTimeProperty().setValue(ltStart); //setText(timeStart);
    
    String timeEnd = "";
    if (b.getEndDate().getTime().getHours() < 10) timeEnd = "0";
    timeEnd += b.getEndDate().getTime().getHours()+":";
    if (b.getEndDate().getTime().getMinutes() < 10) timeEnd += "0";
    timeEnd += b.getEndDate().getTime().getMinutes();
    
    LocalTime ltEnd = LocalTime.parse(timeEnd);
    
    editBookingTimeEnd.localTimeProperty().setValue(ltEnd);
    
    editBookingBayNumber.setValue(b.getBayNumber());
    editBookingNotes.setText(b.getBookingNotes());
    
   if (b.getPartList() != null)
    if (b.getPartList().size() > 0)
    editBookingParts.setText(PartListToString(b.getPartList()));
    else
        editBookingParts.setText("N/A");
    }
    
     public String PartListToString(ArrayList<Part> list){
        String msg = "";
        
        for (int i=0; i < list.size(); i++)
            msg += "[ "+list.get(i).getName()+" ]";
        
        return msg;
    }
     
    
    public void setStage(Stage stage){
        prevStage = stage;
    }
    
    
    public BookingAppointment getAppointment() {
        return appointment;
    }

    public void setAppointment(BookingAppointment appointment) {
        this.appointment = appointment;
        booking = appointment.getBooking();      
    }
    
    public void applyChanges(){
    
    booking.setStartDate(parseToDate(editBookingDate.getValue(),editBookingTimeStart.localTimeProperty().getValue()));
    booking.setEndDate(parseToDate(editBookingDate.getValue(),editBookingTimeEnd.localTimeProperty().getValue()));
    booking.setMechanic(dbH.getMechanicByID(Integer.parseInt(editBookingMechanic.getSelectionModel().getSelectedItem().toString().substring(0,1))));
    int bay = booking.getBayNumber();
    try {
    bay = Integer.parseInt(editBookingBayNumber.getSelectionModel().getSelectedItem().toString());
    }
    catch (NumberFormatException e) {
    }
    
   booking.setBayNumber(bay);
    booking.setBookingFee(dbH.getBookingPricebyID(booking.getBookingType()));
    
    }
    
    public void saveBooking(){
    Validator v = new Validator();
       
        if (editBookingType.getValue().toString().equals("Repair"))
            Dialogs.create()
                             .owner(prevStage)
                             .title("Error!")
                             .masthead(null)
                             .message("Go to Diagnosis and Repair tab for editing Repair Bookings!")
                             .showInformation();
        else
        if(v.checkBookingDate(parseToDate(editBookingDate.getValue(),editBookingTimeStart.localTimeProperty().getValue()),
                parseToDate(editBookingDate.getValue(),editBookingTimeEnd.localTimeProperty().getValue())))            
        {
            
            int mID = Integer.parseInt(editBookingMechanic.getSelectionModel().getSelectedItem().toString().substring(0,1));
            
            if (mID != GLOBAL.getMechanicID())
            if (!getAuth(mID)) return;
         
            
            applyChanges();
           dbH.updateBooking(booking);
             Dialogs.create()
                             .owner(prevStage)
                             .title("Success!")
                             .masthead(null)
                             .message("Booking has been succesfully updated!")
                             .showInformation();
             closeWindow();
        }
        else
        {
            System.out.println("fail");
        }
        
    }
    
     public GregorianCalendar parseToDate(LocalDate date, LocalTime time){ 
        return new GregorianCalendar(date.getYear(),date.getMonthValue()-1,date.getDayOfMonth(),time.getHour(),time.getMinute());
    }
    
     
    public boolean getAuth(int id){
    FXMLLoader loader = new FXMLLoader(
    getClass().getResource(
      "/FXML/ScheduledMaintenanceBookings/AuthFXML.fxml"
    )
  );

  Stage stage = new Stage();
        try {
            stage.setScene(
                    new Scene(
                            (Pane) loader.load()
                    )
            );    } catch (IOException ex) {
            Logger.getLogger(CreateBookingFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }

  AuthFXMLController controller = 
    loader.<AuthFXMLController>getController();
    controller.setOtherMechID(id);
    GLOBAL.StartPagePointer.disableTab();
    stage.setResizable(false);
    stage.setAlwaysOnTop(true);
    stage.showAndWait();
GLOBAL.StartPagePointer.enableTab();
  return controller.getResult();
    }
      
  
    public void closeWindow(){
    Stage sb = (Stage)btnCloseBooking.getScene().getWindow();
    sb.close();
    }
    
    public void setBooking(Booking booking){
    this.booking = booking;
    }
    
    public void confirmDelete(){
       
   Action response = Dialogs.create()
        .owner(prevStage)
        .title("Delete Booking Confirmation")
        .masthead("You are about to delete this Booking. This can not be undone.")
        .message("Are you sure you want to delete this booking?")
        .actions(Dialog.ACTION_YES, Dialog.ACTION_NO)
        .showConfirm();

if (response == Dialog.ACTION_YES) {
    BookingHandler dbH = new BookingHandler();
    dbH.deleteBooking(booking);
    deleted = true;
    closeWindow();    
} else {
    // ... user chose CANCEL, or closed the dialog
}


    }
}
