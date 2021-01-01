/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.DiagnosisRepairBookings;
import jfxtras.scene.control.agenda.Agenda.AppointmentImpl;

/**
 * @author jackilrajnicant
 */
public class BookingAppointment extends AppointmentImpl {
    
    private DRBooking booking;

    public DRBooking getBooking() {
        return booking;
    }

    public void setBooking(DRBooking booking) {
        this.booking = booking;
    }
    
    public BookingAppointment(DRBooking booking){
        super();
        this.booking = booking;
        this.withSummary(this.createSummary(booking));
        this.withDescription("TEST");
    
    //this.withAppointmentGroup(lAppointmentGroupMap.get("Bay "+booking.getBayNumber()));
    }

    public String createSummary(DRBooking booking){
    
       
        String summary;
        if (booking.getVehicle().getCustomer().getTypeID() == 1)
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


