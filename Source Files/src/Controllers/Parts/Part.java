package Controllers.Parts;
import java.time.LocalDate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sun.java2d.pipe.SpanShapeRenderer;

/**
 *
 * @author Alexander_2
 */
 
//GENERAL OBJECT FOR A PART TO BE ADDED TO A MAIN STOCK
public class Part {
   
    private final IntegerProperty id;
    private final StringProperty Name;
    private final StringProperty Description;
    private final IntegerProperty LeftInStock;
    private final DoubleProperty Price;
    private final SimpleBooleanProperty Order;
    private final StringProperty Type;
   // private final ObjectProperty<LocalDate> birthday;
   
    public Part(int Diez,String Name, String Description, int LeftInstock, double Price, Boolean Order, String typie) {
        this.id=new SimpleIntegerProperty(Diez);
        this.Name = new SimpleStringProperty(Name);
        this.Description = new SimpleStringProperty(Description);

        // Some initial dummy data, just for convenient testing.
        this.LeftInStock = new SimpleIntegerProperty(LeftInstock);
        this.Price = new SimpleDoubleProperty(Price);
        this.Order = new SimpleBooleanProperty(Order);
        this.Type=new SimpleStringProperty(typie);
        //this.birthday = new SimpleObjectProperty<LocalDate>(LocalDate.of(1999, 2, 21));
    }
   public void setType(String t) {
        this.Type.set(t);
    }
    public void setId(int ID) {
        this.id.set(ID);
    }
    public void setName(String partName) {
        this.Name.set(partName);
    }
    public void setDescription(String partDesc) {
        this.Description.set(partDesc);
    }
    public void setQuantaty(int partNum) {
        this.LeftInStock.set(partNum);
    }
    public void setPrice(double partPrice) {
        this.Price.set(partPrice);
    }
    public void setOrdered(Boolean partOrder) {
        this.Order.set(partOrder);
    }
     public StringProperty getNameProperty() {
        return Name;
    }
      public StringProperty getTypeProperty() {
        return Type;
    }
      public StringProperty getDescProperty() {
        return Description;
    }
      public IntegerProperty getIdProperty() {
        return id;
    } 
      public IntegerProperty getQuantatyProperty() {
        return LeftInStock;
    }
        public DoubleProperty getPricePropery() {
        return Price;
    }
         public SimpleBooleanProperty getOrderProperty() {
        return Order;
    }
         public String getName() {
        return Name.get();
    }
         public String getType() {
        return Type.get();
    }
       public String getDesc() {
        return Description.get();
    }
       public int getId() {
        return id.get();
    }
       public int getQuantaty() {
        return LeftInStock.get();
    }
       public double getPrice() {
        return Price.get();
    }
       public boolean getOrder() {
        return Order.get();
    }
}
