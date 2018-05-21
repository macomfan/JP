package DataEngine;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by u0151316 on 1/8/2018.
 */

class SettingFile {
    abstract class Tag {
        public String type = "UNKNOWN";

        public abstract void read(BufferedReader reader) throws Exception;

        public abstract void write(BufferedWriter writer) throws Exception;
    }

    class StringTag extends Tag {
        public String value_ = null;

        public void read(BufferedReader reader) throws Exception {
            value_ = reader.readLine();
        }

        public void write(BufferedWriter writer) throws Exception {
            writer.write(value_);
            writer.write("\r\n");
        }
    }

    class ListTag extends Tag {
        public List<String> value_ = new LinkedList<>();

        public void read(BufferedReader reader) throws Exception {
            int number = Integer.parseInt(reader.readLine(), 10);

            for (int i = 0; i < number; i++) {
                value_.add(reader.readLine());
            }
        }

        public void write(BufferedWriter writer) throws Exception {
            writer.write(Integer.toString(value_.size(), 10));
            writer.write("\r\n");
            for (String item : value_) {
                writer.write(item);
                writer.write("\r\n");
            }
        }
    }


    private final static String TYPE_STRING = "STRING";
    private final static String TYPE_LIST = "LIST";
    private final static String FILE_NAME = "setting.ini";
    private final static String CODEC = "utf-8";

    private Map<String, Tag> tags_ = new HashMap<>();

    private Map<String, Tag> strings_ = new HashMap<>();
    private Map<String, ListTag> lists_ = new HashMap<>();

    public SettingFile() {
    }

    public void load(Context context) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.openFileInput(FILE_NAME), CODEC));
            String line;
            while ((line = reader.readLine()) != null) {
                int s = line.indexOf('=');
                if (s == -1) {
                    continue;
                }
                String key = line.substring(0, s);
                String type = line.substring(s + 1, line.length());
                Tag value = null;
                if (type.equals(TYPE_STRING)) {
                    value = new StringTag();
                } else if (type.equals(TYPE_LIST)) {
                    value = new ListTag();
                }
                if (value != null) {
                    value.type = type;
                    value.read(reader);
                    tags_.put(key, value);
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void save(Context context) {
        try {
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE), CODEC));
            for (Map.Entry<String, Tag> entry : tags_.entrySet()) {
                String line = entry.getKey() + "=" + entry.getValue().type;
                writer.write(line + "\r\n");
                entry.getValue().write(writer);
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {

        }
    }

    public List<String> getList(String key) {
        if (tags_.containsKey(key)) {
            Tag value = tags_.get(key);
            if (value.type.equals(TYPE_LIST)) {
                return ((ListTag) value).value_;
            }
        }
        return null;
    }

    public String getString(String key) {
        if (tags_.containsKey(key)) {
            Tag value = tags_.get(key);
            if (value.type.equals(TYPE_STRING)) {
                return ((StringTag) value).value_;
            }
        }
        return "";
    }

    public void setString(String key, String value) {
        StringTag tag = new StringTag();
        tag.type = TYPE_STRING;
        tag.value_ = value;
        tags_.put(key, tag);
    }

    public void setList(String key, List<String> value) {
        ListTag tag = new ListTag();
        tag.type = TYPE_LIST;
        tag.value_ = value;
        tags_.put(key, tag);
    }
}
