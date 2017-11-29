package se.su.dsv.mastermcvoiceit.mainCards;

/**
 * Created by felix on 2017-11-28.
 */

public abstract class CardInfo {
    private CardInfoType itemViewType;

    public CardInfo(CardInfoType itemViewType) {
        this.itemViewType = itemViewType;
    }

    public CardInfoType getItemViewType() {
        return itemViewType;
    }
}
