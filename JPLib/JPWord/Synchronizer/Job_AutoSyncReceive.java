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
public class Job_AutoSyncReceive extends Job_Base {

    public Job_AutoSyncReceive(TCPCommunication tcp, IWordDictionary dict, Logging logging) {
        super(tcp, dict, logging);
    }

    @Override
    public JobResult doAction(Message msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void start() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
