/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.VehicleRecords;

import javafx.scene.control.TextField;

/**
 *
 * @author ibraaheem
 */
public class NumberTextField extends TextField {
    
    public NumberTextField() {
        
    }
    
    @Override 
    public void replaceText(int i, int il, String string) {
        
        if(string.matches("[0-9]") || string.isEmpty()) {
            
            super.replaceText(i,il,string);
        }
            
    }
    
}
