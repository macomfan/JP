/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Synchronizer;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author u0151316
 */
class Logging implements ILogging {
    
    Queue<Log> logs_ = new ArrayBlockingQueue<>(1000);
    
    private boolean mJobDone = false;
    
    public Logging() {
        
    }
    
    public synchronized void push(Log msg) {
        logs_.add(msg);
        System.out.println("[SYNC LOG]  " + msg.what());
    }
    
    public synchronized void push(Log.Type type, String what) {
        push(new Log(type, what));
    }
    
    public void setJobDone() {
        mJobDone = true;
    }
    
    @Override
    public synchronized Log pop() {
        if (logs_.isEmpty()) {
            return null;
        }
        return logs_.poll();
    }
    
    @Override
    public boolean isJobDone() {
        return mJobDone;
    }
}
