package Controllers.Parts;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Alexander_2
 */
 
//THIS CLASS IS CREATED TO FILL IN THE SUPPLIER INTERFACE
public class Supplier {
   
    private final IntegerProperty ID;
    private final StringProperty Name;
    private final StringProperty Description;
    private final StringProperty Address;
    private final StringProperty Phone;
    private final StringProperty Email;
    private final BooleanProperty Delete;

   // private final ObjectProperty<LocalDate> birthday;
   
    public Supplier(int id,String Name, String Description, String Address, String Phone, String Email,Boolean Delete) {
        this.ID=new SimpleIntegerProperty(id);
        this.Name = new SimpleStringProperty(Name);
        this.Description = new SimpleStringProperty(Description);

        // Some initial dummy data, just for convenient testing.
        this.Address = new SimpleStringProperty(Address);
        this.Phone = new SimpleStringProperty(Phone);
        this.Email = new SimpleStringProperty(Email);
        this.Delete=new SimpleBooleanProperty(Delete);
        //this.birthday = new SimpleObjectProperty<LocalDate>(LocalDate.of(1999, 2, 21));
    }
    public void setID(int partName) {
        this.ID.set(partName);
    }
    public void setName(String partName) {
        this.Name.set(partName);
    }
    public void setDescription(String partDesc) {
        this.Description.set(partDesc);
    }
    public void setAddress(String partAddress) {
        this.Address.set(partAddress);
    }
    public void setPhone(String partDesc) {
        this.Phone.set(partDesc);
    }
    public void setEmail(String partDesc) {
        this.Email.set(partDesc);
    }
    public void setDelete(boolean partDesc) {
        this.Delete.set(partDesc);
    }
     public IntegerProperty getIDProperty() {
        return ID;
    } 
    public StringProperty getNameProperty() {
        return Name;
    }
      public StringProperty getDescProperty() {
        return Description;
    }
      public StringProperty getAddressProperty() {
        return Address;
    }
      public StringProperty getPhoneProperty() {
        return Phone;
    } 
      public StringProperty getEmailProperty() {
        return Email;
    }
        public BooleanProperty getDeleteProperty() {
        return Delete;
    }
         public int getID() {
        return ID.get();
    }
        public String getName() {
        return Name.get();
    }
       public String getDesc() {
        return Description.get();
    }
        public String getAddress() {
        return Address.get();
    }
       public String getPhone() {
        return Phone.get();
    }
        public String getEmail() {
        return Email.get();
    }
         public boolean getDelete() {
        return Delete.get();
    }
}
