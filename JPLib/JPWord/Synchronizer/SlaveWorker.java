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
class SlaveWorker extends Thread implements ITCPCallback {

    private TCPCommunication tcp_ = new TCPCommunication();
    private IWordDictionary dictionary_ = null;
    private Sync.Method method_;

    public SlaveWorker(IWordDictionary wordDictionary, Sync.Method method) {
        dictionary_ = wordDictionary;
        method_ = method;
    }

    @Override
    public void onConnect() {
        Message msg = new Message(Message.MSG_SYN);
        msg.setValue("AAAAAAAAA");
        System.out.println("Sending via TCP");
        tcp_.send(msg);
    }

    @Override
    public void onReceive(Message msg) {
        System.out.println("Msg --");
        switch (msg.getType()) {
            case Message.MSG_ACK: {
                // Send data
                System.out.println("ACK");

                for (IWord w : dictionary_.getWords()) {
                    Message newMsg = new Message(Message.MSG_DAT);
                    String line = w.encodeToString();
                    if (line != null && !line.equals("")) {
                        newMsg.setValue(line);
                    }
                    tcp_.send(newMsg);
                }

                Message ack = new Message(Message.MSG_ACK);
                tcp_.send(ack);
                System.out.println("Sent ACK");
                break;
            }

            case Message.MSG_BYE: {
                // Complete, close
                System.out.println("BYE");
                tcp_.close();
                break;
            }

        }
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
                System.out.println("Sending... ");
                Thread.sleep(1000);
                if (tcp_.getStatus() == TCPCommunication.Status.CONNECTED) {
                    tcp_.receive();
                    break;
                }
            }
            System.out.println("Done");
            tcp_.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
