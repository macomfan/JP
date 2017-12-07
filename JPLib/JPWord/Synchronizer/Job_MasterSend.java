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
class Job_MasterSend extends Job_Base {

    public Job_MasterSend(TCPCommunication tcp, IWordDictionary dict, Logging logging) {
        super(tcp, dict, logging);
    }

    @Override
    public void start() {
        Message number = new Message(Message.MSG_SYN);
        number.addTag(Constant.NUMBER, Integer.toString(dict_.getWords().size(), 10));
        tcp_.send(number);
        logging_.push("[N] Send number");
    }

    @Override
    public JobResult doAction(Message msg) {
        if (msg.getType() == Message.MSG_ACK) {
            logging_.push("[N] Get ACK from Slave");
            for (IWord word : dict_.getWords()) {
                Message data = new Message(Message.MSG_DAT);
                String wordString = word.encodeToString();
                data.setValue(wordString);
                tcp_.send(data);
            }
            return JobResult.SUCCESS;
        } else if (msg.getType() == Message.MSG_BYE) {
            return JobResult.DONE;
        }
        return JobResult.EXPECTRD;
    }

}
