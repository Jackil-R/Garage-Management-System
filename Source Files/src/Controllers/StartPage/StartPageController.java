package Controllers.StartPage;

import Controllers.ScheduledMaintenanceBookings.AuthFXMLController;
import Controllers.ScheduledMaintenanceBookings.CreateBookingFXMLController;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.GLOBAL;
import org.controlsfx.dialog.Dialogs;

public class StartPageController implements Initializable {

    @FXML
    private TabPane tabPane;
    
    @FXML
    private Menu labelMech;
    public MenuItem miLock;
    public MenuItem miExit;
    public MenuItem miCustomerManual;
    public MenuItem miVehicleManual;
    public MenuItem miDiagnosisManual;
    public MenuItem miScheduledManual;
    public MenuItem miPartsManual;
    public MenuItem miAbout;
    
    public void openPDF(String name){
    
        if (Desktop.isDesktopSupported()) {
    try {
        File myFile = new File("resources/"+name+".pdf");
        Desktop.getDesktop().open(myFile);
    } catch (IOException ex) {
        // no application registered for PDFs
    }
}
        
    }
    
    public void disableTab(){
    tabPane.setDisable(true);
        
        GaussianBlur gb = new GaussianBlur();
        gb.setRadius(0);
        tabPane.setEffect(gb);
        
    }
    
    public void enableTab(){
    tabPane.setDisable(false);     
    tabPane.setEffect(null);
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GLOBAL.StartPagePointer = this;
              
        labelMech.setText(labelMech.getText()+GLOBAL.getMechName());
        
        
        
        
        miAbout.setOnAction(new EventHandler<ActionEvent>() {
    public void handle(ActionEvent e) {
        Dialogs.create()
                             .owner(tabPane)
                             .title("About")
                             .masthead("Authors: Julian Mukaj, Joshua Myner, Ibraheem Fazal, Alex Nesteruk and Jackil Rajnicant")
                             .message("This is Group 2D's implementation of the Garage Management System. No need to tell us it's the best, we already know. "
                                     + "Please view our manuals if you aren't sure how to do a specific task. ")
                             .showInformation();
    }
});
        
        miCustomerManual.setOnAction(new EventHandler<ActionEvent>() {
    public void handle(ActionEvent e) {
        openPDF("customer manual");
    }
});
        miVehicleManual.setOnAction(new EventHandler<ActionEvent>() {
    public void handle(ActionEvent e) {
        openPDF("VehicleRecordsManual");
    }
});
        miDiagnosisManual.setOnAction(new EventHandler<ActionEvent>() {
    public void handle(ActionEvent e) {
        openPDF("Diagnosis and Repair Booking Manuals");
    }
});
        miScheduledManual.setOnAction(new EventHandler<ActionEvent>() {
    public void handle(ActionEvent e) {
        openPDF("Scheduled Manual");
    }
});
        miPartsManual.setOnAction(new EventHandler<ActionEvent>() {
    public void handle(ActionEvent e) {
        openPDF("Parts_Manual_Final");
    }
});
        
        miLock.setOnAction(new EventHandler<ActionEvent>() {
    public void handle(ActionEvent e) {
    
        tabPane.setDisable(true);
        
        GaussianBlur gb = new GaussianBlur();
        gb.setRadius(0);
        tabPane.setEffect(gb);
        
         new Thread(new Runnable() {
    @Override public void run() {
        for (int i=1; i <= 100000; i++) {
            final int counter = i;
            Platform.runLater(new Runnable() {
                @Override public void run() {
                   gb.setRadius(counter/5000);
                  
                }
            });
        }
    }
}).start();
         
        if (getAuth(GLOBAL.getMechanicID()))
        {
            tabPane.setDisable(false);
            tabPane.setEffect(null);
        }
    }
});
        
        miExit.setOnAction(new EventHandler<ActionEvent>() {
    public void handle(ActionEvent e) {
        System.exit(1);
    }
});
        
        
        
        tabPane.getSelectionModel().selectedItemProperty().addListener(
    new ChangeListener<Tab>() {
        @Override
        public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {
            if (newTab.getText().equals("Scheduled Maintenance Bookings"))
                GLOBAL.ScheduledPointer.refreshAgenda();
            
            if (newTab.getText().equals("Parts"))
                GLOBAL.PartsPointer.refreshStock();
            
            if (newTab.getText().equals("Diagnosis and Repair Bookings"))
                try {
                    GLOBAL.DiagnosisPointer.display();
            } catch (ParseException ex) {
                Logger.getLogger(StartPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }                              
        );
    }    
    
    
    public boolean getAuth(int id){
    FXMLLoader loader = new FXMLLoader(
    getClass().getResource(
      "/FXML/ScheduledMaintenanceBookings/AuthFXML.fxml"
    )
  );

  Stage stage = new Stage();
        try {
            stage.setScene(
                    new Scene(
                            (Pane) loader.load()
                    )
            );    } catch (IOException ex) {
            Logger.getLogger(CreateBookingFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }

  AuthFXMLController controller = 
    loader.<AuthFXMLController>getController();
    controller.setOtherMechID(id);
    stage.setResizable(false);
    stage.initStyle(StageStyle.UNDECORATED);
    stage.setAlwaysOnTop(true);
    stage.showAndWait();

  return controller.getResult();
    }
    
}
