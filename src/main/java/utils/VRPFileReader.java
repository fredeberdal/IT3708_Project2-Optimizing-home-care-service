package utils;

import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class VRPFileReader { // Lese fra interface #n filer


    JSONArray a = (JSONArray) parser.parse(new FileReader("\\Instances\train_0.json"));
    public VRPFileReader() throws FileNotFoundException {
    }
}
