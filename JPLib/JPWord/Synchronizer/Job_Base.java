/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Synchronizer;

import JPWord.Data.IWordDictionary;

/**
 *
 * @author u0151316
 */
abstract class Job_Base {

    public enum JobResult
    {
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
}
