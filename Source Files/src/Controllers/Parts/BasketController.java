package Controllers.Parts;

/**
 *
 * @author Alexander_2
 */
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.ResourceBundle;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import javax.swing.JOptionPane;


public class BasketController implements Initializable {
   // fxml variables
  @FXML private PartsControllerRepair repair;
  @FXML
   private Button closer;
  @FXML
  private Button back;
    @FXML
    private TableView<BasketPart> basketView;
    @FXML
    private TableColumn<BasketPart, Integer> basketId;
    @FXML
    private TableColumn<BasketPart, String> basketName;
    @FXML
    private TableColumn<BasketPart,String> basketDesc;
    @FXML
    private TableColumn<BasketPart, Integer> basketStock;
    @FXML
    private TableColumn<BasketPart, Integer> basketPrice;
    @FXML
    private TableColumn<BasketPart, Boolean> basketOrder;
    @FXML
    private TableColumn<BasketPart, String> basketWithdraw;
    Boolean toObject=true;
    public ObservableList<BasketPart> basketTable=FXCollections.observableArrayList();
    @FXML
    private Label warrantyStart;
    @FXML
    private Label warrantyEnd;
    @FXML
    private Label total;
    @FXML
    private Button calculate;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    //get data from stock
    public void getData(ObservableList<Part> myObject, PartsControllerRepair rep)
    {   //connector to the parent controller
        repair=rep;    
        //fill up the model
        for(int i=0; i<myObject.size();i++)
        {
            BasketPart inter=new BasketPart(myObject.get(i).getId(),myObject.get(i).getName(),
                                            myObject.get(i).getDesc(),
                                            myObject.get(i).getQuantaty(),
                                            myObject.get(i).getPrice(), toObject, "1");
            basketTable.add(inter);
        }
        //set up the table
        basketId.setCellValueFactory(new PropertyValueFactory<>("id"));
        basketName.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        basketDesc.setCellValueFactory(cellData -> cellData.getValue().getDescProperty());
        basketStock.setCellValueFactory(new PropertyValueFactory<>("Quantaty"));
        basketPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        basketOrder.setCellValueFactory(new PropertyValueFactory<BasketPart, Boolean>("Basket"));
        basketOrder.setCellFactory(new Callback<TableColumn<BasketPart, Boolean>, TableCell<BasketPart, Boolean>>() {

                public TableCell<BasketPart, Boolean> call(TableColumn<BasketPart, Boolean> p) {
                CheckBoxTableCell<BasketPart, Boolean> cell=new CheckBoxTableCell<BasketPart, Boolean>();
                BooleanProperty selected= new SimpleBooleanProperty();
                selected.setValue(true);
                cell.setSelectedStateCallback(new Callback<Integer, ObservableValue<Boolean>>() {
                                @Override
                                public ObservableValue<Boolean> call(Integer index) {
                                    return selected ;
                                }
                            });
                            selected.addListener(new ChangeListener<Boolean>() {
                                @Override
                                public void changed(ObservableValue<? extends Boolean> obs, Boolean wasSelected, Boolean isSelected) {
                                    System.out.println(isSelected);
                                    int chosen=cell.getTableRow().getIndex();
                                    basketTable.get(chosen).setOrdered(isSelected);
                                }
                            });
                                            return cell;
                                        }
                                    });
        basketWithdraw.setCellValueFactory(new PropertyValueFactory<>("withdraw"));
        basketWithdraw.setCellFactory(new Callback<TableColumn<BasketPart,String>, TableCell<BasketPart,String>>() {

            @Override
            public TableCell<BasketPart, String> call(
                    TableColumn<BasketPart, String> param)
            {
                TextFieldTableCell<BasketPart, String>myEditableTableCell = new TextFieldTableCell<BasketPart, String>(new DefaultStringConverter())
                {
                    @Override
                    public void commitEdit(String val) 
                    {
                        int index = this.getTableRow().getIndex();
                        BasketPart newUser = this.getTableView().getItems().get(index);
                        StringProperty oldval = newUser.getWithdrawPropery();
                        basketTable.get(index).setWithdraw(val);
                        this.setItem(val);
                    }
               };
               
                
                myEditableTableCell.addEventFilter(KeyEvent.KEY_TYPED, numeric_Validation(10));
                return myEditableTableCell; 
            }
        });
        //set up the warranty dates
        basketView.setItems(basketTable);
        Date date = new Date();
        String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
        warrantyStart.setText("Warranty Start:   "+modifiedDate);
        Calendar c = Calendar.getInstance(); 
        c.setTime(date); 
        c.add(Calendar.YEAR, 1);
        Date dateEnd = new Date();
        dateEnd = c.getTime();
        modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(dateEnd);
        warrantyEnd.setText("Warranty End:   "+modifiedDate);
    
    }
    //numeric validation
    public EventHandler<KeyEvent> numeric_Validation(final Integer max_Lengh) {
    return new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent e) {
           if(!e.getCharacter().matches("[0-9.]")){ 
                 e.consume();
            }
        }
    };
}  
    //check items out
    public void closeBasket(ActionEvent w) throws NoSuchFieldException, IOException, ClassNotFoundException, SQLException, ConcurrentModificationException, ParseException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Parts/PartsRepair.fxml"));
        Parent root = (Parent)loader.load();
        double cost=this.partCost();
        if(cost>=0){
            //set the awaiting values in parent controller
        repair.setTotal(cost);
        repair.setRepairPart(basketTable);
        repair.updateTables(basketTable,cost);
        Stage stage = (Stage) closer.getScene().getWindow();
        stage.close();}
    }
    //calculate total
    public void calculateTotal(ActionEvent a) throws ParseException
    {
            total.setText("Running total: £"+partCost());
    }
    public double partCost() throws ParseException
    {
            double totaler= 0;
            double pricer;
            int amounter;
            
            for (int i=0;i<basketTable.size();i++)
            {
               if(basketTable.get(i).getOrder()==true && basketTable.get(i).getQuantaty()>=Integer.parseInt(basketTable.get(i).getWithdraw()))
               {
                   pricer=basketTable.get(i).getPrice();
               
                amounter=Integer.parseInt(basketTable.get(i).getWithdraw());
                totaler=totaler+pricer*amounter;
               }
               else{JOptionPane.showMessageDialog(null,"You cannot buy more parts more than available");
                    }
            }
            
            totaler = Math.round( totaler * 100.0 ) / 100.0;
            return totaler;
    }
    //close without checkout
    public void close(ActionEvent e)
    {
        Stage stage = (Stage) back.getScene().getWindow();
        stage.close();
    }
}