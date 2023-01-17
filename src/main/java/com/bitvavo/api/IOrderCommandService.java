package com.bitvavo.api;

import com.bitvavo.orderbook.entity.Order;

/**
 * Business logic of sending , cancelling or editing order by user.
 * It can be implemented based on business logic : Limit Order, Stop Order, MarketMaker Order, etc.
 */
public interface IOrderCommandService {
    /**
     *
     * @param order : based on user input new order
     * It should print [traded data] if exist, plus [OrderBook] after execution.
     */
    void sendOrder(Order order);
}
