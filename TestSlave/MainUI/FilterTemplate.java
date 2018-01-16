/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainUI;

import JPWord.Data.Constant;
import JPWord.Data.Database;
import JPWord.Data.Filter.FilterByTextTag;
import JPWord.Data.Filter.FilterByType;
import JPWord.Data.Filter.IItemFilter;
import JPWord.Data.Filter.SoftByNumberTag;
import JPWord.Data.IMeaning;
import JPWord.Data.IWord;
import JPWord.Data.IWordDictionary;
import java.util.List;
import java.util.LinkedList;

public class FilterTemplate {

    public static final String FILTER_BY_SKILL = "Sort by skill";
    public static final String FILTER_BY_RD = "Sort by review date";
    public static final String FILTER_BY_TYPE = "Sort by type";
    public static final String FILTER_BY_CLASS = "Sort by class";
    public static final String FILTER_BY_HARD = "Sort by hard";
    public static final String FILTER_BY_MY = "Sort by my word";

    class Params extends LinkedList<String> {

        public boolean mSingleSelect = false;

        public Params(boolean singleSelect) {
            mSingleSelect = singleSelect;
        }
    }

    public class TemplateEntity {

        public String mName;
        public String mShortname;
        public String mSpecial;
        public boolean mIsSingleParam = false;
        public Params mParams = null;
        private Class mClass = null;

        public TemplateEntity(String mName, String mShortname, Class instClass, String mSpecial, Params mParams) {
            this.mName = mName;
            this.mShortname = mShortname;
            this.mSpecial = mSpecial;
            this.mParams = mParams;
            this.mClass = instClass;
        }

        public IItemFilter createFilterInstance(List<String> params) {
            if (mClass == null) {
                return null;
            }
            try {
                IItemFilter filter = (IItemFilter) mClass.newInstance();

                return filter.createSelf(mSpecial, params);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            return null;
        }
    }

    private static final FilterTemplate ourInstance = new FilterTemplate();

    public static FilterTemplate getInstance() {
        return ourInstance;
    }

    private List<TemplateEntity> mEntities = new LinkedList<>();

    public List<TemplateEntity> get() {
        return mEntities;
    }

    private FilterTemplate() {
        Params order = new Params(true);
        order.add("ASCE");
        order.add("DESC");
        Params types = new Params(false);
        Params cls = new Params(false);
        
        IWordDictionary dict = null;
        for (IWord word : dict.getWords()) {
            Constant.getInstance().addClass(word.getTagValue("Cls"));
            for (IMeaning meaning : word.getMeanings()) {
                Constant.getInstance().addType(meaning.getType());
            }
        }
        types.addAll(Constant.getInstance().getTypes());
        cls.addAll(Constant.getInstance().getClasses());
        
        mEntities.add(new TemplateEntity(FILTER_BY_SKILL, "SKILL", SoftByNumberTag.class, "SKILL", order));
        mEntities.add(new TemplateEntity(FILTER_BY_RD, "RD", SoftByNumberTag.class, "RD", order));
        mEntities.add(new TemplateEntity(FILTER_BY_TYPE, "TYPE", FilterByType.class, "", types));
        mEntities.add(new TemplateEntity(FILTER_BY_CLASS, "CLS", FilterByTextTag.class, "CLS", cls));
        mEntities.add(new TemplateEntity(FILTER_BY_HARD, "HARD", FilterByTextTag.class, "HARD", null));
        mEntities.add(new TemplateEntity(FILTER_BY_MY, "MY", FilterByTextTag.class, "MY", null));

    }

}
