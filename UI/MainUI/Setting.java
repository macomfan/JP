/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainUI;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author u0151316
 */
public class Setting {

    class SettingFile {

        public SettingFile() {
            File file = new File("setting.ini");
//            BufferedReader reader = new BufferedReader(
//                    new InputStreamReader(new FileInputStream(file_), "utf-8"));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                IWord word = new Word();
//                if (word.decodeFromString(line)) {
//                    quickKey_.put(word.getID(), (Word) word);
//                    words_.add((Word) word);
//                }
//            }
//            reader.close();
        }
    }

    public String filename_ = "";
    public List<FilterStruct> filters_ = new LinkedList<>();

    private Setting() {

    }

    public void save() {

    }
}
