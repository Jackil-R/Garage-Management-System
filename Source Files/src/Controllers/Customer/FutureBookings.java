package Controllers.Customer;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FutureBookings {
    
    private StringProperty name;
    private IntegerProperty price;
    private StringProperty date;
    private StringProperty timeStart;
    private StringProperty timeEnd;
    
   public FutureBookings(String nameIn, int priceIn, String dateIn, String timeStartIn, String timeEndIn) {
       this.name = new SimpleStringProperty(nameIn);
       this.price = new SimpleIntegerProperty(priceIn);
       this.date = new SimpleStringProperty(dateIn);
       this.timeStart = new SimpleStringProperty(timeStartIn);
       this.timeEnd = new SimpleStringProperty(timeEndIn);
   }

    public String getName() {
        return name.get();
    }

    public Integer getPrice() {
        return price.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getTimeStart() {
        return timeStart.get();
    }

    public String getTimeEnd() {
        return timeEnd.get();
    } 
}
