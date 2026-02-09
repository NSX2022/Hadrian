package networking;
import config.Config;
import utils.Primes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Sender {
    private Config conf;
    //Making this multithreaded could make it easier for an attacker to try to brute force multiple message attempts
    //private Thread sender_thread;

    public Sender(Config conf){
        this.conf = conf;
    }

    //Decided to make this return false rather than an exception when a message attempt fails
    //TODO
    /**
     *
     * @param user_message
     * @param IP
     * @return
     */
    public boolean sendMessage(String user_message, InetAddress IP){
        byte message[] = user_message.getBytes();
        DatagramSocket out_socket = null;
        try {
            out_socket = new DatagramSocket();
            out_socket.setBroadcast(true);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        DatagramPacket packet = new DatagramPacket(
                message,
                message.length,
                IP,
                conf.getPort()
        );

        //TODO encrypt, send, find factors, decrypt, confirm, get + store usernames, etc.
        try {
            out_socket.send(packet);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
