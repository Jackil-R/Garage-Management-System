/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.ScheduledMaintenanceBookings;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 *
 * @author Julians
 */
public class Booking {

    public void setID(int ID) {
        this.ID = ID;
    }
    
    private int ID;
    private GregorianCalendar startDate;
    private GregorianCalendar endDate;
   // private CustomerAccount customer;
    private Vehicle vehicle;
    private int bayNumber;
    private int bookingType;
    private String bookingTypeName;
    private BigDecimal bookingFee;
    private Mechanic mechanic;

    public void setBookingTypeName(String BookingTypeName) {
        this.bookingTypeName = BookingTypeName;
    }
    private String bookingNotes;
    private ArrayList<Part> partList;

    public Booking(){
    }
    
    public int getID() {
        return ID;
    }
    
    public String getBookingNotes() {
        return bookingNotes;
    }

    public void setBookingNotes(String bookingNotes) {
        this.bookingNotes = bookingNotes;
    }

    public ArrayList<Part> getPartList() {
        return partList;
    }

    public void setPartList(ArrayList<Part> partList) {
        this.partList = partList;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }
    
    public BigDecimal getBookingFee() {
        return bookingFee;
    }

    public void setBookingFee(BigDecimal bookingFee) {
        this.bookingFee = bookingFee;
    }

    public int getBookingType() {
        return bookingType;
    }
    
    public String getBookingTypeName(){
        return bookingTypeName;
    }

    public void setBookingType(int bookingType) {
        this.bookingType = bookingType;
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
     
}
