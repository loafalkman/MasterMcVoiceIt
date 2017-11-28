package se.su.dsv.mastermcvoiceit.mainCards;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by felix on 2017-11-28.
 */

public class CardRVAdapter extends RecyclerView.Adapter {
    ArrayList<CardInfo> cardModels;



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType (int position) {
        //Some logic to know which type will come next;
        return cardModels.get(position).getItemViewType().ordinal();
    }
}
