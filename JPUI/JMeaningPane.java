/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

/**
 *
 * @author u0151316
 */
public class JMeaningPane extends JTextPane {

    class LineStartEndInfo {

        public int start_ = 0;
        public int end_ = 0;
        public int type_ = 0;  // 1 is meaning, 2 is example 3 is error
        public int lineID_ = 0;

        public LineStartEndInfo(int start, int end, int lineid) {
            start_ = start;
            end_ = end;
            lineID_ = lineid;
        }
    }

    private Vector<LineStartEndInfo> lineStartEndInfos_ = new Vector();
    private int currentLine_ = -1;
    private SimpleAttributeSet err_ = new SimpleAttributeSet();
    private SimpleAttributeSet nor_ = new SimpleAttributeSet();
    private SimpleAttributeSet ex_ = new SimpleAttributeSet();
    private DefaultStyledDocument doc_ = null;

    public JMeaningPane() {

        //doc_.setParagraphAttributes(0, 0, paraSet, true);
        doc_ = new DefaultStyledDocument();
        StyleConstants.setForeground(err_, Color.red);
        StyleConstants.setForeground(nor_, Color.black);
        StyleConstants.setForeground(ex_, Color.GRAY);
        this.setDocument(doc_);
        StyleContext sc = StyleContext.getDefaultStyleContext();
        TabSet tabs = new TabSet(new TabStop[]{new TabStop(40)});
        AttributeSet paraSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.TabSet, tabs);

        this.setParagraphAttributes(paraSet, true);
        this.setBounds(500, 500, 500, 500);
        //jtxtImi.setFont(new java.awt.Font("MS Mincho", 0, 18)); // NOI18N
        lineStartEndInfos_.removeAllElements();
        this.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                onKeyReleased(evt);
            }
        });
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public void setSize(Dimension d) {
        if (d.width < getParent().getSize().width) {
            d.width = getParent().getSize().width;
        }
        d.width += 1;
        super.setSize(d);
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font); //To change body of generated methods, choose Tools | Templates.
        if (doc_ != null) {
            SimpleAttributeSet newFont = new SimpleAttributeSet();
            StyleConstants.setFontFamily(newFont, font.getFontName());
            StyleConstants.setFontSize(newFont, font.getSize());
            this.setParagraphAttributes(newFont, true);
        }

    }

    @Override
    public void setText(String t) {
        super.setText(t); //To change body of generated methods, choose Tools | Templates.
        analyze();
    }

    private void indexEachLine() {
        lineStartEndInfos_.removeAllElements();
        Element root = doc_.getDefaultRootElement();
        int cursorPos = this.getCaretPosition();
        currentLine_ = root.getElementIndex(cursorPos);
        for (int i = 0; i < root.getElementCount(); i++) {
            Element line = root.getElement(i);
            int start = line.getStartOffset();
            int end = line.getEndOffset() - 1;
            try {
                LineStartEndInfo info = new LineStartEndInfo(start, end, i);
                String curline = this.getText(start, end - start);
                if (curline.length() != 0) {
                    if (curline.charAt(0) == '\t' || curline.charAt(0) == ' ') {
                        info.type_ = 2;
                    } else {
                        info.type_ = 1;
                    }
                    if (info.type_ == 1) {
                        if (curline.indexOf(':') == -1 && curline.indexOf(']') == -1) {
                            info.type_ = 3;
                        }
                    }
                }
                lineStartEndInfos_.add(info);
            } catch (BadLocationException ex) {

            }
        }
    }

    private void analyze() {
        indexEachLine();
        //System.err.println("\"-----------");
        for (LineStartEndInfo info : lineStartEndInfos_) {
            SimpleAttributeSet obj = nor_;
            switch (info.type_) {
                case 2:
                    //System.err.println("Set line ex" + info.lineID_);
                    obj = ex_;
                    break;
                case 3:
                    //System.err.println("Set line err" + info.lineID_);
                    obj = err_;
                    break;
                default:
                    //System.err.println("Set line nor" + info.lineID_);
                    obj = nor_;
                    break;
            }
            doc_.setCharacterAttributes(info.start_, info.end_ - info.start_, obj, true);

            if (currentLine_ == info.lineID_) {
                //System.err.println("Set cur" + info.lineID_);
                this.setCharacterAttributes(obj, true);
            }
        }
//        String ll = "";
//        Element root = doc_.getDefaultRootElement();
//        ll += "----------- " + Integer.toString(root.getElementIndex(this.getCaretPosition())) + " -----\n";
//        for (LineStartEndInfo index : lineStartEndInfos_) {
//            ll += Integer.toString(index.start_) + "-" + Integer.toString(index.end_);
//            ll += "\n";
//        }
//        System.out.println(ll);
    }

    private void onKeyReleased(java.awt.event.KeyEvent evt) {
        analyze();
    }

}
