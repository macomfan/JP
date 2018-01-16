/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import java.util.LinkedList;
import java.util.UUID;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Calendar;

/**
 *
 * @author u0151316
 */
class Word extends Tagable implements IWord {

    private static final String ITEM_SEP = "|";
    private static final String SOH = String.valueOf((char) 0x01);

    private String kana_ = "";
    private String content_ = "";
    private String tone_ = "";
    private List<IMeaning> means_ = new LinkedList<>();
    private UUID id_ = null;
    private IRoma roma_ = null;
    private boolean changeFlag_ = false;
    private long timestamp_ = 0;

    public Word(UUID id) {
        id_ = id;
    }

    public Word() {
    }

    @Override
    public boolean isEmpty() {
        if (content_.isEmpty() && kana_.isEmpty() && tone_.isEmpty() && getTags().size() == 0) {
            for (IMeaning iMeaning : means_) {
                if (!iMeaning.isEmpty()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public long getTimeStamp() {
        return timestamp_;
    }

    public void updatedFlag() {
        changeFlag_ = true;
        timestamp_ = System.currentTimeMillis();
    }

    public boolean isUpdated() {
        return changeFlag_;
    }

    @Override
    public void setContent(String value) {
        content_ = value;
        updatedFlag();
    }

    @Override
    public String getContent() {
        return content_;
    }

    @Override
    public void setKana(String value) {
        kana_ = value;
        roma_ = Yin50.getInstance().kanaToRoma(kana_);
        updatedFlag();
    }

    @Override
    public String getKana() {
        return kana_;
    }

    @Override
    public void setTone(String value) {
        tone_ = value;
        updatedFlag();
    }

    @Override
    public ITag setTag(String Name, String Value) {
        updatedFlag();
        return super.setTag(Name, Value);
    }

    @Override
    public String getTone() {
        return tone_;
    }

    @Override
    public String getID() {
        return id_.toString();
    }

    @Override
    public IRoma getRoma() {
        if (roma_ == null) {
            roma_ = Yin50.getInstance().kanaToRoma(kana_);
        }
        return roma_;
    }

    @Override
    public int getSkill() {
        ITag skill = getTagItem(ITag.TAG_Skill);
        if (skill == null) {
            return 0;
        }
        return Integer.parseInt(skill.getValue(), 10);
    }

    @Override
    public String getNote() {
        ITag note = getTagItem("Note");
        if (note == null) {
            return "";
        }
        return note.getValue().replaceAll("<&N>", "\n");
    }

    @Override
    public void setNote(String value) {
        ITag note = getAndCreateTag("Note");
        value = value.replaceAll("\\r", "");
        value = value.replaceAll("\\n", "<&N>");
        note.setValue(value);
        updatedFlag();
    }

    @Override
    public void increaseSkill() {
        updateSkill(getSkill() + 1);
    }

    @Override
    public void decreaseSkill() {
        updateSkill(getSkill() - 1);
    }

    @Override
    public void updateSkill(int skillValue) {
        ITag skill = getTagItem(ITag.TAG_Skill);
        if (skill == null) {
            skill = setTag(ITag.TAG_Skill, "0");
        }
        int value = Integer.parseInt(skill.getValue(), 10);
        skill.setValue(Integer.toString(skillValue));
        if (value < 0) {
            setHardFlag(true);
        } else if (value > 5) {
            setHardFlag(false);
        }
        updateReviewTime();
        updatedFlag();
    }

    @Override
    public void setAsMyWord(boolean flag) {
        ITag my = getTagItem(ITag.TAG_MY);
        if (my == null) {
            if (flag != false) {
                my = setTag(ITag.TAG_MY, "1");
            }
        } else {
            if (flag == false) {
                removeTag(ITag.TAG_MY);
            }
        }
    }

    @Override
    public void addMeaning(IMeaning meaning) {
        ((Meaning) meaning).parent_ = this;
        if (!meaning.isEmpty()) {
            updatedFlag();
        }
        means_.add((IMeaning) meaning);
    }

    @Override
    public List<IMeaning> getMeanings() {
        List<IMeaning> means = new LinkedList<>();
        for (IMeaning m : means_) {
            means.add(m);
        }
        return means;
    }

    private ITag getAndCreateTag(String Name) {
        ITag tag = getTagItem(Name);
        if (tag == null) {
            tag = setTag(Name, "");
        }
        return tag;
    }

    private void updateReviewTime() {
        Calendar now = Calendar.getInstance();
        ITag rt = getTagItem("RD");
        if (rt == null) {
            rt = setTag("RD", "0");
        }
        rt.setValue(String.format("%04d%02d%02d", now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH)));
    }

    private void setHardFlag(boolean flag) {
        ITag hard = getTagItem("HD");
        if (hard == null) {
            hard = setTag("HD", "N");
        }
        if (flag == true) {
            hard.setValue("Y");
        } else {
            hard.setValue("N");
        }
        updatedFlag();
    }

    @Override
    public String encodeToString() {
        if (content_.equals("")) {
            return null;
        }
        String line = "\"" + getID();
        line += "\"" + "|";
        line += content_ + "|";
        line += kana_ + "|";
        line += tone_;
        boolean meanExist = false;
        for (IMeaning mean : means_) {
            meanExist = true;
            line += "|";
            line += mean.encodeToString();
        }
        if (!meanExist) {
            line += "|";
        }
        line += "|";
        String tagString = "";
        for (ITag tag : getTags()) {
            tagString += tag.getName();
            tagString += "=";
            tagString += tag.getValue();
            tagString += SOH;
        }
        if (tagString.length() != 0) {
            tagString = SOH + tagString;
            tagString += "|";
            line += tagString;
        }
        line += "#";
        line += Long.toString(timestamp_, 10);
        return line;
    }

    @Override
    public boolean decodeFromString(String str) {
        String items[] = str.split("\\" + ITEM_SEP);
        if (items.length < 4) {
            System.out.println(String.format("Pares error at: %s", str));
            return false;
        }
        if (items[0].length() != 38) {
            System.out.println(String.format("Not start with GUID: %s", str));
            return false;
        }
        id_ = UUID.fromString(items[0].substring(1, 37));
        content_ = items[1];
        kana_ = items[2];
        tone_ = items[3];
//        if (!tone_.equals("")) {
//            Constant.getInstance().addTone(tone_);
//        }
        for (int i = 4; i < items.length; i++) {
            if (items[i].length() != 0) {
                if (items[i].charAt(0) == '#') {
                    timestamp_ = Long.parseLong(items[i].substring(1, items[i].length()), 10);
                } else if (items[i].charAt(0) != SOH.charAt(0)) {
                    Meaning mean = new Meaning();
                    if (mean.decodeFromString(items[i])) {
                        mean.parent_ = this;
                        means_.add(mean);
                    }
                } else {
                    parseTag(items[i]);
                }
            }

        }
        changeFlag_ = false;
        return true;
    }

    private void parseTag(String item) {
        if (item == null || item.equals("")) {
            return;
        }
        String[] tagstrings = item.substring(1).split("\\" + SOH);
        for (String tagString : tagstrings) {
            int eqIndex = tagString.indexOf('=');
            if (eqIndex == -1) {
                continue;
            }
            String name = tagString.substring(0, eqIndex);
            String value = tagString.substring(eqIndex + 1);
            super.setTag(name, value);
        }
    }

    @Override
    public void updateMeaning(List<IMeaning> meanings) {
        means_.clear();
        means_.addAll(meanings);
        updatedFlag();
    }

}
