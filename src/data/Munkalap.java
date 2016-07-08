package data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Munkalap extends Adatlap{
    private int id,munkaora;
    private String nev,cim,rendszam,problema;
    private Munkatars munkatars;
    private Date datum;
    private Allapot allapot;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public Munkalap(String nev, String cim, String rendszam, String problema,
            Munkatars munkatars, Date datum, Allapot allapot, int id, int munkaora) {
        this.nev = nev;
        this.cim = cim;
        this.rendszam = rendszam;
        this.problema = (problema == null) ? "-" : problema;
        this.munkatars = (munkatars == null) ? 
                new Munkatars("-","-",0,0,0) : munkatars;
        this.datum = datum;
        this.allapot = allapot;
        this.id = id;       
        this.munkaora = munkaora;
    }
    
    public Munkalap(ArrayList<String> str) throws ParseException{
        this.id = Integer.parseInt(str.get(0));
        this.nev = str.get(1);
        this.cim = str.get(2);
        this.rendszam = str.get(3);
        this.problema = str.get(4);
        this.munkatars = str.get(5).equals("-") ? new Munkatars("-","-",0,0,0) : Munkatars.munkatarsBackConverter(str.get(5));
        this.datum = sdf.parse(str.get(6));
        Allapot all = null;
        for(Allapot a : Allapot.values()){
            if(a.formazott.equals(str.get(7))){
                all = a;
                break;
            }
        }
        this.allapot = all;
        this.munkaora = Integer.parseInt(str.get(8));
    }
    
    /**
     * Kiszámolja a munkalaphoz Felh_Alkatresz-ben rendelt alkatrészek értékének összegét
     * @return 
     */
    private int countAr(){
        int c =0;
        for(Felh_Alkatresz f: DataReader.getFelh_alkatreszek()){
            if(f.getMunkalap() == id){
                c+= (f.getMennyiseg() * f.getAr());
                break;
            }
        }
         c+= (munkatars.getOradij() * munkaora);
        return c;
    }
    
    @Override
    public String[] getColumnNames() {
        String[] columnNames = {
        "ID", "Név", "Cím","Rendszám","Leírás","Munkatárs", "Dátum", "Állapot", "Munkaórák", "Teljes ár"};
        return columnNames;
    }  
    
     @Override
    public String toString(){
        return "'"+nev+"','"+ cim +"','"+rendszam+"','"+problema +"','"+munkatars.getId()+"', TO_DATE('"+sdf.format(datum)+"', 'YYYY-MM-DD') ,'"+allapot.name()+"','"+id+"','"+ munkaora+"'";
    }
     
    @Override
    public Object[] getObject() {
        Object[] obj = 
        {id, nev, cim, rendszam, problema,munkatars.getNev(), sdf.format(datum), allapot.formazott, munkaora, countAr()};
        return obj;
    }
    /** Sablon új EditorWindownak
     * @return  */
    public static Munkalap getSablon(){       
        return new Munkalap("","","","",null,new Date(),Allapot.FELDOLGOZANDO,generateId(),0);
    }
    /** Új, nem foglalt ID-t generál a sablonnak
     * @return  */
    private static int generateId(){
        int max = -1;
        for(Munkalap m : DataReader.getMunkalapok()){
            if(m.getId() > max)
                max = m.getId();
        }
        return max+1;
    }
    public String getNev() {
        return nev;
    }

    public String getCim() {
        return cim;
    }

    public String getRendszam() {
        return rendszam;
    }

    public String getProblema() {
        return problema;
    }

    public Munkatars getMunkatars() {
        return munkatars;
    }

    public Date getDatum() {
        return datum;
    }

    public Allapot getAllapot() {
        return allapot;
    }

    public int getId() {
        return id;
    }

    public int getMunkaora() {
        return munkaora;
    }

    public void setMunkaora(int munkaora) {
        this.munkaora = munkaora;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public void setCim(String cim) {
        this.cim = cim;
    }

    public void setRendszam(String rendszam) {
        this.rendszam = rendszam;
    }

    public void setProblema(String problema) {
        this.problema = problema;
    }

    public void setMunkatars(Munkatars munkatars) {
        this.munkatars = munkatars;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public void setAllapot(Allapot allapot) {
        this.allapot = allapot;
    }

    public void setId(int id) {
        this.id = id;
    }
}
