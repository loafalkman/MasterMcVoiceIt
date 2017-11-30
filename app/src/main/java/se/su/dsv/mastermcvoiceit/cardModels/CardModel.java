package se.su.dsv.mastermcvoiceit.cardModels;

/**
 * Created by felix on 2017-11-28.
 */

public abstract class CardModel {
    private CardInfoType itemViewType;

    public CardModel(CardInfoType itemViewType) {
        this.itemViewType = itemViewType;
    }

    public CardInfoType getItemViewType() {
        return itemViewType;
    }
}
