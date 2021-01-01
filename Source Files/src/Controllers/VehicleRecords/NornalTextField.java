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
public class NornalTextField extends TextField {
    
    public NornalTextField() {
        
    }
    
    @Override 
    public void replaceText(int i, int il, String string) {
        
        if((!string.matches("[\\\\!\"#$%&()*+,./:;<=>?@\\[\\]^_{|}~]+")) || (string.isEmpty())) {
            
            super.replaceText(i,il,string);
        }
            
    }
    
}

