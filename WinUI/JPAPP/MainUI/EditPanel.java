/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainUI;

import JPWord.Data.IMeaning;
import JPWord.Data.IWord;
import JPWord.Data.IWordDictionary;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author u0151316
 */
public class EditPanel extends javax.swing.JPanel {
    
    private String searchText_ = null;
    IWordDictionary dictionary_ = null;
    private IWord currentWord_ = null;

    /**
     * Creates new form EditPanel
     */
    public EditPanel() {
        initComponents();
        
    }
    
    public void initialize() {
        dictionary_ = Database.getInstance().getDatabase();
        jtabMainTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (jtabMainTable.getSelectedRow() == -1) {
                    selectItem(null);
                } else {
                    selectItem((IWord) jtabMainTable.getModel().getValueAt(jtabMainTable.getSelectedRow(), 3));
                }
                
            }
        }
        );
        refreshMainTable(null);
        selectItem(null);
    }
    
    private void selectItem(IWord word) {
        if (word == null) {
            jtxtContent.setText("");
            jtxtKana.setText("");
            jtxtTone.setText("");
            jtxtNode.setText("");
            jtxtMean.setText("");
            jlbRoma.setText("");
            jtxtContent.setEnabled(false);
            jtxtKana.setEnabled(false);
            jtxtTone.setEnabled(false);
            jtxtNode.setEnabled(false);
            jtxtMean.setEnabled(false);
        } else {
            currentWord_ = word;
            jtxtContent.setEnabled(true);
            jtxtKana.setEnabled(true);
            jtxtTone.setEnabled(true);
            jtxtNode.setEnabled(true);
            jtxtMean.setEnabled(true);
            
            jtxtContent.setText(currentWord_.getContent());
            if (currentWord_.getRoma() != null) {
                jlbRoma.setText("[" + currentWord_.getRoma().getString() + "]");
            }
            jtxtKana.setText(currentWord_.getKana());
            jtxtTone.setText(currentWord_.getTone());
            jtxtNode.setText(currentWord_.getNote());
            jtxtMean.setText(MeaningUtil.meaningToString(currentWord_));
        }
        
    }
    
    private void addWordToRow(IWord w, DefaultTableModel dtm) {
        String content = w.getContent();
        String kana = w.getKana();
        if (!w.getTone().equals("")) {
            kana += "[" + w.getTone() + "]";
        }
        String type = "[";
        for (IMeaning m : w.getMeanings()) {
            if (!type.contains(m.getType())) {
                type += m.getType();
                type += " ";
            }
        }
        if (!type.equals("")) {
            type += "]";
        } else if (type.equals("[")) {
            type = "";
        }
        dtm.addRow(new Object[]{content, kana, type, w});
    }
    
    private void refreshMainTable(IWord word) {
        String currentSearchText = jtxtSearch.getText();
        if (searchText_ == currentSearchText) {
            return;
        }
        
        DefaultTableModel dtm = new DefaultTableModel();
        dtm.addColumn("Word");
        dtm.addColumn("kana");
        dtm.addColumn("Type");
        dtm.addColumn("ID");
        int row = -1;
        int selectedRow = -1;
        for (IWord w : dictionary_.getWords()) {
            if (currentSearchText != null && currentSearchText.equals("")) {
                row++;
                addWordToRow(w, dtm);
            } else if (w.getContent().indexOf(currentSearchText) != -1
                    || w.getKana().indexOf(currentSearchText) != -1
                    || w.getRoma().hitTest(currentSearchText) == true) {
                row++;
                addWordToRow(w, dtm);
            }
            if (word != null) {
                if (w.getID().equals(word.getID())) {
                    selectedRow = row;
                }
            }
            
        }
        jtabMainTable.setModel(dtm);
        {
            TableColumn column = jtabMainTable.getColumnModel().getColumn(3);
            column.setMinWidth(0);
            column.setMaxWidth(0);
            column.setWidth(0);
            column.setPreferredWidth(0);
        }
        {
            TableColumn column = jtabMainTable.getColumnModel().getColumn(2);
            column.setMinWidth(60);
            column.setMaxWidth(60);
            column.setWidth(60);
            column.setPreferredWidth(60);
        }
        searchText_ = currentSearchText;
        
        if (word != null && selectedRow != -1) {
            jtabMainTable.setRowSelectionInterval(selectedRow, selectedRow);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jtabMainTable = new javax.swing.JTable();
        jtxtSearch = new javax.swing.JTextField();
        jtxtContent = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jlbRoma = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jtxtKana = new javax.swing.JTextField();
        jtxtTone = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtxtNode = new javax.swing.JTextArea();
        jbtnSave = new javax.swing.JButton();
        jbtnNew = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtxtMean = new MainUI.JMeaningPane();

        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setMinimumSize(new java.awt.Dimension(680, 400));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jtabMainTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null}
            },
            new String [] {
                "Word", "kana", "Type"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jtabMainTable);

        jtxtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtSearchKeyReleased(evt);
            }
        });

        jLabel1.setText("Word");
        jLabel1.setToolTipText("");

        jLabel2.setText("kana");
        jLabel2.setToolTipText("");

        jLabel3.setText("Note");
        jLabel3.setToolTipText("");

        jtxtNode.setColumns(20);
        jtxtNode.setRows(5);
        jScrollPane3.setViewportView(jtxtNode);

        jbtnSave.setText("Save");
        jbtnSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbtnSaveMouseClicked(evt);
            }
        });

        jbtnNew.setText("New");
        jbtnNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbtnNewMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(jtxtMean);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(jtxtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jtxtContent, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jlbRoma, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jbtnNew)
                                .addGap(227, 227, 227)
                                .addComponent(jbtnSave))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(22, 22, 22)
                                        .addComponent(jtxtKana, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(8, 8, 8)
                                        .addComponent(jtxtTone, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(12, 12, 12)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtxtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel1))
                    .addComponent(jtxtContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbRoma, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel2))
                            .addComponent(jtxtKana, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtTone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbtnNew)
                            .addComponent(jbtnSave))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jtxtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtSearchKeyReleased
        // TODO add your handling code here:
        refreshMainTable(null);
    }//GEN-LAST:event_jtxtSearchKeyReleased

    private void jbtnSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbtnSaveMouseClicked
        // TODO add your handling code here:
        //MeaningUtil.parseStringToMeaning(currentWord_, jtxtMean.getText());
        if (checkChanged()) {
            currentWord_.setContent(jtxtContent.getText());
            currentWord_.setKana(jtxtKana.getText());
            currentWord_.setTone(jtxtTone.getText());
            currentWord_.setNote(jtxtNode.getText());
            List<IMeaning> means = MeaningUtil.parseStringToMeaning(jtxtMean.getText());
            currentWord_.updateMeaning(means);
//            for (IMeaning mean : means) {
//                System.err.println("MMM: " + mean.encodeToString());
//            }
            Database.getInstance().getDatabase().addWord(currentWord_);
            refreshMainTable(currentWord_);
        }
    }//GEN-LAST:event_jbtnSaveMouseClicked
    
    private boolean checkChanged() {
        boolean changed = false;
        if (!currentWord_.getContent().equals(jtxtContent.getText())) {
            changed = true;
        } else if (!currentWord_.getKana().equals(jtxtKana.getText())) {
            changed = true;
        } else if (!currentWord_.getTone().equals(jtxtTone.getText())) {
            changed = true;
        } else if (!currentWord_.getNote().equals(jtxtNode.getText())) {
            changed = true;
        }
        String m1 = MeaningUtil.meaningToString(currentWord_);
        String m2 = MeaningUtil.meaningToString(MeaningUtil.parseStringToMeaning(jtxtMean.getText()));
        if (!m1.equals(m2)) {
            changed = true;
        }
        return changed;
    }

    private void jbtnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbtnNewMouseClicked
        // TODO add your handling code here:
        IWord newWord = Database.getInstance().getDatabase().createWord();
        selectItem(newWord);
    }//GEN-LAST:event_jbtnNewMouseClicked

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
        refreshMainTable(currentWord_);
    }//GEN-LAST:event_formComponentShown


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton jbtnNew;
    private javax.swing.JButton jbtnSave;
    private javax.swing.JLabel jlbRoma;
    private javax.swing.JTable jtabMainTable;
    private javax.swing.JTextField jtxtContent;
    private javax.swing.JTextField jtxtKana;
    private MainUI.JMeaningPane jtxtMean;
    private javax.swing.JTextArea jtxtNode;
    private javax.swing.JTextField jtxtSearch;
    private javax.swing.JTextField jtxtTone;
    // End of variables declaration//GEN-END:variables
}
