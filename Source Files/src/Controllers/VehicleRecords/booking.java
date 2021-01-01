/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.VehicleRecords;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author ibraaheem
 */
public class booking {
    
   IntegerProperty  bid;
   StringProperty  type;
   StringProperty  ts;
   StringProperty  te;
   IntegerProperty  bn;
   StringProperty  mid;
   IntegerProperty bill;
    
    public booking(int a, String b, String d, String e, int f, String g, int h) {
        this.bid = new SimpleIntegerProperty(a);
        this.type= new SimpleStringProperty(b);
        this.ts= new SimpleStringProperty(d);
        this.te= new SimpleStringProperty(e);
        this.bn= new SimpleIntegerProperty(f);
        this.mid= new SimpleStringProperty(g); 
        this.bill= new SimpleIntegerProperty(h); 
    }
    
    public int getBid() {
            return this.bid.get();
        }
    
    public String getType() {
            return this.type.get();
        }
     
    public String getTs() {
            return this.ts.get();
        }
    
    public String getTe() {
            return this.te.get();
        }
    public int getBn() {
            return this.bn.get();
        }
    public String getMid() {
            return this.mid.get();
        }
    
    public int getBill() {
            return this.bill.get();
        }

    void bidProperty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
      
}