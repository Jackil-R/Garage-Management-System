/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.DiagnosisRepairBookings;
import Controllers.Customer.SearchModuleController;
import Controllers.Parts.BasketPart;
import Controllers.Parts.OrderPart;
import Controllers.Parts.PartsControllerRepair;
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
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import jfxtras.scene.control.LocalTimePicker;
import jfxtras.scene.control.agenda.Agenda;
import org.controlsfx.dialog.Dialogs;

/**
 * FXML Controller class
 *
 * @author jackilrajnicant
 */
public class CreateBookingController implements Initializable {
     
    private Stage prevStage;
    private Calendar startCalendarRange;
    private Calendar endCalendarRange;
    private BookingAppointment appointment;
    private Map<String, Agenda.AppointmentGroup> lAppointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
    private DatabaseHandler databaseHandler = new DatabaseHandler();
    private DRBooking booking;
    private Vehicle vehicle;
    private CustomerAccount customer;
    private ArrayList<Mechanic> allMechanics;
    private ObservableList<BasketPart> parts;
    private ObservableList<OrderPart> partsOrdered;
    private double partTotal;
    private double totalCost;
    private ArrayList <Calendar> expertedDate = new ArrayList<Calendar>();    
    
    //Booking Components
        //TextField
        @FXML private TextField TbookingType = new TextField();
        @FXML private TextField TcurrentMileage = new TextField();
        @FXML private TextField TbookingFee = new TextField();
        @FXML private TextField showCustomer = new TextField();
        @FXML private TextField TcustomerID = new TextField();
        @FXML private TextField vehicleID = new TextField();
        @FXML private TextField vehicleReg = new TextField();
        //ComboBox and Dates
        @FXML private LocalTimePicker TstartTime = new LocalTimePicker();
        @FXML private LocalTimePicker TendTime = new LocalTimePicker();
        @FXML private ComboBox TbayNumber = new ComboBox();
        @FXML private ComboBox Tmechanics = new ComboBox();
        @FXML private ObservableList<String> optionsMech = FXCollections.observableArrayList();
        @FXML private DatePicker datePickerStart = new DatePicker();
   
        //TextArea
        @FXML private TextArea Tfault = new TextArea();
        @FXML private TextArea Tparts = new TextArea();
    
    //AlertBoxes
    @FXML Alert alertUpdate = new Alert(Alert.AlertType.ERROR);
    @FXML Alert warningUpdate = new Alert(Alert.AlertType.WARNING);
    @FXML Alert bookingFeeInformation = new Alert(Alert.AlertType.WARNING);
    
    //Buttons Components
    @FXML private Button closeWindow = new Button();
    @FXML private Button createBooking = new Button();
    @FXML private Button viewParts = new Button();
    @FXML private Button selectCustomer = new Button();
    
    private boolean orderedFromSupplier;

    /**
     * The method below is a setter for Scene.
     * @param scene 
     */
    public void setStage(Stage stage){
        prevStage = stage;
    }
    
    /**
     * The method below get the current appointment object.
     * @return 
     */
    public BookingAppointment getCurrentAppointment() {
        return appointment;
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
     * The methods basically closes the current window
     * It also check the Database connection is closed or not.
     * @throws IOException 
     */
    public void closeStage() throws IOException{
        try {
            if(!databaseHandler.connection.isClosed())
                databaseHandler.connection.close();
        } catch (SQLException ex) {
            System.out.println("DRBooking CreateBooking Class, "+ex);
        }
        Stage stage = (Stage)closeWindow.getScene().getWindow();
        stage.close();
    }
    
    /**
     * This method basically checks the whole form which the user fill in
     * and checks null value which will prevent from NullPointException
     * @return 
     */
    public boolean validateForm(){
        Validator validate = new Validator();
        try {
            if(customer==null){
                customerNotSelected();
                return false;
            }else if(vehicle==null){ 
                vehicleNotSelected();
                return false;
            }else if (TstartTime.localTimeProperty() == null) {
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
                nullError("Start End");
                return false;
            }else if (TcurrentMileage.getText() == null || TcurrentMileage.getText().equals("")) {
                nullError("Mileage");
                return false;
            }else if(orderedFromSupplier){
                if(checkexpectedDeliveryDate()){
                    return true;
                }else{
                    Dialogs.create()
                             .owner(prevStage)
                             .title("Booking Date Error!")
                             .masthead(null)
                             .message("You are trying to allocate an order to a booking but"
                    + " the order will not arrive on time. \n"
                    + "Please let the customer know that the booking will have to be rescheduled")
                             .showInformation();
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
     * The addBooking methods displays the total cost for the booking
     * It also add the booking to agenda and calls a method databaseHandler which
     * stored the booking in the database.-
     * @throws IOException 
     */
    public void addBooking() throws IOException{ 
        if(!validateForm()) return;

        if(vehicle.getWarranty()!=0){
             totalCost=0;
        }else{
            totalCost=calculateBookingFee(LocalDatetoToCalendar(datePickerStart.getValue(),TstartTime.localTimeProperty().getValue()), 
                LocalDatetoToCalendar(datePickerStart.getValue(),TendTime.localTimeProperty().getValue()),
                databaseHandler.getMechanicByName(Tmechanics.getSelectionModel().getSelectedItem().toString()));
        }
        
        bookingFeeInformation.setTitle("Booking Fee");
        bookingFeeInformation.setHeaderText(null);
        bookingFeeInformation.setContentText("Booking Cost for this Repair Booking is: "+totalCost);  
        ButtonType buttonNo = new ButtonType("NO",ButtonBar.ButtonData.NO);
        ButtonType buttonYes = new ButtonType("YES",ButtonBar.ButtonData.YES);
        bookingFeeInformation.getButtonTypes().setAll(buttonNo, buttonYes);
        Optional<ButtonType> result = bookingFeeInformation.showAndWait();
     
        if (result.get() == buttonYes) {
            booking = createBookingObject();
            databaseHandler.addBooking(booking);
            booking.setID(databaseHandler.getLastBookingID());
            appointment = createAppointment(booking);
            closeStage();
        } else {
            //Change customer not happy with the cost
        }  
    }
    
    /**
     * The method set is called when a appointment is created and 
     * set the start time and end time of the booking.
     * @param startRange
     * @param endRange 
     */
    public void setDate(Calendar startRange,Calendar endRange){
        startCalendarRange = startRange;
        endCalendarRange = endRange;
        TstartTime.localTimeProperty().set(LocalTime.of(startCalendarRange.getTime().getHours(), startCalendarRange.getTime().getMinutes()));
        TendTime.localTimeProperty().set(LocalTime.of(endCalendarRange.getTime().getHours(), endCalendarRange.getTime().getMinutes()));  
        Instant instant = startRange.toInstant();
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        LocalDate date = zdt.toLocalDate();
        datePickerStart.setValue(date);
        getBayNumber();
    }
    
    /**
     * This method create object of booking and sets all the attributes
     * and return its.
     * @return 
     */
    public DRBooking createBookingObject(){
        booking.setVehicle(vehicle);
        booking.getVehicle().setCustomer(customer);
        booking.setBookingType(3);
        booking.setStartDate(LocalDatetoToCalendar(datePickerStart.getValue(),TstartTime.localTimeProperty().getValue()));
        booking.setEndDate(LocalDatetoToCalendar(datePickerStart.getValue(),TendTime.localTimeProperty().getValue()));
        booking.setMechanic(databaseHandler.getMechanicByName(Tmechanics.getSelectionModel().getSelectedItem().toString()));
        booking.setBayNumber(Integer.parseInt(TbayNumber.getSelectionModel().getSelectedItem().toString()));
        booking.setFault(Tfault.getText());
        booking.getVehicle().setMileage(Integer.parseInt(TcurrentMileage.getText()));
        booking.setPartList(parts);
        booking.setBookingFinished(false);
       
        if(vehicle.getWarranty()!=0){
            booking.setBookingFee(0);
        }else{
            booking.setBookingFee(calculateBookingFee(LocalDatetoToCalendar(datePickerStart.getValue(),TstartTime.localTimeProperty().getValue()), 
                LocalDatetoToCalendar(datePickerStart.getValue(),TendTime.localTimeProperty().getValue()),
                databaseHandler.getMechanicByName(Tmechanics.getSelectionModel().getSelectedItem().toString())));
        }
        
        return booking;
    }

    /**
     * This method create object of BookingAppointment and sets all the attributes
     * and return its.
     * @param booking
     * @return 
     */
    public BookingAppointment createAppointment(DRBooking booking){
               
        try{
            BookingAppointment ba = new BookingAppointment();
            ba.setBooking(booking);
            ba.withStartTime(booking.getStartDate());
            ba.withEndTime(booking.getEndDate());
            ba.withSummary(ba.createSummary(booking));
            ba.withAppointmentGroup(lAppointmentGroupMap.get("Bay "+booking.getBayNumber()));
            return ba;
        }catch(NullPointerException e){
            return null;
        }
    }
  
    /**
     * The methods calculateBookingFee return the fee for booking working with 
     * the start and end dates and mechanics rate.
     * @param st
     * @param et
     * @param m
     * @return 
     */
    public double calculateBookingFee(GregorianCalendar st, GregorianCalendar et, Mechanic m){
        int hour = et.getTime().getHours()-st.getTime().getHours();
        double workedPerHour = hour*m.getRate();
        double partSum=0;
        if(partTotal==0){
            return workedPerHour;
        }else{
            return partSum+workedPerHour+partTotal;
        }
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
    
    public void numberFormatError(){
        alertUpdate.setTitle("Error!");
        alertUpdate.setHeaderText(null);
        alertUpdate.setContentText("Please input a numeric value.");
        alertUpdate.showAndWait();
    }
    
    /**
     * The methods customerNotSelected display dialog box if the customer is not selected.
     */
    public void customerNotSelected(){
        warningUpdate.setTitle("Warning");
        warningUpdate.setHeaderText(null);
        warningUpdate.setContentText("Please Select Customer!");  
        warningUpdate.showAndWait();
    }
    
    /**
     * The methods vehicleNotSelected display dialog box if the vehicle is not selected.
     */
    public void vehicleNotSelected(){
        warningUpdate.setTitle("Warning");
        warningUpdate.setHeaderText(null);
        warningUpdate.setContentText("Please Select Vehicle!");  
        warningUpdate.showAndWait();
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
     * The method below convert a Calendar object to LocalDate object.
     * @param date
     * @return 
     */
    public LocalDate CalendartoLocalDate(Calendar date){
        LocalDate lDate = LocalDate.of(date.getTime().getYear(),date.getTime().getMonth(),date.getTime().getDay());
        return lDate;
    }
    
    /**
     * The method below convert a calendar object to LocalDate object.
     * @param date
     * @param time
     * @return 
     */
    public GregorianCalendar LocalDatetoToCalendar(LocalDate date, LocalTime time){ 
        return new GregorianCalendar(date.getYear(),date.getMonthValue()-1,date.getDayOfMonth(),time.getHour(),time.getMinute());
    }
    
    
    public GregorianCalendar parseToDate(LocalDate date, LocalTime time) {
        return new GregorianCalendar(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), time.getHour(), time.getMinute());
    }
    
    /**
     * The method below convert a calendar object to string object for getting the time.
     * @param date
     * @return 
     */
    public String CalendartoTime(Calendar date){
        String time = "";
        if (date.getTime().getHours() < 10){time = "0";}
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
     * When the button selectCustomer is clicked, it called this method
     * which opened up dialog box where the user can select the customer.
     * @throws IOException 
     */
    public void getCustomer() throws IOException, SQLException, ParseException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Customer/SearchModule.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        SearchModuleController controller = loader.<SearchModuleController>getController();
        stage.showAndWait();
        int found = controller.getSelectedID();
        if (found == -1 || found ==0) {
            customer=null;
        } else {
            Controllers.Customer.CustomerAccount ca = controller.getCustomerAccountFromSearch();
            CustomerAccount myCA = databaseHandler.getCustomerByID(found);
            if(myCA.getTypeID()==1){
                showCustomer.setText(myCA.getCompanyName());
            }else{
                showCustomer.setText(myCA.getFirstName() + " " + myCA.getLastName());
            }
            TcustomerID.setText(Integer.toString(myCA.getCustomerID()));
            customer=myCA;
            getVehicle();
            stage.close();
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
     * initialize is default method and this is where all the EventHandler, ActionListener and all the type
     * of listeners are initialize.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        TstartTime.localTimeProperty().set(LocalTime.of(0,0));
        TendTime.localTimeProperty().set(LocalTime.of(0,0));
        
        booking = new DRBooking();
        allMechanics=databaseHandler.getAllMechanics();
        for(Mechanic m : allMechanics){
            optionsMech.add(m.getFirstName()+" "+m.getLastName()); 
        }
        Tmechanics.setItems(optionsMech);
       
       
        selectCustomer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
//                    Random rand = new Random();
//                    vehicle = databaseHandler.getVehicleByID(rand.nextInt(6)+1);
//                    vehicle.setCustomer(databaseHandler.getCustomerByID(vehicle.getCustomer().getCustomerID()));
//                    booking.setVehicle(vehicle);
//                    displayCustomerVehicleDetails(booking);
                    getCustomer();
                } catch (IOException ex) {
                    Logger.getLogger(CreateBookingController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(CreateBookingController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(CreateBookingController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
       
       viewParts.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    getParts();
                } catch (IOException ex) {
                    Logger.getLogger(CreateBookingController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(CreateBookingController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
       
        datePickerStart.valueProperty().addListener(datePickerListener);
        TcurrentMileage.addEventFilter(KeyEvent.KEY_TYPED, numeric_Validation(10));
        
    }
    
    /**
     * When the button selectVehicle is clicked, it called this method
     * which opened up dialog box where the user can select the vehicle for
     * that particular customer.
     * @throws IOException
     * @throws SQLException
     * @throws ParseException 
     */
    public void getVehicle() throws IOException, SQLException, ParseException{
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/VehicleRecords/details.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene((Pane) loader.load()));
            DetailsController controller = loader.<DetailsController>getController();
            controller.tablecustomer(Integer.toString(customer.getCustomerID()));
            stage.showAndWait();
            int id = controller.getID();
            if (id == 0) {
                vehicle = null;
            } else{
                vehicle = databaseHandler.getVehicleByID(id);
                vehicleID.setText(Integer.toString(vehicle.getID()));
                vehicleReg.setText(vehicle.getRegistration());
            }
        }catch(NullPointerException e){
            customerNotSelected();
        }
    }
    
    /**
     * When the button viewPart is clicked, it called this method
     * which opened up dialog box where the user can select the parts 
     * or order parts.
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
        partsOrdered = controller.getExpectedPart();
        Tparts.setText(getPartName(parts));
       
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
     * The method below is handle which prevent the users from entering character but can only 
     * numeric value.
     * @param max_Length
     * @return 
     */
    public Calendar StringtoCalendar(String dateStr) throws ParseException {
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObj = curFormater.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateObj);
        return calendar;
    }
    
    public EventHandler<KeyEvent> numeric_Validation(final Integer max_Lengh) {
        return new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                TextField txt_TextField = (TextField) e.getSource();
                if (txt_TextField.getText().length() >= max_Lengh) {
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
    
    public void displayCustomerVehicleDetails(DRBooking booking){
        Vehicle vehicle1 = booking.getVehicle();
        CustomerAccount customer = vehicle1.getCustomer();
        showCustomer.setText(customer.getFirstName()+" "+customer.getLastName());
        TcustomerID.setText(Integer.toString(customer.getCustomerID()));
        vehicleID.setText(Integer.toString(vehicle1.getID()));
        vehicleReg.setText(vehicle1.getRegistration());
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
                
                bays.removeAll(takenBays);
                
                TbayNumber.getItems().addAll(bays.toArray());
            } catch (ParseException ex) {
                Logger.getLogger(CreateBookingController.class.getName()).log(Level.SEVERE, null, ex);
            }
            TbayNumber.getSelectionModel().select(0);
        } catch (Exception e) {
            Dialogs.create()
                    .owner(prevStage)
                    .title("Error!")
                    .masthead(null)
                    .message("Error Creating Booking, Please re-do the booking.")
                    .showInformation();
        }
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

}
