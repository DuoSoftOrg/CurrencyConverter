package org.duosoft.currencyconverter.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import org.duosoft.currencyconverter.R;
import org.duosoft.currencyconverter.model.adapter.CurrenciesAdapter;
import org.duosoft.currencyconverter.utils.Db;
import org.duosoft.currencyconverter.utils.Settings;

public class CurrenciesActivity extends Activity {

    EditText search_et;
    ListView list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currencies);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        search_et = (EditText) findViewById(R.id.search_et);
        list_view = (ListView) findViewById(R.id.list_view);

        final CurrenciesAdapter adapter = new CurrenciesAdapter(CurrenciesActivity.this, Db.query("select * from currencies"));
        list_view.setAdapter(adapter);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = ((CurrenciesAdapter)parent.getAdapter()).getCursor();
                int _id = c.getInt(c.getColumnIndex("_id"));

                Intent i = new Intent();
                i.putExtra("_id", _id);
                setResult(RESULT_OK, i);

                finish();
            }
        });

        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String q = s.toString().trim().toLowerCase();

                if (Settings.getDefaultLanguage().equals(Settings.LANGUAGE_RU))
                    adapter.changeCursor(Db.query("select * from currencies where [tag] like '"+q+"%' collate nocase or [name_ru] like '%"+q+"%' collate nocase"));
                else
                    adapter.changeCursor(Db.query("select * from currencies where [tag] like '"+q+"%' collate nocase or [name_en] like '%"+q+"%' collate nocase"));

                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
