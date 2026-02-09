package config;

import org.json.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Loads and stores specific JSON values from config.json
 */
public class Config {
    /*
       Load config.json values for the server + TUI
    */

    //Default port number
    private int port = 9090;
    private String[] ip_blacklist;
    private String[] mac_blacklist;
    private String[] ip_whitelist;
    private String[] mac_whitelist;
    //Whether or not to display the IP address in the TUI
    private boolean hide_ip = true;
    private int max_bytes_per_message = 1024;
    private String username = "Anon";

    public Config(){
        //TODO Create default values if the file does not exist
    }

    public void read_config() throws FileNotFoundException {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.json");
        if (inputStream == null) {
            throw new FileNotFoundException("config.json not found in resources");
        }
        String json = new Scanner(inputStream).useDelimiter("\\Z").next();        JSONObject json_object = new JSONObject(json);
        port = json_object.getInt("port");

        ip_blacklist = json_object.getJSONArray("ip_blacklist")
                .toList()
                .stream()
                .map(Object::toString)
                .toArray(String[]::new);

        mac_blacklist = json_object.getJSONArray("mac_blacklist")
                .toList()
                .stream()
                .map(Object::toString)
                .toArray(String[]::new);

        ip_whitelist = json_object.getJSONArray("ip_whitelist")
                .toList()
                .stream()
                .map(Object::toString)
                .toArray(String[]::new);

        mac_whitelist = json_object.getJSONArray("mac_whitelist")
                .toList()
                .stream()
                .map(Object::toString)
                .toArray(String[]::new);

        hide_ip = json_object.getBoolean("hide_ip");

        max_bytes_per_message = json_object.getInt("max_bytes_per_message");

        username = json_object.getString("username");
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String[] getMac_blacklist() {
        return mac_blacklist;
    }

    public String[] getIp_blacklist() {
        return ip_blacklist;
    }

    public String[] getIp_whitelist() {
        return ip_whitelist;
    }

    public String[] getMac_whitelist() {
        return mac_whitelist;
    }

    public boolean getHide_ip() {
        return hide_ip;
    }

    public void setHide_ip(boolean hide_ip) {
        this.hide_ip = hide_ip;
    }

    public int getMax_bytes_per_message() {
        return max_bytes_per_message;
    }

    public void setMax_bytes_per_message(int max_bytes_per_message) {
        this.max_bytes_per_message = max_bytes_per_message;
    }
}
