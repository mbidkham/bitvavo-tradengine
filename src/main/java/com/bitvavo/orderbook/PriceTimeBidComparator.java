package com.bitvavo.orderbook;

import com.bitvavo.orderbook.entity.Order;

import java.util.Comparator;

/**
 * Comparator for sorting Buy Orders descending based on price and oldest to the newest items.
 */
public class PriceTimeBidComparator implements Comparator<Order> {
    @Override
    public int compare(Order order, Order t1) {
        int diff;
        diff = t1.getPrice().compareTo(order.getPrice());
        if (diff == 0)
            diff = order.getTimestamp().compareTo(t1.getTimestamp());
        return diff;
    }
}
