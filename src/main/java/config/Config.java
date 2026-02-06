package config;

import org.json.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Config {
    /*
       TODO Load config.json values for the server + TUI
    */
    private int port = 9090;

    public Config(){
        //TODO Create default values if the file does not exist
    }
    public void read_config() throws FileNotFoundException {
        String Json = new Scanner(new File("config.json")).useDelimiter("\\Z").next();
        JSONObject Jsonobject = new JSONObject(Json);

    }

}
