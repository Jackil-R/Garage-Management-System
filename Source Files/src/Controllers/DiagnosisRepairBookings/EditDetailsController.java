/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.DiagnosisRepairBookings;
import Controllers.Parts.BasketPart;
import Controllers.Parts.OrderPart;
import Controllers.Parts.PartsControllerRepair;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalTimePicker;
import jfxtras.scene.control.agenda.Agenda;
import org.controlsfx.dialog.Dialogs;

/**
 * FXML Controller class
 *
 * @author jackilrajnicant
 */
public class EditDetailsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    //Object
    private boolean deleteBookingFromAgenda;
    private Stage prevStage;
    private DRBooking booking;
    private BookingAppointment appointment;
    private Map<String, Agenda.AppointmentGroup> lAppointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
    private DatabaseHandler databaseHandler = new DatabaseHandler();
    private ArrayList<Mechanic> allMechanics;
    private ObservableList<OrderPart> partsOrdered;
    private ArrayList <Calendar> expertedDate = new ArrayList<Calendar>();    
    private boolean orderedFromSupplier;
    private double partTotal;
    private ObservableList<BasketPart> parts;
    
    
    //Booking Componentss
        //Textfield Components
        @FXML private TextField TbookingType = new TextField();
        @FXML private TextField TbookingFee = new TextField();
        @FXML private TextField showCustomer = new TextField();
        @FXML private TextField TcurrentMileage = new TextField();
        @FXML private TextField TcustomerID = new TextField();
        @FXML private TextField TvehicleID = new TextField();
        @FXML private TextField TvehicleReg = new TextField();
        //ComboBox and Date Components
        @FXML private LocalTimePicker TstartTime = new LocalTimePicker();
        @FXML private LocalTimePicker TendTime = new LocalTimePicker();
        @FXML private DatePicker datePickerStart = new DatePicker();
        @FXML private ComboBox TbayNumber = new ComboBox();
        @FXML private ObservableList<String> optionsMech = FXCollections.observableArrayList();
        @FXML private ComboBox Tmechanics = new ComboBox(optionsMech);
        //TextArea Components
        @FXML private TextArea Tfault = new TextArea();
        @FXML private TextArea Tparts = new TextArea();

    //AlertBoxes
    @FXML Alert alertUpdate = new Alert(Alert.AlertType.ERROR);
    @FXML Alert warningUpdate = new Alert(Alert.AlertType.WARNING);
    @FXML Alert deleteComfirmBox = new Alert(Alert.AlertType.CONFIRMATION);
    @FXML Alert finishBooking = new Alert(Alert.AlertType.CONFIRMATION);
    
    //Buttons Components
    @FXML private Button closeWindow = new Button();
    @FXML private Button saveBooking = new Button();
    @FXML private Button DeleteBooking = new Button();
    @FXML private Button viewParts = new Button();
    @FXML private Button completeBooking = new Button();
    
  

    public EditDetailsController(){
        
    }
    
     /**
     * The method setlAppointmentGroupMap set lAppointmentGroupMap object with object of this class
     * @param lAppointmentGroupMap 
     */
    public void setlAppointmentGroupMap(Map<String, Agenda.AppointmentGroup> lAppointmentGroupMap) {
        this.lAppointmentGroupMap = lAppointmentGroupMap;
    }
    
    /**
     * The method below get the Map<String, Agenda.AppointmentGroup> object.
     * @return 
     */
    public Map<String, Agenda.AppointmentGroup> getlAppointmentGroupMap() {
        return lAppointmentGroupMap;
    }
 
    /**
     * The method below is a setter for Stage.
     * @param stage 
     */
    public void setStage(Stage stage){
        prevStage = stage;
    }

    
    /**
     * 
     * @param appointment 
     */
    public void setAppointment(BookingAppointment appointment) {
        this.appointment = appointment;
        booking = appointment.getBooking();      
    }

    /**
     * The methods basically closes the current window
     * It also check the Database connection is closed or not.
     */
    public void closeStage(){
        try {
            if(!databaseHandler.connection.isClosed())
                databaseHandler.connection.close();
        } catch (SQLException ex) {
            System.out.println("DRBooking EditingBooking Class, "+ex);
        }
        
        Stage sb = (Stage)closeWindow.getScene().getWindow();
        sb.close();
    }
    
    /**
     * The method nullError display dialog box with field name which is null.
     * @param field 
     */
    public void nullError(String field){
        alertUpdate.setTitle("Error Dialog");
        alertUpdate.setHeaderText(null);
        alertUpdate.setContentText("Please fill in the "+ field+" field.");
        alertUpdate.showAndWait();
    }
    
    /**
     * The method DateToCalendar basically take a the date object and converts it to calendar object
     * @param date
     * @return 
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
     * The method below convert a LocalDate object to Calendar object.
     * @param date
     * @param time
     * @return 
     */
    public GregorianCalendar LocalDatetoToCalendar(LocalDate date, LocalTime time){ 
        return new GregorianCalendar(date.getYear(),date.getMonthValue()-1,date.getDayOfMonth(),time.getHour(),time.getMinute());
    }
    
    /**
     * The method below convert a Calendar object to LocalDate object.
     * @param date
     * @return 
     */
    public LocalDate CalendartoLocalDate(Calendar date){
        LocalDate lDate = LocalDate.of(date.getTime().getYear(),date.getTime().getMonth(),date.getTime().getDay());
        return lDate;
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
     * The method below convert a string object to calendar object
     * @param date
     * @param time
     * @return 
     */
    public Calendar StringtoCalendar(String date, String time){
        String[] splitDate = date.split("-");
        int year = Integer.parseInt(splitDate[0]);
        int month = Integer.parseInt(splitDate[1]);
        int day = Integer.parseInt(splitDate[2]);
        String[] splieTime = time.split(":");
        int hour = Integer.parseInt(splieTime[0]);
        int min = Integer.parseInt(splieTime[1]);
        return new GregorianCalendar(year, month, day, hour, min);
    }
   
    /**
     * The method display is display the details of the selected booking 
     * in the field.
     */
    public void displayData(){
        DRBooking b = booking;
        Vehicle v = b.getVehicle();
        CustomerAccount ca = v.getCustomer(); 
        Mechanic m = b.getMechanic();   
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String name = m.getFirstName();
        int found=0;
        for(int i=0; i<optionsMech.size();i++){
            if(optionsMech.get(i).equalsIgnoreCase(name)){
                found=i;
            }
        }      
        TbookingType.setText("Repair");
        Tmechanics.setValue(optionsMech.get(found));
        TstartTime.localTimeProperty().set(LocalTime.of(b.getStartDate().getTime().getHours(), b.getStartDate().getTime().getHours()));
        TendTime.localTimeProperty().set(LocalTime.of(b.getEndDate().getTime().getHours(), b.getEndDate().getTime().getHours()));
        TbayNumber.setValue(b.getBayNumber());
        showCustomer.setText(ca.getFirstName()+" "+ca.getLastName());
        TcurrentMileage.setText(Integer.toString(v.getMileage()));
        TcustomerID.setText(Integer.toString(ca.getCustomerID()));
        TvehicleID.setText(Integer.toString(v.getID()));
        TvehicleReg.setText(v.getRegistration());
        Instant instant = b.getStartDate().toInstant();
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        LocalDate startdate = zdt.toLocalDate();
        datePickerStart.setValue(startdate);        
        Tfault.setText(b.getFault());
        Tparts.setText(getPartName(b.getPartList()));
        String fee = formatter.format(b.getBookingFee());
        TbookingFee.setText(fee);
        getBayNumber();
        
    }
    
    /**
     * The method below record all the changes in the all the fields and validates it
     * Then called updateBooking Methods in DatabaseHandler
     * @throws ParseException 
     */
    public void saveChanges() throws ParseException{
        try{
        booking.setBookingType(3);
        booking.setStartDate(LocalDatetoToCalendar(datePickerStart.getValue(),TstartTime.localTimeProperty().getValue()));
        booking.setEndDate(LocalDatetoToCalendar(datePickerStart.getValue(),TendTime.localTimeProperty().getValue()));
        booking.setMechanic(databaseHandler.getMechanicByName(Tmechanics.getSelectionModel().getSelectedItem().toString()));
        booking.setBayNumber(Integer.parseInt(TbayNumber.getSelectionModel().getSelectedItem().toString()));
        booking.setFault(Tfault.getText());
        booking.getVehicle().setMileage(Integer.parseInt(TcurrentMileage.getText()));
        booking.setBookingFinished(false);
        }catch(NullPointerException e){
            
        }
        
        if(parts==null){
            booking.setPartList(booking.getPartList());
        }else{
            booking.setPartList(parts);
        }   

        if(booking.getVehicle().getWarranty()!=0){
            booking.setBookingFee(0);
        }else{
            booking.setBookingFee(calculateBookingFee(booking.getStartDate(),booking.getEndDate(),booking.getMechanic(),booking.getPartList()));
        }
  
        appointment.setBooking(booking);
        appointment.withStartTime(booking.getStartDate());
        appointment.withEndTime(booking.getEndDate());
        appointment.withSummary(appointment.createSummary(booking));
        appointment.withAppointmentGroup(lAppointmentGroupMap.get("Bay "+booking.getBayNumber()));

        if (!validateForm()) return;
        databaseHandler.updateBooking(booking, booking.getID());
        deleteBookingFromAgenda=false;
        closeStage();
    }
    
    /**
     * The method below is called when the completeBooking button pressed and 
     * update the database.
     * @throws ParseException 
     */
    public void finishedBooking() throws ParseException{
        finishBooking.setTitle("Booking Completed");
        finishBooking.setHeaderText("Your about the complete a booking.");
        finishBooking.setContentText("Are you sure you want to Finish this booking?");
        ButtonType buttonNo = new ButtonType("NO",ButtonData.NO);
        ButtonType buttonYes = new ButtonType("YES",ButtonData.YES);
        finishBooking.getButtonTypes().setAll(buttonNo, buttonYes);
        Optional<ButtonType> result = finishBooking.showAndWait();

        if (result.get() == buttonYes){
            booking.setBookingFinished(true);
            databaseHandler.updateBooking(booking, booking.getID());
            databaseHandler.bookingCompleted(booking, booking.getID());
            deleteBookingFromAgenda=false;
            closeStage();
        } else {
            //booking not finished
        }
     
        
    }
    
    /**
     * The deleteBooking method deletes the booking from the agenda and database.
     * @throws SQLException 
     */
    public void deleteBooking() throws SQLException{
        deleteComfirmBox.setTitle("Delete Booking Confirmation!");
        deleteComfirmBox.setHeaderText("You are about to delete this Booking. This can not be undone.");
        deleteComfirmBox.setContentText("Are you sure you want to delete this booking?");
        ButtonType buttonNo = new ButtonType("NO",ButtonData.NO);
        ButtonType buttonYes = new ButtonType("YES",ButtonData.YES);
        deleteComfirmBox.getButtonTypes().setAll(buttonNo, buttonYes);
        Optional<ButtonType> result = deleteComfirmBox.showAndWait();
     
        
        if (result.get() == buttonYes){
            databaseHandler.delectBooking(booking,booking.getID());
            deleteBookingFromAgenda=true;
            closeStage();
        } else {
            //not delete the booking
        } 
        
        
    }
    
    /**
     * Listener which listen for change to date so that bay number can be updated
     */
    final ChangeListener<LocalDate> datePickerListener = new ChangeListener<LocalDate>(){
        @Override
        public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
            getBayNumber();
        }  
    };
   
    
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
     * initialize is default method and this is where all the EventHandler, ActionListener and all the type
     * of listeners are initialize.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        datePickerStart.valueProperty().addListener(datePickerListener);
       
        booking = new DRBooking();
        deleteBookingFromAgenda=false;
        allMechanics=databaseHandler.getAllMechanics();
        for(Mechanic m : allMechanics){
            optionsMech.add(m.getFirstName()+" "+m.getLastName()); 
        }
        Tmechanics.setItems(optionsMech);
        viewParts.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    getParts();
                } catch (IOException ex) {
                    System.out.println(ex);
                } catch (ParseException ex) {
                    Logger.getLogger(EditDetailsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        TcurrentMileage.addEventFilter(KeyEvent.KEY_TYPED, numeric_Validation(10));
    }    
    
    /**
     * When the button viewPart is clicked, it called this method
     * which opened up dialog box where the user can select the parts 
     * @throws IOException
     * @throws ParseException 
     */
    public void getParts() throws IOException, ParseException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Parts/PartsRepair.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        PartsControllerRepair controller = loader.<PartsControllerRepair>getController();
        stage.showAndWait();
        parts = controller.getRepairPart();
        partTotal = controller.getTotal();
        Tparts.setText(getPartName(parts));
        partsOrdered = controller.getExpectedPart();
        if(partsOrdered==null){
            orderedFromSupplier=false;
        }else{
            orderedFromSupplier=true;
            getOrderDates(partsOrdered);
        }
    }
    
    /**
     * The method stored all the expected dates in a ArrayList and then sort all the date.
     * @param partsOrdered
     * @throws ParseException 
     */
    public void getOrderDates(ObservableList<OrderPart> partsOrdered) throws ParseException{
        for(int i=0; i<partsOrdered.size();i++){
            expertedDate.add(StringtoCalendar(partsOrdered.get(i).getDate()));
        }        
        Collections.sort(expertedDate);

    }
    
    /**
     * The method below convert a string object to calendar object.
     * @param dateStr
     * @return
     * @throws ParseException 
     */
    public Calendar StringtoCalendar(String dateStr) throws ParseException {
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObj = curFormater.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateObj);
        return calendar;
    }
    
    /**
     * The method below calculates the available bay from booking.
     */
    public void getBayNumber(){
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String timeStart = datePickerStart.getValue().getDayOfMonth() + "/" + datePickerStart.getValue().getMonthValue() + "/"
                    + datePickerStart.getValue().getYear() + " " + TstartTime.localTimeProperty().get().getHour()
                    + ":" + TstartTime.localTimeProperty().get().getMinute() + ":" + TstartTime.localTimeProperty().get().getSecond();

            String timeEnd = datePickerStart.getValue().getDayOfMonth() + "/" + datePickerStart.getValue().getMonthValue() + "/"
                    + datePickerStart.getValue().getYear() + " " + TendTime.localTimeProperty().get().getHour()
                    + ":" + TendTime.localTimeProperty().get().getMinute() + ":" + TendTime.localTimeProperty().get().getSecond();

            try {
                Date startDate = formatter.parse(timeStart);
                Date endDate = formatter.parse(timeEnd);
                TbayNumber.getItems().clear();
                ArrayList<Integer> bays = new ArrayList<Integer>();
                ArrayList<Integer> takenBays = databaseHandler.getFreeBayNumber(startDate, endDate);
                for (int i = 1; i <= 6; i++) {
                    bays.add(i);
                }
                for (int i = 0; i <= takenBays.size() - 1; i++) {
                    bays.remove(takenBays.get(i) - 1);
                }
                TbayNumber.getItems().addAll(bays.toArray());
            } catch (ParseException ex) {
                System.out.println(ex);
            }
        } catch (Exception e) {
            Dialogs.create()
                             .owner(prevStage)
                             .title("Error!")
                             .masthead(null)
                             .message("Please close the window and reopen the Edit Window.")
                             .showInformation();
        }
    }
    
    
    /**
     * The methods calculateBookingFee return the fee for booking working with 
     * the start and end dates and mechanics rate.
     * @param st
     * @param et
     * @param m
     * @param parts
     * @return 
     */
    public double calculateBookingFee(GregorianCalendar st, GregorianCalendar et, Mechanic m,ObservableList<BasketPart> parts){
        int hour = et.getTime().getHours()-st.getTime().getHours();
        double workedPerHour = hour*m.getRate();
        double partSum=0;
        if(parts==null){
            return workedPerHour;
        }else{  
            for(int i=0;i<parts.size();i++){
                int q =Integer.parseInt(parts.get(i).getWithdraw());
                partSum=partSum+(parts.get(i).getPrice()*q);
            }
            return partSum+workedPerHour;
        }
    }
    
    /**
     * The method below is handle which prevent the users from entering character but can only 
     * numeric value.
     * @param max_Length
     * @return 
     */
    public EventHandler<KeyEvent> numeric_Validation(final Integer max_Length) {
        return new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                TextField txt_TextField = (TextField) e.getSource();
                if (txt_TextField.getText().length() >= max_Length) {
                    e.consume();
                }
                if (e.getCharacter().matches("[0-9.]")) {
                    if (txt_TextField.getText().contains(".") && e.getCharacter().matches("[.]")) {
                        e.consume();
                    } else if (txt_TextField.getText().length() == 0 && e.getCharacter().matches("[.]")) {
                        e.consume();
                    }
                } else {
                    e.consume();
                }
            }
        };
    }
    
    /**
     * This method basically checks the whole form which the user fill in
     * and checks null value which will prevent from NullPointException
     * @return 
     */
    public boolean validateForm(){
        Validator validate = new Validator();
        try {
            if (TstartTime.localTimeProperty() == null) {
                nullError("Start Time");
                return false;
            } else if (TendTime.localTimeProperty() == null) {
                nullError("End Time");
                return false;
            } else if (TbayNumber.getValue() == null) {
                nullError("Bay Number");
                return false;
            } else if (Tmechanics.getValue() == null) {
                nullError("Mechanic");
                return false;
            } else if (datePickerStart.getValue() == null) {
                nullError("Start Date");
                return false;
            }else if (TcurrentMileage.getText() == null || TcurrentMileage.getText().equals("")) {
                nullError("Mileage");
                return false;
            } else if(orderedFromSupplier){
                if(checkexpectedDeliveryDate()){
                    return true;
                }else{
                    message("You are trying to allocate an order to a booking but"
                    + " the order will not arrive on time. \n"
                    + "Please let the customer know that the booking will have to be rescheduled");
                    return false;
                }
            }else if(!validate.checkBookingDate(LocalDatetoToCalendar(datePickerStart.getValue(),TstartTime.localTimeProperty().getValue()),
                    LocalDatetoToCalendar(datePickerStart.getValue(),TendTime.localTimeProperty().getValue()))){  
                message(validate.getError());
                return false;
            } else{
                return true;  
            }            
        } catch (Exception e) {
            return false;
        }        
    }
    
    /**
     * The methods below check delivery date of the parts ordered with the booking date.
     * @return 
     */
    public boolean checkexpectedDeliveryDate(){
        for(int i=0;i<expertedDate.size();i++)
            if(!LocalDatetoToCalendar(datePickerStart.getValue(),TendTime.localTimeProperty().getValue()).after(expertedDate.get(i))){
                return false;
            }
        return true;
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
     * This method is called before opening the edit booking window to check if the booking
     * if booking finished or if its it pass of the booking day.
     */
    public void checkDates() {
        Date today = new Date();
        if (today.after((booking.getEndDate().getTime()))) {
            saveBooking.setDisable(true);
            DeleteBooking.setDisable(true);
            viewParts.setDisable(true);
        }
        
        if(booking.isBookingFinished()){
            saveBooking.setDisable(true);
            DeleteBooking.setDisable(true);
            viewParts.setDisable(true);
            completeBooking.setDisable(true);
        }
        
        if((booking.getEndDate().getTime()).after(today)){
            completeBooking.setDisable(true);
        }
        
    
    }
    
    
    /**
     * The method return boolean value which signals if the booking is deleted or not.
     * @return 
     */
    public boolean isDeleteBookingFromAgenda() {
        return deleteBookingFromAgenda;
    }
}
