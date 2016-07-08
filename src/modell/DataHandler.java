package modell;

import java.sql.*;
import data.*;

public class DataHandler {

    static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";
    static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:XE";

    static final String USER = "system";
    static final String PASS = "abzi46";
    
    /**
     * SQL parancsot generál egy rekord felvételére a megfelelő Adatlap táblába
     * @param adat
     * @return 
     */
    public static  String sqlAddData(Adatlap adat) { 
        if(adat instanceof Munkalap){
            return "Insert into SYSTEM.MUNKALAP"
                + " (NEV,CIM,RENDSZAM,LEIRAS,MUNKATARS,DATUM,ALLAPOT,ID,MUNKAORA)"
                + " values ("+adat.toString()+")";
        }           
        else if (adat instanceof Munkatars) {
            return "Insert into SYSTEM.MUNKATARS"
                + " (NEV,CIM,TELEFON,ORADIJ,ID)"
                + " values ("+adat.toString()+")";

        } else if (adat instanceof Alkatresz) {
            return "Insert into SYSTEM.ALKATRESZ"
                + " (CIKKSZAM,NEV,EGYSEGAR,MENNYISEG)"
                + " values ("+adat.toString()+")";
        } else if (adat instanceof Felh_Alkatresz) {
            return "Insert into SYSTEM.FELH_ALKATRESZ"
                + " (CIKKSZAM,MENNYISEG,MUNKALAP,ID)"
                + " values ("+adat.toString()+")";
        } else {
            System.out.println("Az Adatlap típusa nem felismert!");
            return null;
        }
        
    }
    /**
     * SQL parancsot generál egy rekord tölésére a megfelelő Adatlap táblába
     * @param adat
     * @return 
     */
    
    public static  String sqlRemoveData(Adatlap adat) {
        if (adat instanceof Munkalap) {
                return "DELETE FROM SYSTEM.MUNKALAP WHERE ID = " + ((Munkalap) adat).getId();           
        } else if (adat instanceof Munkatars) {
            return "DELETE FROM SYSTEM.MUNKATARS WHERE ID = " + ((Munkatars) adat).getId();
        } else if (adat instanceof Alkatresz) {
            return "DELETE FROM SYSTEM.ALKATRESZ WHERE CIKKSZAM = " + ((Alkatresz) adat).getCikkszam();
        } else if (adat instanceof Felh_Alkatresz) {
            return "DELETE FROM SYSTEM.FELH_ALKATRESZ WHERE ID = " + ((Felh_Alkatresz) adat).getId();
        } else {
            System.out.println("instanceof nem működött");
        }
        return null;

    }
    /**
     * Végrehajt egy a paraméterben megadott SQL parancst az adatbázison
     * @param query 
     */
    public static void sqlConnect(String query) {
        Connection conn = null;
        Statement stmt = null;
        System.out.println(query);
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(query);
            rs = stmt.executeQuery("COMMIT");
            
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                System.out.println(se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                System.out.println(se);
            }
        }
    }
}
