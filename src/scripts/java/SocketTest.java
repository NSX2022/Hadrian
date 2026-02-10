// Source - https://stackoverflow.com/a/40058144
// Posted by user3841581
// Retrieved 2026-02-05, License - CC BY-SA 3.0

import config.Config;
import networking.Receiver;
import networking.Sender;

import java.io.FileNotFoundException;
import java.net.*;
import java.util.Scanner;

public class SocketTest {
    private static Config conf;
    public static void main(String[] args){
        conf = new Config();
        try {
            conf.read_config();
        } catch (FileNotFoundException e) {
            System.out.println(SocketTest.class.getProtectionDomain().getCodeSource().getLocation());
            throw new RuntimeException(e);
        }

        Receiver receiver = new Receiver(conf);
        Sender sender = new Sender(conf);

        receiver.setOpen(true);
        receiver.startServer();

        InetAddress toSend;
        //Press ENTER when ready to send
        Scanner wait = new Scanner(System.in);
        try {
            //Replace with my IP address
            toSend = InetAddress.getByName("10.3.30.137");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        do {
            System.out.print(">");
            System.out.flush();
            sender.sendMessage(wait.nextLine(), toSend);
        } while(wait.hasNextLine());

        receiver.stopServer();
    }
}
