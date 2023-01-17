package com.bitvavo;

import com.bitvavo.api.OrderCommandServiceImpl;
import com.bitvavo.orderbook.entity.Order;
import com.bitvavo.orderbook.entity.OrderAction;
import com.bitvavo.orderbook.entity.OrderBook;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TradingEngineApp {
    public static void main(String[] args) {

        OrderCommandServiceImpl orderCommandService = new OrderCommandServiceImpl();
        Scanner scanner = new Scanner(System.in);
        List<Order> orders = new ArrayList<>();
        String line;
        while (!(line = scanner.nextLine()).equals("")) {
            String[] items = line.split(",");
            Order order = Order
                .builder()
                .orderId(items[0])
                .side(OrderAction.actionFactory(items[1]))
                .price(Integer.valueOf(items[2]))
                .volume(Integer.parseInt(items[3]))
                .timestamp(Instant.now())
                .build();
            orders.add(order);
        }
        orders.forEach(orderCommandService::sendOrder);
        OrderBook.printOrderBook();
    }
}
