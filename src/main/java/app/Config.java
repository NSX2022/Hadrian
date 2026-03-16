package app;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.Logging;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Class that loads and stores specific JSON values from config.json resource
 */
public class Config {
    private final String IP_BLACKLIST = "ipBlacklist", MAC_BLACKLIST = "macBlacklist",
            IP_WHITELIST = "ipWhitelist", MAC_WHITELIST = "macWhitelist",
            HIDE_IP = "hideIp", USERNAME = "username", PORT = "port";
    private final JSONObject jsonObject;
    
    private int port;
    private ArrayList<String> ipBlacklist, macBlacklist, ipWhitelist, macWhitelist;
    private boolean hideIp;
    private String username;
    
    public Config() throws IOException {
        InputStream input = ClassLoader.getSystemResourceAsStream("config.json");
        if (input == null) {
            String message = "config.json Missing From Resources";
            FileNotFoundException exception = new FileNotFoundException(message);
            Logging.log(message, Level.SEVERE, exception);
            throw exception;
        }
        
        try (input) {
            String json = new Scanner(input).useDelimiter("\\Z").next();
            jsonObject = new JSONObject(json);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        
        readConfig();
    }
    
    /**
     * Reads config.json, setting the values contained to java variables to be accessed using getters/setters.
     *
     * @throws FileNotFoundException if config.json is not found
     * @see JSONObject
     * @see JSONObject#getJSONArray(String)
     */
    public void readConfig() throws UnknownHostException {
        ipBlacklist = JSONArrayToStringList(jsonObject.getJSONArray(IP_BLACKLIST));
        macBlacklist = JSONArrayToStringList(jsonObject.getJSONArray(MAC_BLACKLIST));
        ipWhitelist = JSONArrayToStringList(jsonObject.getJSONArray(IP_WHITELIST));
        macWhitelist = JSONArrayToStringList(jsonObject.getJSONArray(MAC_WHITELIST));
        
        port = jsonObject.getInt(PORT);
        hideIp = jsonObject.getBoolean(HIDE_IP);
        username = jsonObject.getString(USERNAME);
        
        if (username.isBlank())
            username = InetAddress.getLocalHost().getHostName();
    }
    
    private ArrayList<String> JSONArrayToStringList(JSONArray jsonArray) {
        ArrayList<String> list = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++)
            list.add(jsonArray.getString(i));
        return list;
    }
    
    // region Getters/Setters
    public int getPort() {
        return port;
    }
    
    public void setPort(int port) {
        this.port = port;
        jsonObject.put(PORT, port);
    }
    
    public ArrayList<String> getIpBlacklist() {
        return ipBlacklist;
    }
    
    public void addIpBlackList(String ipAddress) {
        ipBlacklist.add(ipAddress);
        jsonObject.put(IP_BLACKLIST, ipAddress);
    }
    
    public ArrayList<String> getMacBlacklist() {
        return macBlacklist;
    }
    
    public String[] getIpBlacklist() {
        return ipBlacklist;
    }
    
    public ArrayList<String> getIpWhitelist() {
        return ipWhitelist;
    }
    
    public void addIpWhiteList(String ipAddress) {
        ipWhitelist.add(ipAddress);
        jsonObject.put(IP_WHITELIST, ipAddress);
    }
    
    
    public ArrayList<String> getMacWhitelist() {
        return macWhitelist;
    }
    
    public boolean getHideIp() {
        return hideIp;
    }
    
    public void setHideIp(boolean hideIp) {
        this.hideIp = hideIp;
    }
    // endregion
}
