package Controllers.Customer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VehiclesOwned {
    
    private StringProperty name;
    private StringProperty make;
    private StringProperty model;
    
    public VehiclesOwned(String nameIn, String makeIn, String modelIn) {
        this.name = new SimpleStringProperty(nameIn);
        this.make = new SimpleStringProperty(makeIn);
        this.model = new SimpleStringProperty(modelIn);
    }

    public String getName() {
        return this.name.get();
    }

    public String getMake() {
        return this.make.get();
    }

    public String getModel() {
        return this.model.get();
    }
}
