/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author u0151316
 */
class Tagable {

    private final String SOH = String.valueOf((char) 0x01);
    private final Map<String, String> tags_ = new HashMap<>();

    public Tagable() {
    }

    public boolean isExist(String name) {
        return tags_.containsKey(name);
    }
    
    protected String getTag(String Name) {
        if (tags_.containsKey(Name)) {
            return tags_.get(Name);
        }
        return "";
    }

    public Map<String, String> getTags() {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, String> entry : tags_.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public void setTag(String name, String value) {
        if (!tags_.containsKey(name)) {
            tags_.put(name, value);
        } else {
            tags_.put(name, value);
        }
    }

    public void removeTag(String Name) {
        tags_.remove(Name);
    }

    public String encodeToString() {
        String tagString = "";
        for (Map.Entry<String, String> entry : tags_.entrySet()) {
            tagString += entry.getKey();
            tagString += "=";
            tagString += entry.getValue();
            tagString += SOH;
        }
        return tagString;
    }

    public boolean decodeFromString(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        String[] tagstrings = str.split("\\" + SOH);
        for (String tagString : tagstrings) {
            int eqIndex = tagString.indexOf('=');
            if (eqIndex == -1) {
                continue;
            }
            String name = tagString.substring(0, eqIndex);
            String value = tagString.substring(eqIndex + 1);
            this.setTag(name, value);
        }
        return true;
    }
}
