import java.net.InetAddress;
import java.net.UnknownHostException;

public class CLI {
    public static void main(String[] args) throws UnknownHostException {
        System.out.println(InetAddress.getLocalHost().getHostName());
        System.out.println(InetAddress.getLoopbackAddress().getHostAddress());
        System.out.println(InetAddress.getByName("8.8.8.8").getHostName());
        
        InetAddress localHost = InetAddress.getLocalHost();
        InetAddress address = InetAddress.getByName(String.valueOf(localHost.getHostAddress()));
        System.out.println(localHost.getHostAddress());
        System.out.println(address.getHostAddress());
        System.out.println(address.getHostAddress().equals(localHost.getHostAddress()) ? "me" : address);
    }
}
