package se.su.dsv.mastermcvoiceit.mainCards;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import se.su.dsv.mastermcvoiceit.R;

/**
 * Created by felix on 2017-11-28.
 */

public class CardRVAdapter extends RecyclerView.Adapter {
    Context context;
    LayoutInflater inflater;

    ArrayList<CardInfo> cardModels;

    public CardRVAdapter(Context context, ArrayList<CardInfo> cardModels) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.cardModels = cardModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardInfoType type = CardInfoType.values()[viewType];

        switch (type) {
            case TEMPERATURE:
                View tempView = inflater.inflate(R.layout.item_commandhistory_temp, parent, false);
                Log.v("CardRVAda", "made temp viewHolder");
                return new TempViewHolder(tempView);

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CardInfo cardInfo = cardModels.get(position);

        switch (cardInfo.getItemViewType()) {

            case TEMPERATURE:
                TextView tempTV = ((TempViewHolder) holder).message;
                String tempText = ((TempCardInfo) cardInfo).getText();
                tempTV.setText(tempText);

        }
    }

    @Override
    public int getItemCount() {
        return cardModels.size();
    }

    @Override
    public int getItemViewType (int position) {
        return cardModels.get(position).getItemViewType().ordinal();
    }


    public class TempViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView message;

        TempViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.textview_tempitem_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, "That tickles!", Toast.LENGTH_SHORT).show();
        }
    }
}
