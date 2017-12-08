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
public class Sync {

    public enum Method {
        REBASE_TO_MASTER,
        REBASE_FROM_MASTER,
        AUTO_SYNC,
    }

    private static Sync instance_;
    private Logging logging_ = new Logging();
    public final static int BroadcastPort = 13998;
    public final static int TCPPort = 13999;
    public final static int TryTimes = 10;

    private Sync() {

    }

    public static Sync getInstance() {
        if (instance_ == null) {
            instance_ = new Sync();
        }
        return instance_;
    }

    public ILogging getLogging() {
        return logging_;
    }

    public void startAsSlave(IWordDictionary wordDictionary, Method method) {
        SlaveWorker slave = new SlaveWorker(wordDictionary, method);
        slave.setLogging(logging_);
        slave.start();
    }

    public IController runAsMaster(IWordDictionary wordDictionary) {
        MasterWorker master = new MasterWorker(wordDictionary);
        master.setLogging(logging_);
        master.start();
        return master;
    }
}
