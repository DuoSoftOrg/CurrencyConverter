package org.duosoft.currencyconverter.utils;

import android.util.Log;

import java.text.DecimalFormat;
import java.util.Calendar;

public class Helper {
    public static String doubleToString(double val) {
        return new DecimalFormat("#0.000").format(val);
    }
    public static void printLine (String message) {
        Log.d("tag", message);
    }
}
