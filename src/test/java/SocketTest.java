// Source - https://stackoverflow.com/a/40058144
// Posted by user3841581
// Retrieved 2026-02-05, License - CC BY-SA 3.0

import java.io.IOException;
import java.net.*;

public class SocketTest {
    private boolean run = true;


    public static void main(String[] args) throws IOException {

        startServer();
        startSender();
    }

    public static void startSender() throws UnknownHostException{
        InetAddress aHost = InetAddress.getLocalHost();
        (new Thread() {
            @Override
            public void run() {
                byte data[] = "Hello".getBytes();
                DatagramSocket socket = null;
                try {
                    socket = new DatagramSocket();
                    socket.setBroadcast(true);
                } catch (SocketException ex) {
                    ex.printStackTrace();
                    //parent.quit();
                }

                DatagramPacket packet = new DatagramPacket(
                        data,
                        data.length,
                        aHost,
                        9090);

                int i=0;
                while (i<10) {
                    try {
                        System.out.println("SENDER packet data: "+new String(packet.getData()));
                        socket.send(packet);
                        Thread.sleep(50);
                        i++;
                        System.out.println(i);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        // parent.quit();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                        // parent.quit();
                    }
                }
            }}).start();
    }


    public static void startServer() {
        (new Thread(() -> {

            //byte data[] = new byte[0];
            DatagramSocket socket = null;
            try {
                socket = new DatagramSocket(9090);
                //socket.setBroadcast(true);;

            } catch (SocketException ex) {
                ex.printStackTrace();
                //parent.quit();
            }
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            //System.out.println("this is what has been received111"+packet.getData());
            String temp;
            while (true) {
                try {
                    socket.receive(packet);
                    temp=new String(packet.getData());
                    System.out.println("RECEIVER: Raw packet data:: "+temp);


                    //System.out.println("Message received ..."+ temp);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    //parent.quit();
                }

            }
        })).start();
    }
}
