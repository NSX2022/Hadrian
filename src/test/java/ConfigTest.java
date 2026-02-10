import config.Config;

import java.io.FileNotFoundException;

public class ConfigTest {
    public static void main(String[] args) throws FileNotFoundException {
        Config conf = new Config();
        conf.readConfig();
        System.out.println(conf.getPort());
    }
}
