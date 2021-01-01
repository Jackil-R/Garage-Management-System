package Controllers.DiagnosisRepairBookings;
import Controllers.Parts.BasketPart;
import Controllers.ScheduledMaintenanceBookings.AuthFXMLController;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.AgendaSkinSwitcher;
import main.GLOBAL;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author jackilrajnicant
 */
public class FXMLDocumentController implements Initializable {

    //Objects
    Validator validate = new Validator();
    @FXML Collection<Appointment> selectedAppointments;
    @FXML Agenda agenda = new Agenda();
    @FXML GridPane gridPane;
    @FXML Map<String, Agenda.AppointmentGroup> lAppointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
    @FXML Stage prevStage;
    @FXML Scene scene;
    @FXML AuthFXMLController a = new AuthFXMLController();
  

    
    //normal variables
    @FXML private boolean openCustomer;
    @FXML private boolean openVehicle;
    @FXML private boolean openCreate;
    @FXML private boolean openEdit=false;    
    //AlertBoxes
    @FXML Alert infoUpdate = new Alert(AlertType.INFORMATION);
    @FXML Alert warningUpdate = new Alert(AlertType.WARNING);
    @FXML Alert bookingDates = new Alert(AlertType.INFORMATION);

    //TextField Components
    @FXML TextField mil = new TextField();
    @FXML TextField TbookingID = new TextField();
    @FXML TextField TbookingType = new TextField();
    @FXML TextField TbookingDate = new TextField();
    @FXML TextField Tmechanic = new TextField();
    @FXML TextField TstartTime = new TextField();
    @FXML TextField TendTime = new TextField();
    @FXML TextField TbayNumber = new TextField();
    @FXML TextField TbookingFee = new TextField();
    @FXML TextField Tsearch = new TextField();
    @FXML TextField user = new TextField();
    @FXML TextField status = new TextField();

    //TextArea Components
    @FXML TextArea Tfault = new TextArea();
    @FXML TextArea Tparts = new TextArea();
    @FXML DatePicker sd = new DatePicker(LocalDate.now());

    //Button Components
    @FXML Button viewCustomerDetails = new Button();
    @FXML Button viewVehicleDetails = new Button();
    @FXML Button left = new Button();
    @FXML Button right = new Button();
    @FXML Button editBooking = new Button();

    /**
     * The method DateToCalendar basically take a the date object and converts it to calendar object
     * @param date
     * @return Calendar
     */
    public Calendar DateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
    
    /**
     * The method below convert a calendar object to string object for getting the time.
     * @param date
     * @return 
     */
    public String CalendartoTime(Calendar date){
        String time = "";
        if (date.getTime().getHours() < 10) {time = "0";}
        time += date.getTime().getHours() + ":";
        if (date.getTime().getMinutes() < 10) {time += "0";}
        time += date.getTime().getMinutes();
        return time;
    }

    /**
     * The method below parses the string object to date object therefore
     * we throw ParseException to catch the parsing error if error occurs.
     * @param date
     * @return
     * @throws ParseException 
     */
    public Date dateParse(String date) throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String input = date;
        Date t = ft.parse(input);
        return t;
    }
    
    /**
     * The method below is a setter for Scene.
     * @param scene 
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    
    /**
     * The method below is a setter for setting the previous Scene.
     * @param stage 
     */
    public void setPrevStage(Stage stage) {
        this.prevStage = stage;
    }
 
    /**
     * The methods multipleWindow is called when there are multiple window of one type is opened.
     * @param str 
     */
    public void multipleWindow(String str){
        warningUpdate.setTitle("Warning");
        warningUpdate.setHeaderText(null);
        warningUpdate.setContentText(str+" window is already open.");  
        warningUpdate.showAndWait();
    }
    
    /**
     *  This method display a dialog for user direct them to maintenance booking.
     */
    public void editMaintance(){
        infoUpdate.setTitle("Information");
        infoUpdate.setHeaderText(null);
        infoUpdate.setContentText("Go to Maintaince Booking to Edit");  
        infoUpdate.showAndWait();
    }
    
    /**
     * This method display a dialog for displaying many message on the screen.
     * @param a 
     */
    public void message(String a){
        warningUpdate.setWidth(200);
        warningUpdate.setHeight(200);
        warningUpdate.setResizable(true);
        warningUpdate.setTitle("Warning");
        warningUpdate.setHeaderText(null);
        warningUpdate.setContentText(a);
        warningUpdate.showAndWait();
    }
    
    /**
     * The editBookingDetail methods opens up edit booking window for user.
     * @param appointment
     * @return
     * @throws IOException 
     */
    public boolean editBookingDetail(BookingAppointment appointment) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/DiagnosisRepairBookings/EditDetails.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        EditDetailsController controller = loader.<EditDetailsController>getController();
        controller.setAppointment(appointment);
        controller.displayData();
        controller.setlAppointmentGroupMap(lAppointmentGroupMap);
        controller.checkDates();
        controller.setStage(stage);
        GLOBAL.StartPagePointer.disableTab();
        stage.showAndWait();
        GLOBAL.StartPagePointer.enableTab();
        openEdit=false;
        return controller.isDeleteBookingFromAgenda();
    }

    /**
     * The createBookingDetail methods opens up create new booking window for user.
     * @param startRange
     * @param endRange
     * @return
     * @throws IOException 
     */
    public BookingAppointment createBookingDetail(Calendar startRange, Calendar endRange) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/DiagnosisRepairBookings/CreateBooking.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        CreateBookingController controller = loader.<CreateBookingController>getController();
        controller.setDate(startRange, endRange);
        controller.setStage(stage);
        controller.setlAppointmentGroupMap(lAppointmentGroupMap);
        GLOBAL.StartPagePointer.disableTab();
        stage.showAndWait();
        GLOBAL.StartPagePointer.enableTab();
        openCreate=false;
        return controller.getCurrentAppointment();
    }
    
    /**
     * The displayCustomerDetails methods opens up a dialog box with customer details.
     * @param booking
     * @throws IOException 
     */
    public void displayCustomerDetails(DRBooking booking) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/DiagnosisRepairBookings/ViewCustomer.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        ViewCustomerController controller = loader.<ViewCustomerController>getController();
        controller.setBooking(booking);
        controller.displayData();
        controller.setStage(stage);
        GLOBAL.StartPagePointer.disableTab();
        stage.showAndWait();
        GLOBAL.StartPagePointer.enableTab();
        openCustomer=false;
    }

    /**
     * The displayVehicleDetails methods opens up a dialog box with vehicle details.
     * @param booking
     * @throws IOException 
     */
    public void displayVehicleDetails(DRBooking booking) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/DiagnosisRepairBookings/ViewVehicle.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        ViewVehicleController controller = loader.<ViewVehicleController>getController();
        controller.setBooking(booking);
        controller.displayData();
        controller.setStage(stage);
        GLOBAL.StartPagePointer.disableTab();
        stage.showAndWait();
        GLOBAL.StartPagePointer.enableTab();
        openVehicle=false;
    }

    /**
     * initialize is default method and this is where all the EventHandler, ActionListener and all the type
     * of listeners are initialize.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GLOBAL.DiagnosisPointer = this;
        
        sd.setValue(LocalDate.now());
        AgendaSkinSwitcher agendaSkinSwitcher = new AgendaSkinSwitcher(agenda);
        gridPane.add(agendaSkinSwitcher, 0, 0);
        sd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    agenda.setDisplayedCalendar(DateToCalendar(dateParse(sd.getValue().toString())));
                } catch (ParseException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
                agenda.refresh();
            }
        });
        
        right.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                LocalDate d = sd.getValue();
                d = d.plusDays(1);
                sd.setValue(d);
                try {
                    agenda.setDisplayedCalendar(DateToCalendar(dateParse(sd.getValue().toString())));
                } catch (ParseException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
                agenda.refresh();
            }
        });

        left.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                LocalDate d = sd.getValue();
                d = d.plusDays(-1);
                sd.setValue(d);
                try {
                    agenda.setDisplayedCalendar(DateToCalendar(dateParse(sd.getValue().toString())));
                } catch (ParseException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
                agenda.refresh();
            }
        });

        selectedAppointments = agenda.selectedAppointments();
        // setup appointment groups
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
        
        try {
            displayAllBooking();
        } catch (ParseException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        //When right click on a appointment, it will do something.....
        agenda.editAppointmentCallbackProperty().set(new Callback<Agenda.Appointment, java.lang.Void>() {
            @Override
            public Void call(Agenda.Appointment param) {
                if (!openEdit) {
                    try {
                        BookingAppointment ba = (BookingAppointment) param;
                        if (ba.getBooking().getBookingType() == 3) {
                            openEdit=true;
                            if(editBookingDetail(ba)) {
                                agenda.appointments().remove(param);
                                display();
                            } else {
                                display();
                            }
                            displayBooking(ba.getBooking());
                        } else {
                            editMaintance();
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return null;
                } else {
                    multipleWindow("Edit Booking");
                    return null;

                }
            }
        });
        
     

        // creating a new appointment and creatingbooking window will pop up.
        agenda.createAppointmentCallbackProperty().set(new Callback<Agenda.CalendarRange, Agenda.Appointment>() {
            @Override
            public Agenda.Appointment call(Agenda.CalendarRange calendarRange) {
                if (!openCreate) {
                    if (!validate.checkBookingDate(calendarRange.getStartCalendar(), calendarRange.getEndCalendar())) {
                        message(validate.getError());
                    } else {
                        try {
                            openCreate=true;
                            BookingAppointment ba = createBookingDetail(calendarRange.getStartCalendar(), calendarRange.getEndCalendar());
                            
                            agenda.refresh();
                            return ba;
                        } catch (IOException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    return null;
                } else {
                    multipleWindow("Create Booking");
                    return null;
                }
            }
            
        });

        // selecting a appointment and customers and vechicle will shown on GUI
        // Also after selecting the appointment if clicked on editing booking then editingbooking window will pop up
        agenda.selectedAppointments().addListener(new ListChangeListener<Appointment>() {
            @Override
            public void onChanged(Change<? extends Appointment> c) {
                c.next();
                for (Appointment a : c.getAddedSubList()) {
                    BookingAppointment ba = (BookingAppointment) a;
                    displayBooking(ba.getBooking());
                    
                    
                    //customer
                    viewCustomerDetails.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            if(!openCustomer){
                                try {
                                    openCustomer=true;
                                    displayCustomerDetails(ba.getBooking());
                                } catch (IOException ex) {
                                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }else{
                                multipleWindow("Customer Details");
                            }   
                        }
                    });
                   
                    //vehicle
                    viewVehicleDetails.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            if(!openVehicle){
                                try {
                                    openVehicle=true;
                                    displayVehicleDetails(ba.getBooking());
                                } catch (IOException ex) {
                                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }else{
                                multipleWindow("Vehicle Details");
                            }     
                        }
                    });

                    //Editing Details
                    editBooking.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            if(!openEdit){
                                try {
                                    if (ba.getBooking().getBookingType() == 3) {
                                        openEdit=true;
                                        if (editBookingDetail(ba)) {
                                            agenda.appointments().remove(a);
                                            display();
                                        } else {
                                            display();
                                        }
                                        displayBooking(ba.getBooking());
                                    } else {
                                        editMaintance();
                                    }
                                } catch (IOException ex) {
                                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (ParseException ex) {   
                                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                                }   
                            }else{
                                multipleWindow("Edit Booking");
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * The method below displays all the details about the booking on the Main page clicked on any booking on 
     * the agenda.
     * @param booking 
     */
    private void displayBooking(DRBooking booking) {
        Mechanic m = booking.getMechanic();
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        mil.setText(Integer.toString(booking.getVehicle().getMileage()));
        
        int type = booking.getBookingType();
        String bType="";
        if(type==1){
            bType="Service";
        }else if(type==2){
            bType="MOT";
        }else{
            bType="Repair";
        }
        TbookingID.setText(Integer.toString(booking.getID()));
        TbookingType.setText(bType);
        TbookingDate.setText(booking.getStartDate().getTime().toString());
        Tmechanic.setText(m.getFirstName() + " " + m.getLastName());
        TstartTime.setText(CalendartoTime(booking.getStartDate()));
        TendTime.setText(CalendartoTime(booking.getEndDate()));
        TbayNumber.setText(Integer.toString(booking.getBayNumber()));
        String fee = formatter.format(booking.getBookingFee());
        TbookingFee.setText(fee);
        if(booking.isBookingFinished()){
            status.setText("Completed");
        }else{
            status.setText("Not Completed");
        }
        Tfault.setText(booking.getFault());
        Tparts.setText(getPartName(booking.getPartList()));
    }
    
    /**
     * The method return a string with the names of parts.
     * @param s
     * @return 
     */
    private String getPartName(ObservableList<BasketPart> s) {
        String list = "";
        if (s == null || s.isEmpty()) {
            return null;
        }
        for (BasketPart item : s) {
            list += item.getName() + ", Quantity: "+item.getWithdraw()+"\n";
        }
        list = list.substring(0, list.length() - 1);
        return list;
    }
    
    /**
     * The method displayAllBooking get all the booking from the database and stores it in a ArrayList
     * of all booking objects and adds it to the agenda.
     * @throws ParseException 
     */
   public void displayAllBooking() throws ParseException{
          DatabaseHandler databaseHandler = new DatabaseHandler();
        ArrayList<DRBooking> listBookings = databaseHandler.getAllBookings();    
        for (int i = 0; i < listBookings.size(); i++)
        {          
            DRBooking b = listBookings.get(i);
            BookingAppointment ba = new BookingAppointment(b);
            ba.withAppointmentGroup(lAppointmentGroupMap.get("Bay " + b.getBayNumber()));
            ba.withStartTime(b.getStartDate());
            ba.withEndTime(b.getEndDate());
            agenda.appointments().add(ba);
            addListeners(ba);
        }    
           
    }
   
   public void display() throws ParseException{
         DatabaseHandler databaseHandler = new DatabaseHandler();
       agenda.appointments().removeAll(agenda.appointments());
       
         ArrayList<DRBooking> listBookings = databaseHandler.getAllBookings();    
        for (int i = 0; i < listBookings.size(); i++)
        {          
            DRBooking b = listBookings.get(i);
            BookingAppointment ba = new BookingAppointment(b);
            ba.withAppointmentGroup(lAppointmentGroupMap.get("Bay " + b.getBayNumber()));
            ba.withStartTime(b.getStartDate());
            ba.withEndTime(b.getEndDate());
            agenda.appointments().add(ba);
            addListeners(ba);
        }    
           
    }
    
    public void addListeners(Controllers.DiagnosisRepairBookings.BookingAppointment ba) {
        FXMLDocumentController.ConflictHandle ch = new FXMLDocumentController.ConflictHandle(ba);
        ba.startTimeProperty().addListener(ch.startListen);
        ba.endTimeProperty().addListener(ch.endListen);
    }

    public class ConflictHandle {

        ChangeListener startListen = new ChangeListener<Calendar>() {

            @Override
            public void changed(
                    ObservableValue<? extends Calendar> observableValue,
                    Calendar oldValue, Calendar newValue) {
                if (handleChangeTime(oldValue, newValue, "START")) {
                    bookingA.startTimeProperty().removeListener(startListen);
                    bookingA.withStartTime(newValue);
                    resetValues();
                    bookingA.startTimeProperty().addListener(startListen);
                } else {
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
                if (handleChangeTime(oldValue, newValue, "END")) {
                    bookingA.endTimeProperty().removeListener(endListen);
                    bookingA.withEndTime(newValue);
                    resetValues();
                    bookingA.endTimeProperty().addListener(endListen);
                } else {
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
        Controllers.DiagnosisRepairBookings.BookingAppointment bookingA;

        public ConflictHandle(Controllers.DiagnosisRepairBookings.BookingAppointment ba) {
            bookingA = ba;
        }

        public boolean handleChangeTime(Calendar oS, Calendar oE, String sORe) {
            if (resolved) {
                return true;
            }

            if (!firstSet) {
                oldS = oS;
                newS = oE;
                firstSet = true;
                return false;
            }

            if (firstSet) {
                System.out.println("2nd " + sORe);
                //Due to conflict with change listeners, you must check that the new values do not match with the start time
                //No booking will have start and end time as equal so this method is valid for checking
                if (oE == null || oS == null) {
                    System.out.println("NULL");
                    return false;
                }

                if (oldS.equals(oE) || newS.equals(oS)) {
                    return false;
                }
                oldE = oS;
                newE = oE;
                secondSet = true;
                return promptChangeTimes();
                //  return true;
            }
            return false;
        }

        public boolean promptChangeTimes() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Action response = Dialogs.create()
                    .owner(prevStage)
                    .title("Change Booking Time Confirmation")
                    .masthead("You are about to change this booking START time from: " + sdf.format(oldS.getTime()).toString()
                            + " to: " + sdf.format(newS.getTime()).toString() + " AND END time from: " + sdf.format(oldE.getTime()).toString()
                            + " to: " + sdf.format(newE.getTime()).toString() + ".")
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

        public void resetValues() {
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
