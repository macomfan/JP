package com.jpword.ma.jpword;

import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import DataEngine.AppLogging;
import DataEngine.DBEntity;
import DataEngine.DatabaseServiceConnection;
import DataEngine.IDatabaseChangeListener;
import JPWord.Data.IExample;
import JPWord.Data.IMeaning;
import JPWord.Data.IRoma;
import JPWord.Data.IWord;
import JPWord.Data.Yin50;

/**
 * Created by u0151316 on 12/21/2017.
 */

public class ActivityEdit extends com.jpword.ma.baseui.ActivityEdit implements IDatabaseChangeListener {
    private IWord currentWord_ = null;
    private DBEntity dbEntity_ = null;

    private DatabaseServiceConnection connection_ = new DatabaseServiceConnection() {
        @Override
        public void onServiceConnected() {
            AppLogging.showDebug(ActivityEdit.class, "onServiceConnected");
            dbEntity_ = getDatabaseOperator().getDBEntity();
        }

        @Override
        public void onServiceDisconnected() {

        }
    };

    @Override
    public void onDatabaseChange(DBEntity dbEntity) {
        dbEntity_ = dbEntity;
    }

    @Override
    protected boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnEdit:
                if (currentWord_ != null) {
                    setEditMode(true);
                }
                return true;
        }
        return false;
    }

    @Override
    protected void aftertxtKanaTextChanged(Editable s) {
        if (s.length() != 0) {
            IRoma roma = Yin50.getInstance().kanaToRoma(s.toString());
            if (roma == null) {
                txtRoma_.setText("?");
            } else {
                txtRoma_.setText(roma.getString());
            }
        }
    }

    @Override
    protected void onbtnSaveClick(View v) {
        if (checkChanged()) {
            currentWord_.setContent(txtContent_.getText().toString());
            currentWord_.setKana(txtKana_.getText().toString());
            currentWord_.setTone(txtTone_.getText().toString());
            currentWord_.setNote(txtNote_.getText().toString());
//            List<IMeaning> means = MeaningUtil.parseStringToMeaning(jtxtMean.getText());
//            currentWord_.updateMeaning(means);
////            for (IMeaning mean : means) {
////                System.err.println("MMM: " + mean.encodeToString());
////            }
            dbEntity_.dict_.addWord(currentWord_);
//            refreshMainTable(currentWord_);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String idString = this.getIntent().getStringExtra("ID");
        if (idString.equals("")) {
            return;
        } else if (idString.equals("NEW")) {
            currentWord_ = dbEntity_.dict_.createWord();
            bindCurrentWord();
            setEditMode(true);
        } else {
            currentWord_ = dbEntity_.dict_.getWord(idString);
            bindCurrentWord();
            setEditMode(false);
        }
    }

    private void setEditMode(boolean editMode) {
        if (editMode) {
            barMenu_.setVisibility(View.VISIBLE);
            txtContent_.setEnabled(true);
            txtKana_.setEnabled(true);
            txtTone_.setEnabled(true);
            txtNote_.setEnabled(true);
        } else {
            barMenu_.setVisibility(View.GONE);
            txtContent_.setEnabled(false);
            txtKana_.setEnabled(false);
            txtTone_.setEnabled(false);
            txtNote_.setEnabled(false);
        }
    }

    private void bindCurrentWord() {
        if (currentWord_ == null) {
            return;
        }
        txtContent_.setText(currentWord_.getContent());
        txtKana_.setText(currentWord_.getKana());
        if (currentWord_.getRoma() != null) {
            txtRoma_.setText(currentWord_.getRoma().getString());
        }
        txtNote_.setText(currentWord_.getNote());
        txtTone_.setText(currentWord_.getTone());
        refreshMeaning();
    }

    private void refreshMeaning() {
        if (currentWord_.getMeanings().isEmpty()) {
            barImi_.setVisibility(View.INVISIBLE);
        } else {
            barImi_.setVisibility(View.VISIBLE);
        }
        ArrayList<Map<String, Object>> data = new ArrayList<>();
        for (IMeaning mean : currentWord_.getMeanings()) {
            String type = "[ " + mean.getType() + " ]";
            String meaning = mean.getInCHS();
            if (!mean.getInJP().isEmpty()) {
                meaning += "\r\n" + "(" + mean.getInJP() + ")";
            }
            Map<String, Object> item = new HashMap<>();
            item.put("type", type);
            item.put("mean", meaning);
            String EXP = "";
            for (IExample example : mean.getExamples()) {
                EXP += example.getExampleInCHS();
            }
            item.put("exp", EXP);
            data.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.listview_entity_meaning,
                new String[]{"type", "mean", "exp"}, new int[]{R.id.txtType, R.id.txtMeaning, R.id.txtExample});
        lvMeaning.setAdapter(adapter);
    }

    private boolean checkChanged() {
        boolean changed = false;
        if (!currentWord_.getContent().equals(txtContent_.getText().toString())) {
            changed = true;
        } else if (!currentWord_.getKana().equals(txtKana_.getText().toString())) {
            changed = true;
        } else if (!currentWord_.getTone().equals(txtTone_.getText().toString())) {
            changed = true;
//        } else if (!currentWord_.getNote().equals(jtxtNode.getText())) {
//            changed = true;
//        }
//        String m1 = MeaningUtil.meaningToString(currentWord_);
//        String m2 = MeaningUtil.meaningToString(MeaningUtil.parseStringToMeaning(jtxtMean.getText()));
//        if (!m1.equals(m2)) {
//            changed = true;
        }
        return changed;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            AppLogging.showDebug(ActivityEdit.class, "onDestroy");
            DatabaseService.unbind(this, connection_);
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
