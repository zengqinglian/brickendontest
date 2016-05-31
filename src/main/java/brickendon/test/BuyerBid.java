package brickendon.test;

public class BuyerBid
{
    private final int itemId;
    private final int userId;
    private final int quantity;
    private final int unitPrice; // pence to avoid float point issue

    public BuyerBid( int itemId, int userId, int quantity, int unitPrice ) {
        this.itemId = itemId;
        this.userId = userId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getItemId() {
        return itemId;
    }

    public int getUserId() {
        return userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getUnitPrice() {
        return unitPrice;
    }
}
