/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Synchronizer;

import JPWord.Data.Database;
import JPWord.Data.ITag;
import JPWord.Data.IWord;
import JPWord.Data.IWordDictionary;
import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author u0151316
 */
class SlaveWorker extends Thread implements ITCPCallback, IController {

    private TCPCommunication tcp_ = new TCPCommunication();
    private String dictName_ = "";
    private Method method_;
    private Job_Base currentJob_ = null;

    Logging logging_ = null;

    public void setLogging(Logging logging) {
        logging_ = logging;
    }

    public SlaveWorker(String dictName, Method method) {
        dictName_ = dictName;
        method_ = method;
    }

    @Override
    public void onConnect() {
        logging_.push(Log.Type.SUCCESS, "Connected to master");
        Message msg = new Message(Message.SYS_REQUEST);
        msg.setValue("Hello");
        msg.addTag(Constant.METHOD, method_.getValue());
        msg.addTag(Constant.DICTNAME, dictName_);
        logging_.push(Log.Type.HARMLESS, "Send request to master");

        if (method_.is(Method.AUTO_SYNC)) {
            currentJob_ = new Job_AutoSyncSend(tcp_, dictName_, logging_);
        } else if (method_.is(Method.REBASE_FROM_MASTER)) {
            currentJob_ = new Job_RebaseReceive(tcp_, dictName_, logging_);
        } else if (method_.is(Method.REBASE_TO_MASTER)) {
            currentJob_ = new Job_RebaseSend(tcp_, dictName_, logging_);
        } else if (method_.is(Method.OVERLAP)) {
            currentJob_ = new Job_OverlapSend(tcp_, dictName_, logging_);
        }
        tcp_.send(msg);
    }

    private void processSystemMessage(Message msg) {
        switch (msg.getType()) {
            case Message.SYS_RESPONSE: {
                logging_.push(Log.Type.SUCCESS, "RESPONSE received");
                if (currentJob_ != null) {
                    if (currentJob_.start() != Job_Base.JobResult.SUCCESS) {
                        closeJob();
                    }
                }
                break;
            }
            case Message.SYS_BYE: {
                // Complete, close
                logging_.push(Log.Type.WARNING, "BYE received");
                currentJob_ = null;
                tcp_.close();
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
    public void stopWorker() {
        this.interrupt();
    }

    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.
        try {
            tcp_.setCallback(this);
            tcp_.listen();
            String host = "255.255.255.255";
            InetAddress adds = InetAddress.getByName(host);
            Message msg = new Message(Message.SYS_DETECT);
            byte[] buf = msg.toByteArray();
            DatagramSocket socket = new DatagramSocket();
            for (int i = 0; i < Sync.TryTimes; i++) {
                DatagramPacket dp = new DatagramPacket(buf, buf.length, adds, Sync.BroadcastPort);
                socket.send(dp);
                logging_.push(Log.Type.HARMLESS, "Sending...");
                Thread.sleep(1000);
                if (tcp_.getStatus() == TCPCommunication.Status.CONNECTED) {
                    tcp_.receive();
                    break;
                }
            }
            logging_.push(Log.Type.HARMLESS, "Done");
            closeJob();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
