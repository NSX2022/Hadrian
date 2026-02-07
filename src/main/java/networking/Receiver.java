package networking;
import config.Config;
import utils.Primes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Receiver {
    //Whether or not to listen on the port
    private boolean open = false;
    private Config conf;
    private Thread server_thread;

    public Receiver(Config conf) {
        this.conf = conf;
    }

    /**
     * Returns a thread that runs the message listener. The listener in the current thread cannot be
     * updated (e.g, which ports it listens to), the thread must be stopped and updated
     * (Done by stopServer() by default)
     * @return
     * @throws UnknownHostException
     * @throws FileNotFoundException
     */
    public Thread serverFactory() throws UnknownHostException {
        //For loading config changes while the program is executing, kill the current server/thread and start a new one
        try {
            conf.read_config();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return (new Thread(() -> {
            DatagramSocket socket;
            try {
                socket = new DatagramSocket(conf.getPort());
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }

            DatagramPacket packet =
                    new DatagramPacket(new byte[conf.getMax_bytes_per_message()], conf.getMax_bytes_per_message());

            String message_holder;
            //TODO: Switch to BigDecimal, difficulty system

            while(!this.server_thread.isInterrupted()){
                //TODO Check message header, Send + check pub_num et al, Decrypt, get + send confirmation/username
                try {
                    long factors[] = Primes.generatePrimes(5);
                    long pub_num = factors[0] * factors[1];


                    socket.receive(packet);
                    message_holder = new String(packet.getData());

                    //TODO message handling
                    System.out.println(message_holder);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }));
    }


    public void startServer() {
        try {
            server_thread = serverFactory();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        server_thread.start();
    }

    public void stopServer() {
        server_thread.interrupt();
        try {
            server_thread = serverFactory();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Config getConf() {
        return conf;
    }

    public void setConf(Config conf) {
        this.conf = conf;
    }

    public Thread getServer_thread() {
        return server_thread;
    }
}
