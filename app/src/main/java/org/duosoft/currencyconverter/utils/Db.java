package org.duosoft.currencyconverter.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.duosoft.currencyconverter.model.Currency;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Db extends SQLiteOpenHelper {

    private static String DB_NAME = "db.sqlite";
    private static String DB_PATH = "/data/data/org.duosoft.currencyconverter/databases/";
    private static int DB_VERSION = 1;

    private static Db db;

    private Context mContext;
    private SQLiteDatabase myDataBase;

    public static void init(Context context) {
        if (db != null) return;
        db = new Db(context);
        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Db(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        if(android.os.Build.VERSION.SDK_INT >= 4.2){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;
    }

    private void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if(dbExist){
            openDataBase();
        } else{
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase(){

        File dbFile = mContext.getDatabasePath(DB_NAME);
        return dbFile.exists();

    }

    private void copyDataBase() throws IOException {

        InputStream myInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[15000];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static Cursor query (String sql) {
        return db.getReadableDatabase().rawQuery(sql, null);
    }
    public static void exec (String sql) {
        db.getWritableDatabase().execSQL(sql);
    }
    public static Currency getCurrency (int id) {
        Cursor c = query("select * from currencies where _id = " + id);
        Currency currency = null;
        if (c.moveToNext()) {
            currency = new Currency();
            currency.setTag(c.getString(c.getColumnIndex("tag")));
            if (Settings.getDefaultLanguage().equals(Settings.LANGUAGE_RU))
                currency.setName(c.getString(c.getColumnIndex("name_ru")));
            else
                currency.setName(c.getString(c.getColumnIndex("name_eng")));
            currency.setImage(currency.getTag().toLowerCase() + ".png");
        }
        c.close();
        return currency;
    }
    public static ArrayList<Currency> getCurrencies () {
        ArrayList<Currency> currencies = null;
        Cursor c = query("select * from currencies");
        while (c.moveToNext()) {
            if (currencies == null)
                currencies = new ArrayList<>();

            Currency currency = new Currency();
            currency.setTag(c.getString(c.getColumnIndex("tag")));
            if (Settings.getDefaultLanguage().equals(Settings.LANGUAGE_RU))
                currency.setName(c.getString(c.getColumnIndex("name_ru")));
            else
                currency.setName(c.getString(c.getColumnIndex("name_eng")));
            currency.setImage(currency.getTag().toLowerCase() + ".png");

            currencies.add(currency);
        }
        c.close();
        return currencies;
    }
}
