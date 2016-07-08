package data;

import java.sql.*;
import java.util.*;

public class DataReader {

    static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";
    static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:XE";

    static final String USER = "system";
    static final String PASS = "abzi46";

    private final static List<Alkatresz> alkatreszek = new ArrayList<>();
    private final static List<Felh_Alkatresz> felh_alkatreszek = new ArrayList<>();
    private final static List<Munkalap> munkalapok = new ArrayList<>();
    private final static List<Munkatars> munkatarsak = new ArrayList<>();

    /**
     * Munkatars ID => Munkatars osztaly
     *
     * @param id
     * @param munkatarsak
     * @return
     */
    public static Munkatars munkatarsConverter(int id, List<Munkatars> munkatarsak) {
        for (Munkatars m : munkatarsak) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    public static List<Alkatresz> getAlkatreszek() {
        return alkatreszek;
    }

    public static List<Felh_Alkatresz> getFelh_alkatreszek() {
        return felh_alkatreszek;
    }

    public static List<Munkalap> getMunkalapok() {
        return munkalapok;
    }

    public static List<Munkatars> getMunkatarsak() {
        return munkatarsak;
    }

    /**
     *  Beolvassa s által kiválasztott táblát
     *  stmt-hez kapcsolat előfeltétel!
     * @return
     */
    private static void readTable(String s, Statement stmt) throws SQLException {
        ResultSet rs = null;
        switch (s) {
            case "ALKATRESZ":
                alkatreszek.clear();
                rs = stmt.executeQuery("SELECT * FROM ALKATRESZ ORDER BY CIKKSZAM");
                while (rs.next()) {
                    //Retrieve by column name
                    int cikkszam = rs.getInt("CIKKSZAM");
                    int egysegar = rs.getInt("EGYSEGAR");
                    int mennyiseg = rs.getInt("MENNYISEG");
                    String nev = rs.getString("NEV");
                    alkatreszek.add(new Alkatresz(nev, cikkszam, egysegar, mennyiseg));
                }
                break;
            case "FELH_ALKATRESZ":
                felh_alkatreszek.clear();
                rs = stmt.executeQuery("SELECT * FROM FELH_ALKATRESZ ORDER BY ID");
                while (rs.next()) {
                    //Retrieve by column name
                    int cikkszam = rs.getInt("CIKKSZAM");
                    int munkalap = rs.getInt("MUNKALAP");
                    int mennyiseg = rs.getInt("MENNYISEG");
                    int id = rs.getInt("ID");
                    felh_alkatreszek.add(new Felh_Alkatresz(cikkszam, mennyiseg, munkalap, id));
                }
                break;
            case "MUNKATARS":
                munkatarsak.clear();
                rs = stmt.executeQuery("SELECT * FROM MUNKATARS ORDER BY ID");
                while (rs.next()) {
                    String nev = rs.getString("NEV");
                    String cim = rs.getString("CIM");
                    long telefon = rs.getLong("TELEFON");
                    int oradij = rs.getInt("ORADIJ");
                    int id = rs.getInt("ID");
                    munkatarsak.add(new Munkatars(nev, cim, telefon, oradij, id));
                }
                break;
            case "MUNKALAP":
                munkalapok.clear();
                rs = stmt.executeQuery("SELECT * FROM MUNKALAP ORDER BY ID");
                while (rs.next()) {
                    String nev = rs.getString("NEV");
                    String cim = rs.getString("CIM");
                    String rendszam = rs.getString("RENDSZAM");
                    String leiras = rs.getString("LEIRAS");
                    int intmunkatars = rs.getInt("MUNKATARS");
                    Munkatars m = munkatarsConverter(intmunkatars, munkatarsak);
                    java.util.Date date = rs.getDate("DATUM");
                    String strallapot = rs.getString("ALLAPOT");
                    Allapot allapot = Allapot.valueOf(strallapot);
                    int id = rs.getInt("ID");
                    int munkaora = rs.getInt("MUNKAORA");
                    munkalapok.add(
                            new Munkalap(nev, cim, rendszam, leiras, m, date, allapot, id, munkaora));
                }
                break;
            default:
                throw new SQLException("Invalid table name");
        }
        rs.close();
    }

    /**
     * kapcsolódik az adatbázishoz és lekéri a négy táblát
     * @return
     */
    public static void run() {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            readTable("ALKATRESZ", stmt);
            readTable("FELH_ALKATRESZ", stmt);
            readTable("MUNKATARS", stmt);
            readTable("MUNKALAP", stmt);
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            System.out.println(se);
        } catch (Exception e) {
            System.out.println(e);
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
