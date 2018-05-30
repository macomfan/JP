package com.jpword.ma.jpword;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import DataEngine.DB;
import DataEngine.Filter;
import DataEngine.FilterEntity;
import JPWord.Data.IWord;
import JPWord.Data.IWordDictionary;

/**
 * Created by Ma on 2017/12/20.
 */

public class FragmentRemember extends com.jpword.ma.baseui.FragmentRemember {
    private IWord currentWord_ = null;

    @Override
    protected void onbtnNextClicked(View v) {
        currentWord_ = DB.getInstance().getWordSequence().next();
        displayWord();
    }

    @Override
    protected void onbtnPrevClicked(View v) {
        currentWord_ = DB.getInstance().getWordSequence().prev();
        displayWord();
    }

    @Override
    protected void onbtnHintClicked(View v) {
        if (currentWord_ == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        LayoutInflater factory = LayoutInflater.from(this.getContext());
        View dialogView = factory.inflate(R.layout.dialog_hint, null);
        TextView txtContent = (TextView) dialogView.findViewById(R.id.txtContent);
        TextView txtKana = (TextView) dialogView.findViewById(R.id.txtKana);
        TextView txtRoma = (TextView) dialogView.findViewById(R.id.txtRoma);
        TextView txtImi = (TextView) dialogView.findViewById(R.id.txtImi);
        TextView txtNote = (TextView) dialogView.findViewById(R.id.txtNote);
        txtContent.setText(currentWord_.getContent());
        String kana = currentWord_.getKana();
        if (!currentWord_.getTone().isEmpty()) {
            kana += " (";
            kana += currentWord_.getTone();
            kana += ")";
        }
        txtKana.setText(kana);
        String roma = "";
        if (currentWord_.getRoma() != null) {
            roma = currentWord_.getRoma().getString();
        }
        txtRoma.setText(roma);
        String meaning = DataEngine.MeaningUtil.meaningToString(currentWord_.getMeanings());
        txtImi.setText(meaning);

        if (currentWord_.getNote().equals("")) {
            txtNote.setVisibility(View.GONE);
        } else {
            txtNote.setVisibility(View.VISIBLE);
            txtNote.setText(currentWord_.getNote());
        }

        builder.setTitle("Hint");
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        builder.create().show();
    }

    @Override
    protected void onbtnPassClicked(View v) {
        if (currentWord_ != null) {
            currentWord_.increaseSkill();
            currentWord_ = DB.getInstance().getWordSequence().next();
            displayWord();
        }
    }

    @Override
    protected void onbtnFailClicked(View v) {
        if (currentWord_ != null) {
            currentWord_.updateSkill(-5);
            currentWord_ = DB.getInstance().getWordSequence().next();
            displayWord();
        }
    }

    @Override
    protected void onbtRefreshClicked(View v) {
        DB.getInstance().getWordSequence().reSort();
        currentWord_ = DB.getInstance().getWordSequence().current();
        displayWord();
        Toast.makeText(getActivity(), "Refresh done", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        InputMethodManager imm = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(this.getView().getApplicationWindowToken(), 0);
        }
        displayWord();
        refreshFilterDisplay();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        currentWord_ = DB.getInstance().getWordSequence().current();
        displayWord();
        refreshFilterDisplay();
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnSetting: {
                Intent intent = new Intent(getActivity(), ActivityFilterSetting.class);
                getActivity().startActivity(intent);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onImgMyWordClicked(View v) {
        if (currentWord_ == null) {
            return;
        }
        currentWord_.setTag("MY", "Y");
        mImgMyWord.setClicked(true);
    }

    private void refreshFilterDisplay() {
        Filter filter = DB.getInstance().getWordSequence().getFilter();
        int i = 0;
        for (FilterEntity entity : filter.getCurrentFilter()) {
            mTxtFilterList.get(i).setVisibility(View.VISIBLE);
            if (entity != null) {
                mTxtFilterList.get(i).setText(entity.filterTemplate_.shortname_);
            } else {
                mTxtFilterList.get(i).setText("ERR");
            }

            i++;
        }
        for (; i < mTxtFilterList.size(); i++) {
            mTxtFilterList.get(i).setVisibility(View.GONE);
        }
    }

    private void displayWord() {
        boolean displayKanJi = DB.getInstance().getWordSequence().getFilter().isDisplayKanJi();
        progbar_.setMax(DB.getInstance().getWordSequence().count());
        if (DB.getInstance().getWordSequence().getCurrentIndex() == -1) {
            btnPrev_.setEnabled(false);
        } else {
            btnPrev_.setEnabled(true);
        }
        if (DB.getInstance().getWordSequence().getCurrentIndex() == DB.getInstance().getWordSequence().count()) {
            btnNext_.setEnabled(false);
        } else {
            btnNext_.setEnabled(true);
        }

        if (currentWord_ == null) {
            txtRememberMainText_.setText("");
            txtCount_.setText("");
            txtSkill_.setText("");
            txtRD_.setText("");
            mTxtCls.setText("");
            progbar_.setProgress(0);
            btnHint_.setEnabled(false);
            btnFail_.setEnabled(false);
            btnPass_.setEnabled(false);
            mImgMyWord.setClicked(false);
            return;
        }
        btnHint_.setEnabled(true);
        btnPass_.setEnabled(true);
        btnFail_.setEnabled(true);
        if (displayKanJi) {
            txtRememberMainText_.setText(currentWord_.getContent());
        } else {
            txtRememberMainText_.setText(currentWord_.getKana());
        }

        if (DB.getInstance().getWordSequence().getCurrentIndex() < progbar_.getMax())
            progbar_.setProgress(DB.getInstance().getWordSequence().getCurrentIndex() + 1);

        txtCount_.setText(String.format("%d / %d", DB.getInstance().getWordSequence().getCurrentIndex() + 1, DB.getInstance().getWordSequence().count()));
        txtSkill_.setText(Integer.toString(currentWord_.getSkill(), 10));

        txtRD_.setText(currentWord_.getReviewDate());
        mTxtCls.setText(Integer.toString(currentWord_.getCls(),10));

        if (!currentWord_.getTag("MY").equals("")) {
            mImgMyWord.setClicked(true);
        } else {
            mImgMyWord.setClicked(false);
        }
    }

}
