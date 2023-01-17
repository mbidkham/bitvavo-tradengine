package com.bitvavo.processors;

import com.bitvavo.orderbook.entity.Order;
import com.bitvavo.orderbook.entity.OrderBook;
import com.bitvavo.trading.TradeService;
import java.util.ArrayList;

/**
 * MatchingEngine is a part of main processes in sending orders.
 * Main responsibility is to check if new order can be matched with current OrderBook or not.
 */
public class MatchingEngine {

    private final TradeService tradeService;

    public MatchingEngine() {
        this.tradeService = new TradeService();
    }

    /**
     * @param takerOrder is the new 'sell' order created by user to check if is matched by Orderbook items or not.
     *                   matching rule: the price of sell order must be equal or less than Bid items in Orderbook.
     *                   taker = new order
     *                   maker = orderbook item which matches this order
     */
    public void matchSellTakerOrder(Order takerOrder) {
        ArrayList<Order> toBeDeletedOrders = new ArrayList<>();
        OrderBook.insertToOrderBook(takerOrder);
        for (Order candidateOrder : OrderBook.getBidTree()) {
            if (takerOrder.getPrice() > candidateOrder.getPrice()) {
                return;
            }
            if (candidateOrder.getVolume() < takerOrder.getVolume()) {
                toBeDeletedOrders.add(candidateOrder);
                tradeService.updateOrderBookOnTrade(takerOrder, candidateOrder);
                tradeService.onTradeHappen(takerOrder.getOrderId(), candidateOrder.getOrderId(),
                    candidateOrder.getPrice(), candidateOrder.getVolume());

            } else if (candidateOrder.getVolume() > takerOrder.getVolume()) {
                toBeDeletedOrders.add(takerOrder);
                tradeService.updateOrderBookOnTrade(candidateOrder, takerOrder);
                tradeService.onTradeHappen(takerOrder.getOrderId(), candidateOrder.getOrderId(),
                    candidateOrder.getPrice(), takerOrder.getVolume());
                break;

            } else {
                toBeDeletedOrders.add(candidateOrder);
                toBeDeletedOrders.add(takerOrder);
                tradeService.onTradeHappen(takerOrder.getOrderId(), candidateOrder.getOrderId(),
                    candidateOrder.getPrice(), takerOrder.getVolume());
                break;
            }
        }
        OrderBook.removeMatchedOrders(toBeDeletedOrders);
    }

    /**
     * @param takerOrder is the new 'buy' order created by user to check if is matched by Orderbook items or not.
     *                   matching rule: the price of buy order must be equal or greater than Ask items in Orderbook.
     *                   new order = taker
     *                   orderbook item which matches this order = maker
     */
    public void matchBuyTakerOrder(Order takerOrder) {
        ArrayList<Order> toBeDeletedOrders = new ArrayList<>();
        OrderBook.insertToOrderBook(takerOrder);
        for (Order candidateOrder : OrderBook.getAskTree()) {
            if (takerOrder.getPrice() < candidateOrder.getPrice()) {
                return;
            }
            if (candidateOrder.getVolume() < takerOrder.getVolume()) {
                toBeDeletedOrders.add(candidateOrder);
                tradeService.updateOrderBookOnTrade(takerOrder, candidateOrder);
                tradeService.onTradeHappen(takerOrder.getOrderId(), candidateOrder.getOrderId(),
                    candidateOrder.getPrice(), candidateOrder.getVolume());
            } else if (candidateOrder.getVolume() > takerOrder.getVolume()) {
                toBeDeletedOrders.add(takerOrder);
                tradeService.updateOrderBookOnTrade(candidateOrder, takerOrder);
                tradeService.onTradeHappen(takerOrder.getOrderId(), candidateOrder.getOrderId(),
                    candidateOrder.getPrice(), takerOrder.getVolume());
                break;
            } else {
                toBeDeletedOrders.add(candidateOrder);
                toBeDeletedOrders.add(takerOrder);
                tradeService.onTradeHappen(takerOrder.getOrderId(), candidateOrder.getOrderId(),
                    candidateOrder.getPrice(), takerOrder.getVolume());
                break;
            }
        }

        OrderBook.removeMatchedOrders(toBeDeletedOrders);

    }
}
