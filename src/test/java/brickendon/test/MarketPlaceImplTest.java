package brickendon.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MarketPlaceImplTest
{
    private MarketPlaceImpl marketPlace = new MarketPlaceImpl();
    private BuyerBid bid1;
    private BuyerBid bid2;
    private SellerOffer offer1;
    private SellerOffer offer2;

    @Before
    public void setUp() {
        bid1 = new BuyerBid( 1, 1, 1, 1 );
        bid2 = new BuyerBid( 2, 2, 2, 2 );
        offer1 = new SellerOffer( 3, 3, 3, 3 );
        offer2 = new SellerOffer( 4, 4, 4, 4 );
    }

    @Test(expected = RuntimeException.class)
    public void testAddBuyerBidNull() {
        marketPlace.addBuyerBid( null );
    }

    @Test
    public void testAddBuyerBidNoOrderCreated() {
        marketPlace.addBuyerBid( bid1 ); // no offer
        Assert.assertEquals( 0, marketPlace.getAllOrders().size() );

        // no matched offer
        marketPlace.addSellerOffer( offer1 );
        marketPlace.addBuyerBid( bid2 );
        Assert.assertEquals( 0, marketPlace.getAllOrders().size() );

    }

    @Test(expected = RuntimeException.class)
    public void testAddSellerOfferNull() {
        marketPlace.addSellerOffer( null );
    }

    @Test
    public void testAddSellerOfferNoOrderCreated() {
        marketPlace.addSellerOffer( offer1 );
        Assert.assertEquals( 0, marketPlace.getAllOrders().size() ); // no bid

        // no match bid
        marketPlace.addBuyerBid( bid1 );
        marketPlace.addSellerOffer( offer2 );
        Assert.assertEquals( 0, marketPlace.getAllOrders().size() );

    }

    @Test
    public void testAddBuyerBidOrderCreated() {
        // first matched offer - offer removed
        marketPlace.addSellerOffer( offer1 );
        marketPlace.addSellerOffer( offer2 );

        SellerOffer offer3 = new SellerOffer( 3, 5, 3, 4 );
        marketPlace.addSellerOffer( offer3 );

        BuyerBid bid3 = new BuyerBid( 3, 6, 3, 4 );
        marketPlace.addBuyerBid( bid3 );

        Assert.assertEquals( 1, marketPlace.getAllOrders().size() );
        Assert.assertEquals( 2, marketPlace.getAllSellerOffers().size() );
        Assert.assertEquals( 0, marketPlace.getAllBuyerBids().size() );
        Order order1 = marketPlace.getAllOrders().get( 0 );
        Assert.assertEquals( 6, order1.getBuyerId() );
        Assert.assertEquals( 3, order1.getSellerId() );
        Assert.assertEquals( 3, order1.getQuantity() );
        Assert.assertEquals( 3, order1.getUnitPrice() );

        // first matched offer - offer quantity changes
        BuyerBid bid4 = new BuyerBid( 3, 6, 1, 5 );
        marketPlace.addBuyerBid( bid4 );
        Assert.assertEquals( 2, marketPlace.getAllOrders().size() );
        Assert.assertEquals( 2, marketPlace.getAllSellerOffers().size() );
        Assert.assertEquals( 0, marketPlace.getAllBuyerBids().size() );

        Order order2 = marketPlace.getAllOrders().get( 1 );
        Assert.assertEquals( 6, order2.getBuyerId() );
        Assert.assertEquals( 5, order2.getSellerId() );
        Assert.assertEquals( 1, order2.getQuantity() );
        Assert.assertEquals( 4, order2.getUnitPrice() );
    }

    @Test
    public void testAddSellerOfferOrderCreated() {
        marketPlace.addBuyerBid( bid1 );
        marketPlace.addBuyerBid( bid2 );
        BuyerBid bid3 = new BuyerBid( 1, 6, 3, 4 );
        marketPlace.addBuyerBid( bid3 );

        SellerOffer offer3 = new SellerOffer( 1, 3, 1, 1 );
        // order created, find first bid, offer match, no new offer
        marketPlace.addSellerOffer( offer3 );
        Assert.assertEquals( 1, marketPlace.getAllOrders().size() );
        Assert.assertEquals( 0, marketPlace.getAllSellerOffers().size() );
        Assert.assertEquals( 2, marketPlace.getAllBuyerBids().size() );
        Order order1 = marketPlace.getAllOrders().get( 0 );
        Assert.assertEquals( 1, order1.getBuyerId() );
        Assert.assertEquals( 3, order1.getSellerId() );
        Assert.assertEquals( 1, order1.getQuantity() );
        Assert.assertEquals( 1, order1.getUnitPrice() );

        SellerOffer offer4 = new SellerOffer( 1, 5, 5, 2 );
        marketPlace.addSellerOffer( offer4 );

        Assert.assertEquals( 2, marketPlace.getAllOrders().size() );
        Assert.assertEquals( 1, marketPlace.getAllSellerOffers().size() );
        Assert.assertEquals( 1, marketPlace.getAllBuyerBids().size() );

        Order order2 = marketPlace.getAllOrders().get( 1 );
        Assert.assertEquals( 6, order2.getBuyerId() );
        Assert.assertEquals( 5, order2.getSellerId() );
        Assert.assertEquals( 3, order2.getQuantity() );
        Assert.assertEquals( 2, order2.getUnitPrice() );

    }

    @Test
    public void testGetBuyerBidsFromBuyerID() {
        marketPlace.addBuyerBid( bid1 );
        marketPlace.addBuyerBid( bid2 );
        BuyerBid bid3 = new BuyerBid( 3, 1, 3, 4 );
        marketPlace.addBuyerBid( bid3 );

        Assert.assertEquals( 2, marketPlace.getBuyerBidsFromBuyerID( 1 ).size() );

    }

    @Test
    public void testGetSellerOfferFromSellerID() {
        marketPlace.addSellerOffer( offer1 );
        marketPlace.addSellerOffer( offer2 );

        SellerOffer offer3 = new SellerOffer( 5, 3, 3, 4 );
        marketPlace.addSellerOffer( offer3 );

        Assert.assertEquals( 2, marketPlace.getSellerOfferFromSellerID( 3 ).size() );
    }

    @Test
    public void testGetOrdersFromBuyerIDOrSellerID() {
        marketPlace.addBuyerBid( bid1 );
        marketPlace.addBuyerBid( bid2 );
        BuyerBid bid3 = new BuyerBid( 1, 6, 3, 4 );
        marketPlace.addBuyerBid( bid3 );
        SellerOffer offer3 = new SellerOffer( 1, 3, 1, 1 );
        marketPlace.addSellerOffer( offer3 );
        SellerOffer offer4 = new SellerOffer( 1, 5, 5, 2 );
        marketPlace.addSellerOffer( offer4 );

        Assert.assertEquals( 1, marketPlace.getOrdersFromBuyerID( 6 ).size() );
        Assert.assertEquals( 1, marketPlace.getOrdersFromBuyerID( 1 ).size() );
        Assert.assertEquals( 0, marketPlace.getOrdersFromBuyerID( 5 ).size() );

        Assert.assertEquals( 1, marketPlace.getOrdersFromSellerID( 3 ).size() );
        Assert.assertEquals( 1, marketPlace.getOrdersFromSellerID( 5 ).size() );
        Assert.assertEquals( 0, marketPlace.getOrdersFromSellerID( 2 ).size() );

        Assert.assertEquals( 2, marketPlace.getAllOrders().size() );

    }

    @Test
    public void testLowestOfferPriceForItem() {
        marketPlace.addSellerOffer( offer1 );
        marketPlace.addSellerOffer( offer2 );
        SellerOffer offer3 = new SellerOffer( 1, 3, 1, 1 );
        marketPlace.addSellerOffer( offer3 );
        Assert.assertEquals( 1, marketPlace.lowestOfferPriceForItem( 1 ) );

    }

    public void testHighestBidPriceForItem() {
        marketPlace.addBuyerBid( bid1 );
        marketPlace.addBuyerBid( bid2 );
        BuyerBid bid3 = new BuyerBid( 1, 6, 3, 4 );
        marketPlace.addBuyerBid( bid3 );

        Assert.assertEquals( 4, marketPlace.highestBidPriceForItem( 1 ) );
    }
}
