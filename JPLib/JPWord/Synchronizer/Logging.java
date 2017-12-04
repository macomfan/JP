/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Synchronizer;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author u0151316
 */
class Logging implements ILogging{

    Stack<String> logs_ = new Stack<>();
    
    public Logging() {

    }
    
    public synchronized void push(String msg)
    {
        logs_.push(msg);
        System.out.println(msg);
    }
    
    public synchronized String pop()
    {
        if (logs_.isEmpty()) {
            return null;
        }
        return logs_.pop();
    }
}
