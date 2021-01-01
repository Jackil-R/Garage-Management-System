/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.ScheduledMaintenanceBookings;

import Controllers.Customer.CustomerAccount;
import Controllers.Customer.SearchModuleController;
import Controllers.VehicleRecords.DetailsController;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jfxtras.scene.control.CalendarTimePicker;
import jfxtras.scene.control.LocalTimePicker;
import jfxtras.scene.control.agenda.Agenda;
import main.GLOBAL;
import org.controlsfx.dialog.Dialogs;

/**
 * FXML Controller class
 *
 * @author Julian
 */
public class CreateBookingFXMLController implements Initializable {

    private Stage prevStage;
    private Booking booking;
    private Calendar startCalendarRange;
    private Calendar endCalendarRange;
    private BookingAppointment appointment;
    private BookingHandler dbH = new BookingHandler();
    private Vehicle vehicle;
    
    //Appointment Group Map
    public Map<String, Agenda.AppointmentGroup> lAppointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();

    public void setlAppointmentGroupMap(Map<String, Agenda.AppointmentGroup> lAppointmentGroupMap) {
        this.lAppointmentGroupMap = lAppointmentGroupMap;
    }
    //CUSTOMERS
    public TextField createCustomerName;
    public TextField createCustomerSurname;
    public TextField createCustomerNumber;
    public TextField createCustomerAddress1;
    public TextField createCustomerAddress2;
    public TextField createCustomerCounty;
    public TextField createCustomerPostal;
    public TextField createCustomerCompany;
    //VEHICLES
    public TextField createVehicleMake;
    public TextField createVehicleModel;
    public TextField createVehicleReg;
    public TextField createVehicleMileage;
    public TextField createVehicleEngine;
    public TextField createVehicleColour;
    public TextField createVehicleFuel;
    public TextField createVehicleMOT;
    public TextField createVehicleService;
    public TextField createVehicleWarranty;
    //BOOKINGS
    public ComboBox createBookingType;
    public TextField createBookingFee;
    public DatePicker createBookingDate;
    public ComboBox createBookingMechanic;
    public LocalTimePicker createBookingTimeStart;
    public LocalTimePicker createBookingTimeEnd;
    public ComboBox createBookingBayNumber;
    ///BUTTONS
    public Button btnCloseBooking;
    public Button btnCreateBooking;
    public Button btnFindCustomer;
    public Button btnFindParts;
    private boolean customerSet = false;
    
    
    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        createBookingTimeStart.localTimeProperty().set(LocalTime.of(0,0));
        createBookingTimeEnd.localTimeProperty().set(LocalTime.of(0,0));
        
        booking = new Booking();
        
        
        createBookingType.getItems().addAll(dbH.getBookingTypes(2).toArray(new String[dbH.getBookingTypes(2).size()]));
 
        createBookingMechanic.getItems().addAll(getMechanicNames(dbH.getAllMechanics()));       
 
    }    
    
    public void addListeners(){
    
        //Listener for when they change booking type
        createBookingType.valueProperty().addListener(new ChangeListener<String>() {
        @Override public void changed(ObservableValue ov, String t, String t1) {
            createBookingFee.setText(dbH.getBookingPrice(t1).toString());
            updateFreeBays();
        }    
    });
        
         //Listener for when they change start time
        createBookingTimeStart.localTimeProperty().addListener(new ChangeListener<LocalTime>() {
        @Override
        public void changed(
                ObservableValue<? extends LocalTime> observableValue,
                LocalTime oldValue, LocalTime newValue) {           
            updateFreeBays();         
        }
    });
        
        //Listener for when they change end time
        createBookingTimeEnd.localTimeProperty().addListener(new ChangeListener<LocalTime>() {
        @Override
        public void changed(
                ObservableValue<? extends LocalTime> observableValue,
                LocalTime oldValue, LocalTime newValue) {           
            updateFreeBays();         
        }
    });
        
        //EventHandler for when they change the date
       createBookingDate.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {       
               updateFreeBays();  
            }
        });
       
    }
    
    public ArrayList<String> getMechanicNames(ArrayList<Mechanic> list){
        ArrayList<String> names = new ArrayList<String>();
        
        for (Mechanic m : list)
            names.add(m.getID()+":"+m.getFirstName()+" "+m.getLastName());
        
        
        return names;
    }
    
    public void updateFreeBays(){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        
        String timeStart = createBookingDate.getValue().getDayOfMonth()+"/"+createBookingDate.getValue().getMonthValue()+"/"
                +createBookingDate.getValue().getYear()+" "+createBookingTimeStart.localTimeProperty().get().getHour()
                +":"+createBookingTimeStart.localTimeProperty().get().getMinute()+":"+createBookingTimeStart.localTimeProperty().get().getSecond();
        
        String timeEnd = createBookingDate.getValue().getDayOfMonth()+"/"+createBookingDate.getValue().getMonthValue()+"/"
                +createBookingDate.getValue().getYear()+" "+createBookingTimeEnd.localTimeProperty().get().getHour()
                +":"+createBookingTimeEnd.localTimeProperty().get().getMinute()+":"+createBookingTimeEnd.localTimeProperty().get().getSecond();
        
        try {
            
            Date startDate = formatter.parse(timeStart);
            Date endDate = formatter.parse(timeEnd);
            
            createBookingBayNumber.getItems().clear();
            ArrayList<Integer> bays = new ArrayList<Integer>();
            ArrayList<Integer> takenBays = dbH.getFreeBayNumber(startDate,endDate);
            
            //Fill Bays
            for (int i=1; i <= 6; i++) bays.add(i);
                     
             bays.removeAll(takenBays);

                                            
            createBookingBayNumber.getItems().addAll(bays.toArray());                  
            
        } catch (ParseException ex) {
            Logger.getLogger(CreateBookingFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        createBookingBayNumber.getSelectionModel().select(0);
               
        
    }
    
    public BookingAppointment getAppointment() {
        return appointment;
    }
    
    public void setStage(Stage stage){
        prevStage = stage;
    }
    
    public void closeWindow(){
    Stage sb = (Stage)btnCloseBooking.getScene().getWindow();
    sb.close();
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
      
    public void insertBooking(){
      //  if (dbH.getBookingTypeIDbyName(createBookingType.getSelectionModel().getSelectedItem().toString()) == 3)
            
        Validator v = new Validator();
        if (createBookingType.getValue() == null)
        {
        
            Dialogs.create()
                             .owner(prevStage)
                             .title("Error!")
                             .masthead(null)
                             .message("Set Booking Type!")
                             .showInformation();
            
            return;
        
        }
      if (createBookingType.getValue().toString().equals("Repair"))
            Dialogs.create()
                             .owner(prevStage)
                             .title("Error!")
                             .masthead(null)
                             .message("Go to Diagnosis and Repair tab for creating Repair Bookings!")
                             .showInformation();
       
      
        if(v.checkBookingDate(parseToDate(createBookingDate.getValue(),createBookingTimeStart.localTimeProperty().getValue()),
                parseToDate(createBookingDate.getValue(),createBookingTimeEnd.localTimeProperty().getValue()))
            && customerSet)
            
        {     
            
          int mID = GLOBAL.getMechanicID();
          
            try {
             mID = Integer.parseInt(createBookingMechanic.getSelectionModel().getSelectedItem().toString().substring(0,1));
            } catch (Exception ex) {
                Dialogs.create()
                             .owner(prevStage)
                             .title("Error!")
                             .masthead(null)
                             .message("Set Mechanic!")
                             .showInformation();
                return;
            }
            
            if (mID != GLOBAL.getMechanicID())
            if (!getAuth(mID)) return;
         
       createBookingClass();
       dbH.insertBooking(booking);
       booking.setID(dbH.getLastBookingID());
       appointment = createAppointment(booking);
       closeWindow();
       
        }
        else
        {
            String extra = "";
            if (!customerSet) extra = " Customer and/or Vehicle is not set!";
        Dialogs.create()
                             .owner(prevStage)
                             .title("Error!")
                             .masthead(null)
                             .message("Errors with booking:"+v.getError()+extra)
                             .showInformation();
        }
       
    }
     
    public void setCalenderRanges(Calendar startRange,Calendar endRange){
        createBookingMechanic.getSelectionModel().select(GLOBAL.getMechanicID()-1);
        startCalendarRange = startRange;
        endCalendarRange = endRange;
        createBookingTimeStart.localTimeProperty().set(LocalTime.of(startRange.getTime().getHours(), startRange.getTime().getMinutes()));
        createBookingTimeEnd.localTimeProperty().set(LocalTime.of(endRange.getTime().getHours(), endRange.getTime().getMinutes()));
        
        Instant instant = startRange.toInstant();
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        LocalDate date = zdt.toLocalDate();

        createBookingDate.setValue(date);
        
        updateFreeBays();
    }
    
    public void getCustomer(){
    Random rand = new Random();
    vehicle = dbH.getVehicleByID(rand.nextInt(4)+1);
    vehicle.setCustomer(dbH.getCustomerByID(vehicle.getCustomer().getCustomerID()));
    booking.setVehicle(vehicle);
    updateDisplayFields(booking);
    }
       
    public void updateDisplayFields(Booking b){
     //  Booking b = booking;
        Vehicle v = b.getVehicle();
        CustomerAccount ca = v.getCustomer(); 
        
        
    createCustomerName.setText(ca.getFirstName());
    createCustomerSurname.setText(ca.getLastName());
    createCustomerNumber.setText(ca.getPhoneNumber());
    createCustomerAddress1.setText(ca.getAddressLine1());
    createCustomerAddress2.setText(ca.getAddressLine2());
    createCustomerCounty.setText(ca.getCounty());
    createCustomerPostal.setText(ca.getPostCode());
    createCustomerCompany.setText(ca.getCompanyName());
    //VEHICLES
    createVehicleMake.setText(v.getMake());
    createVehicleModel.setText(v.getModel());
    createVehicleReg.setText(v.getRegistration());
    createVehicleMileage.setText(Integer.toString(v.getMileage()));
    createVehicleEngine.setText(v.getEngineSize());
    createVehicleColour.setText(v.getColour());
    createVehicleFuel.setText(v.getFuelType());
    String MOTexpire = "";
    if (v.getMOTExpire() == null)
        MOTexpire = "N/A";
    else
        MOTexpire = v.getMOTExpire().getTime().toString();
        
    createVehicleMOT.setText(MOTexpire);
    
     String ServiceDate = "";
    if (v.getLastService() == null)
        ServiceDate = "N/A";
    else
        ServiceDate = v.getLastService().getTime().toString();
    
    createVehicleService.setText(ServiceDate);
    createVehicleWarranty.setText(v.getWarrantyCompany());

    }
    
    public GregorianCalendar parseToDate(LocalDate date, LocalTime time){ 
        return new GregorianCalendar(date.getYear(),date.getMonthValue()-1,date.getDayOfMonth(),time.getHour(),time.getMinute());
    }
    
    public Booking createBookingClass(){
      
    booking.setBookingType(dbH.getBookingTypeIDbyName(createBookingType.getSelectionModel().getSelectedItem().toString()));
    booking.setBookingTypeName(dbH.getBookingTypeNameByID(booking.getBookingType()));
    booking.setStartDate(parseToDate(createBookingDate.getValue(),createBookingTimeStart.localTimeProperty().getValue()));
    booking.setEndDate(parseToDate(createBookingDate.getValue(),createBookingTimeEnd.localTimeProperty().getValue()));
    booking.setMechanic(dbH.getMechanicByID(Integer.parseInt(createBookingMechanic.getSelectionModel().getSelectedItem().toString().substring(0,1))));
    booking.setBayNumber(Integer.parseInt(createBookingBayNumber.getSelectionModel().getSelectedItem().toString()));
    booking.setBookingFee(dbH.getBookingPricebyID(booking.getBookingType()));
    
    return booking;
    }
    
    public BookingAppointment createAppointment(Booking booking){
       
           if (booking != null)
       {
           BookingAppointment ba = new BookingAppointment();
           ba.setBooking(booking);
           ba.withStartTime(booking.getStartDate());
           ba.withEndTime(booking.getEndDate());
           ba.withSummary(ba.createSummary(booking));
           ba.withAppointmentGroup(lAppointmentGroupMap.get("Bay "+booking.getBayNumber()));
           return ba;
       }
        else         
       return null;    
       
    }
    
    public void setBookingVehicle(int vID, int cID){
    
    booking.setVehicle(dbH.getVehicleByID(vID));
    booking.getVehicle().setCustomer(dbH.getCustomerByID(cID));
    updateDisplayFields(booking);
    
    }
    
    
    public void findVehicle(int cID){
        
    try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/VehicleRecords/details.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene((Pane) loader.load()));
            DetailsController controller = loader.<DetailsController>getController();
            controller.tablecustomer(String.valueOf(cID));
            stage.setAlwaysOnTop(true);
             stage.setResizable(false);
            stage.showAndWait();
            int vID = controller.getID();
            if (vID == 0){
                customerSet = false;
             Dialogs.create()
                             .owner(prevStage)
                             .title("Error!")
                             .masthead(null)
                             .message("Make sure to select a customer AND a vehicle")
                             .showInformation();
            }
            else {
            setBookingVehicle(vID, cID);
            customerSet = true;
            }
        } catch (IOException ex) {
            Logger.getLogger(CreateBookingFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CreateBookingFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    public void findCustomer(){
    
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Customer/SearchModule.fxml"));
            Stage stage = new Stage();
            stage.setAlwaysOnTop(true);
            GLOBAL.StartPagePointer.disableTab();
             stage.setResizable(false);
            stage.setScene(new Scene((Pane) loader.load()));
            SearchModuleController controller = loader.<SearchModuleController>getController();
            stage.showAndWait();
            int found = controller.getSelectedID();
            if (found == -1) {
                return;
            } else {
                Controllers.Customer.CustomerAccount ca = controller.getCustomerAccountFromSearch();
                CustomerAccount myCA = dbH.getCustomerByID(found);
                findVehicle(found);
                stage.close();
                GLOBAL.StartPagePointer.enableTab();
                
            }
        } catch (IOException ex) {
            Logger.getLogger(CreateBookingFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
