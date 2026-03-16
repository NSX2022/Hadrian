import app.Config;

import java.io.IOException;

public class ConfigTest {
    public static void main(String[] args) throws IOException {
        Config conf = new Config();
        System.out.println(conf.getPort());
    }
}
