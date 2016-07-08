package data;

import java.util.ArrayList;

public class Alkatresz extends Adatlap{
    private String nev;
    private int cikkszam, egysegar, mennyiseg;

    public Alkatresz(String nev, int cikkszam, int egysegar, int mennyiseg) {
        this.nev = nev;
        this.cikkszam = cikkszam;
        this.egysegar = egysegar;
        this.mennyiseg = mennyiseg;
    }
    
    public Alkatresz(ArrayList<String> str){
        this.cikkszam = Integer.parseInt(str.get(0));
        this.nev = str.get(1);
        this.egysegar = Integer.parseInt(str.get(2));
        this.mennyiseg = Integer.parseInt(str.get(3));
        
    }
    
    /**Sablon új EditorWindownak
     * @return  */
    public static Alkatresz getSablon(){
        return new Alkatresz("",0,0,0);
    }
    
    @Override
    public String[] getColumnNames() {
        String[] columnNames = {
        "Cikkszám","Név", "Egységár","Mennyiség"};
        return columnNames;
    }  
    
    @Override
    public Object[] getObject() {
        Object[] obj = 
        {cikkszam, nev,  egysegar, mennyiseg};
        return obj;
    }
    
    @Override
    public String toString(){
        return "'"+cikkszam+"','"+ nev +"','"+egysegar+"','"+mennyiseg+"'";
    }
    /** Megnézi hogy a cikkszám szerepel-e már az adatbázisban
     * @return  */
    public static boolean checkCollision(int csz){
        for(Alkatresz a : DataReader.getAlkatreszek()){
            if(a.getCikkszam() == csz)
                return true;
        }
        return false;
    }
    
    public String getNev() {
        return nev;
    }

    public int getCikkszam() {
        return cikkszam;
    }

    public int getEgysegar() {
        return egysegar;
    }

    public int getMennyiseg() {
        return mennyiseg;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public void setCikkszam(int cikkszam) {
        this.cikkszam = cikkszam;
    }

    public void setEgysegar(int egysegar) {
        this.egysegar = egysegar;
    }

    public void setMennyiseg(int mennyiseg) {
        this.mennyiseg = mennyiseg;
    }
    
}
