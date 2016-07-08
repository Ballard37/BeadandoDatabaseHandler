package windows;

import java.awt.*;
import javax.swing.*;

import data.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import modell.*;

public class EditorWindow extends Window implements ActionListener {

    private boolean add;
    private TitkarnoUI t;   
    private Adatlap adat;    
    private Object[] data;
    private int colCount;
    
    Component[] forms;
    JPanel panel, panel2;
    JButton bOK, bCancel;

    /**
     * Konstruktor. Külön viselkedik ha a megadott Adatlap Munkalap Az első
     * rekord mindig az ID, ezért az nem megváltoztatható (kivéve Alkatrész)
     *
     * @param adat
     * @param t
     * @param add
     */
    public EditorWindow(Adatlap adat, TitkarnoUI t, boolean add) {
        this.add = add;
        this.t = t;
        this.adat = adat;
        this.panel = new JPanel(new SpringLayout());
        String[] cn = adat.getColumnNames();
        this.colCount = cn.length;
        data = adat.getObject();
        for (int i = 0; i < cn.length; i++) {
            cn[i] += ": ";
        }

        if (adat instanceof Munkalap) {
            panel.add(new JLabel(cn[0]));
            JTextField id = new JTextField(data[0].toString());
            id.setEditable(false);
            panel.add(id);
            for (int i = 1; i < 5; i++) {
                panel.add(new JLabel(cn[i]));
                panel.add(new JTextField(data[i].toString()));
            }
            panel.add(new JLabel(cn[5]));
            String[] strmunkatars = new String[DataReader.getMunkatarsak().size() + 1];
            for (int i = 0; i < DataReader.getMunkatarsak().size(); i++) {
                strmunkatars[i] = DataReader.getMunkatarsak().get(i).getNev();
            }
            strmunkatars[strmunkatars.length - 1] = "-";
            JComboBox munkatars = new JComboBox(strmunkatars);
            munkatars.setSelectedItem(((Munkalap) adat).getMunkatars().getNev());
            panel.add(munkatars);
            panel.add(new JLabel(cn[6]));
            JTextField d = new JTextField(data[6].toString());
            d.setEditable(false);
            panel.add(d);
            panel.add(new JLabel(cn[7]));
            String[] strallapot = new String[Allapot.values().length];
            int ind = 0;
            for (Allapot a : Allapot.values()) {
                strallapot[ind] = a.formazott;
                ind++;
            }
            JComboBox allapot = new JComboBox(strallapot);
            allapot.setSelectedItem(((Munkalap) adat).getAllapot().formazott);
            if(t.getClass() == SzereloUI.class)
                allapot.setEnabled(false);
            panel.add(allapot);
            panel.add(new JLabel(cn[8]));
            panel.add(new JTextField(data[8].toString()));
        } else {
            for (int i = 0; i < colCount; i++) {
                panel.add(new JLabel(cn[i]));
                if (i == 0 && adat.getClass() != Alkatresz.class) {
                    JTextField tf = new JTextField(data[i].toString());
                    tf.setEditable(false);
                    panel.add(tf);
                } else {
                    panel.add(new JTextField(data[i].toString()));
                }
            }
        }
        forms = panel.getComponents();

        bOK = new JButton("OK");
        bCancel = new JButton("Mégse");
        if (adat.getClass() == Munkalap.class) {
            colCount--;
        }
        SpringUtilities.makeCompactGrid(panel,
                colCount, 2, //rows, cols
                6, 6, //initX, initY
                6, 6);       //xPad, yPad       
        panel2 = new JPanel(new FlowLayout());
        bOK.addActionListener(this);
        bCancel.addActionListener(this);
        panel2.add(bOK);
        panel2.add(bCancel);
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(panel2, BorderLayout.SOUTH);
        super.pack();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == bOK) {
            ArrayList<String> strforms = new ArrayList<>();
            for (Component c : forms) {
                if (c instanceof JTextField) {
                    strforms.add(((JTextField) c).getText());
                } else if (c instanceof JComboBox) {
                    strforms.add(((JComboBox) c).getSelectedItem().toString());
                }
            }
            String hiba = Modell.changeData(adat, strforms, add);
            boolean b = true; //Dobott-e changeData hibát
            if (!hiba.equals("")) {
                JOptionPane.showMessageDialog(this,
                        hiba,
                        "Hiba!",
                        JOptionPane.ERROR_MESSAGE);
                b = false;
            }
            t.refresh();
            if (b) {
                super.dispose();
            }
        } else if (ae.getSource() == bCancel) {
            super.dispose();
        }
    }
}

/**
 * Java hivatalos tutorial oldaláról bemásolva segít az űrlap létrehozásában
 *
 * @author Oracle
 */

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 
class SpringUtilities {

    /**
     * A debugging utility that prints to stdout the component's minimum,
     * preferred, and maximum sizes.
     */
    public static void printSizes(Component c) {
        System.out.println("minimumSize = " + c.getMinimumSize());
        System.out.println("preferredSize = " + c.getPreferredSize());
        System.out.println("maximumSize = " + c.getMaximumSize());
    }

    /**
     * Aligns the first <code>rows</code> * <code>cols</code> components of
     * <code>parent</code> in a grid. Each component is as big as the maximum
     * preferred width and height of the components. The parent is made just big
     * enough to fit them all.
     *
     * @param rows number of rows
     * @param cols number of columns
     * @param initialX x location to start the grid at
     * @param initialY y location to start the grid at
     * @param xPad x padding between cells
     * @param yPad y padding between cells
     */
    public static void makeGrid(Container parent, int rows, int cols, int initialX, int initialY,
            int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout) parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeGrid must use SpringLayout.");
            return;
        }

        Spring xPadSpring = Spring.constant(xPad);
        Spring yPadSpring = Spring.constant(yPad);
        Spring initialXSpring = Spring.constant(initialX);
        Spring initialYSpring = Spring.constant(initialY);
        int max = rows * cols;

        // Calculate Springs that are the max of the width/height so that all
        // cells have the same size.
        Spring maxWidthSpring = layout.getConstraints(parent.getComponent(0)).getWidth();
        Spring maxHeightSpring = layout.getConstraints(parent.getComponent(0)).getWidth();
        for (int i = 1; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(parent.getComponent(i));

            maxWidthSpring = Spring.max(maxWidthSpring, cons.getWidth());
            maxHeightSpring = Spring.max(maxHeightSpring, cons.getHeight());
        }

        // Apply the new width/height Spring. This forces all the
        // components to have the same size.
        for (int i = 0; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(parent.getComponent(i));

            cons.setWidth(maxWidthSpring);
            cons.setHeight(maxHeightSpring);
        }

        // Then adjust the x/y constraints of all the cells so that they
        // are aligned in a grid.
        SpringLayout.Constraints lastCons = null;
        SpringLayout.Constraints lastRowCons = null;
        for (int i = 0; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(parent.getComponent(i));
            if (i % cols == 0) { // start of new row
                lastRowCons = lastCons;
                cons.setX(initialXSpring);
            } else { // x position depends on previous component
                cons.setX(Spring.sum(lastCons.getConstraint(SpringLayout.EAST), xPadSpring));
            }

            if (i / cols == 0) { // first row
                cons.setY(initialYSpring);
            } else { // y position depends on previous row
                cons.setY(Spring.sum(lastRowCons.getConstraint(SpringLayout.SOUTH), yPadSpring));
            }
            lastCons = cons;
        }

        // Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH, Spring.sum(Spring.constant(yPad), lastCons
                .getConstraint(SpringLayout.SOUTH)));
        pCons.setConstraint(SpringLayout.EAST, Spring.sum(Spring.constant(xPad), lastCons
                .getConstraint(SpringLayout.EAST)));
    }

    /* Used by makeCompactGrid. */
    private static SpringLayout.Constraints getConstraintsForCell(int row, int col, Container parent,
            int cols) {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    }

    /**
     * Aligns the first <code>rows</code> * <code>cols</code> components of
     * <code>parent</code> in a grid. Each component in a column is as wide as
     * the maximum preferred width of the components in that column; height is
     * similarly determined for each row. The parent is made just big enough to
     * fit them all.
     *
     * @param rows number of rows
     * @param cols number of columns
     * @param initialX x location to start the grid at
     * @param initialY y location to start the grid at
     * @param xPad x padding between cells
     * @param yPad y padding between cells
     */
    public static void makeCompactGrid(Container parent, int rows, int cols, int initialX,
            int initialY, int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout) parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
            return;
        }

        // Align all cells in each column and make them the same width.
        Spring x = Spring.constant(initialX);
        for (int c = 0; c < cols; c++) {
            Spring width = Spring.constant(0);
            for (int r = 0; r < rows; r++) {
                width = Spring.max(width, getConstraintsForCell(r, c, parent, cols).getWidth());
            }
            for (int r = 0; r < rows; r++) {
                SpringLayout.Constraints constraints = getConstraintsForCell(r, c, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }

        // Align all cells in each row and make them the same height.
        Spring y = Spring.constant(initialY);
        for (int r = 0; r < rows; r++) {
            Spring height = Spring.constant(0);
            for (int c = 0; c < cols; c++) {
                height = Spring.max(height, getConstraintsForCell(r, c, parent, cols).getHeight());
            }
            for (int c = 0; c < cols; c++) {
                SpringLayout.Constraints constraints = getConstraintsForCell(r, c, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }

        // Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH, y);
        pCons.setConstraint(SpringLayout.EAST, x);
    }
}
