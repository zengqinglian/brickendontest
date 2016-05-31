package brickendon.test;

/**
 * @author qinglian.zeng
 *
 */
public class Order
{
    private final int buyerId;
    private final int sellerId;
    private final int itemId;
    private final int quantity;
    private final int unitPrice;

    public Order( int buyerId, int sellerId, int itemId, int unitPrice, int quantity ) {
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.itemId = itemId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getUnitPrice() {
        return unitPrice;
    }
}
