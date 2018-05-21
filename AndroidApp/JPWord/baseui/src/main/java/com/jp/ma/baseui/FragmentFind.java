package com.jp.ma.baseui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

/**
 * Created by u0151316 on 1/4/2018.
 */

public class FragmentFind extends Fragment {
    protected ListView mainListView_ = null;
    protected SearchView svSearchInput_ = null;
    protected ImageButton btnNew_ = null;

    protected boolean onsvSearchInputQueryTextSubmit(String query) {
        return false;
    }

    protected boolean onsvSearchInputQueryTextChange(String newText) {
        return false;
    }

    protected void onmainListViewClick(AdapterView<?> parent, View view, int position, long id) {
    }

    protected void onbtnNewClick(View v) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_find, container, false);
        svSearchInput_ = (SearchView) v.findViewById(R.id.svSearchInput);
        svSearchInput_.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return FragmentFind.this.onsvSearchInputQueryTextSubmit(query);
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return FragmentFind.this.onsvSearchInputQueryTextChange(newText);
            }
        });
        mainListView_ = (ListView) v.findViewById(R.id.lvMainListView);
        mainListView_.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentFind.this.onmainListViewClick(parent, view, position, id);
            }
        });
        btnNew_ = (ImageButton) v.findViewById(R.id.btnNew);
        btnNew_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentFind.this.onbtnNewClick(v);
            }
        });
        return v;
    }
}
