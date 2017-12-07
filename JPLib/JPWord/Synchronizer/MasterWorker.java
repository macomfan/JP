/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Synchronizer;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
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
    boolean isClosed_ = false;

    public void setLogging(Logging logging) {
        logging_ = logging;
    }

    @Override
    public void onConnect() {
        // DO nothing
    }

    @Override
    public void stopWorker() {
        this.interrupt();
    }

    @Override
    public boolean isClosed() {
        return isClosed_;
    }

    @Override
    public void onReceive(Message msg) {
        switch (msg.getType()) {
            case Message.MSG_SYN: {
                // Get number and Send ACK
                System.out.println("SYN");
                file_ = new File("backup.dat");
                Message newMsg = new Message(Message.MSG_ACK);
                tcp_.send(newMsg);
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
            isClosed_ = false;
            logging_.push("[N] Server started...");
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
                System.out.println("received broadcast...");
                // Check, start a thread to connect slave
                tcp_ = new TCPCommunication();
                tcp_.setCallback(this);
                tcp_.connect(dp.getAddress(), Sync.TCPPort);
                tcp_.receive();
                //System.out.println("Get: " + " 1:" + dp.getAddress() + " 2:" + dp.getSocketAddress());
                tcp_.close();
                tcp_ = null;
                Thread.sleep(1000);
            }
            isClosed_ = true;
            logging_.push("[E] Exit by user");
            socket.close();
        } catch (InterruptedException e) {
            isClosed_ = true;
            logging_.push("[E] Exit by user");
        } catch (Exception e) {
            isClosed_ = true;
            logging_.push("[E] " + e.getMessage());
        }
        isClosed_ = true;
    }
}
