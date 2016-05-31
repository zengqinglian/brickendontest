package brickendon.test;

import java.util.List;

/**
 * @author qinglian.zeng
 *
 */
public interface MarketPlace
{

    /**
     * @param bid
     */
    public void addBuyerBid( BuyerBid bid );

    /**
     * @param offer
     */
    public void addSellerOffer( SellerOffer offer );

    /**
     * @param buyerId
     * @return
     */
    public List<BuyerBid> getBuyerBidsFromBuyerID( int buyerId );

    /**
     * @param sellerId
     * @return
     */
    public List<SellerOffer> getSellerOfferFromSellerID( int sellerId );

    /**
     * @param buyerId
     * @return
     */
    public List<Order> getOrdersFromBuyerID( int buyerId );

    /**
     * @param sellerId
     * @return
     */
    public List<Order> getOrdersFromSellerID( int sellerId );

    /**
     * @param itemId
     * @return
     */
    public int highestBidPriceForItem( int itemId );

    /**
     * @param itemId
     * @return
     */
    public int lowestOfferPriceForItem( int itemId );
}
