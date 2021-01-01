/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.DiagnosisRepairBookings;


import java.util.Calendar;

/**
 *
 * @author jackilrajnicant
 */
public class Vehicle {
    
    private int vehicleID;
    private CustomerAccount customer;
    private int CarType;
    private String Make;
    private String Model;
    private String EngineSize;
    private String FuelType;
    private String Colour;
    private Calendar MOTExpire;
    private String Registration;    
    private int Year;
    private int Mileage;
    private Calendar LastService;
    private int Warranty;
    private String WarrantyCompany;
    private String WarrentyExpireDate;
    
    public Vehicle(){
        
    }
    
    
    public int getID(){
        return vehicleID;
    }
    
    public void setID(int id){
        vehicleID=id;
    }
    
    
    public Vehicle(CustomerAccount ca){
        customer = ca;
    }

    public String getWarrantyCompany() {
        return WarrantyCompany;
    }

    public void setWarrantyCompany(String WarrantyCompany) {
        this.WarrantyCompany = WarrantyCompany;
    }
    
    
    public String getRegistration() {
        return Registration;
    }

    public void setRegistration(String Registration) {
        this.Registration = Registration;
    }
    
    
    public int getCarType() {
        return CarType;
    }

    public void setCarType(int CarType) {
        this.CarType = CarType;
    }  

    public String getMake() {
        return Make;
    }

    public void setMake(String Make) {
        this.Make = Make;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String Model) {
        this.Model = Model;
    }

    public String getEngineSize() {
        return EngineSize;
    }

    public void setEngineSize(String EngineSize) {
        this.EngineSize = EngineSize;
    }

    public String getFuelType() {
        return FuelType;
    }

    public void setFuelType(String FuelType) {
        this.FuelType = FuelType;
    }

    public String getColour() {
        return Colour;
    }

    public void setColour(String Colour) {
        this.Colour = Colour;
    }

    public Calendar getMOTExpire() {
        return MOTExpire;
    }

    public void setMOTExpire(Calendar MOTExpire) {
        this.MOTExpire = MOTExpire;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int Year) {
        this.Year = Year;
    }

    public int getMileage() {
        return Mileage;
    }

    public void setMileage(int Mileage) {
        this.Mileage = Mileage;
    }

    public Calendar getLastService() {
        return LastService;
    }

    public void setLastService(Calendar LastService) {
        this.LastService = LastService;
    }

    public int getWarranty() {
        return Warranty;
    }

    public void setWarranty(int Warranty) {
        this.Warranty = Warranty;
    }
       

    public CustomerAccount getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerAccount customer) {
        this.customer = customer;
    }

    /**
     * @return the WarrentyExpireDate
     */
    public String getWarrentyExpireDate() {
        return WarrentyExpireDate;
    }

    /**
     * @param WarrentyExpireDate the WarrentyExpireDate to set
     */
    public void setWarrentyExpireDate(String WarrentyExpireDate) {
        this.WarrentyExpireDate = WarrentyExpireDate;
    }
    
}
