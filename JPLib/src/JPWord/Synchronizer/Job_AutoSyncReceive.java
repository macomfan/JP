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
class Job_AutoSyncReceive extends Job_Base {

    private int number_ = 0;
    private int index_ = 0;

    @Override
    public JobResult start() {
        // do nothing
        return JobResult.SUCCESS;
    }

    public Job_AutoSyncReceive(TCPCommunication tcp, String dictName, Logging logging) {
        super(tcp, dictName, logging);
    }

    @Override
    public JobResult doAction(Message msg) {
        if (msg.getType() == Message.MSG_SYN) {
            String number = msg.getTag(Constant.NUMBER);
            number_ = Integer.parseInt(number);
            logging_.push(Log.Type.HARMLESS, "Received number is " + number);
            Message ack = new Message(Message.MSG_ACK);
            sendMessage(ack);
            //tempDict_ = Database.createWordDictionary(null, null);

            return JobResult.SUCCESS;
        } else if (msg.getType() == Message.MSG_DAT) {
            //IWord word = tempDict_.createWord();
            // word.decodeFromString(msg.getValue());

            //tempDict_.addWord(word);
            index_++;
            if (index_ == number_) {
                logging_.push(Log.Type.HARMLESS, "Received done");
            }
            return JobResult.SUCCESS;
        } else if (msg.getType() == Message.MSG_ACK) {
            logging_.push(Log.Type.HARMLESS, String.format("Received complete, number is %d", index_));
            try {
                //dict_.save();
            } catch (Exception e) {
            }

            Message bye = new Message(Message.MSG_FIN);
            sendMessage(bye);
            return JobResult.DONE;
        } else if (msg.getType() == Message.MSG_FIN) {
            logging_.push(Log.Type.WARNING, "Closed by sender");
            return JobResult.FAIL;
        }
        return JobResult.FAIL;
    }
}
