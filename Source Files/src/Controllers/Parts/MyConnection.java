package Controllers.Parts;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.sql.Statement;


public class MyConnection
{
    Connection c = null;
    Statement stmt = null;
    public static Collection<Part> partView;
  MyConnection () throws ClassNotFoundException, SQLException
  {
    Connection c = null;
    Statement stmt = null;
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:resources//mechanic_universal_New.db3");
      c.setAutoCommit(true);
      System.out.println("Opened database successfully");
  }
  //remove part from stock
  public void removePart(Part par) throws ClassNotFoundException, SQLException
  {
       Connection c = null;
      Statement stmt = null;
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:resources//mechanic_universal_New.db3");
      c.setAutoCommit(true);
      stmt = c.createStatement();
      int value=par.getQuantaty();
      String query="DELETE FROM Parts WHERE id='"+par.getId()+"';";
      stmt.executeUpdate(query);    
      stmt.close();
      c.close();
  }
  //fill in part stock
  public static Collection readPartStock() throws SQLException, ClassNotFoundException
  {
      Collection<Part> collected=new ArrayList<>();
    
      Connection c = null;
      Statement stmt = null;
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:resources//mechanic_universal_New.db3");
      c.setAutoCommit(false);
      stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM Parts;");
      while ( rs.next() ) {
         int id = rs.getInt("ID");
         String  name = rs.getString("Name");
         String  description = rs.getString("Description");
         int stock  = rs.getInt("Stock");
         double price  = rs.getDouble("Price");
         String type = rs.getString("Type");
         collected.add(new Part(id, name, description, stock, price, Boolean.FALSE,type));
         
      }
      rs.close();
      stmt.close();
      c.close();
     //return collection for filling
      return collected;
  
  }
  //add part to stock
  public void addPartStock(Part par) throws ClassNotFoundException, SQLException
  {
      Connection c = null;
      Statement stmt = null;
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:resources//mechanic_universal_New.db3");
      c.setAutoCommit(true);
      PreparedStatement pstmt = c.prepareStatement("INSERT INTO Parts (Name, Description, Type, Price, Stock)"+
                            " VALUES (?,?,?,?,?);");
      pstmt.setString(1, par.getName());
      pstmt.setString(2, par.getDesc());
      pstmt.setString(3, par.getType());
      pstmt.setDouble(4, par.getPrice());
      pstmt.setInt(5, par.getQuantaty());
      pstmt.executeUpdate(); 
      pstmt.close();
      c.close();
     
  }
  //update part stock witht the new quantity after basket interface has been used
   public void updatePartStock(Part par) throws ClassNotFoundException, SQLException
  {
      Connection c = null;
      Statement stmt = null;
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:resources//mechanic_universal_New.db3");
      c.setAutoCommit(true);
      PreparedStatement pstmt = c.prepareStatement("UPDATE Parts SET Stock= ? WHERE id= ?;");
      pstmt.setInt(1, par.getQuantaty());
      pstmt.setInt(2, par.getId());
      pstmt.executeUpdate();
      c.close();
     
  }
   //method to assing to the model's id the ID from the database
   public int idDB(String name) throws ClassNotFoundException, SQLException
   {
       Connection c = null;
      Statement stmt = null;
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:resources//mechanic_universal_New.db3");
      c.setAutoCommit(true);
      stmt = c.createStatement();
      PreparedStatement pstmt = c.prepareStatement("SELECT ID FROM Parts WHERE Name= ?;");
      pstmt.setString(1, name);
      ResultSet rs=pstmt.executeQuery();
      int identify = rs.getInt(1);
      rs.close();
      stmt.close();
      pstmt.close();
      c.close();
      return identify;
      
   }
   //assign database id of a supplier to supplier's model on the interfae
   public int idDBSupplier(String name) throws ClassNotFoundException, SQLException
   {
       Connection c = null;
      Statement stmt = null;
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:resources//mechanic_universal_New.db3");
      c.setAutoCommit(true);
      stmt = c.createStatement();
      PreparedStatement pstmt = c.prepareStatement("SELECT ID FROM Supplier WHERE Name= ?;");
      pstmt.setString(1, name);
      ResultSet rs=pstmt.executeQuery();
      int identify = rs.getInt(1);
      rs.close();
      stmt.close();
      pstmt.close();
      c.close();
      return identify;
      
   }
   //assign database id of a pending parts to pending part's model on the interfae
   public int idDBPending(String name) throws ClassNotFoundException, SQLException
   {
       Connection c = null;
      Statement stmt = null;
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:resources//mechanic_universal_New.db3");
      c.setAutoCommit(true);
      stmt = c.createStatement();
      PreparedStatement pstmt = c.prepareStatement("SELECT ID FROM Part_Orders WHERE Name= ?;");
      pstmt.setString(1, name);
      ResultSet rs=pstmt.executeQuery();
      int identify = rs.getInt(1);
      rs.close();
      stmt.close();
      pstmt.close();
      c.close();
      return identify;
      
   }
   // fill in supplier table
   public static Collection<Supplier> readSupplierList() throws ClassNotFoundException, SQLException
   {
       Collection<Supplier> collected=new ArrayList<>();
    
      Connection c = null;
      Statement stmt = null;
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:resources//mechanic_universal_New.db3");
      c.setAutoCommit(false);
      stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM Supplier;");
      while ( rs.next() ) {
         int id=rs.getInt("ID");
         String  name = rs.getString("Name");
         String  description = rs.getString("Description");
         String  Address = rs.getString("Address");
         String  Phone = rs.getString("Phone");
         String  Email = rs.getString("Email");
         collected.add(new Supplier( id,name, description, Address, Phone, Email, false));
         
      }
      rs.close();
      stmt.close();
      c.close();
     
      return collected;
   }
   //commit user's changes to existing suppliers to the database
   public void updateSupplierList(Supplier sup) throws ClassNotFoundException, SQLException
  {
      Connection c = null;
      Statement stmt = null;
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:resources//mechanic_universal_New.db3");
      c.setAutoCommit(true);
      PreparedStatement pstmt = c.prepareStatement("UPDATE Supplier SET Name= ?, Description= ?, Address= ?, Phone= ?, Email= ? WHERE ID = ?;");
      pstmt.setString(1, sup.getName());
      pstmt.setString(2, sup.getDesc());
      pstmt.setString(3, sup.getAddress());
      pstmt.setString(4, sup.getPhone());
      pstmt.setString(5, sup.getEmail());
      pstmt.setInt(6, sup.getID());
      pstmt.executeUpdate();   
      pstmt.close();
      c.close();
     
  }
   //add new supplier to the database
   public void addSupplier(Supplier sup) throws ClassNotFoundException, SQLException
  {
      Connection c = null;
      Statement stmt = null;
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:resources//mechanic_universal_New.db3");
      c.setAutoCommit(true);
      PreparedStatement pstmt = c.prepareStatement("INSERT INTO Supplier (Name, Description, Address, Phone, Email) "+
              "VALUES (?,?,?,?,?);");
      pstmt.setString(1, sup.getName());
      pstmt.setString(2, sup.getDesc());
      pstmt.setString(3, sup.getAddress());
      pstmt.setString(4, sup.getPhone());
      pstmt.setString(5, sup.getEmail());
      pstmt.execute();   
      pstmt.close();
      c.close();
     
  }
   //remove supplier from the database
   public void removeSupplier(Supplier sup)throws ClassNotFoundException, SQLException
   {
      Connection c = null;
      Statement stmt = null;
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:resources//mechanic_universal_New.db3");
      c.setAutoCommit(true);
      stmt = c.createStatement();
      PreparedStatement pstmt = c.prepareStatement("DELETE FROM Supplier WHERE id= ?;");
      pstmt.setInt(1, sup.getID());
      pstmt.executeUpdate();    
      pstmt.close();
      c.close();
   
   }
  //add a pending part to the database
  public void addPending(OrderPart par) throws ClassNotFoundException, SQLException
  {
  Connection c = null;
      Statement stmt = null;
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:resources//mechanic_universal_New.db3");
      c.setAutoCommit(true);
      stmt = c.createStatement();
      PreparedStatement pstmt = c.prepareStatement("INSERT INTO Part_Orders (Name, Supplier, Quantity, Price, Date_Expected) VALUES (?, ?, ?, ?, ?);");
      pstmt.setString(1, par.getName());
      pstmt.setString(2, par.getSupplier());
      pstmt.setInt(3, par.getQuantaty());
      pstmt.setDouble(4, par.getPrice());
      pstmt.setString(5, par.getDate()); 
      pstmt.execute();
      stmt.close();
      c.close();
  
  }
  //fill in teh pending table
  public static Collection<OrderPart> readPendingList() throws ClassNotFoundException, SQLException
   {
       Collection<OrderPart> collected=new ArrayList<>();
    
      Connection c = null;
      Statement stmt = null;
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:resources//mechanic_universal_New.db3");
      c.setAutoCommit(false);
      stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM Part_Orders;");
      while ( rs.next() ) {
         int id=rs.getInt("ID");
         String  name = rs.getString("Name");
         String  Supplier = rs.getString("Supplier");
         int  Amount = rs.getInt("Quantity");
         double  Price = rs.getDouble("Price");
         String  Date = rs.getString("Date_Expected");
         collected.add(new OrderPart(id,name,"", Amount, Price, false,"",Supplier,Date));
         
      }
      rs.close();
      stmt.close();
      c.close();
     
      return collected;
   }
  //remove the pending part from the database
  public void removePending(OrderPart par)throws ClassNotFoundException, SQLException
   {
      Connection c = null;
      Statement stmt = null;
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:resources//mechanic_universal_New.db3");
      c.setAutoCommit(true);
      stmt = c.createStatement();
      PreparedStatement pstmt = c.prepareStatement("DELETE FROM Part_Orders WHERE id= ?;");
      pstmt.setInt(1, par.getId());
      pstmt.executeUpdate();    
      pstmt.close();
      c.close();
   
   }
  //check if a selected for removal part has been used in an existing booking
  public boolean checkIfUsed(int pID)throws ClassNotFoundException, SQLException
   {
      Connection c = null;
      int parID=pID;
      Boolean used=false;
      Statement stmt = null;
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:resources//mechanic_universal_New.db3");
      c.setAutoCommit(true);
      stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT PartsID FROM Service_Parts;");
      while ( rs.next() ) {
         int id=rs.getInt("PartsID");
         if(id==parID)
         {
             used=true;
             break;
         }
      }  
      stmt.close();
      c.close();
      return used;
   
   }
}
