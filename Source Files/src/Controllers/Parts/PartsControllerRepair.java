package Controllers.Parts;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alexander_2
 */
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javax.swing.JOptionPane;
import javafx.util.Callback;
import main.GLOBAL;


public class PartsControllerRepair implements Initializable {
   //prepare dates and the models collections
    private String expectedDate;
    private ObservableList<OrderPart> supplierParts=FXCollections.observableArrayList();
    private int tableIndex;
    private ArrayList<ObservableList<Part>> stock;
    private double partTotal;
    ObservableList<BasketPart> repairPart=FXCollections.observableArrayList();
    //fxml variables
    @FXML
    AnchorPane anchor;
    @FXML
    private TabPane tableHolder;
    @FXML
    private Tab bodyMainTab;
    @FXML
    private Tab powerTrainTab;
    @FXML
    private Tab chassisTab;
    @FXML
    private Tab lightsTab;
    @FXML
    private Tab ElectricsTab;
    @FXML
    private Tab audioTab;
    @FXML
    private Tab interiorTab;
    @FXML
    private Tab miscTab;
    @FXML
    private Tab outfitTab;
    @FXML
    private Tab airTab;
    @FXML
    private TilePane basket;
    @FXML 
    private Button addPart=new Button();
    @FXML
    private ImageView logo=new ImageView();
    //bodymain
    ArrayList<TableColumn> bodyColumns=new ArrayList<TableColumn>();
    Boolean toObject=false;
    private final ObservableList<Part> bodyMainTable=FXCollections.observableArrayList();
    ArrayList<TableView> views;
    @FXML
    private TableView<Part> bodyMainView;
    @FXML
    private TableColumn<Part, Integer> idBody;
    @FXML
    private TableColumn<Part, String> partName;
    @FXML
    private TableColumn<Part,String> partDesc;
    @FXML
    private TableColumn<Part, Integer> partLeftInStock;
    @FXML
    private TableColumn<Part, Integer> partPrice;
    @FXML
    private TableColumn<Part, Boolean> partOrder;
    //powertrain
    @FXML
     private final ObservableList<Part> powerTrainTable=FXCollections.observableArrayList();
    @FXML
    private TableView<Part> powerTrainView;
    @FXML
    private TableColumn<Part, Integer> idPower;
    @FXML
    private TableColumn<Part, String> partNamePower;
    @FXML
    private TableColumn<Part,String> partDescPower;
    @FXML
    private TableColumn<Part, Integer> partLeftInStockPower;
    @FXML
    private TableColumn<Part, Integer> partPricePower;
    @FXML
    private TableColumn<Part, Boolean> partOrderPower;
//    
//    //chassis
    @FXML
     private final ObservableList<Part> chassisTable=FXCollections.observableArrayList();
    @FXML
    private TableView<Part> chassisView;
    @FXML
    private TableColumn<Part, Integer> idChassis;
    @FXML
    private TableColumn<Part, String> partNameChassis;
    @FXML
    private TableColumn<Part,String> partDescChassis;
    @FXML
    private TableColumn<Part, Integer> partLeftInStockChassis;
    @FXML
    private TableColumn<Part, Integer> partPriceChassis;
    @FXML
    private TableColumn<Part, Boolean> partOrderChassis;
 //Lights
    @FXML
     private final ObservableList<Part> lightsTable=FXCollections.observableArrayList();
    @FXML
    private TableView<Part> lightsView;
    @FXML
    private TableColumn<Part, Integer> idLights;
    @FXML
    private TableColumn<Part, String> partNameLights;
    @FXML
    private TableColumn<Part,String> partDescLights;
    @FXML
    private TableColumn<Part, Integer> partLeftInStockLights;
    @FXML
    private TableColumn<Part, Integer> partPriceLights;
    @FXML
    private TableColumn<Part, Boolean> partOrderLights;
    //Electrics
    @FXML
     private final ObservableList<Part> electricsTable=FXCollections.observableArrayList();
    @FXML
    private TableView<Part> electricsView;
    @FXML
    private TableColumn<Part, Integer> idElectrics;
    @FXML
    private TableColumn<Part, String> partNameElectrics;
    @FXML
    private TableColumn<Part,String> partDescElectrics;
    @FXML
    private TableColumn<Part, Integer> partLeftInStockElectrics;
    @FXML
    private TableColumn<Part, Integer> partPriceElectrics;
    @FXML
    private TableColumn<Part, Boolean> partOrderElectrics;
    //Audio Video
    @FXML
    private final ObservableList<Part> audioTable=FXCollections.observableArrayList();
    @FXML
    private TableView<Part> audioView;
    @FXML
    private TableColumn<Part, String> partNameAudio;
    @FXML
    private TableColumn<Part, Integer> idAudio;
    @FXML
    private TableColumn<Part,String> partDescAudio;
    @FXML
    private TableColumn<Part, Integer> partLeftInStockAudio;
    @FXML
    private TableColumn<Part, Integer> partPriceAudio;
    @FXML
    private TableColumn<Part, Boolean> partOrderAudio;
    //Interior
    @FXML
    private final ObservableList<Part> interiorTable=FXCollections.observableArrayList();
    @FXML
    private TableView<Part> interiorView;
    @FXML
    private TableColumn<Part, String> partNameInterior;
    @FXML
    private TableColumn<Part, Integer> idInterior;
    @FXML
    private TableColumn<Part,String> partDescInterior;
    @FXML
    private TableColumn<Part, Integer> partLeftInStockInterior;
    @FXML
    private TableColumn<Part, Integer> partPriceInterior;
    @FXML
    private TableColumn<Part, Boolean> partOrderInterior;
    //MISC
    @FXML
    private final ObservableList<Part> miscTable=FXCollections.observableArrayList();
    @FXML
    private TableView<Part> miscView;
    @FXML
    private TableColumn<Part, Integer> idMisc;
    @FXML
    private TableColumn<Part, String> partNameMisc;
    @FXML
    private TableColumn<Part,String> partDescMisc;
    @FXML
    private TableColumn<Part, Integer> partLeftInStockMisc;
    @FXML
    private TableColumn<Part, Integer> partPriceMisc;
    @FXML
    private TableColumn<Part, Boolean> partOrderMisc;
    //Outfit
    @FXML
    private final ObservableList<Part> outfitTable=FXCollections.observableArrayList();
    @FXML
    private TableView<Part> outfitView;
    @FXML
    private TableColumn<Part, Integer> idOutfit;
    @FXML
    private TableColumn<Part, String> partNameOutfit;
    @FXML
    private TableColumn<Part,String> partDescOutfit;
    @FXML
    private TableColumn<Part, Integer> partLeftInStockOutfit;
    @FXML
    private TableColumn<Part, Integer> partPriceOutfit;
    @FXML
    private TableColumn<Part, Boolean> partOrderOutfit;
    //Air and Heat
    @FXML
    private final ObservableList<Part> airTable=FXCollections.observableArrayList();
    @FXML
    private TableView<Part> airView;
    @FXML
    private TableColumn<Part, String> partNameAir;
    @FXML
    private TableColumn<Part, Integer> idAir;
    @FXML
    private TableColumn<Part,String> partDescAir;
    @FXML
    private TableColumn<Part, Integer> partLeftInStockAir;
    @FXML
    private TableColumn<Part, Integer> partPriceAir;
    @FXML
    private TableColumn<Part, Boolean> partOrderAir;
   //TextFields ofr input
    @FXML 
    private TextField nameInput;
    @FXML
    private TextField descInput;
    @FXML
    private TextField amountInput;
    @FXML
    private TextField priceInput;
    @FXML
    private ComboBox typeChooser;

    public PartsControllerRepair() throws ClassNotFoundException, SQLException {
      stock=new ArrayList<ObservableList<Part>>();
        stock.add(bodyMainTable);
        stock.add(powerTrainTable);
        stock.add(chassisTable);
        stock.add(lightsTable);
        stock.add(electricsTable);
        stock.add(audioTable);
        stock.add(interiorTable);
        stock.add(miscTable);
        stock.add(outfitTable);
        stock.add(airTable);
        //check for repeating values and assign parts to types categories
     try { 
         
         Collection<Part> parts = MyConnection.readPartStock();
         for(Part p: parts)
            {
               if( p.getType().equals("Body and Main"))
               {
                   bodyMainTable.add(p);
               }
               else if( p.getType().equals("Powertrain"))
               {
                   powerTrainTable.add(p);
               }
               else if( p.getType().equals("Chassis"))
               {
                   chassisTable.add(p);
               }
               else if( p.getType().equals("Lights"))
               {
                   lightsTable.add(p);
               }
               else if( p.getType().equals("Electrics"))
               {
                   electricsTable.add(p);
               }
               else if( p.getType().equals("Audio"))
               {
                   audioTable.add(p);
               }
               else if( p.getType().equals("Interior"))
               {
                   interiorTable.add(p);
               }
               else if( p.getType().equals("Misc"))
               {
                   miscTable.add(p);
               }
               else if( p.getType().equals("Outfit"))
               {
                   outfitTable.add(p);
               }
               else if( p.getType().equals("Air"))
               {
                   airTable.add(p);
               }
            }
     } catch (SQLException ex) {
            Logger.getLogger(PartsControllerRepair.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PartsControllerRepair.class.getName()).log(Level.SEVERE, null, ex);
        }    
      
    }
    @Override
    public void initialize(URL url, ResourceBundle rb){
       
        anchor.setStyle("-fx-background-color: #FFFFFF;");
        //my methods for the tableMain
        idBody.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        partDesc.setCellValueFactory(cellData -> cellData.getValue().getDescProperty());
        partLeftInStock.setCellValueFactory(new PropertyValueFactory<>("Quantaty"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        partOrder.setCellValueFactory(new PropertyValueFactory<Part, Boolean>("Basket"));
        partOrder.setCellFactory(new Callback<TableColumn<Part, Boolean>, TableCell<Part, Boolean>>() {

                public TableCell<Part, Boolean> call(TableColumn<Part, Boolean> p) {
                CheckBoxTableCell<Part, Boolean> cell=new CheckBoxTableCell<Part, Boolean>();
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
                                    bodyMainTable.get(chosen).setOrdered(isSelected);
                                    
                                    
                                }
                            });
                                            return cell;
                                        }
                                    });
                bodyColumns.add(idBody);
                bodyColumns.add(partName);
                bodyColumns.add(partDesc);
                bodyColumns.add(partLeftInStock);
                bodyColumns.add(partPrice);
                bodyColumns.add(partOrder);
                
                bodyMainView.setItems(bodyMainTable);
        
        //my methods for the Powertrain
        idPower.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNamePower.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        partDescPower.setCellValueFactory(cellData -> cellData.getValue().getDescProperty());
        partLeftInStockPower.setCellValueFactory(new PropertyValueFactory<>("Quantaty"));
        partPricePower.setCellValueFactory(new PropertyValueFactory<>("Price"));
        partOrderPower.setCellValueFactory(new PropertyValueFactory<Part, Boolean>("Basket"));
        partOrderPower.setCellFactory(new Callback<TableColumn<Part, Boolean>, TableCell<Part, Boolean>>() {

                public TableCell<Part, Boolean> call(TableColumn<Part, Boolean> p) {
                CheckBoxTableCell<Part, Boolean> cell=new CheckBoxTableCell<Part, Boolean>();
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
                                    powerTrainTable.get(chosen).setOrdered(isSelected);
                                }
                            });
                                            return cell;
                                        }
                                    });
        
        powerTrainView.setItems(powerTrainTable);
        //my methods for the chassis
        idChassis.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameChassis.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        partDescChassis.setCellValueFactory(cellData -> cellData.getValue().getDescProperty());
        partLeftInStockChassis.setCellValueFactory(new PropertyValueFactory<>("Quantaty"));
        partPriceChassis.setCellValueFactory(new PropertyValueFactory<>("Price"));
        partOrderChassis.setCellValueFactory(new PropertyValueFactory<Part, Boolean>("Basket"));
        partOrderChassis.setCellFactory(new Callback<TableColumn<Part, Boolean>, TableCell<Part, Boolean>>() {

                public TableCell<Part, Boolean> call(TableColumn<Part, Boolean> p) {
                CheckBoxTableCell<Part, Boolean> cell=new CheckBoxTableCell<Part, Boolean>();
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
                                    chassisTable.get(chosen).setOrdered(isSelected);
                                }
                            });
                                            return cell;
                                        }
                                    });
        chassisView.setItems(chassisTable);
         //my methods for the Lights
        idLights.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameLights.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        partDescLights.setCellValueFactory(cellData -> cellData.getValue().getDescProperty());
        partLeftInStockLights.setCellValueFactory(new PropertyValueFactory<>("Quantaty"));
        partPriceLights.setCellValueFactory(new PropertyValueFactory<>("Price"));
        partOrderLights.setCellValueFactory(new PropertyValueFactory<Part, Boolean>("Basket"));
        partOrderLights.setCellFactory(new Callback<TableColumn<Part, Boolean>, TableCell<Part, Boolean>>() {

                public TableCell<Part, Boolean> call(TableColumn<Part, Boolean> p) {
                CheckBoxTableCell<Part, Boolean> cell=new CheckBoxTableCell<Part, Boolean>();
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
                                    lightsTable.get(chosen).setOrdered(isSelected);
                                }
                            });
                                            return cell;
                                        }
                                    });
        lightsView.setItems(lightsTable);
        //my methods for the Electrics
        idElectrics.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameElectrics.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        partDescElectrics.setCellValueFactory(cellData -> cellData.getValue().getDescProperty());
        partLeftInStockElectrics.setCellValueFactory(new PropertyValueFactory<>("Quantaty"));
        partPriceElectrics.setCellValueFactory(new PropertyValueFactory<>("Price"));
        partOrderElectrics.setCellValueFactory(new PropertyValueFactory<Part, Boolean>("Basket"));
        partOrderElectrics.setCellFactory(new Callback<TableColumn<Part, Boolean>, TableCell<Part, Boolean>>() {

                public TableCell<Part, Boolean> call(TableColumn<Part, Boolean> p) {
                CheckBoxTableCell<Part, Boolean> cell=new CheckBoxTableCell<Part, Boolean>();
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
                                    electricsTable.get(chosen).setOrdered(isSelected);
                                }
                            });
                                            return cell;
                                        }
                                    });
        electricsView.setItems(electricsTable);
        //my methods for the Audio Video
        idAudio.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameAudio.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        partDescAudio.setCellValueFactory(cellData -> cellData.getValue().getDescProperty());
        partLeftInStockAudio.setCellValueFactory(new PropertyValueFactory<>("Quantaty"));
        partPriceAudio.setCellValueFactory(new PropertyValueFactory<>("Price"));
         partOrderAudio.setCellValueFactory(new PropertyValueFactory<Part, Boolean>("Basket"));
        partOrderAudio.setCellFactory(new Callback<TableColumn<Part, Boolean>, TableCell<Part, Boolean>>() {

                public TableCell<Part, Boolean> call(TableColumn<Part, Boolean> p) {
                CheckBoxTableCell<Part, Boolean> cell=new CheckBoxTableCell<Part, Boolean>();
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
                                    audioTable.get(chosen).setOrdered(isSelected);
                                }
                            });
                                            return cell;
                                        }
                                    });
        audioView.setItems(audioTable);
        //my methods for the Interior
        idInterior.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameInterior.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        partDescInterior.setCellValueFactory(cellData -> cellData.getValue().getDescProperty());
        partLeftInStockInterior.setCellValueFactory(new PropertyValueFactory<>("Quantaty"));
        partPriceInterior.setCellValueFactory(new PropertyValueFactory<>("Price"));
         partOrderInterior.setCellValueFactory(new PropertyValueFactory<Part, Boolean>("Basket"));
        partOrderInterior.setCellFactory(new Callback<TableColumn<Part, Boolean>, TableCell<Part, Boolean>>() {

                public TableCell<Part, Boolean> call(TableColumn<Part, Boolean> p) {
                CheckBoxTableCell<Part, Boolean> cell=new CheckBoxTableCell<Part, Boolean>();
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
                                    interiorTable.get(chosen).setOrdered(isSelected);
                                }
                            });
                                            return cell;
                                        }
                                    });
        interiorView.setItems(interiorTable);
         //my methods for the MISC
        idMisc.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameMisc.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        partDescMisc.setCellValueFactory(cellData -> cellData.getValue().getDescProperty());
        partLeftInStockMisc.setCellValueFactory(new PropertyValueFactory<>("Quantaty"));
        partPriceMisc.setCellValueFactory(new PropertyValueFactory<>("Price"));
         partOrderMisc.setCellValueFactory(new PropertyValueFactory<Part, Boolean>("Basket"));
        partOrderMisc.setCellFactory(new Callback<TableColumn<Part, Boolean>, TableCell<Part, Boolean>>() {

                public TableCell<Part, Boolean> call(TableColumn<Part, Boolean> p) {
                CheckBoxTableCell<Part, Boolean> cell=new CheckBoxTableCell<Part, Boolean>();
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
                                    miscTable.get(chosen).setOrdered(isSelected);
                                }
                            });
                                            return cell;
                                        }
                                    });
        miscView.setItems(miscTable);
        //my methods for the Outfit
        idOutfit.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameOutfit.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        partDescOutfit.setCellValueFactory(cellData -> cellData.getValue().getDescProperty());
        partLeftInStockOutfit.setCellValueFactory(new PropertyValueFactory<>("Quantaty"));
        partPriceOutfit.setCellValueFactory(new PropertyValueFactory<>("Price"));
         partOrderOutfit.setCellValueFactory(new PropertyValueFactory<Part, Boolean>("Basket"));
        partOrderOutfit.setCellFactory(new Callback<TableColumn<Part, Boolean>, TableCell<Part, Boolean>>() {

                public TableCell<Part, Boolean> call(TableColumn<Part, Boolean> p) {
                CheckBoxTableCell<Part, Boolean> cell=new CheckBoxTableCell<Part, Boolean>();
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
                                    outfitTable.get(chosen).setOrdered(isSelected);
                                }
                            });
                                            return cell;
                                        }
                                    });
        outfitView.setItems(outfitTable);
        //my methods for the Air and Heating
        idAir.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameAir.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        partDescAir.setCellValueFactory(cellData -> cellData.getValue().getDescProperty());
        partLeftInStockAir.setCellValueFactory(new PropertyValueFactory<>("Quantaty"));
        partPriceAir.setCellValueFactory(new PropertyValueFactory<>("Price"));
        partOrderAir.setCellValueFactory(new PropertyValueFactory<Part, Boolean>("Basket"));
        partOrderAir.setCellFactory(new Callback<TableColumn<Part, Boolean>, TableCell<Part, Boolean>>() {

                public TableCell<Part, Boolean> call(TableColumn<Part, Boolean> p) {
                CheckBoxTableCell<Part, Boolean> cell=new CheckBoxTableCell<Part, Boolean>();
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
                                    airTable.get(chosen).setOrdered(isSelected);
                                }
                            });
                                            return cell;
                                        }
                                    });
        airView.setItems(airTable);
       
        logo.setImage(new Image("/images/logo.jpg"));
        //method call
          

        
    }
    //numeric validation
   public EventHandler<KeyEvent> numeric_Validation(final Integer max_Lengh) {
    return new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent e) {
            TextField txt_TextField = (TextField) e.getSource();                
            if (txt_TextField.getText().length() >= max_Lengh) {                    
                e.consume();
            }
            if(e.getCharacter().matches("[0-9.]")){ 
                if(txt_TextField.getText().contains(".") && e.getCharacter().matches("[.]")){
                    e.consume();
                }else if(txt_TextField.getText().length() == 0 && e.getCharacter().matches("[.]")){
                    e.consume(); 
                }
            }else{
                e.consume();
            }
        }
    };
}  //send items to basket
    public void getBasket(ActionEvent e) throws IOException
    {
        //add items in stock
       
        //items that will go to basket
        ObservableList<Part> toBasket=FXCollections.observableArrayList();
        
      for(int j=0;j<stock.size();j++)
      {
        for(int i=0;i<stock.get(j).size();i++){
        //System.out.println(toBasket.get(j).get(i).getOrder());
        if(stock.get(j).get(i).getOrder()==true)
            {
                //fill basket
                toBasket.add(stock.get(j).get(i));
                
            }
         
        }   
      }  
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Parts/Basket.fxml"));
        Parent root = (Parent)loader.load();
        BasketController control= loader.<Controllers.Parts.BasketController>getController();
        control.getData(toBasket,this);
        Stage basket=new Stage();
        Scene scene=new Scene(root);
        basket.setTitle("Basket");
        basket.setScene(scene);
        GLOBAL.StartPagePointer.disableTab();
        basket.showAndWait();
         GLOBAL.StartPagePointer.enableTab();
    }
   
    //show supplier interface
    public void getSupplierList(ActionEvent e) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Parts/SupplierList.fxml"));
        Parent root = (Parent)loader.load();
        SupplierListController sup=loader.<Controllers.Parts.SupplierListController>getController();
        sup.setConnectors(this);
        Scene scene = new Scene(root);
        Stage supplier=new Stage();
        supplier.setTitle("Supplier List");
        supplier.setScene(scene);
        supplier.initModality(Modality.APPLICATION_MODAL);
        
        GLOBAL.StartPagePointer.disableTab();
        supplier.showAndWait();
         GLOBAL.StartPagePointer.enableTab();
    }
    
    //update stock interface after the basket interface
    public void updateTables(ObservableList<BasketPart> updateObject, double total)throws IOException, ClassNotFoundException, SQLException
    {   //prepare views for the loop
        views=new ArrayList<TableView>();
        views.add(bodyMainView);
        views.add(powerTrainView);
        views.add(chassisView);
        views.add(lightsView);
        views.add(electricsView);
        views.add(audioView);
        views.add(interiorView);
        views.add(miscView);
        views.add(outfitView);
        views.add(airView);
        MyConnection newconn=new MyConnection();
        //search for changed quantaty in all the tables
        for(TableView v : views){
            
            
            ObservableList<Part> viewRows=FXCollections.observableArrayList();
                    viewRows.addAll(v.getItems());
            v.setItems(null);
            for(int t=0;t<viewRows.size();t++)
            {
                for(BasketPart b: updateObject)
                {   int amount = viewRows.get(t).getQuantaty();
                    if(viewRows.get(t).getName().equalsIgnoreCase(b.getName()))
                    {   
                        amount=amount-Integer.parseInt(b.getWithdraw());
                        viewRows.get(t).setQuantaty(amount);
                        if(amount<=0)
                        {  //if all sold out - amount it to zero
                        viewRows.get(t).setQuantaty(0);
                            Part toDB;
                                toDB=new Part(viewRows.get(t).getId(),viewRows.get(t).getName(),viewRows.get(t).getDesc(),0,viewRows.get(t).getPrice(),viewRows.get(t).getOrder(),viewRows.get(t).getType());
                                newconn.updatePartStock(toDB);
                        }else{//if not all - set the resultant abount
                                Part toDB;
                                toDB=new Part(viewRows.get(t).getId(),viewRows.get(t).getName(),viewRows.get(t).getDesc(),amount,viewRows.get(t).getPrice(),viewRows.get(t).getOrder(),viewRows.get(t).getType());
                                newconn.updatePartStock(toDB); 
                                }
                             }
                         }
                       }
                       v.layout(); 
                       v.setItems(FXCollections.observableList(viewRows));
                    }
        //assign total variable for parent controller
            partTotal=total;
        }
    //setter and getter methods for passing on the parts into the Diagnosis and Repairs controller
    
    //for cost
    public double getTotal()
    {
        return partTotal;
    }
    public void setTotal(double tot)
    {
        partTotal=tot;
    }
    //for stock part
    public ObservableList<BasketPart> getRepairPart()
    {
        return repairPart;
    }
    public void setRepairPart(ObservableList<BasketPart> par)
    {
        repairPart.addAll(par);
    }
    //for part ordered from the supplier
    public void setExpectedPart(OrderPart par)
    {
        supplierParts.add(par);
    }
    public ObservableList<OrderPart> getExpectedPart()
    {
        return supplierParts;
    }
}
