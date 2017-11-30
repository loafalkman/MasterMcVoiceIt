package se.su.dsv.mastermcvoiceit.cardViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import se.su.dsv.mastermcvoiceit.R;

/**
 * Created by annika on 2017-11-29.
 */
public class TempView extends RelativeLayout {
    LayoutInflater mInflater;
    TextView tempTextView;

    public TempView(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public TempView(Context context, AttributeSet attrs, int defStyle)  {
        super(context, attrs, defStyle);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public TempView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public void init() {
        View v = mInflater.inflate(R.layout.item_commandhistory_temp, this, true);
        tempTextView = (TextView) v.findViewById(R.id.textview_tempitem_value);
    }
}