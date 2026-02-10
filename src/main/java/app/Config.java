package app;

import org.json.*;
import utils.Logging;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Utility class that loads and stores specific JSON values from config.json
 */
public class Config {
    /*
       Load config.json values for the server + TUI
    */
    
    //Default port number
    private int port = 9090, maxBytesPerMessage = 1024;
    private String[] ipBlacklist, macBlacklist, ipWhitelist, macWhitelist;
    //Whether or not to display the IP address in the TUI
    private boolean hideIp = true;
    private String username = "Anon";
    
    public Config() {
        //TODO Create default values if the file does not exist
    }
    
    /**
     * Reads config.json, setting the values contained to java variables to be accessed using getters/setters.
     *
     * @throws FileNotFoundException if config.json is not found
     * @see JSONObject
     * @see JSONObject#getJSONArray(String)
     */
    public void readConfig() throws FileNotFoundException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.json");
        if (inputStream == null) {
            String message = "config.json Not Found In Resources Folder";
            Logging.log(message, Level.SEVERE, new FileNotFoundException(message));
            throw new FileNotFoundException(message);
        }
        
        String json = new Scanner(inputStream).useDelimiter("\\Z").next();
        JSONObject jsonObject = new JSONObject(json);
        port = jsonObject.getInt("port");

        //NOTE: Camel case
        ipBlacklist = jsonObject.getJSONArray("ipBlacklist").toList().toArray(new String[0]);
        macBlacklist = jsonObject.getJSONArray("macBlacklist").toList().toArray(new String[0]);
        ipWhitelist = jsonObject.getJSONArray("ipWhitelist").toList().toArray(new String[0]);
        macWhitelist = jsonObject.getJSONArray("macWhitelist").toList().toArray(new String[0]);
        
        hideIp = jsonObject.getBoolean("hideIp");
        maxBytesPerMessage = jsonObject.getInt("maxBytesPerMessage");
        username = jsonObject.getString("username");
    }
    
    public int getPort() {
        return port;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public String[] getMacBlacklist() {
        return macBlacklist;
    }
    
    public String[] getIpBlacklist() {
        return ipBlacklist;
    }
    
    public String[] getIpWhitelist() {
        return ipWhitelist;
    }
    
    public String[] getMacWhitelist() {
        return macWhitelist;
    }
    
    public boolean getHideIp() {
        return hideIp;
    }
    
    public void setHideIp(boolean hideIp) {
        this.hideIp = hideIp;
    }
    
    public int getMaxBytesPerMessage() {
        return maxBytesPerMessage;
    }
    
    public void setMaxBytesPerMessage(int maxBytesPerMessage) {
        this.maxBytesPerMessage = maxBytesPerMessage;
    }
}
