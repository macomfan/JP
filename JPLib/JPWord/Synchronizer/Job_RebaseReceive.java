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
class Job_RebaseReceive extends Job_Base {

    private int number_ = 0;
    private int index_ = 0;
    private IWordDictionary dict_ = null;

    public Job_RebaseReceive(TCPCommunication tcp, String dictName, Logging logging) {
        super(tcp, dictName, logging);
    }

    @Override
    public JobResult start() {
        dict_ = Database.getInstance().createDictionary(dictName_);
        Message syn = new Message(Message.MSG_SYN);
        syn.addTag(Constant.ACTION, "READY");
        sendMessage(syn);
        return JobResult.SUCCESS;
    }

    @Override
    public JobResult doAction(Message msg) {
        if (msg.getType() == Message.MSG_SYN) {
            String number = msg.getTag(Constant.NUMBER);
            number_ = Integer.parseInt(number);
            logging_.push(Log.Type.HARMLESS, "Receive the number is " + number);
            Message ack = new Message(Message.MSG_ACK);
            sendMessage(ack);
            return JobResult.SUCCESS;
        } else if (msg.getType() == Message.MSG_DAT) {
            IWord word = dict_.createWord();
            word.decodeFromString(msg.getValue());
            dict_.addWord(word);
            index_++;
            if (index_ == number_) {
                logging_.push(Log.Type.HARMLESS, "Received done");
            }
            return JobResult.SUCCESS;
        } else if (msg.getType() == Message.MSG_ACK) {
            logging_.push(Log.Type.HARMLESS, String.format("Send finished, received %d", index_));
            try {
                dict_.save();
            } catch (Exception e) {
            }

            Message fin = new Message(Message.MSG_FIN);
            sendMessage(fin);
            return JobResult.DONE;
        } else if (msg.getType() == Message.MSG_FIN) {
            logging_.push(Log.Type.WARNING, "Closed by sender");
            return JobResult.FAIL;
        }
        return JobResult.FAIL;
    }
}
