package app;

import org.json.JSONArray;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testingClasses.ReflectionUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

class ConfigTest {
    private Config config;
    
    @BeforeEach
    void setUp() throws IOException {
        config = new Config();
    }
    
    @AfterEach
    void tearDown() {
    }
    
    @Test
    void readConfig() throws UnknownHostException {
        assert ClassLoader.getSystemResourceAsStream("config.json") != null;
        
        assert config.getPort() == 9090
                && config.getHideIp()
                && config.getUsername().equals(InetAddress.getLocalHost().getHostName())
                && config.getIpBlacklist().isEmpty()
                && config.getIpWhitelist().isEmpty()
                && config.getMacBlacklist().isEmpty()
                && config.getMacWhitelist().isEmpty();
    }
    
    @Test
    void configError() {
    }
    
    @Test
    void JSONArrayToStringList() throws NoSuchMethodException {
        JSONArray array = new JSONArray();
        array.put("Test 1");
        array.put("Test 2");
        array.put("Test 3");
        
        ArrayList<String> returnValue = (ArrayList<String>) ReflectionUtils.invokePrivateInstance(
                config,
                "JSONArrayToStringList",
                new Class[] { JSONArray.class },
                array
        );
        
        assert returnValue.size() == array.length();
        
        for (int i = 0; i < returnValue.size(); i++)
            assert returnValue.get(i) == array.get(i);
    }
    
    @Test
    void resetToDefaults() {
    }
    
    @Test
    void setPort() {
    }
    
    @Test
    void addIpBlackList() {
    }
    
    @Test
    void addMacBlackList() {
    }
    
    @Test
    void addIpWhiteList() {
    }
    
    @Test
    void addMacWhiteList() {
    }
    
    @Test
    void setHideIp() {
    }
    
    @Test
    void setUsername() {
    }
}
