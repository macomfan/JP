/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPLibFilters;

import JPWord.Data.Filter.IItemFilter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author u0151316
 */
abstract public class FilterTemplate {

    /**
     *
     */
    public static final String FILTER_BY_SKILL = "Sort by skill";

    /**
     *
     */
    public static final String FILTER_BY_RD = "Sort by review date";

    /**
     *
     */
    public static final String FILTER_BY_TYPE = "Filter by type";

    /**
     *
     */
    public static final String FILTER_BY_CLASS = "Filter by class";

    /**
     *
     */
    public static final String FILTER_BY_MY = "Filter by my word";

    /**
     *
     */
    public String name_ = "";

    /**
     *
     */
    public String shortname_ = "";

    /**
     *
     */
    public CandidateParams candidateParams_ = null;

    /**
     *
     */
    protected FilterTemplate() {
    }

    /**
     *
     * @param params
     * @return
     * @throws Exception
     */
    public List<String> checkAndSplitParams(String params) throws Exception {
        if (params == null || params.equals("")) {
            if (candidateParams_.type_ == ParamType.Mandatory
                    || candidateParams_.type_ == ParamType.SignleSelectAndMandatory) {
                throw new Exception("[JPLib] param is mandatory");
            }
            return new LinkedList<>();
        }
        String paramItems[] = params.split("\\,");
        if (candidateParams_ != null) {
            if (candidateParams_.type_ == ParamType.SignleSelect
                    || candidateParams_.type_ == ParamType.SignleSelectAndMandatory) {
                int size = 0;
                for (String paramItem : paramItems) {
                    if (!paramItem.equals("")) {
                        size++;
                    }
                }
                if (size != 1) {
                    throw new Exception("[JPLib] the multi params is not allowed");
                }
            }
            for (String paramItem : paramItems) {
                if (!paramItem.equals("") && !candidateParams_.contains(paramItem)) {
                    throw new Exception("[JPLib] the param is not allowed");
                }
            }
            return Arrays.asList(paramItems);
        }
        return Arrays.asList(paramItems);
    }

    public enum ParamType {
        Mandatory,
        SignleSelect,
        SignleSelectAndMandatory,
        Unknown
    };

    /**
     *
     */
    public class CandidateParams extends LinkedList<String> {

        /**
         *
         */
        public ParamType type_ = ParamType.Unknown;

        /**
         *
         */
        public String defaultParam_ = null;

        /**
         *
         * @param type
         * @param defaultParam
         */
        public CandidateParams(ParamType type, String defaultParam) {
            type_ = type;
            defaultParam_ = defaultParam;
        }
    }

    /**
     *
     * @param params
     * @return
     * @throws Exception
     */
    abstract public IItemFilter createFilter(String params) throws Exception;
}
