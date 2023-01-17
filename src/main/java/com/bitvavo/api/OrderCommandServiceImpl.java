package com.bitvavo.api;

import com.bitvavo.orderbook.entity.Order;
import com.bitvavo.orderbook.entity.OrderBook;
import com.bitvavo.processors.MatchingEngine;

/**
 * Market Order business Implementation of IOrderCommandService.
 */
public class OrderCommandServiceImpl implements IOrderCommandService{
    private final MatchingEngine matchingEngine;

    public OrderCommandServiceImpl() {
        this.matchingEngine = new MatchingEngine();
    }

    /**
     *
     * @param order based on user input new order
     * Send new Order,
     *             if it can be matched immediately with current orderbook: new Trade will happen.
     *             Then print trades + new updated orderbook.
     *              else: just updated orderbook will be printed.
     */
    public void sendOrder(Order order) {
        switch (order.getSide()){
            case BID:
                if (OrderBook.getAskTree().isEmpty()){
                    OrderBook.insertToOrderBook(order);
                }
                else {
                    matchingEngine.matchBuyTakerOrder(order);
                }
                break;
            case ASK:
                if (OrderBook.getBidTree().isEmpty()){
                    OrderBook.insertToOrderBook(order);
                }
                else {
                    matchingEngine.matchSellTakerOrder(order);
                }
                break;
            default:
                throw new IllegalArgumentException("Incompatible order type");
        }
//       OrderBook.printOrderBook();
    }
}
