package com.bitvavo.orderbook.entity;

import com.bitvavo.orderbook.PriceTimeAskComparator;
import com.bitvavo.orderbook.PriceTimeBidComparator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * OrderBook is the list of sell and buy orders exist in the exchange.
 *
 * @example :
 * bidTree            | askTree
 * 600         100    | 91     1100
 * 1100        98     | 93     500
 * | 95     1000
 */
public class OrderBook {

    /**
     * List of Buy Orders Sorted based on descending price and oldest to newest created date.
     */
    private static SortedSet<Order> bidTree = new TreeSet<>(new PriceTimeBidComparator());
    /**
     * List of Sell Orders Sorted based on ascending price and oldest to newest created date.
     */
    private static SortedSet<Order> askTree = new TreeSet<>(new PriceTimeAskComparator());


    /**
     * Add new Items to fill OrderBook.
     *
     * @param order
     */
    public static void insertToOrderBook(Order order) {
        switch (order.getSide()) {
            case ASK:
                askTree.add(order);
                break;
            case BID:
                bidTree.add(order);
                break;
            default:
                throw new IllegalArgumentException("Incompatible order type");
        }
    }

    public static void updateOrderBook(Order order) {
        if (order.getSide().equals(OrderAction.ASK)) {
            Order updateOrder = askTree
                .stream()
                .filter(order1 -> order.getOrderId().equals(order1.getOrderId()))
                .findFirst()
                .orElseThrow();
            updateOrder.setVolume(order.getVolume());
        } else if (order.getSide().equals(OrderAction.BID)) {
            Order updateOrder = bidTree
                .stream()
                .filter(order1 -> order.getOrderId().equals(order1.getOrderId()))
                .findFirst()
                .orElseThrow();
            updateOrder.setVolume(order.getVolume());
        }
    }

    /**
     * print BidAsk based on the assignment ouput need.
     */

    public static void printOrderBook() {
        System.out.println("Buyers      Sellers");
        Iterator<Order> bidTreeIterate = bidTree.iterator();
        Iterator<Order> askTreeIterate = askTree.iterator();
        while (bidTreeIterate.hasNext() || askTreeIterate.hasNext()) {
            String buyLine = String.format("%-18s", "");
            String sellLine = String.format("%-18s", "");

            if (bidTreeIterate.hasNext()) {
                Order bidOrder = bidTreeIterate.next();
                String volume = String.format("%-11s", bidOrder.getVolume());
                String price = String.format("%-6s", bidOrder.getPrice());
                buyLine = (volume + " " + price);
            }
            if (askTreeIterate.hasNext()) {
                Order askOrder = askTreeIterate.next();
                String volume = String.format("%-11s", askOrder.getVolume());
                String price = String.format("%-6s", askOrder.getPrice());
                sellLine = (price + " " + volume);
            }
            System.out.println(buyLine + " | " + sellLine);
        }
    }

    public static void removeMatchedOrders(List<Order> matchedOrders){
        askTree
            .removeAll(matchedOrders.stream().filter(order -> order.getSide().equals(OrderAction.ASK))
                .collect(Collectors.toSet()));
       bidTree
            .removeAll(matchedOrders.stream().filter(order -> order.getSide().equals(OrderAction.BID))
                .collect(Collectors.toSet()));
    }

    public static SortedSet<Order> getBidTree() {
        return bidTree;
    }

    public static void setBidTree(SortedSet<Order> bidTree) {
        OrderBook.bidTree = bidTree;
    }

    public static SortedSet<Order> getAskTree() {
        return askTree;
    }

    public static void setAskTree(SortedSet<Order> askTree) {
        OrderBook.askTree = askTree;
    }
}
