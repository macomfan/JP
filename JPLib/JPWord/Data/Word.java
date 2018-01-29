/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import java.util.LinkedList;
import java.util.UUID;
import java.util.List;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

/**
 *
 * @author u0151316
 */
class Word extends Tagable implements IWord {

    public static class Codec_V1 implements ICodec {

        private final String ITEM_SEP = "|";
        private final String SOH = String.valueOf((char) 0x01);

        public String encodeToString(Object obj) {
            Word word = (Word)obj;
            
            if (word.content_.equals("")) {
                return null;
            }
            String line = "\"" + word.getID();
            line += "\"" + "|";
            line += word.content_ + "|";
            line += word.kana_ + "|";
            line += word.tone_;
            boolean meanExist = false;
            for (IMeaning mean : word.means_) {
                meanExist = true;
                line += "|";
                line += mean.encodeToString();
            }
            if (!meanExist) {
                line += "|";
            }
            line += "|";
            String tagString = Persistence.getInstance().getCurrentTagCodec().encodeToString(obj);
            if (tagString.length() != 0) {
                tagString = SOH + tagString;
                tagString += "|";
                line += tagString;
            }
            line += "#";
            line += Long.toString(word.timestamp_, 10);
            return line;
        }

        public boolean decodeFromString(Object obj, String str) {
            Word word = (Word)obj;
            String items[] = str.split("\\" + ITEM_SEP);
            if (items.length < 4) {
                System.out.println(String.format("Pares error at: %s", str));
                return false;
            }
            if (items[0].length() != 38) {
                System.out.println(String.format("Not start with GUID: %s", str));
                return false;
            }
            word.id_ = UUID.fromString(items[0].substring(1, 37));
            word.content_ = items[1];
            word.kana_ = items[2];
            word.tone_ = items[3];
            for (int i = 4; i < items.length; i++) {
                if (items[i].length() != 0) {
                    if (items[i].charAt(0) == '#') {
                        word.timestamp_ = Long.parseLong(items[i].substring(1, items[i].length()), 10);
                    } else if (items[i].charAt(0) != SOH.charAt(0)) {
                        Meaning mean = new Meaning();
                        if (mean.decodeFromString(items[i])) {
                            mean.parent_ = word;
                            word.means_.add(mean);
                        }
                    } else {
                        Persistence.getInstance().getCurrentTagCodec().decodeFromString(obj, items[i]);
                    }
                }
            }
            word.changeFlag_ = false;
            return true;
        }
    }

    private String kana_ = "";
    private String content_ = "";
    private String tone_ = "";
    private List<IMeaning> means_ = new LinkedList<>();
    private UUID id_ = null;
    private IRoma roma_ = null;
    private boolean changeFlag_ = false;
    private long timestamp_ = 0;
    public WordDictionary parent_ = null;

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
        return means_;
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
        return Persistence.getInstance().getCurrentWordCodec().encodeToString(this);
    }

    @Override
    public boolean decodeFromString(String str) {
        return Persistence.getInstance().getCurrentWordCodec().decodeFromString(this, str);
    }

    @Override
    public void updateMeaning(List<IMeaning> meanings) {
        means_.clear();
        means_.addAll(meanings);
        updatedFlag();
    }

}
