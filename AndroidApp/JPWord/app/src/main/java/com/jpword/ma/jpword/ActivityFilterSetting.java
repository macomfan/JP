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
import android.widget.Toast;

import com.jpword.ma.baseui.DialogMultiTextSelect;
import com.jpword.ma.baseui.DialogSingleItemSelect;
import com.jpword.ma.baseui.IMyDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import DataEngine.AppLogging;
import DataEngine.DatabaseServiceConnection;
import JPLibAssist.FilterGenerator;
import JPLibAssist.FilterTemplate;
import JPLibAssist.DisplaySetting;
import JPLibAssist.FilterEntity;
import JPLibAssist.Filters;


/**
 * Created by u0151316 on 1/3/2018.
 */

public class ActivityFilterSetting extends com.jpword.ma.baseui.ActivityFilterSetting {

    private Filters filters_ = null;
    private DisplaySetting displaySetting_ = null;

    private DatabaseServiceConnection connection_ = new DatabaseServiceConnection() {
        @Override
        public void onServiceConnected() {
            filters_ = getDatabaseOperator().getDBEntity().filters_;
            displaySetting_ = getDatabaseOperator().getDBEntity().displaySetting_;
        }

        @Override
        public void onServiceDisconnected() {

        }
    };

    public void onAdd(final int index) {
        final String defParam = "";
        List<String> filterNames = new LinkedList<>();
        for (FilterTemplate template : FilterGenerator.getInstance().getTemplates()) {
            boolean found = false;
            for (FilterEntity currentEntity : filters_.getCurrentFilters()) {
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
                FilterTemplate template = FilterGenerator.getInstance().getTemplateByName(ret);
                String defParam = "";
                if (template != null) {
                    defParam = template.candidateParams_.defaultParam_;
                }
                filters_.addFilterByFilterTemplate(index, template, defParam);
                refreshCurrentFilter();
            }
        });
    }

    public void onDelete(int index) {
        filters_.removeFilter(index);
        refreshCurrentFilter();
    }

    public void onEditParam(int index) {
        final FilterEntity entity = filters_.getAt(index);
        FilterTemplate template = FilterGenerator.getInstance().getTemplateByShortname(entity.filterTemplate_.shortname_);

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
            FilterTemplate template = FilterGenerator.getInstance().getTemplateByShortname(entity.filterTemplate_.shortname_);
            if (template == null || template.candidateParams_.size() == 0) {
                return;
            }
            onEditParam(position);
        }
    }

    @Override
    protected void onRBGroupCheckedChange(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == R.id.rbDispKanji) {
            displaySetting_.setDisplayKanJi(true);
        } else if (checkedId == R.id.rbDispKana) {
            displaySetting_.setDisplayKanJi(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerForContextMenu(lvFilterListView_);
        refreshCurrentFilter();
    }

    private void refreshCurrentFilter() {
        boolean displayKanji = displaySetting_.isDisplayKanJi();
        if (displayKanji) {
            mRBDisplayKanJi.setChecked(true);
        } else {
            mRBDisplayKana.setChecked(true);
        }

        List<String> listTitle = new LinkedList<>();
        List<String> listParam = new LinkedList<>();
        List<FilterEntity> listFilter = new LinkedList<>();
        if (filters_.getCurrentFilters().isEmpty()) {
            // no filter
            listTitle.add("No filter");
            listParam.add("");
            listFilter.add(null);
        } else {
            for (FilterEntity entity : filters_.getCurrentFilters()) {
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
        FilterEntity entity = (FilterEntity) current.get("filter");
        if (entity == null) {
            menu.add(0, 0, Menu.NONE, "Add");
        } else {
            FilterTemplate template = FilterGenerator.getInstance().getTemplateByShortname(entity.filterTemplate_.shortname_);
            if (filters_.getCurrentFilters().size()
                    == FilterGenerator.getInstance().getTemplates().size()) {
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
        switch (item.getItemId()) {
            case 0:
                onAdd(filters_.getCurrentFilters().size());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            AppLogging.showDebug(ActivityFilterSetting.class, "onDestroy");
            DatabaseService.unbind(this, connection_);
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
