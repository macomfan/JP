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

    public ITag addTag(String Name, String Value) {
        Tag tag = new Tag();
        tag.setName(Name);
        tag.setValue(Value);
        tags_.put(Name, (ITag) tag);
        return tag;
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
