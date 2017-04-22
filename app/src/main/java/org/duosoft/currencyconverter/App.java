package org.duosoft.currencyconverter;

import android.app.Application;
import android.content.Context;

import org.duosoft.currencyconverter.utils.Db;
import org.duosoft.currencyconverter.utils.Settings;

import java.util.Arrays;
import java.util.Locale;

public class App extends Application {

    private static Context context;

    private static String [] russianSpeakingCountryCodesISO3 = {"RUS", "UKR", "BLR", "KGZ", "KAZ", "TJK", "UZB", "MDA"};

    @Override
    public void onCreate() {
        super.onCreate();

        App.context = getApplicationContext();
        Db.init(App.context);

        String county = Locale.getDefault().getISO3Country();
        if (Arrays.asList(russianSpeakingCountryCodesISO3).contains(county)) {
            Settings.setDefaultLanguage(Settings.LANGUAGE_RU);
        }
    }

    public static Context getAppContext() {
        return App.context;
    }

}
