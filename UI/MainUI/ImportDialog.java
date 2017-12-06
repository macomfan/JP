/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author u0151316
 */
public class ImportDialog extends javax.swing.JDialog {

    /**
     * Creates new form ImportDialog
     */
    public ImportDialog(java.awt.Frame parent, boolean modal, File file) {
        super(parent, modal);
        initComponents();

        DefaultTableModel dtm = new DefaultTableModel();
        dtm.addColumn("Word");
        dtm.addColumn("kana");
        dtm.addColumn("Type");
        dtm.addColumn("Mean");
        dtm.addColumn("Other");
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                String items[] = line.split("|");
                if (items[0].equals("[DUP]")) {
                    continue;
                }
                if (items.length < 5) {
                    //error
                    dtm.addRow(new Object[]{"!!!!!!", "!!!!!!", "!!!!!!", "!!!!!!", "!!!!!!"});
                } else {
                    String w = items[0];
                    String k = "";
                    if (items[1].equals("")) {
                        k = items[0];
                    } else {
                        k = items[1];
                    }
                    dtm.addRow(new Object[]{w, k, items[2], items[3], items[4]});
                    if (items.length > 5) {
                        dtm.addRow(new Object[]{w, k, items[2], items[3], items[4] + " ????????"});
                    } else {
                        dtm.addRow(new Object[]{w, k, items[2], items[3], items[4]});
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
        }

        jTable1.setModel(dtm);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(680, 447));
        setLayout(null);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        add(jScrollPane1);
        jScrollPane1.setBounds(10, 10, 620, 340);

        jButton1.setText("jButton1");
        add(jButton1);
        jButton1.setBounds(440, 360, 81, 25);

        jButton2.setText("jButton2");
        add(jButton2);
        jButton2.setBounds(540, 360, 81, 25);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}