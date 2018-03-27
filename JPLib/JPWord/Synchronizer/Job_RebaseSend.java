/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Synchronizer;

import JPWord.Data.Database;
import JPWord.Data.IWord;
import JPWord.Data.IWordDictionary;

/**
 *
 * @author u0151316
 */
class Job_RebaseSend extends Job_Base { 

    private IWordDictionary dict_ = null;

    public Job_RebaseSend(TCPCommunication tcp, String dictName, Logging logging) {
        super(tcp, dictName, logging);
    }

    @Override
    public JobResult start() {
        dict_ = Database.getInstance().loadDictionary(dictName_);
        if (dict_ == null) {
            Message reason = new Message(Message.MSG_FIN);
            reason.addTag(Constant.REASON, "Cannot find dict name");
            sendMessage(reason);
            return JobResult.FAIL;
        }
        Message number = new Message(Message.MSG_SYN);
        number.addTag(Constant.NUMBER, Integer.toString(dict_.getWords().size(), 10));
        sendMessage(number);
        logging_.push(Log.Type.HARMLESS, "Send number");
        return JobResult.SUCCESS;
    }

    @Override
    public JobResult doAction(Message msg) {
        if (msg.getType() == Message.MSG_ACK) {
            logging_.push(Log.Type.HARMLESS, "Get confirmed message");
            logging_.push(Log.Type.HARMLESS, "Sending content...");
            int logindex = 0;
            for (IWord word : dict_.getWords()) {
                Message data = new Message(Message.MSG_DAT);
                String wordString = word.encodeToString();
                data.setValue(wordString);
                sendMessage(data);
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                }
            }
            Message done = new Message(Message.MSG_ACK);
            sendMessage(done);
            return JobResult.DONE;
        } else if (msg.getType() == Message.MSG_REP) {
            logging_.push(Log.Type.HARMLESS, "Receive retransmission request, NOT supported");
            Message fin = new Message(Message.MSG_FIN);
            sendMessage(fin);
            return JobResult.DONE;
        } else if (msg.getType() == Message.MSG_FIN) {
            return JobResult.DONE;
        }
        return JobResult.FAIL;
    }

}
