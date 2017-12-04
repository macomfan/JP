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

    public enum Method
    {
        SYNC_TO_MASTER,
        SYNC_FROM_MASTER,
    }
    
    private static Sync instance_;
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

    public void startAsSlave(IWordDictionary wordDictionary, Method method) {
        SlaveWorker slave = new SlaveWorker(wordDictionary, method);
        slave.start();
    }

    public IController runAsMaster() {
        MasterWorker master = new MasterWorker();
        master.start();
        return master;
    }
}
