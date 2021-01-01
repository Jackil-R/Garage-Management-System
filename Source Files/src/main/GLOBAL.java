package main;

import Controllers.DiagnosisRepairBookings.FXMLDocumentController;
import Controllers.Parts.PartsController;
import Controllers.ScheduledMaintenanceBookings.MainFXMLController;
import Controllers.StartPage.StartPageController;

/**
 *
 * @author Julians
 */
public class GLOBAL {
    private static int mechanicID;
    private static String mechName;

    public static String getMechName() {
        return mechName;
    }

    public static void setMechName(String mechName) {
        GLOBAL.mechName = mechName;
    }
    private static boolean authenticated = false;
    
    public static MainFXMLController ScheduledPointer;
    public static PartsController PartsPointer;
    public static FXMLDocumentController DiagnosisPointer;
    public static StartPageController StartPagePointer;


    public static boolean isAuthenticated() {
        return authenticated;
    }

    public static void setAuthenticated(boolean authenticated) {
        GLOBAL.authenticated = authenticated;
    }

    public static int getMechanicID() {
        return mechanicID;
    }

    public static void setMechanicID(int mechanicID) {
        GLOBAL.mechanicID = mechanicID;
    }
    
    
}
