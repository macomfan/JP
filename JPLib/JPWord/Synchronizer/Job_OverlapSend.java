/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Synchronizer;

import JPWord.Data.Database;
import JPWord.Data.IMeaning;
import JPWord.Data.IWord;
import JPWord.Data.IWordDictionary;

/**
 *
 * @author u0151316
 */
public class Job_OverlapSend extends Job_Base {

    public Job_OverlapSend(TCPCommunication tcp, String dictName, Logging logging) {
        super(tcp, dictName, logging);
    }
    private IWordDictionary dict_ = null;
    private int number_ = 0;
    private int index_ = 0;

    @Override
    public JobResult start() {
        dict_ = Database.getInstance().loadDictionary(dictName_);
        if (dict_ == null) {
            Message reason = new Message(Message.MSG_FIN);
            reason.addTag(Constant.REASON, "Cannot find dict name");
            sendMessage(reason);
            return JobResult.FAIL;
        }
        sendDataSummary(dict_);
        return JobResult.SUCCESS;
    }

    @Override
    public JobResult doAction(Message msg) {
        if (msg.getType() == Message.MSG_ACK) {
            logging_.push(Log.Type.HARMLESS, "Get confirmed message");
            logging_.push(Log.Type.HARMLESS, "Sending content...");
            sendWholeData(dict_);
            return JobResult.SUCCESS;
        } else if (msg.getType() == Message.MSG_SYN) {
            logging_.push(Log.Type.HARMLESS, "Receive sync message");
            number_ = confirmDataSummary(msg);
            if (number_ == 0) {
                sendError("Receive number is 0");
                return JobResult.FAIL;
            }
            return JobResult.SUCCESS;
        } else if (msg.getType() == Message.MSG_DAT) {
            IWord word = dict_.createWord();
            //word.decodeFromString(msg.getValue());
            index_++;
            IWord orgWord = dict_.getWord(word.getID());
            if (orgWord == null) {
                logging_.push(Log.Type.WARNING, "Cannot find word: " + word.getContent());
                dict_.addWord(word);
            }
            else {
                orgWord.getMeanings().clear();
                for (IMeaning meaning : word.getMeanings()) {
                    orgWord.addMeaning(meaning);
                }
                orgWord.setNote(word.getNote());
//                orgWord.setTag(ITag.TAG_Cls, word.getTagValue(ITag.TAG_Cls));
                orgWord.setContent(word.getContent());
                orgWord.setKana(word.getKana());
                orgWord.setTone(word.getTone());
            }
            if (index_ == number_) {
                logging_.push(Log.Type.HARMLESS, "Received done");
            }
            return JobResult.SUCCESS;
        } else if (msg.getType() == Message.MSG_FIN) {
            logging_.push(Log.Type.HARMLESS, "Received FIN");
            try {
                dict_.saveToDB();
            } catch (Exception e) {
            }
            return JobResult.DONE;
        }
        return JobResult.FAIL;
    }
}
