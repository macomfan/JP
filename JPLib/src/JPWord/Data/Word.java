/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import JPWord.DBStruct.DB_Word;
import java.util.LinkedList;
import java.util.UUID;
import java.util.List;
import java.util.Calendar;
import java.util.Map;
import SqliteEngine_Interface.DB.OP_Update;
import SqliteEngine_Interface.*;
import SqliteEngine_Interface.DB.Bytes_SQLResult;

/**
 *
 * @author u0151316
 */
class Word implements IWord, IWordCodec {

    public static DB_Word DBS = new DB_Word();

    enum Type {
        DB,
        OTF,
    }

    private String kana_ = "";
    private String content_ = "";
    private String tone_ = "";
    private String note_ = "";
    private List<IMeaning> means_ = new LinkedList<>();
    private UUID id_ = null;
    private IRoma roma_ = null;
    private int cls_ = 0;
    private int skill_ = 0;
    private String reviewDate_ = "";
    private Tagable tags_ = new Tagable();
    public Type type_ = Type.OTF;

    private final String MEAN_SEP = "|";

    public WordDictionary parent_ = null;

    public Word(UUID id) {
        id_ = id;
    }

    public Word() {
    }

    private void updateSingle(OP_Update update) {
        if (type_ == Type.DB) {
            DBS.update(DBS.GUID.where(id_.toString()), update);
        }
    }

    @Override
    public boolean isEmpty() {
        if (content_.isEmpty() && kana_.isEmpty() && tone_.isEmpty() && getTags().isEmpty()) {
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
    public void setContent(String value) {
        if (!content_.equals(value)) {
            content_ = value;
            updateSingle(DBS.CONTENT.update(value));
        }
    }

    @Override
    public String getContent() {
        return content_;
    }

    @Override
    public void setKana(String value) {
        if (!kana_.equals(value)) {
            kana_ = value;
            roma_ = Yin50.getInstance().kanaToRoma(kana_);
            updateSingle(DBS.KANA.update(value));
        }
    }

    @Override
    public String getKana() {
        return kana_;
    }

    @Override
    public void setTone(String value) {
        if (!tone_.equals(value)) {
            tone_ = value;
            updateSingle(DBS.TONE.update(value));
        }
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
        return skill_;
    }

    @Override
    public String getNote() {
        return note_;
    }

    @Override
    public void setNote(String value) {
        if (!note_.equals(value)) {
            note_ = value;
            updateSingle(DBS.NOTE.update(value));
        }
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
        if (skill_ != skillValue) {
            skill_ = skillValue;
            updateSingle(DBS.SKILL.update(skill_));
            updateReviewDate();
        }
    }

    @Override
    public void setAsMyWord(boolean flag) {
        if (flag) {
            setTag(Tag_MYWORD, "1");
        } else {
            removeTag(Tag_MYWORD);
        }
    }

    @Override
    public void addMeaning(IMeaning meaning) {
        ((Meaning) meaning).parent_ = this;
        if (!meaning.isEmpty()) {
        }
        means_.add((IMeaning) meaning);
        if (type_ == Type.DB) {
            updateSingle(DBS.MEANS.update(encodeMeans()));
        }
    }

    private String encodeMeans() {
        String meansString = "";
        for (IMeaning iMeaning : means_) {
            meansString += iMeaning.encodeToString();
            meansString += MEAN_SEP;
        }
        return meansString;
    }

    @Override
    public List<IMeaning> getMeanings() {
        return means_;
    }

    @Override
    public String getReviewDate() {
        return reviewDate_;
    }

    @Override
    public void setReviewDate(String rd) {
        reviewDate_ = rd;
    }

    private void updateReviewDate() {
        Calendar now = Calendar.getInstance();
        String newReviewDate = String.format("%04d%02d%02d", now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH));
        if (!newReviewDate.equals(reviewDate_)) {
            reviewDate_ = newReviewDate;
            updateSingle(DBS.REVIEWDATE.update(reviewDate_));
        }
    }

    @Override
    public void updateMeaning(List<IMeaning> meanings) {
        means_.clear();
        means_.addAll(meanings);
        if (type_ == Type.DB) {
            updateSingle(DBS.MEANS.update(encodeMeans()));
        }
    }

    @Override
    public int getCls() {
        return cls_;
    }

    @Override
    public void setCls(int cls) {
        cls_ = cls;
    }

    @Override
    public Map<String, String> getTags() {
        return tags_.getTags();
    }

    @Override
    public String getTag(String name) {
        return tags_.getTag(name);
    }

    @Override
    public void setTag(String name, String value) {
        String tagString = tags_.getTag(name);
        if (!tagString.equals(value)) {
            tags_.setTag(name, value);
            updateSingle(DBS.TAGS.update(tags_.encodeToString()));
        }
    }

    @Override
    public void removeTag(String name) {
        if (tags_.isExist(name)) {
            tags_.removeTag(name);
            updateSingle(DBS.TAGS.update(tags_.encodeToString()));
        }
    }
    static int ind = 0;

    public void decodeFromSQL(ISQLResult rs) throws Exception {
        try {
            type_ = Type.DB;
            String guid = DBS.GUID.getValueFromRS(rs);
            id_ = UUID.fromString(guid);
            content_ = DBS.CONTENT.getValueFromRS(rs);
            kana_ = DBS.KANA.getValueFromRS(rs);
            tone_ = DBS.TONE.getValueFromRS(rs);
            note_ = DBS.NOTE.getValueFromRS(rs);
            String means = DBS.MEANS.getValueFromRS(rs);
            if (means != null && !"".equals(means)) {
                String meanItems[] = means.split("\\" + MEAN_SEP);
                for (String meanItem : meanItems) {
                    Meaning mean = new Meaning(this);
                    mean.decodeFromString(meanItem);
                    means_.add(mean);
                }
            }
            cls_ = DBS.CLASS.getValueFromRS(rs);
            skill_ = DBS.SKILL.getValueFromRS(rs);
            reviewDate_ = DBS.REVIEWDATE.getValueFromRS(rs);
            String tags = DBS.TAGS.getValueFromRS(rs);
            tags_.decodeFromString(tags);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void encodeToSQL() {
        String tagString = tags_.encodeToString();
        DBS.insert(
                DBS.GUID.update(id_.toString()),
                DBS.CONTENT.update(content_),
                DBS.KANA.update(kana_),
                DBS.TONE.update(tone_),
                DBS.NOTE.update(note_),
                DBS.MEANS.update(encodeMeans()),
                DBS.CLASS.update(cls_),
                DBS.SKILL.update(skill_),
                DBS.REVIEWDATE.update(reviewDate_),
                DBS.TAGS.update(tagString));
        type_ = Type.DB;
    }

    @Override
    public byte[] encodeToBytes() {
        String tagString = tags_.encodeToString();
        return DBS.encodeToBytes(
                DBS.GUID.updateRaw(id_.toString()),
                DBS.CONTENT.updateRaw(content_),
                DBS.KANA.updateRaw(kana_),
                DBS.TONE.updateRaw(tone_),
                DBS.NOTE.updateRaw(note_),
                DBS.MEANS.updateRaw(encodeMeans()),
                DBS.CLASS.updateRaw(cls_),
                DBS.SKILL.updateRaw(skill_),
                DBS.REVIEWDATE.updateRaw(reviewDate_),
                DBS.TAGS.updateRaw(tagString));
    }

    @Override
    public void decodeFromBytes(byte[] bytes) throws Exception {
        Bytes_SQLResult rs = new Bytes_SQLResult(bytes);
        decodeFromSQL(rs);
    }
}
