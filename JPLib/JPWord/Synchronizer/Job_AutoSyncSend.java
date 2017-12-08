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
public class Job_AutoSyncSend extends Job_Base {

    public Job_AutoSyncSend(TCPCommunication tcp, IWordDictionary dict, Logging logging) {
        super(tcp, dict, logging);
    }

    private int number_ = 0;
    
    @Override
    public JobResult doAction(Message msg) {
        if (msg.getType() == Message.MSG_ACK) {
            logging_.push("[N] Get confirmed message");
            logging_.push("[N] Sending content...");
            for (IWord word : dict_.getWords()) {
                Message data = new Message(Message.MSG_DAT);
                String wordString = word.encodeToString();
                data.setValue(wordString);
                tcp_.send(data);
            }
            return JobResult.SUCCESS;
        } else if (msg.getType() == Message.MSG_SYN) {
            logging_.push("[N] Receive sync message");
            String number = msg.getTag(Constant.NUMBER);
            number_ = Integer.parseInt(number);
            logging_.push(String.format("[N] %d items need be changed", number_));
            return JobResult.SUCCESS;
        } else if (msg.getType() == Message.MSG_DAT) {
            String action = msg.getTag(Constant.ACTION);
            
        }
        return JobResult.FAIL;
    }

    @Override
    public void start() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
