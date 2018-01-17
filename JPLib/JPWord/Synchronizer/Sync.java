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

    public void startAsSlave(String dictName, Method method) {
        logging_ = new Logging();
        SlaveWorker slave = new SlaveWorker(dictName, method);
        slave.setLogging(logging_);
        slave.start();
    }

    public IController runAsMaster() {
        MasterWorker master = new MasterWorker();
        master.setLogging(logging_);
        master.start();
        return master;
    }
}
