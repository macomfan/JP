/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Synchronizer;

import JPWord.Data.IWord;
import JPWord.Data.IWordDictionary;

/**
 *
 * @author u0151316
 */
abstract class Job_Base {

    public enum JobResult {
        FAIL,
        SUCCESS,
        EXPECTED,
        DONE,
    }

    private TCPCommunication tcp_ = null;
    protected String dictName_ = "";
    protected Logging logging_ = null;

    public Job_Base(TCPCommunication tcp, String dictName, Logging logging) {
        tcp_ = tcp;
        dictName_ = dictName;
        logging_ = logging;
    }

    protected void sendMessage(Message msg) {
        if (tcp_.getStatus() == TCPCommunication.Status.CONNECTED) {
            tcp_.send(msg);
        }
    }

    public abstract JobResult doAction(Message msg);

    public abstract JobResult start();

    protected void sendDataSummary(IWordDictionary dict) {
        Message msg = new Message(Message.MSG_SYN);
        msg.addTag(Constant.NUMBER, Integer.toString(dict.getWords().size(), 10));
        msg.addTag(Constant.DICTNAME, dict.getName());
        logging_.push(Log.Type.HARMLESS, "Sending number: %d" + Integer.toString(dict.getWords().size(), 10));
        sendMessage(msg);
    }

    protected void sendConfirmDataSummary(IWordDictionary dict) {
        Message ack = new Message(Message.MSG_ACK);
        sendMessage(ack);
    }

    protected void sendWholeData(IWordDictionary dict) {
        for (IWord word : dict.getWords()) {
            Message data = new Message(Message.MSG_DAT);
            String wordString = word.encodeToString();
            data.setValue(wordString);
            sendMessage(data);
        }
        logging_.push(Log.Type.HARMLESS, "Sending finished");
        Message fin = new Message(Message.MSG_FIN);
        sendMessage(fin);
    }

    protected int confirmDataSummary(Message msg) {
        int objNumber = 0;
        if (msg.getType() != Message.MSG_SYN) {
            return 0;
        }
        String number = msg.getTag(Constant.NUMBER);
        objNumber = Integer.parseInt(number);
        logging_.push(Log.Type.HARMLESS, "Receive the number is " + number);
        Message ack = new Message(Message.MSG_ACK);
        sendMessage(ack);
        return objNumber;
    }
    
    protected void sendError(String errorString) {
        Message err = new Message(Message.MSG_ERR);
        err.addTag(Constant.REASON, errorString);
        sendMessage(err);
    }
}
