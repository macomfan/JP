/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author u0151316
 */
class Tagable {

    public static class Codec_V1 implements ICodec {

        private final String SOH = String.valueOf((char) 0x01);

        @Override
        public String encodeToString(Object obj) {
            Tagable tagable = (Tagable) obj;
            String tagString = "";
            for (ITag tag : tagable.getTags()) {
                tagString += tag.getName();
                tagString += "=";
                tagString += tag.getValue();
                tagString += SOH;
            }
            return tagString;
        }

        @Override
        public boolean decodeFromString(Object obj, String str) {
            Tagable tagable = (Tagable) obj;
            if (str == null || str.equals("")) {
                return false;
            }
            String[] tagstrings = str.substring(1).split("\\" + SOH);
            for (String tagString : tagstrings) {
                int eqIndex = tagString.indexOf('=');
                if (eqIndex == -1) {
                    continue;
                }
                String name = tagString.substring(0, eqIndex);
                String value = tagString.substring(eqIndex + 1);
                tagable.setTag(name, value);
            }
            return true;
        }

    }

    private SortedMap<String, ITag> tags_ = new TreeMap<>();

    public Tagable() {

    }

    protected ITag getTagItem(String Name) {
        if (tags_.containsKey(Name)) {
            return tags_.get(Name);
        }
        return null;
    }

    public List<ITag> getTags() {
        List<ITag> temp = new LinkedList<>();
        for (Map.Entry<String, ITag> entry : tags_.entrySet()) {
            temp.add(entry.getValue());
        }
        return temp;
    }

    public ITag setTag(String Name, String Value) {
        if (!tags_.containsKey(Name)) {
            Tag tag = new Tag();
            tag.setName(Name);
            tag.setValue(Value);
            tags_.put(Name, (ITag) tag);
            return tag;
        } else {
            ITag tag = tags_.get(Name);
            tag.setValue(Value);
            return tag;
        }
    }

    public String getTagValue(String Name) {
        if (tags_.containsKey(Name)) {
            return tags_.get(Name).getValue();
        }
        return "";
    }

    public void removeTag(String Name) {
        tags_.remove(Name);
    }
}
