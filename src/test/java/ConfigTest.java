import app.Config;

import java.io.FileNotFoundException;
import java.net.UnknownHostException;

public class ConfigTest {
    public static void main(String[] args) throws FileNotFoundException, UnknownHostException {
        Config conf = new Config();
        conf.readConfig();
        System.out.println(conf.getPort());
    }
}
