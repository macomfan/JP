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
    public final static int TryTimes = 5;

    private Sync() {

    }

    public static Sync getInstance() {
        if (instance_ == null) {
            instance_ = new Sync();
        }
        return instance_;
    }

    public ILogging getDefaultLogging() {
        return logging_;
    }

    public ILogging createLogging() {
        return new Logging();
    }

    public void startAsSlave(SlaveParam param, Method method, ILogging logging) {
        if (param.masterSideDictname_.equals("") && param.slaveSideDictname_.equals("")) {
            return;
        }
        if (param.masterSideDictname_.equals("")) {
            param.masterSideDictname_ = param.slaveSideDictname_;
        }
        if (param.slaveSideDictname_.equals("")) {
            param.slaveSideDictname_ = param.masterSideDictname_;
        }
        SlaveWorker slave = new SlaveWorker(param.masterSideDictname_, param.slaveSideDictname_, method);
        slave.setLogging((Logging) logging);
        slave.start();
    }

    public void startAsSlave(SlaveParam param, Method method) {
        startAsSlave(param, method, logging_);
    }

    public IController runAsMaster() {
        return runAsMaster(logging_);
    }

    public IController runAsMaster(ILogging logging) {
        MasterWorker master = new MasterWorker();
        master.setLogging((Logging) logging);
        master.start();
        return master;
    }
}
