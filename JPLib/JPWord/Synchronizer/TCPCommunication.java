/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Synchronizer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author u0151316
 */
final class TCPCommunication {

    class Listener extends Thread {

        private TCPCommunication p_ = null;

        public Listener(TCPCommunication p) {
            p_ = p;
        }

        @Override
        public void run() {
            try {
                if (p_ == null) {
                    return;
                }
                p_.serverSocket_ = new ServerSocket(Sync.TCPPort);
                Socket socket = p_.serverSocket_.accept();
                p_.socket_ = socket;
                p_.socket_.setSendBufferSize(1024 * 1024 * 2);
                p_.socket_.setReceiveBufferSize(1024 * 1024 * 2);
                p_.socket_.setKeepAlive(true);
                p_.status_ = Status.CONNECTED;
                p_.onConnected();
            } catch (Exception e) {
            }

        }

    }

    enum Status {
        LISTENING,
        CONNECTED,
        CLOSED,
    }
    private Socket socket_ = null;
    private ServerSocket serverSocket_ = null;
    private Status status_ = Status.CLOSED;
    private ITCPCallback onEventCallback_ = null;

    private InputStream inputStream_ = null;
    private OutputStream outputStream_ = null;

    private ByteBuffer partialBufferHandler_ = ByteBuffer.allocate(1024 * 4);

    public TCPCommunication() {
    }

    public void setCallback(ITCPCallback callback) {
        onEventCallback_ = callback;
    }

    public void listen() {
        try {
            Listener listener = new Listener(this);
            listener.start();
            status_ = Status.LISTENING;
        } catch (Exception e) {
        }
    }

    public void onConnected() {
        System.out.println("On Connected");
        status_ = Status.CONNECTED;
        try {
            inputStream_ = socket_.getInputStream();
            outputStream_ = socket_.getOutputStream();
        } catch (Exception e) {
        }

        if (onEventCallback_ != null) {
            onEventCallback_.onConnect();
        }
    }

    public void connect(InetAddress address, int port) {
        try {
            System.out.println("Trying...");
            socket_ = new Socket(address, port);
            socket_.setSendBufferSize(1024 * 1024 * 2);
            socket_.setReceiveBufferSize(1024 * 1024 * 2);
            socket_.setKeepAlive(true);
            System.out.println("Connect OK...");
            onConnected();
        } catch (Exception e) {
        }
    }

    public void receive() {
        try {
            InputStream inputStream = socket_.getInputStream();
            BufferedInputStream reader = new BufferedInputStream(inputStream);
            byte[] buf = new byte[1024 * 1024];
            int size = 0;
            do {
                size = reader.read(buf);
                System.out.println(Integer.toString(size, 10));
                if (size == 0) {
                    continue;
                }
                ByteBuffer tempBuffer;
                if (partialBufferHandler_.position() != 0) {
                    tempBuffer = ByteBuffer.allocate(size + partialBufferHandler_.position());
                    tempBuffer.put(partialBufferHandler_.array(), 0, partialBufferHandler_.position());
                    partialBufferHandler_.clear();
                } else {
                    tempBuffer = ByteBuffer.allocate(size);
                }
                tempBuffer.put(buf, 0, size);
                tempBuffer.rewind();
                while (tempBuffer.remaining() > 0) {
                    Message msg = new Message();
                    int position = tempBuffer.position();
                    int processedSize = msg.Parse(tempBuffer);
                    if (processedSize == 0) {
                        tempBuffer.position(position);
                        partialBufferHandler_.put(tempBuffer);
                        break;
                    } else {
                        partialBufferHandler_.clear();
                    }
                    if (onEventCallback_ != null) {
                        onEventCallback_.onReceive(msg);
                    }
                }

            } while (size != 0 && size != -1);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("2 Error: " + e.getMessage());
        }

    }

    public Status getStatus() {
        return status_;
    }

    public void send(Message msg) {
        try {
            System.err.println(String.format("----Send msg %d", msg.getType()));
            outputStream_.write(msg.toByteArray());
            outputStream_.flush();
            try {
                Thread.sleep(1);
            } catch (Exception e) {
            }
        } catch (IOException ex) {
            Logger.getLogger(TCPCommunication.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() {
        if (status_ != Status.CLOSED) {
            try {
                System.out.println("TCP CLOSING !!!!!!");
                if (inputStream_ != null) {
                    inputStream_.close();
                }
                if (outputStream_ != null) {
                    outputStream_.close();
                }
                if (socket_ != null) {
                    socket_.close();
                }
                if (serverSocket_ != null) {
                    serverSocket_.close();
                }
            } catch (Exception e) {
                System.out.println("3 Error :" + e.getMessage());
            }
            status_ = Status.CLOSED;
        }
    }
}
