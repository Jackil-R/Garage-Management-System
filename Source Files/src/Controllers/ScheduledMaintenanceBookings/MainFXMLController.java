/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.ScheduledMaintenanceBookings;

import Controllers.Customer.CustomerAccount;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.internal.scene.control.skin.agenda.AgendaWeekSkin;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.CalendarRange;
import jfxtras.scene.control.agenda.AgendaSkinSwitcher;
import main.GLOBAL;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Julians
 */
public class MainFXMLController implements Initializable {
    
    private Collection<Appointment> selectedAppointments;
    Stage prevStage;
    Scene scene;
    private boolean dialogOpen;
    private Booking selectedBooking;
    private BookingHandler dbH = new BookingHandler();
    
    /**
     *
     */
    @FXML
    public AnchorPane pane;

    /**
     *
     */
    public Agenda agenda;

    /**
     *
     */
    public DatePicker datepicker;

    /**
     *
     */
    public Button btnCreateNewBooking;

    /**
     *
     */
    public Button btnCreateNewServiceReport;

    /**
     *
     */
    public Button btnAgendaWeekLeft;

    /**
     *
     */
    public Button btnAgendaWeekRight;

    /**
     *
     */
    public GridPane gridPane;
    //CUSTOMERS

    /**
     *
     */
        public TextField tfCustomerName;

    /**
     *
     */
    public TextField tfCustomerSurname;

    /**
     *
     */
    public TextField tfCustomerNumber;

    /**
     *
     */
    public TextField tfCustomerAddress1;

    /**
     *
     */
    public TextField tfCustomerAddress2;

    /**
     *
     */
    public TextField tfCustomerCounty;

    /**
     *
     */
    public TextField tfCustomerPostal;

    /**
     *
     */
    public TextField tfCustomerCompany;
    //VEHICLES

    /**
     *
     */
        public TextField tfVehicleMake;

    /**
     *
     */
    public TextField tfVehicleModel;

    /**
     *
     */
    public TextField tfVehicleReg;

    /**
     *
     */
    public TextField tfVehicleMileage;

    /**
     *
     */
    public TextField tfVehicleEngine;

    /**
     *
     */
    public TextField tfVehicleColour;

    /**
     *
     */
    public TextField tfVehicleFuel;

    /**
     *
     */
    public TextField tfVehicleMOT;

    /**
     *
     */
    public TextField tfVehicleService;

    /**
     *
     */
    public TextField tfVehicleWarranty;
    //BOOKINGS

    /**
     *
     */
        public TextField tfBookingType;

    /**
     *
     */
    public TextField tfBookingFee;

    /**
     *
     */
    public TextField tfBookingDate;

    /**
     *
     */
    public TextField tfBookingMechanic;

    /**
     *
     */
    public TextField tfBookingTimeStart;

    /**
     *
     */
    public TextField tfBookingTimeEnd;

    /**
     *
     */
    public TextField tfBookingBayNumber;

    /**
     *
     */
    public TextArea tfBookingNotes;

    /**
     *
     */
    public TextArea tfBookingParts;

    /**
     *
     */
    public Map<String, Agenda.AppointmentGroup> lAppointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();

    /**
     *
     * @return
     */
    public Map<String, Agenda.AppointmentGroup> getlAppointmentGroupMap() {
        return lAppointmentGroupMap;
    }
    
    /**
     *
     */
    public MainFXMLController(){
    }
  
    /**
     *
     * @param scene
     */
    public void setScene(Scene scene){
        //Previous stage for when going back between modules
         this.scene = scene;
    }
     
    /**
     *
     * @param stage
     */
    public void setPrevStage(Stage stage){
        //Previous stage for when going back between modules
         this.prevStage = stage;
    }
    
    /*
    HERE IS THE CODE FOR OPENING THE GUI DIALOG FOR WHEN CREATING A NEW BOOKING
    */

    /**
     *Takes a BookingAppointment and opens the dialog for editing the booking
     * 
     * @param appointment
     * @return True if edited, False if no edit occured
     * @throws IOException
     */
    
    public boolean editBookingDialog(BookingAppointment appointment) throws IOException{
       Date now = new Date();
       boolean beforeNow = appointment.getBooking().getStartDate().getTime().before(now); 
             
        
        if (beforeNow)
            {

                  Dialogs.create()
                             .owner(prevStage)
                             .title("Error!")
                             .masthead(null)
                             .message("You can not edit a booking in the past!")
                             .showInformation();

                  return false;
              }
        
        dialogOpen = true;
        
        FXMLLoader loader = new FXMLLoader(
    getClass().getResource(
      "/FXML/ScheduledMaintenanceBookings/BookingDetailFXML.fxml"
    )
  );

  Stage stage = new Stage();
  stage.setScene(
    new Scene(
      (Pane) loader.load()
    )
  );

  BookingDetailFXMLController controller = 
    loader.<BookingDetailFXMLController>getController();
 
  
    controller.setAppointment(appointment);
    
    controller.updateDisplayFields();
    controller.addListeners();
    controller.setStage(stage);
    GLOBAL.StartPagePointer.disableTab();
    stage.setResizable(false);
    stage.setAlwaysOnTop(true);
    stage.showAndWait();
    dialogOpen = false;
    GLOBAL.StartPagePointer.enableTab();

  return controller.isDeleted();
    }
    
    /*
    HERE IS THE CODE FOR OPENING THE GUI DIALOG FOR WHEN CREATING A NEW BOOKING FROM DRAGGING ON THE AGENDA
    */

    /**
     *
     * @param startRange
     * @param endRange
     * @return
     * @throws IOException
     */
    
     public BookingAppointment createNewBookingDialog(Calendar startRange,Calendar endRange) throws IOException{
         Validator v = new Validator();
        if (! v.checkHoliday(startRange) )
            {

                  Dialogs.create()
                             .owner(prevStage)
                             .title("Error!")
                             .masthead(null)
                             .message("This day is a public holiday!")
                             .showInformation();

                  return null;
              }
        
        if (! v.checkPastDay(startRange) )
            {

                  Dialogs.create()
                             .owner(prevStage)
                             .title("Error!")
                             .masthead(null)
                             .message("You can not make a booking in the past!")
                             .showInformation();

                  return null;
              }
            
        dialogOpen = true;
        
      FXMLLoader loader = new FXMLLoader(
    getClass().getResource(
      "/FXML/ScheduledMaintenanceBookings/CreateBookingFXML.fxml"
    )
  );

  Stage stage = new Stage();
  stage.setScene(
    new Scene(
      (Pane) loader.load()
    )
  );

  CreateBookingFXMLController controller = 
    loader.<CreateBookingFXMLController>getController();
 
    controller.setCalenderRanges(startRange, endRange);
    controller.addListeners();
    controller.setStage(stage);
    controller.setlAppointmentGroupMap(lAppointmentGroupMap);
    
    GLOBAL.StartPagePointer.disableTab();
    
    stage.setResizable(false);
    stage.setAlwaysOnTop(true);
    stage.showAndWait();
  dialogOpen = false;
  GLOBAL.StartPagePointer.enableTab();
    return controller.getAppointment();
    }
       
    /**
     *
     */
    public void createServiceReport(){
        if (dialogOpen)
        {
        
        Dialogs.create()
                             .owner(prevStage)
                             .title("Complete Action!")
                             .masthead(null)
                             .message("Booking Detail window already open.")
                             .showInformation();
        
        return;
        
        
        }
        boolean editingPrev = false;
        
        if (selectedBooking == null) {
        Dialogs.create()
                             .owner(prevStage)
                             .title("Complete Action!")
                             .masthead(null)
                             .message("Select a booking first.")
                             .showInformation();
        return;
        }
        
        
        if (selectedBooking.getBookingType() == 3) 
                         {
                          Dialogs.create()
                             .owner(prevStage)
                             .title("Error!")
                             .masthead(null)
                             .message("Go to Diagnosis and Repair to create a service report for this booking.")
                             .showInformation();
                          return;
                         }
                          
        Date now = new Date();
        boolean isNow = selectedBooking.getStartDate().getTime().before(now) && selectedBooking.getEndDate().getTime().after(now);
        
        if (!isNow)
            {

              if   (  selectedBooking.getEndDate().getTime().before(now)   ){
                  Action response = Dialogs.create()
        .owner(prevStage)
        .title("Edit Service Report")
        .masthead("You are attempting to create a service report for a booking in the past.")
        .message("Are you sure you want to create this report? It will overwrite the previous one.")
        .actions(Dialog.ACTION_YES, Dialog.ACTION_NO)
        .showConfirm();

if (response == Dialog.ACTION_YES) {
    editingPrev = true;
} else {
    return;
}
              }  else
              {
        Dialogs.create()
                             .owner(prevStage)
                             .title("Complete Action!")
                             .masthead(null)
                             .message("This booking is not due yet.")
                             .showInformation();
        return;}
        }
        

       dialogOpen = true;
       
       
     FXMLLoader loader = new FXMLLoader(
    getClass().getResource(
      "/FXML/ScheduledMaintenanceBookings/ReportDialogFXML.fxml"
    )
  );

  Stage stage = new Stage();
        try {
            stage.setScene(
                    new Scene(
                            (Pane) loader.load()
                    )
            );    } catch (IOException ex) {
            Logger.getLogger(MainFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }

  ReportDialogController controller = 
    loader.<ReportDialogController>getController();
 
    controller.setPrevStage(stage);
    controller.setBooking(selectedBooking, editingPrev);
    GLOBAL.StartPagePointer.disableTab();
    stage.setResizable(false);
    stage.setAlwaysOnTop(true);
    dialogOpen = true;
    stage.showAndWait();
    dialogOpen = false;
    GLOBAL.StartPagePointer.enableTab();
  
   // return controller.getAppointment();
    }
    
    /**
     *
     */
    public void setDatePicker(){
    Instant instant = agenda.getDisplayedCalendar().getTime().toInstant();
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        LocalDate date = zdt.toLocalDate();

        datepicker.setValue(date);
    }    
    
    private void updateDisplayFields(Booking b){
        Vehicle v = b.getVehicle();
        CustomerAccount ca = v.getCustomer(); 
        Mechanic m = b.getMechanic();
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
              
    tfCustomerName.setText(ca.getFirstName());
    tfCustomerSurname.setText(ca.getLastName());
    tfCustomerNumber.setText(ca.getPhoneNumber());
    tfCustomerAddress1.setText(ca.getAddressLine1());
    tfCustomerAddress2.setText(ca.getAddressLine2());
    tfCustomerCounty.setText(ca.getCounty());
    tfCustomerPostal.setText(ca.getPostCode());
    tfCustomerCompany.setText(ca.getCompanyName());
    //VEHICLES
    tfVehicleMake.setText(v.getMake());
    tfVehicleModel.setText(v.getModel());
    tfVehicleReg.setText(v.getRegistration());
    tfVehicleMileage.setText(Integer.toString(v.getMileage()));
    tfVehicleEngine.setText(v.getEngineSize());
    tfVehicleColour.setText(v.getColour());
    tfVehicleFuel.setText(v.getFuelType());
    
     String MOTexpire = "";
    if (v.getMOTExpire() == null)
        MOTexpire = "N/A";
    else
        MOTexpire = v.getMOTExpire().getTime().toString();
        
    tfVehicleMOT.setText(MOTexpire);
    
     String ServiceDate = "";
    if (v.getLastService() == null)
        ServiceDate = "N/A";
    else
        ServiceDate = v.getLastService().getTime().toString();
    
    tfVehicleService.setText(ServiceDate);
    
    tfVehicleWarranty.setText(v.getWarrantyCompany());
    //BOOKINGS
    tfBookingType.setText(b.getBookingTypeName());

    String fee = b.getBookingFee().toString();
    tfBookingFee.setText(fee);
    
    tfBookingDate.setText(b.getStartDate().getTime().toString());
    
    tfBookingMechanic.setText(m.getFirstName()+" "+m.getLastName());
    
      String timeStart = "";
    if (b.getStartDate().getTime().getHours() < 10) timeStart = "0";
    timeStart += b.getStartDate().getTime().getHours()+":";
    
    if (b.getStartDate().getTime().getMinutes() < 10) timeStart += "0";
    timeStart += b.getStartDate().getTime().getMinutes();
    
    tfBookingTimeStart.setText(timeStart);
    
    String timeEnd = "";
    if (b.getEndDate().getTime().getHours() < 10) timeEnd = "0";
    timeEnd += b.getEndDate().getTime().getHours()+":";
    
    if (b.getEndDate().getTime().getMinutes() < 10) timeEnd += "0";
    timeEnd += b.getEndDate().getTime().getMinutes();
    
    tfBookingTimeEnd.setText(timeEnd);
    
    tfBookingBayNumber.setText(Integer.toString(b.getBayNumber()));
    tfBookingNotes.setText(b.getBookingNotes());
    if (b.getPartList() != null)
    if (b.getPartList().size() > 0)
    tfBookingParts.setText(PartListToString(b.getPartList()));
    else
        tfBookingParts.setText("N/A");
    }
    
    /**
     *
     * @param list
     * @return
     */
    public String PartListToString(ArrayList<Part> list){
        String msg = "";
        
        for (int i=0; i < list.size(); i++)
            msg += "[ "+list.get(i).getName()+" ]";
        
        return msg;
    }
     
    
    static private Calendar getFirstDayOfWeekCalendar(Locale locale, Calendar c)
    {
        // result
        int lFirstDayOfWeek = Calendar.getInstance(locale).getFirstDayOfWeek();
        int lCurrentDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int lDelta = 0;
        if (lFirstDayOfWeek <= lCurrentDayOfWeek)
        {
            lDelta = -lCurrentDayOfWeek + lFirstDayOfWeek;
        }
        else
        {
            lDelta = -lCurrentDayOfWeek - (7-lFirstDayOfWeek);
        }
        c = ((Calendar)c.clone());
        c.add(Calendar.DATE, lDelta);
        return c;
    }
    
    /**
     *
     */
    public void goDayForward(){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(agenda.displayedCalendar().get().getTime());
        calendar.add(Calendar.DATE, 1);
        
    agenda.setDisplayedCalendar(calendar);
    agenda.refresh();
    setDatePicker();
    }
    
    /**
     *
     */
    public void goDayBack(){
    GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(agenda.displayedCalendar().get().getTime());
        calendar.add(Calendar.DATE, -1);
        
    agenda.setDisplayedCalendar(calendar);
    agenda.refresh();
    setDatePicker();
    }
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        GLOBAL.ScheduledPointer = this;
                
        AgendaSkinSwitcher agendaSkinSwitcher = new AgendaSkinSwitcher(agenda);
        gridPane.add(agendaSkinSwitcher,0,0);
        Calendar c = Calendar.getInstance();
        agenda.displayedCalendar().set(c);
        
      //  agenda.allowResizeProperty().set(false);
        
        Mechanic m = dbH.getMechanicByID(GLOBAL.getMechanicID());
        String mName = m.getFirstName()+" "+m.getLastName();
        int jobCount = dbH.getTotalBookingsTodayByMechanicID(GLOBAL.getMechanicID());
        
        Dialogs.create()
                             .owner(prevStage)
                             .title("Welcome Back!")
                             .masthead(null)
                             .message("Hello, "+mName+"! You have "+jobCount+" Bookings scheduled coming up!")
                             .showInformation();
              
        dialogOpen = false;
       selectedAppointments = agenda.selectedAppointments();
        
        
        lAppointmentGroupMap.put("Repair", new Agenda.AppointmentGroupImpl().withStyleClass("group18"));
        lAppointmentGroupMap.put("Bay 1", new Agenda.AppointmentGroupImpl().withStyleClass("group1"));
        lAppointmentGroupMap.put("Bay 2", new Agenda.AppointmentGroupImpl().withStyleClass("group4"));
        lAppointmentGroupMap.put("Bay 3", new Agenda.AppointmentGroupImpl().withStyleClass("group6"));
        lAppointmentGroupMap.put("Bay 4", new Agenda.AppointmentGroupImpl().withStyleClass("group9"));
        lAppointmentGroupMap.put("Bay 5", new Agenda.AppointmentGroupImpl().withStyleClass("group15"));
        lAppointmentGroupMap.put("Bay 6", new Agenda.AppointmentGroupImpl().withStyleClass("group22"));
        for (String lId : lAppointmentGroupMap.keySet())
        {
            Agenda.AppointmentGroup lAppointmentGroup = lAppointmentGroupMap.get(lId);
            lAppointmentGroup.setDescription(lId);
            agenda.appointmentGroups().add(lAppointmentGroup);
        }
               
        
        /*
        NEW BOOKINGS ARE HANDLED HERE
        THE METHOD CALL(CALENDER RANGE) MUST RETURN AN OBJECT OF APPOINTMENTIMP OR A SUBCLASS OF THIS
        */
        agenda.createAppointmentCallbackProperty().set(new Callback<Agenda.CalendarRange, Agenda.Appointment>()
        {
            @Override
            public Agenda.Appointment call(Agenda.CalendarRange calendarRange)
            {        
                if (!dialogOpen){
                try {   
                    BookingAppointment ba = createNewBookingDialog(calendarRange.getStartCalendar(), calendarRange.getEndCalendar());                   
                    return ba;
                } catch (IOException ex) {
                    Logger.getLogger(MainFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
                else {               
                     Dialogs.create()
                             .owner(prevStage)
                             .title("Complete Action!")
                             .masthead(null)
                             .message("Booking Detail window already open.")
                             .showInformation();
                     
                     return null;
                }
  
                Dialogs.create()
        .owner(prevStage)
        .title("Error!")
        .masthead(null)
        .message("There was an error creating your booking. Please try again.")
        .showInformation();
                return null;

            }
        });
 
        /*
            THIS IS FOR SELECTED BOOKINGS
        THIS IS CALLED WHEN USER LEFT CLICKS ON A BOOKING/APPOINTMENT ON THE AGENDA
        THIS WILL CHANGE VIEWS ON THE DISPLAY
        */
        agenda.selectedAppointments().addListener(new ListChangeListener<Appointment>() {
              @Override
              public void onChanged(ListChangeListener.Change<? extends Appointment> c) {
                  c.next();
                  for (Appointment a : c.getAddedSubList()) {
                      BookingAppointment ba = (BookingAppointment) a;
                      updateDisplayFields(ba.getBooking());
                      selectedBooking = ba.getBooking();
                 }
                     }
                });
        
        /*
            THIS IS FOR EDITING BOOKINGS
        THIS IS CALLED WHEN USER RIGHT CLICKS ON BOOKING/APPOINTMENT ON THE AGENDA
        */
        agenda.editAppointmentCallbackProperty().set(new Callback<Agenda.Appointment, java.lang.Void >()
        {          

             @Override
             public Void call(Agenda.Appointment param) {                
                 if (!dialogOpen){                     
                     try {
                         BookingAppointment ba = (BookingAppointment) param;    
                         if (ba.getBooking().getBookingType() == 3) 
                         {
                          Dialogs.create()
                             .owner(prevStage)
                             .title("Error!")
                             .masthead(null)
                             .message("Go to Diagnosis and Repair to edit this Booking.")
                             .showInformation();
                          return null;
                         }   
                         
                         //ALLOW EDIT OF BOOKING
                         if (!editBookingDialog(ba)){
                         ba.withStartTime(ba.getBooking().getStartDate());
                         ba.withEndTime(ba.getBooking().getEndDate());
                         refreshAgenda();
                         }
                         else {
                             agenda.appointments().remove(ba);
                             
                         }
                         
                     } catch (IOException ex) {
                         Logger.getLogger(MainFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                     }
                     return null;
                 }
                 else
                 {
                     Dialogs.create()
                             .owner(prevStage)
                             .title("Complete Action!")
                             .masthead(null)
                             .message("Booking Detail window already open.")
                             .showInformation();
                     
                     return null;
                 }
                 

             }
        });     
        
        
        
        
        
        //ADD ALL BOOKINGS FROM DATABASE TO AGENDA HERE       
        ArrayList<Booking> listBookings = dbH.getAllBookings();
        for (int i = 0; i < listBookings.size(); i++)
        {           
            Booking b = listBookings.get(i);
            BookingAppointment ba = new BookingAppointment(b);
 
            
            if (b.getBookingType() == 3)
            {
            ba.withAppointmentGroup(lAppointmentGroupMap.get("Repair"));
            }
            else
            ba.withAppointmentGroup(lAppointmentGroupMap.get("Bay "+b.getBayNumber()));
            
            ba.withStartTime(b.getStartDate());
            ba.withEndTime(b.getEndDate());
            agenda.appointments().add(ba);    
            addListeners(ba);
            ServiceReport sr = dbH.getServiceReportByBookingID(ba.getBooking().getID());
            int srID = sr.getID();
            if (srID != -1)
            ba.getBooking().setPartList(dbH.getServicePartsBySRID(srID));
            
        }     
        
        setDatePicker();
        datepicker.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {       
                LocalDate ldate = datepicker.getValue();
                Instant instant = Instant.from(ldate.atStartOfDay(ZoneId.of("GMT")));
                Date date = Date.from(instant);
                Calendar cal = new GregorianCalendar();
                cal.setTime(date);
                agenda.displayedCalendar().set(cal);
            }
        });
        
        
    }  
    
    /**
     *
     */
    public void refreshAgenda(){
        
    agenda.appointments().removeAll(agenda.appointments());
    
        //ADD ALL BOOKINGS FROM DATABASE TO AGENDA HERE       
        ArrayList<Booking> listBookings = dbH.getAllBookings();
        for (int i = 0; i < listBookings.size(); i++)
        {           
            Booking b = listBookings.get(i);
            BookingAppointment ba = new BookingAppointment(b);
            
            if (b.getBookingType() == 3)
            {
            ba.withAppointmentGroup(lAppointmentGroupMap.get("Repair"));
            }
            else
            ba.withAppointmentGroup(lAppointmentGroupMap.get("Bay "+b.getBayNumber()));
            
            ba.withStartTime(b.getStartDate());
            ba.withEndTime(b.getEndDate());
            agenda.appointments().add(ba);    
            addListeners(ba);
            ServiceReport sr = dbH.getServiceReportByBookingID(ba.getBooking().getID());
            int srID = sr.getID();
            if (srID != -1)
            ba.getBooking().setPartList(dbH.getServicePartsBySRID(srID));
            
        }   
    }
    
    /**
     *
     * @param ba
     */
    public void addListeners(BookingAppointment ba){
    
            ConflictHandle ch = new ConflictHandle(ba);
                     
    ba.startTimeProperty().addListener(ch.startListen);
    
    ba.endTimeProperty().addListener(ch.endListen);
       
    }
        
     
     //BROKEN CLASS, CURRENTLY DISSALLOWS DRAGGING APPOINTMENT TO NEW TIME

    /**
     *
     */
         public class ConflictHandle{
         
         ChangeListener startListen = new ChangeListener<Calendar>() {
     
        @Override
        public void changed(
                ObservableValue<? extends Calendar> observableValue,
                Calendar oldValue, Calendar newValue) {
           if (handleChangeTime(oldValue, newValue,"START"))
           {
               bookingA.startTimeProperty().removeListener(startListen);
              bookingA.withStartTime(newValue);
              resetValues();
              bookingA.startTimeProperty().addListener(startListen);
           }
            else 
           {
               bookingA.startTimeProperty().removeListener(startListen);
                bookingA.withStartTime(oldValue);
                resetValues();
                bookingA.startTimeProperty().addListener(startListen);
           }
        }
        };
         
         ChangeListener endListen = new ChangeListener<Calendar>() {
            
        @Override
        public void changed(
                ObservableValue<? extends Calendar> observableValue,
                Calendar oldValue, Calendar newValue) {
            if (handleChangeTime(oldValue, newValue,"END"))  
            {
                bookingA.endTimeProperty().removeListener(endListen);
            bookingA.withEndTime(newValue);
            resetValues();
            bookingA.endTimeProperty().addListener(endListen);
            }
            else 
            {
                 bookingA.endTimeProperty().removeListener(endListen);
                bookingA.withEndTime(oldValue);
                resetValues();
                 bookingA.endTimeProperty().addListener(endListen);
            }
            
            
        }
        };   
         
         boolean firstSet = false;
         boolean secondSet = false;
         boolean resolved = false;
         private Calendar oldS, oldE, newS, newE;
         BookingAppointment bookingA;
         
        /**
         *
         * @param ba
         */
        public ConflictHandle(BookingAppointment ba){
             bookingA = ba;
         }
     
        /**
         *
         * @param oS
         * @param oE
         * @param sORe
         * @return
         */
        public boolean handleChangeTime(Calendar oS, Calendar oE, String sORe){
          if (resolved) return true;
        
          if (!firstSet )
          { oldS = oS; newS = oE; firstSet = true; 
//              System.out.println("FIRST");
//              System.out.println("OS: "+oS.getTime().getMinutes());
//              System.out.println("OE: "+oE.getTime().getMinutes());
//              System.out.println("1st "+sORe);
          return false;}
          
          if (firstSet )
          {
              System.out.println("2nd "+sORe);
              //Due to conflict with change listeners, you must check that the new values do not match with the start time
              //No booking will have start and end time as equal so this method is valid for checking
              if (oE == null || oS == null )
              {
                  System.out.println("NULL");
                      return false;
              }
              
              if ( oldS.equals(oE) || newS.equals(oS)){
//                  System.out.println("FALSE on SECOND");
//              System.out.println("OS: "+oS.getTime().getMinutes());
//              System.out.println("OE: "+oE.getTime().getMinutes());
              return false;
              }
              
//              
//              System.out.println("SUCCESS ON SECOND");
//              System.out.println("OS: "+oS.getTime().getMinutes());
//              System.out.println("OE: "+oE.getTime().getMinutes());
          oldE = oS; newE = oE; secondSet = true; 
          return promptChangeTimes();
        //  return true;
          }
          
          
          return false;
        }    
      
        /**
         *
         * @return
         */
        public boolean promptChangeTimes(){

      SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");
 
   Action response = Dialogs.create()
        .owner(prevStage)
        .title("Change Booking Time Confirmation")
        .masthead("You are about to change this booking START time from: "+sdf.format(oldS.getTime()).toString()
                +" to: "+sdf.format(newS.getTime()).toString()+" AND END time from: "+sdf.format(oldE.getTime()).toString()
                +" to: "+sdf.format(newE.getTime()).toString()+".")
        .message("Are you sure you want to change this booking?")
        .actions(Dialog.ACTION_YES, Dialog.ACTION_NO)
        .showConfirm();

if (response == Dialog.ACTION_YES) {

        resolved = true;
       // resetValues();
        return true;
} else {

   // resolved = true;
    resetValues();
    return false;
}
     
     
     }
           
        /**
         *
         */
        public void resetValues(){
           firstSet = false;
           secondSet = false;
           oldS = null;
           oldE = null;
           newS = null;
           newE = null;
          // resolved = false;
           }

           
           
     } // END OF CONFLICT CLASS
     

        
        
    
}
