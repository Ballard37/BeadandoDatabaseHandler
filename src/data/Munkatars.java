/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.ArrayList;

/**
 *
 * @author Ballard
 */
public class Munkatars extends Adatlap{
    private String nev, cim;
    private int oradij, id;
    private long tel;
    
    public Munkatars(String nev, String cim, long tel, int oradij, int id) {       
        this.nev = nev;
        this.cim = cim;
        this.tel = tel;
        this.oradij = oradij;
        this.id = id;
    }
    
    public Munkatars(ArrayList<String> str){
        this.id = Integer.parseInt(str.get(0));
        this.nev = str.get(1);
        this.cim = str.get(2);
        this.tel = Long.parseLong(str.get(3));
        this.oradij = Integer.parseInt(str.get(4));
    }
    
    private int getOrak(){
        int orak = 0;
        for( Munkalap m : DataReader.getMunkalapok()){
            if(m.getAllapot() != Allapot.KIFIZETETT && 
                    m.getMunkatars() == this){
                orak += m.getMunkaora();
            }
        }
        return orak;
    }
    
    public static Munkatars munkatarsBackConverter(String nev){
        for(Munkatars m : DataReader.getMunkatarsak()){
            if(m.getNev().equals(nev))
                return m;
        }
        return null;
    }
    @Override
    public String[] getColumnNames() {
        String[] columnNames = {"ID", "Név", "Cím", "Telefon" ,"Óradíj", "Hátralévő órák"};
        return columnNames;
    }  
    
    @Override
    public Object[] getObject() {
        String telefon = "0" + Long.toString(tel);
        Object[] obj = {id, nev, cim, telefon, oradij, getOrak()};
        return obj;
    }
    @Override
    public String toString(){
        return "'"+nev+"','"+ cim +"','0"+tel+"','"+oradij +"','"+id+"'";
    }
    /** Sablon új EditorWindownak
     * @return  */
    public static Munkatars getSablon(){       
        return new Munkatars("","",0,0,generateId());
    }
     /** Új, nem foglalt ID-t generál a sablonnak
     * @return  */
    private static int generateId(){
        int max = 0;
        for(Munkatars m : DataReader.getMunkatarsak()){
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

    public long getTel() {
        return tel;
    }

    public int getOradij() {
        return oradij;
    }
    
    public int getId(){
        return id;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public void setCim(String cim) {
        this.cim = cim;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public void setOradij(int oradij) {
        this.oradij = oradij;
    }  
}
