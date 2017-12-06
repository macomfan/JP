/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainUI;

import JPWord.Data.IWord;
import JPWord.Synchronizer.IController;
import JPWord.Synchronizer.ILogging;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Iterator;
import JPWord.Synchronizer.Sync;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author u0151316
 */
public class DatabasePanel extends javax.swing.JPanel {

    class TestClass extends Thread {

        public DatabasePanel p_ = null;

        @Override
        public void run() {
            try {
//                DefaultTableModel dtm = (DefaultTableModel) jLogTable.getModel();
//                IController c = Sync.getInstance().runAsMaster();
//                ILogging logging = c.getLogging();
//                while (c.isClosed() == false) {
//                    String msg = logging.pop();
//                    if (msg != null) {
//                        dtm.addRow(new Object[]{"", msg});
//                    }
//                    Thread.sleep(1);
//                }
            } catch (Exception e) {
            }

        }
    }

    /**
     * Creates new form DatabasePanel
     */
    public DatabasePanel() {
        initComponents();

        jtxtFilename.setText(Database.getInstance().getFilename());
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jtxtFilename = new javax.swing.JTextField();
        jbtnRunAsMaster = new javax.swing.JButton();
        jbtnBackup = new javax.swing.JButton();
        jbtnImport = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jlistLog = new MainUI.JLogList();

        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setMinimumSize(new java.awt.Dimension(680, 400));
        setName(""); // NOI18N

        jLabel1.setText("Filename");

        jtxtFilename.setEditable(false);
        jtxtFilename.setText("jTextField1");

        jbtnRunAsMaster.setText("Run as master");
        jbtnRunAsMaster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnRunAsMasterActionPerformed(evt);
            }
        });

        jbtnBackup.setText("Backup");
        jbtnBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBackupActionPerformed(evt);
            }
        });

        jbtnImport.setText("Import...");
        jbtnImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnImportActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(jlistLog);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtxtFilename, javax.swing.GroupLayout.PREFERRED_SIZE, 576, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jbtnRunAsMaster)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbtnBackup)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbtnImport)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtxtFilename, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnRunAsMaster)
                    .addComponent(jbtnBackup)
                    .addComponent(jbtnImport))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBackupActionPerformed
        // TODO add your handling code here:
        String filename = Database.getInstance().getFilename();
        Path p = FileSystems.getDefault().getPath(filename);
        String s1 = p.getParent().toString();
        String s2 = p.getRoot().toString();
        String s3 = p.getFileName().toString();
    }//GEN-LAST:event_jbtnBackupActionPerformed

    private void jbtnRunAsMasterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnRunAsMasterActionPerformed
        // TODO add your handling code here:
        //Sync.getInstance().runAsMaster();
        TestClass t = new TestClass();
        t.p_ = this;
        t.start();
    }//GEN-LAST:event_jbtnRunAsMasterActionPerformed

    private void jbtnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnImportActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        String filename = Database.getInstance().getFilename();
        Path p = FileSystems.getDefault().getPath(filename);
        chooser.setCurrentDirectory(new File(p.toString()));
        int res = chooser.showOpenDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            File file = chooser.getSelectedFile();
            List<String[]> items = new LinkedList<>();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                String strItems[] = line.split("\\|");
                if (strItems[0].equals("[DUP]")) {
                    continue;
                }
                if (strItems.length < 5) {
                    //error
                    items.add(new String[]{""});
                } else {
                    String w = strItems[0];
                    String k = "";
                    if (strItems[1].equals("")) {
                        k = strItems[0];
                    } else {
                        k = strItems[1];
                    }
                    if (strItems.length > 5) {
                        items.add(new String[]{w, k, strItems[2], strItems[3], strItems[4], strItems[5]});
                    } else {
                        items.add(new String[]{w, k, strItems[2], strItems[3], strItems[4]});
                    }
                }
            }
            reader.close();
            ImportDialog fd = new ImportDialog(null, true, items);
            fd.setLocation(this.getLocationOnScreen());
            fd.setVisible(true);
            if (!fd.isOK_) {
                return;
            }
            for (String[] item : items) {
                if (item.length < 5 || item.length > 5) {
                    continue;
                    //error
                }
                if (item[0].equals("[DUP]") || item.length != 5) {
                    continue;
                }
                JPWord.Data.IWord word = null;
                for (IWord word1 : Database.getInstance().getDatabase().getWords()) {
                    
                }
                jlistLog.addLog(JLogList.LogType.NORMAL, item[1]);
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jbtnImportActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnBackup;
    private javax.swing.JButton jbtnImport;
    private javax.swing.JButton jbtnRunAsMaster;
    private MainUI.JLogList jlistLog;
    private javax.swing.JTextField jtxtFilename;
    // End of variables declaration//GEN-END:variables
}
