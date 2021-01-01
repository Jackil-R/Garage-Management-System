/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.VehicleRecords;

import java.time.LocalDate;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author ibraaheem
 */
public class warranty {
    
   IntegerProperty  wid;
   StringProperty name;
   StringProperty  address; 
   StringProperty  expire;
   
   public warranty (int a, String b, String c, String d) {
       
       this.wid = new SimpleIntegerProperty(a);
       this.name= new SimpleStringProperty(b);
       this.address= new SimpleStringProperty(c);
       this.expire= new SimpleStringProperty(d);
   
   }
   
   public int getWid() {
            return this.wid.get();
        }
   
   public String getName() {
            return this.name.get();
        }
   
   public String getAddress() {
            return this.address.get();
        }
   
   public String getExpire() {
            return this.expire.get();
        }
   
}

