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
class Logging implements ILogging{

    Queue<String> logs_ = new ArrayBlockingQueue<>(1000);
    
    public Logging() {

    }
    
    public synchronized void push(String msg)
    {
        logs_.add(msg);
        System.out.println(msg);
    }
    
    public synchronized String pop()
    {
        if (logs_.isEmpty()) {
            return null;
        }
        return logs_.poll();
    }
}
