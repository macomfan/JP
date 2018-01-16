/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Synchronizer;

import JPWord.Data.IWordDictionary;
import java.io.File;
import java.io.BufferedWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

/**
 *
 * @author u0151316
 */
class MasterWorker extends Thread implements ITCPCallback, IController {
    
    private TCPCommunication tcp_ = null;
    private File file_ = null;
    BufferedWriter writer_ = null;
    Logging logging_ = null;
    private Job_Base currentJob_ = null;
    
    public MasterWorker() {
    }
    
    public void setLogging(Logging logging) {
        logging_ = logging;
    }
    
    @Override
    public void onConnect() {
        logging_.push(Log.Type.HARMLESS, "Start connection");
    }
    
    @Override
    public void stopWorker() {
        this.interrupt();
    }
    
    private void processSystemMessage(Message msg) {
        switch (msg.getType()) {
            case Message.SYS_REQUEST: {
                logging_.push(Log.Type.HARMLESS, "REQUEST received");
                Method method = new Method(msg.getTag(Constant.METHOD));
                String dictName = msg.getTag(Constant.DICTNAME);
                Message newMsg = new Message(Message.SYS_RESPONSE);
                
                if (method.is(Method.REBASE_FROM_MASTER)) {
                    logging_.push(Log.Type.HARMLESS, "Work mode: REBASE FROM MASTER");
                    currentJob_ = new Job_RebaseSend(tcp_, dictName, logging_);
                } else if (method.is(Method.REBASE_TO_MASTER)) {
                    logging_.push(Log.Type.HARMLESS, "Work mode: REBASE TO MASTER");
                    currentJob_ = new Job_RebaseReceive(tcp_, dictName, logging_);
                } else if (method.is(Method.AUTO_SYNC)) {
                    logging_.push(Log.Type.HARMLESS, "Work mode: AUTO SYNC");
                    currentJob_ = new Job_AutoSyncReceive(tcp_, dictName, logging_);
                } else if (method.is(Method.OVERLAP)) {
                    logging_.push(Log.Type.HARMLESS, "Work mode: OVERLAP");
                    currentJob_ = new Job_OverlapReceive(tcp_, dictName, logging_);
                } else {
                    logging_.push(Log.Type.FAILURE, "Work mode: UNKNOWN");
                    Message byeMsg = new Message(Message.SYS_BYE);
                    newMsg.addTag(Constant.REASON, Constant.UNKNOW_METHOD);
                    tcp_.send(byeMsg);
                    tcp_.close();
                    return;
                }
                if (currentJob_ != null) {
                    if (currentJob_.start() != Job_Base.JobResult.SUCCESS) {
                        closeJob();
                    }
                }
                newMsg.addTag(Constant.METHOD, method.getValue());
                tcp_.send(newMsg);
                file_ = new File("backup.dat");
                break;
            }
        }
    }
    
    private void processJobMessage(Message msg) {
        if (currentJob_ != null) {
            Job_Base.JobResult result = currentJob_.doAction(msg);
            if (result == Job_Base.JobResult.FAIL) {
                logging_.push(Log.Type.FAILURE, "Job failed");
                closeJob();
            } else if (result == Job_Base.JobResult.DONE) {
                logging_.push(Log.Type.SUCCESS, "Job finished");
                closeJob();
            }
        }
    }
    
    @Override
    public void onReceive(Message msg) {
        //logging_.push(Log.Type.HARMLESS, "Receive a message");
        if (msg.isSystemMessage()) {
            processSystemMessage(msg);
        } else {
            processJobMessage(msg);
        }
    }
    
    private void closeJob() {
        tcp_.close();
        currentJob_ = null;
    }
    
    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.
        try {
            logging_.push(Log.Type.HARMLESS, "Server started...");
            DatagramSocket socket = new DatagramSocket(Sync.BroadcastPort);
            byte[] buf = new byte[1024];
            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            socket.setSoTimeout(200);
            while (!this.isInterrupted()) {
                try {
                    socket.receive(dp);
                } catch (SocketTimeoutException e) {
                    continue;
                }
                
                byte[] data = dp.getData();
                Message msg = new Message();
                ByteBuffer byteBuffer = ByteBuffer.allocate(data.length);
                byteBuffer.put(data);
                byteBuffer.rewind();
                if (msg.Parse(byteBuffer) == 0) {
                    continue;
                }
                logging_.push(Log.Type.SUCCESS, "Received broadcast");
                // Check, start a thread to connect slave
                tcp_ = new TCPCommunication();
                tcp_.setCallback(this);
                tcp_.connect(dp.getAddress(), Sync.TCPPort);
                tcp_.receive();
                closeJob();
                Thread.sleep(1000);
            }
            logging_.push(Log.Type.FAILURE, "Exit by user");
            socket.close();
        } catch (InterruptedException e) {
            closeJob();
            logging_.push(Log.Type.FAILURE, "Exit by user");
        } catch (Exception e) {
            closeJob();
            logging_.push(Log.Type.FAILURE, e.getMessage());
        }
        
    }
}
