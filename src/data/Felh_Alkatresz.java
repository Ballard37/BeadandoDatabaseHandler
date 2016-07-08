package data;

import java.util.ArrayList;

public class Felh_Alkatresz extends Adatlap {
    private int cikkszam, mennyiseg,munkalap,id;

    public Felh_Alkatresz(int cikkszam, int mennyiseg, int munkalap, int id) {
        this.cikkszam = cikkszam;
        this.mennyiseg = mennyiseg;
        this.munkalap = munkalap;
        this.id = id;
    }
    
    public Felh_Alkatresz(ArrayList<String> str){
        this.id = Integer.parseInt(str.get(0));
        this.cikkszam = Integer.parseInt(str.get(1));
        this.mennyiseg = Integer.parseInt(str.get(2));
        this.munkalap = Integer.parseInt(str.get(3));
    }
    
    @Override
    public String[] getColumnNames() {
        String[] columnNames = {
        "ID", "Cikkszám", "Mennyiség", "MunkalapID"};
        return columnNames;
    }  
    
    @Override
    public Object[] getObject() {
        Object[] obj = 
        {id, cikkszam, mennyiseg, munkalap};
        return obj;
    }
    
    @Override
    public String toString(){
        return "'"+cikkszam+"','"+ mennyiseg +"','"+munkalap+"','"+id+"'";
    }
    /** Sablon új EditorWindownak
     * @return  */
    public static Felh_Alkatresz getSablon(){        
        return new Felh_Alkatresz(0,0,0,generateId());
    }
    /** Új, nem foglalt ID-t generál a sablonnak
     * @return  */
    private static int generateId(){
        int max = 0;
        for(Felh_Alkatresz m : DataReader.getFelh_alkatreszek()){
            if(m.getId() > max)
                max = m.getId();
        }
        return max+1;
    }
    /** Cikkszám => Egységár
     * Az alkatrésztábla segítségével
     * @return  */
    public int getAr(){
        for(Alkatresz a : DataReader.getAlkatreszek()){
            if(a.getCikkszam() == cikkszam){
                return a.getEgysegar();
            }
        }
        return 0;
    }
    
    public int getCikkszam() {
        return cikkszam;
    }

    public int getMennyiseg() {
        return mennyiseg;
    }

    public int getMunkalap() {
        return munkalap;
    }

    public int getId() {
        return id;
    }

    public void setCikkszam(int cikkszam) {
        this.cikkszam = cikkszam;
    }

    public void setMennyiseg(int mennyiseg) {
        this.mennyiseg = mennyiseg;
    }

    public void setMunkalap(int munkalap) {
        this.munkalap = munkalap;
    }

    public void setId(int id) {
        this.id = id;
    }
}
