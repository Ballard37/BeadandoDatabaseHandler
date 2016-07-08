package windows;

import java.awt.event.*;

import javax.swing.*;

/**
 * Sablon  minden ablakhoz, minden ablak szülőosztálya
 * @author Ballard
 */
public class Window extends JFrame {
    public Window(){
        setTitle("Autoszerviz");
        setSize(1000,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                showExitConfirmation();
            }     
        });
    }
    /**
     * Kilépés megerősítése párbeszédablak Kilépés gomb megnyomásakor
     */
    private void showExitConfirmation(){
        int n = JOptionPane.showConfirmDialog(this, "Valóban ki akarsz lépni?",
                "Megerősítés", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            doUponExit();
        }       
    }

    private void doUponExit() {
        this.dispose();
    }
    
}
