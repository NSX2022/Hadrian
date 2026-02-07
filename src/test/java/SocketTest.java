// Source - https://stackoverflow.com/a/40058144
// Posted by user3841581
// Retrieved 2026-02-05, License - CC BY-SA 3.0

import config.Config;
import networking.Receiver;
import networking.Sender;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class SocketTest {
    private static Config conf;
    public static void Main(String[] args){
        conf = new Config();
        try {
            conf.read_config();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Receiver receiver = new Receiver(conf);
        Sender sender = new Sender(conf);

        receiver.startServer();
        receiver.setOpen(true);

        InetAddress toSend;
        Scanner wait = new Scanner(System.in);
        try {
            //Replace with my IP address
            toSend = InetAddress.getByName("172.27.244.157");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        sender.sendMessage("SAVE THE CHICKENS", toSend);
    }
}
