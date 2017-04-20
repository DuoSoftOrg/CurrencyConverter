package org.duosoft.currencyconverter.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Exchanger {

    public static double getRate (String tagFrom, String tagTo) {
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL("https://query.yahooapis.com/v1/public/yql?q=select+*+from+yahoo.finance.xchange+where+pair+=+%22"+tagFrom+tagTo+"%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");
            Log.d("tag", "https://query.yahooapis.com/v1/public/yql?q=select+*+from+yahoo.finance.xchange+where+pair+=+%22"+tagFrom+tagTo+"%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }


        String rate = "";
        try {
            JSONObject resp = new JSONObject(buffer.toString());
            rate = resp.getJSONObject("query").getJSONObject("results").getJSONObject("rate").getString("Rate");
        }catch (Exception e) {
            e.printStackTrace();
            return -2;
        }

        double rateD = 1;
        try {
            rateD = Double.parseDouble(rate);
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
        return rateD;
    }
}
