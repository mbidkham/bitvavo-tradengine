package com.bitvavo.trading;


import com.bitvavo.orderbook.entity.Order;
import com.bitvavo.orderbook.entity.OrderBook;
import com.bitvavo.trading.entity.Trade;


/**
 * Trading part of business logic.
 */
public class TradeService {

    /**
     * UPDATE OrderBook items based on traded order.
     * @param moreByVolumeOrder the order which has not completed yet.
     * @param lessByVolumeOrder the order which filled all volume.
     */
    public void updateOrderBookOnTrade(Order moreByVolumeOrder, Order lessByVolumeOrder){
        int remainedVolume = moreByVolumeOrder.getVolume() - lessByVolumeOrder.getVolume();
        moreByVolumeOrder.setVolume(remainedVolume);
        OrderBook.updateOrderBook(moreByVolumeOrder);
    }

    /**
     *
     * @param takerId taker is the new order
     * @param makerId maker is the matched order in OrderBook by the new order
     * @param price maker price
     * @param volume trade volume
     */
    public Trade onTradeHappen(String takerId, String makerId, int price, int volume){
        Trade trade = Trade.builder()
            .takerId(takerId)
            .makerId(makerId)
            .price(price)
            .volume(volume)
            .build();
        System.out.println(trade.toString());
        return trade;
    }
}
