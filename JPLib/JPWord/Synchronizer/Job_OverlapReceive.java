/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Synchronizer;

import JPWord.Data.Database;
import JPWord.Data.ITag;
import JPWord.Data.IWord;
import JPWord.Data.IWordDictionary;

/**
 *
 * @author u0151316
 */
public class Job_OverlapReceive extends Job_Base {

    private int number_ = 0;
    private int index_ = 0;
    private IWordDictionary dict_ = null;

    public Job_OverlapReceive(TCPCommunication tcp, String dictName, Logging logging) {
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
        return JobResult.SUCCESS;
    }

    @Override
    public JobResult doAction(Message msg) {
        if (msg.getType() == Message.MSG_SYN) {
            number_ = confirmDataSummary(msg);
            return JobResult.SUCCESS;
        } else if (msg.getType() == Message.MSG_DAT) {
            IWord word = dict_.createWord();
            word.decodeFromString(msg.getValue());
            index_++;
            IWord orgWord = dict_.getWord(word.getID());
            if (orgWord == null) {
                logging_.push(Log.Type.WARNING, "Cannot find word: " + word.getContent());
            } else {
                orgWord.setTag(ITag.TAG_Skill, word.getTagValue(ITag.TAG_Skill));
                orgWord.setTag(ITag.TAG_RD, word.getTagValue(ITag.TAG_RD));
                orgWord.setTag(ITag.TAG_MY, word.getTagValue(ITag.TAG_MY));
            }
            if (index_ == number_) {
                logging_.push(Log.Type.HARMLESS, "Received done");
            }
            return JobResult.SUCCESS;
        } else if (msg.getType() == Message.MSG_FIN) {
            logging_.push(Log.Type.HARMLESS, String.format("Send finished by sender, received %d", index_));
            try {
                dict_.save();
            } catch (Exception e) {
            }
            sendDataSummary(dict_);
            return JobResult.SUCCESS;
        } else if (msg.getType() == Message.MSG_ACK) {
            logging_.push(Log.Type.HARMLESS, "Get confirmed message");
            logging_.push(Log.Type.HARMLESS, "Sending content...");
            sendWholeData(dict_);
            return JobResult.SUCCESS;
        } else if (msg.getType() == Message.MSG_ERR) {
            return JobResult.FAIL;
        }
        return JobResult.FAIL;
    }
}
