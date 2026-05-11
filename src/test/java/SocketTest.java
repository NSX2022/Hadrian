import app.Config;
import network.Receiver;
import network.Sender;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class SocketTest {
    private static Config conf;

    public static void main(String[] args) throws FileNotFoundException, UnknownHostException {
        try {
            conf = new Config();
        } catch (IOException e) {
            System.out.println(SocketTest.class.getProtectionDomain().getCodeSource().getLocation());
            throw new RuntimeException(e);
        }

        Receiver receiver = new Receiver(conf);
        Sender sender = new Sender(conf);

        receiver.setOpen(true);
        receiver.startServer();

        Scanner input = new Scanner(System.in);

        // Ask for the peer IP at startup instead of hardcoding it.
        // Run `ip addr` (Linux/macOS) or `ipconfig` (Windows) on the peer machine to find it.
        System.out.print("Enter peer IP address: ");
        InetAddress toSend;
        try {
            toSend = InetAddress.getByName(input.nextLine().trim());
        } catch (UnknownHostException e) {
            System.out.println("Invalid IP address.");
            receiver.stopServer();
            return;
        }

        System.out.println("Sending as: " + conf.getUsername());
        System.out.println("Listening on port: " + conf.getPort());
        System.out.println("Sending to: " + toSend.getHostAddress());
        System.out.println("Type a message and press Enter to send. Ctrl+C to quit.\n");

        while (input.hasNextLine()) {
            System.out.print("\n> ");
            String message = input.nextLine();
            if (message.isBlank()) continue;

            boolean sent = sender.sendMessage(message, toSend);
            if (!sent) {
                System.out.println("[!] Failed to send message — check that the peer is reachable and listening.");
            }
        }

        receiver.stopServer();
    }
}
