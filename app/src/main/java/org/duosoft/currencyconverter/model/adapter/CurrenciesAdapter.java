package org.duosoft.currencyconverter.model.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.duosoft.currencyconverter.R;
import org.duosoft.currencyconverter.utils.Settings;

public class CurrenciesAdapter extends CursorAdapter {

    public CurrenciesAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_currencies, parent, false);
    }

    @Override
    public void bindView(View v, final Context context, final Cursor c) {

        ImageView image_iv = (ImageView) v.findViewById(R.id.image_iv);
        TextView tag_tv = (TextView) v.findViewById(R.id.tag_tv);
        TextView name_tv = (TextView) v.findViewById(R.id.name_tv);

        String tag = c.getString(c.getColumnIndex("tag"));
        String imageName = tag.toLowerCase() + ".png";
        String name = "";
        if (Settings.getDefaultLanguage().equals(Settings.LANGUAGE_RU))
            name = c.getString(c.getColumnIndex("name_ru"));
        else
            name = c.getString(c.getColumnIndex("name_en"));

        try {
            Drawable drawable = Drawable.createFromStream(v.getContext().getAssets().open(imageName), null);
            image_iv.setImageDrawable(drawable);
        }catch (Exception e) {
            e.printStackTrace();
        }

        name_tv.setText(name);
        tag_tv.setText(tag);

    }
}
