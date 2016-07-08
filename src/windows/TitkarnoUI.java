package windows;

import data.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import modell.Modell;

/**
 *
 * @author Ballard
 */
public class TitkarnoUI extends Window implements ActionListener {
    JPanel gombok = new JPanel();
    JTextField kereso = new JTextField("");
    JLabel jL = new JLabel("    Keresés: ");
    JTable table;
    JMenuBar mb = new JMenuBar();
    JButton mRefresh = new JButton("Frissítés");
    JMenu szerkesztes = new JMenu("Adatlapok...");
    JMenuItem m1 = new JMenuItem("Munkalapok");
    JMenuItem m2 = new JMenuItem("Munkatársak");
    JMenuItem m3 = new JMenuItem("Alkatrészek");
    JMenuItem m4 = new JMenuItem("Felhasznált alkatrészek");
    JButton mSwitch = new JButton("Szerelő nézet");
    JButton mUj = new JButton("Új...");
    JButton mSzerkesztes = new JButton("Szerkesztés...");
    JButton mTorles = new JButton("Törlés...");
    JPanel panel = new JPanel();
    
    String[] columnNames = null;
    Object[][] adatok = null;   
    String nezet;
     
    public TitkarnoUI(String nezet) {
        this.nezet = nezet;
        data.DataReader.run();
        getAdatok(nezet,"");      

        panel.setLayout(new BorderLayout());
        gombok.setLayout(new BoxLayout(gombok, BoxLayout.Y_AXIS));
        gombok.setAlignmentY(LEFT_ALIGNMENT);
        mUj.setMinimumSize(new Dimension(100, 150));
        gombok.add(mUj);
        gombok.add(Box.createRigidArea(new Dimension(0, 5)));
        gombok.add(mSzerkesztes);
        gombok.add(Box.createRigidArea(new Dimension(0, 5)));
        gombok.add(mTorles);
        gombok.add(Box.createRigidArea(new Dimension(0, 5)));

        panel.add(gombok, BorderLayout.EAST);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
       
        DocumentListener dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {               
                String s = kereso.getText();
                if(s == null)
                    s = "";
                //System.out.println(s);
                getAdatok(nezet,s);               
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                String s = kereso.getText();
               getAdatok(nezet,s);
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                String s = kereso.getText();
               getAdatok(nezet,s);
            }
        };
        
        kereso.getDocument().addDocumentListener(dl);
        mb.add(szerkesztes);
        mb.add(mRefresh);
        mb.add(mSwitch);
        mb.add(jL);
        mb.add(kereso);
        szerkesztes.add(m1);
        szerkesztes.add(m2);
        szerkesztes.add(m3);
        szerkesztes.add(m4);
        getContentPane().add(mb, BorderLayout.NORTH);
        getContentPane().add(panel, BorderLayout.CENTER);
        m1.addActionListener(this);
        m2.addActionListener(this);
        m3.addActionListener(this);
        m4.addActionListener(this);
        mUj.addActionListener(this);
        mSzerkesztes.addActionListener(this);
        mTorles.addActionListener(this);
        mRefresh.addActionListener(this);
        mSwitch.addActionListener(this);       
    }

    /**
     * Visszaadja a táblában épp kiválasztott elem Adatlapját
     * Kihasználja azt a szabályt, hogy az ID/Cikkszám mindig az első oszlop!
     * @return 
     */
    protected Adatlap getSelected() {
        int index = table.getSelectedRow();
        int id = (Integer) table.getModel().getValueAt(index, 0);
                
        switch (nezet) {
            case "Munkalap":
                for (Munkalap m : DataReader.getMunkalapok()) {
                    if(m.getId() == id)
                        return m;
                }
                break;
            case "Munkatars":
                for (Munkatars m : DataReader.getMunkatarsak()) {
                    if(m.getId() == id)
                        return m;
                }
                break;
            case "Alkatresz":
                for (Alkatresz m : DataReader.getAlkatreszek()) {
                    if(m.getCikkszam() == id)
                        return m;
                }
                break;
            case "Felh_Alkatresz":
               for (Felh_Alkatresz m : DataReader.getFelh_alkatreszek()) {
                    if(m.getId() == id)
                        return m;
                }
                break;
        }
        return null;
    }

    /**
     * megadja az adott nézethez tartozó Adatlapok sablonját (.getSablon() metódus)
     * @return 
     */
    protected Adatlap getNewAdatlap() {
        switch (nezet) {
            case "Munkalap":
                return Munkalap.getSablon();
            case "Munkatars":
                return Munkatars.getSablon();
            case "Alkatresz":
                return Alkatresz.getSablon();
            case "Felh_Alkatresz":
                return Felh_Alkatresz.getSablon();
        }
        return null;
    }

    /**
     * Az adott String alapján feltölti az adatok és columnNames változókat,
     * és ebből majd a konstruktor a táblát
     * @param n 
     */
    private void getAdatok(String n, String szuro) {
        switch (n) {
            case "Munkalap":
                ArrayList<Munkalap> adatokArray = new ArrayList<>();                
                for (int i = 0; i < DataReader.getMunkalapok().size(); i++) {
                    Munkalap m = DataReader.getMunkalapok().get(i);                   
                    if(m.getAllapot().formazott.contains(szuro) || 
                            m.getMunkatars().getNev().contains(szuro) || 
                            m.getNev().contains(szuro))
                        adatokArray.add(m);
                } 
                adatok = new Object[adatokArray.size()][];
                for (int i = 0; i < adatokArray.size(); i++) {
                    adatok[i] = adatokArray.get(i).getObject();
                }
                columnNames = DataReader.getMunkalapok().get(0).getColumnNames();
                break;
            case "Munkatars":
                adatok = new Object[DataReader.getMunkatarsak().size()][];
                columnNames = DataReader.getMunkatarsak().get(0).getColumnNames();
                for (int i = 0; i < DataReader.getMunkatarsak().size(); i++) {
                    adatok[i] = DataReader.getMunkatarsak().get(i).getObject();
                }
                break;
            case "Alkatresz":
                adatok = new Object[DataReader.getAlkatreszek().size()][];
                columnNames = DataReader.getAlkatreszek().get(0).getColumnNames();
                for (int i = 0; i < DataReader.getAlkatreszek().size(); i++) {
                    adatok[i] = DataReader.getAlkatreszek().get(i).getObject();
                }
                break;
            case "Felh_Alkatresz":
                adatok = new Object[DataReader.getFelh_alkatreszek().size()][];
                columnNames = DataReader.getFelh_alkatreszek().get(0).getColumnNames();
                for (int i = 0; i < DataReader.getFelh_alkatreszek().size(); i++) {
                    adatok[i] = DataReader.getFelh_alkatreszek().get(i).getObject();
                }
                break;
        }
        table = new JTable(adatok, columnNames);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().setSelectionInterval(0, 0);
        table.requestFocusInWindow();
        panel.removeAll();
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(gombok, BorderLayout.EAST);
        panel.revalidate();
        panel.repaint();
        nezet = n;
        if(nezet.equals("Munkalap"))
            kereso.setVisible(true);
        else
            kereso.setVisible(false);
    }

    /**
     * Frissíti az adatbázisból az adatokat, majd a táblát.
     */
    public void refresh() {
        data.DataReader.run();
        getAdatok(nezet,"");
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == m1) {
            getAdatok("Munkalap","");
        } else if (ae.getSource() == m2) {
            getAdatok("Munkatars","");
        } else if (ae.getSource() == m3) {
            getAdatok("Alkatresz","");
        } else if (ae.getSource() == m4) {
            getAdatok("Felh_Alkatresz","");
        } else if (ae.getSource() == mRefresh) {
            refresh();        
        } else if (ae.getSource() == mSwitch) {
            Window w = new SzereloUI();
            w.setVisible(true);
            this.dispose();            
        }else if (ae.getSource() == mUj) {
            Adatlap adat = getNewAdatlap();
            EditorWindow editorWindow = new EditorWindow(adat, this, true);
            editorWindow.setVisible(true);
        } else if (ae.getSource() == mSzerkesztes) {
            Adatlap adat = getSelected();
            EditorWindow editorWindow = new EditorWindow(adat, this, false);
            editorWindow.setVisible(true);
        } else if (ae.getSource() == mTorles) {
            Adatlap adat = getSelected();
            String s = Modell.removeData(adat);
            if(!s.equals(""))
                JOptionPane.showMessageDialog(this,
                        s,
                        "Hiba!",
                        JOptionPane.ERROR_MESSAGE);
            refresh();
        }

    }

}
