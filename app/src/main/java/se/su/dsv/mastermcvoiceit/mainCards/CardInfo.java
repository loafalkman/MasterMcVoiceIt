package se.su.dsv.mastermcvoiceit.mainCards;

/**
 * Created by felix on 2017-11-28.
 */

public abstract class CardInfo {
    private int itemViewType;

    public CardInfo(int itemViewType) {
        this.itemViewType = itemViewType;
    }

    public int getItemViewType() {
        return itemViewType;
    }
}
