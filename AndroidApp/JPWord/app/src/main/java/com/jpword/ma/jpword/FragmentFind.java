package com.jpword.ma.jpword;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import DataEngine.DB;
import JPWord.Data.IMeaning;
import JPWord.Data.IWord;
import JPWord.Data.IWordDictionary;

/**
 * Created by u0151316 on 12/14/2017.
 */

public class FragmentFind extends com.jpword.ma.baseui.FragmentFind {
    enum SearchMode {
        SEARCH_BY_KANA,
        SEARCH_BY_IMI,
    }

    private String txt = "";
    private SearchMode mSearchMode = SearchMode.SEARCH_BY_KANA;

    @Override
    protected boolean onsvSearchInputQueryTextSubmit(String query) {
        return super.onsvSearchInputQueryTextSubmit(query);
    }

    @Override
    protected boolean onsvSearchInputQueryTextChange(String newText) {
        txt = newText;
        refreshMainListView(newText);
        return false;
    }

    @Override
    protected void onmainListViewClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> current = (Map<String, Object>) mainListView_.getItemAtPosition(position);
        String idString = (String) current.get("id");
        System.out.println("setOnItemClickListener: " + idString);
        Intent intent = new Intent(this.getActivity(), ActivityEdit.class);
        intent.putExtra("ID", idString);
        this.getActivity().startActivity(intent);
    }

    @Override
    protected void onbtnNewClick(View v) {
        Intent intent = new Intent(this.getActivity(), ActivityEdit.class);
        intent.putExtra("ID", "NEW");
        this.getActivity().startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        refreshMainListView("");
        return v;
    }

    private void addWordToRow(IWord word, ArrayList<Map<String, Object>> data) {
        if (word == null) {
            return;
        }

        String type = "[ ";
        for (IMeaning m : word.getMeanings()) {
            if (!type.contains(m.getType())) {
                type += m.getType();
                type += " ";
            }
        }
        if (!type.equals("")) {
            type += "]";
        } else if (type.equals("[ ")) {
            type = "";
        }

        Map<String, Object> item = new HashMap<>();
        item.put("title", word.getContent());
        item.put("text", word.getKana() + "  (" + word.getTone() + ")");
        item.put("type", type);
        item.put("id", word.getID());
        data.add(item);
    }

    private void refreshMainListView(String searchString) {
        ArrayList<Map<String, Object>> data = new ArrayList<>();
        IWordDictionary db = DB.getInstance().getDatabase();
        for (IWord word : db.getWords()) {
            if (searchString != null && searchString.equals("*")) {
                addWordToRow(word, data);
            } else if (searchString != null && searchString.equals("")) {
                //Do nothing
            } else if (word.getContent().indexOf(searchString) != -1
                    || word.getKana().indexOf(searchString) != -1
                    || word.getRoma().hitTest(searchString) == true) {
                addWordToRow(word, data);
            }
        }

        SimpleAdapter adapter = new SimpleAdapter(this.getActivity(), data, R.layout.listview_entity_word,
                new String[]{"title", "text", "type"}, new int[]{R.id.txtTitle, R.id.txtContent, R.id.txtType});
        mainListView_.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemKana: {
                mSearchMode = SearchMode.SEARCH_BY_KANA;
                svSearchInput_.setQueryHint("Pls input kana or roma");
                item.setChecked(true);
                return true;
            }
            case R.id.itemImi: {
                mSearchMode = SearchMode.SEARCH_BY_IMI;
                svSearchInput_.setQueryHint("Pls input imi");
                item.setChecked(true);
                return true;
            }
        }
        return false;
    }
}
