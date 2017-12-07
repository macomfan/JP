/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainUI;

import java.awt.Color;
import java.awt.Component;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 *
 * @author u0151316
 */
public class JLogList extends JList<String> {

    public enum LogType {
        ERROR,
        WARNING,
        NORMAL,
        SUCCESSFUL,
    }

    class LogInfo {

        public LogType type_;
        public String header_;
        public String log_;

        public LogInfo(LogType type, String header, String log) {
            this.type_ = type;
            this.log_ = log;
            this.header_ = header;
        }

    }

    class ColorRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            if (!(value instanceof LogInfo)) {
                return super.getListCellRendererComponent(list, "[UNKNOWN]", index, isSelected, cellHasFocus);
            }
            LogInfo loginfo = (LogInfo) value;
            String v = loginfo.header_ + " - " + loginfo.log_;
            Component p = super.getListCellRendererComponent(list, v, index, isSelected, cellHasFocus);
            if (null != loginfo.type_) {
                switch (loginfo.type_) {
                    case ERROR:
                        this.setForeground(Color.red);
                        break;
                    case NORMAL:
                        break;
                    case SUCCESSFUL:
                        this.setForeground(Color.GREEN);
                        break;
                    case WARNING:
                        this.setForeground(Color.ORANGE);
                        break;
                    default:
                        break;
                }
            }
            return p;
        }
    }

    private List<LogInfo> logs_ = new LinkedList<>();
    private DefaultListModel listModel_ = new DefaultListModel();

    public JLogList() {
        this.setModel(listModel_);
        this.setCellRenderer(new ColorRenderer());
    }

    public void addLog(LogType type, String log) {
        Calendar now = Calendar.getInstance();
//        String header = String.format("%04d-%02d-%02d %02d:%02d:%02d.%03d",
//                now.get(Calendar.YEAR),
//                now.get(Calendar.MONTH) + 1,
//                now.get(Calendar.DAY_OF_MONTH),
//                now.get(Calendar.HOUR_OF_DAY),
//                now.get(Calendar.MINUTE),
//                now.get(Calendar.SECOND),
//                now.get(Calendar.MILLISECOND));
        String header = String.format("%02d:%02d:%02d.%03d",
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                now.get(Calendar.MILLISECOND));
        LogInfo logInfo = new LogInfo(type, header, log);
        logs_.add(logInfo);
        listModel_.addElement(logInfo);
        if (listModel_.getSize() > 2) {
//            this.repaint();
//            this.updateUI();
            this.scrollRectToVisible(this.getCellBounds(listModel_.getSize() - 1, listModel_.getSize()));
        }

    }
}
