package brickendon.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author qinglian.zeng
 * 
 */
public class MarketPlaceImpl implements MarketPlace
{
    private List<Order> orders = new ArrayList<>();
    private List<BuyerBid> buyerBids = new LinkedList<>(); // linkList no shift of element after removing
    private List<SellerOffer> sellerOffers = new LinkedList<>();

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    /*
     * (non-Javadoc)
     * 
     * @see brickendon.test.MarketPlace#addBuyerBid(brickendon.test.BuyerBid)
     */
    public void addBuyerBid( BuyerBid bid ) {
        if( bid == null ) {
            throw new RuntimeException( "invalid null object" );
        }

        // guarded by writeLock
        writeLock.lock();
        try {
            if( !checkOffers( bid ) ) {

                buyerBids.add( bid );

            }
        } finally {
            writeLock.unlock();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see brickendon.test.MarketPlace#addSellerOffer(brickendon.test.SellerOffer)
     */
    public void addSellerOffer( SellerOffer offer ) {
        if( offer == null ) {
            throw new RuntimeException( "invalid null object" );
        }

        writeLock.lock();
        try {
            if( !checkBid( offer ) ) {

                sellerOffers.add( offer );

            }
        } finally {
            writeLock.unlock();
        }
    }

    private boolean checkOffers( BuyerBid bid ) {

        Iterator<SellerOffer> ita = sellerOffers.iterator();

        while( ita.hasNext() ) {
            SellerOffer offer = ita.next();
            if( offer.getItemId() == bid.getItemId() && offer.getQuantity() >= bid.getQuantity()
                    && offer.getUnitPrice() <= bid.getUnitPrice() ) {
                processBid( bid, offer, ita );
                return true;
            }
        }

        return false;
    }

    private boolean checkBid( SellerOffer offer ) {

        Iterator<BuyerBid> ita = buyerBids.iterator();

        while( ita.hasNext() ) {
            BuyerBid bid = ita.next();
            if( offer.getItemId() == bid.getItemId() && offer.getQuantity() >= bid.getQuantity()
                    && offer.getUnitPrice() <= bid.getUnitPrice() ) {

                processOffer( bid, offer, ita );
                if( offer.getQuantity() == 0 ) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }

    private void processOffer( BuyerBid bid, SellerOffer offer, Iterator<BuyerBid> ita ) {

        createOrder( bid, offer );
        ita.remove(); // linklist O(1)
        offer.setQuantity( offer.getQuantity() - bid.getQuantity() );

    }

    private void processBid( BuyerBid bid, SellerOffer offer, Iterator<SellerOffer> ita ) {

        createOrder( bid, offer );
        if( bid.getQuantity() == offer.getQuantity() ) {
            ita.remove();// linklist O(1)
        } else {
            offer.setQuantity( offer.getQuantity() - bid.getQuantity() );
        }

    }

    private void createOrder( BuyerBid bid, SellerOffer offer ) {
        Order order = new Order( bid.getUserId(), offer.getUserId(), bid.getItemId(), offer.getUnitPrice(), bid.getQuantity() );
        orders.add( order );
    }

    /*
     * (non-Javadoc)
     * 
     * @see brickendon.test.MarketPlace#getBuyerBidsFromBuyerID(int)
     */
    public List<BuyerBid> getBuyerBidsFromBuyerID( int buyerId ) {
        List<BuyerBid> list = new ArrayList<>();
        readLock.lock();
        try {
            for( BuyerBid bid : buyerBids ) {
                if( bid.getUserId() == buyerId ) {
                    list.add( bid );
                }
            }
        } finally {
            readLock.unlock();
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see brickendon.test.MarketPlace#getSellerOfferFromSellerID(int)
     */
    public List<SellerOffer> getSellerOfferFromSellerID( int sellerId ) {
        List<SellerOffer> list = new ArrayList<>();
        readLock.lock();
        try {
            for( SellerOffer offer : sellerOffers ) {
                if( offer.getUserId() == sellerId ) {
                    list.add( offer );
                }
            }
        } finally {
            readLock.unlock();
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see brickendon.test.MarketPlace#getOrdersFromBuyerID(int)
     */
    public List<Order> getOrdersFromBuyerID( int buyerId ) {
        List<Order> list = new ArrayList<>();
        readLock.lock();
        try {
            for( Order order : orders ) {
                if( order.getBuyerId() == buyerId ) {
                    list.add( order );
                }
            }
        } finally {
            readLock.unlock();
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see brickendon.test.MarketPlace#getOrdersFromSellerID(int)
     */
    public List<Order> getOrdersFromSellerID( int sellerId ) {
        List<Order> list = new ArrayList<>();
        readLock.lock();
        try {
            for( Order order : orders ) {
                if( order.getSellerId() == sellerId ) {
                    list.add( order );
                }
            }
        } finally {
            readLock.unlock();
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see brickendon.test.MarketPlace#highestBidPriceForItem(int)
     */
    public int highestBidPriceForItem( int itemId ) {
        int max = Integer.MAX_VALUE;

        readLock.lock();
        try {
            for( BuyerBid bid : buyerBids ) {
                if( bid.getItemId() == itemId && max < bid.getUnitPrice() ) {
                    max = bid.getUnitPrice();
                }
            }
        } finally {
            readLock.unlock();
        }

        return max;
    }

    /*
     * (non-Javadoc)
     * 
     * @see brickendon.test.MarketPlace#lowestOfferPriceForItem(int)
     */
    public int lowestOfferPriceForItem( int itemId ) {
        int min = Integer.MAX_VALUE;

        readLock.lock();
        try {
            for( SellerOffer offer : sellerOffers ) {
                if( offer.getItemId() == itemId && min > offer.getUnitPrice() ) {
                    min = offer.getUnitPrice();
                }
            }
        } finally {
            readLock.unlock();
        }
        return min;
    }

    protected List<Order> getAllOrders() {
        readLock.lock();
        try {
            return orders;
        } finally {
            readLock.unlock();
        }
    }

    protected List<BuyerBid> getAllBuyerBids() {
        readLock.lock();
        try {
            return buyerBids;
        } finally {
            readLock.unlock();
        }
    }

    protected List<SellerOffer> getAllSellerOffers() {
        readLock.lock();
        try {
            return sellerOffers;
        } finally {
            readLock.unlock();
        }
    }
}
