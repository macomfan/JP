package DataEngine;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import JPWord.Data.Constant;
import JPWord.Data.Filter.FilterByTextTag;
import JPWord.Data.Filter.FilterByType;
import JPWord.Data.Filter.IItemFilter;
import JPWord.Data.Filter.SoftByNumberTag;
import JPWord.Data.IMeaning;
import JPWord.Data.ITag;
import JPWord.Data.IWord;
import JPWord.Data.IWordDictionary;

/**
 * Created by u0151316 on 1/11/2018.
 */

public class FilterTemplate {

    public static final String FILTER_BY_SKILL = "Sort by skill";
    public static final String FILTER_BY_RD = "Sort by review date";
    public static final String FILTER_BY_TYPE = "Sort by type";
    public static final String FILTER_BY_CLASS = "Sort by class";
    public static final String FILTER_BY_HARD = "Sort by hard";
    public static final String FILTER_BY_MY = "Sort by my word";

    public class Params extends LinkedList<String> {

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
        public String mDefParam;

        public TemplateEntity(String mName, String mShortname, Class instClass, String mSpecial, Params mParams, String defParam) {
            this.mName = mName;
            this.mShortname = mShortname;
            this.mSpecial = mSpecial;
            this.mParams = mParams;
            this.mClass = instClass;
            if (defParam == null) {
                this.mDefParam = "";
            }
            else {
                this.mDefParam = defParam;
            }

        }

        public IItemFilter createFilterInstance(String params) {
            List<String> p = null;
            if (params.equals("")) {
                p = Arrays.asList(mDefParam.split("\\,"));
            } else {
                p = Arrays.asList(params.split("\\,"));
            }
            return createFilterInstance(p);
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

    public TemplateEntity getEntitieByFilterStruct(Filter.FilterStruct f) {
        for (TemplateEntity entity : getEntities()) {
            if (entity.mName.equals(f.name_)) {
                return entity;
            }
        }
        return null;
    }

    public TemplateEntity getEntitiesByName(String name) {
        for (TemplateEntity entity : getEntities()) {
            if (entity.mName.equals(name)) {
                return entity;
            }
        }
        return null;
    }

    public List<TemplateEntity> getEntities() {
        if (mEntities.size() == 0) {
            Params order = new Params(true);
            order.add("ASCE");
            order.add("DESC");
            Params types = new Params(false);
            Params cls = new Params(false);

            IWordDictionary dict = DB.getInstance().getDatabase();
            for (IWord word : dict.getWords()) {
                Constant.getInstance().addClass(word.getTagValue("Cls"));
                for (IMeaning meaning : word.getMeanings()) {
                    Constant.getInstance().addType(meaning.getType());
                }
            }
            types.addAll(Constant.getInstance().getTypes());
            cls.addAll(Constant.getInstance().getClasses());
            mEntities.add(new TemplateEntity(FILTER_BY_SKILL, "SKILL", SoftByNumberTag.class, ITag.TAG_Skill, order, order.get(0)));
            mEntities.add(new TemplateEntity(FILTER_BY_RD, "RD", SoftByNumberTag.class, ITag.TAG_RD, order, order.get(0)));
            mEntities.add(new TemplateEntity(FILTER_BY_TYPE, "TYPE", FilterByType.class, "", types, null));
            mEntities.add(new TemplateEntity(FILTER_BY_CLASS, "CLS", FilterByTextTag.class, ITag.TAG_Cls, cls, null));
            mEntities.add(new TemplateEntity(FILTER_BY_HARD, "HARD", FilterByTextTag.class, ITag.TAG_HD, null, null));
            mEntities.add(new TemplateEntity(FILTER_BY_MY, "MY", FilterByTextTag.class, ITag.TAG_MY, null, null));
        }
        return mEntities;
    }

    private FilterTemplate() {
    }

}
