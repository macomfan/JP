/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Synchronizer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author u0151316
 */
class SlaveWorker extends Thread implements ITCPCallback, IController {

    private TCPCommunication tcp_ = new TCPCommunication();
    private String slaveSideDictname_ = "";
    private String masterSideDictname_ = "";
    private Method method_;
    private Job_Base currentJob_ = null;
    
    private static final int ERROR_ = 1;

    Logging logging_ = null;

    public void setLogging(Logging logging) {
        logging_ = logging;
    }

    public SlaveWorker(String masterSideDictname, String slaveSideDictname, Method method) {
        masterSideDictname_ = masterSideDictname;
        slaveSideDictname_ = slaveSideDictname;
        method_ = method;
    }

    @Override
    public void onConnect() {
        logging_.push(Log.Type.SUCCESS, "Connected to master");
        Message msg = new Message(Message.SYS_REQUEST);
        try {
            msg.setValue("Hello".getBytes("UTF-8"));
        } catch (Exception e) {
        }
        msg.addTag(Constant.METHOD, method_.getStringValue());
        msg.addTag(Constant.DICTNAME, masterSideDictname_);
        logging_.push(Log.Type.HARMLESS, "Send request to master, sync mode is " + method_.getStringValue());

        if (method_.is(Method.REBASE_FROM_MASTER)) {
            currentJob_ = new Job_RebaseReceive(tcp_, slaveSideDictname_, logging_);
        } else if (method_.is(Method.REBASE_TO_MASTER)) {
            currentJob_ = new Job_RebaseSend(tcp_, slaveSideDictname_, logging_);
        } else if (method_.is(Method.OVERLAP)) {
            currentJob_ = new Job_OverlapSend(tcp_, slaveSideDictname_, logging_);
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
        logging_.push(Log.Type.SUCCESS, "Start as slave ...");
        try {
            tcp_.setCallback(this);
            tcp_.listen();
            String host = "255.255.255.255";
            InetAddress adds = InetAddress.getByName(host);
            Message msg = new Message(Message.SYS_DETECT);
            byte[] buf = msg.toByteArray();
            DatagramSocket socket = new DatagramSocket();
            boolean connected = false;
            for (int i = 0; i < Sync.TryTimes; i++) {
                DatagramPacket dp = new DatagramPacket(buf, buf.length, adds, Sync.BroadcastPort);
                socket.send(dp);
                logging_.push(Log.Type.HARMLESS, "Finding master services ...");
                Thread.sleep(1000);
                if (tcp_.getStatus() == TCPCommunication.Status.CONNECTED) {
                    connected = true;
                    tcp_.receive();
                    break;
                }
            }
            if (connected) {
                logging_.push(Log.Type.SUCCESS, "Done");
            }
            else {
                logging_.push(Log.Type.FAILURE, "Cannot find master");
            }
            logging_.setJobDone();
            closeJob();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
