/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainUI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author u0151316
 */
public class Setting {

    class SettingFile {

        private Map<String, String> values_ = new HashMap<>();

        public SettingFile() {
            try {
                File file = new File("setting.ini");
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file), "utf-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    int s = line.indexOf('=');
                    String key = line.substring(0, s);
                    key = key.trim();
                    String value = line.substring(s, line.length());
                    value = value.trim();
                    values_.put(key, value);
                }
                reader.close();
            } catch (Exception e) {
            }

        }

        public void save() {
            try {
                File file = new File("setting.ini");
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
                for (Map.Entry<String, String> item : values_.entrySet()) {
                    String line = item.getKey() + "=" + item.getValue();
                    if (line != null && !line.equals("")) {
                        writer.write(line);
                        writer.write("\r\n");
                    }
                }
                writer.flush();
                writer.close();
            } catch (Exception e) {
            }

        }

        public void setValue(String key, String value) {
            values_.put(key, value);
        }

        public String getValue(String key) {
            return values_.get(key);
        }

        public boolean hasValue(String key) {
            return values_.containsKey(key);
        }
    }

    private String rootFolder_ = "";
    private String filename_ = "";
    public List<FilterStruct> filters_ = new LinkedList<>();
    private SettingFile setting_ = new SettingFile();
    private static Setting instance_;

    private Setting() {
        if (!setting_.hasValue("RootFolder"))
        {
            filename_ = "C:\\Users\\u0151316\\Documents\\JP\\";
        }
        else {
            filename_ = setting_.getValue("RootFolder");
        }
        
    }

    public String getRootFolder() {
        return filename_;
    }

    public static Setting getInstance() {
        if (instance_ == null) {
            instance_ = new Setting();
        }
        return instance_;
    }

    public void save() {
        setting_.save();
    }
}
