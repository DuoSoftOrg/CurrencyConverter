package org.duosoft.currencyconverter.utils;


import android.preference.PreferenceManager;

import org.duosoft.currencyconverter.App;

public class Settings {

    public static final String LANGUAGE_EN = "En";
    public static final String LANGUAGE_RU = "Ru";

    public static int getDefaultCurrencyIdFrom () {
        return PreferenceManager.getDefaultSharedPreferences(App.getAppContext()).getInt("default_currency_from", 142); //USA dollar
    }
    public static void setDefaultCurrencyIdFrom (int id) {
        PreferenceManager.getDefaultSharedPreferences(App.getAppContext()).edit().putInt("default_currency_from", id).apply();
    }
    public static int getDefaultCurrencyIdTo () {
        return PreferenceManager.getDefaultSharedPreferences(App.getAppContext()).getInt("default_currency_to", 9); //Russian ruble
    }
    public static void setDefaultCurrencyIdTo (int id) {
        PreferenceManager.getDefaultSharedPreferences(App.getAppContext()).edit().putInt("default_currency_to", id).apply();
    }
    public static String getDefaultLanguage() {
        return PreferenceManager.getDefaultSharedPreferences(App.getAppContext()).getString("default_language", Settings.LANGUAGE_RU); //Russian currency names
    }
    public static void setDefaultLanguage (String langTag) {
        PreferenceManager.getDefaultSharedPreferences(App.getAppContext()).edit().putString("default_language", langTag).apply();
    }
}
