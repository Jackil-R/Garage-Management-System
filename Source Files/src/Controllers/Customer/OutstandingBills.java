package Controllers.Customer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OutstandingBills {
    
    private StringProperty id;
    private StringProperty date;
    private StringProperty time;
    private StringProperty amount;
    
   public OutstandingBills(String billID, String date, String time, String amount) {
       this.id = new SimpleStringProperty(billID);
       this.date = new SimpleStringProperty(date);
       this.time = new SimpleStringProperty(time);
       this.amount = new SimpleStringProperty(amount);
   }

    public String getId() {
        return id.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getTime() {
        return time.get();
    }

    public String getAmount() {
        return amount.get();
    }
}
