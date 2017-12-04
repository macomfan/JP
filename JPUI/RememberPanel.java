/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPUI;

import JPWord.Data.Filter.IItemFilter;
import JPWord.Data.IMeaning;
import JPWord.Data.IWord;
import JPWord.Data.IWordDictionary;
import JPWord.Data.Filter.ItemGroup;
import JPWord.Data.Filter.SoftByNumberTag;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 *
 * @author u0151316
 */
public class RememberPanel extends javax.swing.JPanel {
    
    private IWordDictionary dictionary_ = null;
    private ItemGroup group_ = null;
    private IWord currentWord_ = null;
    
    private boolean showKanji_ = false;
    private boolean showKana_ = false;
    private boolean showImi_ = false;
    private boolean showRoma_ = false;
    private boolean showNote_ = false;
    private int currentNumber_ = 0;
    
    private List<FilterStruct> filterList_ = new LinkedList<>();

    /**
     * Creates new form RememberPanel
     */
    public RememberPanel() {
        initComponents();
        
    }
    
    public void initialize() {
        dictionary_ = Database.getInstance().getDatabase();
        group_ = new ItemGroup(dictionary_.getWords());
        filterList_.add(new FilterStruct("Sort by skill", new SoftByNumberTag("Skill")));
        filterList_.add(new FilterStruct("Sort by review date", new SoftByNumberTag("RD")));
        displayFilter();
        reSort();
    }
    
    private void findNext() {
        currentWord_ = (IWord) group_.Next();
        if (currentWord_ != null) {
            currentNumber_++;
        }
    }
    
    private void reSort() {
        List<IItemFilter> temp = new LinkedList<>();
        for (int i = 0; i < filterList_.size(); i++) {
            temp.add(filterList_.get(i).filter_);
        }
        group_ = new ItemGroup(dictionary_.getWords());
        group_.Shuffle();
        group_.Sort(temp);
        currentNumber_ = 0;
    }
    
    private void displayFilter() {
        DefaultListModel dlm = new DefaultListModel();
        for (int i = 0; i < filterList_.size(); i++) {
            dlm.addElement(filterList_.get(i).name_);
        }
        jListFilter.setModel(dlm);
    }
    
    private void displayWord() {
        if (currentWord_ == null) {
            this.jtxtKanji.setText("");
            this.jtxtKana.setText("");
            this.jtxtNote.setText("");
            jtxtCount.setText("0");
            jtxtSkill.setText("");
            jtxtRD.setText("");
            return;
        }
        String tone = "";
        if (!currentWord_.getTone().equals("")) {
            tone = " [" + currentWord_.getTone() + "]";
        }
        if (jcbKana.isSelected() || showKana_) {
            this.jtxtKana.setText(currentWord_.getKana() + tone);
        } else {
            this.jtxtKana.setText("");
        }
        if (jcbKanji.isSelected() || showKanji_) {
            this.jtxtKanji.setText(currentWord_.getContent() + tone);
        } else {
            this.jtxtKanji.setText("");
        }
        if (jcbImi.isSelected() || showImi_) {
            String mean = "";
            this.jtxtImi.setText(MeaningUtil.meaningToString(currentWord_));
//            for (IMeaning m : currentWord_.getMeanings()) {
//                mean += "[" + m.getType() + "]";
//                mean += m.getInCHS();
//                mean += "\r\n";
//            }
//            this.jtxtImi.setText(mean);
        } else {
            this.jtxtImi.setText("");
        }
        if (jcbRoma.isSelected() || showRoma_) {
            this.jtxtRoma.setText(currentWord_.getRoma().getString());
        } else {
            this.jtxtRoma.setText("");
        }
        if (jcbNote.isSelected() || showNote_) {
            this.jtxtNote.setText(currentWord_.getNote());
        } else {
            this.jtxtNote.setText("");
        }
        jtxtSkill.setText(currentWord_.getTagValue("Skill"));
        jtxtRD.setText(currentWord_.getTagValue("RD"));
        if (group_ != null) {
            jtxtCount.setText(String.format("%d/%d", currentNumber_, group_.getCount()));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jcbKanji = new javax.swing.JCheckBox();
        jtxtKanji = new javax.swing.JTextField();
        jtxtKana = new javax.swing.JTextField();
        jcbKana = new javax.swing.JCheckBox();
        jcbImi = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtxtNote = new javax.swing.JTextArea();
        jbtnNext = new javax.swing.JButton();
        jbtnPass = new javax.swing.JButton();
        jbtnFail = new javax.swing.JButton();
        jcbNote = new javax.swing.JCheckBox();
        jtxtRoma = new javax.swing.JTextField();
        jcbRoma = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListFilter = new javax.swing.JList<>();
        jbtnAddFilter = new javax.swing.JButton();
        jbtnRemoveFilter = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jtxtCount = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jtxtSkill = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jtxtRD = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtxtImi = new JPUI.JMeaningPane();

        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setMinimumSize(new java.awt.Dimension(680, 400));
        setPreferredSize(new java.awt.Dimension(680, 400));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jcbKanji.setText("kanji");
        jcbKanji.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbKanjiItemStateChanged(evt);
            }
        });
        jcbKanji.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jcbKanjiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jcbKanjiMouseExited(evt);
            }
        });
        add(jcbKanji, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 17, -1, -1));

        jtxtKanji.setFont(new java.awt.Font("MS Mincho", 0, 18)); // NOI18N
        jtxtKanji.setToolTipText("");
        add(jtxtKanji, new org.netbeans.lib.awtextra.AbsoluteConstraints(77, 15, 280, -1));

        jtxtKana.setFont(new java.awt.Font("MS Mincho", 0, 18)); // NOI18N
        jtxtKana.setToolTipText("");
        add(jtxtKana, new org.netbeans.lib.awtextra.AbsoluteConstraints(77, 45, 280, -1));

        jcbKana.setSelected(true);
        jcbKana.setText("kana");
        jcbKana.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbKanaItemStateChanged(evt);
            }
        });
        jcbKana.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jcbKanaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jcbKanaMouseExited(evt);
            }
        });
        add(jcbKana, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 47, -1, -1));

        jcbImi.setText("imi");
        jcbImi.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbImiItemStateChanged(evt);
            }
        });
        jcbImi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jcbImiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jcbImiMouseExited(evt);
            }
        });
        add(jcbImi, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 109, -1, -1));

        jtxtNote.setColumns(20);
        jtxtNote.setFont(new java.awt.Font("SimSun", 0, 14)); // NOI18N
        jtxtNote.setRows(4);
        jtxtNote.setToolTipText("Note");
        jtxtNote.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jtxtNote.setName(""); // NOI18N
        jScrollPane1.setViewportView(jtxtNote);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(77, 243, 280, 90));

        jbtnNext.setText("Next");
        jbtnNext.setMaximumSize(new java.awt.Dimension(65, 30));
        jbtnNext.setMinimumSize(new java.awt.Dimension(65, 30));
        jbtnNext.setPreferredSize(new java.awt.Dimension(65, 30));
        jbtnNext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbtnNextMouseClicked(evt);
            }
        });
        add(jbtnNext, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, -1, -1));

        jbtnPass.setText("Pass");
        jbtnPass.setMaximumSize(new java.awt.Dimension(65, 30));
        jbtnPass.setMinimumSize(new java.awt.Dimension(65, 30));
        jbtnPass.setPreferredSize(new java.awt.Dimension(65, 30));
        jbtnPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPassActionPerformed(evt);
            }
        });
        add(jbtnPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 350, -1, -1));

        jbtnFail.setText("Fail");
        jbtnFail.setMaximumSize(new java.awt.Dimension(65, 30));
        jbtnFail.setMinimumSize(new java.awt.Dimension(65, 30));
        jbtnFail.setPreferredSize(new java.awt.Dimension(65, 30));
        jbtnFail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnFailActionPerformed(evt);
            }
        });
        add(jbtnFail, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 350, -1, -1));

        jcbNote.setActionCommand("note");
        jcbNote.setLabel("note");
        jcbNote.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbNoteItemStateChanged(evt);
            }
        });
        jcbNote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jcbNoteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jcbNoteMouseExited(evt);
            }
        });
        add(jcbNote, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, -1, -1));

        jtxtRoma.setFont(new java.awt.Font("MS Mincho", 0, 18)); // NOI18N
        jtxtRoma.setToolTipText("");
        add(jtxtRoma, new org.netbeans.lib.awtextra.AbsoluteConstraints(77, 77, 280, -1));

        jcbRoma.setText("Roma");
        jcbRoma.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbRomaItemStateChanged(evt);
            }
        });
        jcbRoma.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jcbRomaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jcbRomaMouseExited(evt);
            }
        });
        add(jcbRoma, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 79, -1, -1));

        jScrollPane2.setViewportView(jListFilter);

        add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 202, 245, 130));

        jbtnAddFilter.setText("+");
        jbtnAddFilter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbtnAddFilterMouseClicked(evt);
            }
        });
        add(jbtnAddFilter, new org.netbeans.lib.awtextra.AbsoluteConstraints(625, 260, 47, 33));

        jbtnRemoveFilter.setText("-");
        jbtnRemoveFilter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbtnRemoveFilterMouseClicked(evt);
            }
        });
        add(jbtnRemoveFilter, new org.netbeans.lib.awtextra.AbsoluteConstraints(625, 300, 47, 35));

        jLabel1.setText("Count");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(375, 21, -1, -1));

        jtxtCount.setToolTipText("");
        jtxtCount.setEnabled(false);
        add(jtxtCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(413, 18, 88, -1));

        jLabel2.setText("Skill");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(375, 51, -1, -1));

        jtxtSkill.setToolTipText("");
        jtxtSkill.setEnabled(false);
        add(jtxtSkill, new org.netbeans.lib.awtextra.AbsoluteConstraints(413, 48, 88, -1));

        jLabel3.setText("RD");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(375, 80, -1, -1));

        jtxtRD.setToolTipText("");
        jtxtRD.setEnabled(false);
        add(jtxtRD, new org.netbeans.lib.awtextra.AbsoluteConstraints(413, 77, 88, -1));

        jtxtImi.setFont(new java.awt.Font("SimSun", 0, 14)); // NOI18N
        jScrollPane4.setViewportView(jtxtImi);

        add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(77, 110, 280, 125));
    }// </editor-fold>//GEN-END:initComponents

    private void jcbKanjiItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbKanjiItemStateChanged
        // TODO add your handling code here:
        displayWord();
    }//GEN-LAST:event_jcbKanjiItemStateChanged

    private void jcbKanjiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jcbKanjiMouseEntered
        // TODO add your handling code here:
        showKanji_ = true;
        displayWord();
    }//GEN-LAST:event_jcbKanjiMouseEntered

    private void jcbKanjiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jcbKanjiMouseExited
        // TODO add your handling code here:
        showKanji_ = false;
        displayWord();
    }//GEN-LAST:event_jcbKanjiMouseExited

    private void jcbKanaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbKanaItemStateChanged
        // TODO add your handling code here:
        displayWord();
    }//GEN-LAST:event_jcbKanaItemStateChanged

    private void jcbKanaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jcbKanaMouseEntered
        // TODO add your handling code here:
        showKana_ = true;
        displayWord();
    }//GEN-LAST:event_jcbKanaMouseEntered

    private void jcbKanaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jcbKanaMouseExited
        // TODO add your handling code here:
        showKana_ = false;
        displayWord();
    }//GEN-LAST:event_jcbKanaMouseExited

    private void jcbImiItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbImiItemStateChanged
        // TODO add your handling code here:
        displayWord();
    }//GEN-LAST:event_jcbImiItemStateChanged

    private void jcbImiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jcbImiMouseEntered
        // TODO add your handling code here:
        showImi_ = true;
        displayWord();
    }//GEN-LAST:event_jcbImiMouseEntered

    private void jcbImiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jcbImiMouseExited
        // TODO add your handling code here:
        showImi_ = false;
        displayWord();
    }//GEN-LAST:event_jcbImiMouseExited

    private void jbtnNextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbtnNextMouseClicked
        // TODO add your handling code here:
        findNext();
        displayWord();
    }//GEN-LAST:event_jbtnNextMouseClicked

    private void jcbNoteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbNoteItemStateChanged
        // TODO add your handling code here:
        displayWord();
    }//GEN-LAST:event_jcbNoteItemStateChanged

    private void jcbNoteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jcbNoteMouseEntered
        // TODO add your handling code here:
        showNote_ = true;
        displayWord();
    }//GEN-LAST:event_jcbNoteMouseEntered

    private void jcbNoteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jcbNoteMouseExited
        // TODO add your handling code here:
        showNote_ = false;
        displayWord();
    }//GEN-LAST:event_jcbNoteMouseExited

    private void jcbRomaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbRomaItemStateChanged
        // TODO add your handling code here:
        displayWord();
    }//GEN-LAST:event_jcbRomaItemStateChanged

    private void jcbRomaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jcbRomaMouseEntered
        // TODO add your handling code here:
        showRoma_ = true;
        displayWord();
    }//GEN-LAST:event_jcbRomaMouseEntered

    private void jcbRomaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jcbRomaMouseExited
        // TODO add your handling code here:
        showRoma_ = false;
        displayWord();
    }//GEN-LAST:event_jcbRomaMouseExited

    private void jbtnPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPassActionPerformed
        // TODO add your handling code here:
        if (currentWord_ != null) {
            currentWord_.increaseSkill();
        }
        findNext();
        displayWord();
    }//GEN-LAST:event_jbtnPassActionPerformed

    private void jbtnFailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnFailActionPerformed
        // TODO add your handling code here:
        if (currentWord_ != null) {
            currentWord_.updateSkill(-5);
        }
        findNext();
        displayWord();
    }//GEN-LAST:event_jbtnFailActionPerformed

    private void jbtnRemoveFilterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbtnRemoveFilterMouseClicked
        // TODO add your handling code here:
        if (jListFilter.getSelectedIndex() != -1) {
            System.out.println(Integer.toString(jListFilter.getSelectedIndex()));
            String str = (String) jListFilter.getModel().getElementAt(jListFilter.getSelectedIndex());
            for (int i = 0; i < filterList_.size(); i++) {
                if (filterList_.get(i).name_.equals(str)) {
                    filterList_.remove(i);
                    break;
                }
            }
            displayFilter();
            reSort();
        }
    }//GEN-LAST:event_jbtnRemoveFilterMouseClicked

    private void jbtnAddFilterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbtnAddFilterMouseClicked
        // TODO add your handling code here:
        FilterDialog fd = new FilterDialog(null, true);
        fd.setLocation(this.getLocationOnScreen());
        fd.setVisible(true);
        if (fd.isOK_ && fd.filter_ != null) {
            filterList_.add(fd.filter_);
            displayFilter();
            reSort();
        }
        System.out.println("Done");
    }//GEN-LAST:event_jbtnAddFilterMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<String> jListFilter;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton jbtnAddFilter;
    private javax.swing.JButton jbtnFail;
    private javax.swing.JButton jbtnNext;
    private javax.swing.JButton jbtnPass;
    private javax.swing.JButton jbtnRemoveFilter;
    private javax.swing.JCheckBox jcbImi;
    private javax.swing.JCheckBox jcbKana;
    private javax.swing.JCheckBox jcbKanji;
    private javax.swing.JCheckBox jcbNote;
    private javax.swing.JCheckBox jcbRoma;
    private javax.swing.JTextField jtxtCount;
    private JPUI.JMeaningPane jtxtImi;
    private javax.swing.JTextField jtxtKana;
    private javax.swing.JTextField jtxtKanji;
    private javax.swing.JTextArea jtxtNote;
    private javax.swing.JTextField jtxtRD;
    private javax.swing.JTextField jtxtRoma;
    private javax.swing.JTextField jtxtSkill;
    // End of variables declaration//GEN-END:variables
}
