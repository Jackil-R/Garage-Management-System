/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.ScheduledMaintenanceBookings;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Julians
 */
public class Validator {
    
    public Validator(){
    
    }
    
    String error = "";

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        String e = error;
        error = "";
        return e;
    }
    
    public boolean checkServiceReport(String rst, String mil, String desc, Booking b){
        
        boolean result = checkOutcome(rst) && checkMileage(mil,b) && checkDescription(desc) && checkBookingValid(b);
        
        setError(errorMessageServiceReport(checkOutcome(rst), checkMileage(mil,b), checkDescription(desc), checkBookingValid(b)));    
                
        return true;
    }
    
    public boolean checkOutcome(String s){
    
        if (s.equals("SUCCESS") || s.equals("FAIL"))
        return true;
        
        return false;
    }
    
    public boolean checkMileage(String m, Booking b){
        try {
          int value = Integer.parseInt(m);
          if (b.getVehicle().getMileage() > value)
              return false;
    } catch (Exception e) {
            return false;
    } // END OF TRY CATCH
        return true;
    }
    
    public String removeSQL(String s){
    s =  s.replace("(;)", "");
    s =  s.replace("ALTER", "");
    s =  s.replace("CREATE", "");
    s =  s.replace("DELETE", "");
    s =  s.replace("DELETETREE", "");
    s =  s.replace("DROP", "");
    s =  s.replace("EXEC", "");
    s =  s.replace("INSERT", "");
    s =  s.replace("MERGE", "");
    s =  s.replace("SELECT", "");
    s =  s.replace("UNION", "");
    s =  s.replace("UPDATE", "");
 
 return s;
    }
    
   
    public boolean checkDescription(String s){     
        
        if (s.toUpperCase().contains("ALTER")) return false;
        if (s.toUpperCase().contains("CREATE")) return false;
        if (s.toUpperCase().contains("DELETE")) return false;
        if (s.toUpperCase().contains("DELETETREE")) return false;
        if (s.toUpperCase().contains("DROP")) return false;
        if (s.toUpperCase().contains("EXEC")) return false;
        if (s.toUpperCase().contains("INSERT")) return false;
        if (s.toUpperCase().contains("MERGE")) return false;
        if (s.toUpperCase().contains("SELECT")) return false;
        if (s.toUpperCase().contains("UNION")) return false;
        if (s.toUpperCase().contains("UPDATE")) return false;
        
        
       return true; 
    }
    
    public boolean checkBookingValid(Booking b){
       if (b == null) return false;
       if (!(b.getID() > 0)) return false;
      
       return true;
    }
    
    public boolean checkBookingDate(Calendar c, Calendar d){      
        
        boolean result = checkDay(c) && checkTimes(c,d) && checkPastDay(c) && checkHoliday(c);
        
        setError(errorMessage(checkDay(c), checkTimes(c,d), checkPastDay(c), checkHoliday(c)));
        
        return result;
    }
    
    private String errorMessage(boolean a,boolean b, boolean c, boolean d){
        String msg = "";
        if (!a) msg += " Garage is not open on Sunday.";
        if (!b) msg += " Garage is not open at these times.";
        if (!c) msg += " You can not make a booking in the past.";
        if (!d) msg += " Booking date is on a public holiday.";
        
        return msg;
    }
    
    private String errorMessageServiceReport(boolean a,boolean b, boolean c, boolean d){
        String msg = "";
        if (!a) msg += " Maintanence outcome is not set.";
        if (!b) msg += " Mileage is incorrect value. Make sure it is not lower than previous value.";
        if (!c) msg += " Description is invalid. SQL Keywords aren't allowed here.";
        if (!d) msg += " There has been a fatal error, please close this dialog and try again.";
        
        return msg;
    }
    
  
    private ArrayList<Holiday> parseHolidays(){
        
        ArrayList<Holiday> list = new ArrayList<Holiday>();
    
       FileInputStream fstream = null;
        try {
            fstream = new FileInputStream("resources/holidays.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
           
            while ((strLine = br.readLine()) != null)   {
               
                
                if (strLine.contains("BEGIN:VEVENT")){
                    Holiday h = new Holiday();
                    
                    String end = br.readLine();
                    String[] endSplit = end.split(":");
                    int endYear = Integer.parseInt(endSplit[1].substring(0,4));
                    int endMonth = Integer.parseInt(endSplit[1].substring(4,6));
                    int endDay = Integer.parseInt(endSplit[1].substring(6,8));
                    Calendar c1 = Calendar.getInstance();
                    c1.set(endYear, endMonth-1, endDay); 
                  //  System.out.println("END : year: "+endYear+" month: "+endMonth+" day: "+endDay);
                    h.setEnd(c1);
                    
                    
                    String start = br.readLine();
                    String[] startSplit = start.split(":");
                    int startYear = Integer.parseInt(startSplit[1].substring(0,4));
                    int startMonth = Integer.parseInt(startSplit[1].substring(4,6));
                    int startDay = Integer.parseInt(startSplit[1].substring(6,8));
                    Calendar c2 = Calendar.getInstance();
                    c2.set(startYear, startMonth-1, startDay); 
                  //  System.out.println("START : year: "+startYear+" month: "+startMonth+" day: "+startDay);
                    h.setStart(c2);
                    
                    String summary = br.readLine();
                    String[] s = summary.split(":");
                    h.setSummary(s[1]);
                    
                    list.add(h);               
                }
           
                
            }          
br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Validator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Validator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fstream.close();
            } catch (IOException ex) {
                Logger.getLogger(Validator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return list;
        
    }
    
    
    private boolean checkDay(Calendar c){
       int day = c.get(Calendar.DAY_OF_WEEK);
        System.out.println(day);
        if (day == Calendar.SUNDAY )
        return false;
        
    return true;
    }
    
    private boolean checkTimes(Calendar startTime, Calendar endTime){
        if (endTime.before(startTime)) return false;
        
        if (startTime.equals(endTime)) return false;
        
        int day = startTime.get(Calendar.DAY_OF_WEEK);
        if (day != Calendar.SATURDAY) {
            String start = "09:00";
            String end   = "17:30";
            SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm");
            String starthour = sdfHour.format(startTime.getTime());
            String endhour = sdfHour.format(endTime.getTime());
            boolean startCheck = isHourInInterval(starthour,start,end);
            boolean endCheck = isHourInInterval(endhour,start,end);
            return startCheck && endCheck;
        }else{
            String start = "09:00";
            String end   = "12:00";
            SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm");
            String starthour = sdfHour.format(startTime.getTime());
            String endhour = sdfHour.format(endTime.getTime());
            boolean startCheck = isHourInInterval(starthour,start,end);
            boolean endCheck = isHourInInterval(endhour,start,end);
            return startCheck && endCheck;
        }
    }
    
    public static boolean isHourInInterval(String target, String start, String end) {
        return ((target.compareTo(start) >= 0) && (target.compareTo(end) <= 0));
    }
    
    
    public boolean checkPastDay(Calendar c){
        Date today = new Date();
        if (today.after((c.getTime()))) {
            return false;
        }
        return true;
    }

    public boolean checkHoliday(Calendar c) {
         ArrayList<Holiday> list = parseHolidays();
       int day = c.getTime().getDate();
       int month = c.getTime().getMonth();
       int year = c.getTime().getYear();
        
        for (int i=0; i < list.size(); i++)
        {
       int Hday = list.get(i).getStart().getTime().getDate();
       int Hmonth = list.get(i).getStart().getTime().getMonth();
       int Hyear = list.get(i).getStart().getTime().getYear();
       
                if (
                        day == Hday && month == Hmonth && year == Hyear
                        )
                    return false;
        }
        
        return true;
    }

}
