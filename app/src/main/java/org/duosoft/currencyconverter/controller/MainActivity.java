package org.duosoft.currencyconverter.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.duosoft.currencyconverter.R;
import org.duosoft.currencyconverter.model.Currency;
import org.duosoft.currencyconverter.utils.Db;
import org.duosoft.currencyconverter.utils.Exchanger;
import org.duosoft.currencyconverter.utils.Helper;
import org.duosoft.currencyconverter.utils.RequestCodes;
import org.duosoft.currencyconverter.utils.Settings;

public class MainActivity extends Activity {

    View loading_l, convert_l, from_l, to_l;
    TextView from_tag_tv, to_tag_tv, from_name_tv, to_name_tv;
    ImageView from_image_iv, to_image_iv;
    EditText from_et;
    TextView result_tv;

    Currency selectedCurrencyFrom, selectedCurrencyTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loading_l = findViewById(R.id.loading_l);
        convert_l = findViewById(R.id.convert_l);
        from_l = findViewById(R.id.from_l);
        to_l = findViewById(R.id.to_l);

        from_et = (EditText) findViewById(R.id.from_et);

        from_tag_tv = (TextView) findViewById(R.id.from_tag_tv);
        to_tag_tv = (TextView) findViewById(R.id.to_tag_tv);
        from_name_tv = (TextView) findViewById(R.id.from_name_tv);
        to_name_tv = (TextView) findViewById(R.id.to_name_tv);

        result_tv = (TextView) findViewById(R.id.result_tv);

        from_image_iv = (ImageView) findViewById(R.id.from_image_iv);
        to_image_iv = (ImageView) findViewById(R.id.to_image_iv);

        loading_l.setVisibility(View.GONE);
        convert_l.setVisibility(View.VISIBLE);

        selectedCurrencyFrom = Db.getCurrency(Settings.getDefaultCurrencyIdFrom());
        selectedCurrencyTo = Db.getCurrency(Settings.getDefaultCurrencyIdTo());

        from_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CurrenciesActivity.class);
                startActivityForResult(intent, RequestCodes.SELECT_CURRENCY_FROM);
            }
        });

        to_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CurrenciesActivity.class);
                startActivityForResult(intent, RequestCodes.SELECT_CURRENCY_TO);
            }
        });

        convert_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convert();
            }
        });

        AdView adView = (AdView) findViewById(R.id.adView);
        MobileAds.initialize(getApplicationContext(), getString(R.string.admob_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        setViews();
        convert();
    }

    void convert() {

        final String tagFrom, tagTo;

        if (selectedCurrencyFrom == null || selectedCurrencyTo == null) {
            return;
        }

        tagFrom = selectedCurrencyFrom.getTag();
        tagTo = selectedCurrencyTo.getTag();

        loading_l.setVisibility(View.VISIBLE);
        convert_l.setVisibility(View.GONE);
        result_tv.setText("0.000");

        new AsyncTask<Void, Void, Void>() {

            double rate;

            @Override
            protected Void doInBackground(Void... voids) {

                rate = Exchanger.getRate(tagFrom, tagTo);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                if (rate == -1) {
                    Toast.makeText(MainActivity.this, R.string.internet_request_failed_message, Toast.LENGTH_SHORT).show();
                }
                if (rate == -2) {
                    Toast.makeText(MainActivity.this, R.string.json_parse_failed_message, Toast.LENGTH_SHORT).show();
                }
                if (rate >= 0) {

                    double from = 1.0;
                    try {
                        from = Double.parseDouble(from_et.getText().toString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    result_tv.setText(Helper.doubleToString(rate * from));
                }

                loading_l.setVisibility(View.GONE);
                convert_l.setVisibility(View.VISIBLE);

            }
        }.execute();
    }

    void setViews () {
        from_tag_tv.setText(selectedCurrencyFrom.getTag());
        from_name_tv.setText(selectedCurrencyFrom.getName());
        to_tag_tv.setText(selectedCurrencyTo.getTag());
        to_name_tv.setText(selectedCurrencyTo.getName());
        try {
            from_image_iv.setImageDrawable(Drawable.createFromStream(getAssets().open(selectedCurrencyFrom.getImage()), null));
            to_image_iv.setImageDrawable(Drawable.createFromStream(getAssets().open(selectedCurrencyTo.getImage()), null));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodes.SELECT_CURRENCY_FROM && resultCode == RESULT_OK && data != null) {
            int id = data.getIntExtra("_id", 9);
            selectedCurrencyFrom = Db.getCurrency(id);
            Settings.setDefaultCurrencyIdFrom(id);
        }
        if (requestCode == RequestCodes.SELECT_CURRENCY_TO && resultCode == RESULT_OK && data != null) {
            int id = data.getIntExtra("_id", 9);
            selectedCurrencyTo = Db.getCurrency(id);
            Settings.setDefaultCurrencyIdTo(id);
        }
        setViews();
    }
}
