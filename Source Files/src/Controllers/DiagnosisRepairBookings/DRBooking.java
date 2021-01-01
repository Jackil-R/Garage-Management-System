/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.DiagnosisRepairBookings;
import Controllers.Parts.BasketPart;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javafx.collections.ObservableList;
/**
 * @author jackilrajnicant
 */
public class DRBooking {
   
    private int id;
    private GregorianCalendar startDate;
    private GregorianCalendar endDate;
    private Vehicle vehicle;
    private int bayNumber;
    private int bookingType;
    private double bookingFee;
    private Mechanic mechanic;
    private String fault;
    private ObservableList<BasketPart> partList;
    private boolean bookingFinished;
    
    
    public DRBooking(){
      
    }
    
    public int getID(){
        return id;
    }
    
    public void setID(int ID){
        this.id=ID;
    }
    
    public String getFault() {
        return fault;
    }

    public void setFault(String fault) {
        this.fault = fault;
    }

    public ObservableList<BasketPart> getPartList() {
        return partList;
    }

    public void setPartList(ObservableList<BasketPart> partList) {
        this.partList = partList;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }
    
    public double getBookingFee() {
        return bookingFee;
    }

    public void setBookingFee(double fee) {
        bookingFee=fee;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public int getBayNumber() {
        return bayNumber;
    }

    public void setBayNumber(int bayNumber) {
        this.bayNumber = bayNumber;
    }

    public GregorianCalendar getStartDate() {
        return startDate;
    }

    public void setStartDate(GregorianCalendar startDate) {
        this.startDate = startDate;
    }

    public GregorianCalendar getEndDate() {
        return endDate;
    }

    public void setEndDate(GregorianCalendar endDate) {
        this.endDate = endDate;
    }

    public int getBookingType() {
        return bookingType;
    }

    public void setBookingType(int bookingType) {
        this.bookingType = bookingType;
    }

    public boolean isBookingFinished() {
        return bookingFinished;
    }

    public void setBookingFinished(boolean bookingFinished) {
        this.bookingFinished = bookingFinished;
    }

}
