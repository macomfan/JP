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
        EXPECTRD,
        DONE,
    }

    protected TCPCommunication tcp_ = null;
    protected IWordDictionary dict_ = null;
    protected Logging logging_ = null;

    public Job_Base(TCPCommunication tcp, IWordDictionary dict, Logging logging) {
        tcp_ = tcp;
        dict_ = dict;
        logging_ = logging;
    }

    public abstract JobResult doAction(Message msg);

    public abstract void start();
}
