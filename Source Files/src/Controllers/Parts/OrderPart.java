package Controllers.Parts;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Alexander_2
 */

//THIS CLASS IS CREATED TO FILL IN THE PENDING DELIVERIES PARTS AND ADDING THEM TO DIAGNOSIS AND REPAIRS
public class OrderPart extends Part {
private final StringProperty supplier;
private final StringProperty date;

    public OrderPart(int Diez,String Name, String Description, int LeftInstock, double Price, Boolean Order,String type,String Supplier,String Date) {
        super(Diez,Name, Description, LeftInstock, Price, Order,type);
       
        this.supplier=new SimpleStringProperty(Supplier);
        this.date=new SimpleStringProperty(Date);
    }
    public void setDate(String input) {
        this.date.set(input);
    }
    public void setSupplier(String input) {
        this.supplier.set(input);
    }
    public String getDate() {
        return date.get();
    }
    public String getSupplier() {
        return supplier.get();
    }
    public StringProperty getDateProperty() {
        return date;
    }
    public StringProperty getSupplierPropery() {
        return supplier;
    }
   
    
}
