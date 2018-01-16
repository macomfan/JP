/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainUI;

import JPWord.Data.Constant;
import JPWord.Data.Filter.FilterByTextTag;
import JPWord.Data.Filter.FilterByType;
import JPWord.Data.Filter.FilterHardOnly;
import JPWord.Data.Filter.SoftByNumberTag;
import JPWord.Data.ITag;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author u0151316
 */
public class FilterDialog extends javax.swing.JDialog {
    
    public boolean isOK_ = false;
    public FilterStruct filter_ = null;

    /**
     * Creates new form FilterDialog
     */
    public FilterDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        mainButtonGroup.add(jrbCls);
        mainButtonGroup.add(jrbHD);
        mainButtonGroup.add(jrbRD);
        mainButtonGroup.add(jrbSkill);
        mainButtonGroup.add(jrbType);
        DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
        for (String s : Constant.getInstance().getTypes())
        {
            dcbm.addElement(s);
        }
        jcbType.setModel(dcbm);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainButtonGroup = new javax.swing.ButtonGroup();
        jrbType = new javax.swing.JRadioButton();
        jrbCls = new javax.swing.JRadioButton();
        jrbHD = new javax.swing.JRadioButton();
        jrbSkill = new javax.swing.JRadioButton();
        jrbRD = new javax.swing.JRadioButton();
        jbtnOK = new javax.swing.JButton();
        jcbType = new javax.swing.JComboBox<>();
        jtxtCls = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jrbType.setText("Filter by type");

        jrbCls.setText("Filter by class");

        jrbHD.setText("Hard only");

        jrbSkill.setText("Sort by skill");

        jrbRD.setText("Sort by review date");

        jbtnOK.setText("OK");
        jbtnOK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbtnOKMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jrbType)
                            .addComponent(jrbCls))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcbType, 0, 86, Short.MAX_VALUE)
                            .addComponent(jtxtCls)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jrbRD)
                            .addComponent(jrbSkill))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jrbHD)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbtnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jrbType)
                    .addComponent(jcbType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jrbCls)
                    .addComponent(jtxtCls, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jrbRD)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jrbSkill)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jrbHD)
                    .addComponent(jbtnOK))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnOKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbtnOKMouseClicked
        // TODO add your handling code here:
        isOK_ = true;
        if (jrbCls.isSelected()) {
            String f = "Filter by class: " + jtxtCls.getText();
            filter_ = new FilterStruct(f, new FilterByTextTag("CLS", jtxtCls.getText()));
        } else if (jrbHD.isSelected()) {
            filter_ = new FilterStruct("Hard only", new FilterHardOnly());
        } else if (jrbRD.isSelected()) {
            filter_ = new FilterStruct("Sort by review date", new SoftByNumberTag(ITag.TAG_RD, null));
        } else if (jrbSkill.isSelected()) {
            filter_ = new FilterStruct("Sort by skill", new SoftByNumberTag(ITag.TAG_Skill, null));
        } else if (jrbType.isSelected()) {
            String f = "Filter by type: " + (String)jcbType.getSelectedItem();
            filter_ = new FilterStruct(f, new FilterByType((String)jcbType.getSelectedItem()));
        }
        this.setVisible(false);
    }//GEN-LAST:event_jbtnOKMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbtnOK;
    private javax.swing.JComboBox<String> jcbType;
    private javax.swing.JRadioButton jrbCls;
    private javax.swing.JRadioButton jrbHD;
    private javax.swing.JRadioButton jrbRD;
    private javax.swing.JRadioButton jrbSkill;
    private javax.swing.JRadioButton jrbType;
    private javax.swing.JTextField jtxtCls;
    private javax.swing.ButtonGroup mainButtonGroup;
    // End of variables declaration//GEN-END:variables
}
