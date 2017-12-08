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
class Job_RebaseReceive extends Job_Base {

    private int number_ = 0;
    private int index_ = 0;

    public Job_RebaseReceive(TCPCommunication tcp, IWordDictionary dict, Logging logging) {
        super(tcp, dict, logging);
    }

    @Override
    public JobResult doAction(Message msg) {
        if (msg.getType() == Message.MSG_SYN) {
            String number = msg.getTag(Constant.NUMBER);
            number_ = Integer.parseInt(number);
            logging_.push("[N] Receive the number is " + number);
            Message ack = new Message(Message.MSG_ACK);
            tcp_.send(ack);
            return JobResult.SUCCESS;
        } else if (msg.getType() == Message.MSG_DAT) {
            IWord word = dict_.createWord();
            word.decodeFromString(msg.getValue());
            dict_.addWord(word);
            index_++;
            if (index_ == number_) {
                logging_.push("[N] Received done");
            }
            return JobResult.SUCCESS;
        } else if (msg.getType() == Message.MSG_ACK) {
            logging_.push(String.format("[N] Send finished, received %d", index_));
            dict_.save();
            Message bye = new Message(Message.MSG_BYE);
            tcp_.send(bye);
            return JobResult.DONE;
        } else if (msg.getType() == Message.MSG_BYE) {
            logging_.push("[E] Closed by sender");
            return JobResult.FAIL;
        }
        return JobResult.FAIL;
    }

    @Override
    public void start() {
        // do nothing
    }

}
