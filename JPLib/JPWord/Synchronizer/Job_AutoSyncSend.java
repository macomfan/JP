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
class Job_AutoSyncSend extends Job_Base {

    public Job_AutoSyncSend(TCPCommunication tcp, String dictName, Logging logging) {
        super(tcp, dictName, logging);
    }
    private IWordDictionary dict_ = null;
    private int number_ = 0;

    @Override
    public JobResult start() {
        dict_ = Database.getInstance().loadDictionary(dictName_);
        if (dict_ == null) {
            Message reason = new Message(Message.MSG_FIN);
            reason.addTag(Constant.REASON, "Cannot find dict name");
            sendMessage(reason);
            return JobResult.FAIL;
        }
        Message msg = new Message(Message.MSG_SYN);
        msg.addTag(Constant.NUMBER, Integer.toString(dict_.getWords().size(), 10));
        msg.addTag(Constant.DICTNAME, dict_.getName());
        logging_.push(Log.Type.HARMLESS, "Sending number: %d" + Integer.toString(dict_.getWords().size(), 10));

        sendMessage(msg);
        return JobResult.SUCCESS;
    }

    @Override
    public JobResult doAction(Message msg) {
        if (msg.getType() == Message.MSG_ACK) {
            logging_.push(Log.Type.HARMLESS, "Get confirmed message");
            logging_.push(Log.Type.HARMLESS, "Sending content...");
            for (IWord word : dict_.getWords()) {
                Message data = new Message(Message.MSG_DAT);
                //String wordString = word.encodeToString();
                //data.setValue(wordString);
                sendMessage(data);
            }
            return JobResult.SUCCESS;
        } else if (msg.getType() == Message.MSG_SYN) {
            logging_.push(Log.Type.HARMLESS, "Receive sync message");
            String number = msg.getTag(Constant.NUMBER);
            number_ = Integer.parseInt(number);
            logging_.push(Log.Type.HARMLESS, String.format("%d items need be changed", number_));
            return JobResult.SUCCESS;
        } else if (msg.getType() == Message.MSG_DAT) {
            String action = msg.getTag(Constant.ACTION);

        } else if (msg.getType() == Message.MSG_REP) {

        }
        return JobResult.FAIL;
    }
}
