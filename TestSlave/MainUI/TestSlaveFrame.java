/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainUI;

import JPWord.Data.Database;
import JPWord.Data.IWordDictionary;
import JPWord.File.DefaultFileReader;
import JPWord.File.DefaultFileWriter;
import JPWord.Synchronizer.ILogging;
import JPWord.Synchronizer.Sync;
import java.io.File;

/**
 *
 * @author u0151316
 */
public class TestSlaveFrame extends javax.swing.JFrame {

    class WorkerClass extends Thread {

        public TestSlaveFrame p_ = null;

        @Override
        public void run() {
            try {
                ILogging logging = Sync.getInstance().getLogging();
                while (true) {
                    String msg = logging.pop();
                    if (msg != null) {
                        String log = p_.jtxtLog.getText();
                        log += msg;
                        log += "\r\n";
                        p_.jtxtLog.setText(log);
                    }
                    Thread.sleep(10);
                }
            } catch (Exception e) {
            }

        }
    }

    /**
     * Creates new form TestSlaveFrame
     */
    public TestSlaveFrame() {
        initComponents();
        WorkerClass worker = new WorkerClass();
        worker.p_ = this;
        worker.start();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbtnStartSlave = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtxtLog = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jbtnStartSlave.setText("Start Slave");
        jbtnStartSlave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnStartSlaveActionPerformed(evt);
            }
        });

        jtxtLog.setColumns(20);
        jtxtLog.setRows(5);
        jScrollPane1.setViewportView(jtxtLog);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 673, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbtnStartSlave)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtnStartSlave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnStartSlaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnStartSlaveActionPerformed
        // TODO add your handling code here:
        File file = new File("C:\\Users\\u0151316\\Documents\\JP\\Dictionary_Slave.dat");
        DefaultFileReader reader = new DefaultFileReader(file);
        DefaultFileWriter writer = new DefaultFileWriter(file);
        IWordDictionary dict = Database.createWordDictionary(reader, writer);
        Sync.getInstance().startAsSlave(dict, Sync.Method.REBASE_FROM_MASTER);
        try {
            dict.save();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jbtnStartSlaveActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TestSlaveFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TestSlaveFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TestSlaveFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TestSlaveFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TestSlaveFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnStartSlave;
    private javax.swing.JTextArea jtxtLog;
    // End of variables declaration//GEN-END:variables
}
