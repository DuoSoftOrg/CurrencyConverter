package org.duosoft.currencyconverter;

import android.app.Application;
import android.content.Context;

import org.duosoft.currencyconverter.utils.Db;

public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        App.context = getApplicationContext();
        Db.init(App.context);
    }

    public static Context getAppContext() {
        return App.context;
    }

}
