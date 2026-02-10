package networking;

import config.Config;
import utils.Logging;
import utils.Primes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;

public class Receiver {
    //Whether or not to listen on the port
    private boolean open = false;
    private Config conf;
    private Thread serverThread;
    
    public Receiver(Config conf) {
        this.conf = conf;
    }
    
    /**
     * Reads the configuration and creates a thread to manage message listening.
     * <p>
     * Thread can only be stopped or updated using the appropriate provided methods.
     *
     * @return message listener thread
     * @throws FileNotFoundException when config file cannot be found
     * @see #startServer()
     * @see #stopServer()
     */
    private Thread serverFactory() throws FileNotFoundException {
        //For loading config changes while the program is executing, kill the current server/thread and start a new one
        conf.readConfig();
        
        return new Thread(() -> {
            DatagramSocket socket;
            try {
                socket = new DatagramSocket(conf.getPort());
            } catch (SocketException e) {
                Logging.log("Failed To Initialize Server Thread", Level.SEVERE, e);
                throw new RuntimeException(e);
            }
            
            DatagramPacket packet =
                    new DatagramPacket(new byte[conf.getMaxBytesPerMessage()], conf.getMaxBytesPerMessage());
            
            String messageHolder;
            //TODO: Switch to BigDecimal, difficulty system
            
            while (!serverThread.isInterrupted()) {
                //TODO Check message header, Send + check pub_num et al, Decrypt, get + send confirmation/username
                try {
                    long[] factors = Primes.generatePrimes(5);
                    long pubNum = factors[0] * factors[1];
                    
                    socket.receive(packet);
                    messageHolder = new String(packet.getData());
                    
                    //TODO message handling
                    System.out.println(messageHolder);
                } catch (IOException e) {
                    Logging.log("Failed To Receive Packets", Level.SEVERE, e);
                    throw new RuntimeException(e);
                }
            }
            
            socket.close();
        });
    }
    
    /**
     * Creates the server thread, saves it to a local variable, and starts it.
     *
     * @throws FileNotFoundException when config file cannot be found
     * @see #serverFactory()
     * @see #stopServer()
     */
    public void startServer() throws FileNotFoundException {
        serverThread = serverFactory();
        serverThread.start();
    }
    
    /**
     * Stops the server thread.
     *
     * @see #serverFactory()
     * @see #startServer()
     */
    public void stopServer() {
        serverThread.interrupt();
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
    
    public Thread getServerThread() {
        return serverThread;
    }
}
