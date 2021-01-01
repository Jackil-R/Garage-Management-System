/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.VehicleRecords;

import java.time.LocalDate;
import java.time.LocalTime;
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
public class parts {
    
   StringProperty  vid;
   IntegerProperty  bid;
   StringProperty  pid;
   StringProperty  date;
    
   public parts(String a, int b, String c, String d) {
       
       this.vid = new SimpleStringProperty(a);
       this.bid= new SimpleIntegerProperty(b);
       this.pid= new SimpleStringProperty(c);
       this.date= new SimpleStringProperty(d);
   }
   
   public String getVid() {
            return this.vid.get();
        }
  
   public int getBid() {
            return this.bid.get();
        }
   
   public String getPid() {
            return this.pid.get();
        }
   
   public String getDate() {
            return this.date.get();
        }
   
   
}
