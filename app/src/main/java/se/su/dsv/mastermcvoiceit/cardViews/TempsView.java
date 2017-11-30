package se.su.dsv.mastermcvoiceit.cardViews;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import se.su.dsv.mastermcvoiceit.R;

public class TempsView extends ConstraintLayout {
    private LayoutInflater mInflater;
    private LinearLayout tempsList;

    public TempsView(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public TempsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public TempsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public void init() {
        View v = mInflater.inflate(R.layout.item_cardholder_temperatures, this, true);
        tempsList = (LinearLayout) v.findViewById(R.id.linearLayout_temperatures_tempslist);
    }

    public void setTempsList(String[] sensorNames, float[] temperatures) {
        tempsList.removeAllViews();

        for (int i = 0; i < sensorNames.length; i++) {
            View row = mInflater.inflate(R.layout.row_temperatures_nameandvalue, null, false);

            TextView sensorName = row.findViewById(R.id.textview_temprow_sensorname);
            TextView sensorValue = row.findViewById(R.id.textview_temprow_sensorvalue);

            sensorName.setText(sensorNames[i] + ":");
            sensorValue.setText(temperatures[i] + "C ");

            tempsList.addView(row);
        }

    }
}