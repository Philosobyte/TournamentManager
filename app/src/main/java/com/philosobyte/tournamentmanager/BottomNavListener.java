package com.philosobyte.tournamentmanager;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by Ray on 1/18/2018.
 */

class BottomNavListener implements BottomNavigationView.OnNavigationItemSelectedListener {
    CheckBox checkBox;
    Button btnAdd;
    EditText editText;
    String savedAddText = "";
    String savedSearchText = "";
    MenuItem lastSelected;
    SelectableAdapter adapter;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //STUB
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //STUB
        }

        @Override
        public void afterTextChanged(Editable s) {
            adapter.getFilter().filter(s);
        }
    };

    public BottomNavListener(Button btnAdd, EditText editText, SelectableAdapter adapter) {
        this(null, btnAdd, editText, adapter);
    }

    public BottomNavListener(CheckBox checkBox, Button btnAdd,
                             EditText editText,SelectableAdapter adapter) {
        this.checkBox = checkBox;
        this.btnAdd = btnAdd;
        this.editText = editText;
        this.adapter = adapter;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item == lastSelected) {
            return true;
        }
        lastSelected = item;
        switch (item.getItemId()) {
            case R.id.action_add:
                savedSearchText = editText.getText().toString();
                if (checkBox != null) {
                    checkBox.setVisibility(View.VISIBLE);
                }
                btnAdd.setVisibility(View.VISIBLE);
                editText.removeTextChangedListener(textWatcher);
                adapter.getFilter().filter("");
                editText.setText(savedAddText);
                break;
            case R.id.action_search:
                savedAddText = editText.getText().toString();
                if (checkBox != null) {
                    checkBox.setVisibility(View.GONE);
                }
                btnAdd.setVisibility(View.GONE);
                editText.addTextChangedListener(textWatcher);
                editText.setText(savedSearchText);

        }
        return true;
    }
}
