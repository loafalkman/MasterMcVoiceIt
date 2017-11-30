package se.su.dsv.mastermcvoiceit.cardModels;

/**
 * Created by felix on 2017-11-28.
 */

public abstract class CardModel {
    private CardModelType itemViewType;

    public CardModel(CardModelType itemViewType) {
        this.itemViewType = itemViewType;
    }

    public CardModelType getItemViewType() {
        return itemViewType;
    }
}
