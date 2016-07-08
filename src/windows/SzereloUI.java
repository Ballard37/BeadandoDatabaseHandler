package windows;

import data.*;
import java.awt.Dimension;
import modell.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class SzereloUI extends TitkarnoUI implements ActionListener {

    private JButton jChangeAllapot = new JButton();
    private JButton addAlk = new JButton("Alkatrész hozzáadása...");

    public SzereloUI() {
        super("Munkalap");
        mb.remove(szerkesztes);
        mSwitch.setText("Titkárnő nézet");
        gombok.removeAll();
        szerkesztes.removeAll();
        setChangeAllapot();

        jChangeAllapot.addActionListener(this);
        addAlk.addActionListener(this);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                setChangeAllapot();
            }
        });
        
    }

    /**
     * A jChangeAllapot és addAlk gombok más funkciót látnak el attól függően,
     * hogy a táblában melyik adat van kiválasztva
     */
    private void setChangeAllapot() {        
        int index = table.getSelectedRow();
        Allapot a = DataReader.getMunkalapok().get(index).getAllapot();
        boolean b = true;
        boolean b2 = false;
        if (a == Allapot.FELDOLGOZANDO || a == Allapot.ALKATRESZ) {
            jChangeAllapot = new JButton("Feldolgozás...");
            b2 = true;
        } else if (a == Allapot.FELDOLGOZOTT) {
            jChangeAllapot = new JButton("Elkezdés...");
        } else if (a == Allapot.FOLYAMATBAN) {
            jChangeAllapot = new JButton("Kifizetés...");
        } else {
            b = false;
        }
        gombok.setPreferredSize(new Dimension(180, 500));
        gombok.removeAll();
        gombok.add(mUj);
        if (b) {
            if(b2){
                gombok.add(Box.createRigidArea(new Dimension(0, 5)));
                gombok.add(addAlk);
            }
            gombok.add(Box.createRigidArea(new Dimension(0, 5)));
            jChangeAllapot.addActionListener(this);
            gombok.add(jChangeAllapot);
        }
        gombok.revalidate();
        gombok.repaint();
    }
    
    /**
     * Kiegészítve, hogy frissítés után is megtartsa változó tulajdonságát a jChaneAllapot és addAlk
     */
    @Override
    public void refresh(){
        super.refresh();
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                setChangeAllapot();
            }
        });       
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == mSwitch) {
            Window w = new TitkarnoUI(this.nezet);
            w.setVisible(true);
            this.dispose();
        } else if (ae.getSource() == mUj) {
            Adatlap adat = getNewAdatlap();
            EditorWindow editorWindow = new EditorWindow(adat, this, true);
            editorWindow.setVisible(true);
        } else if (ae.getSource() == addAlk) {
            Adatlap adat = getSelected();
            Felh_Alkatresz f = Felh_Alkatresz.getSablon();
            f.setMunkalap(((Munkalap) adat).getId());
            EditorWindow editorWindow = new EditorWindow(f, this, true);
            editorWindow.setVisible(true);
        } else if (ae.getSource() == jChangeAllapot) {
            Munkalap adat = (Munkalap) getSelected();
            String str = "";
            if (adat.getAllapot() == Allapot.ALKATRESZ || adat.getAllapot() == Allapot.FELDOLGOZANDO) {
                str = Modell.feldolgoz(adat);               
            } else if (adat.getAllapot() == Allapot.FELDOLGOZOTT) {
                Modell.folyamatba(adat);
            } else if (adat.getAllapot() == Allapot.FOLYAMATBAN) {
                str = Modell.kifizet(adat);
            } else{
                str = "A munkalap nem változtatható, mert Kifizetett!";
            }
            if(!str.equals(""))
                JOptionPane.showMessageDialog(this,
                        str,
                        "Hiba!",
                        JOptionPane.ERROR_MESSAGE);
            
            refresh();
        } else if (ae.getSource() == mRefresh) {
            refresh();

        }
    }
}