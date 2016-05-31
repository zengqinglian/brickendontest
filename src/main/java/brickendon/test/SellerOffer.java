package brickendon.test;

public class SellerOffer
{
    private final int itemId;
    private final int userId;
    private int quantity;
    private final int unitPrice; // pence

    public SellerOffer( int itemId, int userId, int quantity, int unitPrice ) {
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

    public void setQuantity( int quantity ) {
        this.quantity = quantity;
    }

}
