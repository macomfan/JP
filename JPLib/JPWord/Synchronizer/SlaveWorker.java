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
    private IWordDictionary dictionary_ = null;
    private Method method_;
    private Job_Base currentJob_ = null;

    Logging logging_ = null;

    public void setLogging(Logging logging) {
        logging_ = logging;
    }

    public SlaveWorker(IWordDictionary wordDictionary, Method method) {
        dictionary_ = wordDictionary;
        method_ = method;
    }

    @Override
    public void onConnect() {
        logging_.push(Log.Type.SUCCESS, "Connected to master");
        Message msg = new Message(Message.MSG_SYN);
        msg.setValue("Hello");
        msg.addTag("METHOD", method_.getValue());
        logging_.push(Log.Type.HARMLESS, "Send request to master");
        tcp_.send(msg);
    }

    @Override
    public void onReceive(Message msg) {
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

        System.out.println("Msg --");
        switch (msg.getType()) {
            case Message.MSG_ACK: {
                logging_.push(Log.Type.SUCCESS, "ACK received");
                Method method = new Method(msg.getTag(Constant.METHOD));
                if (method.is(Method.AUTO_SYNC)) {
                    currentJob_ = new Job_AutoSyncSend(tcp_, dictionary_, logging_);
                    currentJob_.start();
                } else if (method.is(Method.REBASE_FROM_MASTER)) {
                    currentJob_ = new Job_RebaseReceive(tcp_, dictionary_, logging_);
                    currentJob_.start();
                } else if (method.is(Method.REBASE_TO_MASTER)) {
                    currentJob_ = new Job_RebaseSend(tcp_, dictionary_, logging_);
                    currentJob_.start();
                }
                break;
            }

            case Message.MSG_BYE: {
                // Complete, close
                logging_.push(Log.Type.WARNING, "BYE received");
                tcp_.close();
                break;
            }

        }
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
            Message msg = new Message(Message.MSG_DETECT);
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
            tcp_.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
