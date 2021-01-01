package Controllers.Parts;

/**
 *
 * @author Alexander_2
 */
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;


public class SupplierListController implements Initializable {
   //fxml variables
    @FXML 
    private TabPane supplierHolder;
    @FXML
    private Tab contAgent;
    @FXML
    private Tab pending;
    @FXML 
    private TextField newSupName;
    @FXML
    private TextField newSupDesc;
    @FXML
    private TextField newSupPhone;
    @FXML
    private TextField newSupE;
    @FXML 
    private TextField newSupAdd;
    @FXML
    private TextField newPenName;
    @FXML
    private ComboBox newPenSup;
    @FXML
    private TextField newPenPrice;
    @FXML
    private TextField newPenAmount;
    @FXML
    private DatePicker newPenDate;
    @FXML
    private Button addSup;
    @FXML
    private Button addPen;
    @FXML
    private Button selectDelete;
    @FXML
    private Button backFromSupplier;
    @FXML
    private final ObservableList<Supplier> supplierTable=FXCollections.observableArrayList();
    @FXML
    private TableView<Supplier> supplierView;
    @FXML
    private TableColumn<Supplier, String> supName;
    @FXML
    private TableColumn<Supplier,String> supDesc;
    @FXML
    private TableColumn<Supplier, String> supAddress;
    @FXML
    private TableColumn<Supplier, String> supPhone;
    @FXML
    private TableColumn<Supplier, String> supEmail;
    @FXML
    private TableColumn<Supplier, Boolean> supDelete;
    //to present pending
   private final ObservableList<OrderPart> pendingTable=FXCollections.observableArrayList();
   
    
    //pending
   @FXML
    private TableView<OrderPart> pendView;
    @FXML
    private TableColumn<OrderPart, String> pendID;
    @FXML
    private TableColumn<OrderPart, String> pendName;
    @FXML
    private TableColumn<OrderPart,String> pendSup;
    @FXML
    private TableColumn<OrderPart,String> pendDate;
    @FXML
    private TableColumn<OrderPart,Integer> pendAmount;
    @FXML
    private TableColumn<OrderPart,Integer> pendPrice;
    @FXML
    private AnchorPane anchor;
    
    //connectors to SupplierRepair
    private String date;
    private PartsControllerRepair repair;
    
    //connector
    
    public void setConnectors(PartsControllerRepair rep)
    {
          repair=rep;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        anchor.setStyle("-fx-background-color: #FFFFFF;");
        
        
        try{Collection<Supplier> readSupplier = MyConnection.readSupplierList();
            supplierTable.addAll(readSupplier);
            
            Collection<OrderPart> pendingColl=MyConnection.readPendingList();
            pendingTable.addAll(pendingColl);
            //check if the deadlines of pending deliveries have been missed
            for(OrderPart o: pendingColl)
            { 
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date deadline = formatter.parse(o.getDate());
                Date today=formatter.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                int compare=deadline.compareTo(today);
                if(compare<=0)
                {
                    int notify=JOptionPane.showConfirmDialog(null,
                        "Deadline for delivery of "+o.getName()+" of "+o.getQuantaty()+" has run out or ends today \n"
                                +"Was the order delivered?",
                        "Notification - Parts deadline hit",
                        JOptionPane.YES_NO_OPTION);
                    if(notify==0){
                    pendingTable.remove(o);
                   MyConnection remover= new MyConnection();
                   remover.removePending(o);
                    }
                }
            }
            //fill up the models
            for (Supplier combo:supplierTable)
        {
            newPenSup.getItems().add(combo.getName());
        }
            newPenSup.getSelectionModel().select(0);
        }catch(SQLException w){System.out.println("Connection failure");} catch (ClassNotFoundException ex) {
            System.out.println("Unknown failure");
        } catch (ParseException ex) {
            Logger.getLogger(SupplierListController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //set up the columns
        supDelete.setCellValueFactory(cellData -> cellData.getValue().getDeleteProperty());
        supDelete.setCellFactory(new Callback<TableColumn<Supplier, Boolean>, TableCell<Supplier, Boolean>>() {
                public TableCell<Supplier, Boolean> call(TableColumn<Supplier, Boolean> p) {
                CheckBoxTableCell<Supplier, Boolean> cell=new CheckBoxTableCell<Supplier, Boolean>();
                BooleanProperty selected= new SimpleBooleanProperty();
                cell.setSelectedStateCallback(new Callback<Integer, ObservableValue<Boolean>>() {
                                @Override
                                public ObservableValue<Boolean> call(Integer index) {
                                    return selected ;
                                }
                            });
                            selected.addListener(new ChangeListener<Boolean>() {
                                @Override
                                public void changed(ObservableValue<? extends Boolean> obs, Boolean wasSelected, Boolean isSelected) {
                                     //System.out.println(isSelected);
                                    int chosen=cell.getTableRow().getIndex();
                                    supplierTable.get(chosen).setDelete(true);
                                    
                                }
                            });             
                                            return cell;
                                        }
                                    });
        supName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        
        supDesc.setCellValueFactory(cellData -> cellData.getValue().getDescProperty());
        supDesc.setCellFactory(new Callback<TableColumn<Supplier,String>, TableCell<Supplier,String>>() {

            @Override
            public TableCell<Supplier, String> call(
                    TableColumn<Supplier, String> param)
            {
                TextFieldTableCell<Supplier, String>myEditableTableCell = new TextFieldTableCell<Supplier, String>(new DefaultStringConverter())
                {
                    @Override
                    public void commitEdit(String val) 
                    {
                        int index = this.getTableRow().getIndex();
                        Supplier newUser = this.getTableView().getItems().get(index);
                        StringProperty oldval = newUser.getDescProperty();
                        supplierTable.get(index).setDescription(val);
                        this.setItem(val);
                    }
               };
               
                return myEditableTableCell; 
            }
        });
        supAddress.setCellValueFactory(cellData -> cellData.getValue().getAddressProperty());
        supAddress.setCellFactory(new Callback<TableColumn<Supplier,String>, TableCell<Supplier,String>>() {

            @Override
            public TableCell<Supplier, String> call(
                    TableColumn<Supplier, String> param)
            {
                TextFieldTableCell<Supplier, String>myEditableTableCell = new TextFieldTableCell<Supplier, String>(new DefaultStringConverter())
                {
                    @Override
                    public void commitEdit(String val) 
                    {
                        int index = this.getTableRow().getIndex();
                        Supplier newUser = this.getTableView().getItems().get(index);
                        StringProperty oldval = newUser.getAddressProperty();
                        supplierTable.get(index).setAddress(val);
                        this.setItem(val);
                    }
               };
                
                return myEditableTableCell; 
            }
        });
        supPhone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        supPhone.setCellFactory(new Callback<TableColumn<Supplier,String>, TableCell<Supplier,String>>() {

            @Override
            public TableCell<Supplier, String> call(
                    TableColumn<Supplier, String> param)
            {
                TextFieldTableCell<Supplier, String>myEditableTableCell = new TextFieldTableCell<Supplier, String>(new DefaultStringConverter())
                {
                    @Override
                    public void commitEdit(String val) 
                    {
                        int index = this.getTableRow().getIndex();
                        Supplier newUser = this.getTableView().getItems().get(index);
                        StringProperty oldval = newUser.getPhoneProperty();
                        supplierTable.get(index).setPhone(val);
                        this.setItem(val);
                    }
               };
                myEditableTableCell.addEventFilter(KeyEvent.KEY_TYPED, numeric_Validation());
                return myEditableTableCell; 
            }
        });
        supEmail.setCellValueFactory(cellData -> cellData.getValue().getEmailProperty());
        supEmail.setCellFactory(new Callback<TableColumn<Supplier,String>, TableCell<Supplier,String>>() {

            @Override
            public TableCell<Supplier, String> call(
                    TableColumn<Supplier, String> param)
            {
                TextFieldTableCell<Supplier, String>myEditableTableCell = new TextFieldTableCell<Supplier, String>(new DefaultStringConverter())
                {
                    @Override
                    public void commitEdit(String val) 
                    {
                        int index = this.getTableRow().getIndex();
                        Supplier newUser = this.getTableView().getItems().get(index);
                        StringProperty oldval = newUser.getEmailProperty();
                        supplierTable.get(index).setEmail(val);
                        this.setItem(val);
                    }
               };
                
                return myEditableTableCell; 
            }
        });
        supplierView.setItems(supplierTable);// TODO
        
        //set up pending columns
        pendID.setCellValueFactory(new PropertyValueFactory<>("id"));
        pendName.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        pendSup.setCellValueFactory(cellData -> cellData.getValue().getSupplierPropery());
        pendAmount.setCellValueFactory(new PropertyValueFactory<>("Quantaty"));
        pendPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        pendDate.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
        pendView.setItems(pendingTable);
        
        newPenPrice.addEventFilter(KeyEvent.KEY_TYPED, numeric_Validation());
        newPenAmount.addEventFilter(KeyEvent.KEY_TYPED, numeric_Validation());
        newPenName.addEventFilter(KeyEvent.KEY_TYPED, characterValidation(30));
        newPenDate.addEventFilter(KeyEvent.KEY_TYPED, dateVal());
        
        newSupName.addEventFilter(KeyEvent.KEY_TYPED, characterValidation(30));
        newSupDesc.addEventFilter(KeyEvent.KEY_TYPED, characterValidation(30));
        newSupAdd.addEventFilter(KeyEvent.KEY_TYPED, characterValidation(30));
        newSupE.addEventFilter(KeyEvent.KEY_TYPED, characterValidation(30));
        newSupPhone.addEventFilter(KeyEvent.KEY_TYPED, phone_Validation(15));
    }
    
    public EventHandler<KeyEvent> characterValidation(final Integer max_Lengh) {
    return new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent e) {
//            TextField txt_TextField = (TextField) e.getSource();                
//            if (txt_TextField.getText().length() >= max_Lengh) {                    
//                e.consume();
//            }
            
            if(e.getCharacter().equals("*") || e.getCharacter().equals(";")){ 
                e.consume();
            }
        }
    };
   }
    public EventHandler<KeyEvent> phone_Validation(final Integer max_Lengh) {
    return new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent e) {
            
           if(!e.getCharacter().matches("[0-9.]")){ 
                 e.consume();
            }
        }
    };
}  
    public EventHandler<KeyEvent> dateVal() {
    return new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent e) {
           e.consume();
        }
    };
}  
    //closer for supplier, write into database filled in methods
    public void closeSupplier(ActionEvent e) throws NoSuchFieldException, ClassNotFoundException, SQLException
    {
         Stage stage = (Stage) backFromSupplier.getScene().getWindow();
         MyConnection conn =new MyConnection();
         for(Supplier p: supplierTable)
         {
             conn.updateSupplierList(p);
         }
         stage.close();
    }
    //numeric validator
    public EventHandler<KeyEvent> numeric_Validation() {
    return new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent e) {
            
            if(!e.getCharacter().matches("[0-9.]")){ 
                
                e.consume();
            }
        }
    };
} 
    //method for removing suppliers
    public void deleteSelection(ActionEvent e) throws IOException, ClassNotFoundException, SQLException
    {       MyConnection deleteSup=new MyConnection();
            int n = JOptionPane.showConfirmDialog(null,
                "Do you want to delete the selection?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);
            if(n==0){
                newPenSup.getItems().clear();
                supplierView.setItems(null);
                for (int i=0;i<supplierTable.size();i++)
                {   
                   
                
                    if (supplierTable.get(i).getDelete()==true)
                    {   
                        deleteSup.removeSupplier(supplierTable.get(i));
                        
                        supplierTable.remove(i);
                        
                    }
                    
                }
                    supplierView.layout(); 
                    supplierView.setItems(FXCollections.observableList(supplierTable));
                    //update pending combobox
                for (Supplier combo:supplierTable)
        {
            newPenSup.getItems().add(combo.getName());
        }
            newPenSup.getSelectionModel().select(0);
                
            }
    }
    //add supplier
    public void toSupplier(ActionEvent e) throws ClassNotFoundException, SQLException, NullPointerException
    {  try{ int n = JOptionPane.showConfirmDialog(null,
                "Do you want to add this supplier?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);
            if(n==0){    
        if(newSupName.getText().equals("") || newSupDesc.getText().equals("") || newSupAdd.getText().equals("")
                || newSupPhone.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Not all fields are filled");
                }
            else{
                    Supplier newsup=new Supplier(supplierTable.size()+1,newSupName.getText(),newSupDesc.getText(),
                    newSupAdd.getText(),newSupPhone.getText(),
                    newSupE.getText(),false);
                    supplierHolder.getSelectionModel().select(0);
                    MyConnection addsup=new MyConnection();
                    addsup.addSupplier(newsup);
                    newsup.setID(addsup.idDBSupplier(newsup.getName()));
                    supplierTable.add(newsup);
                    newSupName.setText(null);
                    newSupDesc.setText(null);
                    newSupAdd.setText(null);
                    newSupPhone.setText(null);
                    newSupE.setText(null);
                    
                     newPenSup.getItems().add(newsup.getName());
                     for (Supplier combo:supplierTable)
        {
            newPenSup.getItems().add(combo.getName());
        }
            newPenSup.getSelectionModel().select(0);
                }
            }}catch(NullPointerException t){JOptionPane.showInputDialog(null, "Not all fields are field");}
    }
    //add to pending
    public void toPending(ActionEvent e) throws ClassNotFoundException, SQLException, ParseException, NullPointerException
    {   
       try{
           SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
           
                Date deadline = formatter.parse( newPenDate.getValue().toString());

                Date today=formatter.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

                int compare=deadline.compareTo(today);
                if(compare<=0)
                {
                    JOptionPane.showMessageDialog(null,"Cannot add a part on todays or a past date!");
                    
                }else{
        int n = JOptionPane.showConfirmDialog(null,
                "Do you want to add this part to pending?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);
        if(n==0){
            if(newPenName.getText().equals("") || newPenAmount.getText().equals("") || newPenPrice.getText().equals("") || newPenAmount.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Not all fields are filled");
                }else{
        OrderPart part=new OrderPart(0, newPenName.getText(),null, 
                Integer.parseInt(newPenAmount.getText()),Double.parseDouble(newPenPrice.getText()),
                false,
                "",
                newPenSup.getSelectionModel().getSelectedItem().toString(),
                newPenDate.getValue().toString());
                supplierHolder.getSelectionModel().select(1);
        MyConnection pendCon= new MyConnection();
        pendCon.addPending(part);
        part.setId(pendCon.idDBPending(part.getName()));
        pendingTable.add(part);
        newPenName.setText(null);
        newPenAmount.setText(null);
        newPenPrice.setText(null);
        newPenDate.setValue(null);
        // return part, if supplier interface is the connector
        date=part.getDate();
        repair.setExpectedPart(part);
            }
        
            }
                
        }
                }catch(NullPointerException t){}
                catch(ParseException b){JOptionPane.showMessageDialog(null, "Date is empty");}
    }
}