package app;

import models.JsonValues;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Logging;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Class that loads and stores JSON values into config.json configuration file
 */
public class Config {
    private final JSONObject configJson, defaultsJson;
    private final Path configPath;
    
    private int port, fontSize;
    private ArrayList<String> ipBlacklist, macBlacklist, ipWhitelist, macWhitelist;
    private boolean hideIp, darkMode;
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
     * @throws UnknownHostException if localhost address cannot be fetched
     * @see JSONObject
     * @see JSONObject#getJSONArray(String)
     */
    public void readConfig() throws UnknownHostException {
        ipBlacklist = JSONArrayToStringList(configJson.getJSONArray(JsonValues.IP_BLACKLIST.value));
        macBlacklist = JSONArrayToStringList(configJson.getJSONArray(JsonValues.MAC_BLACKLIST.value));
        ipWhitelist = JSONArrayToStringList(configJson.getJSONArray(JsonValues.IP_WHITELIST.value));
        macWhitelist = JSONArrayToStringList(configJson.getJSONArray(JsonValues.MAC_WHITELIST.value));
        
        port = configJson.getInt(JsonValues.PORT.value);
        fontSize = configJson.getInt(JsonValues.FONT_SIZE.value);
        hideIp = configJson.getBoolean(JsonValues.HIDE_IP.value);
        darkMode = configJson.getBoolean(JsonValues.DARK_MODE.value);
        username = configJson.getString(JsonValues.USERNAME.value);
        
        if (username.isBlank()) {
            username = InetAddress.getLocalHost().getHostName();
            saveData();
        }
    }
    
    /**
     * Converts a JSONArray object into a String ArrayList.
     *
     * @param jsonArray object read directly from a JSON file
     * @return a String ArrayList containing all the same values as the JSONArray
     * @see JSONArray
     */
    private ArrayList<String> JSONArrayToStringList(JSONArray jsonArray) {
        ArrayList<String> list = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++)
            list.add(jsonArray.getString(i));
        return list;
    }
    
    public void resetToDefaults() {
        // TODO: reset all values to default
    }
    
    // region Getters/Setters
    public int getPort() {
        return port;
    }
    
    public void setPort(int port) {
        this.port = port;
        configJson.put(JsonValues.PORT.value, port);
    }
    
    public int getFontSize() {
        return fontSize;
    }
    
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        configJson.put(JsonValues.FONT_SIZE.value, fontSize);
    }
    
    public boolean getHideIp() {
        return hideIp;
    }
    
    public void setHideIp(boolean hideIp) {
        this.hideIp = hideIp;
        configJson.put(JsonValues.HIDE_IP.value, hideIp);
    }
    
    public boolean isDarkMode() {
        return darkMode;
    }
    
    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
        configJson.put(JsonValues.DARK_MODE.value, darkMode);
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
        configJson.put(JsonValues.USERNAME.value, username);
    }
    
    public ArrayList<String> getIpBlacklist() {
        return ipBlacklist;
    }
    
    public void addIpBlackList(String ipAddress) {
        ipBlacklist.add(ipAddress);
        configJson.append(JsonValues.IP_BLACKLIST.value, ipAddress);
    }
    
    public void setIpBlacklist(ArrayList<String> ipBlacklist) {
        this.ipBlacklist = ipBlacklist;
        configJson.put(JsonValues.IP_BLACKLIST.value, ipBlacklist);
    }
    
    public ArrayList<String> getMacBlacklist() {
        return macBlacklist;
    }
    
    public void addMacBlackList(String macAddress) {
        macBlacklist.add(macAddress);
        configJson.append(JsonValues.MAC_BLACKLIST.value, macAddress);
    }
    
    public void setMacBlacklist(ArrayList<String> macBlacklist) {
        this.macBlacklist = macBlacklist;
        configJson.put(JsonValues.MAC_BLACKLIST.value, macBlacklist);
    }
    
    public ArrayList<String> getIpWhitelist() {
        return ipWhitelist;
    }
    
    public void addIpWhiteList(String ipAddress) {
        ipWhitelist.add(ipAddress);
        configJson.append(JsonValues.IP_WHITELIST.value, ipAddress);
    }
    
    public void setIpWhitelist(ArrayList<String> ipWhitelist) {
        this.ipWhitelist = ipWhitelist;
        configJson.put(JsonValues.IP_WHITELIST.value, ipWhitelist);
    }
    
    public ArrayList<String> getMacWhitelist() {
        return macWhitelist;
    }
    
    public void addMacWhiteList(String macAddress) {
        macWhitelist.add(macAddress);
        configJson.append(JsonValues.MAC_WHITELIST.value, macAddress);
    }
    
    public void setMacWhitelist(ArrayList<String> macWhitelist) {
        this.macWhitelist = macWhitelist;
        configJson.put(JsonValues.MAC_WHITELIST.value, macWhitelist);
    }
    // endregion
}
