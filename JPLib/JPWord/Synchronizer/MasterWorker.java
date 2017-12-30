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
    
    private IWordDictionary wordDictionary_ = null;
    private TCPCommunication tcp_ = null;
    private File file_ = null;
    BufferedWriter writer_ = null;
    Logging logging_ = null;
    private Job_Base currentJob_ = null;
    
    public MasterWorker(IWordDictionary wordDictionary) {
        wordDictionary_ = wordDictionary;
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
    
    @Override
    public void onReceive(Message msg) {
        logging_.push(Log.Type.HARMLESS, "Receive a message");
        
        if (currentJob_ != null) {
            Job_Base.JobResult result = currentJob_.doAction(msg);
            if (result == Job_Base.JobResult.FAIL) {
                logging_.push(Log.Type.FAILURE, "Job failed");
                tcp_.close();
                currentJob_ = null;
            } else if (result == Job_Base.JobResult.DONE) {
                logging_.push(Log.Type.SUCCESS, "Job finished");
                tcp_.close();
                currentJob_ = null;
            }
            return;
        }
        
        switch (msg.getType()) {
            case Message.MSG_SYN: {
                logging_.push(Log.Type.HARMLESS, "SYN received");
                String method = msg.getTag(Constant.METHOD);
                if (method.equals(Constant.REBASE_FROM_MASTER)) {
                    logging_.push(Log.Type.HARMLESS, "Work mode: REBASE FROM MASTER");
                    currentJob_ = new Job_RebaseSend(tcp_, wordDictionary_, logging_);
                    currentJob_.start();
                } else if (method.equals(Constant.REBASE_TO_MASTER)) {
                    logging_.push(Log.Type.HARMLESS, "Work mode: REBASE TO MASTER");
                    currentJob_ = new Job_RebaseReceive(tcp_, wordDictionary_, logging_);
                    currentJob_.start();
                } else if (method.equals(Constant.AUTO_SYNC)) {
                    logging_.push(Log.Type.HARMLESS, "Work mode: AUTO SYNC");
                    currentJob_ = new Job_AutoSyncReceive(tcp_, wordDictionary_, logging_);
                    currentJob_.start();
                } else {
                    logging_.push(Log.Type.FAILURE, "Work mode: UNKNOWN");
                    Message newMsg = new Message(Message.MSG_BYE);
                    newMsg.addTag(Constant.REASON, Constant.UNKNOW_METHOD);
                    tcp_.send(msg);
                    tcp_.close();
                }
//                file_ = new File("backup.dat");
//                Message newMsg = new Message(Message.MSG_ACK);
//                tcp_.send(newMsg);
                break;
            }
            case Message.MSG_DAT: {
                try {
                    
                } catch (Exception e) {
                    
                }

//                BufferedWriter writer = new BufferedWriter(
//                        new OutputStreamWriter(new FileOutputStream(file_), "utf-8"));
                System.out.println("DAT: " + msg.getValue());
                break;
            }
            case Message.MSG_ACK: {
                // Complete, Send bye

                Message newMsg = new Message(Message.MSG_BYE);
                tcp_.send(newMsg);
                System.out.println("ACK");
                break;
            }
        }
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
                tcp_.close();
                tcp_ = null;
                Thread.sleep(1000);
            }
            logging_.push(Log.Type.FAILURE, "Exit by user");
            socket.close();
        } catch (InterruptedException e) {
            logging_.push(Log.Type.FAILURE, "Exit by user");
        } catch (Exception e) {
            logging_.push(Log.Type.FAILURE, e.getMessage());
        }
        
    }
}
