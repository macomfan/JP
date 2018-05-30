package com.jpword.ma.jpword;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;

import com.jpword.ma.baseui.DialogMultiTextSelect;
import com.jpword.ma.baseui.DialogSingleItemSelect;
import com.jpword.ma.baseui.IMyDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import DataEngine.DB;
import DataEngine.Filter;
import DataEngine.FilterEntity;
import JPLibFilters.FilterTemplate;
import JPLibFilters.Filters;


/**
 * Created by u0151316 on 1/3/2018.
 */

public class ActivityFilterSetting extends com.jpword.ma.baseui.ActivityFilterSetting {

    public void onAdd(final int index) {
        final Filter filter = DB.getInstance().getWordSequence().getFilter();
        final String defParam = "";
        List<String> filterNames = new LinkedList<>();
        for (FilterTemplate template : Filters.getInstance().getTemplates()) {
            boolean found = false;
            for (FilterEntity currentEntity : filter.getCurrentFilter()) {
                if (template.shortname_.equals(currentEntity.filterTemplate_.shortname_)) {
                    found = true;
                }
            }
            if (!found) {
                filterNames.add(template.name_);
            }
        }

        final DialogSingleItemSelect dlg = new DialogSingleItemSelect("Choose filter", filterNames, "");
        dlg.show(this, new IMyDialog.CallBack() {
            @Override
            public void confirmed() {
                String ret = dlg.getResult();
                FilterTemplate template = Filters.getInstance().getTemplateByName(ret);
                String defParam = "";
                if (template != null) {
                    defParam = template.candidateParams_.defaultParam_;
                }
                filter.addFilter(index, ret, defParam);
                refreshCurrentFilter();
            }
        });
    }

    public void onDelete(int index) {
        final Filter filter = DB.getInstance().getWordSequence().getFilter();
        filter.removeFilter(index);
        refreshCurrentFilter();
    }

    public void onEditParam(int index) {
        Filter filter = DB.getInstance().getWordSequence().getFilter();
        final FilterEntity entity = filter.getAt(index);
        FilterTemplate template = Filters.getInstance().getTemplateByShortname(entity.filterTemplate_.shortname_);

        if (template == null || template.candidateParams_.size() == 0) {
            return;
        }
        List<String> filterNames = new LinkedList<>();
        filterNames.addAll(template.candidateParams_);
        if (template.candidateParams_.type_ == FilterTemplate.ParamType.SignleSelect
                || template.candidateParams_.type_ == FilterTemplate.ParamType.SignleSelectAndMandatory) {
            final DialogSingleItemSelect dlg = new DialogSingleItemSelect("Edit", filterNames, entity.param_);
            dlg.show(this, new IMyDialog.CallBack() {
                @Override
                public void confirmed() {
                    String ret = dlg.getResult();
                    entity.param_ = ret;
                    refreshCurrentFilter();
                }
            });
        } else {
            final DialogMultiTextSelect dlg = new DialogMultiTextSelect("Edit", filterNames, entity.param_);
            dlg.show(this, new IMyDialog.CallBack() {
                @Override
                public void confirmed() {
                    String ret = dlg.getResult();
                    entity.param_ = ret;
                    refreshCurrentFilter();
                }
            });
        }
    }

    @Override
    protected void onmainListViewClick(AdapterView<?> parent, View view, int position, long id) {
        super.onmainListViewClick(parent, view, position, id);
        Map<String, Object> current = (Map<String, Object>) lvFilterListView_.getItemAtPosition(position);
        FilterEntity entity = (FilterEntity) current.get("filter");
        if (entity == null) {
            onAdd(0);
        } else {
            FilterTemplate template = Filters.getInstance().getTemplateByShortname(entity.filterTemplate_.shortname_);
            if (template == null || template.candidateParams_.size() == 0) {
                return;
            }
            onEditParam(position);
        }
    }

    @Override
    protected void onRBGroupCheckedChange(RadioGroup group, @IdRes int checkedId) {
        Filter filter = DB.getInstance().getWordSequence().getFilter();
        if (checkedId == R.id.rbDispKanji) {
            filter.setDisplayKanJi(true);
        } else if (checkedId == R.id.rbDispKana) {
            filter.setDisplayKanJi(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerForContextMenu(lvFilterListView_);
        refreshCurrentFilter();
    }

    private void refreshCurrentFilter() {
        Filter filter = DB.getInstance().getWordSequence().getFilter();
        boolean displayKanji = filter.isDisplayKanJi();
        if (displayKanji) {
            mRBDisplayKanJi.setChecked(true);
        } else {
            mRBDisplayKana.setChecked(true);
        }

        List<String> listTitle = new LinkedList<>();
        List<String> listParam = new LinkedList<>();
        List<FilterEntity> listFilter = new LinkedList<>();
        if (filter.getCurrentFilter().size() == 0) {
            // no filter
            listTitle.add("No filter");
            listParam.add("");
            listFilter.add(null);
        } else {
            for (FilterEntity entity : filter.getCurrentFilter()) {
                listTitle.add(entity.filterTemplate_.name_);
                listParam.add("    " + entity.param_);
                listFilter.add(entity);
            }
        }
        ArrayList<Map<String, Object>> mData = new ArrayList<>();

        int lengh = listTitle.size();
        for (int i = 0; i < lengh; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("title", listTitle.get(i));
            item.put("text", listParam.get(i));
            item.put("filter", listFilter.get(i));
            mData.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, mData, android.R.layout.simple_list_item_2,
                new String[]{"title", "text"}, new int[]{android.R.id.text1, android.R.id.text2});
        lvFilterListView_.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        Map<String, Object> current = (Map<String, Object>) lvFilterListView_.getItemAtPosition(
                ((AdapterView.AdapterContextMenuInfo) menuInfo).position
        );
        FilterEntity entity = (FilterEntity)current.get("filter");
        if (entity == null) {
            menu.add(0, 0, Menu.NONE, "Add");
        } else {
            FilterTemplate template = Filters.getInstance().getTemplateByShortname(entity.filterTemplate_.shortname_);
            if (DB.getInstance().getWordSequence().getFilter().getCurrentFilter().size()
                    == Filters.getInstance().getTemplates().size()) {
                menu.add(0, 3, Menu.NONE, "Delete");
                if (template.candidateParams_.size() != 0) {
                    menu.add(0, 4, Menu.NONE, "Edit Param");
                }
            } else {
                menu.add(0, 1, Menu.NONE, "Add Before");
                menu.add(0, 2, Menu.NONE, "Add After");
                menu.add(0, 3, Menu.NONE, "Delete");
                if (template.candidateParams_.size() != 0) {
                    menu.add(0, 4, Menu.NONE, "Edit Param");
                }
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Filter filter = DB.getInstance().getWordSequence().getFilter();
        switch (item.getItemId()) {
            case 0:
                onAdd(filter.getCurrentFilter().size());
                break;
            case 1:
                onAdd(menuInfo.position);
                break;
            case 2:
                onAdd(menuInfo.position + 1);
                break;
            case 3:
                onDelete(menuInfo.position);
                break;
            case 4:
                onEditParam(menuInfo.position);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        //adapter.notifyDataSetChanged();
        return true;
    }
}
