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
public class vr {
    
   StringProperty  vid;
   IntegerProperty cid;
   StringProperty  ctid; 
   StringProperty mid;
   StringProperty  mdid;
   StringProperty  esid;
   StringProperty  ftid;
   StringProperty  coid;
   StringProperty  mote;
   IntegerProperty  mile;
   StringProperty  lsd;
   IntegerProperty  wid;
   
   public vr (String a, int b, String c, String d, String e, String f, String g, String h, String i, int j, String k, int l) {
       
       this.vid = new SimpleStringProperty(a);
       this.cid= new SimpleIntegerProperty(b);
       this.ctid= new SimpleStringProperty(c);
       this.mid= new SimpleStringProperty(d);
       this.mdid= new SimpleStringProperty(e);
       
       this.esid = new SimpleStringProperty(f);
       this.ftid= new SimpleStringProperty(g);
       this.coid= new SimpleStringProperty(h);
       this.mote= new SimpleStringProperty(i);
       this.mile= new SimpleIntegerProperty(j);
       
       this.lsd= new SimpleStringProperty(k);
       this.wid= new SimpleIntegerProperty(l);
   
   }
   
   public String getVid() {
            return this.vid.get();
        }
   
   public int getCid() {
            return this.cid.get();
        }
   
   public String getCtid() {
            return this.ctid.get();
        }
   
   public String getMdid() {
            return this.mdid.get();
        }
   
   public String getMid() {
            return this.mid.get();
        }
   
   public String getEsid() {
            return this.esid.get();
        }
   
   public String getFtid() {
            return this.ftid.get();
        }
   
   public String getCoid() {
            return this.coid.get();
        }
   
   public String getMote() {
            return this.mote.get();
        }
   
   public int getMile() {
            return this.mile.get();
        }
   
   public String getLsd() {
            return this.lsd.get();
        }
   
   public int getWid() {
            return this.wid.get();
        }
}
