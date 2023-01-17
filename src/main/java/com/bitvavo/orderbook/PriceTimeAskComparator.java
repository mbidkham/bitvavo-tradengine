package com.bitvavo.orderbook;

import com.bitvavo.orderbook.entity.Order;

import java.util.Comparator;

/**
 * Comparator for sorting Sell Orders ascending based on price and oldest to the newest items.
 */
public class PriceTimeAskComparator implements Comparator<Order> {
    @Override
    public int compare(Order order, Order t1) {
        int diff;
        diff = order.getPrice().compareTo(t1.getPrice());
        if (diff == 0)
            diff = order.getTimestamp().compareTo(t1.getTimestamp());
        return diff;
    }
}
