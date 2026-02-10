package networking;

import config.Config;
import utils.Logging;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;

/**
 * A class providing the necessary methods to send messages between Hadrian applications.
 * <p>
 * This class sends messages to the {@code Receiver} class on another device.
 * <p>
 * This class and {@code Receiver} class must both be set up on both devices to send messages between them.
 *
 * @see Receiver
 */
public class Sender {
    private Config conf;
    //Making this multithreaded could make it easier for an attacker to try to brute force multiple message attempts
    //private Thread sender_thread;
    
    public Sender(Config conf) {
        this.conf = conf;
    }
    
    /**
     * Sends a specified message to a specified IP address that has a receiver thread running.
     *
     * @param user_message message to be sent to receiver
     * @param IP IP address of the receiver device
     * @return true if message is sent successfully, false otherwise
     * @see DatagramSocket
     * @see DatagramPacket
     * @see Receiver
     */
    public boolean sendMessage(String user_message, InetAddress IP) {
        byte[] message = user_message.getBytes();
        DatagramSocket outSocket;
        try {
            outSocket = new DatagramSocket();
            outSocket.setBroadcast(true);
        } catch (SocketException e) {
            Logging.log("Failed To Set Up Socket", Level.SEVERE, e);
            return false;
        }
        
        DatagramPacket packet = new DatagramPacket(
                message,
                message.length,
                IP,
                conf.getPort()
        );
        
        //TODO encrypt, send, find factors, decrypt, confirm, get + store usernames, etc.
        try {
            outSocket.send(packet);
        } catch (IOException e) {
            Logging.log("Failed To Send Message", Level.SEVERE, e);
            return false;
        }
        
        return true;
    }
    
    public Config getConf() {
        return conf;
    }
    
    public void setConf(Config conf) {
        this.conf = conf;
    }
}
