package models;

public enum JsonValues {
    IP_BLACKLIST("ipBlacklist"),
    MAC_BLACKLIST("macBlacklist"),
    IP_WHITELIST("ipWhitelist"),
    MAC_WHITELIST("macWhitelist"),
    HIDE_IP("hideIp"),
    DARK_MODE("darkMode"),
    USERNAME("username"),
    PORT("port"),
    FONT_SIZE("fontSize");
    
    public final String value;
    
    JsonValues(String value) {
        this.value = value;
    }
}
