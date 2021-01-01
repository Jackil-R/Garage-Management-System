/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.DiagnosisRepairBookings;
/**
 *
 * @author jackilrajnicant
 */
public class Mechanic {
    
    private int mechID;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private double rate;

    public Mechanic(){
    }
    
    public int getID() {
        return mechID;
    }

    public void setID(int mechID) {
        this.mechID = mechID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String pwd) {
        this.password = pwd;
    }

    public String getpassword() {
        return password;
    }
    
    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
