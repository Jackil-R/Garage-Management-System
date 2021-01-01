/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.ScheduledMaintenanceBookings;

import jfxtras.scene.control.agenda.Agenda.AppointmentImpl;

/**
 *
 * @author Julians
 */
public class BookingAppointment extends AppointmentImpl {
    
    private Booking booking;

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
    
    public BookingAppointment(Booking booking){
        super();
        this.booking = booking;
        this.withSummary(this.createSummary(booking));
        this.withDescription("TEST");

    }

    public String createSummary(Booking booking){
    
        String summary;
        if (booking.getVehicle().getCustomer().getTypeOfCustomer() == 1)
            summary = booking.getVehicle().getCustomer().getCompanyName();
        else
        summary = booking.getVehicle().getCustomer().getFirstName()+" "+booking.getVehicle().getCustomer().getLastName();
        summary += " with "+booking.getVehicle().getMake()+" "+booking.getVehicle().getModel();
        return summary;
    }
    
    BookingAppointment() {
     super();

    this.withSummary("EMPTY BOOKING");
    this.withDescription("BookingAppointment does not have Booking set or Booking is empty.");
    
    }
    
}
