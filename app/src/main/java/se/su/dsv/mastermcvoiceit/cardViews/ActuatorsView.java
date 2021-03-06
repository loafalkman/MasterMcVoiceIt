package se.su.dsv.mastermcvoiceit.cardViews;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import se.su.dsv.mastermcvoiceit.R;

public class ActuatorsView extends ConstraintLayout {
    private static final String TITLE = "Actuators";
    private LayoutInflater mInflater;
    private LinearLayout actuatorsList;

    private SwitchesListener switchesListener;
    private ArrayList<Switch> switches;

    public ActuatorsView(Context context, SwitchesListener switchesListener) {
        super(context);
        this.switchesListener = switchesListener;
        mInflater = LayoutInflater.from(context);
        init();
    }

    public ActuatorsView(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public ActuatorsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public ActuatorsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public void init() {
        View v = mInflater.inflate(R.layout.layout_cardsholder_genericlayout, this, true);
        actuatorsList = (LinearLayout) v.findViewById(R.id.linearlayout_genericlayout_container);

        TextView title = v.findViewById(R.id.textview_genericlayout_title);
        title.setText(TITLE);
    }

    public void setSwitches(String[] actuatorNames) {
        switches = new ArrayList<>();
        actuatorsList.removeAllViews();

        for (int i = 0; i < actuatorNames.length; i++) {
            final Switch swtch = new Switch(getContext());
            swtch.setText(actuatorNames[i]);
            final int finalI = i;

            swtch.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    switchesListener.onSwitchChange(finalI, ((Switch) view).isChecked());
                }
            });

            switches.add(swtch);
            actuatorsList.addView(swtch);
        }

    }

    public void updateSwitches(int[] states) {
        for (int i = 0; i < switches.size(); i++) {
            switches.get(i).setChecked(states[i] > 0);
        }
    }

    public interface SwitchesListener {
        void onSwitchChange(int switchIndex, boolean checked);
    }
}